INSERT INTO users (id, phone, email, password_hash, role, is_active)
VALUES 
('11111111-1111-1111-1111-111111111111', '+1000000000', 'admin@velocourrier.com', '$2a$12$K...', 'ADMIN', true);

INSERT INTO vehicle_categories (id, name, base_fare, per_km_rate, per_min_rate)
VALUES 
('22222222-2222-2222-2222-222222222221', 'Two-Wheeler / Courier', 5.00, 1.20, 0.50),
('22222222-2222-2222-2222-222222222222', 'Small Truck / Instant', 15.00, 2.50, 0.80),
('22222222-2222-2222-2222-222222222223', 'Large Truck / Movers', 50.00, 5.00, 1.50);
