package org.demo.aimusic.module.generation.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GenerationTaskMessage represents a message containing details about a generation task,
 * including its unique identifier and the associated prompt.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerationTaskMessage implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Long taskId;
  private String prompt;
}
