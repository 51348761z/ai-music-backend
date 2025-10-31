package org.demo.aimusic.module.user.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.demo.aimusic.common.enums.DatePattern;

@Getter
@Setter
@TableName("tb_users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.ASSIGN_ID)
  private Long id;

  @Size(max = 36)
  @NotNull
  @TableField("uuid")
  private String uuid;

  @Size(max = 50)
  @NotNull
  @TableField("user_id_str")
  private String userIdStr;

  @Size(max = 100)
  @NotNull
  @TableField("email")
  private String email;

  @Size(max = 255)
  @NotNull
  @TableField(value = "password_hash",select = false)
  private String passwordHash;

  @Size(max = 50)
  @NotNull
  @TableField("nickname")
  private String nickname;

  @Size(max = 512)
  @TableField("avatar_url")
  private String avatarUrl;

  @Size(max = 50)
  @NotNull
  @TableField("role")
  @Builder.Default
  private String role = "user";

  @NotNull
  @TableField("points_balance")
  @Builder.Default
  private Integer pointsBalance = 0;

  @Size(max = 20)
  @NotNull
  @TableField("status")
  @Builder.Default
  private String status = "enabled";

  @NotNull
  @Version
  @TableField("version")
  @Builder.Default
  private Integer version = 0;

  @NotNull
  @TableField(value = "created_at", fill = FieldFill.INSERT)
  @JsonFormat(shape = Shape.STRING, pattern = DatePattern.YYYY_MM_DD_HH_MM_SS)
  private LocalDateTime createdAt;

  @NotNull
  @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
  @JsonFormat(shape = Shape.STRING, pattern = DatePattern.YYYY_MM_DD_HH_MM_SS)
  private LocalDateTime updatedAt;
}
