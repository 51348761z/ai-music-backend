package org.demo.aimusic.module.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.demo.aimusic.common.controller.BaseController;
import org.demo.aimusic.common.dto.ApiResult;
import org.demo.aimusic.common.dto.PageDto;
import org.demo.aimusic.module.auth.dto.LoginUserDetails;
import org.demo.aimusic.module.user.dto.CreateUserDto;
import org.demo.aimusic.module.user.dto.UserInfoDto;
import org.demo.aimusic.module.user.dto.UserQueryDto;
import org.demo.aimusic.module.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("UserController")
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends BaseController {
  private final UserService userService;

  @RequestMapping(value = "/me", method = RequestMethod.GET)
  public ResponseEntity<ApiResult<UserInfoDto>> getCurrentUserInfo(
      @AuthenticationPrincipal LoginUserDetails currentUser) {
    UserInfoDto userInfoDto = userService.getCurrentUserInfo(currentUser);
    return ok(userInfoDto);
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResult<PageDto<UserInfoDto>>> listUsers(
      @Valid UserQueryDto userQueryDto) {
    PageDto<UserInfoDto> userPage = userService.listUsers(userQueryDto);
    return ok(userPage);
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResult<UserInfoDto>> createUser(
      @Valid @RequestBody CreateUserDto createUserDto) {
    UserInfoDto userInfoDto = userService.createUser(createUserDto);
    return ok(userInfoDto);
  }
}
