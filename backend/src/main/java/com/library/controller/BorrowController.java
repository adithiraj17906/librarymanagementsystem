package com.library.controller;

import com.library.dto.BorrowRecordDTO;
import com.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/{userId}/{bookId}")
    public ResponseEntity<BorrowRecordDTO> borrowBook(@PathVariable Long userId, @PathVariable Long bookId) {
        return ResponseEntity.ok(borrowService.borrowBook(userId, bookId));
    }

    @PostMapping("/return/{borrowRecordId}")
    public ResponseEntity<BorrowRecordDTO> returnBook(@PathVariable Long borrowRecordId) {
        return ResponseEntity.ok(borrowService.returnBook(borrowRecordId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BorrowRecordDTO>> getUserBorrows(@PathVariable Long userId) {
        return ResponseEntity.ok(borrowService.getUserBorrowRecords(userId));
    }
}
