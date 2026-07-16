package com.library.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {

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
    private LocalDate borrowDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    private Double fineAmount = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowStatus status;

    // No-args constructor
    public BorrowRecord() {
    }

    // All-args constructor
    public BorrowRecord(Long id, User user, Book book, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, Double fineAmount, BorrowStatus status) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount != null ? fineAmount : 0.0;
        this.status = status;
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

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }

    // Manual Builder Pattern
    public static BorrowRecordBuilder builder() {
        return new BorrowRecordBuilder();
    }

    public static class BorrowRecordBuilder {
        private Long id;
        private User user;
        private Book book;
        private LocalDate borrowDate;
        private LocalDate dueDate;
        private LocalDate returnDate;
        private Double fineAmount = 0.0;
        private BorrowStatus status;

        public BorrowRecordBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public BorrowRecordBuilder user(User user) {
            this.user = user;
            return this;
        }

        public BorrowRecordBuilder book(Book book) {
            this.book = book;
            return this;
        }

        public BorrowRecordBuilder borrowDate(LocalDate borrowDate) {
            this.borrowDate = borrowDate;
            return this;
        }

        public BorrowRecordBuilder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public BorrowRecordBuilder returnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public BorrowRecordBuilder fineAmount(Double fineAmount) {
            this.fineAmount = fineAmount;
            return this;
        }

        public BorrowRecordBuilder status(BorrowStatus status) {
            this.status = status;
            return this;
        }

        public BorrowRecord build() {
            return new BorrowRecord(id, user, book, borrowDate, dueDate, returnDate, fineAmount, status);
        }
    }
}
