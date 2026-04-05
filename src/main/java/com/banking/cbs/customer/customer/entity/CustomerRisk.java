package com.banking.cbs.customer.customer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "customer_risk")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerRisk {

    @Id
    @UuidGenerator
    @Column(name = "risk_id", length = 36, updatable = false, nullable = false)
    private String riskId;

    @Column(name = "customer_id", length = 36, nullable = false, unique = true)
    private String customerId;

    @Column(name = "aml_risk_rating", length = 10, nullable = false)
    private String amlRiskRating = "LOW";

    @Column(name = "pep_status", length = 20, nullable = false)
    private String pepStatus = "NOT_PEP";

    @Column(name = "pep_category", length = 50)
    private String pepCategory;

    @Column(name = "sanctions_hit", nullable = false)
    private Boolean sanctionsHit = false;

    @Column(name = "sanctions_list_ref", length = 200)
    private String sanctionsListRef;

    @Column(name = "fatca_status", length = 30)
    private String fatcaStatus;

    @Column(name = "crs_status", length = 30)
    private String crsStatus;

    @Column(name = "tax_residency_country", length = 3)
    private String taxResidencyCountry;

    @Column(name = "tin_number", length = 50)
    private String tinNumber;

    @Column(name = "next_review_date")
    private LocalDate nextReviewDate;

    @Column(name = "last_reviewed_by", length = 36)
    private String lastReviewedBy;

    @Column(name = "last_reviewed_at")
    private Instant lastReviewedAt;

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
        if (this.amlRiskRating == null) {
            this.amlRiskRating = "LOW";
        }
        if (this.pepStatus == null) {
            this.pepStatus = "NOT_PEP";
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
