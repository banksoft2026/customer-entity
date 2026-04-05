package com.banking.cbs.customer.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerDocumentRequest {

    @NotBlank(message = "docType is required")
    @Size(max = 40)
    private String docType;

    @Size(max = 100)
    private String docNumber;

    @Size(max = 3)
    private String issuingCountry;

    @Size(max = 200)
    private String issuingAuthority;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    @Size(max = 20)
    private String docStatus;

    @Size(max = 500)
    private String storageRef;

    private Boolean isMandatory = false;

    @Size(max = 36)
    private String verifiedBy;

    @Size(max = 500)
    private String rejectionReason;

    @NotBlank(message = "createdBy is required")
    @Size(max = 36)
    private String createdBy;

    private String updatedBy;
}
