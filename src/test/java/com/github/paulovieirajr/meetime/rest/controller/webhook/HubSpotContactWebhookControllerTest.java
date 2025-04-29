package com.github.paulovieirajr.meetime.rest.controller.webhook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HubSpotContactWebhookController.class)
class HubSpotContactWebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class ReceiveContactCreatedWebhook {

        @Test
        @DisplayName("Should return 200 OK when receiving webhook payload")
        void shouldReturnOkWhenReceivingWebhookPayload() throws Exception {
            String payloadJson = """
                    [
                      {
                            "appId": 11577049,
                            "eventId": 100,
                            "subscriptionId": 3549385,
                            "portalId": 242627423,
                            "occurredAt": 1745877107292,
                            "subscriptionType": "contact.creation",
                            "attemptNumber": 0,
                            "objectId": 123,
                            "changeSource": "CRM",
                            "changeFlag": "NEW"
                          }
                    ]
                    """;

            mockMvc.perform(post("/webhook")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(payloadJson))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].eventId").value("100"))
                    .andExpect(jsonPath("$[0].subscriptionType").value("contact.creation"));
        }
    }
}