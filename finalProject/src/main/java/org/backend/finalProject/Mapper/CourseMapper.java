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
    @Select("SELECT * FROM webfinal.courses WHERE course_id = #{courseId}")
    CourseDTO getCourseById(@Param("courseId") Integer courseId);

    // 获取所有课程信息
    @Select("SELECT * FROM webfinal.courses")
    List<CourseDTO> getAllCourses();

    // 更新课程信息
    @Update("UPDATE webfinal.courses SET course_name = #{courseName}, course_description = #{courseDescription}, " +
            "course_image = #{courseImage}, course_type = #{courseType}, allow_comments = #{allowComments}, " +
            "allow_notes = #{allowNotes}, status = #{status}, start_time = #{startTime}, end_time = #{endTime}, " +
            "updated_at = NOW() WHERE course_id = #{courseId}")
    void updateCourse(CourseDTO courseDTO);

    // 删除课程
    @Delete("DELETE FROM webfinal.courses WHERE course_id = #{courseId}")
    void deleteCourse(@Param("courseId") Integer courseId);
}
