package com.github.paulovieirajr.meetime.rest.controller.webhook;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Tag(name = "HubSpot Contact Webhook", description = "Operations related to HubSpot contact webhooks")
public interface HubSpotContactWebhookControllerSwagger {

    @Operation(summary = "Only Triggered when a contact is created in HubSpot", description = "This endpoint is triggered when a contact is created in HubSpot. It receives the contact data and processes it accordingly.")
    @ApiResponse(responseCode = "200", description = "Webhook received successfully")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @ApiResponse(responseCode = "503", description = "Service unavailable")
    ResponseEntity<?> receiveContactCreatedWebhook(@RequestBody List<Map<String, Object>> payload);
}
