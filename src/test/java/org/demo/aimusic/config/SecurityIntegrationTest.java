package org.demo.aimusic.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import org.demo.aimusic.common.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private JwtUtil jwtUtil;

  private static final String PROTECTED_URL = "/api/v1/some-protected-endpoint";

  private static final String PUBLIC_URL = "/api/auth/some-public-endpoint";

  @Test
  void whenAccessingPublicUrl_thenIsOk() throws Exception {
    mockMvc.perform(get(PUBLIC_URL)).andExpect(status().isOk());
  }

  @Test
  void whenAccessingProtectedUrlWithoutToken_thenIsUnauthorized() throws Exception {
    mockMvc.perform(get(PROTECTED_URL)).andExpect(status().isUnauthorized());
  }

  @Test
  void whenAccessingProtectedUrlWithValidToken_thenIsOk() throws Exception {
    // 创建一个模拟用户并为其生成一个token
    UserDetails userDetails = new User("test@example.com", "password", new ArrayList<>());
    String token = jwtUtil.generateToken(userDetails);

    mockMvc
        .perform(get(PROTECTED_URL).header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  void whenAccessingProtectedUrlWithInvalidToken_thenIsUnauthorized() throws Exception {
    String invalidToken = "this.is.an.invalid.token";

    mockMvc
        .perform(get(PROTECTED_URL).header("Authorization", "Bearer " + invalidToken))
        .andExpect(status().isUnauthorized());
  }
}
