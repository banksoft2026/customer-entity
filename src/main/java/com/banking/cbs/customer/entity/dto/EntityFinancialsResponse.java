package com.banking.cbs.customer.entity.dto;

import com.banking.cbs.customer.entity.entity.EntityFinancials;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Data
public class EntityFinancialsResponse {

    private String financialsId;
    private String entityId;
    private String financialYear;
    private BigDecimal annualTurnover;
    private String turnoverCurrency;
    private BigDecimal netWorth;
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private Integer employeeCount;
    private String auditorName;
    private LocalDate accountsFiledDate;
    private String creditRating;
    private String creditRatingAgency;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private Integer version;

    public static EntityFinancialsResponse from(EntityFinancials f) {
        EntityFinancialsResponse r = new EntityFinancialsResponse();
        r.setFinancialsId(f.getFinancialsId());
        r.setEntityId(f.getEntityId());
        r.setFinancialYear(f.getFinancialYear());
        r.setAnnualTurnover(f.getAnnualTurnover());
        r.setTurnoverCurrency(f.getTurnoverCurrency());
        r.setNetWorth(f.getNetWorth());
        r.setTotalAssets(f.getTotalAssets());
        r.setTotalLiabilities(f.getTotalLiabilities());
        r.setEmployeeCount(f.getEmployeeCount());
        r.setAuditorName(f.getAuditorName());
        r.setAccountsFiledDate(f.getAccountsFiledDate());
        r.setCreditRating(f.getCreditRating());
        r.setCreditRatingAgency(f.getCreditRatingAgency());
        r.setCreatedBy(f.getCreatedBy());
        r.setCreatedAt(f.getCreatedAt());
        r.setUpdatedBy(f.getUpdatedBy());
        r.setUpdatedAt(f.getUpdatedAt());
        r.setVersion(f.getVersion());
        return r;
    }
}
