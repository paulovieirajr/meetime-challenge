package com.github.paulovieirajr.meetime.rest.controller.webhook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.github.paulovieirajr.meetime.rest.constants.HubSpotPathController.WEBHOOK_PATH;
import static com.github.paulovieirajr.meetime.rest.controller.webhook.log.ContactWebhookLogger.LOG_PAYLOAD_RECEIVED;

@RestController
public class HubSpotContactWebhookController implements HubSpotContactWebhookControllerSwagger {

    private static final Logger LOGGER = LoggerFactory.getLogger(HubSpotContactWebhookController.class);

    @PostMapping(WEBHOOK_PATH)
    public ResponseEntity<?> receiveContactCreatedWebhook(List<Map<String, Object>> payload) {
        LOGGER.info(LOG_PAYLOAD_RECEIVED.getMessage(), payload);
        return ResponseEntity.ok(payload);
    }
}
