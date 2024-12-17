package org.backend.finalProject.Service;

import lombok.extern.slf4j.Slf4j;
import org.backend.finalProject.Mapper.CourseMapper;
import org.backend.finalProject.Pojo.CourseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    // 添加课程
    public void addCourse(CourseDTO courseDTO) {
        courseMapper.insertCourse(courseDTO);
    }

    // 根据课程ID获取课程
    public CourseDTO getCourseByCourseId(Integer courseId) {
        return courseMapper.getCourseByCourseId(courseId);
    }

    // 根据教师ID获取课程
    public List<CourseDTO> getCoursesByTeacherId(Integer courseId) {
        return courseMapper.getCoursesByTeacherId(courseId);
    }

    // 获取所有课程
    public List<CourseDTO> getAllCourses() {
        return courseMapper.getAllCourses();
    }

    // 更新课程信息
    public void updateCourse(CourseDTO courseDTO) {
        courseMapper.updateCourse(courseDTO);
    }

    // 删除课程
    public void deleteCourse(Integer courseId) {
        courseMapper.deleteCourse(courseId);
    }

    // 设置推荐
    public void updateRecommendation(Integer courseId, Boolean isRecommended) {
        courseMapper.updateRecommendation(courseId, isRecommended);
    }
}
