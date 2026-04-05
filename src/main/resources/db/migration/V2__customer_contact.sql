CREATE TABLE customer_contact (
    contact_id        VARCHAR(36)  PRIMARY KEY,
    customer_id       VARCHAR(36)  NOT NULL REFERENCES customer_master(customer_id),
    contact_type      VARCHAR(20)  NOT NULL,
    address_line1     VARCHAR(100),
    address_line2     VARCHAR(100),
    city              VARCHAR(80),
    state_province    VARCHAR(80),
    postal_code       VARCHAR(20),
    country_code      VARCHAR(3)   NOT NULL,
    phone_primary     VARCHAR(30),
    phone_secondary   VARCHAR(30),
    mobile            VARCHAR(30),
    email_primary     VARCHAR(200),
    email_secondary   VARCHAR(200),
    is_primary        BOOLEAN      NOT NULL DEFAULT FALSE,
    is_verified       BOOLEAN      NOT NULL DEFAULT FALSE,
    effective_from    TIMESTAMP    NOT NULL DEFAULT NOW(),
    effective_to      TIMESTAMP,
    created_by        VARCHAR(36)  NOT NULL,
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by        VARCHAR(36),
    updated_at        TIMESTAMP,
    version           INTEGER      NOT NULL DEFAULT 1
);
CREATE INDEX idx_contact_customer ON customer_contact(customer_id);
CREATE INDEX idx_contact_active   ON customer_contact(customer_id) WHERE effective_to IS NULL;
