package org.demo.aimusic.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.demo.aimusic.module.user.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {

  @Select("select * from tb_users where email = #{email}")
  User selectByEmailForAuth(@Param("email") String email);
}
