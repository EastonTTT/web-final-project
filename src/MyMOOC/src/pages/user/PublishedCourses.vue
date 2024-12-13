<!-- 已发布课程 -->

<template>
  <div>
    <h3>已发布课程</h3>
    <el-table :data="publishedCourses" style="width: 100%">
      <el-table-column prop="name" label="课程名称" width="180"></el-table-column>
      <el-table-column prop="description" label="课程简介"></el-table-column>
      <el-table-column label="操作" width="300">
        <template #default="scope">
          <el-button type="primary" size="small" @click="editCourse(scope.row)">编辑</el-button>
          <el-button type="warning" size="small" @click="withdrawCourse(scope.row)">撤回发布</el-button>
          <el-button type="danger" size="small" @click="deleteCourse(scope.row)">删除</el-button>
          <el-button type="info" size="small" @click="viewCourse(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑课程弹窗 -->
    <el-dialog :visible.sync="editDialogVisible" title="编辑课程">
      <el-form :model="currentCourse" label-width="120px" v-if="currentCourse">
        <el-form-item label="课程名称" required>
          <el-input v-model="currentCourse.name"></el-input>
        </el-form-item>
        <el-form-item label="课程简介" required>
          <el-input type="textarea" v-model="currentCourse.description"></el-input>
        </el-form-item>
        <el-form-item label="是否开启评论区">
          <el-switch v-model="currentCourse.commentsEnabled" active-text="开启" inactive-text="关闭"></el-switch>
        </el-form-item>
        <el-form-item label="是否开启笔记区">
          <el-switch v-model="currentCourse.notesEnabled" active-text="开启" inactive-text="关闭"></el-switch>
        </el-form-item>
        <!-- 其他需要编辑的字段 -->
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </span>
    </el-dialog>

    <!-- 查看课程弹窗 -->
    <el-dialog :visible.sync="viewDialogVisible" title="查看课程">
      <div v-if="viewCourseData">
        <p><strong>课程名称：</strong>{{ viewCourseData.name }}</p>
        <p><strong>课程简介：</strong>{{ viewCourseData.description }}</p>
        <p><strong>是否开启评论区：</strong>{{ viewCourseData.commentsEnabled ? '开启' : '关闭' }}</p>
        <p><strong>是否开启笔记区：</strong>{{ viewCourseData.notesEnabled ? '开启' : '关闭' }}</p>
        <!-- 其他课程详情 -->
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import {
  ElTable,
  ElTableColumn,
  ElButton,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElSwitch,
  ElMessage,
  ElMessageBox,
} from 'element-plus';

export interface Course {
  id: number;
  name: string;
  description: string;
  commentsEnabled: boolean;
  notesEnabled: boolean;
}

export default defineComponent({
  name: 'PublishedCourses',
  components: {
    ElTable,
    ElTableColumn,
    ElButton,
    ElDialog,
    ElForm,
    ElFormItem,
    ElInput,
    ElSwitch,
  },
  setup() {
    // 模拟已发布课程数据
    const publishedCourses = ref<Course[]>([
      {
        id: 1,
        name: '高等数学',
        description: '介绍高等数学的基本概念和方法。',
        commentsEnabled: true,
        notesEnabled: true,
      },
      {
        id: 2,
        name: '物理学基础',
        description: '涵盖力学、电磁学等基础物理知识。',
        commentsEnabled: false,
        notesEnabled: true,
      },
      // 更多课程
    ]);

    // 编辑相关
    const editDialogVisible = ref(false);
    const currentCourse = ref<Course | null>(null);

    const editCourse = (course: Course) => {
      currentCourse.value = { ...course }; // 深拷贝
      editDialogVisible.value = true;
    };

    const saveEdit = () => {
      if (
        !currentCourse.value ||
        !currentCourse.value.name ||
        !currentCourse.value.description
      ) {
        ElMessage.warning('请填写完整的课程信息');
        return;
      }
      const index = publishedCourses.value.findIndex(c => c.id === currentCourse.value!.id);
      if (index !== -1) {
        publishedCourses.value[index] = { ...currentCourse.value };
        ElMessage.success('课程信息已更新');
      }
      editDialogVisible.value = false;
    };

    // 撤回发布
    const withdrawCourse = (course: Course) => {
      ElMessageBox.confirm(
        '确定要撤回发布此课程吗？',
        '撤回发布',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
        .then(() => {
          // 模拟撤回发布
          publishedCourses.value = publishedCourses.value.filter(c => c.id !== course.id);
          ElMessage.success('课程已撤回发布');
        })
        .catch(() => {
          ElMessage.info('已取消撤回发布');
        });
    };

    // 删除课程
    const deleteCourse = (course: Course) => {
      ElMessageBox.confirm(
        '确定要删除此课程吗？此操作无法撤销。',
        '删除课程',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error',
        }
      )
        .then(() => {
          // 模拟删除
          publishedCourses.value = publishedCourses.value.filter(c => c.id !== course.id);
          ElMessage.success('课程已删除');
        })
        .catch(() => {
          ElMessage.info('已取消删除');
        });
    };

    // 查看课程
    const viewDialogVisible = ref(false);
    const viewCourseData = ref<Course | null>(null);

    const viewCourse = (course: Course) => {
      viewCourseData.value = { ...course };
      viewDialogVisible.value = true;
    };

    return {
      publishedCourses,
      editDialogVisible,
      currentCourse,
      editCourse,
      saveEdit,
      withdrawCourse,
      deleteCourse,
      viewDialogVisible,
      viewCourseData,
      viewCourse,
    };
  },
});
</script>

<style scoped>
/* 表格样式调整 */
.el-table {
  margin-top: 20px;
}

/* 对话框内容样式 */
.dialog-footer {
  text-align: right;
}
</style>
