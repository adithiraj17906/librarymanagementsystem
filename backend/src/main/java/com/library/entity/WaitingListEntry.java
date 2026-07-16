package com.library.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_list")
public class WaitingListEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false)
    private Integer queuePosition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WaitingListStatus status;

    private LocalDateTime notifiedAt;

    private LocalDateTime claimDeadline;

    // No-args constructor
    public WaitingListEntry() {
    }

    // All-args constructor
    public WaitingListEntry(Long id, User user, Book book, Integer queuePosition, WaitingListStatus status, LocalDateTime notifiedAt, LocalDateTime claimDeadline) {
        this.id = id;
        this.user = user;
        this.book = book;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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

    // Manual Builder Pattern
    public static WaitingListEntryBuilder builder() {
        return new WaitingListEntryBuilder();
    }

    public static class WaitingListEntryBuilder {
        private Long id;
        private User user;
        private Book book;
        private Integer queuePosition;
        private WaitingListStatus status;
        private LocalDateTime notifiedAt;
        private LocalDateTime claimDeadline;

        public WaitingListEntryBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public WaitingListEntryBuilder user(User user) {
            this.user = user;
            return this;
        }

        public WaitingListEntryBuilder book(Book book) {
            this.book = book;
            return this;
        }

        public WaitingListEntryBuilder queuePosition(Integer queuePosition) {
            this.queuePosition = queuePosition;
            return this;
        }

        public WaitingListEntryBuilder status(WaitingListStatus status) {
            this.status = status;
            return this;
        }

        public WaitingListEntryBuilder notifiedAt(LocalDateTime notifiedAt) {
            this.notifiedAt = notifiedAt;
            return this;
        }

        public WaitingListEntryBuilder claimDeadline(LocalDateTime claimDeadline) {
            this.claimDeadline = claimDeadline;
            return this;
        }

        public WaitingListEntry build() {
            return new WaitingListEntry(id, user, book, queuePosition, status, notifiedAt, claimDeadline);
        }
    }
}
