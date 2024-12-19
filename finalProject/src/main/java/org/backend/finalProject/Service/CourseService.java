package org.backend.finalProject.Service;

import lombok.extern.slf4j.Slf4j;
import org.backend.finalProject.Mapper.CourseMapper;
import org.backend.finalProject.Pojo.CourseDTO;
import org.backend.finalProject.Pojo.CourseImageDTO;
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

    //推荐课程
    public boolean recommendCourse(String courseName){
        try {
            courseMapper.handleRecommend(1,courseName);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    //取消推荐课程
    public boolean cancelRecommend(String courseName){
        try {
            courseMapper.handleRecommend(0,courseName);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    //获取所有课程图片URL
    public CourseImageDTO[] getAllImage(){
        try {
            return courseMapper.getAllImage();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public CourseImageDTO[] getRecommend() {
        try {
            return courseMapper.getRecommend();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public CourseImageDTO[] getPopular() {
        try {
            return courseMapper.getPopular();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public CourseImageDTO[] getLatest() {
        try {
            return courseMapper.getLatest();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
