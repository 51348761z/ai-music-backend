package org.demo.aimusic.module.user.controller;

import lombok.RequiredArgsConstructor;
import org.demo.aimusic.common.controller.BaseController;
import org.demo.aimusic.common.dto.ApiResult;
import org.demo.aimusic.module.user.dto.UserInfoDto;
import org.demo.aimusic.module.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("UserController")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends BaseController {
  private final UserService userService;

  @RequestMapping(value = "/me", method = RequestMethod.GET)
  public ResponseEntity<ApiResult<UserInfoDto>> getCurrentUserInfo() {
    UserInfoDto userInfoDto = userService.getCurrentUserInfo();
    return ok(userInfoDto);
  }
}
