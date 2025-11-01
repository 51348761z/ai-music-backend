package org.demo.aimusic.module.generation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerationService {
  private final GenerationTaskService generationTaskService;
  private final RedisTemplate<String, Object> redisTemplate;

  private static final String STREAM_KEY = "generation:tasks";
  private static final Integer DEFAULT_AI_MOREL_ID = 1;
  private static final Integer DEFAULT_POINTS_CONSUMED = 1;

}
