import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { getBooks, searchBooks, borrowBook, joinWaitingList } from '../api/api';

function BookList({ currentUserId, showToast }) {
  const [books, setBooks]         = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [loading, setLoading]     = useState(false);
  const [viewMode, setViewMode]   = useState('grid'); // 'grid' | 'list'

  const fetchBooks = async (query = '') => {
    setLoading(true);
    try {
      const res = query ? await searchBooks(query) : await getBooks();
      setBooks(res.data);
    } catch {
      showToast('Failed to load catalog. Is the server online?', 'error');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchBooks(); }, []);

  const handleBorrow = async (bookId, title) => {
    try {
      await borrowBook(currentUserId, bookId);
      showToast(`✅ Borrowed "${title}"!`, 'success');
      fetchBooks(searchQuery);
    } catch (err) {
      showToast(err.response?.data?.error || 'Failed to borrow.', 'error');
    }
  };

  const handleJoinWaitlist = async (bookId, title) => {
    try {
      const res = await joinWaitingList(currentUserId, bookId);
      showToast(`⏳ Joined waitlist for "${title}"! Position: ${res.data.queuePosition}`, 'success');
    } catch (err) {
      showToast(err.response?.data?.error || 'Failed to join waitlist.', 'error');
    }
  };

  const placeholder = (title) =>
    `https://via.placeholder.com/160x220/2c3e50/ffffff?text=${encodeURIComponent(title.slice(0,12))}`;

  return (
    <div>
      <div className="page-title-bar">
        <h2>📚 Book Catalog</h2>
        <p>Browse our collection — borrow instantly or reserve unavailable titles.</p>
      </div>

      {/* Search + view toggle bar */}
      <div className="catalog-toolbar">
        <form
          onSubmit={e => { e.preventDefault(); fetchBooks(searchQuery); }}
          className="search-bar"
          style={{ flex: 1, margin: 0 }}
        >
          <input
            type="text"
            placeholder="Search by title or author…"
            value={searchQuery}
            onChange={e => setSearchQuery(e.target.value)}
          />
          <button type="submit" className="btn btn-primary">🔍 Search</button>
          {searchQuery && (
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => { setSearchQuery(''); fetchBooks(''); }}
            >
              Clear
            </button>
          )}
        </form>

        <div className="view-toggle">
          <button
            className={`btn btn-sm ${viewMode === 'grid' ? 'btn-primary' : 'btn-secondary'}`}
            onClick={() => setViewMode('grid')}
          >⊞ Grid</button>
          <button
            className={`btn btn-sm ${viewMode === 'list' ? 'btn-primary' : 'btn-secondary'}`}
            onClick={() => setViewMode('list')}
          >≡ List</button>
        </div>
      </div>

      {loading ? (
        <div className="loading-state">Loading library catalog…</div>
      ) : books.length === 0 ? (
        <div className="empty-state">No books found. Try a different search or ask an admin to seed the catalog.</div>
      ) : viewMode === 'grid' ? (

        /* ─── GRID VIEW ─── */
        <div className="books-grid">
          {books.map(book => (
            <div key={book.id} className="book-card">
              <Link to={`/books/${book.id}`} className="book-card-cover-link">
                <img
                  src={book.coverUrl || placeholder(book.title)}
                  alt={book.title}
                  className="book-card-cover"
                  onError={e => { e.target.src = placeholder(book.title); }}
                />
                {book.availableCopies === 0 && (
                  <div className="book-card-overlay">Unavailable</div>
                )}
              </Link>

              <div className="book-card-body">
                <Link to={`/books/${book.id}`} className="book-card-title">{book.title}</Link>
                <p className="book-card-author">by {book.author}</p>
                {book.genre && <span className="badge badge-info" style={{ fontSize: 10, marginBottom: 8 }}>{book.genre}</span>}
                <div className="book-card-footer">
                  <span className={`badge ${book.availableCopies > 0 ? 'badge-success' : 'badge-danger'}`}>
                    {book.availableCopies > 0 ? `${book.availableCopies} avail.` : 'Unavail.'}
                  </span>
                  {book.availableCopies > 0 ? (
                    <button
                      className="btn btn-success btn-sm"
                      onClick={() => handleBorrow(book.id, book.title)}
                    >Borrow</button>
                  ) : (
                    <button
                      className="btn btn-warning btn-sm"
                      onClick={() => handleJoinWaitlist(book.id, book.title)}
                    >Waitlist</button>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>

      ) : (

        /* ─── LIST VIEW ─── */
        <div className="dashboard-card">
          <div className="table-responsive">
            <table className="styled-table">
              <thead>
                <tr>
                  <th>Cover</th>
                  <th>Title & Author</th>
                  <th>Genre</th>
                  <th>Year</th>
                  <th>Availability</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {books.map(book => (
                  <tr key={book.id}>
                    <td>
                      <img
                        src={book.coverUrl || placeholder(book.title)}
                        alt={book.title}
                        style={{ width: 40, height: 56, objectFit: 'cover', borderRadius: 4 }}
                        onError={e => { e.target.src = placeholder(book.title); }}
                      />
                    </td>
                    <td>
                      <Link to={`/books/${book.id}`} className="book-table-title">{book.title}</Link>
                      <div className="book-author-cell">by {book.author}</div>
                    </td>
                    <td>{book.genre || '—'}</td>
                    <td>{book.publishedYear || '—'}</td>
                    <td>
                      <span className={`badge ${book.availableCopies > 0 ? 'badge-success' : 'badge-danger'}`}>
                        {book.availableCopies}/{book.totalCopies}
                      </span>
                    </td>
                    <td>
                      {book.availableCopies > 0 ? (
                        <button className="btn btn-success btn-sm" onClick={() => handleBorrow(book.id, book.title)}>
                          📖 Borrow
                        </button>
                      ) : (
                        <button className="btn btn-warning btn-sm" onClick={() => handleJoinWaitlist(book.id, book.title)}>
                          ⏳ Waitlist
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}

export default BookList;
