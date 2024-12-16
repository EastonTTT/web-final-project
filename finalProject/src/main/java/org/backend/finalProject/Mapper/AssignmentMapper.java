package org.backend.finalProject.Mapper;

import org.apache.ibatis.annotations.*;
import org.backend.finalProject.Pojo.AssignmentDTO;
import java.util.List;

/**
 * Assignment Mapper Interface
 *
 * 使用 MyBatis 进行数据库操作，处理作业相关的增删改查。
 */
@Mapper
public interface AssignmentMapper {

    /**
     * 添加作业
     *
     * @param assignmentDTO 作业信息
     * @return 影响的行数
     */
    @Insert("INSERT INTO webfinal.assignments (course_id, title, description, due_date) " +
            "VALUES (#{course_id}, #{title}, #{description}, #{due_date})")
    @Options(useGeneratedKeys = true, keyProperty = "assignment_id")
    int addAssignment(AssignmentDTO assignmentDTO);

    /**
     * 更新作业
     *
     * @param assignmentDTO 作业信息
     * @return 影响的行数
     */
    @Update("<script>" +
            "UPDATE webfinal.assignments " +
            "<set>" +
            "<if test='course_id != null'>course_id = #{course_id},</if>" +
            "<if test='title != null'>title = #{title},</if>" +
            "<if test='description != null'>description = #{description},</if>" +
            "<if test='due_date != null'>due_date = #{due_date},</if>" +
            "</set> " +
            "WHERE assignment_id = #{assignment_id}" +
            "</script>")
    int updateAssignment(AssignmentDTO assignmentDTO);

    /**
     * 删除作业
     *
     * @param assignment_id 作业ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM webfinal.assignments WHERE assignment_id = #{assignment_id}")
    int deleteAssignment(Integer assignment_id);

    /**
     * 根据作业ID获取作业
     *
     * @param assignment_id 作业ID
     * @return 作业信息
     */
    @Select("SELECT * FROM webfinal.assignments WHERE assignment_id = #{assignment_id}")
    AssignmentDTO getAssignmentById(Integer assignment_id);

    /**
     * 根据课程ID获取所有作业
     *
     * @param course_id 课程ID
     * @return 作业列表
     */
    @Select("SELECT * FROM webfinal.assignments WHERE course_id = #{course_id}")
    List<AssignmentDTO> getAssignmentsByCourseId(Integer course_id);
}
