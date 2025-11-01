package org.demo.aimusic.config;

import java.time.Duration;
import org.demo.aimusic.module.generation.service.TaskConsumerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

@Configuration
public class RedisStreamConfig {

  @Value("${app.stream.key}")
  private String streamKey;

  @Value(("${app.stream.consumer-group}"))
  private String consumerGroup;

  public Subscription subscription(
      RedisConnectionFactory redisConnectionFactory,
      TaskConsumerService taskConsumerService,
      RedisTemplate<String, String> redisTemplate) {
    try {
      redisTemplate.opsForStream().createGroup(streamKey, consumerGroup);
    } catch (Exception e) {
      // If the consumer group already exists, we can ignore the exception
    }

    StreamMessageListenerContainer.StreamMessageListenerContainerOptions<
            String, ObjectRecord<String, String>>
        options =
            StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                .pollTimeout(Duration.ofSeconds(1))
                .targetType(String.class)
                .build();

    StreamMessageListenerContainer<String, ObjectRecord<String, String>> listenerContainer =
        StreamMessageListenerContainer.create(redisConnectionFactory, options);

    Subscription subscription =
        listenerContainer.receive(
            Consumer.from(consumerGroup, "consumer-1"),
            StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
            taskConsumerService::handleTask);

    listenerContainer.start();
    return subscription;
  }
}
