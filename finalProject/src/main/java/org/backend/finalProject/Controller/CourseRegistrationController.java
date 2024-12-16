package org.backend.finalProject.Controller;

import org.backend.finalProject.Pojo.CourseRegistrationDTO;
import org.backend.finalProject.Pojo.Result;
import org.backend.finalProject.Service.CourseRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-registrations")
public class CourseRegistrationController {

    @Autowired
    private CourseRegistrationService registrationService;

    /**
     * 注册课程
     * POST /course-registrations/register
     * 请求参数：student_id, course_id
     */
    @PostMapping("/register")
    public Result<?> registerCourse(@RequestParam Integer student_id, @RequestParam Integer course_id) {
        return registrationService.registerCourse(student_id, course_id);
    }

    /**
     * 获取用户已注册的所有课程
     * GET /course-registrations/registered?student_id=xxx
     */
    @GetMapping("/registered")
    public Result<?> getRegisteredCourses(@RequestParam Integer student_id) {
        return registrationService.getRegisteredCourses(student_id);
    }

    /**
     * 取消注册课程
     * DELETE /course-registrations/cancel?registration_id=xxx&student_id=yyy
     */
    @DeleteMapping("/cancel")
    public Result<?> cancelRegistration(@RequestParam Integer registration_id, @RequestParam Integer student_id) {
        return registrationService.cancelRegistration(registration_id, student_id);
    }
}
