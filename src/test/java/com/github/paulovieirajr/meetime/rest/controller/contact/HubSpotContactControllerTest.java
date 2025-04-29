package com.github.paulovieirajr.meetime.rest.controller.contact;

import com.github.paulovieirajr.meetime.rest.client.HubSpotContactClient;
import com.github.paulovieirajr.meetime.rest.dto.HubSpotContactResponse;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HubSpotContactController.class)
class HubSpotContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HubSpotContactClient hubSpotContactClient;

    @Nested
    class CreateContact {

        @Test
        @DisplayName("Should create a contact successfully when Bearer token is valid")
        void shouldCreateContactSuccessfully() throws Exception {
            String bearerToken = "Bearer valid-token";

            HubSpotContactResponse contactResponse = new HubSpotContactResponse(
                    "117735199472",
                    "2025-04-28T05:44:30.459Z"
            );

            when(hubSpotContactClient.createContact(eq(bearerToken), any())).thenReturn(contactResponse);

            mockMvc.perform(post("/create-contact")
                            .header("Authorization", bearerToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                        {
                                            "firstname": "John",
                                            "lastname": "Doe",
                                            "email": "john.doe@example.com"
                                        }
                                    """))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value("117735199472"))
                    .andExpect(jsonPath("$.createdAt").value("2025-04-28T05:44:30.459Z"));

            verify(hubSpotContactClient, times(1)).createContact(eq(bearerToken), any());
        }

        @Test
        @DisplayName("Should throw error when Bearer token is invalid")
        void shouldThrowErrorWhenBearerTokenInvalid() throws Exception {
            String invalidToken = "InvalidToken";

            mockMvc.perform(post("/create-contact")
                            .header("Authorization", invalidToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                        {
                                            "firstname": "Jane",
                                            "lastname": "Smith",
                                            "email": "jane.smith@example.com"
                                        }
                                    """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 409 Conflict when contact already exists")
        void shouldReturnConflictWhenContactAlreadyExists() throws Exception {
            String bearerToken = "Bearer valid-token";

            FeignException conflictException = FeignException.errorStatus(
                    "createContact",
                    Response.builder()
                            .status(409)
                            .reason("Conflict")
                            .request(Request.create(Request.HttpMethod.POST, "/hubspot/create-contact", Map.of(), null, Charset.defaultCharset(), null))
                            .build()
            );

            when(hubSpotContactClient.createContact(eq(bearerToken), any()))
                    .thenThrow(conflictException);

            mockMvc.perform(post("/create-contact")
                            .header("Authorization", bearerToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                        {
                                            "firstname": "John",
                                            "lastname": "Doe",
                                            "email": "john.doe@example.com"
                                        }
                                    """))
                    .andExpect(status().isConflict());
        }
    }
}