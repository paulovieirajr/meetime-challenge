package com.github.paulovieirajr.meetime.rest.constants;

public enum HubSpotParams {
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    REDIRECT_URI("redirect_uri"),
    SCOPE("scope"),
    CODE("code"),
    GRANT_TYPE("grant_type"),
    AUTHORIZATION_CODE("authorization_code"),
    REFRESH_TOKEN("refresh_token"),
    SESSION_ID("session_id"),
    PROPERTIES("properties");

    private final String value;

    HubSpotParams(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
