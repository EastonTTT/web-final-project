package org.backend.finalProject.Mapper;

import org.apache.ibatis.annotations.*;
import org.backend.finalProject.Pojo.AssignmentSubmissionDTO;

import java.util.List;

@Mapper
public interface AssignmentSubmissionMapper {

    // 插入作业提交记录
    @Insert("INSERT INTO webfinal.assignment_submissions (assignment_id, student_id, submit_time, content_url, text_content, grade, feedback) " +
            "VALUES (#{assignment_id}, #{student_id}, NOW(), #{content_url}, #{text_content}, #{grade}, #{feedback})")
    @Options(useGeneratedKeys = true, keyProperty = "submission_id")
    void insertSubmission(AssignmentSubmissionDTO submission);

    // 根据提交ID获取作业提交记录
    @Select("SELECT * FROM webfinal.assignment_submissions WHERE submission_id = #{submission_id}")
    AssignmentSubmissionDTO getSubmissionById(@Param("submission_id") Integer submissionId);

    // 根据学生ID获取所有作业提交记录
    @Select("SELECT * FROM webfinal.assignment_submissions WHERE student_id = #{student_id}")
    List<AssignmentSubmissionDTO> getSubmissionsByStudent(@Param("student_id") Integer studentId);

    // 获取所有待办的作业（未提交的作业）
    @Select("SELECT a.* FROM webfinal.assignments a " +
            "JOIN course_registrations cr ON a.course_id = cr.course_id " +
            "LEFT JOIN webfinal.assignment_submissions asub ON a.assignment_id = asub.assignment_id AND asub.student_id = #{student_id} " +
            "WHERE cr.student_id = #{student_id} AND asub.submission_id IS NULL")
    List<AssignmentSubmissionDTO> getPendingAssignments(@Param("student_id") Integer studentId);

    // 删除作业提交记录
    @Delete("DELETE FROM webfinal.assignment_submissions WHERE submission_id = #{submission_id} AND student_id = #{student_id}")
    void deleteSubmission(@Param("submission_id") Integer submissionId, @Param("student_id") Integer studentId);

    // 更新分数和反馈
    @Update("UPDATE webfinal.assignment_submissions SET grade = #{grade}, feedback = #{feedback} WHERE submission_id = #{submission_id}")
    void gradeSubmission(@Param("submission_id") Integer submissionId, @Param("grade") Float grade, @Param("feedback") String feedback);

    // 更新作业提交内容
    @Update("UPDATE webfinal.assignment_submissions SET text_content = #{text_content}, content_url = #{content_url}, submit_time = NOW() WHERE submission_id = #{submission_id} AND student_id = #{student_id}")
    void updateSubmission(@Param("submission_id") Integer submissionId, @Param("student_id") Integer studentId,
                          @Param("text_content") String textContent, @Param("content_url") String contentUrl);

    /**
     * 根据作业ID获取所有提交记录
     *
     * @param assignmentId 作业ID
     * @return 提交记录列表
     */
    @Select("SELECT * FROM webfinal.assignment_submissions WHERE assignment_id = #{assignmentId}")
    List<AssignmentSubmissionDTO> getSubmissionsByAssignmentId(@Param("assignmentId") Integer assignmentId);

}
