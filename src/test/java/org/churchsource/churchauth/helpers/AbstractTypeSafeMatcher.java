package org.churchsource.churchauth.helpers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractTypeSafeMatcher<T> extends TypeSafeMatcher<T> {

  public T expected;

  public AbstractTypeSafeMatcher(T expected) {
    this.expected = expected;
  }

  public abstract void appendDescription(Description description, T t);

  @Override
  public void describeTo(Description description) {
    appendDescription("a " + expected.getClass().getSimpleName() + " with the following state:", description, expected);
  }

  @Override
  protected void describeMismatchSafely(T item, Description mismatchDescription) {
    appendDescription("was a " + item.getClass().getSimpleName() + " with the following state:", mismatchDescription, item);
  }

  private void appendDescription(String initialMessage, Description description, T t) {
    description.appendText(initialMessage);
    appendDescription(description, t);
  }

  protected Boolean isListsEqual(List list1, List list2) {
    return Objects.equals((list1 != null ?  new ArrayList(list1) : new ArrayList()),
            (list2 != null ?  new ArrayList(list2) : new ArrayList()));
  }
}
