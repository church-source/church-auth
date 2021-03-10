package org.churchsource.churchauth.security.jwt.helpers;

import org.churchsource.churchauth.helpers.AbstractTypeSafeMatcher;
import org.churchsource.churchauth.security.jwt.JwtTokenRequest;
import org.hamcrest.Description;

import java.util.Objects;

public class JwtTokenRequestMatcher extends AbstractTypeSafeMatcher<JwtTokenRequest> {

  public JwtTokenRequestMatcher(JwtTokenRequest expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, JwtTokenRequest jwtTokenRequest) {
    description.appendText("A JwtTokenRequest with the following state:")
        .appendText("\nUser Name: ").appendValue(jwtTokenRequest.getUsername())
        .appendText("\nPassword: ").appendValue(jwtTokenRequest.getPassword());
  }

  @Override
  protected boolean matchesSafely(JwtTokenRequest actual) {
    return Objects.equals(actual.getUsername(), expected.getUsername())
        && Objects.equals(actual.getPassword(), expected.getPassword());
  }

  public static JwtTokenRequestMatcher hasSameStateAsJwtTokenRequest(JwtTokenRequest expected) {
    return new JwtTokenRequestMatcher(expected);
  }

}
