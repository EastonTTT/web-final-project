<template>
  <div>
    <el-table :data="toDoItems" style="width: 100%">
      <el-table-column prop="taskId" label="任务ID" width="100"></el-table-column>
      <el-table-column prop="taskName" label="任务名称"></el-table-column>
      <el-table-column prop="courseName" label="所属课程"></el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button type="primary" size="mini" @click="submitTask(scope.row)">提交</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="margin-top: 20px; text-align: center;"
      @current-change="handlePageChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="prev, pager, next"
      :total="totalToDo">
    </el-pagination>

    <!-- 提交任务对话框 -->
    <el-dialog :visible.sync="submitDialogVisible" title="提交任务">
      <el-form :model="submitForm" label-width="80px">
        <el-form-item label="内容">
          <el-input type="textarea" v-model="submitForm.content" placeholder="请输入作业内容"></el-input>
        </el-form-item>
        <el-form-item label="上传文件">
          <el-upload
            class="upload-demo"
            action="/upload" <!-- 替换为实际上传接口 -->
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
            multiple>
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">支持多文件上传，格式为图片、代码等</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="submitDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSubmit">提交</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import type { UploadFile } from 'element-plus';

interface ToDoItem {
  taskId: number;
  taskName: string;
  courseName: string;
}

export default defineComponent({
  name: 'ToDoList',
  setup() {
    const toDoItems = ref<ToDoItem[]>([]);
    const currentPage = ref<number>(1);
    const pageSize = ref<number>(10);
    const totalToDo = ref<number>(0);

    const submitDialogVisible = ref<boolean>(false);
    const currentTask = ref<ToDoItem | null>(null); // 修正类型定义
    const submitForm = ref<{
      content: string;
      files: File[];
    }>({
      content: '',
      files: [],
    });

    const fetchToDoItems = async () => {
      try {
        // 模拟API请求，替换为实际API调用
        // 例如：const response = await axios.get('/api/tasks/todo', { params: { page: currentPage.value, size: pageSize.value } });
        toDoItems.value = [
          { taskId: 1, taskName: '提交作业', courseName: '数学' },
          { taskId: 2, taskName: '完成练习', courseName: '物理' },
          // 更多待办任务...
        ];
        totalToDo.value = 2; // 实际应从API获取总数
      } catch (error) {
        ElMessage.error('获取待办事项失败');
      }
    };

    const submitTask = (task: ToDoItem) => {
      currentTask.value = task; // 使用 .value 赋值
      submitForm.value.content = '';
      submitForm.value.files = [];
      submitDialogVisible.value = true;
    };

    const handleUploadSuccess = (response: any, file: UploadFile, fileList: UploadFile[]) => {
      if (file.raw instanceof File) {
        submitForm.value.files.push(file.raw);
        ElMessage.success(`${file.name} 上传成功`);
      }
    };

    const beforeUpload = (file: File) => {
      const isAllowedType = ['image/jpeg', 'image/png', 'application/pdf', 'text/plain', 'video/mp4'].includes(file.type);
      if (!isAllowedType) {
        ElMessage.error('不支持的文件类型');
      }
      return isAllowedType;
    };

    const confirmSubmit = async () => {
      if (!currentTask.value) {
        ElMessage.error('当前任务为空');
        return;
      }
      try {
        // 模拟提交作业的API请求，替换为实际API调用
        // 例如：await axios.post('/api/tasks/submit', { taskId: currentTask.value.taskId, content: submitForm.value.content, files: submitForm.value.files });
        ElMessage.success(`任务 "${currentTask.value.taskName}" 提交成功`);
        submitDialogVisible.value = false;
        // 刷新待办事项列表
        fetchToDoItems();
      } catch (error) {
        ElMessage.error('提交任务失败');
      }
    };

    const handlePageChange = (page: number) => {
      currentPage.value = page;
      fetchToDoItems();
    };

    onMounted(() => {
      fetchToDoItems();
    });

    return {
      toDoItems,
      currentPage,
      pageSize,
      totalToDo,
      submitDialogVisible,
      submitForm,
      submitTask,
      handleUploadSuccess,
      beforeUpload,
      confirmSubmit,
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
