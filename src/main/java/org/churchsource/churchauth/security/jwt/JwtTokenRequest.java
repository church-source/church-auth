package org.churchsource.churchauth.security.jwt;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "aJwtTokenRequest")
@EqualsAndHashCode
@ToString
public class JwtTokenRequest implements Serializable {
  
  private static final long serialVersionUID = -5616176897013108345L;

  private String username;
  private String password;
}

