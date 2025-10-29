package org.demo.aimusic.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    log.info("Start insert fill for {}", metaObject.getOriginalObject() + "...");
    this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
    this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    log.info("Start update fill for {}", metaObject.getOriginalObject() + "...");
    this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
  }
}
