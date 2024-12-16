package org.backend.finalProject.Controller;

import org.backend.finalProject.Pojo.CourseWareDTO;
import org.backend.finalProject.Pojo.Result;
import org.backend.finalProject.Service.CourseWareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courseware")  // 更新请求路径
public class CourseWareController {

    @Autowired
    private CourseWareService courseWareService;

    /**
     * 添加单个课件
     * POST /courseware
     */
    @PostMapping
    public Result<?> addCourseWare(@RequestBody CourseWareDTO courseWareDTO) {
        try {
            courseWareService.addCourseWare(courseWareDTO);
            return Result.success("课件添加成功");
        } catch (Exception e) {
            return Result.failed("添加课件失败: " + e.getMessage());
        }
    }

    /**
     * 批量添加课件
     * POST /courseware/batch
     */
    @PostMapping("/batch")
    public Result<?> addCourseWares(@RequestBody List<CourseWareDTO> courseWares) {
        try {
            courseWareService.addCourseWares(courseWares);
            return Result.success("课件批量添加成功");
        } catch (Exception e) {
            return Result.failed("批量添加课件失败: " + e.getMessage());
        }
    }

    /**
     * 删除课件
     * POST /courseware/delete
     * 请求体中包含要删除的课件ID
     */
    @PostMapping("/delete")
    public Result<?> deleteCourseWare(@RequestBody Integer materialId) {
        try {
            courseWareService.deleteCourseWare(materialId);
            return Result.success("课件删除成功");
        } catch (Exception e) {
            return Result.failed("删除课件失败: " + e.getMessage());
        }
    }

    /**
     * 更新课件信息
     * POST /courseware/update
     * 请求体中包含更新后的课件信息
     */
    @PostMapping("/update")
    public Result<?> updateCourseWare(@RequestBody CourseWareDTO courseWareDTO) {
        try {
            // 确保请求体中提供了课件ID
            if (courseWareDTO.getMaterial_id() == null) {
                return Result.failed("更新课件时必须提供课件ID");
            }
            courseWareService.updateCourseWare(courseWareDTO);
            return Result.success("课件更新成功");
        } catch (Exception e) {
            return Result.failed("更新课件失败: " + e.getMessage());
        }
    }

    /**
     * 根据课程ID和教师ID获取课件
     * GET /courseware?courseId=xx&teacherId=xx
     */
    @GetMapping
    public Result<List<CourseWareDTO>> getCourseWaresByCourseAndTeacher(
            @RequestParam Integer courseId,
            @RequestParam Integer teacherId) {
        try {
            List<CourseWareDTO> courseWares = courseWareService.getCourseWaresByCourseAndTeacher(courseId, teacherId);
            if (courseWares != null && !courseWares.isEmpty()) {
                return Result.success(courseWares);
            } else {
                return Result.failed("未找到指定课程的课件");
            }
        } catch (Exception e) {
            return Result.failed("获取课件失败: " + e.getMessage());
        }
    }

    /**
     * 根据课程ID获取所有课件
     * GET /courseware/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public Result<List<CourseWareDTO>> getCourseWaresByCourseId(@PathVariable Integer courseId) {
        try {
            List<CourseWareDTO> courseWares = courseWareService.getCourseWaresByCourseId(courseId);
            if (courseWares != null && !courseWares.isEmpty()) {
                return Result.success(courseWares);
            } else {
                return Result.failed("未找到指定课程的课件");
            }
        } catch (Exception e) {
            return Result.failed("获取课件失败: " + e.getMessage());
        }
    }
}
