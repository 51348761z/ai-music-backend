package org.demo.aimusic.module.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.demo.aimusic.module.auth.dto.LoginUserDetails;
import org.demo.aimusic.module.user.entity.User;
import org.demo.aimusic.module.user.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserMapper userMapper;

  /**
   * Load user by email for authentication.
   *
   * @param email the email of the user
   * @return UserDetails object containing user information
   * @throws UsernameNotFoundException if user is not found
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.debug("Attempting to load user by email: {}", email);

    User user = userMapper.selectOne((new LambdaQueryWrapper<User>().eq(User::getEmail, email)));
    if (user == null) {
      log.warn("User not found with email: {}", email);
      throw new UsernameNotFoundException("User not found with email: " + email);
    }

    log.info("User found and loaded successfully: {}", email);

    return new LoginUserDetails(user);
  }
}
