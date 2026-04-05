CREATE TABLE entity_master (
    entity_id              VARCHAR(36)  PRIMARY KEY,
    entity_number          VARCHAR(20)  NOT NULL UNIQUE,
    entity_name            VARCHAR(200) NOT NULL,
    short_name             VARCHAR(50),
    entity_type            VARCHAR(30)  NOT NULL,
    legal_form             VARCHAR(30)  NOT NULL,
    registration_number    VARCHAR(50),
    registration_country   VARCHAR(3)   NOT NULL,
    registration_date      DATE,
    incorporation_date     DATE,
    industry_code          VARCHAR(20),
    sic_code               VARCHAR(10),
    entity_status          VARCHAR(30)  NOT NULL DEFAULT 'PENDING_VERIFICATION',
    kyb_status             VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    kyb_review_date        DATE,
    relationship_manager_id VARCHAR(36),
    onboarding_branch      VARCHAR(20),
    onboarding_channel     VARCHAR(20),
    relationship_since     DATE,
    parent_entity_id       VARCHAR(36)  REFERENCES entity_master(entity_id),
    ultimate_parent_id     VARCHAR(36)  REFERENCES entity_master(entity_id),
    group_structure_flag   BOOLEAN      NOT NULL DEFAULT FALSE,
    created_by             VARCHAR(36)  NOT NULL,
    created_at             TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by             VARCHAR(36),
    updated_at             TIMESTAMP,
    closed_by              VARCHAR(36),
    closed_at              TIMESTAMP,
    close_reason           VARCHAR(200),
    version                INTEGER      NOT NULL DEFAULT 1
);
CREATE INDEX idx_entity_status   ON entity_master(entity_status);
CREATE INDEX idx_entity_type     ON entity_master(entity_type);
CREATE INDEX idx_entity_rm       ON entity_master(relationship_manager_id);
CREATE INDEX idx_entity_parent   ON entity_master(parent_entity_id);
CREATE INDEX idx_entity_upe      ON entity_master(ultimate_parent_id);
CREATE INDEX idx_entity_reg      ON entity_master(registration_number, registration_country);
