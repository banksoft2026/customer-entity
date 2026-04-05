package com.banking.cbs.customer.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EntityRequest {

    @NotBlank(message = "entityName is required")
    @Size(max = 200)
    private String entityName;

    @Size(max = 50)
    private String shortName;

    @NotBlank(message = "entityType is required")
    @Size(max = 30)
    private String entityType;

    @NotBlank(message = "legalForm is required")
    @Size(max = 30)
    private String legalForm;

    @Size(max = 50)
    private String registrationNumber;

    @NotBlank(message = "registrationCountry is required")
    @Size(max = 3)
    private String registrationCountry;

    private LocalDate registrationDate;
    private LocalDate incorporationDate;

    @Size(max = 20)
    private String industryCode;

    @Size(max = 10)
    private String sicCode;

    @Size(max = 30)
    private String entityStatus;

    @Size(max = 20)
    private String kybStatus;

    private LocalDate kybReviewDate;

    @Size(max = 36)
    private String relationshipManagerId;

    @Size(max = 20)
    private String onboardingBranch;

    @Size(max = 20)
    private String onboardingChannel;

    private LocalDate relationshipSince;

    @Size(max = 36)
    private String parentEntityId;

    @Size(max = 36)
    private String ultimateParentId;

    @NotBlank(message = "createdBy is required")
    @Size(max = 36)
    private String createdBy;

    private String updatedBy;

    // Nested sub-resources for onboarding
    private EntityAddressRequest address;
    private EntityDocumentRequest document;
    private EntityFinancialsRequest financials;
    private EntityComplianceRequest compliance;
    private CustomerEntityLinkRequest link;
}
