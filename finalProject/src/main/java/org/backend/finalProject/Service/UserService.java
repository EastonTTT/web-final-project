package org.backend.finalProject.Service;

import lombok.extern.slf4j.Slf4j;
import org.backend.finalProject.Mapper.UserMapper;
import org.backend.finalProject.Pojo.LoginRequestDTO;
import org.backend.finalProject.Pojo.Result;
import org.backend.finalProject.Pojo.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public UserDTO ifLogin(LoginRequestDTO loginRequestDTO){
        return userMapper.selectUser(loginRequestDTO);
    }

    public boolean register(LoginRequestDTO loginRequestDTO){
        UserDTO user = new UserDTO();
        user.setRole(1);
        user.setUsername(loginRequestDTO.getUsername());
        user.setPassword(loginRequestDTO.getPassword());
        try{
            userMapper.insertUser(user);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }


    }

}
