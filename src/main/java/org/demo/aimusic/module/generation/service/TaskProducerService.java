package org.demo.aimusic.module.generation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.aimusic.module.generation.dto.TaskStreamPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskProducerService {
  private final RedisTemplate<String, String> redisTemplate;
  private final ObjectMapper objectMapper;

  @Value("${app.stream.key}")
  private String streamKey;

  /**
   * Publishes a task payload to the Redis stream.
   *
   * @param payload The task payload to be published.
   */
  public void publishTask(TaskStreamPayload payload) {
    try {
      String jsonPayload = objectMapper.writeValueAsString(payload);
      ObjectRecord<String, String> record =
          StreamRecords.newRecord()
              .in(streamKey)
              .ofObject(jsonPayload)
              .withId(RecordId.autoGenerate());

      redisTemplate.opsForStream().add(record);
      log.info("Published task to stream. Payload: {}", jsonPayload);
    } catch (JsonProcessingException e) {
      log.error("Failed to publish task to stream. Payload: {}", payload, e);
      throw new RuntimeException("Failed to publish task to stream", e);
    }
  }
}
