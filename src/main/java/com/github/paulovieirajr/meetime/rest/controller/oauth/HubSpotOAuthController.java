package com.github.paulovieirajr.meetime.rest.controller.oauth;

import com.github.paulovieirajr.meetime.rest.client.HubSpotOAuthClient;
import com.github.paulovieirajr.meetime.rest.dto.AuthUrlResponse;
import com.github.paulovieirajr.meetime.rest.dto.HubSpotTokenResponse;
import com.github.paulovieirajr.meetime.token.OAuthSessionStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static com.github.paulovieirajr.meetime.rest.constants.HubSpotParams.*;
import static com.github.paulovieirajr.meetime.rest.constants.HubSpotPathController.*;

@RestController
public class HubSpotOAuthController implements HubSpotOAuthControllerSwagger {

    @Value("${hubspot.client.id}")
    private String clientId;

    @Value("${hubspot.client.secret}")
    private String clientSecret;

    @Value("${hubspot.scopes}")
    private String scopes;

    @Value("${hubspot.redirect.uri}")
    private String redirectUri;

    @Value("${hubspot.url.for-auth}")
    private String urlForAuthorization;

    @Value("${hubspot.url.oauth-success}")
    private String urlForOAuthSuccess;

    private final HubSpotOAuthClient hubSpotOAuthClient;
    private final OAuthSessionStore oAuthSessionStore;

    public HubSpotOAuthController(HubSpotOAuthClient hubSpotOAuthClient, OAuthSessionStore oAuthSessionStore) {
        this.hubSpotOAuthClient = hubSpotOAuthClient;
        this.oAuthSessionStore = oAuthSessionStore;
    }

    @GetMapping(OAUTH_URL_PATH)
    public ResponseEntity<AuthUrlResponse> getAuthorizationUrl() {
        AuthUrlResponse authUrlResponse = new AuthUrlResponse(UriComponentsBuilder
                .fromUriString(urlForAuthorization)
                .queryParam(CLIENT_ID.getValue(), clientId)
                .queryParam(SCOPE.getValue(), scopes)
                .queryParam(REDIRECT_URI.getValue(), redirectUri)
                .toUriString());
        return ResponseEntity.ok(authUrlResponse);
    }

    @GetMapping(OAUTH_CALLBACK_PATH)
    public ResponseEntity<?> oauthCallback(@RequestParam String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE.getValue(), AUTHORIZATION_CODE.getValue());
        body.add(CLIENT_ID.getValue(), clientId);
        body.add(CLIENT_SECRET.getValue(), clientSecret);
        body.add(REDIRECT_URI.getValue(), redirectUri);
        body.add(CODE.getValue(), code);

        HubSpotTokenResponse hubSpotTokenResponse = hubSpotOAuthClient.exchangeToken(body);
        String sessionId = UUID.randomUUID().toString();
        oAuthSessionStore.saveToken(sessionId, hubSpotTokenResponse);

        URI redirectUri = UriComponentsBuilder
                .fromUriString(urlForOAuthSuccess)
                .queryParam(SESSION_ID.getValue(), sessionId)
                .build()
                .toUri();

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(redirectUri)
                .build();
    }

    @GetMapping(ACCESS_TOKEN_PATH)
    public ResponseEntity<?> getAccessToken(@RequestParam String sessionId) {
        HubSpotTokenResponse tokenResponse = oAuthSessionStore.getToken(sessionId);
        return ResponseEntity.ok(tokenResponse);
    }
}
