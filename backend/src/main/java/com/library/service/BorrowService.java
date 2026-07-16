package com.library.service;

import com.library.dto.BorrowRecordDTO;
import java.util.List;

public interface BorrowService {
    BorrowRecordDTO borrowBook(Long userId, Long bookId);
    BorrowRecordDTO returnBook(Long borrowRecordId);
    List<BorrowRecordDTO> getUserBorrowRecords(Long userId);
}
