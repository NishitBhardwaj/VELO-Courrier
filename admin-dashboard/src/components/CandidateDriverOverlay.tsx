import React, { useState, useEffect } from 'react';

export const CandidateDriverOverlay: React.FC<{ booking: any, onClose: () => void, onAssignmentSuccess: (driverId: string, reason: string) => void }> = ({ booking, onClose, onAssignmentSuccess }) => {
  const [candidates, setCandidates] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [overrideReason, setOverrideReason] = useState('');
  const [submittingId, setSubmittingId] = useState<string | null>(null);

  useEffect(() => {
    // Scaffold fetch: GET /api/v1/admin/dispatch/bookings/{booking.id}/candidates
    setTimeout(() => {
      setCandidates([
        { driverId: 'd-789', distanceMeters: 400, etaSeconds: 180, status: 'AVAILABLE' },
        { driverId: 'd-101', distanceMeters: 1200, etaSeconds: 400, status: 'AVAILABLE' }
      ]);
      setLoading(false);
    }, 1000);
  }, [booking.id]);

  const handleForceAssign = async (driverId: string) => {
    if (!overrideReason.trim()) {
      alert("You must provide an administrative reason for overriding the dispatch matrix.");
      return;
    }
    setSubmittingId(driverId);
    
    // API Call Scaffold: POST /api/v1/admin/dispatch/bookings/{booking.id}/assign
    /*
      await axios.post(`/admin/dispatch/bookings/${booking.id}/assign`, {
         adminId: 'admin-uuid', driverId, reason: overrideReason
      });
    */
    
    setTimeout(() => {
       onAssignmentSuccess(driverId, overrideReason);
    }, 500);
  };

  return (
    <div style={styles.overlay}>
       <div style={styles.modal}>
          <div style={styles.header}>
            <h3>Candidates for Booking {booking.id}</h3>
            <button onClick={onClose} style={styles.closeBtn}>✕</button>
          </div>

          <div style={styles.formGroup}>
            <label style={{ fontWeight: 'bold' }}>Require Audit Reason for Assignment Override:</label>
            <input 
              style={styles.input}
              value={overrideReason} 
              onChange={e => setOverrideReason(e.target.value)} 
              placeholder="e.g. VIP Enterprise Rush, Previous Driver flat tire" 
            />
          </div>

          {loading ? (
             <p>Scanning Redis Global Proximity Cache...</p>
          ) : (
            <div style={styles.candidateList}>
               {candidates.map(c => (
                 <div key={c.driverId} style={styles.candidateCard}>
                    <div>
                      <p style={{ margin: 0, fontWeight: 'bold' }}>Driver: {c.driverId}</p>
                      <p style={{ margin: 0, color: 'gray', fontSize: 12 }}>{c.distanceMeters}m away (ETA: {Math.round(c.etaSeconds / 60)} mins)</p>
                    </div>
                    <button 
                      style={styles.forceBtn}
                      disabled={submittingId === c.driverId}
                      onClick={() => handleForceAssign(c.driverId)}
                    >
                      {submittingId === c.driverId ? "Locking..." : "Force Assign"}
                    </button>
                 </div>
               ))}
            </div>
          )}
       </div>
    </div>
  );
};

const styles = {
  overlay: { position: 'fixed' as const, top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center', justifyContent: 'center', zIndex: 1000 },
  modal: { backgroundColor: 'white', width: 600, padding: 20, borderRadius: 8, boxShadow: '0 10px 25px rgba(0,0,0,0.2)' },
  header: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderBottom: '1px solid #e2e8f0', paddingBottom: 10, marginBottom: 15 },
  closeBtn: { background: 'none', border: 'none', fontSize: 20, cursor: 'pointer', color: '#64748b' },
  formGroup: { marginBottom: 20, display: 'flex', flexDirection: 'column' as const, gap: 8 },
  input: { padding: 10, borderRadius: 4, border: '1px solid #cbd5e1', fontSize: 14 },
  candidateList: { display: 'flex', flexDirection: 'column' as const, gap: 10 },
  candidateCard: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: 15, border: '1px solid #e2e8f0', borderRadius: 6, backgroundColor: '#f8fafc' },
  forceBtn: { backgroundColor: '#ef4444', color: 'white', padding: '10px 15px', border: 'none', borderRadius: 4, fontWeight: 'bold', cursor: 'pointer' }
};
