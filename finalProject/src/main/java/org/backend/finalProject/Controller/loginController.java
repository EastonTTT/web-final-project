package org.backend.finalProject.Controller;

import org.backend.finalProject.Pojo.LoginRequestDTO;
import org.backend.finalProject.Pojo.Result;
import org.backend.finalProject.Pojo.UserDTO;
import org.backend.finalProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.backend.finalProject.Pojo.LoginResponseDTO;
@RestController

public class loginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginResponseDTO> loginHandler(@RequestBody LoginRequestDTO loginRequestDTO){

        UserDTO user = userService.ifLogin(loginRequestDTO);
        if (user != null){
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getUsername(),user.getRole());
            return Result.success(loginResponseDTO);
        }else {
            return Result.failed("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    public Result registerHandler(@RequestBody LoginRequestDTO loginRequestDTO){
        if(userService.register(loginRequestDTO)){
            return Result.success("注册成功");
        }else {
            return Result.failed("用户已存在");
        }
    }
}
