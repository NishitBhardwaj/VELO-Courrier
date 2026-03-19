import React, { useState } from 'react';
import { View, StyleSheet, Text, TextInput, Button, Image, ScrollView, Alert } from 'react-native';
import { api as axios } from './api/axios';

// NOTE: In a real app, `expo-image-picker` and `react-native-signature-canvas` would be installed.
// We are scaffolding the required state and multipart upload payload structure.

interface PODCaptureScreenProps {
  bookingId: string;
  onSuccess: () => void;
}

export const PODCaptureScreen: React.FC<PODCaptureScreenProps> = ({ bookingId, onSuccess }) => {
  const [receiverName, setReceiverName] = useState<string>('');
  const [otp, setOtp] = useState<string>('');
  const [photoUri, setPhotoUri] = useState<string | null>(null);
  const [signatureUri, setSignatureUri] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState<boolean>(false);

  const handleCapturePhoto = () => {
    // Mocking an image picker result
    setPhotoUri('file:///data/user/0/com.velolabs/cache/mock_delivery_photo.jpg');
  };

  const handleCaptureSignature = () => {
    // Mocking a signature pad result
    setSignatureUri('file:///data/user/0/com.velolabs/cache/mock_signature.png');
  };

  const handleSubmitPOD = async () => {
    if (!receiverName || !otp || !photoUri) {
      Alert.alert('Validation Error', 'Receiver Name, OTP, and Photo are mandatory for PoD.');
      return;
    }

    setIsSubmitting(true);
    try {
      const formData = new FormData();
      
      formData.append('receiverName', receiverName);
      formData.append('otp', otp);
      
      formData.append('photo', {
        uri: photoUri,
        name: `photo_${bookingId}.jpg`,
        type: 'image/jpeg'
      } as any);

      if (signatureUri) {
        formData.append('signature', {
          uri: signatureUri,
          name: `signature_${bookingId}.png`,
          type: 'image/png'
        } as any);
      }

      await axios.post(`/bookings/${bookingId}/pod`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      Alert.alert('Success', 'Proof of Delivery Submitted and Booking Completed!');
      onSuccess();
    } catch (error: any) {
      console.error('POD Upload failed:', error);
      Alert.alert('Upload Failed', error.response?.data?.message || 'Failed to submit PoD.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.header}>Proof of Delivery (POD)</Text>
      
      <View style={styles.inputGroup}>
        <Text style={styles.label}>Receiver Name *</Text>
        <TextInput 
          style={styles.input} 
          value={receiverName} 
          onChangeText={setReceiverName} 
          placeholder="e.g. John Doe" 
        />
      </View>

      <View style={styles.inputGroup}>
        <Text style={styles.label}>Delivery OTP *</Text>
        <TextInput 
          style={styles.input} 
          value={otp} 
          onChangeText={setOtp} 
          placeholder="4-digit OTP" 
          keyboardType="numeric" 
          maxLength={4} 
        />
      </View>

      <View style={styles.mediaGroup}>
        <Button title="Capture Photo *" onPress={handleCapturePhoto} />
        {photoUri && <Text style={styles.uriText}>Photo Attached</Text>}
      </View>

      <View style={styles.mediaGroup}>
        <Button title="Capture Signature (Optional)" onPress={handleCaptureSignature} />
        {signatureUri && <Text style={styles.uriText}>Signature Attached</Text>}
      </View>

      <View style={styles.submitContainer}>
        <Button 
          title={isSubmitting ? "Uploading..." : "Submit & Complete Trip"} 
          onPress={handleSubmitPOD} 
          disabled={isSubmitting} 
          color="#0055ff"
        />
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 20, backgroundColor: '#f8fafc' },
  header: { fontSize: 24, fontWeight: 'bold', marginBottom: 20, color: '#0f172a' },
  inputGroup: { marginBottom: 15 },
  label: { fontSize: 14, fontWeight: '600', color: '#475569', marginBottom: 5 },
  input: { borderWidth: 1, borderColor: '#cbd5e1', padding: 12, borderRadius: 8, backgroundColor: '#fff' },
  mediaGroup: { marginVertical: 10, padding: 15, backgroundColor: '#fff', borderRadius: 8, borderWidth: 1, borderColor: '#e2e8f0' },
  uriText: { marginTop: 5, color: 'green', fontSize: 12 },
  submitContainer: { marginTop: 30, marginBottom: 50 },
});
