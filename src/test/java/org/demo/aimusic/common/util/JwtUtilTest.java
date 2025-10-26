package org.demo.aimusic.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("JwtUtil 测试")
@ExtendWith(MockitoExtension.class) // 启用 Mockito 扩展
class JwtUtilTest {

  private JwtUtil jwtUtil;

  @Mock private UserDetails userDetails;

  // 一个符合 HS256 要求的最小长度（32字节）的密钥
  private final String testSecret = "a-very-secure-secret-key-for-testing-purpose";
  private final long testExpirationMs = 3600 * 1000; // 1 hour

  @BeforeEach
  void setUp() {
    jwtUtil = new JwtUtil();
    // 使用 ReflectionTestUtils 来注入 @Value 字段的值
    ReflectionTestUtils.setField(jwtUtil, "secretString", testSecret);
    ReflectionTestUtils.setField(jwtUtil, "expirationMs", testExpirationMs);

    // 手动调用 @PostConstruct 方法，因为在单元测试中它不会自动执行
    jwtUtil.init();

    // 配置 Mock 对象
    //    when(userDetails.getUsername()).thenReturn("testuser");
  }

  @Test
  @DisplayName("init: 当密钥长度足够时，应成功初始化")
  void init_withValidSecret_shouldInitializeSuccessfully() {
    // 这个测试的成功条件是 @BeforeEach 中的 init() 不抛出异常
    assertNotNull(jwtUtil);
  }

  @Test
  @DisplayName("init: 当密钥太短时，应抛出 IllegalArgumentException")
  void init_withShortSecret_shouldThrowIllegalArgumentException() {
    // Arrange
    JwtUtil invalidJwtUtil = new JwtUtil();
    ReflectionTestUtils.setField(invalidJwtUtil, "secretString", "short-secret");

    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, invalidJwtUtil::init);
    assertThat(exception.getMessage())
        .contains("JWT secret must be at least 32 bytes (256 bits) long");
  }

  @Test
  @DisplayName("generateToken: 应为指定用户生成一个有效的 Token")
  void generateToken_shouldReturnValidToken() {

    when(userDetails.getUsername()).thenReturn("testuser");

    // Act
    String token = jwtUtil.generateToken(userDetails);

    // Assert
    assertThat(token).isNotNull().isNotEmpty();

    // 简单的验证，更详细的验证在其他测试中进行
    assertThat(jwtUtil.extractUsername(token)).isEqualTo("testuser");
  }

  @Test
  @DisplayName("extractUsername: 应从 Token 中正确提取用户名")
  void extractUsername_shouldExtractCorrectUsername() {

    when(userDetails.getUsername()).thenReturn("testuser");
    // Arrange
    String token = jwtUtil.generateToken(userDetails);

    // Act
    String extractedUsername = jwtUtil.extractUsername(token);

    // Assert
    assertThat(extractedUsername).isEqualTo("testuser");
  }

  @Test
  @DisplayName("validateToken: 对于有效的 Token 和正确的用户，应返回 true")
  void validateToken_withValidTokenAndCorrectUser_shouldReturnTrue() {

    when(userDetails.getUsername()).thenReturn("testuser");
    // Arrange
    String token = jwtUtil.generateToken(userDetails);

    // Act
    Boolean isValid = jwtUtil.validateToken(token, userDetails);

    // Assert
    assertThat(isValid).isTrue();
  }

  @Test
  @DisplayName("validateToken: 对于用户名不匹配的 Token，应返回 false")
  void validateToken_withMismatchedUsername_shouldReturnFalse() {

    when(userDetails.getUsername()).thenReturn("testuser");
    // Arrange
    String token = jwtUtil.generateToken(userDetails);
    // 更改 mock 的行为以模拟不同的用户名
    when(userDetails.getUsername()).thenReturn("anotheruser");

    // Act
    Boolean isValid = jwtUtil.validateToken(token, userDetails);

    // Assert
    assertThat(isValid).isFalse();
  }

  @Test
  @DisplayName("validateToken: 对于已过期的 Token，应返回 false")
  void validateToken_withExpiredToken_shouldReturnFalse() {

    when(userDetails.getUsername()).thenReturn("anotheruser"); // 更改 mock 的行为
    // Arrange
    // 创建一个立即过期的 Token
    ReflectionTestUtils.setField(jwtUtil, "expirationMs", -1L);
    String expiredToken = jwtUtil.generateToken(userDetails);

    ExpiredJwtException exception =
        assertThrows(
            ExpiredJwtException.class,
            () -> {
              jwtUtil.validateToken(expiredToken, userDetails);
            });
  }

  @Test
  @DisplayName("extractClaim: 尝试从已过期的 Token 中提取 claim 时，应抛出 ExpiredJwtException")
  void extractClaim_fromExpiredToken_shouldThrowExpiredJwtException() {
    // Arrange
    ReflectionTestUtils.setField(jwtUtil, "expirationMs", -1L);

    when(userDetails.getUsername()).thenReturn("testuser");

    String expiredToken = jwtUtil.generateToken(userDetails);

    // Act & Assert
    assertThrows(ExpiredJwtException.class, () -> jwtUtil.extractUsername(expiredToken));
  }

  @Test
  @DisplayName("extractClaim: 尝试从签名无效的 Token 中提取 claim 时，应抛出 SignatureException")
  void extractClaim_fromInvalidSignatureToken_shouldThrowSignatureException() {
    // Arrange
    String token = jwtUtil.generateToken(userDetails);
    // 篡改 Token (例如，在末尾添加一个字符)
    String tamperedToken = token + "a";

    // Act & Assert
    assertThrows(SignatureException.class, () -> jwtUtil.extractUsername(tamperedToken));
  }
}
