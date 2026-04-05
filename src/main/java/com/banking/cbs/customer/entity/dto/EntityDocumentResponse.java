package com.banking.cbs.customer.entity.dto;

import com.banking.cbs.customer.entity.entity.EntityDocument;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class EntityDocumentResponse {

    private String docId;
    private String entityId;
    private String docType;
    private String docNumber;
    private String docName;
    private String issuingAuthority;
    private String issuingCountry;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Boolean isMandatory;
    private String docStatus;
    private String storageRef;
    private String verifiedBy;
    private Instant verifiedAt;
    private String rejectionReason;
    private String supersededBy;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private Integer version;

    public static EntityDocumentResponse from(EntityDocument d) {
        EntityDocumentResponse r = new EntityDocumentResponse();
        r.setDocId(d.getDocId());
        r.setEntityId(d.getEntityId());
        r.setDocType(d.getDocType());
        r.setDocNumber(d.getDocNumber());
        r.setDocName(d.getDocName());
        r.setIssuingAuthority(d.getIssuingAuthority());
        r.setIssuingCountry(d.getIssuingCountry());
        r.setIssueDate(d.getIssueDate());
        r.setExpiryDate(d.getExpiryDate());
        r.setIsMandatory(d.getIsMandatory());
        r.setDocStatus(d.getDocStatus());
        r.setStorageRef(d.getStorageRef());
        r.setVerifiedBy(d.getVerifiedBy());
        r.setVerifiedAt(d.getVerifiedAt());
        r.setRejectionReason(d.getRejectionReason());
        r.setSupersededBy(d.getSupersededBy());
        r.setCreatedBy(d.getCreatedBy());
        r.setCreatedAt(d.getCreatedAt());
        r.setUpdatedBy(d.getUpdatedBy());
        r.setUpdatedAt(d.getUpdatedAt());
        r.setVersion(d.getVersion());
        return r;
    }
}
