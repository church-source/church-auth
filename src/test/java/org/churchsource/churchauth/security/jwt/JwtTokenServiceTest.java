package org.churchsource.churchauth.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.churchsource.churchauth.user.CPUserDetails;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;


import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.churchsource.churchauth.user.CPUserDetails.aCPUserDetails;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenServiceTest {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Value("${jwt.signing.key.secret}")
    private String secret;

    @Test
    public void testGetExpirationDateFromToken_shouldReturnCorrectExpirationDate() {
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(10));
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject("subject").setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        Date d = jwtTokenService.getExpirationDateFromToken(token);
        assertThat(d, is(expirationDate));
    }

    @Test(expected = ExpiredJwtException.class)
    public void testGetExpirationDateFromExpiredToken_shouldThrowException() {
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject("subject").setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        jwtTokenService.getExpirationDateFromToken(token);
    }

    @Test
    public void testGetUserNameFromToken_shouldReturnCorrectUserName() {
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(10));
        Map<String, Object> claims = new HashMap<String, Object>();
        String username = "username";
        String token =  Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        String name = jwtTokenService.getUsernameFromToken(token);
        assertThat(name, is(username));
    }

    @Test
    public void testGetIssuedAtDateFromToken_shouldReturnCorrectIssuedAtDate() {
        Date date = new Date();
        Instant instant = date.toInstant();
        date = Date.from(instant.truncatedTo(ChronoUnit.SECONDS));
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(10));
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject("subject").setIssuedAt(date)
                .setExpiration(expirationDate).setIssuedAt(date).signWith(SignatureAlgorithm.HS512, secret).compact();
        Date d = jwtTokenService.getIssuedAtDateFromToken(token);
        assertThat(d, is(date));
    }

    @Test
    public void testGetReasonFromToken_shouldReturnCorrectReasonDate() {
        Date date = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(10));
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(JwtTokenService.JWT_TOKEN_REASON, JwtTokenService.JWT_TOKEN_REASON_PASSWORD_CHANGE);
        String token =  Jwts.builder().setClaims(claims).setSubject("subject").setIssuedAt(date)
                .setExpiration(expirationDate).setIssuedAt(date).signWith(SignatureAlgorithm.HS512, secret).compact();
        String reason = jwtTokenService.getReasonFromToken(token);
        assertThat(reason, is(JwtTokenService.JWT_TOKEN_REASON_PASSWORD_CHANGE));
    }

    @Test
    public void testIsTokenExpiredForExpiredToken_shouldReturnTrue() {
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject("subject").setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        Boolean isTokenExpired = jwtTokenService.isTokenExpired(token);
        assertThat(isTokenExpired, is(true));
    }

    @Test
    public void testIsTokenExpiredForNonExpiredToken_shouldReturnFalse() {
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject("subject").setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        Boolean isTokenExpired = jwtTokenService.isTokenExpired(token);
        assertThat(isTokenExpired, is(false));
    }


    @Test
    public void testGenerateToken_shouldGenerateToken() {
        String username = "username";
        CPUserDetails userDetails = aCPUserDetails().username(username).build();
        String generatedToken = jwtTokenService.generateToken(userDetails);
        String tokenUserName = (String)Jwts.parser().setSigningKey(secret).parseClaimsJws(generatedToken).getBody().getSubject();
        assertThat(tokenUserName, is(username));
    }

    @Test
    public void testGenerateForceChangePasswordToken_shouldGenerateTokenWithCorrectReason() {
        String username = "username";
        String generatedToken = jwtTokenService.generateForceChangePasswordToken(username);
        String tokenUserName = (String)Jwts.parser().setSigningKey(secret).parseClaimsJws(generatedToken).getBody().getSubject();
        String reason = (String)Jwts.parser().setSigningKey(secret).parseClaimsJws(generatedToken).getBody().get(JwtTokenService.JWT_TOKEN_REASON);
        assertThat(tokenUserName, is(username));
        assertThat(reason, is(JwtTokenService.JWT_TOKEN_REASON_PASSWORD_CHANGE));
    }

    @Test
    public void testCanTokenBeRefreshed_shouldReturnTrue() {
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject("subject").setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        Boolean canTokenBeRefreshed = jwtTokenService.canTokenBeRefreshed(token);
        assertThat(canTokenBeRefreshed, is(true));
    }

    @Test
    public void testCanTokenBeRefreshedForExpiredToken_shouldReturnFalse() {
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject("subject").setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        Boolean canTokenBeRefreshed = jwtTokenService.canTokenBeRefreshed(token);
        assertThat(canTokenBeRefreshed, is(false));
    }

    @Test
    public void testValidateValidToken_shouldReturnTrue() {
        String username = "username";
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        CPUserDetails userDetails = aCPUserDetails().username(username).build();
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        Boolean isTokenValid = jwtTokenService.validateToken(token,userDetails);
        assertThat(isTokenValid, is(true));
    }

    @Test
    public void testValidateValidTokenWithEmail_shouldReturnTrue() {
        String email = "username@test.com";
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        CPUserDetails userDetails = aCPUserDetails().email(email).build();
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject(email).setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        Boolean isTokenValid = jwtTokenService.validateToken(token, userDetails);
        assertThat(isTokenValid, is(true));
    }

    @Test
    public void testValidateValidTokenWithExpiredToken_shouldReturnFalse() {
        String username = "username";
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        CPUserDetails userDetails = aCPUserDetails().username(username).build();
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        Boolean isTokenValid = jwtTokenService.validateToken(token,userDetails);
        assertThat(isTokenValid, is(false));
    }

    @Test
    public void testValidateValidTokenWithEmailAndExpiredToken_shouldReturnFalse() {
        String email = "username@test.com";
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().minusDays(1));
        CPUserDetails userDetails = aCPUserDetails().email(email).build();
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject(email).setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        Boolean isTokenValid = jwtTokenService.validateToken(token, userDetails);
        assertThat(isTokenValid, is(false));
    }

    @Test
    public void testRefreshToken_shouldReturnNewToken() {
        String username = "username";
        final Date createdDate = new Date();
        final Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        Map<String, Object> claims = new HashMap<String, Object>();
        String token =  Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate)
                .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret).compact();
        String newToken = jwtTokenService.refreshToken(token);
        assertThat(newToken, is(not(equalTo(null))));
        String tokenUserName = (String)Jwts.parser().setSigningKey(secret).parseClaimsJws(newToken).getBody().getSubject();
        assertThat(tokenUserName, is(username));
        //TODO shouldn't really call another method in the actual code when asserting.
        Date newExpirationDate = jwtTokenService.getExpirationDateFromToken(newToken);
        assertThat(newExpirationDate.getTime(), greaterThan(createdDate.getTime()));
    }
}
