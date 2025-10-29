package org.demo.aimusic.module.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
  @NotBlank(message = "Email must not be blank")
  @JsonProperty("username")
  private String email;

  @NotBlank(message = "Password must not be blank")
  private String password;

  @NotBlank(message = "Captcha code must not be blank")
  @JsonProperty("code")
  private String captchaCode;

  @NotBlank(message = "Captcha UUID must not be blank")
  private String uuid;
}
