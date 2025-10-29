package org.demo.aimusic.module.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.demo.aimusic.common.controller.BaseController;
import org.demo.aimusic.common.dto.ApiResult;
import org.demo.aimusic.module.auth.dto.LoginRequest;
import org.demo.aimusic.module.auth.dto.LoginResponse;
import org.demo.aimusic.module.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

@RestController("authController")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
  private final AuthService authService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<ApiResult<LoginResponse>> login(
      @Valid @RequestBody LoginRequest loginRequest) {
    String token = authService.login(loginRequest);
    LoginResponse loginResponse = new LoginResponse(token);
    return ok(loginResponse);
  }

  @RequestMapping(value = "/captcha", method = RequestMethod.GET)
  public ResponseEntity<ApiResult<String>> captcha(HttpServletRequest request) {
    return fail(500, "Not implemented.");
  }
}
