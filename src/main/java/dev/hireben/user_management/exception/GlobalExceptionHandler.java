package dev.hireben.user_management.exception;

import java.time.Instant;
import java.util.Collection;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.micrometer.tracing.Tracer;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
final class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  final Tracer tracer;

  @Override
  protected final @NonNull ResponseEntity<Object> createResponseEntity(
      @Nullable Object body,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode statusCode,
      @NonNull WebRequest request) {

    if (body instanceof ProblemDetail problemDetail) {
      problemDetail.setProperty("timestamp", Instant.now());
      problemDetail.setProperty("trace", tracer.currentTraceContext().context().traceId());
    }

    return super.createResponseEntity(body, headers, statusCode, request);
  }

  @Override
  protected final ResponseEntity<Object> handleMethodArgumentNotValid(
      @NonNull MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {

    ProblemDetail problemDetail = ex.updateAndGetBody(getMessageSource(), LocaleContextHolder.getLocale());

    Collection<FieldValidationErrorMap> errors = ex.getBindingResult().getAllErrors().stream()
        .map(error -> FieldValidationErrorMap.builder()
            .field(((FieldError) error).getField())
            .message(error.getDefaultMessage())
            .build())
        .toList();

    problemDetail.setProperty("errors", errors);

    return createResponseEntity(problemDetail, headers, status, request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<Object> handleConstraintViolation(
      ConstraintViolationException ex,
      WebRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, "Validation failed");

    Collection<FieldValidationErrorMap> errors = ex.getConstraintViolations().stream()
        .map(error -> FieldValidationErrorMap.builder()
            .field(error.getPropertyPath().toString())
            .message(error.getMessage())
            .build())
        .toList();

    problemDetail.setProperty("errors", errors);

    return createResponseEntity(problemDetail, HttpHeaders.EMPTY, status, request);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  ResponseEntity<Object> handleApplicationException(
      EntityNotFoundException ex,
      WebRequest request) {

    HttpStatus status = HttpStatus.NOT_FOUND;

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());

    return createResponseEntity(problemDetail, HttpHeaders.EMPTY, status, request);
  }

  @ExceptionHandler(Exception.class)
  ResponseEntity<Object> catchAllException(
      Exception ex,
      WebRequest request) {

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status,
        "An unhandled error occured at the server side");

    logger.error("Unhandled exception caught", ex);

    return createResponseEntity(problemDetail, HttpHeaders.EMPTY, status, request);
  }

}
