<!-- AssignmentsList.vue -->
<template>
  <div class="assignments-list-container">
    <h2 class="title">作业管理</h2>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">加载中...</div>

    <!-- 作业列表 -->
    <div v-else>
      <div v-if="assignments.length === 0" class="no-assignments">
        当前没有作业。
      </div>

      <div v-else class="assignments-list">
        <div v-for="assignment in assignments" :key="assignment.assignment_id" class="assignment-item">
          <h3>{{ assignment.title }}</h3>
          <p>{{ assignment.description }}</p>
          <p><strong>课程名称:</strong> {{ getCourseName(assignment.course_id) }}</p>
          <p><strong>截止日期:</strong> {{ formatDate(assignment.due_date) }}</p>

          <!-- 作业操作按钮 -->
          <div class="buttons">
            <button class="btn modify-btn" @click="openModifyAssignmentModal(assignment)">修改作业</button>
            <button class="btn delete-btn" @click="deleteAssignment(assignment)">删除作业</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 修改作业模态窗口 -->
    <div v-if="isModifyAssignmentModalVisible" class="modal-overlay" @click.self="closeModifyAssignmentModal">
      <div class="modal-content">
        <h2>修改作业 - {{ currentAssignment.title }}</h2>
        <form @submit.prevent="submitAssignmentModification">
          <div class="form-group">
            <label>作业标题 <span class="required">*</span>：</label>
            <input v-model="currentAssignment.title" type="text" required />
          </div>
          <div class="form-group">
            <label>作业描述：</label>
            <textarea v-model="currentAssignment.description"></textarea>
          </div>
          <div class="form-group">
            <label>截止日期 <span class="required">*</span>：</label>
            <input type="datetime-local" v-model="currentAssignment.due_date" required/>
          </div>
          <div class="form-actions">
            <button type="button" class="btn cancel-btn" @click="closeModifyAssignmentModal">取消</button>
            <button type="submit" class="btn submit-btn">保存修改</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import axiosInstance from '@/utils/request/Axios.ts';
import { getLoginRecord } from '../homePage/login/LoginRecord'; // 确保路径正确

interface Assignment {
  assignment_id: number;
  course_id: number;
  title: string;
  description: string;
  due_date: string; // 使用字符串存储日期时间
  create_at: string;
  updated_at: string;
}

