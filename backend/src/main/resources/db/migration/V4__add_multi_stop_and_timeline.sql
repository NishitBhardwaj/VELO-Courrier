CREATE TABLE booking_stops (
    id UUID PRIMARY KEY,
    booking_id UUID NOT NULL REFERENCES bookings(id),
    stop_order INT NOT NULL,
    stop_type VARCHAR(50) NOT NULL,
    contact_name VARCHAR(100),
    contact_phone VARCHAR(20),
    address_json JSONB,
    status VARCHAR(50) DEFAULT 'PENDING',
    eta_at_creation INT,
    completed_at TIMESTAMP,
    notes TEXT,
    CONSTRAINT uq_booking_stops UNIQUE (booking_id, stop_order)
);

CREATE INDEX idx_booking_stops_booking_id ON booking_stops(booking_id);

CREATE TABLE booking_timeline_events (
    id UUID PRIMARY KEY,
    booking_id UUID NOT NULL REFERENCES bookings(id),
    stop_id UUID REFERENCES booking_stops(id),
    event_type VARCHAR(100) NOT NULL,
    actor_type VARCHAR(50) NOT NULL,
    actor_id UUID NOT NULL,
    customer_visible BOOLEAN DEFAULT false,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT now()
);

ALTER TABLE bookings
ADD COLUMN total_stops INT DEFAULT 1,
ADD COLUMN current_stop_order INT DEFAULT 1,
ADD COLUMN multi_stop_enabled BOOLEAN DEFAULT false,
ADD COLUMN route_distance_meters INT,
ADD COLUMN route_duration_seconds INT;
