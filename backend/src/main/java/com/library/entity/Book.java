package com.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author is required")
    @Column(nullable = false)
    private String author;

    @Column(unique = true)
    private String isbn;

    @NotNull(message = "Total copies is required")
    @Min(value = 0, message = "Total copies cannot be negative")
    @Column(nullable = false)
    private Integer totalCopies;

    @NotNull(message = "Available copies is required")
    @Min(value = 0, message = "Available copies cannot be negative")
    @Column(nullable = false)
    private Integer availableCopies;

    // New fields for rich book info
    @Column(length = 1000)
    private String description;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Column(name = "published_year")
    private Integer publishedYear;

    private String genre;

    // No-args constructor
    public Book() {
    }

    // All-args constructor
    public Book(Long id, String title, String author, String isbn,
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

    // Manual Builder Pattern
    public static BookBuilder builder() {
        return new BookBuilder();
    }

    public static class BookBuilder {
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

        public BookBuilder id(Long id) { this.id = id; return this; }
        public BookBuilder title(String title) { this.title = title; return this; }
        public BookBuilder author(String author) { this.author = author; return this; }
        public BookBuilder isbn(String isbn) { this.isbn = isbn; return this; }
        public BookBuilder totalCopies(Integer totalCopies) { this.totalCopies = totalCopies; return this; }
        public BookBuilder availableCopies(Integer availableCopies) { this.availableCopies = availableCopies; return this; }
        public BookBuilder description(String description) { this.description = description; return this; }
        public BookBuilder coverUrl(String coverUrl) { this.coverUrl = coverUrl; return this; }
        public BookBuilder publishedYear(Integer publishedYear) { this.publishedYear = publishedYear; return this; }
        public BookBuilder genre(String genre) { this.genre = genre; return this; }

        public Book build() {
            return new Book(id, title, author, isbn, totalCopies, availableCopies,
                            description, coverUrl, publishedYear, genre);
        }
    }
}
