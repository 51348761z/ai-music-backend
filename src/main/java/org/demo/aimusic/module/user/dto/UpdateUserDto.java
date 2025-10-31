package org.demo.aimusic.module.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDto {

  @NotBlank(message = "Nickname must not be blank")
  @Size(max = 50, message = "Nickname must not exceed 50 characters")
  private String nickname;

  @NotBlank(message = "Role must not be blank")
  @Pattern(regexp = "^(admin|user)$", message = "Role must be either 'admin' or 'user'")
  private String role;

  @NotBlank(message = "Status must not be blank")
  @Pattern(regexp = "^(enabled|disabled)$", message = "Status must be either 'enabled' or 'disabled'")
  private String status;

  @NotNull(message = "Version must not be null")
  private Integer version;
}
