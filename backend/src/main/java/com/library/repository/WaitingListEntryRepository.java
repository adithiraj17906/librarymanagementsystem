package com.library.repository;

import com.library.entity.WaitingListEntry;
import com.library.entity.WaitingListStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface WaitingListEntryRepository extends JpaRepository<WaitingListEntry, Long> {
    List<WaitingListEntry> findByBookIdAndStatusOrderByQueuePositionAsc(Long bookId, WaitingListStatus status);
    List<WaitingListEntry> findByUserIdAndBookIdAndStatusIn(Long userId, Long bookId, Collection<WaitingListStatus> statuses);
    List<WaitingListEntry> findByStatusAndClaimDeadlineBefore(WaitingListStatus status, LocalDateTime deadline);
    List<WaitingListEntry> findByUserIdAndStatusIn(Long userId, Collection<WaitingListStatus> statuses);
    void deleteByUserId(Long userId);
}
