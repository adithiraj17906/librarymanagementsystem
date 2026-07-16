# Library Management System

A full-stack Library Management System built with Spring Boot and React.

## Tech Stack
- **Backend**: Spring Boot, Spring Data JPA, Maven, Java 17, MySQL
- **Frontend**: React, React Router, Axios, plain CSS (no UI frameworks)
- **Database**: MySQL

## Features
Feature 1: Smart Waiting List
- When a student tries to borrow a book with availableCopies = 0, they are added to a WaitingListEntry queue (FIFO, ordered by queuePosition).
- When a book is returned (availableCopies increases):
  - Check if a waiting list exists for that book.
  - Notify the FIRST student in queue (create a Notification of type
    
**Book Catalog**
Browse, search, borrow, or join a waiting list for books.

**Smart Waiting List**
- If a book has 0 copies available, users are added to a FIFO waiting list.
- When a book is returned, it's reserved for the first user in the queue and they get notified.
- The user has 24 hours to claim the book before it moves to the next person in line.
- A scheduled job checks for expired reservations and passes the book to the next user, or releases it if no one is waiting.

**User Dashboard**
View borrowed books, waiting list status, fines (₹5/day for late returns), and claim available reservations.

**Notifications**
Due date reminders, fine updates, and book availability alerts.

**Admin Dashboard**
Manage books and users (add, edit, delete).

## API Testing
A Postman collection (`Library_Management_System.postman_collection.json`) is included in the project root, covering all endpoints: Users, Books, Borrow/Return, Waiting List, and Notifications.

## Project Structure
```
library-management-system/
├── backend/
│   └── src/main/java/com/library/
│       ├── config/        # CORS and scheduler configuration
│       ├── controller/    # REST endpoints
│       ├── dto/           # Request/response objects
│       ├── entity/        # Database entities
│       ├── repository/    # Data access layer
│       ├── scheduler/     # Background jobs (due alerts, expiry checks)
│       └── service/       # Business logic
├── frontend/
│   └── src/
│       ├── api/           # Axios API calls
│       └── pages/         # BookList, MyBorrows, Notifications, AdminBooks
├── Library_Management_System.postman_collection.json
└── README.md
```
