CREATE TABLE operational_regions (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    timezone VARCHAR(50) NOT NULL,
    currency_code VARCHAR(10) DEFAULT 'USD',
    base_fare_override DECIMAL(5,2),
    active BOOLEAN DEFAULT true 
);

CREATE TABLE pricing_surge_logs (
    id UUID PRIMARY KEY,
    region_id UUID NOT NULL REFERENCES operational_regions(id),
    multiplier DECIMAL(4,2) NOT NULL,
    triggered_at TIMESTAMP DEFAULT now(),
    expires_at TIMESTAMP
);

CREATE TABLE promo_codes (
    id UUID PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    discount_amount DECIMAL(5,2) NOT NULL,
    type VARCHAR(20) NOT NULL, -- PERCENTAGE, FLAT
    global_limit INT,
    expiry_date TIMESTAMP,
    active BOOLEAN DEFAULT true
);

CREATE TABLE driver_incentives (
    id UUID PRIMARY KEY,
    driver_id UUID NOT NULL,
    campaign_name VARCHAR(100) NOT NULL,
    bonus_amount DECIMAL(6,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING_PAYOUT',
    created_at TIMESTAMP DEFAULT now()
);

-- Associate Bookings strictly to Operational Regions safely
ALTER TABLE bookings ADD COLUMN region_id UUID REFERENCES operational_regions(id);
ALTER TABLE service_zones ADD COLUMN region_id UUID REFERENCES operational_regions(id);

CREATE INDEX idx_region_surge_active ON pricing_surge_logs(region_id, expires_at);
CREATE INDEX idx_driver_incentive ON driver_incentives(driver_id, status);