interface Course {
  course_id: number;
  course_name: string;
  // 其他课程相关字段...
}
// export {fetchCoursesAndAssignments}
export default defineComponent({
  name: 'AssignmentsList',
  setup() {
    const assignments = ref<Assignment[]>([]);
    const courses = ref<Course[]>([]);
    const loading = ref<boolean>(true);

    // 获取登录记录
    const loginRecord = getLoginRecord();

    // 模态窗口的显示状态
    const isModifyAssignmentModalVisible = ref<boolean>(false);

    // 当前修改的作业
    const currentAssignment = ref<Assignment>({
      assignment_id: 0,
      course_id: 0,
      title: '',
      description: '',
      due_date: '',
      create_at: '',
      updated_at: ''
    });

    // 根据老师id获取该老师发布的课程及其作业
    const fetchCoursesAndAssignments = async () => {
      loading.value = true;
      try {
        const teacherID = loginRecord.user_id;
        const response = await axiosInstance.get(`/courses/teacher/${teacherID}`);
        if (response.status === 200 && response.data.status === 200) {
          courses.value = response.data.data;
          assignments.value = []; // 清空之前的作业列表
          // 为每个课程获取其作业列表
          for (let course of courses.value) {
            const assignmentResponse = await axiosInstance.get(`/assignments/course/${course.course_id}`);
            if (assignmentResponse.status === 200 && assignmentResponse.data.status === 200) {
              assignments.value.push(...assignmentResponse.data.data);
            }
          }
        } else {
          alert('获取课程列表失败: ' + response.data.message);
        }
      } catch (error) {
        console.error(error);
        alert('获取课程列表时发生错误');
      } finally {
        loading.value = false;
      }
    };

    // 格式化日期
    const formatDate = (datetime: string) => {
      const date = new Date(datetime);
      return date.toLocaleString();
    };

    // 获取课程名称
    const getCourseName = (course_id: number) => {
      const course = courses.value.find(c => c.course_id === course_id);
      return course ? course.course_name : '未知课程';
    };

    // 打开修改作业模态窗口
    const openModifyAssignmentModal = (assignment: Assignment) => {
      currentAssignment.value = { ...assignment };
      isModifyAssignmentModalVisible.value = true;
    };

    // 关闭修改作业模态窗口
    const closeModifyAssignmentModal = () => {
      isModifyAssignmentModalVisible.value = false;
    };

    // 提交作业修改
    const submitAssignmentModification = async () => {
      if (!currentAssignment.value.title || !currentAssignment.value.due_date) {
        alert('请填写作业标题和截止日期');
        return;
      }
      try {
        const assignmentData = {
          action: 'update',
          assignment_id: currentAssignment.value.assignment_id,
          course_id: currentAssignment.value.course_id,
          title: currentAssignment.value.title,
          description: currentAssignment.value.description,
          due_date: new Date(currentAssignment.value.due_date)
        };
        const response = await axiosInstance.myPosting('/assignments/action?action=update', assignmentData);
        if (response.status === 200 && response.data.status === 200) {
          alert(response.data.message);
          closeModifyAssignmentModal();
          await fetchCoursesAndAssignments();
        } else {
          alert('修改作业失败: ' + response.data.message);
        }
      } catch (error) {
        console.error(error);
        alert('修改作业时发生错误');
      }
    };

    // 删除作业
    const deleteAssignment = async (assignment: Assignment) => {
      if (!confirm(`确定要删除作业 "${assignment.title}" 吗？`)) {
        return;
      }
      try {
        const response = await axiosInstance.myPosting('/assignments/action?action=delete', { assignment_id: assignment.assignment_id });
        if (response.status === 200 && response.data.status === 200) {
          alert(response.data.message);
          await fetchCoursesAndAssignments();
        } else {
          alert('删除作业失败: ' + response.data.message);
        }
      } catch (error) {
        console.error(error);
        alert('删除作业时发生错误');
      }
    };

    // 在组件挂载时获取数据
    onMounted(async () => {
      await fetchCoursesAndAssignments();
    });

    return {
      assignments,
      courses,
      loading,
      isModifyAssignmentModalVisible,
      currentAssignment,
      formatDate,
      getCourseName,
      openModifyAssignmentModal,
      closeModifyAssignmentModal,
      submitAssignmentModification,
      deleteAssignment
    };
  }
});
</script>

<style scoped>
.assignments-list-container {
  padding: 20px;
  font-family: "Microsoft Yahei", sans-serif;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.title {
  text-align: center;
  margin-bottom: 20px;
  color: #2c3e50;
}

.loading,
.no-assignments {
  text-align: center;
  color: #666;
  font-size: 18px;
}

.assignments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.assignment-item {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.assignment-item h3 {
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.assignment-item p {
  margin: 5px 0;
  color: #555;
}

.buttons {
  margin-top: 15px;
  display: flex;
  gap: 10px;
}

.btn {
  padding: 8px 14px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  color: #fff;
  font-size: 14px;
  transition: background-color 0.3s;
}

.modify-btn {
  background-color: #3498db;
}

.modify-btn:hover {
  background-color: #2980b9;
}

.delete-btn {
  background-color: #e74c3c;
}

.delete-btn:hover {
  background-color: #c0392b;
}

/* 修改作业模态窗口样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.modal-content h2 {
  margin-bottom: 25px;
  text-align: center;
  color: #333;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}

.required {
  color: red;
  margin-left: 5px;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  border-color: #3498db;
  outline: none;
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

.cancel-btn {
  background-color: #95a5a6;
}

.cancel-btn:hover {
  background-color: #7f8c8d;
}

.submit-btn {
  background-color: #27ae60;
}

.submit-btn:hover {
  background-color: #229954;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .assignments-list-container {
    padding: 15px;
  }

  .assignment-item {
    padding: 15px;
  }

  .modal-content {
    padding: 20px;
  }

  .form-actions {
    flex-direction: column;
    align-items: stretch;
  }

  .form-actions button {
    width: 100%;
  }
}
</style>
