package org.backend.finalProject.Service;

import org.backend.finalProject.Pojo.CommentDTO;
import org.backend.finalProject.Mapper.CommentMapper;
import org.backend.finalProject.Pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    // 添加评论
    public void addComment(CommentDTO commentDTO) {
        commentMapper.insertComment(commentDTO);
    }

    // 删除评论（仅评论拥有者可删除）
    public Result<String> deleteComment(Integer commentId) {
        CommentDTO comment = commentMapper.getCommentById(commentId);
        if (comment == null) {
            return Result.failed("未找到指定评论");
        }
        commentMapper.deleteComment(commentId);
        return Result.success("评论删除成功");
    }

    // 根据课程ID获取所有评论
    public List<CommentDTO> getCommentsByCourseId(Integer courseId) {
        return commentMapper.getCommentsByCourseId(courseId);
    }

    // 根据课程ID和用户ID获取该用户在该课程的所有评论
    public List<CommentDTO> getCommentsByCourseIdAndUserId(Integer courseId, Integer userId) {
        return commentMapper.getCommentsByCourseIdAndUserId(courseId, userId);
    }
}
