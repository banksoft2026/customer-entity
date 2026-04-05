package com.banking.cbs.customer.entity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "entity_master")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EntityMaster {

    @Id
    @UuidGenerator
    @Column(name = "entity_id", length = 36, updatable = false, nullable = false)
    private String entityId;

    @Column(name = "entity_number", length = 20, nullable = false, unique = true)
    private String entityNumber;

    @Column(name = "entity_name", length = 200, nullable = false)
    private String entityName;

    @Column(name = "short_name", length = 50)
    private String shortName;

    @Column(name = "entity_type", length = 30, nullable = false)
    private String entityType;

    @Column(name = "legal_form", length = 30, nullable = false)
    private String legalForm;

    @Column(name = "registration_number", length = 50)
    private String registrationNumber;

    @Column(name = "registration_country", length = 3, nullable = false)
    private String registrationCountry;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "incorporation_date")
    private LocalDate incorporationDate;

    @Column(name = "industry_code", length = 20)
    private String industryCode;

    @Column(name = "sic_code", length = 10)
    private String sicCode;

    @Column(name = "entity_status", length = 30, nullable = false)
    private String entityStatus = "PENDING_VERIFICATION";

    @Column(name = "kyb_status", length = 20, nullable = false)
    private String kybStatus = "PENDING";

    @Column(name = "kyb_review_date")
    private LocalDate kybReviewDate;

    @Column(name = "relationship_manager_id", length = 36)
    private String relationshipManagerId;

    @Column(name = "onboarding_branch", length = 20)
    private String onboardingBranch;

    @Column(name = "onboarding_channel", length = 20)
    private String onboardingChannel;

    @Column(name = "relationship_since")
    private LocalDate relationshipSince;

    @Column(name = "parent_entity_id", length = 36)
    private String parentEntityId;

    @Column(name = "ultimate_parent_id", length = 36)
    private String ultimateParentId;

    @Column(name = "group_structure_flag", nullable = false)
    private Boolean groupStructureFlag = false;

    @Column(name = "created_by", length = 36, nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_by", length = 36)
    private String updatedBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "closed_by", length = 36)
    private String closedBy;

    @Column(name = "closed_at")
    private Instant closedAt;

    @Column(name = "close_reason", length = 200)
    private String closeReason;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version;

    @PrePersist
    void onPersist() {
        this.createdAt = Instant.now();
        if (this.entityNumber == null) {
            this.entityNumber = "E-" + System.currentTimeMillis();
        }
        if (this.entityStatus == null) {
            this.entityStatus = "PENDING_VERIFICATION";
        }
        if (this.kybStatus == null) {
            this.kybStatus = "PENDING";
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
