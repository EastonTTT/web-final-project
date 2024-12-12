package org.backend.finalProject.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class LoginResponseDTO {

    //返回登录者的用户名
    private String username;

    //返回登录者的用户类型，1代表学生，2代表老师，3代表管理员
    private Integer role;

    public LoginResponseDTO(String username, Integer role) {
        this.username = username;
        this.role = role;
    }

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
