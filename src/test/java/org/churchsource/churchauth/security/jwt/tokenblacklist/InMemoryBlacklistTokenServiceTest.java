package org.churchsource.churchauth.security.jwt.tokenblacklist;

import org.churchsource.churchauth.security.jwt.JwtTokenService;
import org.churchsource.churchauth.user.CPUserDetailsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InMemoryBlacklistTokenServiceTest {
    @InjectMocks
    private InMemoryTokenBlacklistService inMemoryBlacklistTokenService;

    @Mock
    private JwtTokenService jwtTokenService;

    @Autowired
    private CPUserDetailsService userDetailsService;

    @Test
    public void testIsTokenBlacklistedForBlacklistedToken_shouldReturnTrue() {
        InMemoryTokenBlacklistService imtbls = Mockito.spy(inMemoryBlacklistTokenService);
        Map<String, Set<String>> blacklistTokenCache = new HashMap<>();
        Set<String> tokenSet = new HashSet<>();
        tokenSet.add("token1");
        blacklistTokenCache.put("name", tokenSet);
        Mockito.when(imtbls.getBlacklistTokenCache()).thenReturn(blacklistTokenCache);
        Boolean isBlacklisted = imtbls.isTokenBlacklisted("name", "token1");
        assertThat(isBlacklisted, is(true));
    }

    @Test
    public void testIsTokenBlacklistedForNotBlacklistedToken_shouldReturnFalse() {
        InMemoryTokenBlacklistService imtbls = Mockito.spy(inMemoryBlacklistTokenService);
        Map<String, Set<String>> blacklistTokenCache = new HashMap<>();
        Set<String> tokenSet = new HashSet<>();
        tokenSet.add("token1");
        blacklistTokenCache.put("name", tokenSet);
        Mockito.when(imtbls.getBlacklistTokenCache()).thenReturn(blacklistTokenCache);
        Boolean isBlacklisted = imtbls.isTokenBlacklisted("name", "token2");
        assertThat(isBlacklisted, is(false));
    }

    @Test
    public void testIsTokenBlacklistedForNotBlacklistedTokenAndTokenSetNull_shouldReturnFalse() {
        InMemoryTokenBlacklistService imtbls = Mockito.spy(inMemoryBlacklistTokenService);
        Map<String, Set<String>> blacklistTokenCache = new HashMap<>();
        Mockito.when(imtbls.getBlacklistTokenCache()).thenReturn(blacklistTokenCache);
        Boolean isBlacklisted = imtbls.isTokenBlacklisted("name", "token2");
        assertThat(isBlacklisted, is(false));
    }

    @Test
    public void testBlacklistTokenForUserThatHasNoBlacklistedTokensBefore_shouldBeAddedToblackListTokenCache() {
        InMemoryTokenBlacklistService imtbls = Mockito.spy(inMemoryBlacklistTokenService);
        Map<String, Set<String>> blacklistTokenCache = new HashMap<>();
        Mockito.when(jwtTokenService.isTokenExpired("token1")).thenReturn(false);
        Mockito.when(imtbls.getBlacklistTokenCache()).thenReturn(blacklistTokenCache);
        imtbls.blacklistToken("name", "token1");
        Boolean wasBlacklisted = blacklistTokenCache.get("name").contains("token1");
        assertThat(wasBlacklisted, is(true));
    }

    @Test
    public void testBlacklistTokenForUserThatHasBlacklistedTokensBefore_shouldBeAddedToblackListTokenCache() {
        InMemoryTokenBlacklistService imtbls = Mockito.spy(inMemoryBlacklistTokenService);
        Map<String, Set<String>> blacklistTokenCache = new HashMap<>();
        Set<String> tokenSet = new HashSet<>();
        tokenSet.add("token1");
        blacklistTokenCache.put("name", tokenSet);
        Mockito.when(jwtTokenService.isTokenExpired("token1")).thenReturn(false);
        Mockito.when(jwtTokenService.isTokenExpired("token2")).thenReturn(false);

        Mockito.when(imtbls.getBlacklistTokenCache()).thenReturn(blacklistTokenCache);
        imtbls.blacklistToken("name", "token2");
        Boolean wasBlacklisted = blacklistTokenCache.get("name").contains("token2");
        assertThat(wasBlacklisted, is(true));
        Boolean isToken1StillBlacklisted = blacklistTokenCache.get("name").contains("token1");
        assertThat(isToken1StillBlacklisted, is(true));
    }

    @Test
    public void testBlacklistTokenForUserThatHasBlacklistedTokensBeforeButPreviousExpired_shouldBeAddedToblackListTokenCache() {
        InMemoryTokenBlacklistService imtbls = Mockito.spy(inMemoryBlacklistTokenService);
        Map<String, Set<String>> blacklistTokenCache = new HashMap<>();
        Set<String> tokenSet = new HashSet<>();
        tokenSet.add("token1");
        tokenSet.add("token2");
        blacklistTokenCache.put("name", tokenSet);
        Mockito.when(jwtTokenService.isTokenExpired("token1")).thenReturn(true);
        Mockito.when(jwtTokenService.isTokenExpired("token2")).thenReturn(true);
        Mockito.when(imtbls.getBlacklistTokenCache()).thenReturn(blacklistTokenCache);
        imtbls.blacklistToken("name", "token3");
        Boolean wasBlacklisted = blacklistTokenCache.get("name").contains("token3");
        assertThat(wasBlacklisted, is(true));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Boolean isToken1StillBlacklisted = blacklistTokenCache.get("name").contains("token1");
        assertThat(isToken1StillBlacklisted, is(false));
        Boolean isToken2StillBlacklisted = blacklistTokenCache.get("name").contains("token2");
        assertThat(isToken2StillBlacklisted, is(false));
    }

    @Test
    public void testOnApplicationEvent_shouldCallBlacklistForUserAndToken() {
        InMemoryTokenBlacklistService imtbls = Mockito.spy(inMemoryBlacklistTokenService);
        Mockito.when(jwtTokenService.getUsernameFromToken("token1")).thenReturn("username");
        imtbls.onApplicationEvent(new BlacklistTokenEvent(this, "token1"));
        Mockito.verify(imtbls, times(1)).blacklistToken("username","token1");
    }

    @Test
    public void testOnApplicationEventWithEmptyUserName_shouldNotCallBlacklistForUserAndToken() {
        InMemoryTokenBlacklistService imtbls = Mockito.spy(inMemoryBlacklistTokenService);
        Mockito.when(jwtTokenService.getUsernameFromToken("token1")).thenReturn("");
        imtbls.onApplicationEvent(new BlacklistTokenEvent(this, "token1"));
        Mockito.verify(imtbls, never()).blacklistToken("","token1");
    }

    @Test
    public void testOnApplicationEventWithNullUserName_shouldNotCallBlacklistForUserAndToken() {
        InMemoryTokenBlacklistService imtbls = Mockito.spy(inMemoryBlacklistTokenService);
        Mockito.when(jwtTokenService.getUsernameFromToken("token1")).thenReturn(null);
        imtbls.onApplicationEvent(new BlacklistTokenEvent(this, "token1"));
        Mockito.verify(imtbls, never()).blacklistToken(null,"token1");
    }

}
