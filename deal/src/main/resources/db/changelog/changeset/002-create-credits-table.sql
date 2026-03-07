-- liquibase formatted sql

-- changeset developer:3
CREATE TABLE IF NOT EXISTS credits (
    id UUID PRIMARY KEY,
    amount DECIMAL(15,2) NOT NULL,
    term INTEGER NOT NULL,
    monthly_payment DECIMAL(15,2) NOT NULL,
    rate DECIMAL(5,2) NOT NULL,
    psk DECIMAL(5,2) NOT NULL,
    payment_schedule JSONB,
    insurance_enabled BOOLEAN,
    salary_client BOOLEAN,
    credit_status VARCHAR(20) NOT NULL,
    creation_date TIMESTAMP NOT NULL
);

-- changeset developer:4
CREATE INDEX idx_credits_status ON credits(credit_status);