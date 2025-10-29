package org.demo.aimusic.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "app.initial-admin")
@Data
@Validated
public class InitialAdminProperties {
  @NotBlank(message = "Initial admin email must not be blank")
  private String email;

  @NotBlank(message = "Initial admin password must not be blank")
  @Size(min = 8, message = "Initial admin password must be at least 8 characters long")
  private String password;

  @NotBlank(message = "Initial admin nickname must not be blank")
  private String nickname;
}
