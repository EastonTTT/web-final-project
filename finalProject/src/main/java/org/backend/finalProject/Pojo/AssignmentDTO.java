package org.backend.finalProject.Pojo;

import java.util.Date;

/**
 * Assignment Data Transfer Object
 *
 * 用于在前后端之间传输作业相关的数据。
 */
public class AssignmentDTO {
    /**
     * 作业ID
     */
    private Integer assignment_id;

    /**
     * 所属课程ID
     */
    private Integer course_id;

    /**
     * 作业标题
     */
    private String title;

    /**
     * 作业描述
     */
    private String description;

    /**
     * 截止日期
     */
    private Date due_date;

    /**
     * 创建时间
     */
    private Date create_at;

    /**
     * 更新时间
     */
    private Date updated_at;

    // Getters and Setters

    public Integer getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(Integer assignment_id) {
        this.assignment_id = assignment_id;
    }

    public Integer getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Integer course_id) {
        this.course_id = course_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDue_date() {
        return due_date;
    }

    public void setDue_date(Date due_date) {
        this.due_date = due_date;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "AssignmentDTO{" +
                "assignment_id=" + assignment_id +
                ", course_id=" + course_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", due_date=" + due_date +
                ", create_at=" + create_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
