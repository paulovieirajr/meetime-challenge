package com.github.paulovieirajr.meetime.rest.controller.oauth;

import com.github.paulovieirajr.meetime.rest.client.HubSpotOAuthClient;
import com.github.paulovieirajr.meetime.rest.dto.HubSpotTokenResponse;
import com.github.paulovieirajr.meetime.token.OAuthSessionStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HubSpotOAuthController.class)
@TestPropertySource(properties = {
        "hubspot.client.id=test-client-id",
        "hubspot.client.secret=test-client-secret",
        "hubspot.scopes=oauth",
        "hubspot.redirect.uri=https://example.com/callback",
        "hubspot.url.for-auth=https://example.com/oauth/authorize",
        "hubspot.url.oauth-success=https://example.com/oauth-success"
})
class HubSpotOAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HubSpotOAuthClient hubSpotOAuthClient;

    @MockitoBean
    private OAuthSessionStore oAuthSessionStore;

    @Nested
    class GetAuthorizationUrl {

        @Test
        @DisplayName("Should return 200 OK when generating authorization URL")
        void shouldGenerateAuthorizationUrl() throws Exception {
            mockMvc.perform(get("/oauth-url"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.authorizationUrl")
                            .value(containsString("https://example.com/oauth/authorize")))
                    .andExpect(jsonPath("$.authorizationUrl")
                            .value(containsString("client_id=test-client-id")))
                    .andExpect(jsonPath("$.authorizationUrl")
                            .value(containsString("scope=oauth")))
                    .andExpect(jsonPath("$.authorizationUrl")
                            .value(containsString("redirect_uri=https://example.com/callback")));
        }
    }

    @Nested
    class OAuthCallback {

        @Test
        @DisplayName("Should exchange code for token and redirect with sessionId")
        void shouldExchangeCodeAndRedirect() throws Exception {
            String code = "test-authorization-code";
            HubSpotTokenResponse mockTokenResponse = getHubSpotTokenResponseMock();

            when(hubSpotOAuthClient.exchangeToken(any())).thenReturn(mockTokenResponse);

            mockMvc.perform(get("/oauth-callback")
                            .param("code", code))
                    .andExpect(status().isFound())
                    .andExpect(header().string("Location", containsString("https://example.com/oauth-success")))
                    .andExpect(header().string("Location", containsString("session_id=")));

            verify(oAuthSessionStore, times(1)).saveToken(anyString(), eq(mockTokenResponse));
        }
    }

    @Nested
    class GetAccessToken {

        @Test
        @DisplayName("Should return access token for a valid sessionId")
        void shouldReturnAccessToken() throws Exception {
            String sessionId = "test-session-id";

            HubSpotTokenResponse mockTokenResponse = getHubSpotTokenResponseMock();

            when(oAuthSessionStore.getToken(sessionId)).thenReturn(mockTokenResponse);

            mockMvc.perform(get("/access-token")
                            .param("sessionId", sessionId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.access_token").value("access-token"))
                    .andExpect(jsonPath("$.refresh_token").value("refresh-token"));

            verify(oAuthSessionStore, times(1)).getToken(sessionId);
        }
    }

    private static HubSpotTokenResponse getHubSpotTokenResponseMock() {
        return new HubSpotTokenResponse(
                "access-token", "refresh-token", "1800", "bearer");
    }
}