package org.backend.finalProject.Service;

import org.backend.finalProject.Mapper.AssignmentSubmissionMapper;
import org.backend.finalProject.Pojo.AssignmentSubmissionDTO;
import org.backend.finalProject.Pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentSubmissionService {

    @Autowired
    private AssignmentSubmissionMapper submissionMapper;

    /**
     * 提交作业
     *
     * @param submission 作业提交信息
     * @return 操作结果
     */
    public Result<String> submitAssignment(AssignmentSubmissionDTO submission) {
        try {
            submissionMapper.insertSubmission(submission);
            return Result.success("作业提交成功");
        } catch (Exception e) {
            return Result.failed("作业提交失败: " + e.getMessage());
        }
    }

    /**
     * 更新已提交的作业
     *
     * @param submission 作业提交信息
     * @return 操作结果
     */
    public Result<String> updateAssignmentSubmission(AssignmentSubmissionDTO submission) {
        try {
            // 获取现有提交记录
            AssignmentSubmissionDTO existingSubmission = submissionMapper.getSubmissionById(submission.getSubmission_id());
            if (existingSubmission == null) {
                return Result.failed("未找到对应的作业提交记录");
            }

            // 检查是否已评分或已过截止日期
            // 假设已有方法检查截止日期，可以根据实际情况调整
            // 这里只检查是否已评分
            if (existingSubmission.getGrade() != null) {
                return Result.failed("该作业已经评分，无法更新");
            }

            submissionMapper.updateSubmission(
                    submission.getSubmission_id(),
                    submission.getStudent_id(),
                    submission.getText_content(),
                    submission.getContent_url()
            );
            return Result.success("作业更新成功");
        } catch (Exception e) {
            return Result.failed("作业更新失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的待办作业
     *
     * @param studentId 学生ID
     * @return 待办作业列表
     */
    public Result<List<AssignmentSubmissionDTO>> getTodoAssignments(Integer studentId) {
        try {
            List<AssignmentSubmissionDTO> pendingAssignments = submissionMapper.getPendingAssignments(studentId);
            return Result.success(pendingAssignments);
        } catch (Exception e) {
            return Result.failed("获取待办作业失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户已提交的所有作业
     *
     * @param studentId 学生ID
     * @return 已提交的作业列表
     */
    public Result<List<AssignmentSubmissionDTO>> getCompletedAssignments(Integer studentId) {
        try {
            List<AssignmentSubmissionDTO> completedAssignments = submissionMapper.getSubmissionsByStudent(studentId);
            return Result.success(completedAssignments);
        } catch (Exception e) {
            return Result.failed("获取已完成作业失败: " + e.getMessage());
        }
    }

    /**
     * 删除作业提交
     *
     * @param submissionId 提交ID
     * @param studentId    学生ID
     * @return 操作结果
     */
    public Result<String> deleteSubmission(Integer submissionId, Integer studentId) {
        try {
            AssignmentSubmissionDTO submission = submissionMapper.getSubmissionById(submissionId);
            if (submission == null || !submission.getStudent_id().equals(studentId)) {
                return Result.failed("未找到对应的作业提交记录");
            }

            submissionMapper.deleteSubmission(submissionId, studentId);
            return Result.success("作业提交删除成功");
        } catch (Exception e) {
            return Result.failed("删除作业提交失败: " + e.getMessage());
        }
    }

    /**
     * 教师对作业进行评分
     *
     * @param submissionId 提交ID
     * @param grade        分数
     * @param feedback     教师反馈
     * @return 操作结果
     */
    public Result<String> gradeSubmission(Integer submissionId, Float grade, String feedback) {
        try {
            AssignmentSubmissionDTO submission = submissionMapper.getSubmissionById(submissionId);
            if (submission == null) {
                return Result.failed("未找到对应的作业提交记录");
            }

            submissionMapper.gradeSubmission(submissionId, grade, feedback);
            return Result.success("作业打分成功");
        } catch (Exception e) {
            return Result.failed("作业打分失败: " + e.getMessage());
        }
    }

    /**
     * 根据作业ID获取所有提交记录
     *
     * @param assignmentId 作业ID
     * @return 提交记录列表
     */
    public List<AssignmentSubmissionDTO> getSubmissionsByAssignmentId(Integer assignmentId) {
        return submissionMapper.getSubmissionsByAssignmentId(assignmentId);
    }

}
