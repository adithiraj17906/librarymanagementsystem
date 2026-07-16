import React, { useState } from 'react';
import { loginUser } from '../api/api';

function Login({ onLogin }) {
  const [isAdmin, setIsAdmin] = useState(false);

  // Member login state
  const [email, setEmail]       = useState('');
  const [password, setPassword] = useState('');

  // Admin login state
  const [adminUsername, setAdminUsername] = useState('');
  const [adminPassword, setAdminPassword] = useState('');

  const [error, setError]     = useState('');
  const [loading, setLoading] = useState(false);

  const switchTab = (toAdmin) => {
    setIsAdmin(toAdmin);
    setError('');
    setEmail('');
    setPassword('');
    setAdminUsername('');
    setAdminPassword('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    if (isAdmin) {
      // Hard-coded admin credentials (no DB needed)
      if (adminUsername === 'admin' && adminPassword === 'admin') {
        onLogin({ role: 'admin', id: 0, name: 'Administrator' });
      } else {
        setError('Invalid credentials. Use  username: admin  /  password: admin');
      }
      setLoading(false);
      return;
    }

    // Member: authenticate via backend
    try {
      const res = await loginUser(email, password);
      const user = res.data;
      onLogin({ role: 'user', id: user.id, name: user.name, email: user.email });
    } catch (err) {
      const msg = err.response?.data?.error || 'Login failed. Check your email and password.';
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-wrapper">
      <div className="login-card">

        {/* ── Header ── */}
        <div className="login-header">
          <div style={{ fontSize: 48, marginBottom: 8 }}>📚</div>
          <h2>Library Portal</h2>
          <p>Sign in to access your account</p>
        </div>

        {/* ── Tab Toggle ── */}
        <div className="login-toggle">
          <button
            type="button"
            className={`toggle-btn ${!isAdmin ? 'active' : ''}`}
            onClick={() => switchTab(false)}
          >
            👤 Member Login
          </button>
          <button
            type="button"
            className={`toggle-btn ${isAdmin ? 'active' : ''}`}
            onClick={() => switchTab(true)}
          >
            🔑 Admin Login
          </button>
        </div>

        {/* ── Error Banner ── */}
        {error && <div className="login-error">⚠️ {error}</div>}

        {/* ── Form ── */}
        <form onSubmit={handleSubmit} className="login-form">
          {!isAdmin ? (
            <>
              <div className="form-group">
                <label htmlFor="memberEmail">Email Address</label>
                <input
                  id="memberEmail"
                  type="email"
                  placeholder="you@example.com"
                  value={email}
                  onChange={e => setEmail(e.target.value)}
                  required
                  autoFocus
                />
              </div>
              <div className="form-group">
                <label htmlFor="memberPassword">Password</label>
                <input
                  id="memberPassword"
                  type="password"
                  placeholder="Enter your password"
                  value={password}
                  onChange={e => setPassword(e.target.value)}
                  required
                />
              </div>
              <p style={{ fontSize: 12, color: '#718096', marginTop: 0, marginBottom: 10 }}>
                Don't have an account? Ask an admin to register you.
              </p>
            </>
          ) : (
            <>
              <div className="form-group">
                <label htmlFor="adminUsername">Username</label>
                <input
                  id="adminUsername"
                  type="text"
                  placeholder="admin"
                  value={adminUsername}
                  onChange={e => setAdminUsername(e.target.value)}
                  required
                  autoFocus
                />
              </div>
              <div className="form-group">
                <label htmlFor="adminPassword">Password</label>
                <input
                  id="adminPassword"
                  type="password"
                  placeholder="admin"
                  value={adminPassword}
                  onChange={e => setAdminPassword(e.target.value)}
                  required
                />
              </div>
              <p style={{ fontSize: 12, color: '#718096', marginTop: 0, marginBottom: 10 }}>
                Default credentials: <strong>admin / admin</strong>
              </p>
            </>
          )}

          <button
            type="submit"
            className="btn btn-primary btn-block"
            disabled={loading}
            style={{ marginTop: 6, padding: '13px 0', fontSize: 15 }}
          >
            {loading ? 'Signing in…' : `Sign In as ${isAdmin ? 'Administrator' : 'Member'}`}
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
