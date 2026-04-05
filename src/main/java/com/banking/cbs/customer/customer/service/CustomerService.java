package com.banking.cbs.customer.customer.service;

import com.banking.cbs.customer.common.exception.CbsException;
import com.banking.cbs.customer.customer.dto.*;
import com.banking.cbs.customer.customer.entity.*;
import com.banking.cbs.customer.customer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerMasterRepository customerRepo;
    private final CustomerContactRepository contactRepo;
    private final CustomerDocumentRepository documentRepo;
    private final CustomerRiskRepository riskRepo;

    @Transactional
    public CustomerResponse onboard(CustomerRequest req) {
        if ("INDIVIDUAL".equals(req.getCustomerType()) || "SOLE_TRADER".equals(req.getCustomerType())) {
            if (req.getFirstName() == null || req.getFirstName().isBlank()) {
                throw CbsException.badRequest("VALIDATION_ERROR", "firstName is required for " + req.getCustomerType());
            }
            if (req.getLastName() == null || req.getLastName().isBlank()) {
                throw CbsException.badRequest("VALIDATION_ERROR", "lastName is required for " + req.getCustomerType());
            }
        }

        CustomerMaster master = CustomerMaster.builder()
                .customerType(req.getCustomerType())
                .title(req.getTitle())
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .fullName(req.getFullName() != null ? req.getFullName()
                        : (req.getFirstName() != null ? req.getFirstName() + " " + req.getLastName() : null))
                .dateOfBirth(req.getDateOfBirth())
                .nationality(req.getNationality())
                .countryOfBirth(req.getCountryOfBirth())
                .occupation(req.getOccupation())
                .employerName(req.getEmployerName())
                .customerSegment(req.getCustomerSegment())
                .customerStatus("PENDING_VERIFICATION")
                .kycStatus("PENDING")
                .onboardingChannel(req.getOnboardingChannel())
                .onboardingBranch(req.getOnboardingBranch())
                .relationshipManagerId(req.getRelationshipManagerId())
                .relationshipSince(req.getRelationshipSince())
                .createdBy(req.getCreatedBy())
                .build();
        master = customerRepo.save(master);

        if (req.getContact() != null) {
            addContact(master.getCustomerId(), req.getContact());
        }
        if (req.getDocument() != null) {
            addDocument(master.getCustomerId(), req.getDocument());
        }
        if (req.getRisk() != null) {
            putRisk(master.getCustomerId(), req.getRisk());
        }

        return CustomerResponse.from(master);
    }

    @Transactional(readOnly = true)
    public CustomerResponse get(String customerId) {
        CustomerMaster master = findCustomerOrThrow(customerId);
        CustomerResponse resp = CustomerResponse.from(master);
        resp.setContacts(contactRepo.findByCustomerIdAndEffectiveToIsNull(customerId)
                .stream().map(CustomerContactResponse::from).collect(Collectors.toList()));
        resp.setDocuments(documentRepo.findByCustomerId(customerId)
                .stream().map(CustomerDocumentResponse::from).collect(Collectors.toList()));
        riskRepo.findByCustomerId(customerId).ifPresent(r -> resp.setRisk(CustomerRiskResponse.from(r)));
        return resp;
    }

    @Transactional(readOnly = true)
    public CustomerResponse getSummary(String customerId) {
        return CustomerResponse.from(findCustomerOrThrow(customerId));
    }

    @Transactional
    public CustomerResponse patch(String customerId, CustomerRequest req) {
        CustomerMaster master = findCustomerOrThrow(customerId);
        if (req.getTitle() != null) master.setTitle(req.getTitle());
        if (req.getFirstName() != null) master.setFirstName(req.getFirstName());
        if (req.getLastName() != null) master.setLastName(req.getLastName());
        if (req.getFullName() != null) master.setFullName(req.getFullName());
        if (req.getDateOfBirth() != null) master.setDateOfBirth(req.getDateOfBirth());
        if (req.getNationality() != null) master.setNationality(req.getNationality());
        if (req.getCountryOfBirth() != null) master.setCountryOfBirth(req.getCountryOfBirth());
        if (req.getOccupation() != null) master.setOccupation(req.getOccupation());
        if (req.getEmployerName() != null) master.setEmployerName(req.getEmployerName());
        if (req.getCustomerSegment() != null) master.setCustomerSegment(req.getCustomerSegment());
        if (req.getKycStatus() != null) master.setKycStatus(req.getKycStatus());
        if (req.getKycReviewDate() != null) master.setKycReviewDate(req.getKycReviewDate());
        if (req.getOnboardingChannel() != null) master.setOnboardingChannel(req.getOnboardingChannel());
        if (req.getOnboardingBranch() != null) master.setOnboardingBranch(req.getOnboardingBranch());
        if (req.getRelationshipManagerId() != null) master.setRelationshipManagerId(req.getRelationshipManagerId());
        if (req.getRelationshipSince() != null) master.setRelationshipSince(req.getRelationshipSince());
        if (req.getUpdatedBy() != null) master.setUpdatedBy(req.getUpdatedBy());
        return CustomerResponse.from(customerRepo.save(master));
    }

    @Transactional
    public CustomerResponse updateStatus(String customerId, String newStatus, String reason) {
        CustomerMaster master = findCustomerOrThrow(customerId);
        String current = master.getCustomerStatus();
        validateStatusTransition(current, newStatus, master);
        if ("ACTIVE".equals(newStatus) && "PENDING_VERIFICATION".equals(current)) {
            if (!"VERIFIED".equals(master.getKycStatus())) {
                throw CbsException.unprocessable("KYC_REQUIRED", "KYC must be VERIFIED before activating customer");
            }
        }
        if ("CLOSED".equals(newStatus)) {
            master.setClosedAt(Instant.now());
            master.setCloseReason(reason);
        }
        master.setCustomerStatus(newStatus);
        return CustomerResponse.from(customerRepo.save(master));
    }

    private void validateStatusTransition(String from, String to, CustomerMaster m) {
        boolean valid = switch (from) {
            case "PENDING_VERIFICATION" -> "ACTIVE".equals(to) || "CLOSED".equals(to);
            case "ACTIVE" -> "SUSPENDED".equals(to) || "CLOSED".equals(to);
            case "SUSPENDED" -> "ACTIVE".equals(to) || "CLOSED".equals(to);
            default -> false;
        };
        if (!valid) {
            throw CbsException.unprocessable("INVALID_TRANSITION",
                    "Cannot transition from " + from + " to " + to);
        }
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> list(String segment, String rmId) {
        List<CustomerMaster> results;
        if (segment != null && rmId != null) {
            results = customerRepo.findByCustomerSegmentAndRelationshipManagerId(segment, rmId);
        } else if (segment != null) {
            results = customerRepo.findByCustomerSegment(segment);
        } else if (rmId != null) {
            results = customerRepo.findByRelationshipManagerId(rmId);
        } else {
            results = customerRepo.findAll();
        }
        return results.stream().map(CustomerResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public CustomerResponse softClose(String customerId) {
        CustomerMaster master = findCustomerOrThrow(customerId);
        master.setCustomerStatus("CLOSED");
        master.setClosedAt(Instant.now());
        return CustomerResponse.from(customerRepo.save(master));
    }

    @Transactional
    public CustomerContactResponse addContact(String customerId, CustomerContactRequest req) {
        findCustomerOrThrow(customerId);
        if (Boolean.TRUE.equals(req.getIsPrimary())) {
            contactRepo.findByCustomerIdAndContactTypeAndIsPrimaryTrueAndEffectiveToIsNull(customerId, req.getContactType())
                    .ifPresent(existing -> {
                        existing.setEffectiveTo(Instant.now());
                        contactRepo.save(existing);
                    });
        }
        CustomerContact contact = CustomerContact.builder()
                .customerId(customerId)
                .contactType(req.getContactType())
                .addressLine1(req.getAddressLine1())
                .addressLine2(req.getAddressLine2())
                .city(req.getCity())
                .stateProvince(req.getStateProvince())
                .postalCode(req.getPostalCode())
                .countryCode(req.getCountryCode())
                .phonePrimary(req.getPhonePrimary())
                .phoneSecondary(req.getPhoneSecondary())
                .mobile(req.getMobile())
                .emailPrimary(req.getEmailPrimary())
                .emailSecondary(req.getEmailSecondary())
                .isPrimary(req.getIsPrimary() != null ? req.getIsPrimary() : false)
                .isVerified(false)
                .createdBy(req.getCreatedBy())
                .build();
        return CustomerContactResponse.from(contactRepo.save(contact));
    }

    @Transactional(readOnly = true)
    public List<CustomerContactResponse> listContacts(String customerId) {
        findCustomerOrThrow(customerId);
        return contactRepo.findByCustomerIdAndEffectiveToIsNull(customerId)
                .stream().map(CustomerContactResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public CustomerContactResponse updateContact(String customerId, String contactId, CustomerContactRequest req) {
        findCustomerOrThrow(customerId);
        CustomerContact contact = contactRepo.findById(contactId)
                .orElseThrow(() -> CbsException.notFound("CONTACT_NOT_FOUND", "Contact not found: " + contactId));
        if (!contact.getCustomerId().equals(customerId)) {
            throw CbsException.badRequest("CONTACT_MISMATCH", "Contact does not belong to this customer");
        }
        if (req.getContactType() != null) contact.setContactType(req.getContactType());
        if (req.getAddressLine1() != null) contact.setAddressLine1(req.getAddressLine1());
        if (req.getAddressLine2() != null) contact.setAddressLine2(req.getAddressLine2());
        if (req.getCity() != null) contact.setCity(req.getCity());
        if (req.getStateProvince() != null) contact.setStateProvince(req.getStateProvince());
        if (req.getPostalCode() != null) contact.setPostalCode(req.getPostalCode());
        if (req.getCountryCode() != null) contact.setCountryCode(req.getCountryCode());
        if (req.getPhonePrimary() != null) contact.setPhonePrimary(req.getPhonePrimary());
        if (req.getPhoneSecondary() != null) contact.setPhoneSecondary(req.getPhoneSecondary());
        if (req.getMobile() != null) contact.setMobile(req.getMobile());
        if (req.getEmailPrimary() != null) contact.setEmailPrimary(req.getEmailPrimary());
        if (req.getEmailSecondary() != null) contact.setEmailSecondary(req.getEmailSecondary());
        if (req.getIsPrimary() != null) contact.setIsPrimary(req.getIsPrimary());
        if (req.getUpdatedBy() != null) contact.setUpdatedBy(req.getUpdatedBy());
        return CustomerContactResponse.from(contactRepo.save(contact));
    }

    @Transactional
    public void expireContact(String contactId) {
        CustomerContact contact = contactRepo.findById(contactId)
                .orElseThrow(() -> CbsException.notFound("CONTACT_NOT_FOUND", "Contact not found: " + contactId));
        contact.setEffectiveTo(Instant.now());
        contactRepo.save(contact);
    }

    @Transactional
    public CustomerDocumentResponse addDocument(String customerId, CustomerDocumentRequest req) {
        findCustomerOrThrow(customerId);
        CustomerDocument doc = CustomerDocument.builder()
                .customerId(customerId)
                .docType(req.getDocType())
                .docNumber(req.getDocNumber())
                .issuingCountry(req.getIssuingCountry())
                .issuingAuthority(req.getIssuingAuthority())
                .issueDate(req.getIssueDate())
                .expiryDate(req.getExpiryDate())
                .docStatus(req.getDocStatus() != null ? req.getDocStatus() : "PENDING_VERIFICATION")
                .storageRef(req.getStorageRef())
                .isMandatory(req.getIsMandatory() != null ? req.getIsMandatory() : false)
                .rejectionReason(req.getRejectionReason())
                .createdBy(req.getCreatedBy())
                .build();
        return CustomerDocumentResponse.from(documentRepo.save(doc));
    }

    @Transactional(readOnly = true)
    public List<CustomerDocumentResponse> listDocuments(String customerId) {
        findCustomerOrThrow(customerId);
        return documentRepo.findByCustomerId(customerId)
                .stream().map(CustomerDocumentResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public CustomerDocumentResponse updateDocument(String docId, CustomerDocumentRequest req) {
        CustomerDocument doc = documentRepo.findById(docId)
                .orElseThrow(() -> CbsException.notFound("DOCUMENT_NOT_FOUND", "Document not found: " + docId));
        if (req.getDocType() != null) doc.setDocType(req.getDocType());
        if (req.getDocNumber() != null) doc.setDocNumber(req.getDocNumber());
        if (req.getIssuingCountry() != null) doc.setIssuingCountry(req.getIssuingCountry());
        if (req.getIssuingAuthority() != null) doc.setIssuingAuthority(req.getIssuingAuthority());
        if (req.getIssueDate() != null) doc.setIssueDate(req.getIssueDate());
        if (req.getExpiryDate() != null) doc.setExpiryDate(req.getExpiryDate());
        if (req.getDocStatus() != null) doc.setDocStatus(req.getDocStatus());
        if (req.getStorageRef() != null) doc.setStorageRef(req.getStorageRef());
        if (req.getIsMandatory() != null) doc.setIsMandatory(req.getIsMandatory());
        if (req.getRejectionReason() != null) doc.setRejectionReason(req.getRejectionReason());
        if (req.getUpdatedBy() != null) doc.setUpdatedBy(req.getUpdatedBy());
        return CustomerDocumentResponse.from(documentRepo.save(doc));
    }

    @Transactional
    public void revokeDocument(String docId) {
        CustomerDocument doc = documentRepo.findById(docId)
                .orElseThrow(() -> CbsException.notFound("DOCUMENT_NOT_FOUND", "Document not found: " + docId));
        doc.setDocStatus("REVOKED");
        documentRepo.save(doc);
    }

    @Transactional(readOnly = true)
    public CustomerRiskResponse getRisk(String customerId) {
        findCustomerOrThrow(customerId);
        return riskRepo.findByCustomerId(customerId)
                .map(CustomerRiskResponse::from)
                .orElseThrow(() -> CbsException.notFound("RISK_NOT_FOUND", "Risk profile not found for customer: " + customerId));
    }

    @Transactional
    public CustomerRiskResponse putRisk(String customerId, CustomerRiskRequest req) {
        findCustomerOrThrow(customerId);
        CustomerRisk risk = riskRepo.findByCustomerId(customerId).orElse(null);
        if (risk == null) {
            risk = CustomerRisk.builder()
                    .customerId(customerId)
                    .amlRiskRating(req.getAmlRiskRating() != null ? req.getAmlRiskRating() : "LOW")
                    .pepStatus(req.getPepStatus() != null ? req.getPepStatus() : "NOT_PEP")
                    .pepCategory(req.getPepCategory())
                    .sanctionsHit(req.getSanctionsHit() != null ? req.getSanctionsHit() : false)
                    .sanctionsListRef(req.getSanctionsListRef())
                    .fatcaStatus(req.getFatcaStatus())
                    .crsStatus(req.getCrsStatus())
                    .taxResidencyCountry(req.getTaxResidencyCountry())
                    .tinNumber(req.getTinNumber())
                    .nextReviewDate(req.getNextReviewDate())
                    .lastReviewedBy(req.getLastReviewedBy())
                    .createdBy(req.getCreatedBy())
                    .build();
        } else {
            risk.setAmlRiskRating(req.getAmlRiskRating() != null ? req.getAmlRiskRating() : risk.getAmlRiskRating());
            risk.setPepStatus(req.getPepStatus() != null ? req.getPepStatus() : risk.getPepStatus());
            risk.setPepCategory(req.getPepCategory());
            risk.setSanctionsHit(req.getSanctionsHit() != null ? req.getSanctionsHit() : risk.getSanctionsHit());
            risk.setSanctionsListRef(req.getSanctionsListRef());
            risk.setFatcaStatus(req.getFatcaStatus());
            risk.setCrsStatus(req.getCrsStatus());
            risk.setTaxResidencyCountry(req.getTaxResidencyCountry());
            risk.setTinNumber(req.getTinNumber());
            risk.setNextReviewDate(req.getNextReviewDate());
            risk.setLastReviewedBy(req.getLastReviewedBy());
            risk.setUpdatedBy(req.getUpdatedBy());
        }
        return CustomerRiskResponse.from(riskRepo.save(risk));
    }

    @Transactional
    public CustomerRiskResponse patchRisk(String customerId, CustomerRiskRequest req) {
        findCustomerOrThrow(customerId);
        CustomerRisk risk = riskRepo.findByCustomerId(customerId)
                .orElseThrow(() -> CbsException.notFound("RISK_NOT_FOUND", "Risk profile not found for customer: " + customerId));
        if (req.getAmlRiskRating() != null) risk.setAmlRiskRating(req.getAmlRiskRating());
        if (req.getPepStatus() != null) risk.setPepStatus(req.getPepStatus());
        if (req.getPepCategory() != null) risk.setPepCategory(req.getPepCategory());
        if (req.getSanctionsHit() != null) risk.setSanctionsHit(req.getSanctionsHit());
        if (req.getSanctionsListRef() != null) risk.setSanctionsListRef(req.getSanctionsListRef());
        if (req.getFatcaStatus() != null) risk.setFatcaStatus(req.getFatcaStatus());
        if (req.getCrsStatus() != null) risk.setCrsStatus(req.getCrsStatus());
        if (req.getTaxResidencyCountry() != null) risk.setTaxResidencyCountry(req.getTaxResidencyCountry());
        if (req.getTinNumber() != null) risk.setTinNumber(req.getTinNumber());
        if (req.getNextReviewDate() != null) risk.setNextReviewDate(req.getNextReviewDate());
        if (req.getLastReviewedBy() != null) risk.setLastReviewedBy(req.getLastReviewedBy());
        if (req.getUpdatedBy() != null) risk.setUpdatedBy(req.getUpdatedBy());
        return CustomerRiskResponse.from(riskRepo.save(risk));
    }

    private CustomerMaster findCustomerOrThrow(String customerId) {
        return customerRepo.findById(customerId)
                .orElseThrow(() -> CbsException.notFound("CUSTOMER_NOT_FOUND", "Customer not found: " + customerId));
    }
}
