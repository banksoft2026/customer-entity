CREATE TABLE entity_address (
    address_id        VARCHAR(36)  PRIMARY KEY,
    entity_id         VARCHAR(36)  NOT NULL REFERENCES entity_master(entity_id),
    address_type      VARCHAR(20)  NOT NULL,
    address_line1     VARCHAR(100) NOT NULL,
    address_line2     VARCHAR(100),
    address_line3     VARCHAR(100),
    city              VARCHAR(80)  NOT NULL,
    state_province    VARCHAR(80),
    postal_code       VARCHAR(20),
    country_code      VARCHAR(3)   NOT NULL,
    is_primary        BOOLEAN      NOT NULL DEFAULT FALSE,
    is_verified       BOOLEAN      NOT NULL DEFAULT FALSE,
    verified_by       VARCHAR(36),
    verified_on       DATE,
    effective_from    TIMESTAMP    NOT NULL DEFAULT NOW(),
    effective_to      TIMESTAMP,
    created_by        VARCHAR(36)  NOT NULL,
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by        VARCHAR(36),
    updated_at        TIMESTAMP,
    version           INTEGER      NOT NULL DEFAULT 1
);
CREATE INDEX idx_addr_entity  ON entity_address(entity_id);
CREATE INDEX idx_addr_active  ON entity_address(entity_id) WHERE effective_to IS NULL;
CREATE INDEX idx_addr_country ON entity_address(country_code);
