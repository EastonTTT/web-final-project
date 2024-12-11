<template>
  <div>
    <h2>课程管理</h2>
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="课件管理" name="courseware">
        <CourseManagement />
      </el-tab-pane>
      <el-tab-pane label="课程发布" name="coursePublishing">
        <CoursePublishing />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import CourseManagement from './CoursewareManagement.vue';
import CoursePublishing from './CoursePublishing.vue';

export default defineComponent({
  name: 'TeacherDashboard',
  components: {
    CourseManagement,
    CoursePublishing,
  },
  setup() {
    const activeTab = ref('courseware');

    const handleTabClick = (tab: any) => {
      // 处理标签点击事件（如果需要）
    };

    return {
      activeTab,
      handleTabClick,
    };
  },
});
</script>

<style scoped>
/* 添加一些样式 */
</style>
