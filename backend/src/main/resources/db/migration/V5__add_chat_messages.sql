CREATE TABLE chat_messages (
    id UUID PRIMARY KEY,
    booking_id UUID NOT NULL REFERENCES bookings(id),
    sender_id UUID NOT NULL,
    receiver_id UUID NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_chat_messages_booking_id_time ON chat_messages(booking_id, created_at DESC);
