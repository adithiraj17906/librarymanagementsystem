package com.library.dto;

import com.library.entity.WaitingListEntry;
import com.library.entity.WaitingListStatus;
import java.time.LocalDateTime;

public class WaitingListEntryDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long bookId;
    private String bookTitle;
    private Integer queuePosition;
    private WaitingListStatus status;
    private LocalDateTime notifiedAt;
    private LocalDateTime claimDeadline;

    // No-args constructor
    public WaitingListEntryDTO() {
    }

    // All-args constructor
    public WaitingListEntryDTO(Long id, Long userId, String userName, Long bookId, String bookTitle, Integer queuePosition, WaitingListStatus status, LocalDateTime notifiedAt, LocalDateTime claimDeadline) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.queuePosition = queuePosition;
        this.status = status;
        this.notifiedAt = notifiedAt;
        this.claimDeadline = claimDeadline;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Integer getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(Integer queuePosition) {
        this.queuePosition = queuePosition;
    }

    public WaitingListStatus getStatus() {
        return status;
    }

    public void setStatus(WaitingListStatus status) {
        this.status = status;
    }

    public LocalDateTime getNotifiedAt() {
        return notifiedAt;
    }

    public void setNotifiedAt(LocalDateTime notifiedAt) {
        this.notifiedAt = notifiedAt;
    }

    public LocalDateTime getClaimDeadline() {
        return claimDeadline;
    }

    public void setClaimDeadline(LocalDateTime claimDeadline) {
        this.claimDeadline = claimDeadline;
    }

    public static WaitingListEntryDTO fromEntity(WaitingListEntry entry) {
        if (entry == null) return null;
        return WaitingListEntryDTO.builder()
                .id(entry.getId())
                .userId(entry.getUser() != null ? entry.getUser().getId() : null)
                .userName(entry.getUser() != null ? entry.getUser().getName() : null)
                .bookId(entry.getBook() != null ? entry.getBook().getId() : null)
                .bookTitle(entry.getBook() != null ? entry.getBook().getTitle() : null)
                .queuePosition(entry.getQueuePosition())
                .status(entry.getStatus())
                .notifiedAt(entry.getNotifiedAt())
                .claimDeadline(entry.getClaimDeadline())
                .build();
    }

    // Manual Builder Pattern
    public static WaitingListEntryDTOBuilder builder() {
        return new WaitingListEntryDTOBuilder();
    }

    public static class WaitingListEntryDTOBuilder {
        private Long id;
        private Long userId;
        private String userName;
        private Long bookId;
        private String bookTitle;
        private Integer queuePosition;
        private WaitingListStatus status;
        private LocalDateTime notifiedAt;
        private LocalDateTime claimDeadline;

        public WaitingListEntryDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public WaitingListEntryDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public WaitingListEntryDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public WaitingListEntryDTOBuilder bookId(Long bookId) {
            this.bookId = bookId;
            return this;
        }

        public WaitingListEntryDTOBuilder bookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
            return this;
        }

        public WaitingListEntryDTOBuilder queuePosition(Integer queuePosition) {
            this.queuePosition = queuePosition;
            return this;
        }

        public WaitingListEntryDTOBuilder status(WaitingListStatus status) {
            this.status = status;
            return this;
        }

        public WaitingListEntryDTOBuilder notifiedAt(LocalDateTime notifiedAt) {
            this.notifiedAt = notifiedAt;
            return this;
        }

        public WaitingListEntryDTOBuilder claimDeadline(LocalDateTime claimDeadline) {
            this.claimDeadline = claimDeadline;
            return this;
        }

        public WaitingListEntryDTO build() {
            return new WaitingListEntryDTO(id, userId, userName, bookId, bookTitle, queuePosition, status, notifiedAt, claimDeadline);
        }
    }
}
