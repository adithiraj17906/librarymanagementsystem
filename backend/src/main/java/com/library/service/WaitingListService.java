package com.library.service;

import com.library.dto.WaitingListEntryDTO;
import java.util.List;

public interface WaitingListService {
    WaitingListEntryDTO joinWaitingList(Long userId, Long bookId);
    void claimBook(Long entryId);
    List<WaitingListEntryDTO> getWaitingListForBook(Long bookId);
    List<WaitingListEntryDTO> getUserWaitingList(Long userId);
    void processExpiredNotifications();
}
