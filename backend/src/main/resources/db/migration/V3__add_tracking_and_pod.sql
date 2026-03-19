-- V3__add_tracking_and_pod.sql

-- Driver Location (Realtime + fallback)
CREATE TABLE driver_locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    driver_id UUID NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    heading DOUBLE PRECISION,
    speed DOUBLE PRECISION,
    accuracy DOUBLE PRECISION,
    recorded_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_driver_locations_driver_id ON driver_locations(driver_id);
CREATE INDEX idx_driver_locations_recorded_at ON driver_locations(recorded_at DESC);

-- Booking Tracking Snapshot (for recovery)
CREATE TABLE booking_tracking_snapshots (
    booking_id UUID PRIMARY KEY,
    driver_id UUID,
    last_lat DOUBLE PRECISION,
    last_lng DOUBLE PRECISION,
    last_updated TIMESTAMP,
    status VARCHAR(50)
);

-- Proof of Delivery (POD)
CREATE TABLE proof_of_delivery (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_id UUID NOT NULL,
    driver_id UUID NOT NULL,
    photo_url TEXT,
    signature_url TEXT,
    otp_verified BOOLEAN DEFAULT false,
    receiver_name VARCHAR(255),
    notes TEXT,
    created_at TIMESTAMP DEFAULT now()
);
