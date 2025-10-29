package org.demo.aimusic.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Optional;
import org.demo.aimusic.common.dto.PageDto;
import org.demo.aimusic.module.auth.dto.LoginUserDetails;
import org.demo.aimusic.module.user.dto.UserInfoDto;
import org.demo.aimusic.module.user.dto.UserQueryDto;
import org.demo.aimusic.module.user.entity.User;

public interface UserService extends IService<User> {
  /**   * Find user by email
   *
   * @param email the email of the user
   * @return an {@link Optional} containing the user if found, or empty if not found
   */
  Optional<User> findByEmail(String email);

  /**   * Get current logged-in user information
   *
   * @param currentUser the current logged-in user details
   * @return the user information as {@link UserInfoDto}
   */
  UserInfoDto getCurrentUserInfo(LoginUserDetails currentUser);

  PageDto<UserInfoDto> listUsers(UserQueryDto userQueryDto);
}
