<template>
  <div>
    <Breadcrumb style="margin: 10px;" />
    <div class="courses-container">
      <div
        class="course-card"
        v-for="course in courses"
        :key="course.course_id"
      >
        <router-link :to="`/courses/${course.course_id}`">
          <img :src="getImageUrl(course.course_image)" alt="Course Image" class="course-image" />
        </router-link>
        <div class="course-details">
          <router-link :to="`/courses/${course.course_id}`" class="course-name">
            {{ course.course_name }}
          </router-link>
          <div class="course-meta">
            <span>开始时间: {{ formatDate(course.start_time) }}</span>
            <span>结束时间: {{ formatDate(course.end_time) }}</span>
            <span>状态: {{ course.status }}</span>
          </div>
          <div class="course-description">
            <strong>课程简介：</strong>{{ course.course_description }}
          </div>
        </div>
      </div>
    </div>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-if="error" class="error">{{ error }}</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axiosInstance from '@/utils/request/Axios.ts';
import NavBar from '../../components/NavBar.vue'
import Breadcrumb from './Breadcrumb.vue'

const courses = ref([])
const loading = ref(true)
const error = ref(null)

const fetchCourses = async () => {
  const result = await axiosInstance.myGetting('/courses')
  if (result.status === 200) {
    courses.value = result.data
  } else {
    error.value = result.message || '获取课程列表失败'
  }
  loading.value = false
}

const formatDate = (datetime) => {
  const date = new Date(datetime)
  return date.toLocaleString()
}

const getImageUrl = (relativeUrl) => {
  const baseUrl = 'http://localhost:5173/src/assets/' // 修改为正确的基础URL
  return relativeUrl ? `${baseUrl}${relativeUrl}` : 'http://localhost:5173/assets/default.jpg' // 替换为默认图片URL
}

onMounted(() => {
  fetchCourses()
})
</script>

<style scoped lang="less">
.courses-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
}

.course-card {
  display: flex;
  flex-direction: row;
  width: 100%;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.course-image {
  width: 200px;
  height: 150px;
  object-fit: cover;
  cursor: pointer;
}

.course-details {
  padding: 15px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  flex: 1;
}

.course-name {
  font-size: 1.5em;
  font-weight: bold;
  color: #333;
  text-decoration: none;
  margin-bottom: 10px;
}

.course-name:hover {
  text-decoration: underline;
}

.course-meta {
  display: flex;
  gap: 15px;
  font-size: 0.9em;
  color: #666;
  margin-bottom: 10px;
}

.course-description {
  font-size: 0.8em; /* 调整字体大小 */
  color: #555;
}

.loading,
.error {
  text-align: center;
  margin: 20px;
  font-size: 1.2em;
}

.error {
  color: red;
}

@media (max-width: 768px) {
  .course-card {
    width: 100%;
    flex-direction: column;
    height: auto;
  }

  .course-image {
    width: 100%;
    height: 150px;
  }

  .course-details {
    padding: 15px;
  }
}
</style>
