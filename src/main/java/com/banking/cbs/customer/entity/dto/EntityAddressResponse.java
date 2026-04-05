package com.banking.cbs.customer.entity.dto;

import com.banking.cbs.customer.entity.entity.EntityAddress;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class EntityAddressResponse {

    private String addressId;
    private String entityId;
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String countryCode;
    private Boolean isPrimary;
    private Boolean isVerified;
    private String verifiedBy;
    private LocalDate verifiedOn;
    private Instant effectiveFrom;
    private Instant effectiveTo;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private Integer version;

    public static EntityAddressResponse from(EntityAddress a) {
        EntityAddressResponse r = new EntityAddressResponse();
        r.setAddressId(a.getAddressId());
        r.setEntityId(a.getEntityId());
        r.setAddressType(a.getAddressType());
        r.setAddressLine1(a.getAddressLine1());
        r.setAddressLine2(a.getAddressLine2());
        r.setAddressLine3(a.getAddressLine3());
        r.setCity(a.getCity());
        r.setStateProvince(a.getStateProvince());
        r.setPostalCode(a.getPostalCode());
        r.setCountryCode(a.getCountryCode());
        r.setIsPrimary(a.getIsPrimary());
        r.setIsVerified(a.getIsVerified());
        r.setVerifiedBy(a.getVerifiedBy());
        r.setVerifiedOn(a.getVerifiedOn());
        r.setEffectiveFrom(a.getEffectiveFrom());
        r.setEffectiveTo(a.getEffectiveTo());
        r.setCreatedBy(a.getCreatedBy());
        r.setCreatedAt(a.getCreatedAt());
        r.setUpdatedBy(a.getUpdatedBy());
        r.setUpdatedAt(a.getUpdatedAt());
        r.setVersion(a.getVersion());
        return r;
    }
}
