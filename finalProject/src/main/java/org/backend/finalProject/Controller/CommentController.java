package org.backend.finalProject.Controller;

import org.backend.finalProject.Pojo.CommentDTO;
import org.backend.finalProject.Pojo.Result;
import org.backend.finalProject.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 添加评论
     * POST /comments/action?action=add
     * 请求体包含 course_id, user_id, content
     */
    @PostMapping("/action")
    public Result<?> handleCommentAction(@RequestParam String action, @RequestBody CommentDTO commentDTO) {
        try {
            switch (action) {
                case "add":
                    // 添加评论时，create_at 由数据库自动生成
                    commentService.addComment(commentDTO);
                    return Result.success("评论添加成功");

                case "delete":
                    // 删除评论时，需要提供 comment_id 和 user_id
                    if (commentDTO.getComment_id() == null) {
                        return Result.failed("删除评论时必须提供评论");
                    }
                    return commentService.deleteComment(commentDTO.getComment_id());

                default:
                    return Result.failed("无效的操作类型");
            }
        } catch (Exception e) {
            return Result.failed("操作失败: " + e.getMessage());
        }
    }

    /**
     * 根据课程ID获取所有评论
     * GET /comments/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public Result<List<CommentDTO>> getCommentsByCourseId(@PathVariable Integer courseId) {
        try {
            List<CommentDTO> comments = commentService.getCommentsByCourseId(courseId);
            return Result.success(comments);
        } catch (Exception e) {
            return Result.failed("获取评论列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据课程ID和用户ID获取该用户在该课程的所有评论
     * GET /comments/course/{courseId}/user/{userId}
     */
    @GetMapping("/course/{courseId}/user/{userId}")
    public Result<List<CommentDTO>> getCommentsByCourseIdAndUserId(
            @PathVariable Integer courseId,
            @PathVariable Integer userId) {
        try {
            List<CommentDTO> comments = commentService.getCommentsByCourseIdAndUserId(courseId, userId);
            return Result.success(comments);
        } catch (Exception e) {
            return Result.failed("获取评论失败: " + e.getMessage());
        }
    }
}
