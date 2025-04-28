package com.github.paulovieirajr.meetime.rest.controller.contact.log;

public enum ContactLogger {
    LOG_CREATE_CONTACT_ENDPOINT("[CONTACT] - Acessando o endpoint /create-contact"),
    LOG_BEARER_TOKEN_INVALID("[CONTACT] - Bearer token inválido"),
    LOG_BUILD_REQUEST_BODY("[CONTACT] - Montando o corpo da requisição para criar o contato"),
    LOG_SEND_REQUEST("[CONTACT] - Enviando a requisição ao HubSpot para criar o contato"),
    LOG_CONTACT_CREATED_SUCCESS("[CONTACT] - Contato criado com sucesso: {}");

    private final String message;

    ContactLogger(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
