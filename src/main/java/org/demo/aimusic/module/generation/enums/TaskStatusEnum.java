package org.demo.aimusic.module.generation.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.demo.aimusic.common.enums.BaseEnum;

@Getter
@RequiredArgsConstructor
public enum TaskStatusEnum implements BaseEnum<String> {
  PENDING(0, "PENDING", "Pending"),
  IN_PROGRESS(1, "IN_PROGRESS", "In Progress"),
  COMPLETED(2, "COMPLETED", "Completed"),
  FAILED(3, "FAILED", "Failed");

  private final Integer code;
  @EnumValue
  private final String value;
  @JsonValue
  private final String desc;

}
