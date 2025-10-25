package org.demo.aimusic.common.exception;

import lombok.Getter;
import org.demo.aimusic.common.enums.ApiResultCode;

@Getter
public class BusinessException extends RuntimeException {

  private final ApiResultCode apiResultCode;
  private final String details;

  public BusinessException(ApiResultCode apiResultCode) {
    super(apiResultCode.getMessage());
    this.apiResultCode = apiResultCode;
    this.details = null;
  }

  public BusinessException(ApiResultCode apiResultCode, String details) {
    super(apiResultCode.getMessage() + (details != null? ": " + details: ""));
    this.apiResultCode = apiResultCode;
    this.details = details;
  }

  public BusinessException(String message) {
    super(message);
    this.apiResultCode = ApiResultCode.BAD_REQUEST;
    this.details = message;
  }

  public BusinessException(ApiResultCode apiResultCode, Throwable cause) {
    super(apiResultCode.getMessage(), cause);
    this.apiResultCode = apiResultCode;
    this.details = null;
  }
}
