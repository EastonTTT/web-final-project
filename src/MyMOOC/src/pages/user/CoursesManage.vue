<template>
  <div>
    <h2>课程管理</h2>
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="课程发布" name="coursePublishing">
        <CoursePublishing />
      </el-tab-pane>
      <el-tab-pane label="已发布课程" name="publishedCourses">
        <PublishedCourses />
      </el-tab-pane>
      <el-tab-pane label="课件管理" name="courseware">
        <CourseManagement />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import CourseManagement from './CoursewareManagement.vue';
import CoursePublishing from './CoursePublishing.vue';
import PublishedCourses from './PublishedCourses.vue';

export default defineComponent({
  name: 'CourcesManage',
  components: {
    PublishedCourses,
    CourseManagement,
    CoursePublishing,
  },
  setup() {
    const activeTab = ref('coursePublishing');

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
/* 容器样式 */
div {
  padding: 20px;
  background-color: #f9f9f9;
}

/* 标题样式 */
h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
}

/* 标签页样式 */
.el-tabs__header {
  background-color: #fff;
  border-bottom: 1px solid #ebeef5;
}

.el-tab-pane {
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ebeef5;
  border-top: none;
  border-radius: 0 0 4px 4px;
}

/* 活动标签样式 */
.el-tabs__item.is-active {
  color: #409EFF;
  font-weight: bold;
}

/* 鼠标悬停标签样式 */
.el-tabs__item:hover {
  color: #66b1ff;
}

/* 响应式设计 */
@media (max-width: 768px) {
  div {
    padding: 10px;
  }

  h2 {
    font-size: 20px;
    margin-bottom: 15px;
  }

  .el-tab-pane {
    padding: 15px;
  }
}
</style>
