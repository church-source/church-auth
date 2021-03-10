package org.churchsource.churchauth.security.jwt;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "aJwtTokenResponse")
@EqualsAndHashCode
@ToString
public class JwtTokenResponse implements Serializable {

  private static final long serialVersionUID = 8317676219297719109L;

  private String token;
}