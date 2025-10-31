package org.demo.aimusic.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Optional;
import org.demo.aimusic.common.dto.PageDto;
import org.demo.aimusic.module.auth.dto.LoginUserDetails;
import org.demo.aimusic.module.user.dto.CreateUserDto;
import org.demo.aimusic.module.user.dto.UpdateUserDto;
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

  /**
   * List users with pagination and optional filtering
   * @param userQueryDto the query parameters for filtering and pagination
   * @return a {@link PageDto} containing the paginated list of {@link UserInfoDto}
   */
  PageDto<UserInfoDto> listUsers(UserQueryDto userQueryDto);

  /**
   * Create a new user
   * @param createUserDto the data for creating the new user
   * @return the created user's information as {@link UserInfoDto}
   */
  UserInfoDto createUser(CreateUserDto createUserDto);

  /**
   * Update an existing user
   * @param uuid the UUID of the user to update
   * @param updateUserDto the data for updating the user
   * @return the updated user's information as {@link UserInfoDto}
   */
  UserInfoDto updateUser(String uuid, UpdateUserDto updateUserDto);
}
