<template>
  <div class="courses-manage-container">
    <h2 class="page-title">课程管理</h2>
    <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="tabs-container">
      <el-tab-pane label="课程发布" name="coursePublishing">
        <CoursePublishing />
      </el-tab-pane>
      <el-tab-pane label="已发布课程" name="publishedCourses">
        <PublishedCourses />
      </el-tab-pane>
      <el-tab-pane label="课件管理" name="courseware">
        <CourseManagement />
      </el-tab-pane>
      <el-tab-pane label="作业管理" name="assignments">
        <AssignmentsList />
      </el-tab-pane>
      <el-tab-pane label="批改作业" name="grading">
        <GradingAssignments />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';
import CourseManagement from './CoursewareManagement.vue';
import CoursePublishing from './CoursePublishing.vue';
import PublishedCourses from './PublishedCourses.vue';
import AssignmentsList from './AssignmentsList.vue';
import GradingAssignments from './GradingAssignments.vue';


export default defineComponent({
  name: 'CourcesManage',
  components: {
    PublishedCourses,
    CourseManagement,
    CoursePublishing,
    AssignmentsList,
    GradingAssignments
  },
  setup() {
    const activeTab = ref('coursePublishing');

    const handleTabClick = (tab: any) => {
      // 如果需要，可以在这里处理标签点击事件
    };

    return {
      activeTab,
      handleTabClick,
    };
  },
});
</script>

<style scoped>
.courses-manage-container {
  padding: 40px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  font-family: "Microsoft Yahei", sans-serif;
  max-width: 1200px;
  margin: 0 auto;
}

.page-title {
  text-align: center;
  margin-bottom: 30px;
  color: #34495e;
  font-size: 32px;
  font-weight: 700;
  letter-spacing: 1px;
}

.tabs-container {
  background-color: #ffffff;
  border-radius: 12px;
  overflow: hidden;
}

.el-tabs__header {
  background-color: #ecf0f1;
  border-bottom: none;
}

.el-tab-pane {
  padding: 30px;
  background-color: #ffffff;
  border-top: 2px solid #3498db;
  transition: all 0.3s ease;
}

.el-tabs__item.is-active {
  color: #3498db !important;
  font-weight: 600;
  border-bottom: 3px solid #3498db;
}

.el-tabs__item {
  color: #7f8c8d;
  font-size: 16px;
  padding: 12px 20px;
  transition: color 0.3s ease, border-bottom 0.3s ease;
}

.el-tabs__item:hover {
  color: #2980b9;
  cursor: pointer;
}

@media (max-width: 1024px) {
  .courses-manage-container {
    padding: 30px;
  }

  .page-title {
    font-size: 28px;
    margin-bottom: 25px;
  }

  .el-tabs__item {
    font-size: 15px;
    padding: 10px 16px;
  }

  .el-tab-pane {
    padding: 25px;
  }
}

@media (max-width: 768px) {
  .courses-manage-container {
    padding: 20px;
  }

  .page-title {
    font-size: 24px;
    margin-bottom: 20px;
  }

  .el-tabs__item {
    font-size: 14px;
    padding: 8px 14px;
  }

  .el-tab-pane {
    padding: 20px;
  }
}
</style>
