<!-- src/components/Mylearning.vue -->
<template>
  <div class="my-learning-container">
    <h2>我的学习</h2>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="courses.length === 0" class="no-courses">
      您还没有选修任何课程。
    </div>
    <div v-else>
      <div class="course-grid">
        <div v-for="course in courses" :key="course.course_id" class="course-card">
          <img :src="getImageUrl(course.course_image)" alt="Course Image" class="course-image" />
          <div class="course-info">
            <h3 class="course-name">{{ course.course_name }}</h3>
            <p class="course-meta">开始时间: {{ formatDate(course.start_time) }}</p>
            <p class="course-meta">结束时间: {{ formatDate(course.end_time) }}</p>
            <button @click="startLearning(course.course_id)" class="start-learning-button">
              开始学习
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axiosInstance from '@/utils/request/Axios.ts'
import { useRouter } from 'vue-router'
import { getLoginRecord } from '../homePage/login/LoginRecord'

// 响应式变量
const courses = ref([]) // 存储详细课程信息
const loading = ref(true)
const error = ref(null)

// 路由实例用于页面跳转
const router = useRouter()

// 用户信息
const userInfo = ref(getLoginRecord())

// 获取用户已注册的课程记录
const fetchRegisteredCourses = async () => {
  try {
    const response = await axiosInstance.get('/course-registrations/registered', {
      params: { student_id: userInfo.value.user_id }
    })
    if (response.status === 200 && response.data.status === 200) {
      const registrations = response.data.data
      if (registrations.length === 0) {
        courses.value = []
        return
      }
      // 提取所有 course_id
      const courseIds = registrations.map(reg => reg.course_id)

      // 使用 Promise.all 并行请求所有课程的详细信息
      const courseDetailPromises = courseIds.map(id =>
        axiosInstance.get(`/courses/course/${id}`).then(res => {
          if (res.status === 200 && res.data.status === 200) {
            return res.data.data
          } else {
            console.error(`获取课程ID ${id} 的详细信息失败: ${res.data.message}`)
            return null
          }
        }).catch(err => {
          console.error(`请求课程ID ${id} 失败:`, err.message)
          return null
        })
      )

      const courseDetails = await Promise.all(courseDetailPromises)
      // 过滤掉获取失败的课程
      courses.value = courseDetails.filter(course => course !== null)
    } else {
      error.value = response.data.message || '获取已选课程失败'
      console.error('获取已选课程失败:', response.data.message)
    }
  } catch (err) {
    error.value = err.message || '获取已选课程失败'
    console.error('获取已选课程时发生错误:', err.message)
  } finally {
    loading.value = false
  }
}

// 开始学习按钮点击处理
const startLearning = (courseId) => {
  // 假设学习详情页面的路由为 /courses/:courseId/learn
  router.push(`/courses/${courseId}/learn`)
}

// 格式化日期
const formatDate = (datetime) => {
  const date = new Date(datetime)
  return date.toLocaleString()
}

// 获取图片URL
const getImageUrl = (relativeUrl) => {
  const baseUrl = 'http://localhost:5173/src/assets/' // 修改为正确的基础URL
  return relativeUrl ? `${baseUrl}${relativeUrl}` : `${baseUrl}default_course.jpg` // 替换为默认图片URL
}

// 在组件挂载时获取用户已注册的课程
onMounted(() => {
  fetchRegisteredCourses()
})
</script>

<style scoped lang="less">
.my-learning-container {
  padding: 30px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 2em;
}

.loading,
.error,
.no-courses {
  text-align: center;
  font-size: 1.2em;
  margin-top: 40px;
}

.error {
  color: #e74c3c;
}

.no-courses {
  color: #555;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.course-card {
  background-color: #ffffff;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
  }
}

.course-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
}

.course-info {
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 180px;
}

.course-name {
  font-size: 1.4em;
  color: #2c3e50;
  margin-bottom: 10px;
}

.course-meta {
  font-size: 0.95em;
  color: #7f8c8d;
  margin-bottom: 10px;
}

.start-learning-button {
  padding: 10px 15px;
  background-color: #3498db;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1em;
  transition: background-color 0.3s, transform 0.2s;

  &:hover {
    background-color: #2980b9;
    transform: scale(1.05);
  }

  &:active {
    transform: scale(0.95);
  }
}

@media (max-width: 768px) {
  .my-learning-container {
    padding: 20px;
  }

  .course-info {
    padding: 15px;
    height: auto;
  }

  .course-meta {
    font-size: 0.9em;
  }

  .start-learning-button {
    font-size: 0.9em;
    padding: 8px 12px;
  }
}
</style>
