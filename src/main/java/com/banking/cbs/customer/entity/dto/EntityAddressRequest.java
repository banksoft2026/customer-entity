package com.banking.cbs.customer.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EntityAddressRequest {

    @NotBlank(message = "addressType is required")
    @Size(max = 20)
    private String addressType;

    @NotBlank(message = "addressLine1 is required")
    @Size(max = 100)
    private String addressLine1;

    @Size(max = 100)
    private String addressLine2;

    @Size(max = 100)
    private String addressLine3;

    @NotBlank(message = "city is required")
    @Size(max = 80)
    private String city;

    @Size(max = 80)
    private String stateProvince;

    @Size(max = 20)
    private String postalCode;

    @NotBlank(message = "countryCode is required")
    @Size(max = 3)
    private String countryCode;

    private Boolean isPrimary = false;

    @NotBlank(message = "createdBy is required")
    @Size(max = 36)
    private String createdBy;

    private String updatedBy;
}
