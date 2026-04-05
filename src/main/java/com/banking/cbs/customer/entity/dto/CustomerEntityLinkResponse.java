package com.banking.cbs.customer.entity.dto;

import com.banking.cbs.customer.entity.entity.CustomerEntityLink;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class CustomerEntityLinkResponse {

    private String linkId;
    private String customerId;
    private String entityId;
    private String roleType;
    private BigDecimal ownershipPercentage;
    private Boolean isUbo;
    private Boolean isAuthorisedSignatory;
    private Boolean isDirector;
    private Boolean isPrimaryContact;
    private Boolean isActive;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private Integer version;

    public static CustomerEntityLinkResponse from(CustomerEntityLink l) {
        CustomerEntityLinkResponse r = new CustomerEntityLinkResponse();
        r.setLinkId(l.getLinkId());
        r.setCustomerId(l.getCustomerId());
        r.setEntityId(l.getEntityId());
        r.setRoleType(l.getRoleType());
        r.setOwnershipPercentage(l.getOwnershipPercentage());
        r.setIsUbo(l.getIsUbo());
        r.setIsAuthorisedSignatory(l.getIsAuthorisedSignatory());
        r.setIsDirector(l.getIsDirector());
        r.setIsPrimaryContact(l.getIsPrimaryContact());
        r.setIsActive(l.getIsActive());
        r.setEffectiveFrom(l.getEffectiveFrom());
        r.setEffectiveTo(l.getEffectiveTo());
        r.setCreatedBy(l.getCreatedBy());
        r.setCreatedAt(l.getCreatedAt());
        r.setUpdatedBy(l.getUpdatedBy());
        r.setUpdatedAt(l.getUpdatedAt());
        r.setVersion(l.getVersion());
        return r;
    }
}
