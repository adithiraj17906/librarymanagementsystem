import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: { 'Content-Type': 'application/json' },
});

// Users
export const getUsers    = ()           => API.get('/users');
export const getUser     = (id)         => API.get(`/users/${id}`);
export const createUser  = (data)       => API.post('/users', data);
export const updateUser  = (id, data)   => API.put(`/users/${id}`, data);
export const deleteUser  = (id)         => API.delete(`/users/${id}`);
export const loginUser   = (email, pwd) => API.post('/users/login', { email, password: pwd });

// Books
export const getBooks         = ()        => API.get('/books');
export const getBook          = (id)      => API.get(`/books/${id}`);
export const getAvailableBooks = ()       => API.get('/books/available');
export const searchBooks      = (query)   => API.get(`/books?search=${encodeURIComponent(query)}`);
export const createBook       = (data)    => API.post('/books', data);
export const updateBook       = (id, data)=> API.put(`/books/${id}`, data);
export const deleteBook       = (id)      => API.delete(`/books/${id}`);

// Borrow
export const borrowBook    = (userId, bookId)     => API.post(`/borrow/${userId}/${bookId}`);
export const returnBook    = (recordId)            => API.post(`/borrow/return/${recordId}`);
export const getUserBorrows = (userId)             => API.get(`/borrow/user/${userId}`);

// Waiting List
export const joinWaitingList  = (userId, bookId) => API.post(`/waitlist/join/${userId}/${bookId}`);
export const claimBook        = (entryId)         => API.post(`/waitlist/claim/${entryId}`);
export const getWaitingList   = (bookId)          => API.get(`/waitlist/book/${bookId}`);
export const getUserWaitingList = (userId)        => API.get(`/waitlist/user/${userId}`);

// Notifications
export const getNotifications       = (userId) => API.get(`/notifications/${userId}`);
export const markNotificationAsRead = (id)     => API.put(`/notifications/${id}/read`);

export default API;
