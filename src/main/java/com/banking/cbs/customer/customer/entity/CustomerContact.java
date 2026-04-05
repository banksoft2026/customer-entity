package com.banking.cbs.customer.customer.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;

@Entity
@Table(name = "customer_contact")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerContact {

    @Id
    @UuidGenerator
    @Column(name = "contact_id", length = 36, updatable = false, nullable = false)
    private String contactId;

    @Column(name = "customer_id", length = 36, nullable = false)
    private String customerId;

    @Column(name = "contact_type", length = 20, nullable = false)
    private String contactType;

    @Column(name = "address_line1", length = 100)
    private String addressLine1;

    @Column(name = "address_line2", length = 100)
    private String addressLine2;

    @Column(name = "city", length = 80)
    private String city;

    @Column(name = "state_province", length = 80)
    private String stateProvince;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "country_code", length = 3, nullable = false)
    private String countryCode;

    @Column(name = "phone_primary", length = 30)
    private String phonePrimary;

    @Column(name = "phone_secondary", length = 30)
    private String phoneSecondary;

    @Column(name = "mobile", length = 30)
    private String mobile;

    @Column(name = "email_primary", length = 200)
    private String emailPrimary;

    @Column(name = "email_secondary", length = 200)
    private String emailSecondary;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "effective_from", nullable = false)
    private Instant effectiveFrom;

    @Column(name = "effective_to")
    private Instant effectiveTo;

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
        if (this.effectiveFrom == null) {
            this.effectiveFrom = Instant.now();
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
