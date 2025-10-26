package org.demo.aimusic.module.auth.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import org.demo.aimusic.module.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

@DisplayName("LoginUserDetails tests")
class LoginUserDetailsTest {

  private User testUser;

  @BeforeEach
  void setUp() {
    // Initialize a standard User object before each test method runs
    testUser = new User();
    testUser.setEmail("test@example.com");
    testUser.setPasswordHash("hashedPassword");
    testUser.setRole("user");
    testUser.setStatus("enable");
  }

  @Test
  @DisplayName("Constructor: should create instance when User is not null")
  void constructor_WithValidUser_ShouldCreateInstance() {
    // Act & Assert
    assertDoesNotThrow(() -> new LoginUserDetails(testUser));
  }

  @Test
  @DisplayName("Constructor: should throw IllegalArgumentException when User is null")
  void constructor_WithNullUser_ShouldThrowException() {
    // Act & Assert
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new LoginUserDetails(null));
    assertThat(exception.getMessage()).isEqualTo("User cannot be null");
  }

  @Test
  @DisplayName("getAuthorities: returns 'ROLE_USER' when role is 'user'")
  void getAuthorities_WithUserRole_ShouldReturnRoleUserAuthority() {
    // Arrange
    LoginUserDetails loginUserDetails = new LoginUserDetails(testUser);

    // Act
    Collection<? extends GrantedAuthority> authorities = loginUserDetails.getAuthorities();

    // Assert
    assertThat(authorities)
        .hasSize(1)
        .extracting(GrantedAuthority::getAuthority)
        .contains("ROLE_USER");
  }

  @Test
  @DisplayName("getAuthorities: returns 'ROLE_ADMIN' when role is 'ADMIN' (uppercase)")
  void getAuthorities_WithAdminRole_ShouldReturnRoleAdminAuthority() {
    // Arrange
    testUser.setRole("ADMIN");
    LoginUserDetails loginUserDetails = new LoginUserDetails(testUser);

    // Act
    Collection<? extends GrantedAuthority> authorities = loginUserDetails.getAuthorities();

    // Assert
    assertThat(authorities)
        .hasSize(1)
        .extracting(GrantedAuthority::getAuthority)
        .contains("ROLE_ADMIN");
  }

  @Test
  @DisplayName("getAuthorities: returns empty authorities when role is null or empty")
  void getAuthorities_WithNullOrEmptyRole_ShouldReturnEmptySet() {
    // Arrange
    testUser.setRole(null);
    LoginUserDetails userDetailsWithNullRole = new LoginUserDetails(testUser);

    testUser.setRole("");
    LoginUserDetails userDetailsWithEmptyRole = new LoginUserDetails(testUser);

    // Act
    Collection<? extends GrantedAuthority> authorities1 = userDetailsWithNullRole.getAuthorities();
    Collection<? extends GrantedAuthority> authorities2 = userDetailsWithEmptyRole.getAuthorities();

    // Assert
    assertThat(authorities1).isNotNull().isEmpty();
    assertThat(authorities2).isNotNull().isEmpty();
  }

  @Test
  @DisplayName("getPassword: returns the user's password hash")
  void getPassword_ShouldReturnUserPasswordHash() {
    // Arrange
    LoginUserDetails loginUserDetails = new LoginUserDetails(testUser);

    // Act & Assert
    assertThat(loginUserDetails.getPassword()).isEqualTo("hashedPassword");
  }

  @Test
  @DisplayName("getUsername: returns the user's email address")
  void getUsername_ShouldReturnUserEmail() {
    // Arrange
    LoginUserDetails loginUserDetails = new LoginUserDetails(testUser);

    // Act & Assert
    assertThat(loginUserDetails.getUsername()).isEqualTo("test@example.com");
  }

  @Test
  @DisplayName("isAccountNonExpired: always returns true")
  void isAccountNonExpired_ShouldReturnTrue() {
    // Arrange
    LoginUserDetails loginUserDetails = new LoginUserDetails(testUser);

    // Act & Assert
    assertThat(loginUserDetails.isAccountNonExpired()).isTrue();
  }

  @Test
  @DisplayName("isAccountNonLocked: always returns true")
  void isAccountNonLocked_ShouldReturnTrue() {
    // Arrange
    LoginUserDetails loginUserDetails = new LoginUserDetails(testUser);

    // Act & Assert
    assertThat(loginUserDetails.isAccountNonLocked()).isTrue();
  }

  @Test
  @DisplayName("isCredentialsNonExpired: always returns true")
  void isCredentialsNonExpired_ShouldReturnTrue() {
    // Arrange
    LoginUserDetails loginUserDetails = new LoginUserDetails(testUser);

    // Act & Assert
    assertThat(loginUserDetails.isCredentialsNonExpired()).isTrue();
  }

  @Test
  @DisplayName("isEnabled: returns true when status is 'enable' (case-insensitive)")
  void isEnabled_WhenStatusIsEnable_ShouldReturnTrue() {
    // Arrange
    testUser.setStatus("enable");
    LoginUserDetails loginUserDetails1 = new LoginUserDetails(testUser);

    testUser.setStatus("ENABLE");
    LoginUserDetails loginUserDetails2 = new LoginUserDetails(testUser);

    // Act & Assert
    assertThat(loginUserDetails1.isEnabled()).isTrue();
    assertThat(loginUserDetails2.isEnabled()).isTrue();
  }

  @Test
  @DisplayName("isEnabled: returns false when status is not 'enable'")
  void isEnabled_WhenStatusIsNotEnable_ShouldReturnFalse() {
    // Arrange
    testUser.setStatus("disabled");
    LoginUserDetails loginUserDetails = new LoginUserDetails(testUser);

    // Act & Assert
    assertThat(loginUserDetails.isEnabled()).isFalse();
  }

  @Test
  @DisplayName("getUserEntity: returns the original User object")
  void getUserEntity_ShouldReturnOriginalUserObject() {
    // Arrange
    LoginUserDetails loginUserDetails = new LoginUserDetails(testUser);

    // Act & Assert
    assertThat(loginUserDetails.getUserEntity()).isSameAs(testUser);
  }

  @Test
  @DisplayName("equals and hashCode: comparison based on username")
  void equalsAndHashCode_ShouldBeBasedOnUsername() {
    // Arrange
    User user1 = new User();
    user1.setEmail("test@example.com");
    user1.setPasswordHash("pass1");
    user1.setRole("user");
    user1.setStatus("enable");
    LoginUserDetails details1 = new LoginUserDetails(user1);

    User user2 = new User();
    user2.setEmail("test@example.com");
    user2.setPasswordHash("pass2"); // different password
    user2.setRole("admin"); // different role
    user2.setStatus("enable");
    LoginUserDetails details2 = new LoginUserDetails(user2);

    User user3 = new User();
    user3.setEmail("another@example.com");
    user3.setPasswordHash("pass1");
    user3.setRole("user");
    user3.setStatus("enable");
    LoginUserDetails details3 = new LoginUserDetails(user3);

    // Act & Assert
    // equals
    assertThat(details1).isEqualTo(details2); // email 相同，应该相等
    assertThat(details1).isNotEqualTo(details3); // email 不同，不相等
    assertThat(details1).isNotEqualTo(null);
    assertThat(details1).isNotEqualTo(new Object());

    // hashCode
    assertThat(details1.hashCode()).isEqualTo(details2.hashCode());
    assertThat(details1.hashCode()).isNotEqualTo(details3.hashCode());
  }
}
