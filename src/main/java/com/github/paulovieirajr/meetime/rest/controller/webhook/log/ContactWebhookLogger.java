package com.github.paulovieirajr.meetime.rest.controller.webhook.log;

public enum ContactWebhookLogger {
    LOG_PAYLOAD_RECEIVED("Payload recebido do HubSpot: {}");

    private final String message;

    ContactWebhookLogger(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
