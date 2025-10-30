package org.demo.aimusic.module.auth.service;

import cn.hutool.captcha.CircleCaptcha;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.aimusic.common.enums.ApiResultCode;
import org.demo.aimusic.common.exception.BusinessException;
import org.demo.aimusic.common.util.JwtUtil;
import org.demo.aimusic.module.auth.dto.CaptchaResponse;
import org.demo.aimusic.module.auth.dto.LoginRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final StringRedisTemplate stringRedisTemplate;

  private static final String CAPTCHA_REDIS_KEY_PREFIX = "captcha:";
  private static final long CAPTCHA_EXPIRATION_MINUTES = 2;

  @Override
  public String login(LoginRequest loginRequest) {
    validateCaptcha(loginRequest.getUuid(), loginRequest.getCaptchaCode());

    log.info("Attempting to login for user: {}", loginRequest.getEmail());

    try {
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(
              loginRequest.getEmail(), loginRequest.getPassword());

      Authentication authentication = authenticationManager.authenticate(authenticationToken);

      SecurityContextHolder.getContext()
          .setAuthentication(authentication); // Set authentication in security context

      UserDetails userDetails = (UserDetails) authentication.getPrincipal();

      String jwtToken = jwtUtil.generateToken(userDetails);
      log.info("Login successful for user: {}. Token generated.", userDetails.getUsername());

      return jwtToken;
    } catch (AuthenticationException e) {
      log.warn("Login failed for user: {}. Reason: {}", loginRequest.getEmail(), e.getMessage());
      throw new BusinessException(ApiResultCode.UNAUTHORIZED_401, e.getMessage());
    }
  }

  @Override
  public CaptchaResponse generateCaptcha() {
    CircleCaptcha captcha = new CircleCaptcha(120, 40, 4, 8);
    String code = captcha.getCode();
    String imageBased64 = captcha.getImageBase64();
    String uuid = UUID.randomUUID().toString().replace("-", "");

    String redisKey = CAPTCHA_REDIS_KEY_PREFIX + uuid;
    stringRedisTemplate
        .opsForValue()
        .set(redisKey, code, CAPTCHA_EXPIRATION_MINUTES, TimeUnit.MINUTES);

    return new CaptchaResponse(uuid, "data:image/png;base64," + imageBased64);
  }

  private void validateCaptcha(String uuid, String code) {
    if (code == null) {
      throw new BusinessException(
          ApiResultCode.CAPTCHA_INVALID_422, "Captcha code must not be null");
    }
    String redisKey = CAPTCHA_REDIS_KEY_PREFIX + uuid;
    String storedCode = stringRedisTemplate.opsForValue().get(redisKey);

    if (storedCode == null) {
      throw new BusinessException(
          ApiResultCode.CAPTCHA_INVALID_422, "Captcha has expired or does not exist");
    }

    if (!storedCode.equalsIgnoreCase(code)) {
      throw new BusinessException(ApiResultCode.CAPTCHA_INVALID_422, "Captcha code is incorrect");
    }

    log.info("Captcha validated successfully for UUID: {}", uuid);
    stringRedisTemplate.delete(redisKey); // Remove used captcha
  }
}
