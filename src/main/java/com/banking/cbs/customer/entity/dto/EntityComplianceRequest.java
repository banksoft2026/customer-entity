package com.banking.cbs.customer.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EntityComplianceRequest {

    @Size(max = 10)
    private String amlRiskRating;

    private Boolean sanctionsHit;

    @Size(max = 200)
    private String sanctionsListRef;

    @Size(max = 30)
    private String fatcaClassification;

    @Size(max = 30)
    private String crsClassification;

    @Size(max = 50)
    private String taxIdentificationNumber;

    @Size(max = 50)
    private String vatNumber;

    @Size(max = 20)
    private String leiCode;

    private Boolean pepLinked;

    private LocalDate nextReviewDate;

    @Size(max = 36)
    private String lastReviewedBy;

    @NotBlank(message = "createdBy is required")
    @Size(max = 36)
    private String createdBy;

    private String updatedBy;
}
