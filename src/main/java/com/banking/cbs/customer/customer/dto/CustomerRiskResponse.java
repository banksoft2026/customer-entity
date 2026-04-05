package com.banking.cbs.customer.customer.dto;

import com.banking.cbs.customer.customer.entity.CustomerRisk;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class CustomerRiskResponse {

    private String riskId;
    private String customerId;
    private String amlRiskRating;
    private String pepStatus;
    private String pepCategory;
    private Boolean sanctionsHit;
    private String sanctionsListRef;
    private String fatcaStatus;
    private String crsStatus;
    private String taxResidencyCountry;
    private String tinNumber;
    private LocalDate nextReviewDate;
    private String lastReviewedBy;
    private Instant lastReviewedAt;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private Integer version;

    public static CustomerRiskResponse from(CustomerRisk r) {
        CustomerRiskResponse resp = new CustomerRiskResponse();
        resp.setRiskId(r.getRiskId());
        resp.setCustomerId(r.getCustomerId());
        resp.setAmlRiskRating(r.getAmlRiskRating());
        resp.setPepStatus(r.getPepStatus());
        resp.setPepCategory(r.getPepCategory());
        resp.setSanctionsHit(r.getSanctionsHit());
        resp.setSanctionsListRef(r.getSanctionsListRef());
        resp.setFatcaStatus(r.getFatcaStatus());
        resp.setCrsStatus(r.getCrsStatus());
        resp.setTaxResidencyCountry(r.getTaxResidencyCountry());
        resp.setTinNumber(r.getTinNumber());
        resp.setNextReviewDate(r.getNextReviewDate());
        resp.setLastReviewedBy(r.getLastReviewedBy());
        resp.setLastReviewedAt(r.getLastReviewedAt());
        resp.setCreatedBy(r.getCreatedBy());
        resp.setCreatedAt(r.getCreatedAt());
        resp.setUpdatedBy(r.getUpdatedBy());
        resp.setUpdatedAt(r.getUpdatedAt());
        resp.setVersion(r.getVersion());
        return resp;
    }
}
