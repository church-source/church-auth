package org.churchsource.churchauth.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.churchsource.churchauth.security.jwt.tokenblacklist.BlacklistTokenEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CPLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private ApplicationEventPublisher blackListTokenEventPublisher;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        //noinspection DuplicatedCode
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            try {
                blackListTokenEventPublisher.publishEvent(new BlacklistTokenEvent(this, jwtToken));
            } catch (IllegalArgumentException e) {
                log.error("JWT_TOKEN_UNABLE_TO_GET_USERNAME", e);
            } catch (ExpiredJwtException e) {
                log.warn("JWT_TOKEN_EXPIRED", e);
            }
        } else {
            log.warn("JWT_TOKEN_DOES_NOT_START_WITH_BEARER_STRING");
        }
    }
}
