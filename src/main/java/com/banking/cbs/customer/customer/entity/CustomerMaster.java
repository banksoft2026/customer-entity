package com.banking.cbs.customer.customer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "customer_master")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerMaster {

    @Id
    @UuidGenerator
    @Column(name = "customer_id", length = 36, updatable = false, nullable = false)
    private String customerId;

    @Column(name = "customer_number", length = 20, nullable = false, unique = true)
    private String customerNumber;

    @Column(name = "customer_type", length = 30, nullable = false)
    private String customerType;

    @Column(name = "title", length = 10)
    private String title;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "full_name", length = 200)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "nationality", length = 3)
    private String nationality;

    @Column(name = "country_of_birth", length = 3)
    private String countryOfBirth;

    @Column(name = "occupation", length = 100)
    private String occupation;

    @Column(name = "employer_name", length = 200)
    private String employerName;

    @Column(name = "customer_segment", length = 30)
    private String customerSegment;

    @Column(name = "customer_status", length = 20, nullable = false)
    private String customerStatus = "PENDING_VERIFICATION";

    @Column(name = "kyc_status", length = 20, nullable = false)
    private String kycStatus = "PENDING";

    @Column(name = "kyc_review_date")
    private LocalDate kycReviewDate;

    @Column(name = "onboarding_channel", length = 20)
    private String onboardingChannel;

    @Column(name = "onboarding_branch", length = 20)
    private String onboardingBranch;

    @Column(name = "relationship_manager_id", length = 36)
    private String relationshipManagerId;

    @Column(name = "relationship_since")
    private LocalDate relationshipSince;

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
        if (this.customerNumber == null) {
            this.customerNumber = "C-" + System.currentTimeMillis();
        }
        if (this.customerStatus == null) {
            this.customerStatus = "PENDING_VERIFICATION";
        }
        if (this.kycStatus == null) {
            this.kycStatus = "PENDING";
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
