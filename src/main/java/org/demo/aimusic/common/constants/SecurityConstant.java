package org.demo.aimusic.common.constants;

public class SecurityConstant {
  public static final String[] PUBLIC_URLS = {
      "/api/auth/**",
      "/api/public/**",
      "/v3/api-docs/**",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/error",
      "/auth/**",
      "/users/register"
  };
  public static final String[] LOGIN_WHITELIST = {
      "/dev-api/auth/login",
      "/dev-api/auth/captcha",
      "/dev-api/users/register",
      "/error"
  };
}
