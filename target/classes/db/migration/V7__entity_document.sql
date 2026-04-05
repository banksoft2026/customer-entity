CREATE TABLE entity_document (
    doc_id              VARCHAR(36)  PRIMARY KEY,
    entity_id           VARCHAR(36)  NOT NULL REFERENCES entity_master(entity_id),
    doc_type            VARCHAR(40)  NOT NULL,
    doc_number          VARCHAR(100),
    doc_name            VARCHAR(200),
    issuing_authority   VARCHAR(200),
    issuing_country     VARCHAR(3),
    issue_date          DATE,
    expiry_date         DATE,
    is_mandatory        BOOLEAN      NOT NULL DEFAULT FALSE,
    doc_status          VARCHAR(20)  NOT NULL DEFAULT 'PENDING_VERIFICATION',
    storage_ref         VARCHAR(500),
    verified_by         VARCHAR(36),
    verified_at         TIMESTAMP,
    rejection_reason    VARCHAR(500),
    superseded_by       VARCHAR(36)  REFERENCES entity_document(doc_id),
    created_by          VARCHAR(36)  NOT NULL,
    created_at          TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by          VARCHAR(36),
    updated_at          TIMESTAMP,
    version             INTEGER      NOT NULL DEFAULT 1
);
CREATE INDEX idx_ent_doc_entity ON entity_document(entity_id);
CREATE INDEX idx_ent_doc_status ON entity_document(doc_status);
CREATE INDEX idx_ent_doc_expiry ON entity_document(expiry_date) WHERE doc_status = 'VERIFIED';
