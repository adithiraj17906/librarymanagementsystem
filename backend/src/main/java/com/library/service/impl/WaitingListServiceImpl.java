package com.library.service.impl;

import com.library.dto.WaitingListEntryDTO;
import com.library.entity.*;
import com.library.repository.*;
import com.library.service.WaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WaitingListServiceImpl implements WaitingListService {

    private final WaitingListEntryRepository waitingListEntryRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public WaitingListServiceImpl(WaitingListEntryRepository waitingListEntryRepository,
                                  BookRepository bookRepository,
                                  UserRepository userRepository,
                                  BorrowRecordRepository borrowRecordRepository,
                                  NotificationRepository notificationRepository) {
        this.waitingListEntryRepository = waitingListEntryRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public WaitingListEntryDTO joinWaitingList(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id " + bookId));

        if (book.getAvailableCopies() > 0) {
            throw new IllegalArgumentException("Book has available copies. Please borrow it directly.");
        }

        // Check if user already has an active borrow record for this book
        List<BorrowRecord> borrows = borrowRecordRepository.findByUserIdAndStatus(userId, BorrowStatus.BORROWED);
        List<BorrowRecord> overdueBorrows = borrowRecordRepository.findByUserIdAndStatus(userId, BorrowStatus.OVERDUE);
        boolean alreadyBorrowed = borrows.stream().anyMatch(b -> b.getBook().getId().equals(bookId)) ||
                                  overdueBorrows.stream().anyMatch(b -> b.getBook().getId().equals(bookId));
        if (alreadyBorrowed) {
            throw new IllegalArgumentException("You have already borrowed this book and have not returned it yet");
        }

        // Check if user already in active waiting list (WAITING or NOTIFIED)
        List<WaitingListEntry> activeEntries = waitingListEntryRepository
                .findByUserIdAndBookIdAndStatusIn(userId, bookId, Arrays.asList(WaitingListStatus.WAITING, WaitingListStatus.NOTIFIED));
        if (!activeEntries.isEmpty()) {
            throw new IllegalArgumentException("You are already on the active waiting list for this book");
        }

        // Find max queue position for this book
        List<WaitingListEntry> bookWaitlist = waitingListEntryRepository
                .findByBookIdAndStatusOrderByQueuePositionAsc(bookId, WaitingListStatus.WAITING);
        
        int nextPosition = 1;
        if (!bookWaitlist.isEmpty()) {
            nextPosition = bookWaitlist.get(bookWaitlist.size() - 1).getQueuePosition() + 1;
        }

        WaitingListEntry entry = WaitingListEntry.builder()
                .user(user)
                .book(book)
                .queuePosition(nextPosition)
                .status(WaitingListStatus.WAITING)
                .build();

        WaitingListEntry saved = waitingListEntryRepository.save(entry);
        return WaitingListEntryDTO.fromEntity(saved);
    }

    @Override
    public void claimBook(Long entryId) {
        WaitingListEntry entry = waitingListEntryRepository.findById(entryId)
                .orElseThrow(() -> new IllegalArgumentException("Waiting list entry not found with id " + entryId));

        if (entry.getStatus() != WaitingListStatus.NOTIFIED) {
            throw new IllegalArgumentException("This waiting list entry is not in NOTIFIED status");
        }

        if (entry.getClaimDeadline() != null && LocalDateTime.now().isAfter(entry.getClaimDeadline())) {
            // Already expired, but not yet processed by scheduler. Mark as expired now.
            entry.setStatus(WaitingListStatus.EXPIRED);
            waitingListEntryRepository.save(entry);
            
            // Release copy/notify next user
            processExpiredEntry(entry);
            throw new IllegalArgumentException("Your 24-hour claim window has expired");
        }

        // Complete the claim
        entry.setStatus(WaitingListStatus.CLAIMED);
        waitingListEntryRepository.save(entry);

        // Create Borrow Record (available copies already decremented on notify)
        BorrowRecord record = BorrowRecord.builder()
                .user(entry.getUser())
                .book(entry.getBook())
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BorrowStatus.BORROWED)
                .fineAmount(0.0)
                .build();
        borrowRecordRepository.save(record);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WaitingListEntryDTO> getWaitingListForBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new IllegalArgumentException("Book not found with id " + bookId);
        }
        return waitingListEntryRepository.findByBookIdAndStatusOrderByQueuePositionAsc(bookId, WaitingListStatus.WAITING).stream()
                .map(WaitingListEntryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WaitingListEntryDTO> getUserWaitingList(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id " + userId);
        }
        // Return both WAITING and NOTIFIED entries for user
        return waitingListEntryRepository.findByUserIdAndStatusIn(userId, 
                Arrays.asList(WaitingListStatus.WAITING, WaitingListStatus.NOTIFIED)).stream()
                .map(WaitingListEntryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void processExpiredNotifications() {
        List<WaitingListEntry> expiredEntries = waitingListEntryRepository
                .findByStatusAndClaimDeadlineBefore(WaitingListStatus.NOTIFIED, LocalDateTime.now());

        for (WaitingListEntry entry : expiredEntries) {
            entry.setStatus(WaitingListStatus.EXPIRED);
            waitingListEntryRepository.save(entry);
            processExpiredEntry(entry);
        }
    }

    private void processExpiredEntry(WaitingListEntry entry) {
        Book book = entry.getBook();
        List<WaitingListEntry> waitingList = waitingListEntryRepository
                .findByBookIdAndStatusOrderByQueuePositionAsc(book.getId(), WaitingListStatus.WAITING);

        if (!waitingList.isEmpty()) {
            // Notify next user
            WaitingListEntry nextEntry = waitingList.get(0);
            nextEntry.setStatus(WaitingListStatus.NOTIFIED);
            nextEntry.setNotifiedAt(LocalDateTime.now());
            nextEntry.setClaimDeadline(LocalDateTime.now().plusHours(24));
            waitingListEntryRepository.save(nextEntry);

            // Create notification for next user
            Notification nextNotification = Notification.builder()
                    .user(nextEntry.getUser())
                    .message(String.format("The book '%s' is now available! Please claim it within 24 hours.", book.getTitle()))
                    .type(NotificationType.BOOK_AVAILABLE)
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(nextNotification);
        } else {
            // No one left in queue, release copy back to general availability
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        }
    }
}
