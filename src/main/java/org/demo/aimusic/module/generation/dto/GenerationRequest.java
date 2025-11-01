package org.demo.aimusic.module.generation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerationRequest {

  @NotNull(message = "User ID cannot be null")
  private String userId;

  @NotNull(message = "AI Model ID cannot be null")
  private Long aiModelId;

  @NotNull(message = "Prompt cannot be null")
  private String prompt;
}
