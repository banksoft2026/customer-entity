package com.banking.cbs.customer.entity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "entity_document")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EntityDocument {

    @Id
    @UuidGenerator
    @Column(name = "doc_id", length = 36, updatable = false, nullable = false)
    private String docId;

    @Column(name = "entity_id", length = 36, nullable = false)
    private String entityId;

    @Column(name = "doc_type", length = 40, nullable = false)
    private String docType;

    @Column(name = "doc_number", length = 100)
    private String docNumber;

    @Column(name = "doc_name", length = 200)
    private String docName;

    @Column(name = "issuing_authority", length = 200)
    private String issuingAuthority;

    @Column(name = "issuing_country", length = 3)
    private String issuingCountry;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory = false;

    @Column(name = "doc_status", length = 20, nullable = false)
    private String docStatus = "PENDING_VERIFICATION";

    @Column(name = "storage_ref", length = 500)
    private String storageRef;

    @Column(name = "verified_by", length = 36)
    private String verifiedBy;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    @Column(name = "superseded_by", length = 36)
    private String supersededBy;

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
        if (this.docStatus == null) {
            this.docStatus = "PENDING_VERIFICATION";
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
