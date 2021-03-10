package org.churchsource.churchauth.error;

import static org.churchsource.churchauth.error.ExceptionResponse.anExceptionResponse;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import javax.persistence.NoResultException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleUnhandledException(Exception exception) {
    log.error(exception.getMessage(), exception);
    return ResponseEntity
        .status(INTERNAL_SERVER_ERROR)
        .contentType(MediaType.APPLICATION_JSON)
        .body(anExceptionResponse(INTERNAL_SERVER_ERROR, exception.getMessage()));
  }

  /**
   * Thrown by the persistence provider when getSingleResult() is executed on a
   * query and there is no result to return.
   */
  @ExceptionHandler({NoResultException.class, EmptyResultDataAccessException.class})
  public ResponseEntity<ExceptionResponse> handleNoResultException(NoResultException error) {

    log.error(error.getMessage());

    return ResponseEntity
        .status(NOT_FOUND)
        .contentType(MediaType.APPLICATION_JSON)
        .body(anExceptionResponse(NOT_FOUND, error.getMessage()));

  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    if (status.is4xxClientError()) {
      log.warn(ex.getMessage(), ex);
    } else if (status.is5xxServerError()) {
      log.error(ex.getMessage(), ex);
    }

    return super.handleExceptionInternal(ex, body, headers, status, request);
  }
}