package com.github.paulovieirajr.meetime.rest.controller.oauth;

import com.github.paulovieirajr.meetime.rest.dto.AuthUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "HubSpot OAuth", description = "HubSpot OAuth API")
public interface HubSpotOAuthControllerSwagger {

    @Operation(summary = "Get HubSpot authorization URL", description = "Get the authorization URL for HubSpot OAuth2 authentication")
    @ApiResponse(responseCode = "200", description = "Authorization URL retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @ApiResponse(responseCode = "503", description = "Service unavailable")
    ResponseEntity<AuthUrlResponse> getAuthorizationUrl();

    @Operation(summary = "Only Triggered when the user interacts with the OAuth", description = "Handle the HubSpot OAuth2 callback")
    @ApiResponse(responseCode = "200", description = "Token exchanged successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @ApiResponse(responseCode = "503", description = "Service unavailable")
    ResponseEntity<?> oauthCallback(String code);

    @Operation(summary = "Get HubSpot access token", description = "Get the access token from HubSpot OAuth2 authentication")
    @ApiResponse(responseCode = "200", description = "Token exchanged successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @ApiResponse(responseCode = "503", description = "Service unavailable")
    ResponseEntity<?> getAccessToken(String sessionId);
}
