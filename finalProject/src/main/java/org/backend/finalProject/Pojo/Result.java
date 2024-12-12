package org.backend.finalProject.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Result<T> {

    //状态码
    private Integer status;

    //响应信息
    private String message;

    //响应结果数据
    private T data;

//    //错误类型响应
//    private String errCode;

    public static <T> Result<T> success(){

        Result<T> result = new Result<>();
        result.status = 200;
        result.message = "success.";
        result.data = null;

        return result;
    }
    public static <T> Result<T> success(String msg){

        Result<T> result = new Result<>();
        result.status = 200;
        result.message = msg;
        result.data = null;

        return result;
    }
    public static <T> Result<T> success(T data){

        Result<T> result = new Result<>();
        result.status = 200;
        result.message = "success.";
        result.data = data;

        return result;
    }

    public static <T> Result<T> success(String msg,T data){

        Result<T> result = new Result<>();
        result.status = 200;
        result.message = msg;
        result.data = data;

        return result;
    }

    public static <T> Result<T> failed(){

        Result<T> result = new Result<>();
        result.status = 400;
        result.data = null;
        result.message = "error";

        return result;
    }

    public static <T> Result<T> failed(String msg){

        Result<T> result = new Result<>();
        result.status = 400;
        result.data = null;
        result.message = msg;

        return result;
    }

    public static <T> Result<T> failed(String msg,T data){

        Result<T> result = new Result<>();
        result.status = 400;
        result.data = data;
        result.message = msg;

        return result;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
