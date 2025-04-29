package com.github.paulovieirajr.meetime.views;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OAuthCallbackController.class)
class OAuthCallbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return success view with sessionId in model")
    void shouldReturnSuccessViewWithSessionIdInModel() throws Exception {
        String sessionId = "test-session-id";

        mockMvc.perform(get("/oauth-success")
                        .param("session_id", sessionId))
                .andExpect(status().isOk())
                .andExpect(view().name("success"))
                .andExpect(model().attribute("sessionId", sessionId));
    }
}