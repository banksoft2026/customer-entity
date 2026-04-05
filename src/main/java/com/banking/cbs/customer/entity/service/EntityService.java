package com.banking.cbs.customer.entity.service;

import com.banking.cbs.customer.common.exception.CbsException;
import com.banking.cbs.customer.customer.repository.CustomerMasterRepository;
import com.banking.cbs.customer.entity.dto.*;
import com.banking.cbs.customer.entity.entity.*;
import com.banking.cbs.customer.entity.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntityService {

    private final EntityMasterRepository entityRepo;
    private final EntityAddressRepository addressRepo;
    private final EntityDocumentRepository documentRepo;
    private final EntityFinancialsRepository financialsRepo;
    private final EntityComplianceRepository complianceRepo;
    private final CustomerEntityLinkRepository linkRepo;
    private final CustomerMasterRepository customerRepo;

    @Transactional
    public EntityResponse onboard(EntityRequest req) {
        // Validate entityType + legalForm combo
        validateEntityTypeLegalForm(req.getEntityType(), req.getLegalForm());

        // Validate registration number uniqueness per country
        if (req.getRegistrationNumber() != null && !req.getRegistrationNumber().isBlank()) {
            entityRepo.findByRegistrationNumberAndRegistrationCountry(req.getRegistrationNumber(), req.getRegistrationCountry())
                    .ifPresent(existing -> {
                        throw CbsException.conflict("DUPLICATE_REGISTRATION",
                                "Entity with registration number " + req.getRegistrationNumber() + " already exists in " + req.getRegistrationCountry());
                    });
        }

        // Validate parent
        boolean groupFlag = false;
        if (req.getParentEntityId() != null) {
            EntityMaster parent = entityRepo.findById(req.getParentEntityId())
                    .orElseThrow(() -> CbsException.notFound("PARENT_NOT_FOUND", "Parent entity not found: " + req.getParentEntityId()));
            if (!"ACTIVE".equals(parent.getEntityStatus())) {
                throw CbsException.unprocessable("PARENT_NOT_ACTIVE", "Parent entity must be ACTIVE");
            }
            groupFlag = true;
        }

        EntityMaster entity = EntityMaster.builder()
                .entityName(req.getEntityName())
                .shortName(req.getShortName())
                .entityType(req.getEntityType())
                .legalForm(req.getLegalForm())
                .registrationNumber(req.getRegistrationNumber())
                .registrationCountry(req.getRegistrationCountry())
                .registrationDate(req.getRegistrationDate())
                .incorporationDate(req.getIncorporationDate())
                .industryCode(req.getIndustryCode())
                .sicCode(req.getSicCode())
                .entityStatus("PENDING_VERIFICATION")
                .kybStatus("PENDING")
                .relationshipManagerId(req.getRelationshipManagerId())
                .onboardingBranch(req.getOnboardingBranch())
                .onboardingChannel(req.getOnboardingChannel())
                .relationshipSince(req.getRelationshipSince())
                .parentEntityId(req.getParentEntityId())
                .ultimateParentId(req.getUltimateParentId())
                .groupStructureFlag(groupFlag)
                .createdBy(req.getCreatedBy())
                .build();
        entity = entityRepo.save(entity);

        if (req.getAddress() != null) addAddress(entity.getEntityId(), req.getAddress());
        if (req.getDocument() != null) addDocument(entity.getEntityId(), req.getDocument());
        if (req.getFinancials() != null) putFinancials(entity.getEntityId(), req.getFinancials());
        if (req.getCompliance() != null) putCompliance(entity.getEntityId(), req.getCompliance());
        if (req.getLink() != null) linkCustomer(entity.getEntityId(), req.getLink());

        return EntityResponse.from(entity);
    }

    private void validateEntityTypeLegalForm(String entityType, String legalForm) {
        boolean valid = switch (entityType) {
            case "PRIVATE_LIMITED" -> "LTD".equals(legalForm);
            case "PUBLIC_LIMITED" -> "PLC".equals(legalForm);
            case "LLP" -> "LLP".equals(legalForm);
            case "PARTNERSHIP" -> "PARTNERSHIP".equals(legalForm) || "OTHER".equals(legalForm);
            case "SOLE_TRADER", "TRUST", "SPV", "NGO", "GOVERNMENT", "BRANCH", "COOPERATIVE" -> true;
            default -> true;
        };
        if (!valid) {
            throw CbsException.badRequest("INVALID_LEGAL_FORM",
                    "Legal form " + legalForm + " is not valid for entity type " + entityType);
        }
    }

    @Transactional(readOnly = true)
    public EntityResponse get(String entityId) {
        EntityMaster entity = findEntityOrThrow(entityId);
        EntityResponse resp = EntityResponse.from(entity);
        resp.setAddresses(addressRepo.findByEntityIdAndEffectiveToIsNull(entityId)
                .stream().map(EntityAddressResponse::from).collect(Collectors.toList()));
        resp.setDocuments(documentRepo.findByEntityId(entityId)
                .stream().map(EntityDocumentResponse::from).collect(Collectors.toList()));
        resp.setFinancials(financialsRepo.findByEntityIdOrderByFinancialYearDesc(entityId)
                .stream().map(EntityFinancialsResponse::from).collect(Collectors.toList()));
        complianceRepo.findByEntityId(entityId).ifPresent(c -> resp.setCompliance(EntityComplianceResponse.from(c)));
        resp.setLinks(linkRepo.findByEntityIdAndIsActiveTrue(entityId)
                .stream().map(CustomerEntityLinkResponse::from).collect(Collectors.toList()));
        return resp;
    }

    @Transactional(readOnly = true)
    public EntityResponse getSummary(String entityId) {
        return EntityResponse.from(findEntityOrThrow(entityId));
    }

    @Transactional
    public EntityResponse patch(String entityId, EntityRequest req) {
        EntityMaster entity = findEntityOrThrow(entityId);
        if (req.getEntityName() != null) entity.setEntityName(req.getEntityName());
        if (req.getShortName() != null) entity.setShortName(req.getShortName());
        if (req.getIndustryCode() != null) entity.setIndustryCode(req.getIndustryCode());
        if (req.getSicCode() != null) entity.setSicCode(req.getSicCode());
        if (req.getKybStatus() != null) entity.setKybStatus(req.getKybStatus());
        if (req.getKybReviewDate() != null) entity.setKybReviewDate(req.getKybReviewDate());
        if (req.getRelationshipManagerId() != null) entity.setRelationshipManagerId(req.getRelationshipManagerId());
        if (req.getOnboardingBranch() != null) entity.setOnboardingBranch(req.getOnboardingBranch());
        if (req.getOnboardingChannel() != null) entity.setOnboardingChannel(req.getOnboardingChannel());
        if (req.getRelationshipSince() != null) entity.setRelationshipSince(req.getRelationshipSince());
        if (req.getParentEntityId() != null) entity.setParentEntityId(req.getParentEntityId());
        if (req.getUltimateParentId() != null) entity.setUltimateParentId(req.getUltimateParentId());
        if (req.getUpdatedBy() != null) entity.setUpdatedBy(req.getUpdatedBy());
        return EntityResponse.from(entityRepo.save(entity));
    }

    @Transactional
    public EntityResponse updateStatus(String entityId, String newStatus, String reason) {
        EntityMaster entity = findEntityOrThrow(entityId);
        String current = entity.getEntityStatus();
        if ("CLOSED".equals(current) || "BLACKLISTED".equals(current)) {
            throw CbsException.unprocessable("TERMINAL_STATUS", "Cannot change status from terminal state: " + current);
        }
        validateEntityStatusTransition(current, newStatus, entity);
        if ("ACTIVE".equals(newStatus) && "PENDING_VERIFICATION".equals(current)) {
            if (!"VERIFIED".equals(entity.getKybStatus())) {
                throw CbsException.unprocessable("KYB_REQUIRED", "KYB must be VERIFIED before activating entity");
            }
        }
        if ("CLOSED".equals(newStatus)) {
            entity.setClosedAt(Instant.now());
            entity.setCloseReason(reason);
        }
        entity.setEntityStatus(newStatus);
        return EntityResponse.from(entityRepo.save(entity));
    }

    private void validateEntityStatusTransition(String from, String to, EntityMaster e) {
        boolean valid = switch (from) {
            case "PENDING_VERIFICATION" -> "ACTIVE".equals(to) || "FAILED".equals(to) || "CLOSED".equals(to);
            case "ACTIVE" -> "SUSPENDED".equals(to) || "DORMANT".equals(to) || "CLOSED".equals(to);
            case "SUSPENDED" -> "ACTIVE".equals(to) || "CLOSED".equals(to);
            case "DORMANT" -> "ACTIVE".equals(to) || "CLOSED".equals(to);
            default -> false;
        };
        if (!valid) {
            throw CbsException.unprocessable("INVALID_TRANSITION",
                    "Cannot transition from " + from + " to " + to);
        }
    }

    @Transactional(readOnly = true)
    public List<EntityResponse> list(String status, String entityType, String registrationCountry, String rmId) {
        List<EntityMaster> all = entityRepo.findAll();
        return all.stream()
                .filter(e -> status == null || status.equals(e.getEntityStatus()))
                .filter(e -> entityType == null || entityType.equals(e.getEntityType()))
                .filter(e -> registrationCountry == null || registrationCountry.equals(e.getRegistrationCountry()))
                .filter(e -> rmId == null || rmId.equals(e.getRelationshipManagerId()))
                .map(EntityResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EntityResponse> getGroupStructure(String entityId) {
        List<EntityResponse> chain = new ArrayList<>();
        String currentId = entityId;
        int maxDepth = 10;
        int depth = 0;
        while (currentId != null && depth < maxDepth) {
            EntityMaster entity = entityRepo.findById(currentId).orElse(null);
            if (entity == null) break;
            chain.add(EntityResponse.from(entity));
            currentId = entity.getParentEntityId();
            depth++;
        }
        return chain;
    }

    @Transactional
    public EntityResponse softClose(String entityId) {
        EntityMaster entity = findEntityOrThrow(entityId);
        entity.setEntityStatus("CLOSED");
        entity.setClosedAt(Instant.now());
        return EntityResponse.from(entityRepo.save(entity));
    }

    // ── Addresses ────────────────────────────────────────────────────────────

    @Transactional
    public EntityAddressResponse addAddress(String entityId, EntityAddressRequest req) {
        findEntityOrThrow(entityId);
        if (Boolean.TRUE.equals(req.getIsPrimary())) {
            addressRepo.findByEntityIdAndAddressTypeAndIsPrimaryTrueAndEffectiveToIsNull(entityId, req.getAddressType())
                    .ifPresent(existing -> {
                        existing.setEffectiveTo(Instant.now());
                        addressRepo.save(existing);
                    });
        }
        EntityAddress address = EntityAddress.builder()
                .entityId(entityId)
                .addressType(req.getAddressType())
                .addressLine1(req.getAddressLine1())
                .addressLine2(req.getAddressLine2())
                .addressLine3(req.getAddressLine3())
                .city(req.getCity())
                .stateProvince(req.getStateProvince())
                .postalCode(req.getPostalCode())
                .countryCode(req.getCountryCode())
                .isPrimary(req.getIsPrimary() != null ? req.getIsPrimary() : false)
                .isVerified(false)
                .createdBy(req.getCreatedBy())
                .build();
        return EntityAddressResponse.from(addressRepo.save(address));
    }

    @Transactional(readOnly = true)
    public List<EntityAddressResponse> listAddresses(String entityId) {
        findEntityOrThrow(entityId);
        return addressRepo.findByEntityIdAndEffectiveToIsNull(entityId)
                .stream().map(EntityAddressResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public EntityAddressResponse updateAddress(String entityId, String addressId, EntityAddressRequest req) {
        findEntityOrThrow(entityId);
        EntityAddress address = addressRepo.findById(addressId)
                .orElseThrow(() -> CbsException.notFound("ADDRESS_NOT_FOUND", "Address not found: " + addressId));
        if (!address.getEntityId().equals(entityId)) {
            throw CbsException.badRequest("ADDRESS_MISMATCH", "Address does not belong to this entity");
        }
        if (req.getAddressType() != null) address.setAddressType(req.getAddressType());
        if (req.getAddressLine1() != null) address.setAddressLine1(req.getAddressLine1());
        if (req.getAddressLine2() != null) address.setAddressLine2(req.getAddressLine2());
        if (req.getAddressLine3() != null) address.setAddressLine3(req.getAddressLine3());
        if (req.getCity() != null) address.setCity(req.getCity());
        if (req.getStateProvince() != null) address.setStateProvince(req.getStateProvince());
        if (req.getPostalCode() != null) address.setPostalCode(req.getPostalCode());
        if (req.getCountryCode() != null) address.setCountryCode(req.getCountryCode());
        if (req.getIsPrimary() != null) address.setIsPrimary(req.getIsPrimary());
        if (req.getUpdatedBy() != null) address.setUpdatedBy(req.getUpdatedBy());
        return EntityAddressResponse.from(addressRepo.save(address));
    }

    @Transactional
    public void expireAddress(String addressId) {
        EntityAddress address = addressRepo.findById(addressId)
                .orElseThrow(() -> CbsException.notFound("ADDRESS_NOT_FOUND", "Address not found: " + addressId));
        address.setEffectiveTo(Instant.now());
        addressRepo.save(address);
    }

    // ── Documents ────────────────────────────────────────────────────────────

    @Transactional
    public EntityDocumentResponse addDocument(String entityId, EntityDocumentRequest req) {
        findEntityOrThrow(entityId);
        EntityDocument doc = EntityDocument.builder()
                .entityId(entityId)
                .docType(req.getDocType())
                .docNumber(req.getDocNumber())
                .docName(req.getDocName())
                .issuingAuthority(req.getIssuingAuthority())
                .issuingCountry(req.getIssuingCountry())
                .issueDate(req.getIssueDate())
                .expiryDate(req.getExpiryDate())
                .isMandatory(req.getIsMandatory() != null ? req.getIsMandatory() : false)
                .docStatus(req.getDocStatus() != null ? req.getDocStatus() : "PENDING_VERIFICATION")
                .storageRef(req.getStorageRef())
                .rejectionReason(req.getRejectionReason())
                .supersededBy(req.getSupersededBy())
                .createdBy(req.getCreatedBy())
                .build();
        return EntityDocumentResponse.from(documentRepo.save(doc));
    }

    @Transactional(readOnly = true)
    public List<EntityDocumentResponse> listDocuments(String entityId) {
        findEntityOrThrow(entityId);
        return documentRepo.findByEntityId(entityId)
                .stream().map(EntityDocumentResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public EntityDocumentResponse updateDocument(String docId, EntityDocumentRequest req) {
        EntityDocument doc = documentRepo.findById(docId)
                .orElseThrow(() -> CbsException.notFound("DOCUMENT_NOT_FOUND", "Document not found: " + docId));
        if (req.getDocType() != null) doc.setDocType(req.getDocType());
        if (req.getDocNumber() != null) doc.setDocNumber(req.getDocNumber());
        if (req.getDocName() != null) doc.setDocName(req.getDocName());
        if (req.getIssuingAuthority() != null) doc.setIssuingAuthority(req.getIssuingAuthority());
        if (req.getIssuingCountry() != null) doc.setIssuingCountry(req.getIssuingCountry());
        if (req.getIssueDate() != null) doc.setIssueDate(req.getIssueDate());
        if (req.getExpiryDate() != null) doc.setExpiryDate(req.getExpiryDate());
        if (req.getIsMandatory() != null) doc.setIsMandatory(req.getIsMandatory());
        if (req.getDocStatus() != null) doc.setDocStatus(req.getDocStatus());
        if (req.getStorageRef() != null) doc.setStorageRef(req.getStorageRef());
        if (req.getRejectionReason() != null) doc.setRejectionReason(req.getRejectionReason());
        if (req.getSupersededBy() != null) doc.setSupersededBy(req.getSupersededBy());
        if (req.getUpdatedBy() != null) doc.setUpdatedBy(req.getUpdatedBy());
        return EntityDocumentResponse.from(documentRepo.save(doc));
    }

    @Transactional
    public EntityDocumentResponse verifyDocument(String docId, String verifiedBy) {
        EntityDocument doc = documentRepo.findById(docId)
                .orElseThrow(() -> CbsException.notFound("DOCUMENT_NOT_FOUND", "Document not found: " + docId));
        doc.setDocStatus("VERIFIED");
        doc.setVerifiedBy(verifiedBy);
        doc.setVerifiedAt(Instant.now());
        return EntityDocumentResponse.from(documentRepo.save(doc));
    }

    @Transactional
    public EntityDocumentResponse rejectDocument(String docId, String reason) {
        EntityDocument doc = documentRepo.findById(docId)
                .orElseThrow(() -> CbsException.notFound("DOCUMENT_NOT_FOUND", "Document not found: " + docId));
        doc.setDocStatus("REJECTED");
        doc.setRejectionReason(reason);
        return EntityDocumentResponse.from(documentRepo.save(doc));
    }

    @Transactional
    public void revokeDocument(String docId) {
        EntityDocument doc = documentRepo.findById(docId)
                .orElseThrow(() -> CbsException.notFound("DOCUMENT_NOT_FOUND", "Document not found: " + docId));
        doc.setDocStatus("REVOKED");
        documentRepo.save(doc);
    }

    // ── Financials ───────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<EntityFinancialsResponse> getFinancials(String entityId) {
        findEntityOrThrow(entityId);
        return financialsRepo.findByEntityIdOrderByFinancialYearDesc(entityId)
                .stream().map(EntityFinancialsResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public EntityFinancialsResponse putFinancials(String entityId, EntityFinancialsRequest req) {
        findEntityOrThrow(entityId);
        EntityFinancials fin = financialsRepo.findByEntityIdAndFinancialYear(entityId, req.getFinancialYear())
                .orElse(null);
        if (fin == null) {
            fin = EntityFinancials.builder()
                    .entityId(entityId)
                    .financialYear(req.getFinancialYear())
                    .annualTurnover(req.getAnnualTurnover())
                    .turnoverCurrency(req.getTurnoverCurrency())
                    .netWorth(req.getNetWorth())
                    .totalAssets(req.getTotalAssets())
                    .totalLiabilities(req.getTotalLiabilities())
                    .employeeCount(req.getEmployeeCount())
                    .auditorName(req.getAuditorName())
                    .accountsFiledDate(req.getAccountsFiledDate())
                    .creditRating(req.getCreditRating())
                    .creditRatingAgency(req.getCreditRatingAgency())
                    .createdBy(req.getCreatedBy())
                    .build();
        } else {
            fin.setAnnualTurnover(req.getAnnualTurnover());
            fin.setTurnoverCurrency(req.getTurnoverCurrency());
            fin.setNetWorth(req.getNetWorth());
            fin.setTotalAssets(req.getTotalAssets());
            fin.setTotalLiabilities(req.getTotalLiabilities());
            fin.setEmployeeCount(req.getEmployeeCount());
            fin.setAuditorName(req.getAuditorName());
            fin.setAccountsFiledDate(req.getAccountsFiledDate());
            fin.setCreditRating(req.getCreditRating());
            fin.setCreditRatingAgency(req.getCreditRatingAgency());
            fin.setUpdatedBy(req.getUpdatedBy());
        }
        return EntityFinancialsResponse.from(financialsRepo.save(fin));
    }

    @Transactional
    public EntityFinancialsResponse patchFinancials(String entityId, EntityFinancialsRequest req) {
        findEntityOrThrow(entityId);
        EntityFinancials fin = financialsRepo.findByEntityIdAndFinancialYear(entityId, req.getFinancialYear())
                .orElseThrow(() -> CbsException.notFound("FINANCIALS_NOT_FOUND",
                        "Financials not found for entity " + entityId + " year " + req.getFinancialYear()));
        if (req.getAnnualTurnover() != null) fin.setAnnualTurnover(req.getAnnualTurnover());
        if (req.getTurnoverCurrency() != null) fin.setTurnoverCurrency(req.getTurnoverCurrency());
        if (req.getNetWorth() != null) fin.setNetWorth(req.getNetWorth());
        if (req.getTotalAssets() != null) fin.setTotalAssets(req.getTotalAssets());
        if (req.getTotalLiabilities() != null) fin.setTotalLiabilities(req.getTotalLiabilities());
        if (req.getEmployeeCount() != null) fin.setEmployeeCount(req.getEmployeeCount());
        if (req.getAuditorName() != null) fin.setAuditorName(req.getAuditorName());
        if (req.getAccountsFiledDate() != null) fin.setAccountsFiledDate(req.getAccountsFiledDate());
        if (req.getCreditRating() != null) fin.setCreditRating(req.getCreditRating());
        if (req.getCreditRatingAgency() != null) fin.setCreditRatingAgency(req.getCreditRatingAgency());
        if (req.getUpdatedBy() != null) fin.setUpdatedBy(req.getUpdatedBy());
        return EntityFinancialsResponse.from(financialsRepo.save(fin));
    }

    // ── Compliance ───────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public EntityComplianceResponse getCompliance(String entityId) {
        findEntityOrThrow(entityId);
        return complianceRepo.findByEntityId(entityId)
                .map(EntityComplianceResponse::from)
                .orElseThrow(() -> CbsException.notFound("COMPLIANCE_NOT_FOUND",
                        "Compliance not found for entity: " + entityId));
    }

    @Transactional
    public EntityComplianceResponse putCompliance(String entityId, EntityComplianceRequest req) {
        findEntityOrThrow(entityId);
        EntityCompliance compliance = complianceRepo.findByEntityId(entityId).orElse(null);
        if (compliance == null) {
            compliance = EntityCompliance.builder()
                    .entityId(entityId)
                    .amlRiskRating(req.getAmlRiskRating() != null ? req.getAmlRiskRating() : "LOW")
                    .sanctionsHit(req.getSanctionsHit() != null ? req.getSanctionsHit() : false)
                    .sanctionsListRef(req.getSanctionsListRef())
                    .fatcaClassification(req.getFatcaClassification())
                    .crsClassification(req.getCrsClassification())
                    .taxIdentificationNumber(req.getTaxIdentificationNumber())
                    .vatNumber(req.getVatNumber())
                    .leiCode(req.getLeiCode())
                    .pepLinked(req.getPepLinked() != null ? req.getPepLinked() : false)
                    .nextReviewDate(req.getNextReviewDate())
                    .lastReviewedBy(req.getLastReviewedBy())
                    .createdBy(req.getCreatedBy())
                    .build();
        } else {
            compliance.setAmlRiskRating(req.getAmlRiskRating() != null ? req.getAmlRiskRating() : compliance.getAmlRiskRating());
            compliance.setSanctionsHit(req.getSanctionsHit() != null ? req.getSanctionsHit() : compliance.getSanctionsHit());
            compliance.setSanctionsListRef(req.getSanctionsListRef());
            compliance.setFatcaClassification(req.getFatcaClassification());
            compliance.setCrsClassification(req.getCrsClassification());
            compliance.setTaxIdentificationNumber(req.getTaxIdentificationNumber());
            compliance.setVatNumber(req.getVatNumber());
            compliance.setLeiCode(req.getLeiCode());
            compliance.setPepLinked(req.getPepLinked() != null ? req.getPepLinked() : compliance.getPepLinked());
            compliance.setNextReviewDate(req.getNextReviewDate());
            compliance.setLastReviewedBy(req.getLastReviewedBy());
            compliance.setUpdatedBy(req.getUpdatedBy());
        }
        return EntityComplianceResponse.from(complianceRepo.save(compliance));
    }

    @Transactional
    public EntityComplianceResponse patchCompliance(String entityId, EntityComplianceRequest req) {
        findEntityOrThrow(entityId);
        EntityCompliance compliance = complianceRepo.findByEntityId(entityId)
                .orElseThrow(() -> CbsException.notFound("COMPLIANCE_NOT_FOUND",
                        "Compliance not found for entity: " + entityId));
        if (req.getAmlRiskRating() != null) compliance.setAmlRiskRating(req.getAmlRiskRating());
        if (req.getSanctionsHit() != null) compliance.setSanctionsHit(req.getSanctionsHit());
        if (req.getSanctionsListRef() != null) compliance.setSanctionsListRef(req.getSanctionsListRef());
        if (req.getFatcaClassification() != null) compliance.setFatcaClassification(req.getFatcaClassification());
        if (req.getCrsClassification() != null) compliance.setCrsClassification(req.getCrsClassification());
        if (req.getTaxIdentificationNumber() != null) compliance.setTaxIdentificationNumber(req.getTaxIdentificationNumber());
        if (req.getVatNumber() != null) compliance.setVatNumber(req.getVatNumber());
        if (req.getLeiCode() != null) compliance.setLeiCode(req.getLeiCode());
        if (req.getPepLinked() != null) compliance.setPepLinked(req.getPepLinked());
        if (req.getNextReviewDate() != null) compliance.setNextReviewDate(req.getNextReviewDate());
        if (req.getLastReviewedBy() != null) compliance.setLastReviewedBy(req.getLastReviewedBy());
        if (req.getUpdatedBy() != null) compliance.setUpdatedBy(req.getUpdatedBy());
        return EntityComplianceResponse.from(complianceRepo.save(compliance));
    }

    // ── Links ────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<CustomerEntityLinkResponse> listLinkedCustomers(String entityId) {
        findEntityOrThrow(entityId);
        return linkRepo.findByEntityIdAndIsActiveTrue(entityId)
                .stream().map(CustomerEntityLinkResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public CustomerEntityLinkResponse linkCustomer(String entityId, CustomerEntityLinkRequest req) {
        findEntityOrThrow(entityId);
        customerRepo.findById(req.getCustomerId())
                .orElseThrow(() -> CbsException.notFound("CUSTOMER_NOT_FOUND", "Customer not found: " + req.getCustomerId()));

        CustomerEntityLink link = CustomerEntityLink.builder()
                .customerId(req.getCustomerId())
                .entityId(entityId)
                .roleType(req.getRoleType())
                .ownershipPercentage(req.getOwnershipPercentage())
                .isUbo(req.getIsUbo() != null ? req.getIsUbo() : false)
                .isAuthorisedSignatory(req.getIsAuthorisedSignatory() != null ? req.getIsAuthorisedSignatory() : false)
                .isDirector(req.getIsDirector() != null ? req.getIsDirector() : false)
                .isPrimaryContact(req.getIsPrimaryContact() != null ? req.getIsPrimaryContact() : false)
                .isActive(true)
                .effectiveFrom(req.getEffectiveFrom() != null ? req.getEffectiveFrom() : LocalDate.now())
                .effectiveTo(req.getEffectiveTo())
                .createdBy(req.getCreatedBy())
                .build();
        return CustomerEntityLinkResponse.from(linkRepo.save(link));
    }

    @Transactional
    public CustomerEntityLinkResponse updateLink(String entityId, String customerId, CustomerEntityLinkRequest req) {
        CustomerEntityLink link = linkRepo.findByCustomerIdAndEntityIdAndIsActiveTrue(customerId, entityId)
                .orElseThrow(() -> CbsException.notFound("LINK_NOT_FOUND",
                        "Active link not found for customer " + customerId + " and entity " + entityId));
        if (req.getRoleType() != null) link.setRoleType(req.getRoleType());
        if (req.getOwnershipPercentage() != null) link.setOwnershipPercentage(req.getOwnershipPercentage());
        if (req.getIsUbo() != null) link.setIsUbo(req.getIsUbo());
        if (req.getIsAuthorisedSignatory() != null) link.setIsAuthorisedSignatory(req.getIsAuthorisedSignatory());
        if (req.getIsDirector() != null) link.setIsDirector(req.getIsDirector());
        if (req.getIsPrimaryContact() != null) link.setIsPrimaryContact(req.getIsPrimaryContact());
        if (req.getEffectiveTo() != null) link.setEffectiveTo(req.getEffectiveTo());
        if (req.getUpdatedBy() != null) link.setUpdatedBy(req.getUpdatedBy());
        return CustomerEntityLinkResponse.from(linkRepo.save(link));
    }

    @Transactional
    public void unlinkCustomer(String entityId, String customerId) {
        CustomerEntityLink link = linkRepo.findByCustomerIdAndEntityIdAndIsActiveTrue(customerId, entityId)
                .orElseThrow(() -> CbsException.notFound("LINK_NOT_FOUND",
                        "Active link not found for customer " + customerId + " and entity " + entityId));
        link.setIsActive(false);
        link.setEffectiveTo(LocalDate.now());
        linkRepo.save(link);
    }

    private EntityMaster findEntityOrThrow(String entityId) {
        return entityRepo.findById(entityId)
                .orElseThrow(() -> CbsException.notFound("ENTITY_NOT_FOUND", "Entity not found: " + entityId));
    }
}
