<template>
  <div>
    <el-table :data="completedTasks" style="width: 100%">
      <el-table-column prop="taskId" label="任务ID" width="100"></el-table-column>
      <el-table-column prop="taskName" label="任务名称"></el-table-column>
      <el-table-column prop="courseName" label="所属课程"></el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template #default="scope">
          <el-tag :type="scope.row.status === '已完成' ? 'success' : 'info'">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="评测结果" width="200">
        <template #default="scope">
          <span>{{ scope.row.evaluation }}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="margin-top: 20px; text-align: center;"
      @current-change="handlePageChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="prev, pager, next"
      :total="totalCompleted">
    </el-pagination>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';

interface CompletedTask {
  taskId: number;
  taskName: string;
  courseName: string;
  status: string;
  evaluation: string;
}

export default defineComponent({
  name: 'CompletedTasks',
  setup() {
    const completedTasks = ref<CompletedTask[]>([]);
    const currentPage = ref(1);
    const pageSize = ref(10);
    const totalCompleted = ref(0);

    const fetchCompletedTasks = () => {
      // 模拟API请求，替换为实际API调用
      // 例如：axios.get('/api/tasks/completed', { params: { page: currentPage.value, size: pageSize.value } })
      completedTasks.value = [
        { taskId: 1, taskName: '提交作业', courseName: '数学', status: '已完成', evaluation: '优秀' },
        { taskId: 2, taskName: '完成练习', courseName: '物理', status: '已完成', evaluation: '良好' },
        // 更多已完成任务...
      ];
      totalCompleted.value = 2; // 实际应从API获取总数
    };

    const handlePageChange = (page: number) => {
      currentPage.value = page;
      fetchCompletedTasks();
    };

    onMounted(() => {
      fetchCompletedTasks();
    });

    return {
      completedTasks,
      currentPage,
      pageSize,
      totalCompleted,
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
