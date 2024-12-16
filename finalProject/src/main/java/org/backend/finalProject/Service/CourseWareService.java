package org.backend.finalProject.Service;

import lombok.extern.slf4j.Slf4j;
import org.backend.finalProject.Mapper.CourseWareMapper;
import org.backend.finalProject.Pojo.CourseWareDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CourseWareService {

    @Autowired
    private CourseWareMapper courseWareMapper;

    // 添加单个课件
    public void addCourseWare(CourseWareDTO courseWareDTO) {
        courseWareMapper.insertCourseWare(courseWareDTO);
    }

    // 批量添加课件
    public void addCourseWares(List<CourseWareDTO> courseWares) {
        courseWareMapper.insertCourseWares(courseWares);
    }

    // 删除课件
    public void deleteCourseWare(Integer materialId) {
        courseWareMapper.deleteCourseWare(materialId);
    }

    // 根据课程ID和教师ID获取课件
    public List<CourseWareDTO> getCourseWaresByCourseAndTeacher(Integer courseId, Integer teacherId) {
        return courseWareMapper.getCourseWaresByCourseAndTeacher(courseId, teacherId);
    }

    // 根据课程ID获取课件
    public List<CourseWareDTO> getCourseWaresByCourseId(Integer courseId) {
        return courseWareMapper.getCourseWaresByCourseId(courseId);
    }

    // 获取所有课件
    public List<CourseWareDTO> getAllCourseWares() {
        return courseWareMapper.getAllCourseWares();
    }

    // 根据课件ID获取课件
    public CourseWareDTO getCourseWareById(Integer materialId) {
        return courseWareMapper.getCourseWareById(materialId);
    }

    // 更新课件信息
    public void updateCourseWare(CourseWareDTO courseWareDTO) {
        courseWareMapper.updateCourseWare(courseWareDTO);
    }
}
