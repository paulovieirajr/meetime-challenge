package com.github.paulovieirajr.meetime.token;

import com.github.paulovieirajr.meetime.rest.client.HubSpotOAuthClient;
import com.github.paulovieirajr.meetime.rest.dto.HubSpotTokenResponse;
import com.github.paulovieirajr.meetime.rest.exception.common.SessionNotFoundException;
import com.github.paulovieirajr.meetime.token.model.TokenSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuthSessionStoreTest {

    @Mock
    private HubSpotOAuthClient hubSpotOAuthClient;

    @InjectMocks
    private OAuthSessionStore oAuthSessionStore;

    @Test
    @DisplayName("Should save and retrieve a valid token")
    void shouldSaveAndRetrieveValidToken() {
        String sessionId = "test-session-id";
        HubSpotTokenResponse tokenResponse = new HubSpotTokenResponse(
                "access-token", "refresh-token", "1800", "bearer");

        oAuthSessionStore.saveToken(sessionId, tokenResponse);
        HubSpotTokenResponse retrievedToken = oAuthSessionStore.getToken(sessionId);

        assertThat(retrievedToken.accessToken()).isEqualTo("access-token");
    }

    @Test
    @DisplayName("Should throw SessionNotFoundException when session does not exist")
    void shouldThrowWhenSessionNotFound() {
        String nonExistentSessionId = "non-existent-session";

        assertThatThrownBy(() -> oAuthSessionStore.getToken(nonExistentSessionId))
                .isInstanceOf(SessionNotFoundException.class)
                .hasMessageContaining("Session not found");
    }

    @Test
    @DisplayName("Should refresh token when expired")
    void shouldRefreshTokenWhenExpired() {
        String sessionId = "expired-session-id";

        HubSpotTokenResponse expiredTokenResponse = new HubSpotTokenResponse(
                "access-token",
                "refresh-token",
                "-100",
                "bearer"
        );
        oAuthSessionStore.saveToken(sessionId, expiredTokenResponse);

        HubSpotTokenResponse refreshedToken = new HubSpotTokenResponse(
                "new-access-token",
                "new-refresh-token",
                "1800",
                "bearer"
        );

        when(hubSpotOAuthClient.exchangeToken(any(MultiValueMap.class)))
                .thenReturn(refreshedToken);

        HubSpotTokenResponse retrievedToken = oAuthSessionStore.getToken(sessionId);

        assertThat(retrievedToken.accessToken()).isEqualTo("new-access-token");
        verify(hubSpotOAuthClient, times(1)).exchangeToken(any(MultiValueMap.class));
    }


    @Test
    @DisplayName("Should correctly identify expired token")
    void shouldIdentifyExpiredToken() {
        long expiredTime = System.currentTimeMillis() / 1000 - 100;
        TokenSession expiredToken = new TokenSession(
                "access-token",
                "refresh-token",
                String.valueOf(expiredTime),
                "bearer",
                1000);

        boolean isExpired = oAuthSessionStore.isTokenExpired(expiredToken);

        assertThat(isExpired).isTrue();
    }

    @Test
    @DisplayName("Should correctly identify non-expired token")
    void shouldIdentifyNonExpiredToken() {
        long nowInSeconds = System.currentTimeMillis() / 1000;
        int expiresInSeconds = 1000;
        long expiresAt = nowInSeconds + expiresInSeconds;

        TokenSession validToken = new TokenSession(
                "access-token",
                "refresh-token",
                String.valueOf(expiresInSeconds),
                "bearer",
                expiresAt
        );

        boolean isExpired = oAuthSessionStore.isTokenExpired(validToken);

        assertThat(isExpired).isFalse();
    }
}
