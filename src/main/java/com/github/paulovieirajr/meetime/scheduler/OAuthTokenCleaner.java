package com.github.paulovieirajr.meetime.scheduler;

import com.github.paulovieirajr.meetime.token.OAuthSessionStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OAuthTokenCleaner {

    private final OAuthSessionStore oAuthSessionStore;

    public OAuthTokenCleaner(OAuthSessionStore oAuthSessionStore) {
        this.oAuthSessionStore = oAuthSessionStore;
    }

    @Scheduled(cron = "${hubspot.scheduler.cron}")
    public void cleanExpiredTokens() {
        oAuthSessionStore.cleanExpiredTokens();
    }
}