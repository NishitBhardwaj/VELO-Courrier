import React, { useEffect, useState } from 'react';
import { View, StyleSheet, Text, ActivityIndicator } from 'react-native';
import { api as axios } from '../api/axios';
import { BookingTimelineEvent } from '../types';

interface BookingTimelineProps {
  bookingId: string;
}

export const BookingTimelineComponent: React.FC<BookingTimelineProps> = ({ bookingId }) => {
  const [events, setEvents] = useState<BookingTimelineEvent[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchTimeline = async () => {
      try {
        const response = await axios.get<BookingTimelineEvent[]>(`/bookings/${bookingId}/timeline`);
        // Filter out events not meant for customer if this is the customer app
        // We will just show all true ones here
        setEvents(response.data.filter(e => e.customerVisible));
      } catch (err) {
        console.error('Timeline fetch error', err);
      } finally {
        setLoading(false);
      }
    };

    fetchTimeline();
    // In a real app, STOMP Websocket would append to this array on the fly.
  }, [bookingId]);

  if (loading) return <ActivityIndicator size="small" />;

  return (
    <View style={styles.container}>
      <Text style={styles.header}>Delivery Journey</Text>
      
      {events.map((event, index) => (
        <View key={event.id} style={styles.row}>
          <View style={styles.timelineGraphic}>
            <View style={styles.dot} />
            {index < events.length - 1 && <View style={styles.line} />}
          </View>
          <View style={styles.content}>
            <Text style={styles.eventType}>{event.eventType.replace('_', ' ')}</Text>
            <Text style={styles.time}>{new Date(event.createdAt).toLocaleTimeString()}</Text>
          </View>
        </View>
      ))}

      {events.length === 0 && <Text style={styles.empty}>No timeline events generated yet.</Text>}
    </View>
  );
};

const styles = StyleSheet.create({
  container: { backgroundColor: '#fff', padding: 15, borderRadius: 8, marginTop: 15 },
  header: { fontSize: 18, fontWeight: 'bold', marginBottom: 15 },
  row: { flexDirection: 'row', marginBottom: 10 },
  timelineGraphic: { alignItems: 'center', width: 20, marginRight: 10 },
  dot: { width: 10, height: 10, borderRadius: 5, backgroundColor: '#0055ff', zIndex: 1 },
  line: { width: 2, height: '100%', backgroundColor: '#cbd5e1', position: 'absolute', top: 5 },
  content: { flex: 1, paddingBottom: 15 },
  eventType: { fontSize: 14, fontWeight: 'bold', color: '#1e293b' },
  time: { fontSize: 12, color: '#64748b', marginTop: 3 },
  empty: { color: 'gray', fontStyle: 'italic', fontSize: 12 },
});
