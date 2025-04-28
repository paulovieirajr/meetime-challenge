package com.github.paulovieirajr.meetime.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record HubSpotContactRequest(
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is mandatory")
        String email,

        @NotBlank(message = "First name is mandatory")
        String firstname,

        @Pattern(regexp = "^(?!\\s*$).*$", message = "Last name cannot be empty string")
        String lastname,

        @Pattern(regexp = "^(?!\\s*$).*$", message = "Phone cannot be empty string")
        String phone,

        @Pattern(regexp = "^(?!\\s*$).*$", message = "Company cannot be empty string")
        String company,

        @Pattern(regexp = "^(?!\\s*$).*$", message = "Website cannot be empty string")
        String website,

        @Pattern(regexp = "^(?!\\s*$).*$", message = "Lifecycle stage cannot be empty string")
        String lifecyclestage
) {}
