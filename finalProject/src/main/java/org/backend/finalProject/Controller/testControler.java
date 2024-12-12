package org.backend.finalProject.Controller;

import org.backend.finalProject.Pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("")
public class testControler {

    @GetMapping("/test")
    public <T> Result<T> test(){
        return Result.success();
    }
}
