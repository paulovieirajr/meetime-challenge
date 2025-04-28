package com.github.paulovieirajr.meetime.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "access_token",
        "refresh_token",
        "expires_in",
        "token_type"
})
public record HubSpotTokenResponse(
        @JsonProperty(value = "access_token")
        String accessToken,
        @JsonProperty(value = "refresh_token")
        String refreshToken,
        @JsonProperty(value = "expires_in")
        String expiresIn,
        @JsonProperty(value = "token_type")
        String tokenType
) {
        public HubSpotTokenResponse(String accessToken) {
                this(accessToken, null, null, null);
        }
}
