package com.github.paulovieirajr.meetime.token;

import com.github.paulovieirajr.meetime.rest.client.HubSpotOAuthClient;
import com.github.paulovieirajr.meetime.rest.dto.HubSpotTokenResponse;
import com.github.paulovieirajr.meetime.rest.exception.common.SessionNotFoundException;
import com.github.paulovieirajr.meetime.token.model.TokenSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.paulovieirajr.meetime.rest.constants.HubSpotParams.*;
import static com.github.paulovieirajr.meetime.rest.constants.MessageError.SESSION_NOT_FOUND;

@Component
public class OAuthSessionStore {

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
        TokenSession tokenSession = new TokenSession(token);
        sessions.put(sessionId, tokenSession);
    }

    public HubSpotTokenResponse getToken(String sessionId) {
        TokenSession tokenSession = sessions.get(sessionId);
        if (tokenSession == null) {
            throw new SessionNotFoundException(SESSION_NOT_FOUND.getMessage());
        }

        if (isTokenExpired(tokenSession)) {
            return refreshAccessToken(tokenSession.refreshToken(), sessionId);
        }
        return tokenSession.toHubSpotAccessToken();
    }

    public void removeToken(String sessionId) {
        sessions.remove(sessionId);
    }

    public boolean isTokenExpired(TokenSession token) {
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        return currentTimeInSeconds >= token.expiresAt();
    }

    public void cleanExpiredTokens() {
        sessions.entrySet().removeIf(entry -> isTokenExpired(entry.getValue()));
    }

    private HubSpotTokenResponse refreshAccessToken(String refreshToken, String sessionId) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE.getValue(), REFRESH_TOKEN.getValue());
        body.add(CLIENT_ID.getValue(), clientId);
        body.add(CLIENT_SECRET.getValue(), clientSecret);
        body.add(REFRESH_TOKEN.getValue(), refreshToken);

        HubSpotTokenResponse tokenResponse = hubSpotOAuthClient.exchangeToken(body);
        sessions.put(sessionId, new TokenSession(tokenResponse));
        return new HubSpotTokenResponse(tokenResponse.accessToken());
    }
}
