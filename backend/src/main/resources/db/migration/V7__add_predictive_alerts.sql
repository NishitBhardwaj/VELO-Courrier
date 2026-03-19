CREATE TABLE eta_tracking_logs (
    id UUID PRIMARY KEY,
    booking_id UUID NOT NULL REFERENCES bookings(id),
    stop_order INT NOT NULL,
    original_eta INT,
    current_eta INT,
    delay_seconds INT,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE route_deviation_events (
    id UUID PRIMARY KEY,
    booking_id UUID NOT NULL REFERENCES bookings(id),
    driver_id UUID NOT NULL,
    deviation_distance INT,
    severity VARCHAR(50) NOT NULL,
    lat DOUBLE PRECISION,
    lng DOUBLE PRECISION,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE alert_events (
    id UUID PRIMARY KEY,
    type VARCHAR(50) NOT NULL, 
    severity VARCHAR(50) NOT NULL,
    booking_id UUID REFERENCES bookings(id),
    driver_id UUID,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_route_deviation_booking ON route_deviation_events(booking_id);
CREATE INDEX idx_alert_events_type ON alert_events(type);
