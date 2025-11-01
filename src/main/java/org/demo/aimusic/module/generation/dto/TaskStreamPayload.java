package org.demo.aimusic.module.generation.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStreamPayload implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @NotNull(message = "taskId cannot be null")
  private Long taskId;
  @NotNull(message = "prompt cannot be null")
  private String prompt;
}
