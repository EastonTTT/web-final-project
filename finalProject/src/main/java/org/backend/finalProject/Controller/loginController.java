package org.backend.finalProject.Controller;

import org.backend.finalProject.Pojo.LoginRequestDTO;
import org.backend.finalProject.Pojo.Result;
import org.backend.finalProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class loginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result loginHandler(@RequestBody LoginRequestDTO loginRequestDTO){
        if (userService.ifLogin(loginRequestDTO)){
            return Result.success();
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
