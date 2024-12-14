<template>
  <div class="course-detail-container">
    <Breadcrumb style="margin: 10px;" />
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="course-detail">
      <img :src="getImageUrl(course.course_image)" alt="Course Image" class="course-image" />
      <div class="course-info">
        <h1 class="course-name">{{ course.course_name }}</h1>
        <div class="course-meta">
          <span>开始时间: {{ formatDate(course.start_time) }}</span>
          <span>结束时间: {{ formatDate(course.end_time) }}</span>
          <span>状态: {{ course.status }}</span>
        </div>
        <div class="course-description">
          <strong>课程简介：</strong>{{ course.course_description }}
        </div>
        <!-- 其他详细信息可以在这里添加 -->
        <router-link to="/courses" class="back-button">返回课程列表</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axiosInstance from '@/utils/request/Axios.ts'
import Breadcrumb from './Breadcrumb.vue'

const route = useRoute()
const courseId = route.params.courseId

const course = ref(null)
const loading = ref(true)
const error = ref(null)

const fetchCourseDetail = async () => {
  try {
    const response = await axiosInstance.get(`/courses/course/${courseId}`)
    console.log('API Response:', response) // 调试日志
    if (response.status === 200 && response.data.status === 200) {
      course.value = response.data.data
      console.log('Course Data:', course.value) // 调试日志
    } else {
      error.value = response.data.message || '获取课程详情失败'
      console.error('Error:', error.value) // 调试日志
    }
  } catch (err) {
    error.value = err.message || '获取课程详情失败'
    console.error('Catch Error:', error.value) // 调试日志
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
  fetchCourseDetail()
})
</script>

<style scoped lang="less">
.course-detail-container {
  padding: 20px;
}

.course-detail {
  display: flex;
  flex-direction: row;
  gap: 20px;
  max-width: 1000px;
  margin: 0 auto;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.course-image {
  width: 300px;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
}

.course-info {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.course-name {
  font-size: 2em;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.course-meta {
  display: flex;
  gap: 20px;
  font-size: 1em;
  color: #666;
  margin-bottom: 15px;
}

.course-description {
  font-size: 1em;
  color: #555;
}

.loading,
.error {
  text-align: center;
  margin: 20px;
  font-size: 1.5em;
}

.error {
  color: red;
}

.back-button {
  display: inline-block;
  margin-top: 20px;
  padding: 10px 15px;
  background-color: #007bff;
  color: #fff;
  border-radius: 4px;
  text-decoration: none;
}

.back-button:hover {
  background-color: #0056b3;
}

@media (max-width: 768px) {
  .course-detail {
    flex-direction: column;
    align-items: center;
  }

  .course-image {
    width: 100%;
    height: auto;
  }

  .course-info {
    width: 100%;
  }

  .course-meta {
    flex-direction: column;
    gap: 10px;
  }
}
</style>
