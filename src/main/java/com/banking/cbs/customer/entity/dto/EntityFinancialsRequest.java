package com.banking.cbs.customer.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EntityFinancialsRequest {

    @NotBlank(message = "financialYear is required")
    @Size(max = 4)
    private String financialYear;

    private BigDecimal annualTurnover;

    @Size(max = 3)
    private String turnoverCurrency;

    private BigDecimal netWorth;
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private Integer employeeCount;

    @Size(max = 200)
    private String auditorName;

    private LocalDate accountsFiledDate;

    @Size(max = 10)
    private String creditRating;

    @Size(max = 50)
    private String creditRatingAgency;

    @NotBlank(message = "createdBy is required")
    @Size(max = 36)
    private String createdBy;

    private String updatedBy;
}
