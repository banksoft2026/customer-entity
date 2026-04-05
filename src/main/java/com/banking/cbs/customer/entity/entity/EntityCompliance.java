package com.banking.cbs.customer.entity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "entity_compliance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EntityCompliance {

    @Id
    @UuidGenerator
    @Column(name = "compliance_id", length = 36, updatable = false, nullable = false)
    private String complianceId;

    @Column(name = "entity_id", length = 36, nullable = false, unique = true)
    private String entityId;

    @Column(name = "aml_risk_rating", length = 10, nullable = false)
    private String amlRiskRating = "LOW";

    @Column(name = "sanctions_hit", nullable = false)
    private Boolean sanctionsHit = false;

    @Column(name = "sanctions_list_ref", length = 200)
    private String sanctionsListRef;

    @Column(name = "fatca_classification", length = 30)
    private String fatcaClassification;

    @Column(name = "crs_classification", length = 30)
    private String crsClassification;

    @Column(name = "tax_identification_number", length = 50)
    private String taxIdentificationNumber;

    @Column(name = "vat_number", length = 50)
    private String vatNumber;

    @Column(name = "lei_code", length = 20)
    private String leiCode;

    @Column(name = "pep_linked", nullable = false)
    private Boolean pepLinked = false;

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
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
