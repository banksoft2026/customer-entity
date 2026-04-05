package com.banking.cbs.customer.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerRiskRequest {

    @Size(max = 10)
    private String amlRiskRating;

    @Size(max = 20)
    private String pepStatus;

    @Size(max = 50)
    private String pepCategory;

    private Boolean sanctionsHit;

    @Size(max = 200)
    private String sanctionsListRef;

    @Size(max = 30)
    private String fatcaStatus;

    @Size(max = 30)
    private String crsStatus;

    @Size(max = 3)
    private String taxResidencyCountry;

    @Size(max = 50)
    private String tinNumber;

    private LocalDate nextReviewDate;

    @Size(max = 36)
    private String lastReviewedBy;

    @NotBlank(message = "createdBy is required")
    @Size(max = 36)
    private String createdBy;

    private String updatedBy;
}
