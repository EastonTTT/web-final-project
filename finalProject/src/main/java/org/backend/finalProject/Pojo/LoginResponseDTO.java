package org.backend.finalProject.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LoginResponseDTO {

    // 用户id
    private Integer user_id;

    //返回登录者的用户名
    private String username;

    //返回登录者的用户类型，1代表学生，2代表老师，3代表管理员
    private Integer role;

    public LoginResponseDTO(Integer user_id, String username, Integer role) {
        this.user_id = user_id;
        this.username = username;
        this.role = role;
    }

    public Integer getUser_id() { return user_id; }

    public void setUser_id(Integer user_id) { this.user_id = user_id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

}
