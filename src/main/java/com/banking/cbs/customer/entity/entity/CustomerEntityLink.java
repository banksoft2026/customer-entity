package com.banking.cbs.customer.entity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "customer_entity_link")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerEntityLink {

    @Id
    @UuidGenerator
    @Column(name = "link_id", length = 36, updatable = false, nullable = false)
    private String linkId;

    @Column(name = "customer_id", length = 36, nullable = false)
    private String customerId;

    @Column(name = "entity_id", length = 36, nullable = false)
    private String entityId;

    @Column(name = "role_type", length = 30, nullable = false)
    private String roleType;

    @Column(name = "ownership_percentage", precision = 5, scale = 2)
    private BigDecimal ownershipPercentage = BigDecimal.ZERO;

    @Column(name = "is_ubo", nullable = false)
    private Boolean isUbo = false;

    @Column(name = "is_authorised_signatory", nullable = false)
    private Boolean isAuthorisedSignatory = false;

    @Column(name = "is_director", nullable = false)
    private Boolean isDirector = false;

    @Column(name = "is_primary_contact", nullable = false)
    private Boolean isPrimaryContact = false;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

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
            this.effectiveFrom = LocalDate.now();
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
