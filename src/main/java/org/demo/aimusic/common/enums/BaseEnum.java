package org.demo.aimusic.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;

public interface BaseEnum<T extends Serializable> extends IEnum<T> {
  String getDesc();

  @Override
  @JsonValue
  T getValue();
}
