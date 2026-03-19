import React, { useState, useEffect } from 'react';
import { CandidateDriverOverlay } from '../components/CandidateDriverOverlay';

export const DispatchQueueScreen: React.FC = () => {
  const [unassignedBookings, setUnassignedBookings] = useState<any[]>([
     // Mocking state
     { id: 'b-123', pickup: '12 Rue de Rivoli', status: 'UNASSIGNED', waitTime: '12m', vehicle: 'BICYCLE' },
     { id: 'b-456', pickup: 'Zone 4 Station', status: 'DELAYED', waitTime: '34m', vehicle: 'CAR' }
  ]);

  const [selectedBookingToAssign, setSelectedBookingToAssign] = useState<any | null>(null);

  // useEffect(() => fetch('/api/v1/admin/dispatch/unassigned-bookings')...), etc.

  return (
    <div style={styles.container}>
      <h2>Manual Dispatch Console</h2>
      <p style={{ color: 'gray' }}>Select an unassigned or anomalous booking to Force-Assign an available driver.</p>

      <table style={styles.table}>
        <thead>
          <tr>
            <th>Booking ID</th>
            <th>Pickup Location</th>
            <th>Required Vehicle</th>
            <th>Current Status</th>
            <th>Wait Time</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {unassignedBookings.map(b => (
            <tr key={b.id} style={styles.row}>
              <td>{b.id}</td>
              <td>{b.pickup}</td>
              <td>{b.vehicle}</td>
              <td><span style={{ color: b.status === 'DELAYED' ? 'red' : 'orange' }}>{b.status}</span></td>
              <td>{b.waitTime}</td>
              <td>
                <button 
                  style={styles.assignBtn}
                  onClick={() => setSelectedBookingToAssign(b)}
                >
                  Find Candidates
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {selectedBookingToAssign && (
        <CandidateDriverOverlay 
           booking={selectedBookingToAssign} 
           onClose={() => setSelectedBookingToAssign(null)} 
           onAssignmentSuccess={(driverId, reason) => {
              alert(`Assigned to ${driverId} for reason: ${reason}`);
              setSelectedBookingToAssign(null);
           }}
        />
      )}
    </div>
  );
};

const styles = {
  container: { padding: '20px', fontFamily: 'sans-serif' },
  table: { width: '100%', borderCollapse: 'collapse' as const, marginTop: 20 },
  row: { borderBottom: '1px solid #e2e8f0', height: 40 },
  assignBtn: { padding: '8px 16px', backgroundColor: '#0f172a', color: 'white', border: 'none', borderRadius: 6, cursor: 'pointer' }
};
