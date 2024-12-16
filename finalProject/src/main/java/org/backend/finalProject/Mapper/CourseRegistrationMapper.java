package org.backend.finalProject.Mapper;

import org.apache.ibatis.annotations.*;
import org.backend.finalProject.Pojo.CourseRegistrationDTO;

import java.util.List;

@Mapper
public interface CourseRegistrationMapper {

    // 插入注册记录
    @Insert("INSERT INTO webfinal.course_registrations (student_id, course_id, register_time) " +
            "VALUES (#{student_id}, #{course_id}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "registration_id")
    void insertRegistration(CourseRegistrationDTO registration);

    // 根据学生ID获取所有注册的课程
    @Select("SELECT cr.registration_id, cr.student_id, cr.course_id, cr.register_time, " +
            "c.course_name, c.course_description, c.course_image, c.start_time, c.end_time, c.status " +
            "FROM webfinal.course_registrations cr " +
            "JOIN webfinal.courses c ON cr.course_id = c.course_id " +
            "WHERE cr.student_id = #{student_id}")
    List<CourseRegistrationDTO> getRegistrationsByStudentId(@Param("student_id") Integer studentId);

    // 根据学生ID和课程ID获取注册记录
    @Select("SELECT * FROM webfinal.course_registrations WHERE student_id = #{student_id} AND course_id = #{course_id}")
    CourseRegistrationDTO getRegistration(@Param("student_id") Integer studentId, @Param("course_id") Integer courseId);

    // 删除注册记录
    @Delete("DELETE FROM webfinal.course_registrations WHERE registration_id = #{registration_id} AND student_id = #{student_id}")
    void deleteRegistration(@Param("registration_id") Integer registrationId, @Param("student_id") Integer studentId);
}
