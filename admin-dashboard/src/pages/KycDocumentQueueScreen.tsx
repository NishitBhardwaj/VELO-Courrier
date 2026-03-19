import React, { useState, useEffect } from 'react';

export const KycDocumentQueueScreen: React.FC = () => {
  const [queue, setQueue] = useState<any[]>([]);

  useEffect(() => {
    // Scaffold GET /api/v1/admin/kyc/queue
    setTimeout(() => {
      setQueue([
         { docId: 'doc-89a', driverName: 'Marco P.', type: 'VEHICLE_INSURANCE', expiry: '2027-10-01', status: 'PENDING_REVIEW' },
         { docId: 'doc-12b', driverName: 'Emma W.', type: 'DRIVERS_LICENSE', expiry: '2030-01-15', status: 'PENDING_REVIEW' }
      ]);
    }, 500);
  }, []);

  const handleReview = (id: string, action: 'APPROVE' | 'REJECT') => {
    // API Call Scaffold: POST /api/v1/admin/drivers/{driverId}/documents/{docId}/verify
    /*
       Wait for 200 OK.
       Add Security Audit Log entry internally behind controller.
    */
    alert(`Document ${id} cleanly forced to ${action}. Audit Log Generated.`);
    setQueue(prev => prev.filter(q => q.docId !== id));
  };

  return (
    <div style={styles.container}>
      <h2>Governance & KYC Operations</h2>
      <p style={{ color: 'gray' }}>Platform Legal Liability and physical document certification grid.</p>

      <div style={styles.grid}>
         {queue.map(doc => (
            <div key={doc.docId} style={styles.card}>
               <div style={styles.cardHeader}>
                 <h3 style={{ margin: 0 }}>{doc.driverName}</h3>
                 <span style={styles.badge}>{doc.type}</span>
               </div>
               
               {/* Document Image Placeholder mapping MinIO Signed S3 URL natively */}
               <div style={styles.imagePlaceholder}>
                  <p style={{ color: '#94a3b8' }}>*S3 Object Render Space*</p>
                  <p style={{ fontSize: 12 }}>Expires: <strong>{doc.expiry}</strong></p>
               </div>

               <div style={styles.actionRow}>
                  <button onClick={() => handleReview(doc.docId, 'APPROVE')} style={{ ...styles.btn, backgroundColor: '#22c55e' }}>Approve</button>
                  <button onClick={() => handleReview(doc.docId, 'REJECT')} style={{ ...styles.btn, backgroundColor: '#ef4444' }}>Reject</button>
               </div>
            </div>
         ))}

         {queue.length === 0 && <p style={{ color: '#22c55e', fontStyle: 'italic' }}>Zero pending KYC documents. Platform fully compliant.</p>}
      </div>
    </div>
  );
};

const styles = {
  container: { padding: 20, fontFamily: 'sans-serif' },
  grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: 20, marginTop: 20 },
  card: { padding: 20, borderRadius: 8, border: '1px solid #e2e8f0', backgroundColor: 'white', display: 'flex', flexDirection: 'column' as const, gap: 15 },
  cardHeader: { display: 'flex', justifyContent: 'space-between', alignItems: 'center' },
  badge: { fontSize: 10, padding: '4px 8px', backgroundColor: '#f1f5f9', borderRadius: 4, fontWeight: 'bold' },
  imagePlaceholder: { height: 150, backgroundColor: '#f8fafc', display: 'flex', flexDirection: 'column' as const, alignItems: 'center', justifyContent: 'center', border: '1px dashed #cbd5e1', borderRadius: 4 },
  actionRow: { display: 'flex', gap: 10 },
  btn: { flex: 1, padding: '10px 0', border: 'none', borderRadius: 4, color: 'white', fontWeight: 'bold', cursor: 'pointer' }
};
