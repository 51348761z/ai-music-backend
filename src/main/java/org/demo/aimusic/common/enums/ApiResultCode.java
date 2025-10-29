package org.demo.aimusic.common.enums;

import lombok.Getter;

@Getter
public enum ApiResultCode {
  SUCCESS(200, "Success"),

  // Client Errors (4xx)
  BAD_REQUEST_400(400, "Bad Request"),
  UNAUTHORIZED_401(401, "Unauthorized"),
  FORBIDDEN_403(403, "Forbidden"),
  NOT_FOUND_404(404, "Not Found"),
  METHOD_NOT_ALLOWED_405(405, "Method Not Allowed"),
  REQUEST_TIMEOUT_408(408, "Request Timeout"),
  CONFLICT_409(409, "Conflict"), // E.g., username already exists
  VALIDATION_ERROR_422(422, "Validation Error"), // Unprocessable Entity
  CAPTCHA_INVALID_422(422, "Captcha Invalid"), // For captcha validation failure

  // Server Errors (5xx)
  INTERNAL_SERVER_ERROR_500(500, "Internal Server Error"),
  NOT_IMPLEMENTED_501(501, "Not Implemented"),
  BAD_GATEWAY_502(502, "Bad Gateway"),
  SERVICE_UNAVAILABLE_503(503, "Service Unavailable"),
  GATEWAY_TIMEOUT_504(504, "Gateway Timeout"),
  HTTP_VERSION_NOT_SUPPORTED_505(505, "HTTP Version Not Supported");

  private final int code;
  private final String message;

  ApiResultCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
