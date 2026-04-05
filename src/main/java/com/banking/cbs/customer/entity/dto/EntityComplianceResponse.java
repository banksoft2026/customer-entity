package com.banking.cbs.customer.entity.dto;

import com.banking.cbs.customer.entity.entity.EntityCompliance;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class EntityComplianceResponse {

    private String complianceId;
    private String entityId;
    private String amlRiskRating;
    private Boolean sanctionsHit;
    private String sanctionsListRef;
    private String fatcaClassification;
    private String crsClassification;
    private String taxIdentificationNumber;
    private String vatNumber;
    private String leiCode;
    private Boolean pepLinked;
    private LocalDate nextReviewDate;
    private String lastReviewedBy;
    private Instant lastReviewedAt;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private Integer version;

    public static EntityComplianceResponse from(EntityCompliance c) {
        EntityComplianceResponse r = new EntityComplianceResponse();
        r.setComplianceId(c.getComplianceId());
        r.setEntityId(c.getEntityId());
        r.setAmlRiskRating(c.getAmlRiskRating());
        r.setSanctionsHit(c.getSanctionsHit());
        r.setSanctionsListRef(c.getSanctionsListRef());
        r.setFatcaClassification(c.getFatcaClassification());
        r.setCrsClassification(c.getCrsClassification());
        r.setTaxIdentificationNumber(c.getTaxIdentificationNumber());
        r.setVatNumber(c.getVatNumber());
        r.setLeiCode(c.getLeiCode());
        r.setPepLinked(c.getPepLinked());
        r.setNextReviewDate(c.getNextReviewDate());
        r.setLastReviewedBy(c.getLastReviewedBy());
        r.setLastReviewedAt(c.getLastReviewedAt());
        r.setCreatedBy(c.getCreatedBy());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedBy(c.getUpdatedBy());
        r.setUpdatedAt(c.getUpdatedAt());
        r.setVersion(c.getVersion());
        return r;
    }
}
