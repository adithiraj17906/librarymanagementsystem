# Library Management System

A full-stack Library Management System built with Spring Boot (Java) and React.js (JavaScript).

## Tech Stack
- **Backend**: Spring Boot 3.2.0, Spring Data JPA, Maven, Java 17, MySQL
- **Frontend**: React 18, React Router v6, Axios, Plain CSS (no UI frameworks like Tailwind/MUI)
- **Database**: MySQL 8.0

## Features
- **Book Catalog**: Browse, search, borrow, or join waiting lists.
- **Smart Waiting List**:
  - Automatically puts users on a FIFO waiting list queue when a book has 0 available copies.
  - When a book is returned, it reserves the copy and notifies the first user in the queue.
  - Generates a `BOOK_AVAILABLE` notification with a 24-hour claim window.
  - Automatically expires reservations after 24 hours via a scheduled job, moving to the next user or releasing the copy back to general availability.
- **User Dashboard**: View borrowed books, active waiting lists, accumulated fines (calculated at ₹5/day late), and claim ready reservations.
- **System Notifications**: Display due reminders, fine updates, and availability notifications.
- **Admin Dashboard**: Manage books inventory (CRUD) and library members (CRUD).

---

## Setup Instructions

### 1. Database Setup
1. Ensure MySQL is running on your system.
2. Log into MySQL and create the database:
   ```sql
   CREATE DATABASE library_management_system;
   ```
3. The database tables will be auto-created by Spring Boot on startup because `spring.jpa.hibernate.ddl-auto=update` is enabled in `application.properties`.

### 2. Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```
3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
4. The server will start and listen on port **8080** (http://localhost:8080).

### 3. Frontend Setup
1. Open a new terminal window.
2. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
3. Install dependencies:
   ```bash
   npm install
   ```
4. Start the React development server:
   ```bash
   npm start
   ```
5. The library client portal will open in your browser at http://localhost:3000.

---

## API Testing with Postman
1. Open Postman.
2. Click **Import** and select the file `Library_Management_System.postman_collection.json` located at the root of the project directory.
3. The collection is configured with a collection variable `{{baseUrl}}` pointing to `http://localhost:8080/api`.
4. You can execute requests in folders:
   - **Users**: Create, read, update, delete library members.
   - **Books**: Create, search, update, delete, view available catalog.
   - **Borrow**: Borrow a book, return a book (triggers fine calculation and reservation of next queue user).
   - **Waiting List**: Join a waitlist queue, claim a reserved book.
   - **Notifications**: Retrieve a user's notification list and mark notifications as read.

---

## Detailed Directory Structure
```
library-management-system/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/library/
│       ├── LibraryManagementSystemApplication.java
│       ├── config/              # CORS and Scheduler configurations
│       ├── controller/          # REST endpoints & GlobalExceptionHandler
│       ├── dto/                 # Request/Response DTO wrappers
│       ├── entity/              # JPA Database entities & Enums
│       ├── repository/          # Spring Data JPA Repository interfaces
│       ├── scheduler/           # Daily due alerts and hourly claim expiry cron tasks
│       └── service/             # Business logic layer (Interfaces + Impls)
├── frontend/
│   ├── package.json
│   ├── public/                  # Static index.html root page
│   └── src/
│       ├── App.js               # Router layout and login simulation
│       ├── App.css              # Custom plain CSS style system
│       ├── api/
│       │   └── api.js           # Axios endpoint clients
│       └── pages/
│           ├── AdminBooks.js    # Books and users administration panels
│           ├── BookList.js      # Book catalog with search & borrow/waitlist buttons
│           ├── MyBorrows.js     # User's borrows, returns, and waitlist queues
│           └── Notifications.js # System alerts list & reservation claim options
├── Library_Management_System.postman_collection.json
└── README.md
```

## Scheduled Tasks Configuration
- **Hourly Expiry Job**: `WaitingListScheduler` runs every 1 hour (`fixedRate = 3600000`). It checks for waitlist entries in `NOTIFIED` status whose `claimDeadline` has elapsed, marks them as `EXPIRED`, and triggers notifications for the next waiting user (or releases the copy if the queue is empty).
- **Daily Due Alert Job**: `DueReminderScheduler` runs daily at 8:00 AM (`cron = "0 0 8 * * *"`). It updates the status of late borrows to `OVERDUE` and sends warning alerts for borrows that are due within 24 hours.

## Fine Calculations
- **Overdue Penalty**: ₹5 per day late.
- **Borrow Window**: 14 days from checkout.
- Fines are automatically calculated and recorded inside `BorrowRecord.fineAmount` on return. A notification of type `FINE_UPDATE` is dispatched to the user.
