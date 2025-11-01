package org.demo.aimusic.module.generation.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.demo.aimusic.module.generation.enums.TaskStatusEnum;

@Getter
@Setter
@TableName("tb_generation_tasks")
public class GenerationTask {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @NotNull
  @TableField("user_id")
  private Long userId;

  @NotNull
  @TableField("ai_model_id")
  private Long aiModelId;

  @NotNull
  @TableField("prompt")
  private String prompt;

  @Size(max = 20)
  @NotNull
  @TableField("status")
  private TaskStatusEnum status = TaskStatusEnum.PENDING;

  @TableField("error_message")
  private String errorMessage;

  @Size(max = 512)
  @TableField("result_url")
  private String resultUrl;

  @TableField("duration_seconds")
  private Integer durationSeconds;

  @NotNull
  @TableField("points_consumed")
  private Integer pointsConsumed = 1;

  @NotNull
  @TableField(value = "created_at", fill = FieldFill.INSERT)
  private LocalDateTime createdAt;

  @NotNull
  @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updatedAt;
}
