package org.backend.finalProject.Service;

import lombok.extern.slf4j.Slf4j;
import org.backend.finalProject.Pojo.AssignmentDTO;
import org.backend.finalProject.Mapper.AssignmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AssignmentService {

    @Autowired
    private AssignmentMapper assignmentMapper;

    /**
     * 添加作业
     *
     * @param assignmentDTO 作业信息
     */
    @Transactional
    public void addAssignment(AssignmentDTO assignmentDTO) {
        assignmentMapper.addAssignment(assignmentDTO);
    }

    /**
     * 更新作业
     *
     * @param assignmentDTO 作业信息
     */
    @Transactional
    public void updateAssignment(AssignmentDTO assignmentDTO) {
        // 检查作业是否存在
        AssignmentDTO existingAssignment = assignmentMapper.getAssignmentById(assignmentDTO.getAssignment_id());
        if (existingAssignment == null) {
            throw new RuntimeException("作业不存在");
        }

        assignmentMapper.updateAssignment(assignmentDTO);
    }

    /**
     * 删除作业
     *
     * @param assignment_id 作业ID
     */
    @Transactional
    public void deleteAssignment(Integer assignment_id) {
        // 检查作业是否存在
        AssignmentDTO existingAssignment = assignmentMapper.getAssignmentById(assignment_id);
        if (existingAssignment == null) {
            throw new RuntimeException("作业不存在");
        }

        assignmentMapper.deleteAssignment(assignment_id);
    }

    /**
     * 根据作业ID获取作业
     *
     * @param assignment_id 作业ID
     * @return 作业信息
     */
    public AssignmentDTO getAssignmentById(Integer assignment_id) {
        return assignmentMapper.getAssignmentById(assignment_id);
    }

    /**
     * 根据课程ID获取所有作业
     *
     * @param course_id 课程ID
     * @return 作业列表
     */
    public List<AssignmentDTO> getAssignmentsByCourseId(Integer course_id) {
        return assignmentMapper.getAssignmentsByCourseId(course_id);
    }
}
