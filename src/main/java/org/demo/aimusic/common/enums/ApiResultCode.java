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
  CONFLICT_409(409, "Conflict"), // E.g., username already exists
  VALIDATION_ERROR_422(422, "Validation Error"), // Unprocessable Entity

  // Server Errors (5xx)
  INTERNAL_SERVER_ERROR_500(500, "Internal Server Error"),
  SERVICE_UNAVAILABLE_503(503, "Service Unavailable");

  private final int code;
  private final String message;

  ApiResultCode(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
