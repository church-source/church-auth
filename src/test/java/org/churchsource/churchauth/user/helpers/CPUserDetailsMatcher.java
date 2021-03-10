package org.churchsource.churchauth.user.helpers;

import org.churchsource.churchauth.helpers.AbstractTypeSafeMatcher;
import org.churchsource.churchauth.user.CPUserDetails;
import org.hamcrest.Description;

import java.util.Objects;

public class CPUserDetailsMatcher extends AbstractTypeSafeMatcher<CPUserDetails> {

  public CPUserDetailsMatcher(CPUserDetails expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, CPUserDetails userDetails) {
    description.appendText("A CPUserDetails with the following state:")
        .appendText("\nId: ").appendValue(userDetails.getId())
        .appendText("\nUser Name: ").appendValue(userDetails.getUsername())
        .appendText("\nEmail: ").appendValue(userDetails.getEmail())
        .appendText("\nPassword: ").appendValue(userDetails.getPassword())
        .appendText("\nEnabled: ").appendValue(userDetails.isEnabled())
        .appendText("\nAccount NonExpired: ").appendValue(userDetails.isAccountNonExpired())
        .appendText("\nAccount NonLocked: ").appendValue(userDetails.isAccountNonLocked())
        .appendText("\nCredentials NonExpired: ").appendValue(userDetails.isCredentialsNonExpired())
        .appendText("\nDeleted: ").appendValue(userDetails.getDeleted())
        .appendText("\nForcePasswordChange: ").appendValue(userDetails.isForcePasswordChange())
        .appendText("\nRoles: ").appendValue(userDetails.getRoles());
  }

  @Override
  protected boolean matchesSafely(CPUserDetails actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getUsername(), expected.getUsername())
        && Objects.equals(actual.getEmail(), expected.getEmail())
        && Objects.equals(actual.getPassword(), expected.getPassword())
        && Objects.equals(actual.isEnabled(), expected.isEnabled())
        && Objects.equals(actual.isAccountNonExpired(), expected.isAccountNonExpired())
        && Objects.equals(actual.isAccountNonLocked(), expected.isAccountNonLocked())
        && Objects.equals(actual.isCredentialsNonExpired(), expected.isCredentialsNonExpired())
        && Objects.equals(actual.isForcePasswordChange(), expected.isForcePasswordChange())
        && Objects.equals(actual.getDeleted(), expected.getDeleted())
        && isListsEqual(actual.getRoles(), expected.getRoles());
  }

  public static CPUserDetailsMatcher hasSameStateAsCPUserDetails(CPUserDetails expected) {
    return new CPUserDetailsMatcher(expected);
  }

}
