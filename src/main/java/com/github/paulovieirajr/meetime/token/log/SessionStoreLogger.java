package com.github.paulovieirajr.meetime.token.log;

public enum SessionStoreLogger {
    LOG_SAVING_TOKEN("Salvando o token para a sessão <{}>"),
    LOG_RETRIEVING_TOKEN("[SESSION] - Recuperando o token de acesso da sessão <{}>"),
    LOG_SESSION_NOT_FOUND("[SESSION] - Sessão não encontrada para o sessionId <{}>"),
    LOG_TOKEN_EXPIRED("[SESSION] - O token de acesso está expirado, tentando renová-lo"),
    LOG_TOKEN_VALID("[SESSION] - O token de acesso está válido, retornando-o"),
    LOG_TOKEN_REFRESHED("[SESSION] - O token de acesso foi renovado com sucesso"),
    LOG_BUILDING_REQUEST("[SESSION] - Montando o corpo da requisição para troca de token"),
    LOG_OAUTH_HUBSPOT_TOKEN_ENDPOINT("[SESSION] - Acessando o endpoint de troca de token do HubSpot");

    private final String message;

    SessionStoreLogger(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
