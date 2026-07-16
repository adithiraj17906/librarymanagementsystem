import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getBook, borrowBook, joinWaitingList } from '../api/api';

function BookDetail({ currentUserId, showToast }) {
  const { id } = useParams();
  const navigate = useNavigate();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBook();
    // eslint-disable-next-line
  }, [id]);

  const fetchBook = async () => {
    setLoading(true);
    try {
      const res = await getBook(id);
      setBook(res.data);
    } catch {
      showToast('Book not found.', 'error');
      navigate('/');
    } finally {
      setLoading(false);
    }
  };

  const handleBorrow = async () => {
    try {
      await borrowBook(currentUserId, book.id);
      showToast(`✅ Borrowed "${book.title}" successfully!`, 'success');
      fetchBook();
    } catch (err) {
      showToast(err.response?.data?.error || 'Failed to borrow.', 'error');
    }
  };

  const handleWaitlist = async () => {
    try {
      const res = await joinWaitingList(currentUserId, book.id);
      showToast(`⏳ Added to waitlist! Position: ${res.data.queuePosition}`, 'success');
    } catch (err) {
      showToast(err.response?.data?.error || 'Failed to join waitlist.', 'error');
    }
  };

  if (loading) {
    return <div className="loading-state" style={{ marginTop: 60 }}>Loading book info…</div>;
  }
  if (!book) return null;

  const coverFallback = `https://via.placeholder.com/200x300/2c3e50/ffffff?text=${encodeURIComponent(book.title.slice(0, 15))}`;
  const coverSrc = book.coverUrl || coverFallback;

  return (
    <div className="book-detail-page">
      <button className="btn btn-secondary btn-sm back-btn" onClick={() => navigate(-1)}>
        ← Back to Catalog
      </button>

      <div className="book-detail-card">
        {/* Left: Cover */}
        <div className="book-detail-cover-col">
          <img
            src={coverSrc}
            alt={`Cover of ${book.title}`}
            className="book-detail-cover"
            onError={e => { e.target.src = coverFallback; }}
          />
          <div className="book-detail-availability">
            <span className={`badge ${book.availableCopies > 0 ? 'badge-success' : 'badge-danger'}`}
                  style={{ fontSize: 14, padding: '6px 14px' }}>
              {book.availableCopies > 0
                ? `${book.availableCopies} of ${book.totalCopies} Available`
                : 'All Copies Borrowed'}
            </span>
          </div>
          {currentUserId && (
            <div className="book-detail-actions">
              {book.availableCopies > 0 ? (
                <button className="btn btn-success" style={{ width: '100%' }} onClick={handleBorrow}>
                  📖 Borrow This Book
                </button>
              ) : (
                <button className="btn btn-warning" style={{ width: '100%' }} onClick={handleWaitlist}>
                  ⏳ Join Waiting List
                </button>
              )}
            </div>
          )}
        </div>

        {/* Right: Info */}
        <div className="book-detail-info-col">
          <h1 className="book-detail-title">{book.title}</h1>
          <p className="book-detail-author">by <strong>{book.author}</strong></p>

          <div className="book-detail-meta-grid">
            {book.genre && (
              <div className="meta-item">
                <span className="meta-label">Genre</span>
                <span className="badge badge-info">{book.genre}</span>
              </div>
            )}
            {book.publishedYear && (
              <div className="meta-item">
                <span className="meta-label">Published</span>
                <span className="meta-value">{book.publishedYear}</span>
              </div>
            )}
            {book.isbn && (
              <div className="meta-item">
                <span className="meta-label">ISBN</span>
                <code className="meta-value">{book.isbn}</code>
              </div>
            )}
            <div className="meta-item">
              <span className="meta-label">Total Copies</span>
              <span className="meta-value">{book.totalCopies}</span>
            </div>
          </div>

          {book.description && (
            <div className="book-detail-description">
              <h3>About This Book</h3>
              <p>{book.description}</p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default BookDetail;
