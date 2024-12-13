package org.backend.finalProject.Pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    // 课程ID
    private Integer course_id;

    // 授课教师ID
    private Integer teacher_id;

    // 课程名称
    private String course_name;

    // 课程简介
    private String course_description;

    // 课程图片的本地相对URL
    private String course_image;

    // 课程类型
    private String course_type;

    // 是否开启评论区
    private Boolean allow_comments;

    // 是否开启笔记区
    private Boolean allow_notes;

    // 课程状态（未开课、已开课、已结束）
    private String status;

    // 课程开始时间
    private LocalDateTime start_time;

    // 课程结束时间
    private LocalDateTime end_time;

    // 创建时间
    private LocalDateTime create_at;

    // 更新时间
    private LocalDateTime updated_at;

    public Integer getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }

    public Integer getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(Integer teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_description() {
        return course_description;
    }

    public void setCourse_description(String course_description) {
        this.course_description = course_description;
    }

    public String getCourse_image() {
        return course_image;
    }

    public void setCourse_image(String course_image) {
        this.course_image = course_image;
    }

    public String getCourse_type() {
        return course_type;
    }

    public void setCourse_type(String course_type) {
        this.course_type = course_type;
    }

    public Boolean getAllow_comments() {
        return allow_comments;
    }

    public void setAllow_comments(Boolean allow_comments) {
        this.allow_comments = allow_comments;
    }

    public Boolean getAllow_notes() {
        return allow_notes;
    }

    public void setAllow_notes(Boolean allow_notes) {
        this.allow_notes = allow_notes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalDateTime start_time) {
        this.start_time = start_time;
    }

    public LocalDateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalDateTime end_time) {
        this.end_time = end_time;
    }

    public LocalDateTime getCreate_at() {
        return create_at;
    }

    public void setCreate_at(LocalDateTime create_at) {
        this.create_at = create_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }
}
