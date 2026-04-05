package com.banking.cbs.customer.entity.controller;

import com.banking.cbs.customer.common.response.ApiResponse;
import com.banking.cbs.customer.entity.dto.*;
import com.banking.cbs.customer.entity.service.EntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "2. Entity Master", description = "Entity onboarding, management, addresses, documents, financials, compliance, and customer links")
@RestController
@RequestMapping("/v1/entities")
@RequiredArgsConstructor
public class EntityController {

    private final EntityService service;

    // ── Entity Master ─────────────────────────────────────────────────────────

    @Operation(summary = "Onboard entity", description = "Create a new entity with optional address, document, financials, compliance, and customer link.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EntityResponse> onboard(@Valid @RequestBody EntityRequest req) {
        return ApiResponse.ok("Entity onboarded successfully", service.onboard(req));
    }

    @Operation(summary = "Get full entity", description = "Get full entity details including all sub-resources.")
    @GetMapping("/{entityId}")
    public ApiResponse<EntityResponse> get(@PathVariable String entityId) {
        return ApiResponse.ok(service.get(entityId));
    }

    @Operation(summary = "Get entity summary", description = "Get lightweight entity master record.")
    @GetMapping("/{entityId}/summary")
    public ApiResponse<EntityResponse> getSummary(@PathVariable String entityId) {
        return ApiResponse.ok(service.getSummary(entityId));
    }

    @Operation(summary = "Update entity", description = "Partial update of mutable entity fields.")
    @PatchMapping("/{entityId}")
    public ApiResponse<EntityResponse> patch(@PathVariable String entityId,
                                             @RequestBody EntityRequest req) {
        return ApiResponse.ok(service.patch(entityId, req));
    }

    @Operation(summary = "Update entity status", description = "Transition entity status.")
    @PutMapping("/{entityId}/status")
    public ApiResponse<EntityResponse> updateStatus(@PathVariable String entityId,
                                                    @RequestParam String status,
                                                    @RequestParam(required = false) String reason) {
        return ApiResponse.ok(service.updateStatus(entityId, status, reason));
    }

    @Operation(summary = "List entities", description = "List entities with optional filters.")
    @GetMapping
    public ApiResponse<List<EntityResponse>> list(@RequestParam(required = false) String status,
                                                  @RequestParam(required = false) String entityType,
                                                  @RequestParam(required = false) String registrationCountry,
                                                  @RequestParam(required = false) String rmId) {
        return ApiResponse.ok(service.list(status, entityType, registrationCountry, rmId));
    }

    @Operation(summary = "Get group structure", description = "Walk parent chain up to 10 levels.")
    @GetMapping("/{entityId}/group-structure")
    public ApiResponse<List<EntityResponse>> getGroupStructure(@PathVariable String entityId) {
        return ApiResponse.ok(service.getGroupStructure(entityId));
    }

    @Operation(summary = "Close entity", description = "Soft close an entity record.")
    @DeleteMapping("/{entityId}")
    public ApiResponse<EntityResponse> softClose(@PathVariable String entityId) {
        return ApiResponse.ok("Entity closed", service.softClose(entityId));
    }

    // ── Entity Addresses ──────────────────────────────────────────────────────

    @Operation(summary = "Add address", description = "Add an address to an entity.")
    @PostMapping("/{entityId}/addresses")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EntityAddressResponse> addAddress(@PathVariable String entityId,
                                                         @Valid @RequestBody EntityAddressRequest req) {
        return ApiResponse.ok("Address added", service.addAddress(entityId, req));
    }

    @Operation(summary = "List addresses", description = "List active addresses for an entity.")
    @GetMapping("/{entityId}/addresses")
    public ApiResponse<List<EntityAddressResponse>> listAddresses(@PathVariable String entityId) {
        return ApiResponse.ok(service.listAddresses(entityId));
    }

    @Operation(summary = "Update address", description = "Partial update of an entity address.")
    @PatchMapping("/{entityId}/addresses/{addressId}")
    public ApiResponse<EntityAddressResponse> updateAddress(@PathVariable String entityId,
                                                            @PathVariable String addressId,
                                                            @RequestBody EntityAddressRequest req) {
        return ApiResponse.ok(service.updateAddress(entityId, addressId, req));
    }

    @Operation(summary = "Expire address", description = "Soft-expire an address.")
    @DeleteMapping("/{entityId}/addresses/{addressId}")
    public ApiResponse<Void> expireAddress(@PathVariable String entityId,
                                           @PathVariable String addressId) {
        service.expireAddress(addressId);
        return ApiResponse.ok("Address expired", null);
    }

    // ── Entity Documents ──────────────────────────────────────────────────────

    @Operation(summary = "Add document", description = "Add a document to an entity.")
    @PostMapping("/{entityId}/documents")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EntityDocumentResponse> addDocument(@PathVariable String entityId,
                                                           @Valid @RequestBody EntityDocumentRequest req) {
        return ApiResponse.ok("Document added", service.addDocument(entityId, req));
    }

    @Operation(summary = "List documents", description = "List all documents for an entity.")
    @GetMapping("/{entityId}/documents")
    public ApiResponse<List<EntityDocumentResponse>> listDocuments(@PathVariable String entityId) {
        return ApiResponse.ok(service.listDocuments(entityId));
    }

