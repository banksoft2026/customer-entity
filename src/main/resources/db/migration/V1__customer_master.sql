CREATE TABLE customer_master (
    customer_id          VARCHAR(36)  PRIMARY KEY,
    customer_number      VARCHAR(20)  NOT NULL UNIQUE,
    customer_type        VARCHAR(30)  NOT NULL,
    title                VARCHAR(10),
    first_name           VARCHAR(100),
    last_name            VARCHAR(100),
    full_name            VARCHAR(200),
    date_of_birth        DATE,
    nationality          VARCHAR(3),
    country_of_birth     VARCHAR(3),
    occupation           VARCHAR(100),
    employer_name        VARCHAR(200),
    customer_segment     VARCHAR(30),
    customer_status      VARCHAR(20)  NOT NULL DEFAULT 'PENDING_VERIFICATION',
    kyc_status           VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    kyc_review_date      DATE,
    onboarding_channel   VARCHAR(20),
    onboarding_branch    VARCHAR(20),
    relationship_manager_id VARCHAR(36),
    relationship_since   DATE,
    created_by           VARCHAR(36)  NOT NULL,
    created_at           TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by           VARCHAR(36),
    updated_at           TIMESTAMP,
    closed_by            VARCHAR(36),
    closed_at            TIMESTAMP,
    close_reason         VARCHAR(200),
    version              INTEGER      NOT NULL DEFAULT 1
);
CREATE INDEX idx_customer_status   ON customer_master(customer_status);
CREATE INDEX idx_customer_type     ON customer_master(customer_type);
CREATE INDEX idx_customer_segment  ON customer_master(customer_segment);
CREATE INDEX idx_customer_rm       ON customer_master(relationship_manager_id);
CREATE INDEX idx_customer_kyc      ON customer_master(kyc_review_date) WHERE kyc_status = 'VERIFIED';
