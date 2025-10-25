package org.demo.aimusic.common.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.demo.aimusic.common.dto.ApiResult;
import org.demo.aimusic.common.enums.ApiResultCode;
import org.demo.aimusic.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResult<Void>> handleBusinessException(
      BusinessException ex, WebRequest request) {
    log.warn(
        "Business Exception: {} (Details: {}) - Request: {}",
        ex.getMessage(),
        ex.getDetails(),
        request.getDescription(false));
    ApiResult<Void> errorResult =
        ApiResult.error(ex.getApiResultCode().getCode(), ex.getMessage(), ex.getDetails());

    HttpStatus status =
        switch (ex.getApiResultCode()) {
          case NOT_FOUND_404 -> HttpStatus.NOT_FOUND;
          case CONFLICT_409 -> HttpStatus.CONFLICT;
          case FORBIDDEN_403 -> HttpStatus.FORBIDDEN;
          case UNAUTHORIZED_401 -> HttpStatus.UNAUTHORIZED;
          default -> HttpStatus.BAD_REQUEST;
        };
    return new ResponseEntity<>(errorResult, status);
  }

  /**
   * Handles validation errors for {@code @RequestBody} (JSON) using {@code @Valid}.
   *
   * @param ex The {@link MethodArgumentNotValidException} that was thrown.
   * @param request The current {@link WebRequest}.
   * @return An {@link ApiResult} indicating a validation error.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ApiResult<Void> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException ex, WebRequest request) {
    String errorDetails =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    String.format(
                        "'%s': %s", fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.joining("; "));
    log.warn(
        "Validation Exception (RequestBody): {} - request: {}",
        errorDetails,
        request.getDescription(false));
    return ApiResult.error(ApiResultCode.VALIDATION_ERROR_422, errorDetails);
  }

  /**
   * Handles validation errors for query parameters or form data using {@code @Valid}.
   *
   * @param ex The {@link BindException} that was thrown.
   * @param request The current {@link WebRequest}.
   * @return An {@link ApiResult} indicating a validation error.
   */
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public ApiResult<Void> handleBindException(BindException ex, WebRequest request) {
    String errorDetails =
        ex.getBindingResult().getFieldErrors().stream()
            .map(
                fieldError ->
                    String.format(
                        "'%s': %s", fieldError.getField(), fieldError.getDefaultMessage()))
            .collect(Collectors.joining("; "));
    log.warn(
        "Validation Exception (Params/Form): {} - Request: {}",
        errorDetails,
        request.getDescription(false));
    return ApiResult.error(ApiResultCode.VALIDATION_ERROR_422, errorDetails);
  }

  /**
   * Handles authentication exceptions.
   *
   * @param ex The {@link AuthenticationException} that was thrown.
   * @param request The current {@link WebRequest}.
   * @return An {@link ApiResult} indicating an authentication error.
   */
  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiResult<Void> handleAuthenticationException(
      AuthenticationException ex, WebRequest request) {
    log.warn(
        "Authentication Failed: {} - Request: {}", ex.getMessage(), request.getDescription(false));
    String message =
        (ex instanceof BadCredentialsException) ? "Invalid credentials" : "Authentication failed";
    return ApiResult.error(ApiResultCode.UNAUTHORIZED_401.getCode(), message);
  }

  @ExceptionHandler(JwtException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiResult<Void> handleJwtException(JwtException ex, WebRequest request) {
    log.warn("JWT Exception: {} - Request: {}", ex.getMessage(), request.getDescription(false));
    String message = (ex instanceof ExpiredJwtException) ? "Token has expired" : "Invalid token";
    return ApiResult.error(ApiResultCode.UNAUTHORIZED_401.getCode(), message);
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiResult<Void> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
    log.warn("Access Denied: {} - Request: {}", ex.getMessage(), request.getDescription(false));
    return ApiResult.error(ApiResultCode.FORBIDDEN_403);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ApiResult<Void> handlesHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException ex, WebRequest request) {
    log.warn(
        "Method Not Allowed: {} - Request: {}", ex.getMessage(), request.getDescription(false));
    return ApiResult.error(ApiResultCode.METHOD_NOT_ALLOWED_405);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiResult<Void> handlesGenericException(Exception ex, WebRequest request) {
    log.error(
        "Unhandled Exception: {} - Request: {}", ex.getMessage(), request.getDescription(false));
    return ApiResult.error(ApiResultCode.INTERNAL_SERVER_ERROR_500);
  }
}
