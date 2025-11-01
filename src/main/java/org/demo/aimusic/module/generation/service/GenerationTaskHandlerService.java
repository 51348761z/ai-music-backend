package org.demo.aimusic.module.generation.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.aimusic.module.generation.dto.GenerationTaskMessage;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerationTaskHandlerService {
  private final RedisTemplate<String, Object> redisTemplate;

  private static final String STREAM_KEY = "generation:GenerationTaskHandlerService";
  private static final String GROUP_NAME = "generation-group";
  private static final String CONSUMER_NAME = "worker-" + System.currentTimeMillis();

  // Single-threaded executor for task processing
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private volatile boolean running = true;

  /**
   * Initializes and starts the task consumer for processing generation tasks from the Redis Stream.
   */
  @PostConstruct
  public void startTaskConsumer() {
    log.info("[[GenerationTaskHandler]] Starting the ai-music generation task consumer thead...");

    try {
      // Create the consumer group if it doesn't exist
      redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.from("0"), GROUP_NAME);
      log.info(
          "Redis Stream Group '{}' for Stream '{}' created or already exists.",
          GROUP_NAME,
          STREAM_KEY);
    } catch (Exception e) {
      if (!e.getMessage().contains("BUSYGROUP")) {
        log.error("Error creating Redis Stream Group: {}", e.getMessage());
        return;
      }
    }
    // Start the consumer in a separate thread
    executor.submit(this::taskProcessingLoop);
  }

  /**
   * The main loop for processing tasks from the Redis Stream.
   * Continuously reads messages, processes them, and acknowledges them.
   */
  private void taskProcessingLoop() {
    // Start reading from the last message in the stream.
    StreamReadOptions readOptions =
        StreamReadOptions.empty()
            .block(Duration.ofMillis(5000)) // Block for 1 second if no messages are available
            .count(1); // Read one message at a time

    Consumer consumer = Consumer.from(GROUP_NAME, CONSUMER_NAME);

    StreamOffset<String> streamOffset = StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed());

    while (running) {
      try{
        List<ObjectRecord<String, GenerationTaskMessage>> records = redisTemplate.opsForStream().read(
            GenerationTaskMessage.class,
            consumer,
            readOptions,
            streamOffset
        );

        if (records != null && !records.isEmpty()) {
          for (ObjectRecord<String, GenerationTaskMessage> record: records) {
            RecordId recordId = record.getId();
            GenerationTaskMessage message = record.getValue();
            log.info("[[Worker]] Received task with ID: {}, prompt: {}", message.getTaskId(), message.getPrompt());

            // TODO: Add actual task processing logic here

            Long acknowledgedCount = redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP_NAME, recordId);
            log.info("[[Worker]] Acknowledged task with ID: {}, acknowledged status: {}", message.getTaskId(), (acknowledgedCount > 0));
          }
        }
      } catch (Exception e) {
        log.error("Redis Stream read/acknowledge error: {}", e.getMessage(), e);
        try{
          Thread.sleep(1000);
        } catch (InterruptedException interruptedException) {
          Thread.currentThread().interrupt();;
        }
      }
    }
    log.info("[[GenerationTaskHandler]] Task processing loop has been stopped.");
  }

  @PreDestroy
  public void stopTaskConsumer() {
    this.running = false;
    executor.shutdownNow();
    log.info("[[GenerationTaskHandler]] Stopped the ai-music generation task consumer thread.");
  }
}
