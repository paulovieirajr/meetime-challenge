package com.github.paulovieirajr.meetime.rest.controller.oauth.log;

public enum OAuthLogger {
    LOG_OAUTH_URL_ENDPOINT("[OAUTH] - Acessando o endpoint /oauth-url"),
    LOG_GENERATE_OAUTH_URL("[OAUTH] - Gerando URL de autorização do HubSpot"),
    LOG_SUCCESS_OAUTH_URL("[OAUTH] - URL de autorização gerada com sucesso"),
    LOG_OAUTH_CALLBACK_ENDPOINT("[OAUTH] - Acessando o endpoint /oauth-callback com o código: <{}>"),
    LOG_OAUTH_BUILDING_REQUEST("[OAUTH] - Montando o corpo da requisição para troca de token"),
    LOG_OAUTH_HUBSPOT_TOKEN_ENDPOINT("[OAUTH] - Acessando o endpoint de troca de token do HubSpot"),
    LOG_OAUTH_HUBSPOT_TOKEN_SUCCESS("[OAUTH] - Token de acesso recebido com sucesso"),
    LOG_NEW_SESSION_ID("[OAUTH] - Criando nova sessão com ID: <{}>"),
    LOG_REDIRECTING("[OAUTH] - Redirecionando para a URL de sucesso com o sessionId: <{}>"),
    LOG_ACCESS_TOKEN_ENDPOINT("[OAUTH] - Acessando o endpoint /access-token com o sessionId: <{}>"),
    LOG_TOKEN_RECOVERED_SUCCESS("[OAUTH] - Token de acesso recuperado com sucesso");


    private final String message;

    OAuthLogger(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
