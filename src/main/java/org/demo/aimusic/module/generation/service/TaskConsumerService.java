package org.demo.aimusic.module.generation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.aimusic.module.generation.dto.TaskStreamPayload;
import org.demo.aimusic.module.generation.entity.GenerationTask;
import org.demo.aimusic.module.generation.enums.TaskStatusEnum;
import org.demo.aimusic.module.generation.mapper.GenerationTaskMapper;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskConsumerService {

  private final ObjectMapper objectMapper;
  private final GenerationTaskMapper taskMapper; // for database operations

  // todo: inject other services like AI generation service, file storage service, etc.

  public void handleTask(ObjectRecord<String, String> record) {
    try {
      String jsonPayload = record.getValue();
      TaskStreamPayload payload = objectMapper.readValue(jsonPayload, TaskStreamPayload.class);
      log.info("Received task from stream. Payload: {}", jsonPayload);

      // 1. Update task status to IN_PROGRESS
      GenerationTask task = taskMapper.selectById(payload.getTaskId());
      if (task == null || TaskStatusEnum.PENDING != task.getStatus()) {
        log.warn("Task not found or not in PENDING status. Task ID: {}", payload.getTaskId());
        return;
      }
      task.setStatus(TaskStatusEnum.IN_PROGRESS);
      taskMapper.updateById(task);

      // 2. todo: Call AI generation service to process the task

      // mock processing
      Thread.sleep(5000);
      String resultUrl = "http://example.com/generated_music/" + payload.getTaskId() + ".mp3";

      // 3. Update task status to COMPLETED and set result URL
      task.setStatus(TaskStatusEnum.COMPLETED);
      task.setResultUrl(resultUrl);
      task.setDurationSeconds(180); // mock duration
      taskMapper.updateById(task);

      log.info("Task {} completed successfully. Result URL: {}", payload.getTaskId(), resultUrl);
    } catch (Exception e) {
      log.error("Failed to process task from stream record: {}", record.getId(), e);
      // todo: update task status to FAILED
    }
  }
}
