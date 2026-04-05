package com.banking.cbs.customer.entity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "entity_financials")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EntityFinancials {

    @Id
    @UuidGenerator
    @Column(name = "financials_id", length = 36, updatable = false, nullable = false)
    private String financialsId;

    @Column(name = "entity_id", length = 36, nullable = false)
    private String entityId;

    @Column(name = "financial_year", length = 4, nullable = false)
    private String financialYear;

    @Column(name = "annual_turnover", precision = 20, scale = 2)
    private BigDecimal annualTurnover;

    @Column(name = "turnover_currency", length = 3)
    private String turnoverCurrency;

    @Column(name = "net_worth", precision = 20, scale = 2)
    private BigDecimal netWorth;

    @Column(name = "total_assets", precision = 20, scale = 2)
    private BigDecimal totalAssets;

    @Column(name = "total_liabilities", precision = 20, scale = 2)
    private BigDecimal totalLiabilities;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "auditor_name", length = 200)
    private String auditorName;

    @Column(name = "accounts_filed_date")
    private LocalDate accountsFiledDate;

    @Column(name = "credit_rating", length = 10)
    private String creditRating;

    @Column(name = "credit_rating_agency", length = 50)
    private String creditRatingAgency;

    @Column(name = "created_by", length = 36, nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_by", length = 36)
    private String updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @PrePersist
    void onPersist() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
