package com.banking.cbs.customer.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EntityDocumentRequest {

    @NotBlank(message = "docType is required")
    @Size(max = 40)
    private String docType;

    @Size(max = 100)
    private String docNumber;

    @Size(max = 200)
    private String docName;

    @Size(max = 200)
    private String issuingAuthority;

    @Size(max = 3)
    private String issuingCountry;

    private LocalDate issueDate;
    private LocalDate expiryDate;

    private Boolean isMandatory = false;

    @Size(max = 20)
    private String docStatus;

    @Size(max = 500)
    private String storageRef;

    @Size(max = 36)
    private String verifiedBy;

    @Size(max = 500)
    private String rejectionReason;

    @Size(max = 36)
    private String supersededBy;

    @NotBlank(message = "createdBy is required")
    @Size(max = 36)
    private String createdBy;

    private String updatedBy;
}
