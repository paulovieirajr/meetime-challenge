package com.github.paulovieirajr.meetime.rest.controller.webhook;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.github.paulovieirajr.meetime.rest.constants.HubSpotPathController.WEBHOOK_PATH;

@RestController
public class HubSpotContactWebhookController implements HubSpotContactWebhookControllerSwagger{

    @PostMapping(WEBHOOK_PATH)
    public ResponseEntity<?> receiveContactCreatedWebhook(Map<String, Object> payload) {
        return ResponseEntity.ok(payload);
    }
}
