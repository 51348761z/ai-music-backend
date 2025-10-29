package org.demo.aimusic.module.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.aimusic.common.util.JwtUtil;
import org.demo.aimusic.module.auth.dto.CaptchaResponse;
import org.demo.aimusic.module.auth.dto.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  @Override
  public String login(LoginRequest loginRequest) {
    log.info("Attempting to login for user: {}", loginRequest.getEmail());

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            loginRequest.getEmail(), loginRequest.getPassword());

    Authentication authentication = authenticationManager.authenticate(authenticationToken);

    SecurityContextHolder.getContext()
        .setAuthentication(
            authenticationToken); // Set authentication in context though not necessary here

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String jwtToken = jwtUtil.generateToken(userDetails);
    log.info("Login successful for user: {}. Token generated.", userDetails.getUsername());

    return jwtToken;
  }

  @Override
  public CaptchaResponse generateCaptcha() {
    return null; // todo: implement captcha generation
  }
}
