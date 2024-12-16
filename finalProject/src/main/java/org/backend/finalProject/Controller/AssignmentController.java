package org.backend.finalProject.Controller;

import org.backend.finalProject.Pojo.AssignmentDTO;
import org.backend.finalProject.Pojo.Result;
import org.backend.finalProject.Service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Assignment Controller
 *
 * 处理与作业相关的 HTTP 请求，如创建、更新、删除作业以及获取作业列表。
 */
@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    /**
     * 创建、更新、删除作业的通用接口
     *
     * 前端通过 action 参数区分操作类型：
     * - POST /assignments/action?action=add    （请求体中包含作业信息，新增作业）
     * - POST /assignments/action?action=update （请求体中包含 assignment_id 及修改信息，更新作业）
     * - POST /assignments/action?action=delete （请求体中包含 assignment_id，删除作业）
     *
     * @param action 操作类型（add, update, delete）
     * @param assignmentDTO 作业信息
     * @return 操作结果
     */
    @PostMapping("/action")
    public Result<?> handleAssignmentAction(@RequestParam String action, @RequestBody AssignmentDTO assignmentDTO) {
        try {
            switch (action) {
                case "add":
                    assignmentService.addAssignment(assignmentDTO);
                    return Result.success("作业布置成功");

                case "update":
                    // 更新作业需要 assignment_id 为必传值
                    if (assignmentDTO.getAssignment_id() == null) {
                        return Result.failed("更新作业时必须提供作业ID");
                    }
                    assignmentService.updateAssignment(assignmentDTO);
                    return Result.success("作业更新成功");

                case "delete":
                    // 删除作业同样需要 assignment_id
                    if (assignmentDTO.getAssignment_id() == null) {
                        return Result.failed("删除作业时必须提供作业ID");
                    }
                    assignmentService.deleteAssignment(assignmentDTO.getAssignment_id());
                    return Result.success("作业删除成功");

                default:
                    return Result.failed("无效的操作类型");
            }
        } catch (RuntimeException e) {
            return Result.failed(e.getMessage());
        } catch (Exception e) {
            return Result.failed("操作失败: " + e.getMessage());
        }
    }

    /**
     * 获取某课程的所有作业
     *
     * 前端可通过 GET 请求：GET /assignments/course/{courseId}
     *
     * @param courseId 课程ID
     * @return 作业列表
     */
    @GetMapping("/course/{courseId}")
    public Result<List<AssignmentDTO>> getAssignmentsByCourseId(@PathVariable Integer courseId) {
        try {
            List<AssignmentDTO> assignments = assignmentService.getAssignmentsByCourseId(courseId);
            return Result.success(assignments);
        } catch (RuntimeException e) {
            return Result.failed(e.getMessage());
        } catch (Exception e) {
            return Result.failed("获取作业列表失败: " + e.getMessage());
        }
    }
}
