import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { getBooks, createBook, updateBook, deleteBook, getUsers, createUser, deleteUser } from '../api/api';

function AdminBooks({ showToast }) {
  // Books catalog state
  const [books, setBooks] = useState([]);
  const [searchFilter, setSearchFilter] = useState('');
  const [bookForm, setBookForm] = useState({
    id: null,
    title: '',
    author: '',
    isbn: '',
    totalCopies: 3,
    coverUrl: '',
    description: '',
    genre: '',
    publishedYear: new Date().getFullYear()
  });
  const [isEditingBook, setIsEditingBook] = useState(false);

  // Users state
  const [users, setUsers] = useState([]);
  const [userForm, setUserForm] = useState({ name: '', email: '', phone: '', password: '' });

  // Open Library API search state
  const [olQuery, setOlQuery] = useState('');
  const [olResults, setOlResults] = useState([]);
  const [olLoading, setOlLoading] = useState(false);

  useEffect(() => {
    fetchBooks();
    fetchUsers();
  }, []);

  const fetchBooks = async () => {
    try {
      const res = await getBooks();
      setBooks(res.data);
    } catch (error) {
      console.error("Error fetching books:", error);
    }
  };

  const fetchUsers = async () => {
    try {
      const res = await getUsers();
      setUsers(res.data);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  // Book CRUD actions
  const handleBookInputChange = (e) => {
    const { name, value } = e.target;
    setBookForm(prev => ({
      ...prev,
      [name]: name === 'totalCopies' || name === 'publishedYear' ? parseInt(value, 10) || 0 : value
    }));
  };

  const handleBookSubmit = async (e) => {
    e.preventDefault();
    if (!bookForm.title || !bookForm.author) {
      showToast("Please enter at least Title and Author.", "error");
      return;
    }

    try {
      const payload = {
        title: bookForm.title,
        author: bookForm.author,
        isbn: bookForm.isbn,
        totalCopies: bookForm.totalCopies,
        availableCopies: bookForm.totalCopies,
        coverUrl: bookForm.coverUrl,
        description: bookForm.description,
        genre: bookForm.genre,
        publishedYear: bookForm.publishedYear
      };

      if (isEditingBook) {
        await updateBook(bookForm.id, payload);
        showToast(`Book '${bookForm.title}' updated successfully!`, "success");
      } else {
        await createBook(payload);
        showToast(`Book '${bookForm.title}' added successfully!`, "success");
      }
      resetBookForm();
      fetchBooks();
    } catch (error) {
      const errMsg = error.response?.data?.error || "Failed to save book.";
      showToast(errMsg, "error");
    }
  };

  const handleEditBookClick = (book) => {
    setBookForm({
      id: book.id,
      title: book.title,
      author: book.author,
      isbn: book.isbn || '',
      totalCopies: book.totalCopies,
      coverUrl: book.coverUrl || '',
      description: book.description || '',
      genre: book.genre || '',
      publishedYear: book.publishedYear || new Date().getFullYear()
    });
    setIsEditingBook(true);
  };

  const handleDeleteBookClick = async (id, title) => {
    if (!window.confirm(`Are you sure you want to delete '${title}'?`)) return;
    try {
      await deleteBook(id);
      showToast("Book deleted successfully.", "success");
      fetchBooks();
    } catch (error) {
      const errMsg = error.response?.data?.error || "Failed to delete book. Check if it is currently borrowed.";
      showToast(errMsg, "error");
    }
  };

  const resetBookForm = () => {
    setBookForm({
      id: null,
      title: '',
      author: '',
      isbn: '',
      totalCopies: 3,
      coverUrl: '',
      description: '',
      genre: '',
      publishedYear: new Date().getFullYear()
    });
    setIsEditingBook(false);
  };

  // User actions
  const handleUserInputChange = (e) => {
    const { name, value } = e.target;
    setUserForm(prev => ({ ...prev, [name]: value }));
  };

  const handleUserSubmit = async (e) => {
    e.preventDefault();
    if (!userForm.name || !userForm.email || !userForm.password) {
      showToast("Name, Email and Password are required.", "error");
      return;
    }

    try {
      await createUser(userForm);
      showToast(`User '${userForm.name}' registered successfully!`, "success");
      setUserForm({ name: '', email: '', phone: '', password: '' });
      fetchUsers();
    } catch (error) {
      const errMsg = error.response?.data?.error || "Failed to register user.";
      showToast(errMsg, "error");
    }
  };

  const handleDeleteUserClick = async (id, name) => {
    if (!window.confirm(`Are you sure you want to delete user '${name}'?`)) return;
    try {
      await deleteUser(id);
      showToast("User deleted successfully.", "success");
      fetchUsers();
    } catch (error) {
      const errMsg = error.response?.data?.error || "Failed to delete user. Check if they have active borrows.";
      showToast(errMsg, "error");
    }
  };

  // Open Library API Search
  const handleOlSearch = async (e) => {
    e.preventDefault();
    if (!olQuery.trim()) return;

    setOlLoading(true);
    setOlResults([]);
    try {
      const response = await axios.get(`https://openlibrary.org/search.json?q=${encodeURIComponent(olQuery)}&limit=6`);
      
      const docs = response.data.docs || [];
      const formatted = docs.map(doc => ({
        title: doc.title,
        author: doc.author_name ? doc.author_name.join(', ') : 'Unknown Author',
        isbn: doc.isbn ? doc.isbn[0] : 'N/A',
        coverId: doc.cover_i ? doc.cover_i : null,
        description: doc.first_sentence ? doc.first_sentence[0] : 'No description available.',
        genre: doc.subject ? doc.subject[0] : 'General',
        publishedYear: doc.first_publish_year ? doc.first_publish_year : null
      }));

      setOlResults(formatted);
      if (formatted.length === 0) {
        showToast("No books found in Open Library database.", "info");
      } else {
        showToast(`Found ${formatted.length} books in Open Library database!`, "success");
      }
    } catch (err) {
      console.error(err);
      showToast("Failed to fetch books from Open Library. Try again.", "error");
    } finally {
      setOlLoading(false);
    }
  };

  const handleImportBook = async (olBook) => {
    try {
      // Fallback unique ISBN to satisfy SQL NOT NULL / UNIQUE constraint
      const uniqueIsbn = (olBook.isbn && olBook.isbn !== 'N/A') 
        ? olBook.isbn 
        : `OL-${Date.now()}-${Math.floor(Math.random() * 1000)}`;

      await createBook({
        title: olBook.title,
        author: olBook.author,
        isbn: uniqueIsbn,
        totalCopies: 3,
        availableCopies: 3,
        coverUrl: olBook.coverId ? `https://covers.openlibrary.org/b/id/${olBook.coverId}-L.jpg` : null,
        description: olBook.description,
        genre: olBook.genre,
        publishedYear: olBook.publishedYear
      });
      showToast(`Imported '${olBook.title}' successfully!`, "success");
      fetchBooks();
    } catch (error) {
      const errMsg = error.response?.data?.error || "Failed to import book. It may already exist in library.";
      showToast(errMsg, "error");
    }
  };

  return (
    <div className="admin-dashboard">
      <div className="page-title-bar">
        <h2>Administrator Management Center</h2>
        <p>Control library catalogs, register members, and import books from open databases.</p>
      </div>

      {/* OPEN LIBRARY DATABASE IMPORT */}
      <div className="dashboard-card" style={{ marginBottom: '35px', borderLeftColor: '#9b59b6' }}>
        <div className="card-header">
          <h3>🌐 Import Books from Open Library Database</h3>
          <p>Search standard international catalogs and import titles with one click.</p>
        </div>
        
        <form onSubmit={handleOlSearch} className="search-bar">
          <input
            type="text"
            placeholder="Type book title, topic, or author (e.g. 'design patterns', 'tolkien')..."
            value={olQuery}
            onChange={(e) => setOlQuery(e.target.value)}
          />
          <button type="submit" className="btn btn-primary" style={{ backgroundColor: '#9b59b6' }}>
            {olLoading ? 'Querying...' : 'Search Open Database'}
          </button>
        </form>

        {olResults.length > 0 && (
          <div className="ol-results-grid" style={{ marginTop: '20px' }}>
            <table className="styled-table">
              <thead>
                <tr>
                  <th>Cover</th>
                  <th>Book Title</th>
                  <th>Author</th>
                  <th>ISBN</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {olResults.map((olBook, idx) => (
                  <tr key={idx}>
                    <td>
                      {olBook.coverId ? (
                        <img 
                          src={`https://covers.openlibrary.org/b/id/${olBook.coverId}-S.jpg`} 
                          alt="cover" 
                          style={{ width: '40px', height: '55px', borderRadius: '2px', objectFit: 'cover' }}
                        />
                      ) : (
                        <span style={{ fontSize: '24px' }}>📖</span>
                      )}
                    </td>
                    <td><strong>{olBook.title}</strong></td>
                    <td>{olBook.author}</td>
                    <td><code>{olBook.isbn}</code></td>
                    <td>
                      <button
                        className="btn btn-success btn-sm"
                        onClick={() => handleImportBook(olBook)}
                        style={{ backgroundColor: '#9b59b6' }}
                      >
                        📥 Import (3 Copies)
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div className="admin-grid">
        {/* Left Column: Forms */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '30px' }}>
          {/* Book Catalog CRUD Card */}
          <div className="dashboard-card">
            <div className="card-header">
              <h3>📖 Catalog Management</h3>
              <p>Add new books manually or edit inventory records.</p>
            </div>

            <form onSubmit={handleBookSubmit} className="admin-form">
              <div className="form-group">
                <label>Book Title</label>
                <input
                  type="text"
                  name="title"
                  value={bookForm.title}
                  onChange={handleBookInputChange}
                  placeholder="e.g. Code Complete"
                  required
                />
              </div>
              <div className="form-group">
                <label>Author</label>
                <input
                  type="text"
                  name="author"
                  value={bookForm.author}
                  onChange={handleBookInputChange}
                  placeholder="e.g. Steve McConnell"
                  required
                />
              </div>
              <div className="form-group">
                <label>ISBN Number</label>
                <input
                  type="text"
                  name="isbn"
                  value={bookForm.isbn}
                  onChange={handleBookInputChange}
                  placeholder="e.g. 978-0735619678"
                />
              </div>
              <div className="form-group">
                <label>Total Inventory Copies</label>
                <input
                  type="number"
                  name="totalCopies"
                  value={bookForm.totalCopies}
                  onChange={handleBookInputChange}
                  min="0"
                  required
                />
              </div>

              {/* NEW FIELDS ADDED */}
              <div className="form-group">
                <label>Cover Page Image URL</label>
                <input
                  type="text"
                  name="coverUrl"
                  value={bookForm.coverUrl}
                  onChange={handleBookInputChange}
                  placeholder="https://example.com/cover.jpg"
                />
              </div>
              <div className="form-group">
                <label>Genre</label>
                <input
                  type="text"
                  name="genre"
                  value={bookForm.genre}
                  onChange={handleBookInputChange}
                  placeholder="e.g. Technology, Sci-Fi"
                />
              </div>
              <div className="form-group">
                <label>Published Year</label>
                <input
                  type="number"
                  name="publishedYear"
                  value={bookForm.publishedYear}
                  onChange={handleBookInputChange}
                  placeholder="e.g. 2024"
                />
              </div>
              <div className="form-group">
                <label>Description / Synopsis</label>
                <textarea
                  name="description"
                  value={bookForm.description}
                  onChange={handleBookInputChange}
                  placeholder="Write a short summary..."
                  style={{ width: '100%', minHeight: '80px', padding: '10px', borderRadius: '6px', border: '1px solid var(--border-color)', boxSizing: 'border-box' }}
                />
              </div>

              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  {isEditingBook ? '💾 Update Book' : '➕ Add Book'}
                </button>
                {isEditingBook && (
                  <button type="button" className="btn btn-secondary" onClick={resetBookForm}>
                    Cancel
                  </button>
                )}
              </div>
            </form>
          </div>

          {/* User Registration Form Card */}
          <div className="dashboard-card">
            <div className="card-header">
              <h3>👤 Member Registration</h3>
              <p>Register a new student or faculty profile in the system.</p>
            </div>
            <form onSubmit={handleUserSubmit} className="admin-form">
              <div className="form-group">
                <label>Full Name</label>
                <input
                  type="text"
                  name="name"
                  value={userForm.name}
                  onChange={handleUserInputChange}
                  placeholder="e.g. Jane Doe"
                  required
                />
              </div>
              <div className="form-group">
                <label>Email Address</label>
                <input
                  type="email"
                  name="email"
                  value={userForm.email}
                  onChange={handleUserInputChange}
                  placeholder="e.g. jane@example.com"
                  required
                />
              </div>
              <div className="form-group">
                <label>Password</label>
                <input
                  type="password"
                  name="password"
                  value={userForm.password}
                  onChange={handleUserInputChange}
                  placeholder="Choose a password"
                  required
                />
              </div>
              <div className="form-group">
                <label>Phone Number</label>
                <input
                  type="text"
                  name="phone"
                  value={userForm.phone}
                  onChange={handleUserInputChange}
                  placeholder="e.g. 1234567890"
                />
              </div>
              <button type="submit" className="btn btn-success">➕ Register Profile</button>
            </form>
          </div>
        </div>

        {/* Right Column: Tables */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '30px' }}>
          {/* Book Inventory Log */}
          <div className="dashboard-card">
            <div className="card-header" style={{ marginBottom: '15px' }}>
              <h3>📦 Current Inventory Catalog</h3>
              <p>List of all books in database.</p>
            </div>

            {/* Local Inventory Search Filter */}
            <div style={{ marginBottom: '15px' }}>
              <input
                type="text"
                placeholder="🔍 Quick filter by title or author..."
                value={searchFilter}
                onChange={(e) => setSearchFilter(e.target.value)}
                style={{
                  width: '100%',
                  padding: '10px 12px',
                  borderRadius: '6px',
                  border: '1px solid var(--border-color)',
                  fontSize: '14px',
                  boxSizing: 'border-box'
                }}
              />
            </div>

            <div className="table-responsive" style={{ maxHeight: '480px', overflowY: 'auto', paddingRight: '12px' }}>
              <table className="styled-table">
                <thead>
                  <tr>
                    <th>Title / Author</th>
                    <th>ISBN</th>
                    <th>Availability</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {books
                    .filter(book => 
                      book.title.toLowerCase().includes(searchFilter.toLowerCase()) || 
                      book.author.toLowerCase().includes(searchFilter.toLowerCase())
                    )
                    .map(book => (
                      <tr key={book.id}>
                        <td>
                          <div className="book-title-cell">{book.title}</div>
                          <div className="book-author-cell">by {book.author}</div>
                        </td>
                        <td><code>{book.isbn || 'N/A'}</code></td>
                        <td>
                          <span className={`badge ${book.availableCopies > 0 ? 'badge-success' : 'badge-danger'}`}>
                            {book.availableCopies} / {book.totalCopies}
                          </span>
                        </td>
                        <td>
                          <button
                            className="btn btn-warning btn-sm"
                            onClick={() => handleEditBookClick(book)}
                            style={{ marginRight: '5px' }}
                          >
                            ✏️ Edit
                          </button>
                          <button
                            className="btn btn-danger btn-sm"
                            onClick={() => handleDeleteBookClick(book.id, book.title)}
                          >
                            🗑️ Delete
                          </button>
                        </td>
                      </tr>
                    ))}
                </tbody>
              </table>
            </div>
          </div>

          {/* User List Table Card */}
          <div className="dashboard-card">
            <div className="card-header">
              <h3>👥 Registered Members</h3>
              <p>Review active profiles. Deleting a profile checks for active books.</p>
            </div>
            <div className="table-responsive" style={{ maxHeight: '450px', overflowY: 'auto', paddingRight: '12px' }}>
              <table className="styled-table">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Member Name</th>
                    <th>Email / Phone</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {users.map(user => (
                    <tr key={user.id}>
                      <td><code>#{user.id}</code></td>
                      <td><strong>{user.name}</strong></td>
                      <td>
                        <div>{user.email}</div>
                        <div style={{ fontSize: '11px', color: '#7f8c8d' }}>{user.phone || 'No Phone'}</div>
                      </td>
                      <td>
                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => handleDeleteUserClick(user.id, user.name)}
                        >
                          🗑️ Delete
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AdminBooks;
