package org.backend.finalProject.Mapper;

import org.apache.ibatis.annotations.*;
import org.backend.finalProject.Pojo.CommentDTO;

import java.util.List;

@Mapper
public interface CommentMapper {

    // 插入评论信息
    @Insert("INSERT INTO webfinal.comments (course_id, user_id, content, create_at) " +
            "VALUES (#{course_id}, #{user_id}, #{content}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "comment_id")
    void insertComment(CommentDTO commentDTO);

    // 根据评论ID获取评论信息
    @Select("SELECT * FROM webfinal.comments WHERE comment_id = #{comment_id}")
    CommentDTO getCommentById(@Param("comment_id") Integer commentId);

    // 根据课程ID获取所有评论
    @Select("SELECT * FROM webfinal.comments WHERE course_id = #{course_id}")
    List<CommentDTO> getCommentsByCourseId(@Param("course_id") Integer courseId);

    // 根据课程ID和用户ID获取该用户在该课程的所有评论
    @Select("SELECT * FROM webfinal.comments WHERE course_id = #{course_id} AND user_id = #{user_id}")
    List<CommentDTO> getCommentsByCourseIdAndUserId(@Param("course_id") Integer courseId, @Param("user_id") Integer userId);

    /**
     * 根据用户ID获取该用户的所有评论
     */
    @Select("SELECT * FROM webfinal.comments WHERE user_id = #{userId}")
    List<CommentDTO> selectCommentsByUserId(Integer userId);

    // 删除评论
    @Delete("DELETE FROM webfinal.comments WHERE comment_id = #{comment_id}")
    void deleteComment(@Param("comment_id") Integer commentId);
}
