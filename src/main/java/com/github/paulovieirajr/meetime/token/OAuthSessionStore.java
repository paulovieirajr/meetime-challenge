package com.github.paulovieirajr.meetime.token;

import com.github.paulovieirajr.meetime.rest.client.HubSpotOAuthClient;
import com.github.paulovieirajr.meetime.rest.dto.HubSpotTokenResponse;
import com.github.paulovieirajr.meetime.rest.exception.common.SessionNotFoundException;
import com.github.paulovieirajr.meetime.token.model.TokenSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.paulovieirajr.meetime.rest.constants.HubSpotParams.*;
import static com.github.paulovieirajr.meetime.rest.constants.MessageError.SESSION_NOT_FOUND;
import static com.github.paulovieirajr.meetime.token.log.SessionStoreLogger.*;

@Component
public class OAuthSessionStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthSessionStore.class);

    @Value("${hubspot.client.id}")
    private String clientId;

    @Value("${hubspot.client.secret}")
    private String clientSecret;

    private final Map<String, TokenSession> sessions = new ConcurrentHashMap<>();

    private final HubSpotOAuthClient hubSpotOAuthClient;

    public OAuthSessionStore(HubSpotOAuthClient hubSpotOAuthClient) {
        this.hubSpotOAuthClient = hubSpotOAuthClient;
    }

    public void saveToken(String sessionId, HubSpotTokenResponse token) {
        LOGGER.info(LOG_SAVING_TOKEN.getMessage(), sessionId);
        TokenSession tokenSession = new TokenSession(token);
        sessions.put(sessionId, tokenSession);
    }

    public HubSpotTokenResponse getToken(String sessionId) {
        LOGGER.info(LOG_RETRIEVING_TOKEN.getMessage(), sessionId);
        TokenSession tokenSession = sessions.get(sessionId);
        if (tokenSession == null) {
            LOGGER.error(LOG_SESSION_NOT_FOUND.getMessage(), sessionId);
            throw new SessionNotFoundException(SESSION_NOT_FOUND.getMessage());
        }

        if (isTokenExpired(tokenSession)) {
            LOGGER.info(LOG_TOKEN_EXPIRED.getMessage());
            return refreshAccessToken(tokenSession.refreshToken(), sessionId);
        }
        LOGGER.info(LOG_TOKEN_VALID.getMessage());
        return tokenSession.toHubSpotAccessToken();
    }

    public boolean isTokenExpired(TokenSession token) {
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        return currentTimeInSeconds >= token.expiresAt();
    }

    public void cleanExpiredTokens() {
        sessions.entrySet().removeIf(entry -> isTokenExpired(entry.getValue()));
    }

    private HubSpotTokenResponse refreshAccessToken(String refreshToken, String sessionId) {
        LOGGER.info(LOG_BUILDING_REQUEST.getMessage());
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE.getValue(), REFRESH_TOKEN.getValue());
        body.add(CLIENT_ID.getValue(), clientId);
        body.add(CLIENT_SECRET.getValue(), clientSecret);
        body.add(REFRESH_TOKEN.getValue(), refreshToken);

        LOGGER.info(LOG_OAUTH_HUBSPOT_TOKEN_ENDPOINT.getMessage());
        HubSpotTokenResponse tokenResponse = hubSpotOAuthClient.exchangeToken(body);
        LOGGER.info(LOG_TOKEN_REFRESHED.getMessage());
        sessions.put(sessionId, new TokenSession(tokenResponse));
        return new HubSpotTokenResponse(tokenResponse.accessToken());
    }
}
