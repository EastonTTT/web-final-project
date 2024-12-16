package org.backend.finalProject.Mapper;

import org.apache.ibatis.annotations.*;
import org.backend.finalProject.Pojo.NoteDTO;

import java.util.List;

@Mapper
public interface NoteMapper {

    // 插入笔记信息
    @Insert("INSERT INTO webfinal.notes (course_id, user_id, content, create_at) " +
            "VALUES (#{course_id}, #{user_id}, #{content}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "note_id")
    void insertNote(NoteDTO noteDTO);

    // 根据笔记ID获取笔记信息
    @Select("SELECT * FROM webfinal.notes WHERE note_id = #{note_id}")
    NoteDTO getNoteById(@Param("note_id") Integer noteId);

    // 根据课程ID获取所有笔记
    @Select("SELECT * FROM webfinal.notes WHERE course_id = #{course_id}")
    List<NoteDTO> getNotesByCourseId(@Param("course_id") Integer courseId);

    // 根据课程ID和用户ID获取该用户在该课程的所有笔记
    @Select("SELECT * FROM webfinal.notes WHERE course_id = #{course_id} AND user_id = #{user_id}")
    List<NoteDTO> getNotesByCourseIdAndUserId(@Param("course_id") Integer courseId, @Param("user_id") Integer userId);

    // 删除笔记
    @Delete("DELETE FROM webfinal.notes WHERE note_id = #{note_id}")
    void deleteNote(@Param("note_id") Integer noteId);

    // 更新笔记内容
    @Update("UPDATE webfinal.notes SET content = #{content} WHERE note_id = #{note_id}")
    void updateNoteContent(@Param("note_id") Integer noteId, @Param("content") String content);
}
