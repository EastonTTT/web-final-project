package org.backend.finalProject.Mapper;

import org.apache.ibatis.annotations.*;
import org.backend.finalProject.Pojo.CourseDTO;
import org.backend.finalProject.Pojo.CourseImageDTO;

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
    CourseDTO getCourseByCourseId(@Param("course_id") Integer courseId);

    // 根据课程ID获取课程信息
    @Select("SELECT * FROM webfinal.courses WHERE teacher_id = #{teacher_id}")
    List<CourseDTO> getCoursesByTeacherId(@Param("teacher_id") Integer teacherID);

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

    //课程推荐处理
    @Update("UPDATE webfinal.courses SET is_recommended = #{isRecommend} WHERE course_name = #{courseName}")
    void handleRecommend(Integer isRecommend,String courseName );

    //获取所有课程图片URL
    @Select("SELECT course_id,course_image from webfinal.courses ")
    CourseImageDTO[] getAllImage();

    @Select("SELECT c.course_id,c.course_image,c.course_name,u.username " +
            "FROM webfinal.courses c LEFT JOIN webfinal.users u ON c.teacher_id = u.user_id " +
            "WHERE c.is_recommended = 1")
    CourseImageDTO[] getRecommend();

    @Select("SELECT cr.course_id,c.course_name,c.course_image ,COUNT(cr.student_id) AS quantity,u.username " +
            "FROM webfinal.courses c LEFT JOIN webfinal.course_registrations cr on c.course_id = cr.course_id " +
            "LEFT JOIN webfinal.users u on u.user_id = c.teacher_id " +
            "GROUP BY cr.course_id,c.course_name,c.course_image,u.username " +
            "ORDER BY quantity DESC ")
    CourseImageDTO[] getPopular();

    @Select("SELECT c.course_id,c.course_image,c.course_name,u.username " +
            "FROM webfinal.courses c LEFT JOIN webfinal.users u ON c.teacher_id = u.user_id " +
            "ORDER BY c.create_at DESC LIMIT 6")
    CourseImageDTO[] getLatest();
}
