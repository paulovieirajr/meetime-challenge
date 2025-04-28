package com.github.paulovieirajr.meetime.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record HubSpotContactResponse(
        String id,
        String createdAt
) {
}

