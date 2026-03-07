-- liquibase formatted sql

-- changeset developer:1
CREATE TABLE IF NOT EXISTS clients (
    id UUID PRIMARY KEY,
    last_name VARCHAR(30) NOT NULL,
    first_name VARCHAR(30) NOT NULL,
    middle_name VARCHAR(30),
    birth_date DATE NOT NULL,
    email VARCHAR(50) NOT NULL,
    gender VARCHAR(20),
    marital_status VARCHAR(20),
    dependent_amount INTEGER,
    passport_series VARCHAR(4) NOT NULL,
    passport_number VARCHAR(6) NOT NULL,
    passport_issue_date DATE,
    passport_issue_branch VARCHAR(100),
    employment JSONB,
    account VARCHAR(20),
    creation_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP
);

-- changeset developer:2
CREATE INDEX idx_clients_email ON clients(email);
CREATE INDEX idx_clients_passport ON clients(passport_series, passport_number);