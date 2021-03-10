package org.churchsource.churchauth.user.role.helpers;

import org.churchsource.churchauth.helpers.AbstractTypeSafeMatcher;
import org.churchsource.churchauth.user.role.Privilege;
import org.hamcrest.Description;

import java.util.Objects;

public class PrivilegeMatcher extends AbstractTypeSafeMatcher<Privilege> {

  public PrivilegeMatcher(Privilege expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, Privilege Privilege) {
    description.appendText("A Privilege with the following state:")
        .appendText("\nId: ").appendValue(Privilege.getId())
        .appendText("\nName: ").appendValue(Privilege.getName())
        .appendText("\nDeleted: ").appendValue(Privilege.getDeleted());
  }

  @Override
  protected boolean matchesSafely(Privilege actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getDeleted(), expected.getDeleted());
  }

  public static PrivilegeMatcher hasSameStateAsPrivilege(Privilege expected) {
    return new PrivilegeMatcher(expected);
  }

}
