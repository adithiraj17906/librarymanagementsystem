package com.library.service.impl;

import com.library.dto.BorrowRecordDTO;
import com.library.entity.*;
import com.library.repository.*;
import com.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final WaitingListEntryRepository waitingListEntryRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public BorrowServiceImpl(BorrowRecordRepository borrowRecordRepository,
                             BookRepository bookRepository,
                             UserRepository userRepository,
                             WaitingListEntryRepository waitingListEntryRepository,
                             NotificationRepository notificationRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.waitingListEntryRepository = waitingListEntryRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public BorrowRecordDTO borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + userId));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id " + bookId));

        // Check if user already has an active borrow record for this book
        List<BorrowRecord> existingBorrows = borrowRecordRepository.findByUserIdAndStatus(userId, BorrowStatus.BORROWED);
        List<BorrowRecord> existingOverdue = borrowRecordRepository.findByUserIdAndStatus(userId, BorrowStatus.OVERDUE);
        
        boolean alreadyBorrowed = existingBorrows.stream().anyMatch(r -> r.getBook().getId().equals(bookId)) ||
                                  existingOverdue.stream().anyMatch(r -> r.getBook().getId().equals(bookId));
        if (alreadyBorrowed) {
            throw new IllegalArgumentException("You have already borrowed this book and not returned it yet");
        }

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalArgumentException("No copies available, please join the waiting list");
        }

        // Decrement available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Create borrow record
        BorrowRecord record = BorrowRecord.builder()
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BorrowStatus.BORROWED)
                .fineAmount(0.0)
                .build();

        BorrowRecord saved = borrowRecordRepository.save(record);
        return BorrowRecordDTO.fromEntity(saved);
    }

    @Override
    public BorrowRecordDTO returnBook(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new IllegalArgumentException("Borrow record not found with id " + borrowRecordId));

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new IllegalArgumentException("This book has already been returned");
        }

        LocalDate today = LocalDate.now();
        record.setReturnDate(today);
        record.setStatus(BorrowStatus.RETURNED);

        // Calculate fine if overdue (₹5/day)
        if (today.isAfter(record.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(record.getDueDate(), today);
            double fine = daysOverdue * 5.0;
            record.setFineAmount(fine);

            // Create notification for fine
            Notification fineNotification = Notification.builder()
                    .user(record.getUser())
                    .message(String.format("Fine update: You have been charged a fine of ₹%.2f for returning '%s' %d days late.", 
                            fine, record.getBook().getTitle(), daysOverdue))
                    .type(NotificationType.FINE_UPDATE)
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(fineNotification);
        }

        // Return book: Increment available copies first
        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        // Check if there are users waiting for this book
        List<WaitingListEntry> waitingList = waitingListEntryRepository
                .findByBookIdAndStatusOrderByQueuePositionAsc(book.getId(), WaitingListStatus.WAITING);

        if (!waitingList.isEmpty()) {
            // Notify the first user in the queue
            WaitingListEntry firstEntry = waitingList.get(0);
            firstEntry.setStatus(WaitingListStatus.NOTIFIED);
            firstEntry.setNotifiedAt(LocalDateTime.now());
            firstEntry.setClaimDeadline(LocalDateTime.now().plusHours(24));
            waitingListEntryRepository.save(firstEntry);

            // Decrement copies again (it is reserved for the notified user)
            book.setAvailableCopies(book.getAvailableCopies() - 1);

            // Create Notification
            Notification bookAvailableNotification = Notification.builder()
                    .user(firstEntry.getUser())
                    .message(String.format("The book '%s' is now available! Please claim it within 24 hours.", book.getTitle()))
                    .type(NotificationType.BOOK_AVAILABLE)
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            notificationRepository.save(bookAvailableNotification);
        }

        bookRepository.save(book);
        BorrowRecord saved = borrowRecordRepository.save(record);
        return BorrowRecordDTO.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowRecordDTO> getUserBorrowRecords(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with id " + userId);
        }
        return borrowRecordRepository.findByUserId(userId).stream()
                .map(BorrowRecordDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
