CREATE TABLE entity_financials (
    financials_id        VARCHAR(36)  PRIMARY KEY,
    entity_id            VARCHAR(36)  NOT NULL REFERENCES entity_master(entity_id),
    financial_year       VARCHAR(4)   NOT NULL,
    annual_turnover      DECIMAL(20,2),
    turnover_currency    VARCHAR(3),
    net_worth            DECIMAL(20,2),
    total_assets         DECIMAL(20,2),
    total_liabilities    DECIMAL(20,2),
    employee_count       INTEGER,
    auditor_name         VARCHAR(200),
    accounts_filed_date  DATE,
    credit_rating        VARCHAR(10),
    credit_rating_agency VARCHAR(50),
    created_by           VARCHAR(36)  NOT NULL,
    created_at           TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_by           VARCHAR(36),
    updated_at           TIMESTAMP,
    version              INTEGER      NOT NULL DEFAULT 1,
    UNIQUE (entity_id, financial_year)
);
CREATE INDEX idx_financials_entity ON entity_financials(entity_id);
