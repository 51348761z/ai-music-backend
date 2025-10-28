package org.demo.aimusic.module.user.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UserQueryDto {
  @Min(value = 1, message = "Current page must be greater than or equal to 1")
  private int currentPage = 1;
  @Min(value = 1, message = "Page size must be greater than or equal to 1")
  private int pageSize = 10;

  // --- Filter criteria ---
  private String nickname;
  private String email;
  private String status;
}
