package com.library.scheduler;

import com.library.entity.BorrowRecord;
import com.library.entity.BorrowStatus;
import com.library.entity.Notification;
import com.library.entity.NotificationType;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DueReminderScheduler {

    private final BorrowRecordRepository borrowRecordRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public DueReminderScheduler(BorrowRecordRepository borrowRecordRepository,
                                NotificationRepository notificationRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.notificationRepository = notificationRepository;
    }

    // Runs once daily at 8 AM: "0 0 8 * * *"
    // For testing and reliability, let's keep the cron format standard
    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void checkDueDatesAndSendReminders() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // Find BORROWED records where dueDate is before today (i.e. overdue)
        List<BorrowRecord> newlyOverdue = borrowRecordRepository.findByStatusAndDueDateBefore(BorrowStatus.BORROWED, today);
        for (BorrowRecord record : newlyOverdue) {
            record.setStatus(BorrowStatus.OVERDUE);
            borrowRecordRepository.save(record);
        }

        // 2. Send reminders for due within 24 hours (today or tomorrow)
        // Find BORROWED records due between today and tomorrow
        List<BorrowRecord> dueSoon = borrowRecordRepository.findByStatusAndDueDateBetween(BorrowStatus.BORROWED, today, tomorrow);
        for (BorrowRecord record : dueSoon) {
            createReminderNotification(record, String.format(
                    "Reminder: Your borrow of '%s' is due soon on %s. Please return it to avoid fines.",
                    record.getBook().getTitle(), record.getDueDate()));
        }

        // 3. Send reminders for all records currently marked as OVERDUE
        List<BorrowRecord> overdueRecords = borrowRecordRepository.findByStatusAndDueDateBefore(BorrowStatus.OVERDUE, today.plusDays(1));
        for (BorrowRecord record : overdueRecords) {
            createReminderNotification(record, String.format(
                    "URGENT: Your borrow of '%s' is OVERDUE! It was due on %s. A fine of ₹5/day is accumulating.",
                    record.getBook().getTitle(), record.getDueDate()));
        }
    }

    private void createReminderNotification(BorrowRecord record, String message) {
        Notification notification = Notification.builder()
                .user(record.getUser())
                .message(message)
                .type(NotificationType.DUE_REMINDER)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }
}
