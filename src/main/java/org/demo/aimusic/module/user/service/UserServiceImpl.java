package org.demo.aimusic.module.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Optional;
import org.demo.aimusic.module.user.entity.User;
import org.demo.aimusic.module.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(
        this.baseMapper.selectOne((new LambdaQueryWrapper<User>()).eq(User::getEmail, email)));
  }

}
