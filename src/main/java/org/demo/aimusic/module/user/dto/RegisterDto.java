package org.demo.aimusic.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterDto {

  @NotBlank(message = "Email must not be blank")
  private String email;

  @NotBlank(message = "Nickname must not be blank")
  private String nickname;

  @NotBlank(message = "Password must not be blank")
  private String password;
}
