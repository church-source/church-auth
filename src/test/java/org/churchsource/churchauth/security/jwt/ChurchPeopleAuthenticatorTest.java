package org.churchsource.churchauth.security.jwt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;


import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChurchPeopleAuthenticatorTest {

    @InjectMocks
    private ChurchPeopleAuthenticator churchPeopleAuthenticator;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void testAuthenticate_shouldAuthenticate() {
        ChurchPeopleAuthenticator cpa = Mockito.spy(churchPeopleAuthenticator);
        String username = "username";
        String password = "password";
        cpa.authenticate(username, password);
        UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(username, password);
        Mockito.verify(authenticationManager, times(1)).authenticate(ArgumentMatchers.eq(t));
    }

    @Test(expected=NullPointerException.class)
    public void testAuthenticateWithNullUserName_shouldThrowNullPointerException() {
        ChurchPeopleAuthenticator cpa = Mockito.spy(churchPeopleAuthenticator);
        String username = null;
        String password = "password";
        cpa.authenticate(username, password);
    }

    @Test(expected=NullPointerException.class)
    public void testAuthenticateWithNullPassword_shouldThrowNullPointerException() {
        ChurchPeopleAuthenticator cpa = Mockito.spy(churchPeopleAuthenticator);
        String username = "username";
        String password = null;
        cpa.authenticate(username, password);
    }

    @Test(expected=AuthenticationException.class)
    public void testAuthenticateWithDisabledUser_shouldThrowAuthenticationException() {
        ChurchPeopleAuthenticator cpa = Mockito.spy(churchPeopleAuthenticator);

        String username = "username";
        String password = "password";
        UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(username, password);
        when(authenticationManager.authenticate(ArgumentMatchers.eq(t))).thenThrow(new DisabledException("test"));
        cpa.authenticate(username, password);
    }

    @Test(expected=AuthenticationException.class)
    public void testAuthenticateWithBadCredentials_shouldThrowAuthenticationException() {
        ChurchPeopleAuthenticator cpa = Mockito.spy(churchPeopleAuthenticator);

        String username = "username";
        String password = "password";
        UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(username, password);
        when(authenticationManager.authenticate(ArgumentMatchers.eq(t))).thenThrow(new BadCredentialsException("test"));
        cpa.authenticate(username, password);
    }
}
