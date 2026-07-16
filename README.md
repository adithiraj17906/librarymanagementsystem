# JAVA FULL STACK TRAINING

**Project: Library Management System**

## Team Members
| Name | Roll No |
|---|---|
| Adithi Raj | 2411CS060111 |
| Madhu Sree | 2411CS060119 |
| Sushvika | 2411CS060107 |
| Sai Vardhan | 2411CS060093 |

---

## About the Project
A full-stack Library Management System built with Spring Boot (Java) and React.js.

## Tech Stack
- **Backend**: Spring Boot, Spring Data JPA, Maven, Java, MySQL
- **Frontend**: React, React Router, Axios, Plain CSS
- **Database**: MySQL

## Features

### 1. Book Catalog
Browse, search, borrow, or join a waiting list for books.

### 2. Smart Waiting List
- If a book has 0 copies available, users are added to a FIFO waiting list.
- When a book is returned, the copy is reserved for the first user in the queue.
- That user gets a notification with a 24-hour window to claim the book.
- If they don't claim it in time, it automatically moves to the next user in queue (or is released if no one is waiting).

### 3. User Dashboard
View borrowed books, waiting list status, fines (₹5/day late), and claim available reservations.

### 4. Notifications
Alerts for due date reminders, fine updates, and book availability.

### 5. Admin Dashboard
Manage books and library members (add, update, delete).

---

## API Testing
A Postman collection (`Library_Management_System.postman_collection.json`) is included, covering:
- **Users** – create, view, update, delete members
- **Books** – add, search, update, delete, view available books
- **Borrow** – borrow and return books (auto-calculates fines and reserves for next user in queue)
- **Waiting List** – join queue, claim a reserved book
- **Notifications** – view and mark notifications as read

---

## Project Structure
```
library-management-system/
├── backend/
│   └── src/main/java/com/library/
│       ├── config/          # CORS and Scheduler configs
│       ├── controller/      # REST endpoints
│       ├── dto/             # Request/Response objects
│       ├── entity/          # Database entities
│       ├── repository/      # Data access layer
│       ├── scheduler/       # Background jobs (due alerts, claim expiry)
│       └── service/         # Business logic
├── frontend/
│   └── src/
│       ├── api/              # API calls
│       └── pages/            # App pages (Book List, My Borrows, Notifications, Admin)
├── Library_Management_System.postman_collection.json
└── README.md
```

## Scheduled Background Jobs
- **Waiting List Expiry** – runs every hour, checks if a reserved book's 24-hour claim window has expired, and moves the reservation to the next user in queue.
- **Due Date Reminders** – runs daily, marks overdue borrows and sends reminders for books due soon.

## Fine Rules
- ₹5 per day late
- Borrowing period: 14 days
