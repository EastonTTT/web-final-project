package org.backend.finalProject.Service;

import org.backend.finalProject.Mapper.NoteMapper;
import org.backend.finalProject.Pojo.NoteDTO;
import org.backend.finalProject.Pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    // 添加笔记
    public void addNote(NoteDTO noteDTO) {
        noteMapper.insertNote(noteDTO);
    }

    // 删除笔记（仅笔记拥有者可删除）
    public Result<String> deleteNote(Integer noteId, Integer userId) {
        NoteDTO note = noteMapper.getNoteById(noteId);
        if (note == null) {
            return Result.failed("未找到指定笔记");
        }
        if (!note.getUser_id().equals(userId)) {
            return Result.failed("无权限删除此笔记");
        }
        noteMapper.deleteNote(noteId);
        return Result.success("笔记删除成功");
    }

    // 更新笔记内容（仅笔记拥有者可更新）
    public Result<String> updateNoteContent(Integer noteId, Integer userId, String newContent) {
        NoteDTO note = noteMapper.getNoteById(noteId);
        if (note == null) {
            return Result.failed("未找到指定笔记");
        }
        if (!note.getUser_id().equals(userId)) {
            return Result.failed("无权限更新此笔记");
        }
        noteMapper.updateNoteContent(noteId, newContent);
        return Result.success("笔记更新成功");
    }

    // 根据课程ID获取所有笔记
    public List<NoteDTO> getNotesByCourseId(Integer courseId) {
        return noteMapper.getNotesByCourseId(courseId);
    }

    // 根据课程ID和用户ID获取该用户在该课程的所有笔记
    public List<NoteDTO> getNotesByCourseIdAndUserId(Integer courseId, Integer userId) {
        return noteMapper.getNotesByCourseIdAndUserId(courseId, userId);
    }
}
