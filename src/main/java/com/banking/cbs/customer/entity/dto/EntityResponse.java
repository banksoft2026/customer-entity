package com.banking.cbs.customer.entity.dto;

import com.banking.cbs.customer.entity.entity.EntityMaster;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
public class EntityResponse {

    private String entityId;
    private String entityNumber;
    private String entityName;
    private String shortName;
    private String entityType;
    private String legalForm;
    private String registrationNumber;
    private String registrationCountry;
    private LocalDate registrationDate;
    private LocalDate incorporationDate;
    private String industryCode;
    private String sicCode;
    private String entityStatus;
    private String kybStatus;
    private LocalDate kybReviewDate;
    private String relationshipManagerId;
    private String onboardingBranch;
    private String onboardingChannel;
    private LocalDate relationshipSince;
    private String parentEntityId;
    private String ultimateParentId;
    private Boolean groupStructureFlag;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private String closedBy;
    private Instant closedAt;
    private String closeReason;
    private Integer version;

    // Sub-resources
    private List<EntityAddressResponse> addresses;
    private List<EntityDocumentResponse> documents;
    private List<EntityFinancialsResponse> financials;
    private EntityComplianceResponse compliance;
    private List<CustomerEntityLinkResponse> links;

    public static EntityResponse from(EntityMaster m) {
        EntityResponse r = new EntityResponse();
        r.setEntityId(m.getEntityId());
        r.setEntityNumber(m.getEntityNumber());
        r.setEntityName(m.getEntityName());
        r.setShortName(m.getShortName());
        r.setEntityType(m.getEntityType());
        r.setLegalForm(m.getLegalForm());
        r.setRegistrationNumber(m.getRegistrationNumber());
        r.setRegistrationCountry(m.getRegistrationCountry());
        r.setRegistrationDate(m.getRegistrationDate());
        r.setIncorporationDate(m.getIncorporationDate());
        r.setIndustryCode(m.getIndustryCode());
        r.setSicCode(m.getSicCode());
        r.setEntityStatus(m.getEntityStatus());
        r.setKybStatus(m.getKybStatus());
        r.setKybReviewDate(m.getKybReviewDate());
        r.setRelationshipManagerId(m.getRelationshipManagerId());
        r.setOnboardingBranch(m.getOnboardingBranch());
        r.setOnboardingChannel(m.getOnboardingChannel());
        r.setRelationshipSince(m.getRelationshipSince());
        r.setParentEntityId(m.getParentEntityId());
        r.setUltimateParentId(m.getUltimateParentId());
        r.setGroupStructureFlag(m.getGroupStructureFlag());
        r.setCreatedBy(m.getCreatedBy());
        r.setCreatedAt(m.getCreatedAt());
        r.setUpdatedBy(m.getUpdatedBy());
        r.setUpdatedAt(m.getUpdatedAt());
        r.setClosedBy(m.getClosedBy());
        r.setClosedAt(m.getClosedAt());
        r.setCloseReason(m.getCloseReason());
        r.setVersion(m.getVersion());
        return r;
    }
}
