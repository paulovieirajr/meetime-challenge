package com.github.paulovieirajr.meetime.scheduler;

import com.github.paulovieirajr.meetime.token.OAuthSessionStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OAuthTokenCleanerTest {

    @Mock
    private OAuthSessionStore oAuthSessionStore;

    @InjectMocks
    private OAuthTokenCleaner oAuthTokenCleaner;

    @Test
    @DisplayName("Should call session store to clean expired tokens")
    void shouldCallCleanExpiredTokens() {
        oAuthTokenCleaner.cleanExpiredTokens();
        verify(oAuthSessionStore, times(1)).cleanExpiredTokens();
    }
}