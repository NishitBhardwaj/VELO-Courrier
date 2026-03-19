export interface StopDto {
  id?: string;
  order: number;
  type: 'PICKUP' | 'DROP_OFF';
  contactName: string;
  contactPhone: string;
  addressJson: string; // Serialized Address
  status?: string;     // PENDING, ARRIVED, COMPLETED, FAILED, SKIPPED
  etaAtCreation?: number;
}

export interface MultiStopBookingRequest {
  serviceType: string;
  stops: StopDto[];
  notes?: string;
}

export interface BookingResponse {
  id: string;
  status: string;
  fareEstimate: number;
  serviceType: string;
  pickupAddress: string;
  dropoffAddress: string;
  customerId: string;
  driverId: string;
  // Sprint 2 metrics
  multiStopEnabled: boolean;
  totalStops: number;
  currentStopOrder: number;
  stops?: StopDto[];
}

export interface BookingTimelineEvent {
  id: string;
  bookingId: string;
  stopId?: string;
  eventType: string;
  actorType: string;
  actorId: string;
  customerVisible: boolean;
  metadata: string;
  createdAt: string;
}

export interface ChatMessage {
  id: string;
  bookingId: string;
  senderId: string;
  receiverId: string;
  message: string;
  isRead: boolean;
  createdAt: string;
}
