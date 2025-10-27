package org.demo.aimusic.module.auth.controller;

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
import org.springframework.web.bind.annotation.RestController;

@RestController("authController")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
  private final AuthService authService;

  public ResponseEntity<ApiResult<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest){
    String token = authService.login(loginRequest);
    LoginResponse loginResponse = new LoginResponse(token);
    return ok(loginResponse);
}
}
