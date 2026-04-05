CREATE TABLE entity_compliance (
    compliance_id           VARCHAR(36)  PRIMARY KEY,
    entity_id               VARCHAR(36)  NOT NULL UNIQUE REFERENCES entity_master(entity_id),
    aml_risk_rating         VARCHAR(10)  NOT NULL DEFAULT 'LOW',
    sanctions_hit           BOOLEAN      NOT NULL DEFAULT FALSE,
    sanctions_list_ref      VARCHAR(200),
    fatca_classification    VARCHAR(30),
    crs_classification      VARCHAR(30),
    tax_identification_number VARCHAR(50),
    vat_number              VARCHAR(50),
    lei_code                VARCHAR(20),
    pep_linked              BOOLEAN      NOT NULL DEFAULT FALSE,
    next_review_date        DATE,
    last_reviewed_by        VARCHAR(36),
    last_reviewed_at        TIMESTAMP,
    created_by              VARCHAR(36)  NOT NULL,
    created_at              TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by              VARCHAR(36),
    updated_at              TIMESTAMP,
    version                 INTEGER      NOT NULL DEFAULT 1
);
CREATE INDEX idx_compliance_entity       ON entity_compliance(entity_id);
CREATE INDEX idx_compliance_review_date  ON entity_compliance(next_review_date);
CREATE INDEX idx_compliance_sanctions    ON entity_compliance(sanctions_hit) WHERE sanctions_hit = TRUE;
