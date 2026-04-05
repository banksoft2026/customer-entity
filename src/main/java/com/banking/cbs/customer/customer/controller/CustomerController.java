package com.banking.cbs.customer.customer.controller;

import com.banking.cbs.customer.common.response.ApiResponse;
import com.banking.cbs.customer.customer.dto.*;
import com.banking.cbs.customer.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "1. Customer Master", description = "Customer onboarding, management, contacts, documents, and risk profiling")
@RestController
@RequestMapping("/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    // ── Customer Master ──────────────────────────────────────────────────────

    @Operation(summary = "Onboard customer", description = "Create a new customer with optional contact, document, and risk profile.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CustomerResponse> onboard(@Valid @RequestBody CustomerRequest req) {
        return ApiResponse.ok("Customer onboarded successfully", service.onboard(req));
    }

    @Operation(summary = "Get full customer", description = "Get full customer details including contacts, documents, and risk profile.")
    @GetMapping("/{customerId}")
    public ApiResponse<CustomerResponse> get(@PathVariable String customerId) {
        return ApiResponse.ok(service.get(customerId));
    }

    @Operation(summary = "Get customer summary", description = "Get lightweight customer master record.")
    @GetMapping("/{customerId}/summary")
    public ApiResponse<CustomerResponse> getSummary(@PathVariable String customerId) {
        return ApiResponse.ok(service.getSummary(customerId));
    }

    @Operation(summary = "Update customer", description = "Partial update of mutable customer fields.")
    @PatchMapping("/{customerId}")
    public ApiResponse<CustomerResponse> patch(@PathVariable String customerId,
                                               @RequestBody CustomerRequest req) {
        return ApiResponse.ok(service.patch(customerId, req));
    }

    @Operation(summary = "Update customer status", description = "Transition customer status (PENDING_VERIFICATION→ACTIVE→SUSPENDED/CLOSED).")
    @PutMapping("/{customerId}/status")
    public ApiResponse<CustomerResponse> updateStatus(@PathVariable String customerId,
                                                      @RequestParam String status,
                                                      @RequestParam(required = false) String reason) {
        return ApiResponse.ok(service.updateStatus(customerId, status, reason));
    }

    @Operation(summary = "List customers", description = "List customers, optionally filtered by segment and/or RM.")
    @GetMapping
    public ApiResponse<List<CustomerResponse>> list(@RequestParam(required = false) String segment,
                                                    @RequestParam(required = false) String rmId) {
        return ApiResponse.ok(service.list(segment, rmId));
    }

    @Operation(summary = "Close customer", description = "Soft close a customer record.")
    @DeleteMapping("/{customerId}")
    public ApiResponse<CustomerResponse> softClose(@PathVariable String customerId) {
        return ApiResponse.ok("Customer closed", service.softClose(customerId));
    }

    // ── Customer Contacts ────────────────────────────────────────────────────

    @Operation(summary = "Add contact", description = "Add a contact record to a customer. If isPrimary=true, expires existing primary of same type.")
    @PostMapping("/{customerId}/contacts")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CustomerContactResponse> addContact(@PathVariable String customerId,
                                                           @Valid @RequestBody CustomerContactRequest req) {
        return ApiResponse.ok("Contact added", service.addContact(customerId, req));
    }

    @Operation(summary = "List contacts", description = "List active contacts for a customer.")
    @GetMapping("/{customerId}/contacts")
    public ApiResponse<List<CustomerContactResponse>> listContacts(@PathVariable String customerId) {
        return ApiResponse.ok(service.listContacts(customerId));
    }

    @Operation(summary = "Update contact", description = "Partial update of a customer contact.")
    @PatchMapping("/{customerId}/contacts/{contactId}")
    public ApiResponse<CustomerContactResponse> updateContact(@PathVariable String customerId,
                                                              @PathVariable String contactId,
                                                              @RequestBody CustomerContactRequest req) {
        return ApiResponse.ok(service.updateContact(customerId, contactId, req));
    }

    @Operation(summary = "Expire contact", description = "Soft-expire a contact by setting effectiveTo=now.")
    @DeleteMapping("/{customerId}/contacts/{contactId}")
    public ApiResponse<Void> expireContact(@PathVariable String customerId,
                                           @PathVariable String contactId) {
        service.expireContact(contactId);
        return ApiResponse.ok("Contact expired", null);
    }

    // ── Customer Documents ───────────────────────────────────────────────────

    @Operation(summary = "Add document", description = "Add a document to a customer.")
    @PostMapping("/{customerId}/documents")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CustomerDocumentResponse> addDocument(@PathVariable String customerId,
                                                             @Valid @RequestBody CustomerDocumentRequest req) {
        return ApiResponse.ok("Document added", service.addDocument(customerId, req));
    }

    @Operation(summary = "List documents", description = "List all documents for a customer.")
    @GetMapping("/{customerId}/documents")
    public ApiResponse<List<CustomerDocumentResponse>> listDocuments(@PathVariable String customerId) {
        return ApiResponse.ok(service.listDocuments(customerId));
    }

    @Operation(summary = "Update document", description = "Partial update of a customer document.")
    @PatchMapping("/{customerId}/documents/{docId}")
    public ApiResponse<CustomerDocumentResponse> updateDocument(@PathVariable String customerId,
                                                                @PathVariable String docId,
                                                                @RequestBody CustomerDocumentRequest req) {
        return ApiResponse.ok(service.updateDocument(docId, req));
    }

    // ── Customer Risk ────────────────────────────────────────────────────────

    @Operation(summary = "Get risk profile", description = "Get the risk profile for a customer.")
    @GetMapping("/{customerId}/risk")
    public ApiResponse<CustomerRiskResponse> getRisk(@PathVariable String customerId) {
        return ApiResponse.ok(service.getRisk(customerId));
    }

    @Operation(summary = "Replace risk profile", description = "Replace the full risk profile for a customer.")
    @PutMapping("/{customerId}/risk")
    public ApiResponse<CustomerRiskResponse> putRisk(@PathVariable String customerId,
                                                     @Valid @RequestBody CustomerRiskRequest req) {
        return ApiResponse.ok(service.putRisk(customerId, req));
    }

    @Operation(summary = "Patch risk profile", description = "Partial update of the risk profile for a customer.")
    @PatchMapping("/{customerId}/risk")
    public ApiResponse<CustomerRiskResponse> patchRisk(@PathVariable String customerId,
                                                       @RequestBody CustomerRiskRequest req) {
        return ApiResponse.ok(service.patchRisk(customerId, req));
    }
}
