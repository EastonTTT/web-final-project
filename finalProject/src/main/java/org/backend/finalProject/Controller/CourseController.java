package org.backend.finalProject.Controller;

import org.backend.finalProject.Pojo.CourseDTO;
import org.backend.finalProject.Pojo.Result;
import org.backend.finalProject.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 获取所有课程
     * 前端可通过GET请求: GET /courses
     */
    @GetMapping
    public Result<List<CourseDTO>> getAllCourses() {
        try {
            List<CourseDTO> courses = courseService.getAllCourses();
            return Result.success(courses);
        } catch (Exception e) {
            return Result.failed("获取课程列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据课程ID获取课程
     * 前端可通过GET请求: GET /courses/{courseId}
     */
    @GetMapping("/{courseId}")
    public Result<CourseDTO> getCourseById(@PathVariable Integer courseId) {
        try {
            CourseDTO course = courseService.getCourseById(courseId);
            if (course != null) {
                return Result.success(course);
            } else {
                return Result.failed("未找到指定课程");
            }
        } catch (Exception e) {
            return Result.failed("获取课程信息失败: " + e.getMessage());
        }
    }

    /**
     * 使用POST的action参数执行增、改、删操作
     * 前端调用方式示例：
     * POST /courses/action?action=add     （请求体中包含课程信息，新增课程）
     * POST /courses/action?action=update  （请求体中包含courseId及修改信息，更新课程）
     * POST /courses/action?action=delete  （请求体中包含courseId，删除课程）
     */
    @PostMapping("/action")
    public Result<?> handleCourseAction(@RequestParam String action, @RequestBody CourseDTO courseDTO) {
        try {
            switch (action) {
                case "add":
                    System.out.println("Received CourseDTO: " + courseDTO);
                    // 打印 courseDTO.teacherId 是否为空
                    System.out.println("teacherId: " + courseDTO.getTeacher_id());
                    courseService.addCourse(courseDTO);

                    return Result.success("课程添加成功");

                case "update":
                    // 更新课程需要courseId为必传值
                    if (courseDTO.getCourse_id() == null) {
                        return Result.failed("更新课程时必须提供课程ID");
                    }
                    courseService.updateCourse(courseDTO);
                    return Result.success("课程更新成功");

                case "delete":
                    // 删除课程同样需要courseId
                    if (courseDTO.getCourse_id() == null) {
                        return Result.failed("删除课程时必须提供课程ID");
                    }
                    courseService.deleteCourse(courseDTO.getCourse_id());
                    return Result.success("课程删除成功");

                default:
                    return Result.failed("无效的操作类型");
            }
        } catch (Exception e) {
            return Result.failed("操作失败: " + e.getMessage());
        }
    }
}