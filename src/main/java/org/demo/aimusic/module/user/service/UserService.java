package org.demo.aimusic.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.Optional;
import org.demo.aimusic.module.user.entity.User;

public interface UserService extends IService<User> {
  /**   * Find user by email
   *
   * @param email the email of the user
   * @return an {@link Optional} containing the user if found, or empty if not found
   */
  Optional<User> findByEmail(String email);
}
