package com.banking.cbs.customer.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CustomerEntityLinkRequest {

    @NotBlank(message = "customerId is required")
    @Size(max = 36)
    private String customerId;

    @NotBlank(message = "roleType is required")
    @Size(max = 30)
    private String roleType;

    private BigDecimal ownershipPercentage;
    private Boolean isUbo = false;
    private Boolean isAuthorisedSignatory = false;
    private Boolean isDirector = false;
    private Boolean isPrimaryContact = false;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    @NotBlank(message = "createdBy is required")
    @Size(max = 36)
    private String createdBy;

    private String updatedBy;
}
