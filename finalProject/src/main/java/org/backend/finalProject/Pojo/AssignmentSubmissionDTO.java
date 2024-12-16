package org.backend.finalProject.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmissionDTO {

    // 提交ID
    private Integer submission_id;

    // 作业ID
    private Integer assignment_id;

    // 提交作业的学生ID
    private Integer student_id;

    // 提交时间
    private LocalDateTime submit_time;

    // 文件或资源的URL
    private String content_url;

    // 文本内容(如代码、文字答案)
    private String text_content;

    // 分数
    private Float grade;

    // 教师反馈
    private String feedback;

    public Integer getSubmission_id() {
        return submission_id;
    }

    public void setSubmission_id(Integer submission_id) {
        this.submission_id = submission_id;
    }

    public Integer getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(Integer assignment_id) {
        this.assignment_id = assignment_id;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
    }

    public LocalDateTime getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(LocalDateTime submit_time) {
        this.submit_time = submit_time;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

    public String getText_content() {
        return text_content;
    }

    public void setText_content(String text_content) {
        this.text_content = text_content;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
