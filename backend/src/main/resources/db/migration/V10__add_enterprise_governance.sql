CREATE TABLE enterprise_contracts (
    id UUID PRIMARY KEY,
    customer_user_id UUID NOT NULL,
    discount_percentage DECIMAL(5,2) DEFAULT 0.00,
    sla_priority_tier VARCHAR(20) DEFAULT 'STANDARD',
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE sla_breach_events (
    id UUID PRIMARY KEY,
    booking_id UUID NOT NULL REFERENCES bookings(id),
    contract_id UUID REFERENCES enterprise_contracts(id),
    breach_type VARCHAR(50) NOT NULL,
    severity_level VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE driver_documents (
    id UUID PRIMARY KEY,
    driver_id UUID NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    file_url VARCHAR(255) NOT NULL,
    expiry_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING_REVIEW',
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE security_audit_logs (
    id UUID PRIMARY KEY,
    actor_id UUID NOT NULL,
    target_entity VARCHAR(50),
    target_id UUID,
    action VARCHAR(50) NOT NULL,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT now()
);

CREATE INDEX idx_enterprise_customer ON enterprise_contracts(customer_user_id);
CREATE INDEX idx_driver_document_driver ON driver_documents(driver_id);
CREATE INDEX idx_document_expiry ON driver_documents(expiry_date);
