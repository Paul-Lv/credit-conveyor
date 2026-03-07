-- liquibase formatted sql

-- changeset developer:5
CREATE TABLE IF NOT EXISTS applications (
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL,
    credit_id UUID,
    status VARCHAR(30) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    applied_offer JSONB,
    status_history JSONB,
    CONSTRAINT fk_application_client FOREIGN KEY (client_id) REFERENCES clients(id),
    CONSTRAINT fk_application_credit FOREIGN KEY (credit_id) REFERENCES credits(id)
);

-- changeset developer:6
CREATE INDEX idx_applications_client ON applications(client_id);
CREATE INDEX idx_applications_status ON applications(status);
CREATE INDEX idx_applications_creation_date ON applications(creation_date);