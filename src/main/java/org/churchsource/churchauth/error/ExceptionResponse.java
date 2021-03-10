package org.churchsource.churchauth.error;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse implements Serializable {

  private static final long serialVersionUID = 5795604402500068825L;

  private int status;
  private String error;
  private String message;

  public static ExceptionResponse anExceptionResponse(HttpStatus status, String message) {
    return new ExceptionResponse(status.value(), status.getReasonPhrase(), message);
  }

  public static ExceptionResponse anExceptionResponse(HttpStatus status, String error, String message) {
    return new ExceptionResponse(status.value(), error, message);
  }

  @Override
  public boolean equals(Object objectToCompareTo) {
    if (this == objectToCompareTo)
      return true;
    if (objectToCompareTo == null || getClass() != objectToCompareTo.getClass())
      return false;
    ExceptionResponse exceptionResponseToCompareTo = (ExceptionResponse) objectToCompareTo;
    return getStatus() == exceptionResponseToCompareTo.getStatus() && Objects.equals(getError(), exceptionResponseToCompareTo.getError())
        && Objects.equals(getMessage(), exceptionResponseToCompareTo.getMessage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getStatus(), getError(), getMessage());
  }
}
