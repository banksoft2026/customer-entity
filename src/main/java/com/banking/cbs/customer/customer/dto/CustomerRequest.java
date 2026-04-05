package com.banking.cbs.customer.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerRequest {

    @NotBlank(message = "customerType is required")
    @Size(max = 30)
    private String customerType;

    @Size(max = 10)
    private String title;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 200)
    private String fullName;

    private LocalDate dateOfBirth;

    @Size(max = 3)
    private String nationality;

    @Size(max = 3)
    private String countryOfBirth;

    @Size(max = 100)
    private String occupation;

    @Size(max = 200)
    private String employerName;

    @Size(max = 30)
    private String customerSegment;

    @Size(max = 20)
    private String customerStatus;

    @Size(max = 20)
    private String kycStatus;

    private LocalDate kycReviewDate;

    @Size(max = 20)
    private String onboardingChannel;

    @Size(max = 20)
    private String onboardingBranch;

    @Size(max = 36)
    private String relationshipManagerId;

    private LocalDate relationshipSince;

    @NotBlank(message = "createdBy is required")
    @Size(max = 36)
    private String createdBy;

    private String updatedBy;

    // Nested sub-resources for onboarding
    private CustomerContactRequest contact;
    private CustomerDocumentRequest document;
    private CustomerRiskRequest risk;
}
