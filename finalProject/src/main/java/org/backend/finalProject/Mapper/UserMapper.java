package org.backend.finalProject.Mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.backend.finalProject.Pojo.LoginRequestDTO;
import org.backend.finalProject.Pojo.UserDTO;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM webfinal.users WHERE  username = #{username} AND password = #{password}")
    public UserDTO selectUser(LoginRequestDTO loginRequestDTO);

    @Insert("INSERT INTO webfinal.users (username, password, email, role)" +
            "VALUES (#{username},#{password},#{email},#{role})")
    public void insertUser(UserDTO userDTO);

    @Select("SELECT username FROM webfinal.users WHERE user_id = #{user_id}")
    public String selectUsernameById(int user_id);
}