    @Operation(summary = "Update document", description = "Partial update of an entity document.")
    @PatchMapping("/{entityId}/documents/{docId}")
    public ApiResponse<EntityDocumentResponse> updateDocument(@PathVariable String entityId,
                                                              @PathVariable String docId,
                                                              @RequestBody EntityDocumentRequest req) {
        return ApiResponse.ok(service.updateDocument(docId, req));
    }

    @Operation(summary = "Verify document", description = "Set document status to VERIFIED.")
    @PutMapping("/{entityId}/documents/{docId}/verify")
    public ApiResponse<EntityDocumentResponse> verifyDocument(@PathVariable String entityId,
                                                              @PathVariable String docId,
                                                              @RequestParam String verifiedBy) {
        return ApiResponse.ok(service.verifyDocument(docId, verifiedBy));
    }

    @Operation(summary = "Reject document", description = "Set document status to REJECTED.")
    @PutMapping("/{entityId}/documents/{docId}/reject")
    public ApiResponse<EntityDocumentResponse> rejectDocument(@PathVariable String entityId,
                                                              @PathVariable String docId,
                                                              @RequestParam String reason) {
        return ApiResponse.ok(service.rejectDocument(docId, reason));
    }

    @Operation(summary = "Revoke document", description = "Set document status to REVOKED.")
    @DeleteMapping("/{entityId}/documents/{docId}")
    public ApiResponse<Void> revokeDocument(@PathVariable String entityId,
                                            @PathVariable String docId) {
        service.revokeDocument(docId);
        return ApiResponse.ok("Document revoked", null);
    }

    // ── Entity Financials ─────────────────────────────────────────────────────

    @Operation(summary = "Get financials", description = "Get financial records for an entity.")
    @GetMapping("/{entityId}/financials")
    public ApiResponse<List<EntityFinancialsResponse>> getFinancials(@PathVariable String entityId) {
        return ApiResponse.ok(service.getFinancials(entityId));
    }

    @Operation(summary = "Upsert financials", description = "Create or replace financials for an entity and financial year.")
    @PutMapping("/{entityId}/financials")
    public ApiResponse<EntityFinancialsResponse> putFinancials(@PathVariable String entityId,
                                                               @Valid @RequestBody EntityFinancialsRequest req) {
        return ApiResponse.ok(service.putFinancials(entityId, req));
    }

    @Operation(summary = "Patch financials", description = "Partial update of entity financials.")
    @PatchMapping("/{entityId}/financials")
    public ApiResponse<EntityFinancialsResponse> patchFinancials(@PathVariable String entityId,
                                                                 @RequestBody EntityFinancialsRequest req) {
        return ApiResponse.ok(service.patchFinancials(entityId, req));
    }

    // ── Entity Compliance ─────────────────────────────────────────────────────

    @Operation(summary = "Get compliance", description = "Get compliance record for an entity.")
    @GetMapping("/{entityId}/compliance")
    public ApiResponse<EntityComplianceResponse> getCompliance(@PathVariable String entityId) {
        return ApiResponse.ok(service.getCompliance(entityId));
    }

    @Operation(summary = "Replace compliance", description = "Create or replace compliance record for an entity.")
    @PutMapping("/{entityId}/compliance")
    public ApiResponse<EntityComplianceResponse> putCompliance(@PathVariable String entityId,
                                                               @Valid @RequestBody EntityComplianceRequest req) {
        return ApiResponse.ok(service.putCompliance(entityId, req));
    }

    @Operation(summary = "Patch compliance", description = "Partial update of entity compliance.")
    @PatchMapping("/{entityId}/compliance")
    public ApiResponse<EntityComplianceResponse> patchCompliance(@PathVariable String entityId,
                                                                 @RequestBody EntityComplianceRequest req) {
        return ApiResponse.ok(service.patchCompliance(entityId, req));
    }

    // ── Customer-Entity Links ─────────────────────────────────────────────────

    @Operation(summary = "Link customer", description = "Link a customer to an entity with a role.")
    @PostMapping("/{entityId}/customers")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CustomerEntityLinkResponse> linkCustomer(@PathVariable String entityId,
                                                                @Valid @RequestBody CustomerEntityLinkRequest req) {
        return ApiResponse.ok("Customer linked", service.linkCustomer(entityId, req));
    }

    @Operation(summary = "List linked customers", description = "List all active customer links for an entity.")
    @GetMapping("/{entityId}/customers")
    public ApiResponse<List<CustomerEntityLinkResponse>> listLinkedCustomers(@PathVariable String entityId) {
        return ApiResponse.ok(service.listLinkedCustomers(entityId));
    }

    @Operation(summary = "Update link", description = "Update a customer-entity link.")
    @PatchMapping("/{entityId}/customers/{customerId}")
    public ApiResponse<CustomerEntityLinkResponse> updateLink(@PathVariable String entityId,
                                                              @PathVariable String customerId,
                                                              @RequestBody CustomerEntityLinkRequest req) {
        return ApiResponse.ok(service.updateLink(entityId, customerId, req));
    }

    @Operation(summary = "Unlink customer", description = "Soft-unlink a customer from an entity.")
    @DeleteMapping("/{entityId}/customers/{customerId}")
    public ApiResponse<Void> unlinkCustomer(@PathVariable String entityId,
                                            @PathVariable String customerId) {
        service.unlinkCustomer(entityId, customerId);
        return ApiResponse.ok("Customer unlinked", null);
    }
}
