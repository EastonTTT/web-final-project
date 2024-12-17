package org.backend.finalProject.Mapper;

import org.apache.ibatis.annotations.*;
import org.backend.finalProject.Pojo.LoginRequestDTO;
import org.backend.finalProject.Pojo.UserDTO;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM webfinal.users WHERE  username = #{username} AND password = #{password}")
    UserDTO selectUser(LoginRequestDTO loginRequestDTO);

    @Insert("INSERT INTO webfinal.users (username, password, email, role)" +
            "VALUES (#{username},#{password},#{email},#{role})")
    void insertUser(UserDTO userDTO);

    @Select("SELECT username FROM webfinal.users WHERE user_id = #{user_id}")
    String selectUsernameById(int user_id);

    @Select("SELECT * FROM webfinal.users WHERE user_id = #{user_id}")
    UserDTO selectUserByID(Integer user_id);

    @Select("SELECT * FROM webfinal.users")
    List<UserDTO> selectAllUser();

    @Update("UPDATE webfinal.users SET password = '66666666' WHERE username = #{username}")
    void resetPassword(String username);

    @Delete("DELETE FROM webfinal.users WHERE username = #{username}")
    void deleteUser(String username);
}
