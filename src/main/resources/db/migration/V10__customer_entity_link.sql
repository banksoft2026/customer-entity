CREATE TABLE customer_entity_link (
    link_id               VARCHAR(36)  PRIMARY KEY,
    customer_id           VARCHAR(36)  NOT NULL REFERENCES customer_master(customer_id),
    entity_id             VARCHAR(36)  NOT NULL REFERENCES entity_master(entity_id),
    role_type             VARCHAR(30)  NOT NULL,
    ownership_percentage  DECIMAL(5,2) DEFAULT 0.00,
    is_ubo                BOOLEAN      NOT NULL DEFAULT FALSE,
    is_authorised_signatory BOOLEAN    NOT NULL DEFAULT FALSE,
    is_director           BOOLEAN      NOT NULL DEFAULT FALSE,
    is_primary_contact    BOOLEAN      NOT NULL DEFAULT FALSE,
    is_active             BOOLEAN      NOT NULL DEFAULT TRUE,
    effective_from        DATE         NOT NULL,
    effective_to          DATE,
    created_by            VARCHAR(36)  NOT NULL,
    created_at            TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by            VARCHAR(36),
    updated_at            TIMESTAMP,
    version               INTEGER      NOT NULL DEFAULT 1
);
CREATE UNIQUE INDEX idx_link_unique ON customer_entity_link(customer_id, entity_id) WHERE is_active = TRUE;
CREATE INDEX idx_link_customer ON customer_entity_link(customer_id);
CREATE INDEX idx_link_entity   ON customer_entity_link(entity_id);
