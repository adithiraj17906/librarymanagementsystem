import React, { useState, useEffect } from 'react';
import { getNotifications, markNotificationAsRead, getUserWaitingList, claimBook } from '../api/api';

function Notifications({ currentUserId, showToast }) {
  const [notifications, setNotifications] = useState([]);
  const [notifiedEntries, setNotifiedEntries] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchData = async () => {
    setLoading(true);
    try {
      const notifRes = await getNotifications(currentUserId);
      setNotifications(notifRes.data);

      const waitlistRes = await getUserWaitingList(currentUserId);
      const notified = waitlistRes.data.filter(entry => entry.status === 'NOTIFIED');
      setNotifiedEntries(notified);
    } catch (error) {
      console.error("Error fetching notification details:", error);
      showToast("Failed to load alerts.", "error");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [currentUserId]);

  const handleMarkAsRead = async (notificationId) => {
    try {
      await markNotificationAsRead(notificationId);
      fetchData();
    } catch (error) {
      console.error(error);
    }
  };

  const handleClaim = async (entryId, bookTitle) => {
    try {
      await claimBook(entryId);
      showToast(`Successfully claimed '${bookTitle}'! The copy is now checked out.`, "success");
      fetchData();
    } catch (error) {
      const errMsg = error.response?.data?.error || "Failed to claim book.";
      showToast(errMsg, "error");
    }
  };

  const getNotificationTypeLabel = (type) => {
    switch (type) {
      case 'DUE_REMINDER':
        return <span className="badge badge-warning">🕒 Due Reminder</span>;
      case 'FINE_UPDATE':
        return <span className="badge badge-danger">💸 Fine Charge</span>;
      case 'BOOK_AVAILABLE':
        return <span className="badge badge-success">⭐ Book Ready</span>;
      default:
        return <span className="badge badge-info">{type}</span>;
    }
  };

  return (
    <div className="notifications-container">
      <div className="page-title-bar">
        <h2>My Notifications</h2>
        <p>Stay up to date with returns, overdue notices, and waiting list readiness.</p>
      </div>

      {loading ? (
        <div className="loading-state">Loading system messages...</div>
      ) : (
        <>
          {/* Actionable Reservations Section */}
          {notifiedEntries.length > 0 && (
            <div className="dashboard-card" style={{ borderColor: '#2ecc71', backgroundColor: '#f9fffb', marginBottom: '25px' }}>
              <div className="card-header">
                <h3 style={{ color: '#27ae60', margin: 0 }}>🎉 Ready for Claim (Action Required)</h3>
                <p>Reserved book copies ready for checkout. Claim them before they expire!</p>
              </div>
              <div className="table-responsive" style={{ marginTop: '15px' }}>
                <table className="styled-table">
                  <thead>
                    <tr>
                      <th>Book Title</th>
                      <th>Notified At</th>
                      <th>Claim Deadline</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {notifiedEntries.map((entry) => (
                      <tr key={entry.id}>
                        <td><strong>{entry.bookTitle}</strong></td>
                        <td>{new Date(entry.notifiedAt).toLocaleString()}</td>
                        <td style={{ color: '#e74c3c', fontWeight: 'bold' }}>
                          {new Date(entry.claimDeadline).toLocaleString()}
                        </td>
                        <td>
                          <button
                            className="btn btn-success btn-sm"
                            onClick={() => handleClaim(entry.id, entry.bookTitle)}
                          >
                            📥 Claim & Checkout
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {/* System Notifications List */}
          <div className="dashboard-card">
            <div className="card-header" style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '20px' }}>
              <div>
                <h3>System Notifications</h3>
                <p>List of all reminders and account updates.</p>
              </div>
              <button className="btn btn-secondary btn-sm" onClick={fetchData}>
                🔄 Refresh
              </button>
            </div>

            {notifications.length === 0 ? (
              <div className="empty-state">No notifications recorded.</div>
            ) : (
              <div className="notification-list">
                {notifications.map((notif) => (
                  <div
                    key={notif.id}
                    className={`notification-item ${!notif.isRead ? 'unread' : ''}`}
                    onClick={() => !notif.isRead && handleMarkAsRead(notif.id)}
                  >
                    <div className="notification-content">
                      <div className="notif-header-line" style={{ marginBottom: '8px', display: 'flex', alignItems: 'center', gap: '10px' }}>
                        {getNotificationTypeLabel(notif.type)}
                        <span className="notification-time">
                          {new Date(notif.createdAt).toLocaleString()}
                        </span>
                      </div>
                      <div className="notif-body-text">{notif.message}</div>
                    </div>
                    <div className="notification-action">
                      {!notif.isRead ? (
                        <span className="btn btn-sm btn-primary" style={{ fontSize: '11px', padding: '4px 8px' }}>
                          Mark Read
                        </span>
                      ) : (
                        <span style={{ fontSize: '12px', color: '#bdc3c7' }}>Read</span>
                      )}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
}

export default Notifications;
