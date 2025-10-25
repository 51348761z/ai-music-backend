package org.demo.aimusic.common.dto;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.demo.aimusic.common.enums.ApiResultCode;

@Getter
@NoArgsConstructor
public class ApiResult<T> {
  private int code;
  private String message;
  private Long timestamp;
  private T data; // payload for success responses
  private String errorDetails; // optional field for error responses

  // --- Success Constructors ---
  private ApiResult(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.timestamp = Instant.now().toEpochMilli();
    this.data = data;
  }

  private ApiResult(T data) {
    this(ApiResultCode.SUCCESS.getCode(), ApiResultCode.SUCCESS.getMessage(), data);
  }

  // --- Error Constructors ---
  private ApiResult(int code, String message) {
    this.code = code;
    this.message = message;
    this.timestamp = Instant.now().toEpochMilli();
  }
  private ApiResult(ApiResultCode apiResultCode) {
    this(apiResultCode.getCode(), apiResultCode.getMessage());
  }
  private ApiResult(ApiResultCode apiResultCode, String errorDetails) {
    this(apiResultCode.getCode(), apiResultCode.getMessage());
    this.errorDetails = errorDetails;
  }
  private ApiResult(int code, String message, String errorDetails) {
    this(code, message);
    this.errorDetails = errorDetails;
  }

  // --- Static Factory Methods ---

  // success
  public static <T> ApiResult<T> success(T data) {
    return new ApiResult<>(data);
  }
  public static <T> ApiResult<T> success() {
    return new ApiResult<>(ApiResultCode.SUCCESS.getCode(), ApiResultCode.SUCCESS.getMessage(), null);
  }

  // error
  public static <T> ApiResult<T> error(int code, String message) {
    return new ApiResult<>(code, message);
  }
  public static <T> ApiResult<T> error(ApiResultCode apiResultCode) {
    return new ApiResult<>(apiResultCode);
  }
  public static <T> ApiResult<T> error(ApiResultCode apiResultCode, String errorDetails) {
    return new ApiResult<>(apiResultCode, errorDetails);
  }
  public static <T> ApiResult<T> error(int code, String message, String errorDetails) {
    return new ApiResult<>(code, message, errorDetails);
  }
}
