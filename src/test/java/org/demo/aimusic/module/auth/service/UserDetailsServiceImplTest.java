package org.demo.aimusic.module.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.demo.aimusic.module.user.entity.User;
import org.demo.aimusic.module.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

  @Mock private UserMapper userMapper;

  private UserDetailsServiceImpl userDetailsService;

  @BeforeEach
  void setUp() {
    userDetailsService = new UserDetailsServiceImpl(userMapper);
  }

  @Test
  void whenUserExists_returnUserDetails() {
    String email = "test@example.com";
    User user = new User();
    user.setEmail(email);
    user.setPasswordHash("hashed-password");

    when(userMapper.selectOne(any())).thenReturn(user);

    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    assertNotNull(userDetails);
    assertEquals(email, userDetails.getUsername());
  }

  @Test
  void whenUserNotFound_throwUsernameNotFoundException() {
    when(userMapper.selectOne(any())).thenReturn(null);

    assertThrows(
        UsernameNotFoundException.class,
        () -> userDetailsService.loadUserByUsername("notfound@example.com"));
  }
}
