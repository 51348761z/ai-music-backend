package org.demo.aimusic.module.user.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.demo.aimusic.module.user.entity.User;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class UserInfoDto {
  private String uuid;
  private String userIdStr;
  private String email;
  private String nickname;
  private String avatarUrl;
  private String role;
  private String status;
  private Integer pointsBalance;
  private LocalDateTime creatAt;
  private Integer version;

  public static UserInfoDto fromEntity(User user) {
    UserInfoDto dto = new UserInfoDto();
    BeanUtils.copyProperties(user, dto);
    return dto;
  }
}
