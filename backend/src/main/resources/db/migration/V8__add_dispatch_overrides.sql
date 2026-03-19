CREATE TABLE dispatch_override_events (
    id UUID PRIMARY KEY,
    booking_id UUID NOT NULL REFERENCES bookings(id),
    previous_driver_id UUID,
    new_driver_id UUID,
    admin_user_id UUID NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    reason TEXT NOT NULL,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT now()
);

-- Adding optimistic locking version control to bookings 
-- to prevent parallel admin "Force Assign" race condition clicks
ALTER TABLE bookings ADD COLUMN version INTEGER DEFAULT 0;

CREATE INDEX idx_dispatch_override_booking ON dispatch_override_events(booking_id);
CREATE INDEX idx_dispatch_override_admin ON dispatch_override_events(admin_user_id);
