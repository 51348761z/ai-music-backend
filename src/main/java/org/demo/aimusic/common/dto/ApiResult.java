package org.demo.aimusic.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.demo.aimusic.common.enums.ApiResultCode;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class ApiResult<T> {
  private int code;
  private String message;
  private Long timestamp;
  private T data; // payload for success responses
  private String errorDetails; // optional field for error responses

  // -- Success Constructors -    -
  private ApiResult(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.timestamp = Instant.now().toEpochMilli();
    this.data = data;
  }

  private ApiResult(T data) {
    this(ApiResultCode.SUCCESS.getCode(), ApiResultCode.SUCCESS.getMessage(), data);
  }
  // -- Error Constructors --
  // TODO: Add error constructor(s) and static factory(s)
}
