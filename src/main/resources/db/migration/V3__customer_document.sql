CREATE TABLE customer_document (
    doc_id             VARCHAR(36)  PRIMARY KEY,
    customer_id        VARCHAR(36)  NOT NULL REFERENCES customer_master(customer_id),
    doc_type           VARCHAR(40)  NOT NULL,
    doc_number         VARCHAR(100),
    issuing_country    VARCHAR(3),
    issuing_authority  VARCHAR(200),
    issue_date         DATE,
    expiry_date        DATE,
    doc_status         VARCHAR(20)  NOT NULL DEFAULT 'PENDING_VERIFICATION',
    storage_ref        VARCHAR(500),
    is_mandatory       BOOLEAN      NOT NULL DEFAULT FALSE,
    verified_by        VARCHAR(36),
    verified_at        TIMESTAMP,
    rejection_reason   VARCHAR(500),
    created_by         VARCHAR(36)  NOT NULL,
    created_at         TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by         VARCHAR(36),
    updated_at         TIMESTAMP,
    version            INTEGER      NOT NULL DEFAULT 1
);
CREATE INDEX idx_cust_doc_customer ON customer_document(customer_id);
CREATE INDEX idx_cust_doc_status   ON customer_document(doc_status);
CREATE INDEX idx_cust_doc_expiry   ON customer_document(expiry_date) WHERE doc_status = 'VERIFIED';
