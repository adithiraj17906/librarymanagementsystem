import React, { useState, useEffect } from 'react';
import { Routes, Route, Link, useLocation, useNavigate } from 'react-router-dom';
import './App.css';
import BookList    from './pages/BookList';
import BookDetail  from './pages/BookDetail';
import MyBorrows   from './pages/MyBorrows';
import Notifications from './pages/Notifications';
import AdminBooks  from './pages/AdminBooks';
import Login       from './pages/Login';

function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [toast, setToast] = useState({ show: false, message: '', type: 'success' });

  const location = useLocation();
  const navigate = useNavigate();

  // Restore session
  useEffect(() => {
    const saved = localStorage.getItem('lms_user');
    if (saved) setCurrentUser(JSON.parse(saved));
  }, []);

  const showToast = (message, type = 'success') => {
    setToast({ show: true, message, type });
    setTimeout(() => setToast(p => ({ ...p, show: false })), 4000);
  };

  const handleLogin = (session) => {
    setCurrentUser(session);
    localStorage.setItem('lms_user', JSON.stringify(session));
    showToast(`Welcome, ${session.name}!`, 'success');
    navigate(session.role === 'admin' ? '/admin' : '/');
  };

  const handleLogout = () => {
    setCurrentUser(null);
    localStorage.removeItem('lms_user');
    showToast('Logged out.', 'info');
    navigate('/');
  };

  const ToastEl = () => toast.show
    ? <div className={`toast-notification toast-${toast.type}`}>{toast.message}</div>
    : null;

  // ── Not logged in ───────────────────────────────────────────────────────────
  if (!currentUser) {
    return (
      <div className="login-page-container">
        <ToastEl />
        <Login onLogin={handleLogin} />
      </div>
    );
  }

  const isUser  = currentUser.role === 'user';
  const isAdmin = currentUser.role === 'admin';

  const navLink = (to, label) => (
    <Link
      to={to}
      className={`nav-link ${location.pathname === to || location.pathname.startsWith(to + '/') ? 'active' : ''}`}
    >
      {label}
    </Link>
  );

  // ── Logged in ────────────────────────────────────────────────────────────────
  return (
    <div className="app-container">
      <ToastEl />

      <header className="app-header">
        <div className="brand-section">
          <span className="brand-logo">📚</span>
          <div>
            <h1>Library Management</h1>
            <p className="brand-subtitle">Smart Borrowing &amp; Waitlist System</p>
          </div>
        </div>

        <div className="user-profile-widget">
          <div className="profile-details">
            <span className="profile-name">{currentUser.name}</span>
            <span className={`profile-role role-${currentUser.role}`}>
              {currentUser.role.toUpperCase()}
            </span>
          </div>
          <button className="btn btn-logout btn-sm" onClick={handleLogout}>Logout</button>
        </div>
      </header>

      <nav className="main-nav">
        <div className="nav-links">
          {isUser && (
            <>
              {navLink('/', '📖 Catalog')}
              {navLink('/borrows', '📋 My Borrows')}
              {navLink('/notifications', '🔔 Notifications')}
            </>
          )}
          {isAdmin && navLink('/admin', '⚙️ Admin Dashboard')}
        </div>
      </nav>

      <main className="content-area">
        <Routes>
          {isUser ? (
            <>
              <Route path="/"
                element={<BookList currentUserId={currentUser.id} showToast={showToast} />} />
              <Route path="/books/:id"
                element={<BookDetail currentUserId={currentUser.id} showToast={showToast} />} />
              <Route path="/borrows"
                element={<MyBorrows currentUserId={currentUser.id} showToast={showToast} />} />
              <Route path="/notifications"
                element={<Notifications currentUserId={currentUser.id} showToast={showToast} />} />
              <Route path="*"
                element={<BookList currentUserId={currentUser.id} showToast={showToast} />} />
            </>
          ) : (
            <>
              <Route path="/admin" element={<AdminBooks showToast={showToast} />} />
              <Route path="*"     element={<AdminBooks showToast={showToast} />} />
            </>
          )}
        </Routes>
      </main>
    </div>
  );
}

export default App;
