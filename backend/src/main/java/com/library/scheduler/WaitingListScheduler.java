package com.library.scheduler;

import com.library.service.WaitingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WaitingListScheduler {

    private final WaitingListService waitingListService;

    @Autowired
    public WaitingListScheduler(WaitingListService waitingListService) {
        this.waitingListService = waitingListService;
    }

    // Runs every 1 hour (3,600,000 milliseconds)
    @Scheduled(fixedRate = 3600000)
    public void checkExpiredClaims() {
        waitingListService.processExpiredNotifications();
    }
}
