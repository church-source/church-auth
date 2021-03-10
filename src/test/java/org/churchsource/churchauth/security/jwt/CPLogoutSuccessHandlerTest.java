package org.churchsource.churchauth.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.churchsource.churchauth.security.jwt.tokenblacklist.BlacklistTokenEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CPLogoutSuccessHandlerTest {

    @InjectMocks
    private CPLogoutSuccessHandler cpLogoutSuccessHandler;

    @Mock
    private ApplicationEventPublisher blackListTokenEventPublisher;

    @Test
    public void testOnLogoutSuccess_shouldBlacklistToken() {
        CPLogoutSuccessHandler cplsh = Mockito.spy(cpLogoutSuccessHandler);

        HttpServletRequest req = Mockito.spy(new MockHttpServletRequest());
        when(req.getHeader("Authorization")).thenReturn("Bearer 123");
        BlacklistTokenEvent blb = new BlacklistTokenEvent(cpLogoutSuccessHandler, "123");
        cplsh.onLogoutSuccess(req, null, null);
        Mockito.verify(blackListTokenEventPublisher, times(1)).publishEvent(blb);
    }

    @Test
    public void testOnLogoutSuccessWithNoToken_shouldNotAttemptToBlacklistToken() {
        CPLogoutSuccessHandler cplsh = Mockito.spy(cpLogoutSuccessHandler);

        HttpServletRequest req = Mockito.spy(new MockHttpServletRequest());
        when(req.getHeader("Authorization")).thenReturn(null);
        BlacklistTokenEvent blb = new BlacklistTokenEvent(cpLogoutSuccessHandler, "123");
        cplsh.onLogoutSuccess(req, null, null);
        Mockito.verify(blackListTokenEventPublisher, never()).publishEvent(blb);
    }

    @Test
    public void testOnLogoutSuccessWithNoBearerToken_shouldNotAttemptToBlacklistToken() {
        CPLogoutSuccessHandler cplsh = Mockito.spy(cpLogoutSuccessHandler);

        HttpServletRequest req = Mockito.spy(new MockHttpServletRequest());
        when(req.getHeader("Authorization")).thenReturn("123");
        BlacklistTokenEvent blb = new BlacklistTokenEvent(cpLogoutSuccessHandler, "123");
        cplsh.onLogoutSuccess(req, null, null);
        Mockito.verify(blackListTokenEventPublisher, never()).publishEvent(blb);
    }

    @Test
    public void testOnLogoutSuccessWithNoInvalidBearerToken_shouldNotAttemptToBlacklistToken() {
        CPLogoutSuccessHandler cplsh = Mockito.spy(cpLogoutSuccessHandler);

        HttpServletRequest req = Mockito.spy(new MockHttpServletRequest());

        when(req.getHeader("Authorization")).thenReturn("Bearer 123");
        doThrow(new IllegalArgumentException()).when(blackListTokenEventPublisher).publishEvent(ArgumentMatchers.any());
        cplsh.onLogoutSuccess(req, null, null);
    }

    @Test
    public void testOnLogoutSuccessWithNoExpiredBearerToken_shouldNotAttemptToBlacklistToken() {
        CPLogoutSuccessHandler cplsh = Mockito.spy(cpLogoutSuccessHandler);
        HttpServletRequest req = Mockito.spy(new MockHttpServletRequest());

        when(req.getHeader("Authorization")).thenReturn("Bearer 123");
        doThrow(new ExpiredJwtException(null, null, null)).when(blackListTokenEventPublisher).publishEvent(ArgumentMatchers.any());
        cplsh.onLogoutSuccess(req, null, null);
    }
}
