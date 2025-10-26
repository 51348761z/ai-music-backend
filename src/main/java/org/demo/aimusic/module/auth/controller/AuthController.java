package org.demo.aimusic.module.auth.controller;

import lombok.RequiredArgsConstructor;
import org.demo.aimusic.common.controller.BaseController;
import org.demo.aimusic.module.auth.service.AuthService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("authController")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
  private final AuthService authService;
  // TODO: Implement authentication endpoints
}
