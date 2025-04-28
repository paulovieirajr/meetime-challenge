package com.github.paulovieirajr.meetime.token.model;

import com.github.paulovieirajr.meetime.rest.dto.HubSpotTokenResponse;

public record TokenSession(
        String accessToken,
        String refreshToken,
        String expiresIn,
        String tokenType,
        long expiresAt
) {

    public static final int ONE_SECOND = 1000;

    public TokenSession(HubSpotTokenResponse tokenResponse) {
        this(
                tokenResponse.accessToken(),
                tokenResponse.refreshToken(),
                tokenResponse.expiresIn(),
                tokenResponse.tokenType(),
                System.currentTimeMillis() / ONE_SECOND + Long.parseLong(tokenResponse.expiresIn())
        );
    }

    public HubSpotTokenResponse toHubSpotAccessToken() {
        return new HubSpotTokenResponse(accessToken);
    }
}
