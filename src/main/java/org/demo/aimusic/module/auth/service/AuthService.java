package org.demo.aimusic.module.auth.service;

import org.demo.aimusic.module.auth.dto.CaptchaResponse;
import org.demo.aimusic.module.auth.dto.LoginRequest;

public interface AuthService {

  /**
   * Authenticates a user and generates a JWT token upon successful login.
   *
   * @param loginRequest {@link LoginRequest} DTO containing email and password
   * @return JWT token as a {@link String}
   * @throws org.springframework.security.core.AuthenticationException if authentication fails
   */
  String login(LoginRequest loginRequest);

  /**
   * Generates a new captcha consisting of a UUID and a base64-encoded image.
   *
   * @return {@link CaptchaResponse} containing the captcha details
   */
  CaptchaResponse generateCaptcha();
}
