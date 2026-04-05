package com.banking.cbs.customer.customer.dto;

import com.banking.cbs.customer.customer.entity.CustomerMaster;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
public class CustomerResponse {

    private String customerId;
    private String customerNumber;
    private String customerType;
    private String title;
    private String firstName;
    private String lastName;
    private String fullName;
    private LocalDate dateOfBirth;
    private String nationality;
    private String countryOfBirth;
    private String occupation;
    private String employerName;
    private String customerSegment;
    private String customerStatus;
    private String kycStatus;
    private LocalDate kycReviewDate;
    private String onboardingChannel;
    private String onboardingBranch;
    private String relationshipManagerId;
    private LocalDate relationshipSince;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private String closedBy;
    private Instant closedAt;
    private String closeReason;
    private Integer version;

    // Sub-resources (populated on full fetch)
    private List<CustomerContactResponse> contacts;
    private List<CustomerDocumentResponse> documents;
    private CustomerRiskResponse risk;

    public static CustomerResponse from(CustomerMaster m) {
        CustomerResponse r = new CustomerResponse();
        r.setCustomerId(m.getCustomerId());
        r.setCustomerNumber(m.getCustomerNumber());
        r.setCustomerType(m.getCustomerType());
        r.setTitle(m.getTitle());
        r.setFirstName(m.getFirstName());
        r.setLastName(m.getLastName());
        r.setFullName(m.getFullName());
        r.setDateOfBirth(m.getDateOfBirth());
        r.setNationality(m.getNationality());
        r.setCountryOfBirth(m.getCountryOfBirth());
        r.setOccupation(m.getOccupation());
        r.setEmployerName(m.getEmployerName());
        r.setCustomerSegment(m.getCustomerSegment());
        r.setCustomerStatus(m.getCustomerStatus());
        r.setKycStatus(m.getKycStatus());
        r.setKycReviewDate(m.getKycReviewDate());
        r.setOnboardingChannel(m.getOnboardingChannel());
        r.setOnboardingBranch(m.getOnboardingBranch());
        r.setRelationshipManagerId(m.getRelationshipManagerId());
        r.setRelationshipSince(m.getRelationshipSince());
        r.setCreatedBy(m.getCreatedBy());
        r.setCreatedAt(m.getCreatedAt());
        r.setUpdatedBy(m.getUpdatedBy());
        r.setUpdatedAt(m.getUpdatedAt());
        r.setClosedBy(m.getClosedBy());
        r.setClosedAt(m.getClosedAt());
        r.setCloseReason(m.getCloseReason());
        r.setVersion(m.getVersion());
        return r;
    }
}
