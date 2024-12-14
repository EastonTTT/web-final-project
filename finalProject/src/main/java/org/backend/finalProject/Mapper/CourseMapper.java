package org.backend.finalProject.Mapper;

import org.apache.ibatis.annotations.*;
import org.backend.finalProject.Pojo.CourseDTO;

import java.util.List;

@Mapper
public interface CourseMapper {

    // 插入课程信息
    @Insert("INSERT INTO webfinal.courses (teacher_id, course_name, course_description, course_image, course_type, " +
            "allow_comments, allow_notes, status, start_time, end_time, create_at, updated_at) " +
            "VALUES (#{teacher_id}, #{course_name}, #{course_description}, #{course_image}, #{course_type}, " +
            "#{allow_comments}, #{allow_notes}, #{status}, #{start_time}, #{end_time}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "course_id")
    void insertCourse(CourseDTO courseDTO);

    // 根据课程ID获取课程信息
    @Select("SELECT * FROM webfinal.courses WHERE course_id = #{course_id}")
    CourseDTO getCourseById(@Param("course_id") Integer courseId);

    // 获取所有课程信息
    @Select("SELECT * FROM webfinal.courses")
    List<CourseDTO> getAllCourses();

    // 更新课程信息
    @Update("UPDATE webfinal.courses SET course_name = #{course_name}, course_description = #{course_description}, " +
            "course_image = #{course_image}, course_type = #{course_type}, allow_comments = #{allow_comments}, " +
            "allow_notes = #{allow_notes}, status = #{status}, start_time = #{start_time}, end_time = #{end_time}, " +
            "updated_at = NOW() WHERE course_id = #{course_id}")
    void updateCourse(CourseDTO courseDTO);

    // 删除课程
    @Delete("DELETE FROM webfinal.courses WHERE course_id = #{course_id}")
    void deleteCourse(@Param("course_id") Integer courseId);
}
