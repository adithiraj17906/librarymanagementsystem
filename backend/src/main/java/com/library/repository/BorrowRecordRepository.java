package com.library.repository;

import com.library.entity.BorrowRecord;
import com.library.entity.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUserId(Long userId);
    List<BorrowRecord> findByUserIdAndStatus(Long userId, BorrowStatus status);
    List<BorrowRecord> findByStatusAndDueDateBefore(BorrowStatus status, LocalDate date);
    List<BorrowRecord> findByStatusAndDueDateBetween(BorrowStatus status, LocalDate start, LocalDate end);
    void deleteByUserId(Long userId);
}
