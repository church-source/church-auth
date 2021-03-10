package org.churchsource.churchauth.security.jwt.helpers;

import org.churchsource.churchauth.helpers.AbstractTypeSafeMatcher;
import org.churchsource.churchauth.security.jwt.JwtTokenResponse;
import org.hamcrest.Description;

import java.util.Objects;

public class JwtTokenResponseMatcher extends AbstractTypeSafeMatcher<JwtTokenResponse> {

  public JwtTokenResponseMatcher(JwtTokenResponse expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, JwtTokenResponse jwtTokenResponse) {
    description.appendText("A JwtTokenResponse with the following state:")
        .appendText("\nToken: ").appendValue(jwtTokenResponse.getToken());
  }

  @Override
  protected boolean matchesSafely(JwtTokenResponse actual) {
    return Objects.equals(actual.getToken(), expected.getToken());
  }

  public static JwtTokenResponseMatcher hasSameStateAsJwtTokenResponse(JwtTokenResponse expected) {
    return new JwtTokenResponseMatcher(expected);
  }

}
