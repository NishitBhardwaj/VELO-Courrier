CREATE TABLE ml_training_data (
    booking_id UUID PRIMARY KEY,
    pickup_zone VARCHAR(100),
    drop_zone VARCHAR(100),
    time_of_day TIME,
    driver_assigned UUID,
    eta_seconds INT,
    actual_time_seconds INT,
    delay_seconds INT,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE demand_predictions (
    id UUID PRIMARY KEY,
    zone_id UUID NOT NULL,
    predicted_demand INT NOT NULL,
    time_window TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE dispatch_scores (
    id UUID PRIMARY KEY,
    booking_id UUID NOT NULL,
    driver_id UUID NOT NULL,
    score DECIMAL(4,3) NOT NULL,
    model_version VARCHAR(50),
    created_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_ml_training_zone ON ml_training_data(pickup_zone);
CREATE INDEX idx_demand_zone_time ON demand_predictions(zone_id, time_window);
CREATE INDEX idx_dispatch_score_booking ON dispatch_scores(booking_id);
