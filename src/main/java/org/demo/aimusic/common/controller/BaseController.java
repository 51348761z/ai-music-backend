package org.demo.aimusic.common.controller;

import org.demo.aimusic.common.dto.ApiResult;
import org.demo.aimusic.common.enums.ApiResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * BaseController provides standardized methods for constructing API responses in a consistent
 * format across all controllers.
 */
public abstract class BaseController {

  /**
   * Returns a standardized accepted response (HTTP 202 Accepted) with data payload.
   *
   * @param data The data to include in the response body.
   * @param <T> The type of the data payload.
   * @return {@link ResponseEntity} containing {@link ApiResult} with data.
   */
  protected <T> ResponseEntity<ApiResult<T>> accepted(T data) {
    return ResponseEntity.accepted().body(ApiResult.accept(data));
  }

  protected <T> ResponseEntity<ApiResult<T>> accepted() {
    return ResponseEntity.accepted().body(ApiResult.accept());
  }

  /**
   * Returns a standardized success response (HTTP 200 OK) with data payload.
   *
   * @param data The data to include in the response body.
   * @param <T> The type of the data payload.
   * @return {@link ResponseEntity} containing {@link ApiResult} with data.
   */
  protected <T> ResponseEntity<ApiResult<T>> ok(T data) {
    return ResponseEntity.ok(ApiResult.success(data));
  }

  /**
   * Returns a standardized success response (HTTP 200 OK) without any data payload.
   *
   * @param <T> The type of the data payload (null in this case).
   * @return {@link ResponseEntity} containing {@link ApiResult} without data.
   */
  protected <T> ResponseEntity<ApiResult<T>> ok() {
    return ResponseEntity.ok(ApiResult.success());
  }

  /**
   * Returns a standardized failure response with appropriate HTTP status code based on the provided
   * {@link ApiResultCode}.
   *
   * @param resultCode The {@link ApiResultCode} representing the error condition.
   * @param <T> The type of the data payload (null in this case).
   * @return {@link ResponseEntity} containing {@link ApiResult} with error details.
   */
  protected <T> ResponseEntity<ApiResult<T>> fail(ApiResultCode resultCode) {
    return ResponseEntity.status(mapResultCodeToHttpStatus(resultCode))
        .body(ApiResult.error(resultCode));
  }

  /**
   * Returns a standardized failure response with appropriate HTTP status code based on the provided
   * {@link ApiResultCode} and includes additional error details.
   *
   * @param resultCode The {@link ApiResultCode} representing the error condition.
   * @param details Additional error details to include in the response.
   * @param <T> The type of the data payload (null in this case).
   * @return {@link ResponseEntity} containing {@link ApiResult} with error details.
   */
  protected <T> ResponseEntity<ApiResult<T>> fail(ApiResultCode resultCode, String details) {
    return ResponseEntity.status(mapResultCodeToHttpStatus(resultCode))
        .body(ApiResult.error(resultCode, details));
  }

  /**
   * Returns a standardized failure response with HTTP 400 Bad Request status code.
   *
   * @param code Custom error code.
   * @param message Error message.
   * @param <T> The type of the data payload (null in this case).
   * @return {@link ResponseEntity} containing {@link ApiResult} with error details.
   */
  protected <T> ResponseEntity<ApiResult<T>> fail(int code, String message) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.error(code, message));
  }

  /**
   * Returns a standardized failure response with HTTP 400 Bad Request status code and additional
   * error details.
   *
   * @param code Custom error code.
   * @param message Error message.
   * @param details Additional error details to include in the response.
   * @param <T> The type of the data payload (null in this case).
   * @return {@link ResponseEntity} containing {@link ApiResult} with error details.
   */
  protected <T> ResponseEntity<ApiResult<T>> fail(int code, String message, String details) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResult.error(code, message, details));
  }

  /**
   * Maps an {@link ApiResultCode} to the corresponding {@link HttpStatus}.
   *
   * @param resultCode The {@link ApiResultCode} to map.
   * @return The corresponding {@link HttpStatus}.
   */
  private HttpStatus mapResultCodeToHttpStatus(ApiResultCode resultCode) {
    return switch (resultCode) {
      case SUCCESS -> HttpStatus.OK;
      case UNAUTHORIZED_401 -> HttpStatus.UNAUTHORIZED;
      case FORBIDDEN_403 -> HttpStatus.FORBIDDEN;
      case NOT_FOUND_404 -> HttpStatus.NOT_FOUND;
      case METHOD_NOT_ALLOWED_405 -> HttpStatus.METHOD_NOT_ALLOWED;
      case CONFLICT_409 -> HttpStatus.CONFLICT;
      case VALIDATION_ERROR_422 -> HttpStatus.UNPROCESSABLE_ENTITY;
      case INTERNAL_SERVER_ERROR_500 -> HttpStatus.INTERNAL_SERVER_ERROR;
      case SERVICE_UNAVAILABLE_503 -> HttpStatus.SERVICE_UNAVAILABLE;
      case BAD_REQUEST_400 -> HttpStatus.BAD_REQUEST;
      default -> HttpStatus.BAD_REQUEST;
    };
  }
}
