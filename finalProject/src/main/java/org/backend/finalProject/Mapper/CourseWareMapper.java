package org.backend.finalProject.Mapper;

import org.apache.ibatis.annotations.*;
import org.backend.finalProject.Pojo.CourseWareDTO;

import java.util.List;

@Mapper
public interface CourseWareMapper {

    // 插入单个课件
    @Insert("INSERT INTO webfinal.courseware (course_id, material_name, file_path, created_at, updated_at) " +
            "VALUES (#{course_id}, #{material_name}, #{file_path}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "material_id")
    void insertCourseWare(CourseWareDTO courseWareDTO);

    // 批量插入课件
    @Insert({
            "<script>",
            "INSERT INTO webfinal.courseware (course_id, material_name, file_path, created_at, updated_at) VALUES ",
            "<foreach collection='courseWares' item='courseWare' separator=','>",
            "(#{courseWare.course_id}, #{courseWare.material_name}, #{courseWare.file_path}, NOW(), NOW())",
            "</foreach>",
            "</script>"
    })
    void insertCourseWares(@Param("courseWares") List<CourseWareDTO> courseWares);

    // 根据课件ID删除课件
    @Delete("DELETE FROM webfinal.courseware WHERE material_id = #{material_id}")
    void deleteCourseWare(@Param("material_id") Integer materialId);

    // 根据课程ID和教师ID获取所有课件
    @Select("SELECT cw.* FROM webfinal.courseware cw " +
            "JOIN courses c ON cw.course_id = c.course_id " +
            "WHERE cw.course_id = #{course_id} AND c.teacher_id = #{teacher_id}")
    List<CourseWareDTO> getCourseWaresByCourseAndTeacher(@Param("course_id") Integer courseId, @Param("teacher_id") Integer teacherId);

    // 根据课程ID获取所有课件
    @Select("SELECT * FROM webfinal.courseware WHERE course_id = #{course_id}")
    List<CourseWareDTO> getCourseWaresByCourseId(@Param("course_id") Integer courseId);

    // 获取所有课件
    @Select("SELECT * FROM webfinal.courseware")
    List<CourseWareDTO> getAllCourseWares();

    // 根据课件ID获取课件
    @Select("SELECT * FROM webfinal.courseware WHERE material_id = #{material_id}")
    CourseWareDTO getCourseWareById(@Param("material_id") Integer materialId);

    // 更新课件信息
    @Update("<script>" +
            "UPDATE webfinal.courseware " +
            "<set>" +
            "<if test='material_name != null'>material_name = #{material_name},</if>" +
            "<if test='file_path != null'>file_path = #{file_path},</if>" +
            "updated_at = NOW() " +
            "</set>" +
            "WHERE material_id = #{material_id}" +
            "</script>")
    void updateCourseWare(CourseWareDTO courseWareDTO);
}
