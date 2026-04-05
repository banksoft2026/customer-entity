package com.banking.cbs.customer.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerContactRequest {

    @NotBlank(message = "contactType is required")
    @Size(max = 20)
    private String contactType;

    @Size(max = 100)
    private String addressLine1;

    @Size(max = 100)
    private String addressLine2;

    @Size(max = 80)
    private String city;

    @Size(max = 80)
    private String stateProvince;

    @Size(max = 20)
    private String postalCode;

    @NotBlank(message = "countryCode is required")
    @Size(max = 3)
    private String countryCode;

    @Size(max = 30)
    private String phonePrimary;

    @Size(max = 30)
    private String phoneSecondary;

    @Size(max = 30)
    private String mobile;

    @Size(max = 200)
    private String emailPrimary;

    @Size(max = 200)
    private String emailSecondary;

    private Boolean isPrimary = false;

    @NotBlank(message = "createdBy is required")
    @Size(max = 36)
    private String createdBy;

    private String updatedBy;
}
