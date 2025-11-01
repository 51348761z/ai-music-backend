package org.demo.aimusic.module.generation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.demo.aimusic.common.controller.BaseController;
import org.demo.aimusic.common.dto.ApiResult;
import org.demo.aimusic.module.generation.dto.GenerationRequest;
import org.demo.aimusic.module.generation.dto.TaskStreamPayload;
import org.demo.aimusic.module.generation.entity.GenerationTask;
import org.demo.aimusic.module.generation.service.GenerationTaskService;
import org.demo.aimusic.module.generation.service.TaskProducerService;
import org.demo.aimusic.module.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/music")
@RequiredArgsConstructor
public class MusicGenerationController extends BaseController {

  private final GenerationTaskService taskService;
  private final TaskProducerService producerService;
  private final UserService userService;

  @RequestMapping(value = "/generate", method = RequestMethod.POST)
  public ResponseEntity<ApiResult<String>> generateMusic(
      @Valid @RequestBody GenerationRequest request) {
    GenerationTask task = new GenerationTask();
    BeanUtils.copyProperties(request, task);
    task.setUserId(userService.getIdByUuid(request.getUserId()));
    taskService.save(task);

    TaskStreamPayload payload = new TaskStreamPayload(task.getId(), task.getPrompt());

    producerService.publishTask(payload);

    return accepted("Music generation task accepted with ID: " + task.getId());
  }
}
