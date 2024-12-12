<template>
  <div>
    <el-table :data="registeredCourses" style="width: 100%">
      <el-table-column prop="courseId" label="课程ID" width="100"></el-table-column>
      <el-table-column prop="courseName" label="课程名称"></el-table-column>
      <el-table-column label="学习任务" width="200">
        <template #default="scope">
          <el-button type="text" @click="viewTasks(scope.row)">查看任务</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="margin-top: 20px; text-align: center;"
      @current-change="handlePageChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="prev, pager, next"
      :total="totalCourses">
    </el-pagination>

    <!-- 弹出框显示学习任务 -->
    <el-dialog :visible.sync="dialogVisible" title="学习任务">
      <el-list>
        <el-list-item v-for="task in currentTasks" :key="task.id">
          <el-checkbox v-model="task.completed" @change="toggleTaskCompletion(task)">
            {{ task.name }}
          </el-checkbox>
        </el-list-item>
      </el-list>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';

interface Course {
  courseId: number;
  courseName: string;
}

interface Task {
  id: number;
  name: string;
  completed: boolean;
}

export default defineComponent({
  name: 'MyLearning',
  setup() {
    const registeredCourses = ref<Course[]>([]);
    const currentPage = ref(1);
    const pageSize = ref(10);
    const totalCourses = ref(0);

    const dialogVisible = ref(false);
    const currentTasks = ref<Task[]>([]);

    const fetchRegisteredCourses = () => {
      // 模拟API请求，替换为实际API调用
      // 例如：axios.get('/api/courses/registered', { params: { page: currentPage.value, size: pageSize.value } })
      registeredCourses.value = [
        { courseId: 1, courseName: '数学' },
        { courseId: 2, courseName: '物理' },
        // 更多已注册课程...
      ];
      totalCourses.value = 2; // 实际应从API获取总数
    };

    const viewTasks = (course: Course) => {
      // 模拟获取课程任务的API请求
      // 例如：axios.get(`/api/courses/${course.courseId}/tasks`)
      currentTasks.value = [
        { id: 1, name: '查看文档课件', completed: false },
        { id: 2, name: '观看音视频课件', completed: false },
        { id: 3, name: '提交作业', completed: false },
        // 更多任务...
      ];
      dialogVisible.value = true;
    };

    const toggleTaskCompletion = (task: Task) => {
      task.completed = !task.completed;
      // 可以在这里发送任务完成状态到服务器
      // 例如：axios.post(`/api/tasks/${task.id}/toggle`, { completed: task.completed })
      ElMessage.success(`任务 "${task.name}" 已标记为 ${task.completed ? '完成' : '未完成'}`);
    };

    const handlePageChange = (page: number) => {
      currentPage.value = page;
      fetchRegisteredCourses();
    };

    onMounted(() => {
      fetchRegisteredCourses();
    });

    return {
      registeredCourses,
      currentPage,
      pageSize,
      totalCourses,
      dialogVisible,
      currentTasks,
      viewTasks,
      toggleTaskCompletion,
      handlePageChange,
    };
  },
});
</script>

<style scoped>
/* 表格样式 */
.el-table {
  margin-bottom: 20px;
}
</style>
