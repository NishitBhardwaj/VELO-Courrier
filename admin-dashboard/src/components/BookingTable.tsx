import React, { useState, useEffect } from 'react';

// Example API abstraction for the React Web App
const fetchBookings = async (page: number, size: number, token: string) => {
  const res = await fetch(`http://localhost:8080/api/v1/admin/bookings?page=${page}&size=${size}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
  return res.json();
};

const processRefund = async (bookingId: string, token: string) => {
  await fetch(`http://localhost:8080/api/v1/admin/bookings/${bookingId}/refund`, {
    method: 'POST',
    headers: { Authorization: `Bearer ${token}` }
  });
};

export const BookingTable: React.FC<{ token: string; role: string }> = ({ token, role }) => {
  const [data, setData] = useState<any[]>([]);
  const [page, setPage] = useState(0);

  useEffect(() => {
    fetchBookings(page, 10, token).then(res => setData(res.data.content));
  }, [page, token]);

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Operations Dashboard</h2>
      <table className="min-w-full bg-white border">
        <thead>
          <tr>
            <th className="py-2 px-4 border">ID</th>
            <th className="py-2 px-4 border">Status</th>
            <th className="py-2 px-4 border">Fare</th>
            <th className="py-2 px-4 border">Actions</th>
          </tr>
        </thead>
        <tbody>
          {data.map((booking) => (
            <tr key={booking.id} className="border-t">
              <td className="py-2 px-4">{booking.id.substring(0,8)}</td>
              <td className="py-2 px-4">
                <span className={`px-2 py-1 rounded text-sm ${booking.status === 'DELIVERED' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'}`}>
                  {booking.status}
                </span>
              </td>
              <td className="py-2 px-4">${booking.fare?.toFixed(2)}</td>
              <td className="py-2 px-4 flex gap-2">
                <button className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600">
                  View Map
                </button>
                
                {/* RBAC Evaluation: Only SUPER_ADMIN can process strict refunds */}
                {role === 'SUPER_ADMIN' && booking.status !== 'DELIVERED' && (
                  <button 
                    onClick={() => processRefund(booking.id, token)}
                    className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                  >
                    Force Refund
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="mt-4 flex gap-2">
        <button disabled={page === 0} onClick={() => setPage(page-1)} className="px-4 py-2 border rounded">Prev</button>
        <button onClick={() => setPage(page+1)} className="px-4 py-2 border rounded">Next</button>
      </div>
    </div>
  );
};
