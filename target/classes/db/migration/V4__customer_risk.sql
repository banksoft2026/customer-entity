CREATE TABLE customer_risk (
    risk_id               VARCHAR(36)  PRIMARY KEY,
    customer_id           VARCHAR(36)  NOT NULL UNIQUE REFERENCES customer_master(customer_id),
    aml_risk_rating       VARCHAR(10)  NOT NULL DEFAULT 'LOW',
    pep_status            VARCHAR(20)  NOT NULL DEFAULT 'NOT_PEP',
    pep_category          VARCHAR(50),
    sanctions_hit         BOOLEAN      NOT NULL DEFAULT FALSE,
    sanctions_list_ref    VARCHAR(200),
    fatca_status          VARCHAR(30),
    crs_status            VARCHAR(30),
    tax_residency_country VARCHAR(3),
    tin_number            VARCHAR(50),
    next_review_date      DATE,
    last_reviewed_by      VARCHAR(36),
    last_reviewed_at      TIMESTAMP,
    created_by            VARCHAR(36)  NOT NULL,
    created_at            TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by            VARCHAR(36),
    updated_at            TIMESTAMP,
    version               INTEGER      NOT NULL DEFAULT 1
);
CREATE INDEX idx_risk_customer      ON customer_risk(customer_id);
CREATE INDEX idx_risk_review_date   ON customer_risk(next_review_date);
CREATE INDEX idx_risk_sanctions     ON customer_risk(sanctions_hit) WHERE sanctions_hit = TRUE;
