CREATE TYPE user_role AS ENUM ('CUSTOMER', 'DRIVER', 'BUSINESS', 'ADMIN');
CREATE TYPE booking_status AS ENUM ('DRAFT', 'SEARCHING', 'ACCEPTED', 'ARRIVED', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED');
CREATE TYPE payment_status AS ENUM ('PENDING', 'AUTHORIZED', 'CAPTURED', 'FAILED', 'REFUNDED');
CREATE TYPE ticket_status AS ENUM ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED');

CREATE TABLE users (
    id UUID PRIMARY KEY,
    phone VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    password_hash VARCHAR(255),
    role user_role NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE driver_profiles (
    user_id UUID PRIMARY KEY REFERENCES users(id),
    license_number VARCHAR(50),
    is_kyc_verified BOOLEAN DEFAULT FALSE,
    current_status VARCHAR(20) DEFAULT 'OFFLINE'
);

CREATE TABLE vehicle_categories (
    id UUID PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    base_fare DECIMAL(10,2) NOT NULL,
    per_km_rate DECIMAL(10,2) NOT NULL,
    per_min_rate DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE vehicles (
    id UUID PRIMARY KEY,
    driver_id UUID REFERENCES driver_profiles(user_id),
    category_id UUID REFERENCES vehicle_categories(id),
    plate_number VARCHAR(20) UNIQUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE bookings (
    id UUID PRIMARY KEY,
    customer_id UUID REFERENCES users(id),
    service_type VARCHAR(50),
    pickup_address JSONB,
    dropoff_address JSONB,
    status booking_status DEFAULT 'DRAFT',
    driver_id UUID REFERENCES driver_profiles(user_id),
    vehicle_id UUID REFERENCES vehicles(id),
    scheduled_time TIMESTAMP,
    fare_estimate DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE payments (
    id UUID PRIMARY KEY,
    booking_id UUID REFERENCES bookings(id),
    amount DECIMAL(10,2),
    currency VARCHAR(3) DEFAULT 'USD',
    status payment_status,
    gateway_txn_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE wallets (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    balance DECIMAL(15,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE kyc_documents (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    doc_type VARCHAR(50),
    s3_url VARCHAR(255),
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE support_tickets (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    booking_id UUID REFERENCES bookings(id),
    subject VARCHAR(255),
    description TEXT,
    status ticket_status DEFAULT 'OPEN',
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE business_accounts (
    id UUID PRIMARY KEY,
    company_name VARCHAR(100),
    contract_rate_multiplier DECIMAL(3,2) DEFAULT 1.00,
    wallet_id UUID REFERENCES wallets(id),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE team_users (
    id UUID PRIMARY KEY,
    business_id UUID REFERENCES business_accounts(id),
    user_id UUID REFERENCES users(id),
    role_in_company VARCHAR(50), -- e.g. MANAGER, DISPATCHER
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP
);

CREATE TABLE invoices (
    id UUID PRIMARY KEY,
    booking_id UUID REFERENCES bookings(id),
    business_id UUID REFERENCES business_accounts(id),
    total_amount DECIMAL(10,2),
    tax_amount DECIMAL(10,2),
    pdf_url VARCHAR(255),
    issued_at TIMESTAMP DEFAULT NOW()
);
