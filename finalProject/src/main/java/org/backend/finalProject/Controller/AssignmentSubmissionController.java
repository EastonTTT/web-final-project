package org.backend.finalProject.Controller;

import org.backend.finalProject.Pojo.AssignmentSubmissionDTO;
import org.backend.finalProject.Pojo.Result;
import org.backend.finalProject.Service.AssignmentSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignment-submissions")
public class AssignmentSubmissionController {

    @Autowired
    private AssignmentSubmissionService submissionService;

    /**
     * 提交作业
     * POST /assignment-submissions/submit
     * 请求体包含 assignment_id, student_id, content_url, text_content
     */
    @PostMapping("/submit")
    public Result<?> submitAssignment(@RequestBody AssignmentSubmissionDTO submission) {
        return submissionService.submitAssignment(submission);
    }

    /**
     * 更新已提交的作业
     * POST /assignment-submissions/update
     * 请求体包含 submission_id, student_id, assignment_id, content_url, text_content
     */
    @PostMapping("/update")
    public Result<?> updateAssignmentSubmission(@RequestBody AssignmentSubmissionDTO submission) {
        return submissionService.updateAssignmentSubmission(submission);
    }

    /**
     * 获取用户的待办作业
     * GET /assignment-submissions/todo?student_id=xxx
     */
    @GetMapping("/todo")
    public Result<?> getTodoAssignments(@RequestParam Integer student_id) {
        return submissionService.getTodoAssignments(student_id);
    }

    /**
     * 获取用户已完成的作业
     * GET /assignment-submissions/completed?student_id=xxx
     */
    @GetMapping("/completed")
    public Result<?> getCompletedAssignments(@RequestParam Integer student_id) {
        return submissionService.getCompletedAssignments(student_id);
    }

    /**
     * 删除作业提交
     * DELETE /assignment-submissions/delete?submission_id=xxx&student_id=yyy
     */
    @DeleteMapping("/delete")
    public Result<?> deleteSubmission(@RequestParam Integer submission_id, @RequestParam Integer student_id) {
        return submissionService.deleteSubmission(submission_id, student_id);
    }

    /**
     * 教师打分
     * POST /assignment-submissions/grade
     * 请求参数：submission_id, grade, feedback
     */
    @PostMapping("/grade")
    public Result<?> gradeSubmission(@RequestParam Integer submission_id, @RequestParam Float grade, @RequestParam String feedback) {
        return submissionService.gradeSubmission(submission_id, grade, feedback);

    }

    /**
     * 获取特定作业的所有提交记录
     * GET /assignment-submissions/assignment/{assignmentId}
     */
    @GetMapping("/assignment/{assignmentId}")
    public Result<List<AssignmentSubmissionDTO>> getSubmissionsByAssignmentId(@PathVariable Integer assignmentId) {
        try {
            List<AssignmentSubmissionDTO> submissions = submissionService.getSubmissionsByAssignmentId(assignmentId);
            return Result.success(submissions);
        } catch (Exception e) {
            return Result.failed("获取作业提交记录失败: " + e.getMessage());
        }
    }
}
