import React, { useState, useEffect } from 'react';
import { getUserBorrows, returnBook, getUserWaitingList } from '../api/api';

function MyBorrows({ currentUserId, showToast }) {
  const [borrowRecords, setBorrowRecords] = useState([]);
  const [waitlistEntries, setWaitlistEntries] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchData = async () => {
    setLoading(true);
    try {
      const borrowRes = await getUserBorrows(currentUserId);
      setBorrowRecords(borrowRes.data);

      const waitlistRes = await getUserWaitingList(currentUserId);
      setWaitlistEntries(waitlistRes.data);
    } catch (error) {
      console.error("Error fetching user history:", error);
      showToast("Failed to load your borrow history.", "error");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [currentUserId]);

  const handleReturn = async (recordId, title) => {
    try {
      const res = await returnBook(recordId);
      const returnedRecord = res.data;
      if (returnedRecord.fineAmount > 0) {
        showToast(`Successfully returned '${title}'! Late fine of ₹${returnedRecord.fineAmount.toFixed(2)} incurred.`, "warning");
      } else {
        showToast(`Successfully returned '${title}'!`, "success");
      }
      fetchData();
    } catch (error) {
      const errMsg = error.response?.data?.error || "Failed to return book.";
      showToast(errMsg, "error");
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case 'BORROWED':
        return <span className="badge badge-info">Borrowed</span>;
      case 'OVERDUE':
        return <span className="badge badge-danger">Overdue</span>;
      case 'RETURNED':
        return <span className="badge badge-success">Returned</span>;
      default:
        return <span className="badge">{status}</span>;
    }
  };

  const getWaitlistStatusBadge = (status) => {
    switch (status) {
      case 'WAITING':
        return <span className="badge badge-warning">In Queue</span>;
      case 'NOTIFIED':
        return <span className="badge badge-success">Ready to Claim</span>;
      default:
        return <span className="badge">{status}</span>;
    }
  };

  return (
    <div className="borrows-container">
      <div className="page-title-bar">
        <h2>My Dashboard</h2>
        <p>Monitor your active checkouts, overdue alerts, and waitlist queues.</p>
      </div>

      {loading ? (
        <div className="loading-state">Loading dashboard records...</div>
      ) : (
        <div className="admin-grid" style={{ gridTemplateColumns: '2fr 1fr', gap: '25px' }}>
          {/* Active Borrows Card */}
          <div className="dashboard-card">
            <div className="card-header">
              <h3>📋 Active Checkouts & History</h3>
              <p>Items checked out or returned in the past.</p>
            </div>
            
            {borrowRecords.length === 0 ? (
              <div className="empty-state">No borrow history found for your account.</div>
            ) : (
              <div className="table-responsive">
                <table className="styled-table">
                  <thead>
                    <tr>
                      <th>Title</th>
                      <th>Borrow Date</th>
                      <th>Due Date</th>
                      <th>Fine</th>
                      <th>Status</th>
                      <th>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {borrowRecords.map((record) => (
                      <tr key={record.id}>
                        <td>
                          <div className="book-title-cell">{record.bookTitle}</div>
                        </td>
                        <td>{record.borrowDate}</td>
                        <td>{record.dueDate}</td>
                        <td>
                          {record.fineAmount > 0 ? (
                            <strong style={{ color: '#e74c3c' }}>₹{record.fineAmount.toFixed(2)}</strong>
                          ) : (
                            '₹0.00'
                          )}
                        </td>
                        <td>{getStatusBadge(record.status)}</td>
                        <td>
                          {(record.status === 'BORROWED' || record.status === 'OVERDUE') && (
                            <button
                              className="btn btn-primary btn-sm"
                              onClick={() => handleReturn(record.id, record.bookTitle)}
                            >
                              ↩️ Return
                            </button>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>

          {/* Waitlist Queue Card */}
          <div className="dashboard-card">
            <div className="card-header">
              <h3>⏳ Waitlist Reservations</h3>
              <p>Your current queues for unavailable books.</p>
            </div>

            {waitlistEntries.length === 0 ? (
              <div className="empty-state">You are not currently in any waiting list.</div>
            ) : (
              <div className="waitlist-list" style={{ display: 'flex', flexDirection: 'column', gap: '15px', marginTop: '15px' }}>
                {waitlistEntries.map((entry) => (
                  <div 
                    key={entry.id} 
                    className="waitlist-card-item"
                    style={{ 
                      padding: '15px', 
                      background: '#f8f9fa', 
                      borderRadius: '4px',
                      borderLeft: `4px solid ${entry.status === 'NOTIFIED' ? '#2ecc71' : '#f1c40f'}`
                    }}
                  >
                    <div style={{ fontWeight: 'bold', marginBottom: '5px' }}>{entry.bookTitle}</div>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', fontSize: '13px' }}>
                      <span>
                        {entry.status === 'NOTIFIED' ? (
                          <strong style={{ color: '#2ecc71' }}>⭐ Next in line!</strong>
                        ) : (
                          <span>Queue Position: <strong>#{entry.queuePosition}</strong></span>
                        )}
                      </span>
                      {getWaitlistStatusBadge(entry.status)}
                    </div>
                    {entry.claimDeadline && (
                      <div style={{ fontSize: '11px', color: '#e74c3c', marginTop: '5px', fontWeight: 'bold' }}>
                        Deadline: {new Date(entry.claimDeadline).toLocaleString()}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default MyBorrows;
