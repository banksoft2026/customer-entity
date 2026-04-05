package com.banking.cbs.customer.customer.dto;

import com.banking.cbs.customer.customer.entity.CustomerDocument;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;

@Data
public class CustomerDocumentResponse {

    private String docId;
    private String customerId;
    private String docType;
    private String docNumber;
    private String issuingCountry;
    private String issuingAuthority;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String docStatus;
    private String storageRef;
    private Boolean isMandatory;
    private String verifiedBy;
    private Instant verifiedAt;
    private String rejectionReason;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private Integer version;

    public static CustomerDocumentResponse from(CustomerDocument d) {
        CustomerDocumentResponse r = new CustomerDocumentResponse();
        r.setDocId(d.getDocId());
        r.setCustomerId(d.getCustomerId());
        r.setDocType(d.getDocType());
        r.setDocNumber(d.getDocNumber());
        r.setIssuingCountry(d.getIssuingCountry());
        r.setIssuingAuthority(d.getIssuingAuthority());
        r.setIssueDate(d.getIssueDate());
        r.setExpiryDate(d.getExpiryDate());
        r.setDocStatus(d.getDocStatus());
        r.setStorageRef(d.getStorageRef());
        r.setIsMandatory(d.getIsMandatory());
        r.setVerifiedBy(d.getVerifiedBy());
        r.setVerifiedAt(d.getVerifiedAt());
        r.setRejectionReason(d.getRejectionReason());
        r.setCreatedBy(d.getCreatedBy());
        r.setCreatedAt(d.getCreatedAt());
        r.setUpdatedBy(d.getUpdatedBy());
        r.setUpdatedAt(d.getUpdatedAt());
        r.setVersion(d.getVersion());
        return r;
    }
}
