package org.backend.finalProject.Controller;

import org.backend.finalProject.Pojo.NoteDTO;
import org.backend.finalProject.Pojo.Result;
import org.backend.finalProject.Service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    /**
     * 处理笔记操作
     * POST /notes/action?action=add
     * POST /notes/action?action=delete
     * POST /notes/action?action=update
     * 请求体包含相关字段
     */
    @PostMapping("/action")
    public Result<?> handleNoteAction(@RequestParam String action, @RequestBody NoteDTO noteDTO,
                                      @RequestParam(required = false) Integer userId,
                                      @RequestParam(required = false) String newContent) {
        try {
            switch (action) {
                case "add":
                    // 添加笔记时，create_at 由数据库自动生成
                    noteService.addNote(noteDTO);
                    return Result.success("笔记添加成功");

                case "delete":
                    // 删除笔记时，需要提供 note_id 和 user_id
                    if (noteDTO.getNote_id() == null || userId == null) {
                        return Result.failed("删除笔记时必须提供 note_id 和 user_id");
                    }
                    return noteService.deleteNote(noteDTO.getNote_id(), userId);

                case "update":
                    // 更新笔记内容时，需要提供 note_id, user_id 和 newContent
                    if (noteDTO.getNote_id() == null || userId == null || newContent == null) {
                        return Result.failed("更新笔记时必须提供 note_id, user_id 和 newContent");
                    }
                    return noteService.updateNoteContent(noteDTO.getNote_id(), userId, newContent);

                default:
                    return Result.failed("无效的操作类型");
            }
        } catch (Exception e) {
            return Result.failed("操作失败: " + e.getMessage());
        }
    }

    /**
     * 根据课程ID获取所有笔记
     * GET /notes/course/{courseId}
     */
    @GetMapping("/course/{courseId}")
    public Result<List<NoteDTO>> getNotesByCourseId(@PathVariable Integer courseId) {
        try {
            List<NoteDTO> notes = noteService.getNotesByCourseId(courseId);
            return Result.success(notes);
        } catch (Exception e) {
            return Result.failed("获取笔记列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据课程ID和用户ID获取该用户在该课程的所有笔记
     * GET /notes/course/{courseId}/user/{userId}
     */
    @GetMapping("/course/{courseId}/user/{userId}")
    public Result<List<NoteDTO>> getNotesByCourseIdAndUserId(
            @PathVariable Integer courseId,
            @PathVariable Integer userId) {
        try {
            List<NoteDTO> notes = noteService.getNotesByCourseIdAndUserId(courseId, userId);
            return Result.success(notes);
        } catch (Exception e) {
            return Result.failed("获取笔记失败: " + e.getMessage());
        }
    }
}
