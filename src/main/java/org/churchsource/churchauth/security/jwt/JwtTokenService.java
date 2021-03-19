package org.churchsource.churchauth.security.jwt;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.*;
import org.churchsource.churchauth.user.CPUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;

import io.jsonwebtoken.impl.DefaultClock;

@Component
public class JwtTokenService implements Serializable {

  static final String CLAIM_KEY_USERNAME = "sub";
  static final String CLAIM_KEY_CREATED = "iat";
  private static final long serialVersionUID = -3301605591108950415L;
  public static final String JWT_TOKEN_REASON = "reason";
  public static final String JWT_TOKEN_REASON_PASSWORD_CHANGE = "passwordChange";
  public static final String JWT_TOKEN_PRIVILEGES = "privileges";
  public static final String JWT_TOKEN_AUDIENCE = "audience";
  public static final String JWT_TOKEN_ISSUER = "issuer";


    private Clock clock = DefaultClock.INSTANCE;

  @Value("${jwt.signing.key.secret}")
  private String secret;

  @Value("${jwt.token.expiration.in.seconds}")
  private Long expiration;

  @Value("${jwt.passwordchangetoken.expiration.in.seconds}")
  private Long passwordChangeTokenExpiration;

  public String getUsernameFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getIssuedAtDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getIssuedAt);
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public String getReasonFromToken(String token) {
    return (String)Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody().get(JWT_TOKEN_REASON);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
  }

  public Boolean isTokenExpired(String token) {
    try {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    } catch(ExpiredJwtException e) {
        return true;
    }
  }

  private Boolean ignoreTokenExpiration(String token) {
    // here you specify tokens, for that the expiration is ignored
    return false;
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
    String privs = "";
    if(authorities != null) {
      for(GrantedAuthority authority : authorities) {
          if(!"".equals(privs))
              privs+= ",";
          privs+= authority.getAuthority();
      }
    }

    claims.put(JWT_TOKEN_PRIVILEGES, privs);
    claims.put(JWT_TOKEN_AUDIENCE, "");
    claims.put(JWT_TOKEN_ISSUER, "https://mvsongs.co.za");
    return doGenerateToken(claims, userDetails.getUsername());
  }

  public String generateForceChangePasswordToken(String subject) {
    final Date createdDate = clock.now();
    final Date expirationDate = calculatePasswordChangeExpirationDate(createdDate);
    Map<String, Object> claims = new HashMap<String, Object>();
    claims.put(JWT_TOKEN_REASON, JWT_TOKEN_REASON_PASSWORD_CHANGE);
    return Jwts.builder().setClaims(claims).setSubject(subject).setAudience("https://mvsongs.co.za").setIssuer("https://mvsongs.co.za").setIssuedAt(createdDate)
            .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
  }

  private String doGenerateToken(Map<String, Object> claims, String subject) {
    final Date createdDate = clock.now();
    final Date expirationDate = calculateExpirationDate(createdDate);
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(createdDate).setAudience("https://mvsongs.co.za").setIssuer("https://mvsongs.co.za")
        .setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
  }

  public Boolean canTokenBeRefreshed(String token) {
    return (!isTokenExpired(token) || ignoreTokenExpiration(token));
  }

  public String refreshToken(String token) {
    final Date createdDate = clock.now();
    final Date expirationDate = calculateExpirationDate(createdDate);

    final Claims claims = getAllClaimsFromToken(token);
    claims.setIssuedAt(createdDate);
    claims.setExpiration(expirationDate);

    return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    CPUserDetails user = (CPUserDetails) userDetails;
    try {
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) || username.equals(user.getEmail()) && !isTokenExpired(token));
    } catch (ExpiredJwtException e) {
        return false;
    }
  }

  private Date calculateExpirationDate(Date createdDate) {
    return new Date(createdDate.getTime() + expiration * 1000);
  }

  private Date calculatePasswordChangeExpirationDate(Date createdDate) {
    return new Date(createdDate.getTime() + passwordChangeTokenExpiration * 1000);
  }
}

