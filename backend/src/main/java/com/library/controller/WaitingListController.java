package com.library.controller;

import com.library.dto.WaitingListEntryDTO;
import com.library.service.WaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/waitlist")
public class WaitingListController {

    private final WaitingListService waitingListService;

    @Autowired
    public WaitingListController(WaitingListService waitingListService) {
        this.waitingListService = waitingListService;
    }

    @PostMapping("/join/{userId}/{bookId}")
    public ResponseEntity<WaitingListEntryDTO> joinWaitingList(@PathVariable Long userId, @PathVariable Long bookId) {
        return ResponseEntity.ok(waitingListService.joinWaitingList(userId, bookId));
    }

    @PostMapping("/claim/{entryId}")
    public ResponseEntity<String> claimBook(@PathVariable Long entryId) {
        waitingListService.claimBook(entryId);
        return ResponseEntity.ok("Book successfully claimed and borrowed!");
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<WaitingListEntryDTO>> getWaitingList(@PathVariable Long bookId) {
        return ResponseEntity.ok(waitingListService.getWaitingListForBook(bookId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WaitingListEntryDTO>> getUserWaitingList(@PathVariable Long userId) {
        return ResponseEntity.ok(waitingListService.getUserWaitingList(userId));
    }
}
