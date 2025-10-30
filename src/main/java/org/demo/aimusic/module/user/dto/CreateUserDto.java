package org.demo.aimusic.module.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserDto {

  @NotBlank(message = "Email must not be blank")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password must not be blank")
  @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
  private String password;

  @NotBlank
  @Size(max = 50, message = "Nickname must not exceed 50 characters")
  private String nickname;

  @NotBlank(message = "Role must not be blank")
  @Pattern(regexp = "^(admin|user)$", message = "Role must be either 'admin' or 'user'")
  private String role;

  @NotBlank(message = "Status must not be blank")
  @Pattern(
      regexp = "^(enabled|disabled)$",
      message = "Status must be either 'enabled' or 'disabled'")
  private String status;
}
