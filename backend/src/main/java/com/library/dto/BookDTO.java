package com.library.dto;

import com.library.entity.Book;

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer totalCopies;
    private Integer availableCopies;
    private String description;
    private String coverUrl;
    private Integer publishedYear;
    private String genre;

    // No-args constructor
    public BookDTO() {
    }

    // All-args constructor
    public BookDTO(Long id, String title, String author, String isbn,
                   Integer totalCopies, Integer availableCopies,
                   String description, String coverUrl, Integer publishedYear, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.description = description;
        this.coverUrl = coverUrl;
        this.publishedYear = publishedYear;
        this.genre = genre;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getTotalCopies() { return totalCopies; }
    public void setTotalCopies(Integer totalCopies) { this.totalCopies = totalCopies; }

    public Integer getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(Integer availableCopies) { this.availableCopies = availableCopies; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public Integer getPublishedYear() { return publishedYear; }
    public void setPublishedYear(Integer publishedYear) { this.publishedYear = publishedYear; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public static BookDTO fromEntity(Book book) {
        if (book == null) return null;
        return new BookDTO(
            book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(),
            book.getTotalCopies(), book.getAvailableCopies(),
            book.getDescription(), book.getCoverUrl(), book.getPublishedYear(), book.getGenre()
        );
    }

    public Book toEntity() {
        return Book.builder()
                .id(this.id)
                .title(this.title)
                .author(this.author)
                .isbn(this.isbn)
                .totalCopies(this.totalCopies)
                .availableCopies(this.availableCopies)
                .description(this.description)
                .coverUrl(this.coverUrl)
                .publishedYear(this.publishedYear)
                .genre(this.genre)
                .build();
    }

    // Manual Builder Pattern
    public static BookDTOBuilder builder() {
        return new BookDTOBuilder();
    }

    public static class BookDTOBuilder {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private Integer totalCopies;
        private Integer availableCopies;
        private String description;
        private String coverUrl;
        private Integer publishedYear;
        private String genre;

        public BookDTOBuilder id(Long id) { this.id = id; return this; }
        public BookDTOBuilder title(String title) { this.title = title; return this; }
        public BookDTOBuilder author(String author) { this.author = author; return this; }
        public BookDTOBuilder isbn(String isbn) { this.isbn = isbn; return this; }
        public BookDTOBuilder totalCopies(Integer totalCopies) { this.totalCopies = totalCopies; return this; }
        public BookDTOBuilder availableCopies(Integer availableCopies) { this.availableCopies = availableCopies; return this; }
        public BookDTOBuilder description(String description) { this.description = description; return this; }
        public BookDTOBuilder coverUrl(String coverUrl) { this.coverUrl = coverUrl; return this; }
        public BookDTOBuilder publishedYear(Integer publishedYear) { this.publishedYear = publishedYear; return this; }
        public BookDTOBuilder genre(String genre) { this.genre = genre; return this; }

        public BookDTO build() {
            return new BookDTO(id, title, author, isbn, totalCopies, availableCopies,
                               description, coverUrl, publishedYear, genre);
        }
    }
}
