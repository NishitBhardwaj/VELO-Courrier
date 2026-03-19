CREATE TABLE service_zones (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city_id VARCHAR(50),
    zone_type VARCHAR(50) NOT NULL,
    polygon_geojson JSONB NOT NULL,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP
);

CREATE TABLE zone_service_rules (
    id UUID PRIMARY KEY,
    zone_id UUID REFERENCES service_zones(id),
    service_type VARCHAR(50),
    booking_allowed BOOLEAN DEFAULT true,
    dispatch_allowed BOOLEAN DEFAULT true,
    surcharge_rule_id UUID
);

CREATE TABLE zone_events (
    id UUID PRIMARY KEY,
    booking_id UUID REFERENCES bookings(id),
    driver_id UUID,
    zone_id UUID REFERENCES service_zones(id),
    event_type VARCHAR(50), 
    metadata JSONB,
    created_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_service_zones_active ON service_zones(active);
CREATE INDEX idx_zone_events_driver ON zone_events(driver_id);
