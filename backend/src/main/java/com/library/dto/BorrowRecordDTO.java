package com.library.dto;

import com.library.entity.BorrowRecord;
import com.library.entity.BorrowStatus;
import java.time.LocalDate;

public class BorrowRecordDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long bookId;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Double fineAmount;
    private BorrowStatus status;

    // No-args constructor
    public BorrowRecordDTO() {
    }

    // All-args constructor
    public BorrowRecordDTO(Long id, Long userId, String userName, Long bookId, String bookTitle, LocalDate borrowDate, LocalDate dueDate, LocalDate returnDate, Double fineAmount, BorrowStatus status) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.status = status;
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

    public static BorrowRecordDTO fromEntity(BorrowRecord record) {
        if (record == null) return null;
        return BorrowRecordDTO.builder()
                .id(record.getId())
                .userId(record.getUser() != null ? record.getUser().getId() : null)
                .userName(record.getUser() != null ? record.getUser().getName() : null)
                .bookId(record.getBook() != null ? record.getBook().getId() : null)
                .bookTitle(record.getBook() != null ? record.getBook().getTitle() : null)
                .borrowDate(record.getBorrowDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .fineAmount(record.getFineAmount())
                .status(record.getStatus())
                .build();
    }

    // Manual Builder Pattern
    public static BorrowRecordDTOBuilder builder() {
        return new BorrowRecordDTOBuilder();
    }

    public static class BorrowRecordDTOBuilder {
        private Long id;
        private Long userId;
        private String userName;
        private Long bookId;
        private String bookTitle;
        private LocalDate borrowDate;
        private LocalDate dueDate;
        private LocalDate returnDate;
        private Double fineAmount;
        private BorrowStatus status;

        public BorrowRecordDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public BorrowRecordDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public BorrowRecordDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public BorrowRecordDTOBuilder bookId(Long bookId) {
            this.bookId = bookId;
            return this;
        }

        public BorrowRecordDTOBuilder bookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
            return this;
        }

        public BorrowRecordDTOBuilder borrowDate(LocalDate borrowDate) {
            this.borrowDate = borrowDate;
            return this;
        }

        public BorrowRecordDTOBuilder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public BorrowRecordDTOBuilder returnDate(LocalDate returnDate) {
            this.returnDate = returnDate;
            return this;
        }

        public BorrowRecordDTOBuilder fineAmount(Double fineAmount) {
            this.fineAmount = fineAmount;
            return this;
        }

        public BorrowRecordDTOBuilder status(BorrowStatus status) {
            this.status = status;
            return this;
        }

        public BorrowRecordDTO build() {
            return new BorrowRecordDTO(id, userId, userName, bookId, bookTitle, borrowDate, dueDate, returnDate, fineAmount, status);
        }
    }
}
