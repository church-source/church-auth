package org.churchsource.churchauth.user.helpers;

import org.churchsource.churchauth.helpers.AbstractTypeSafeMatcher;
import org.churchsource.churchauth.user.UserFullViewModel;
import org.hamcrest.Description;

import java.util.Objects;

public class UserFullViewModelMatcher extends AbstractTypeSafeMatcher<UserFullViewModel> {

  public UserFullViewModelMatcher(UserFullViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, UserFullViewModel user) {
    description.appendText("A UserFullViewModel with the following state:")
        .appendText("\nId: ").appendValue(user.getId())
        .appendText("\nUser Name: ").appendValue(user.getUsername())
        .appendText("\nEmail: ").appendValue(user.getEmail())
        .appendText("\nPassword: ").appendValue(user.getPassword())
        .appendText("\nisEnabled: ").appendValue(user.getIsEnabled())
        .appendText("\nisLocked: ").appendValue(user.getIsLocked())
        .appendText("\nisExpired: ").appendValue(user.getIsExpired())
        .appendText("\nRoles: ").appendValue(user.getRoles());
  }

  @Override
  protected boolean matchesSafely(UserFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getUsername(), expected.getUsername())
        && Objects.equals(actual.getEmail(), expected.getEmail())
        && Objects.equals(actual.getPassword(), expected.getPassword())
        && Objects.equals(actual.getIsEnabled(), expected.getIsEnabled())
        && Objects.equals(actual.getIsExpired(), expected.getIsExpired())
        && Objects.equals(actual.getIsLocked(), expected.getIsLocked())
        && isListsEqual(actual.getRoles(), expected.getRoles());
  }

  public static UserFullViewModelMatcher hasSameStateAsUserFullViewModel(UserFullViewModel expected) {
    return new UserFullViewModelMatcher(expected);
  }

}
