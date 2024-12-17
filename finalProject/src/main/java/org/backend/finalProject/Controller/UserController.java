package org.backend.finalProject.Controller;

import org.backend.finalProject.Pojo.ResetPasswordDTO;
import org.backend.finalProject.Pojo.Result;
import org.backend.finalProject.Pojo.UserDTO;
import org.backend.finalProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/{userId}")
    public Result<UserDTO> userInfoHandler(@PathVariable Integer userId ) {
        UserDTO user = userService.userInfo(userId);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.failed("此用户不存在");

        }
    }

    @GetMapping("/accountAll")
    public Result<List<UserDTO>> accountAllHandler(){
        List<UserDTO> users = userService.selectAllUser();
        if(users != null){
            return Result.success(users);
        }else{
            return Result.failed("没有查询到用户");
        }

    }

    @DeleteMapping("/delete/{username}")
    public Result deleteUser(@PathVariable String username){
        if(userService.deleteUser(username)){
            return Result.success("删除用户成功");
        }else {
            return Result.failed("删除用户失败");
        }

    }

    @PostMapping("/update")
    public Result resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        if(userService.resetPassword(resetPasswordDTO.getUsername())){
            return Result.success("重置密码成功");
        }else {
            return Result.failed("重置密码失败");
        }
    }


}
