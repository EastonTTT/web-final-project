package org.backend.finalProject.Service;

import org.backend.finalProject.Mapper.CourseRegistrationMapper;
import org.backend.finalProject.Pojo.CourseRegistrationDTO;
import org.backend.finalProject.Pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseRegistrationService {

    @Autowired
    private CourseRegistrationMapper registrationMapper;

    /**
     * 注册课程
     *
     * @param studentId 学生ID
     * @param courseId  课程ID
     * @return 操作结果
     */
    public Result<String> registerCourse(Integer studentId, Integer courseId) {
        try {
            // 检查是否已注册
            CourseRegistrationDTO existing = registrationMapper.getRegistration(studentId, courseId);
            if (existing != null) {
                return Result.failed("您已注册该课程");
            }

            CourseRegistrationDTO registration = new CourseRegistrationDTO();
            registration.setStudent_id(studentId);
            registration.setCourse_id(courseId);
            registrationMapper.insertRegistration(registration);

            return Result.success("课程注册成功");
        } catch (Exception e) {
            return Result.failed("课程注册失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户已注册的所有课程
     *
     * @param studentId 学生ID
     * @return 注册的课程列表
     */
    public Result<List<CourseRegistrationDTO>> getRegisteredCourses(Integer studentId) {
        try {
            List<CourseRegistrationDTO> registrations = registrationMapper.getRegistrationsByStudentId(studentId);
            return Result.success(registrations);
        } catch (Exception e) {
            return Result.failed("获取注册课程失败: " + e.getMessage());
        }
    }

    /**
     * 取消注册课程
     *
     * @param registrationId 注册记录ID
     * @param studentId      学生ID
     * @return 操作结果
     */
    public Result<String> cancelRegistration(Integer registrationId, Integer studentId) {
        try {
            // 检查是否存在对应的注册记录
            CourseRegistrationDTO registration = registrationMapper.getRegistration(studentId, null);
            if (registration == null || !registration.getRegistration_id().equals(registrationId)) {
                return Result.failed("未找到对应的注册记录");
            }

            registrationMapper.deleteRegistration(registrationId, studentId);
            return Result.success("课程取消注册成功");
        } catch (Exception e) {
            return Result.failed("取消注册失败: " + e.getMessage());
        }
    }
}
