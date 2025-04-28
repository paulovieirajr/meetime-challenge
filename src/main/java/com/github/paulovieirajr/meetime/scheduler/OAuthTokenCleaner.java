package com.github.paulovieirajr.meetime.scheduler;

import com.github.paulovieirajr.meetime.token.OAuthSessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.github.paulovieirajr.meetime.scheduler.log.SchedulerLogger.LOG_REMOVING_EXPIRED_TOKENS;

@Component
public class OAuthTokenCleaner {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthTokenCleaner.class);

    private final OAuthSessionStore oAuthSessionStore;

    public OAuthTokenCleaner(OAuthSessionStore oAuthSessionStore) {
        this.oAuthSessionStore = oAuthSessionStore;
    }

    @Scheduled(cron = "${hubspot.scheduler.cron}")
    public void cleanExpiredTokens() {
        LOGGER.info(LOG_REMOVING_EXPIRED_TOKENS.getMessage());
        oAuthSessionStore.cleanExpiredTokens();
    }
}