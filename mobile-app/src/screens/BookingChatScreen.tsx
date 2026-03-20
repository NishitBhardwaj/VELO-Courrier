import React, { useState, useEffect } from 'react';
import { View, StyleSheet, Text, TextInput, FlatList, TouchableOpacity, KeyboardAvoidingView, Platform, Alert } from 'react-native';
import { api as axios } from '../api/axios';
import { ChatMessage, BookingResponse } from '../types';
import { useAuth } from '../context/AuthContext';
import { useActiveBooking } from '../context/ActiveBookingContext';

export const BookingChatScreen: React.FC = () => {
  const { } = useAuth(); // Extracted context wrapper natively
  const currentUserId = "mock_auth_user_id"; // Scaffold bound natively
  const { activeBooking } = useActiveBooking();
  
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [inputText, setInputText] = useState('');
  const [loading, setLoading] = useState(true);

  const bookingId = activeBooking?.id || "active-booking-uuid-12345";

  useEffect(() => {
    const fetchHistory = async () => {
      try {
        const res = await axios.get<ChatMessage[]>(`/bookings/${bookingId}/chat`);
        setMessages(res.data);
      } catch (err) {
        console.error('Failed to load chat history', err);
      } finally {
        setLoading(false);
      }
    };

    fetchHistory();

    // In a real app, STOMP Websocket listener hooks here:
    /*
      stompClient.subscribe(`/topic/booking/${bookingId}/chat`, (message) => {
        const newMsg = JSON.parse(message.body);
        setMessages(prev => [...prev, newMsg]);
      });
    */
  }, [bookingId]);

  const handleSend = async () => {
    if (!inputText.trim()) return;

    try {
      const res = await axios.post<ChatMessage>(`/bookings/${bookingId}/chat`, { message: inputText });
      setMessages(prev => [...prev, res.data]); // Optimistic append
      setInputText('');
    } catch (err: any) {
      Alert.alert('Cannot Send', err.response?.data?.message || 'Failed to send message');
    }
  };

  const handleMaskedCall = () => {
    Alert.alert('Masked Call Initiated', 'Connecting you securely via proxy router...');
    // Real implementation: POST /calls/initiate then Linking.openURL('tel:...proxy_number')
  };

  const renderItem = ({ item }: { item: ChatMessage }) => {
    const isMe = item.senderId === currentUserId; // Mock match
    return (
      <View style={[styles.bubble, isMe ? styles.myBubble : styles.theirBubble]}>
        <Text style={[styles.messageText, isMe ? styles.myText : styles.theirText]}>{item.message}</Text>
        <Text style={styles.timestamp}>{new Date(item.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</Text>
      </View>
    );
  };

  const isClosed = activeBooking?.status === 'COMPLETED' || activeBooking?.status === 'CANCELED';

  return (
    <KeyboardAvoidingView 
      style={styles.container} 
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <View style={styles.header}>
        <Text style={styles.headerTitle}>Delivery Chat</Text>
        <TouchableOpacity style={styles.callBtn} onPress={handleMaskedCall}>
          <Text style={styles.callText}>📞 Call Setup</Text>
        </TouchableOpacity>
      </View>

      <FlatList
        data={messages}
        keyExtractor={item => item.id}
        renderItem={renderItem}
        contentContainerStyle={styles.listContent}
        inverted={false}
      />

      <View style={styles.inputContainer}>
        {isClosed ? (
           <Text style={styles.closedWarning}>Chat is disabled for closed bookings.</Text>
        ) : (
          <>
            <TextInput
              style={styles.input}
              value={inputText}
              onChangeText={setInputText}
              placeholder="Type a message..."
              placeholderTextColor="#94a3b8"
            />
            <TouchableOpacity style={styles.sendBtn} onPress={handleSend}>
              <Text style={styles.sendText}>Send</Text>
            </TouchableOpacity>
          </>
        )}
      </View>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#f8fafc' },
  header: { flexDirection: 'row', justifyContent: 'space-between', padding: 15, backgroundColor: '#fff', borderBottomWidth: 1, borderBottomColor: '#cbd5e1' },
  headerTitle: { fontSize: 18, fontWeight: 'bold' },
  callBtn: { backgroundColor: '#e2e8f0', paddingHorizontal: 12, paddingVertical: 6, borderRadius: 15 },
  callText: { fontSize: 12, fontWeight: 'bold' },
  listContent: { padding: 15 },
  bubble: { maxWidth: '80%', padding: 12, borderRadius: 16, marginBottom: 10 },
  myBubble: { alignSelf: 'flex-end', backgroundColor: '#0055ff', borderBottomRightRadius: 4 },
  theirBubble: { alignSelf: 'flex-start', backgroundColor: '#e2e8f0', borderBottomLeftRadius: 4 },
  messageText: { fontSize: 16 },
  myText: { color: '#ffffff' },
  theirText: { color: '#1e293b' },
  timestamp: { fontSize: 10, marginTop: 4, alignSelf: 'flex-end', opacity: 0.7, color: 'gray' },
  inputContainer: { flexDirection: 'row', padding: 10, backgroundColor: '#fff', borderTopWidth: 1, borderTopColor: '#cbd5e1' },
  input: { flex: 1, backgroundColor: '#f1f5f9', borderRadius: 20, paddingHorizontal: 15, paddingVertical: 10, fontSize: 16 },
  sendBtn: { justifyContent: 'center', alignItems: 'center', paddingHorizontal: 15 },
  sendText: { color: '#0055ff', fontWeight: 'bold', fontSize: 16 },
  closedWarning: { flex: 1, textAlign: 'center', color: '#ef4444', fontStyle: 'italic', paddingVertical: 10 }
});
