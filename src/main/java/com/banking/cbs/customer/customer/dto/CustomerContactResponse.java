package com.banking.cbs.customer.customer.dto;

import com.banking.cbs.customer.customer.entity.CustomerContact;
import lombok.Data;

import java.time.Instant;

@Data
public class CustomerContactResponse {

    private String contactId;
    private String customerId;
    private String contactType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String countryCode;
    private String phonePrimary;
    private String phoneSecondary;
    private String mobile;
    private String emailPrimary;
    private String emailSecondary;
    private Boolean isPrimary;
    private Boolean isVerified;
    private Instant effectiveFrom;
    private Instant effectiveTo;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private Integer version;

    public static CustomerContactResponse from(CustomerContact c) {
        CustomerContactResponse r = new CustomerContactResponse();
        r.setContactId(c.getContactId());
        r.setCustomerId(c.getCustomerId());
        r.setContactType(c.getContactType());
        r.setAddressLine1(c.getAddressLine1());
        r.setAddressLine2(c.getAddressLine2());
        r.setCity(c.getCity());
        r.setStateProvince(c.getStateProvince());
        r.setPostalCode(c.getPostalCode());
        r.setCountryCode(c.getCountryCode());
        r.setPhonePrimary(c.getPhonePrimary());
        r.setPhoneSecondary(c.getPhoneSecondary());
        r.setMobile(c.getMobile());
        r.setEmailPrimary(c.getEmailPrimary());
        r.setEmailSecondary(c.getEmailSecondary());
        r.setIsPrimary(c.getIsPrimary());
        r.setIsVerified(c.getIsVerified());
        r.setEffectiveFrom(c.getEffectiveFrom());
        r.setEffectiveTo(c.getEffectiveTo());
        r.setCreatedBy(c.getCreatedBy());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedBy(c.getUpdatedBy());
        r.setUpdatedAt(c.getUpdatedAt());
        r.setVersion(c.getVersion());
        return r;
    }
}
