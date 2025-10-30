package org.demo.aimusic.module.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.demo.aimusic.common.dto.PageDto;
import org.demo.aimusic.common.enums.ApiResultCode;
import org.demo.aimusic.common.exception.BusinessException;
import org.demo.aimusic.common.util.GeneratorUtil;
import org.demo.aimusic.module.auth.dto.LoginUserDetails;
import org.demo.aimusic.module.user.dto.CreateUserDto;
import org.demo.aimusic.module.user.dto.UserInfoDto;
import org.demo.aimusic.module.user.dto.UserQueryDto;
import org.demo.aimusic.module.user.entity.User;
import org.demo.aimusic.module.user.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

  private final PasswordEncoder passwordEncoder;

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(
        this.baseMapper.selectOne((new LambdaQueryWrapper<User>()).eq(User::getEmail, email)));
  }

  @Override
  public UserInfoDto getCurrentUserInfo(LoginUserDetails currentUser) {
    User user =
        this.findByEmail(currentUser.getUsername())
            .orElseThrow(
                () ->
                    new BusinessException(
                        ApiResultCode.NOT_FOUND_404,
                        "Authenticated user not found in database. This might indicate a data inconsistency."));

    return UserInfoDto.fromEntity(user);
  }

  @Override
  public PageDto<UserInfoDto> listUsers(UserQueryDto userQueryDto) {
    Page<User> page = new Page<>(userQueryDto.getCurrentPage(), userQueryDto.getPageSize());

    LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(
        StringUtils.hasText(userQueryDto.getNickname()),
        User::getNickname,
        userQueryDto.getNickname());
    queryWrapper.eq(
        StringUtils.hasText(userQueryDto.getEmail()), User::getEmail, userQueryDto.getEmail());
    queryWrapper.eq(
        StringUtils.hasText(userQueryDto.getStatus()), User::getStatus, userQueryDto.getStatus());
    queryWrapper.orderByDesc(User::getCreatedAt);
    IPage<User> userPage = this.baseMapper.selectPage(page, queryWrapper);
    List<UserInfoDto> userInfoDtoList =
        userPage.getRecords().stream().map(UserInfoDto::fromEntity).toList();
    IPage<UserInfoDto> resultPage =
        new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
    resultPage.setRecords(userInfoDtoList);
    return PageDto.fromPage(resultPage);
  }

  @Override
  public UserInfoDto createUser(CreateUserDto createUserDto) {
    this.findByEmail(createUserDto.getEmail())
        .ifPresent(
            user -> {
              throw new BusinessException(
                  ApiResultCode.CONFLICT_409,
                  "Email " + user.getEmail() + " is already registered.");
            });
    String hashedPassword = passwordEncoder.encode(createUserDto.getPassword());

    User newUser =
        User.builder()
            .email(createUserDto.getEmail())
            .nickname(createUserDto.getNickname())
            .userIdStr(GeneratorUtil.generatedUniqueUserIdStr())
            .uuid(UUID.randomUUID().toString())
            .role(createUserDto.getRole())
            .status(createUserDto.getStatus())
            .passwordHash(hashedPassword)
            .version(0)
            .pointsBalance(0)
            .build();

    if (!this.save(newUser)) {
      throw new BusinessException(
          ApiResultCode.INTERNAL_SERVER_ERROR_500, "Failed to save new user to database.");
    }

    return UserInfoDto.fromEntity(newUser);
  }
}
