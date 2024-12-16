<!-- src/components/CourseDetail.vue -->
<template>
  <div class="course-detail-container">
    <Breadcrumb style="margin: 10px;" />
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="course-detail">
      <!-- 第一行：课程图片和课程信息 -->
      <div class="course-header">
        <img :src="getImageUrl(course.course_image)" alt="Course Image" class="course-image" />
        <div class="course-info">
          <h1 class="course-name">{{ course.course_name }}</h1>
          <div class="course-meta">
            <span>开始时间: {{ formatDate(course.start_time) }}</span>
            <span>结束时间: {{ formatDate(course.end_time) }}</span>
            <span>状态: {{ course.status }}</span>
          </div>
          <button
            class="select-course-button"
            :disabled="!canInteractWithCourse"
            @click="handleButtonClick"
          >
            {{ buttonText }}
          </button>
        </div>
      </div>

      <!-- 第二行：切换按钮 -->
      <div class="course-tabs">
        <button
          :class="['tab-button', currentTab === 'description' ? 'active' : '']"
          @click="currentTab = 'description'"
        >
          课程介绍
        </button>

        <!-- 新增：根据 allow_comments 显示“评论区”标签 -->
        <button
          v-if="course.allow_comments"
          :class="['tab-button', currentTab === 'comments' ? 'active' : '']"
          @click="currentTab = 'comments'"
        >
          评论区
        </button>

        <!-- 新增：根据 allow_notes、用户角色和是否已注册显示“笔记区”标签 -->
        <button
          v-if="course.allow_notes && isStudent && isRegistered"
          :class="['tab-button', currentTab === 'notes' ? 'active' : '']"
          @click="currentTab = 'notes'"
        >
          笔记区
        </button>
      </div>

      <!-- 课程介绍、评论区或笔记区 -->
      <div class="tab-content">
        <!-- 课程介绍 -->
        <div v-if="currentTab === 'description'" class="course-description-section">
          <strong>课程简介：</strong>{{ course.course_description }}
        </div>

        <!-- 评论区 -->
        <div
          v-else-if="currentTab === 'comments' && course.allow_comments"
          class="comments-section-wrapper"
        >
          <Comments :courseId="course.course_id" :course="course" />
        </div>

        <!-- 笔记区 -->
        <div
          v-else-if="currentTab === 'notes' && course.allow_notes && isStudent && isRegistered"
          class="notes-section-wrapper"
        >
          <Notes :courseId="course.course_id" :course="course" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router' // 引入 useRouter
import axiosInstance from '@/utils/request/Axios.ts'
import Breadcrumb from './Breadcrumb.vue'
import Comments from './Comments.vue'
import Notes from './Notes.vue'
import { getLoginRecord } from '../homePage/login/LoginRecord'

// 获取路由参数
const route = useRoute()
const courseId = route.params.courseId

// 定义响应式变量
const course = ref(null)
const loading = ref(true)
const error = ref(null)
const currentTab = ref('description') // 'description' 或 'comments' 或 'notes'

// 用户信息
const userInfo = ref(getLoginRecord())

// 引入 router 实例用于页面跳转
const router = useRouter()

// 新增：判断用户是否已注册该课程
const isRegistered = ref(false)

// 新增：判断用户角色是否为学生
const isStudent = computed(() => userInfo.value.role === 1)

// 新增：获取用户已注册的课程列表
const fetchRegisteredCourses = async () => {
  try {
    const response = await axiosInstance.get('/course-registrations/registered', {
      params: { student_id: userInfo.value.user_id }
    })
    if (response.status === 200 && response.data.status === 200) {
      const registeredCourses = response.data.data
      // 检查当前课程是否在已注册课程列表中
      isRegistered.value = registeredCourses.some(registration => registration.course_id === course.value.course_id)
    } else {
      console.error('获取已注册课程失败:', response.data.message)
    }
  } catch (err) {
    console.error('获取已注册课程时发生错误:', err.message)
  }
}

// 获取课程详情
const fetchCourseDetail = async () => {
  console.log(userInfo.value.role)
  console.log(userInfo.value.user_id)
  try {
    const response = await axiosInstance.get(`/courses/course/${courseId}`)
    console.log('API Response:', response) // 调试日志
    if (response.status === 200 && response.data.status === 200) {
      course.value = response.data.data
      console.log('Course Data:', course.value) // 调试日志
      // 获取已注册课程
      await fetchRegisteredCourses()
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

// 格式化日期
const formatDate = (datetime) => {
  const date = new Date(datetime)
  return date.toLocaleString()
}

// 获取图片URL
const getImageUrl = (relativeUrl) => {
  const baseUrl = 'http://localhost:5173/src/assets/' // 修改为正确的基础URL
  return relativeUrl ? `${baseUrl}${relativeUrl}` : `${baseUrl}20241203155134.jpg` // 替换为默认图片URL
}

// 计算是否可以与课程进行交互（选课或进入学习）
const canInteractWithCourse = computed(() => {
  if (userInfo.value.role === 1) { // 学生
    return course.value.status === '未开课' || course.value.status === '已开课' || isRegistered.value
  }
  return false
})

// 计算按钮文本
const buttonText = computed(() => {
  if (userInfo.value.role === 1) { // 学生
    if (isRegistered.value) {
      return '开始学习'
    } else {
      if (course.value.status === '未开课') {
        return '选课 (未开课)'
      } else if (course.value.status === '已开课') {
        return '选课 (已开课)'
      }
    }
  }
  return '不可选课'
})

// 选课或进入学习按钮点击处理
const handleButtonClick = async () => {
  if (isRegistered.value) {
    // 用户已注册，跳转到学习详情页面
    // 假设学习详情页面的路由为 /courses/:courseId/learn
    router.push(`/courses/${course.value.course_id}/learn`)
  } else {
    // 用户未注册，执行选课操作
    handleSelectCourse()
  }
}

// 选课按钮点击处理
const handleSelectCourse = async () => {
  try {
    const payload = new URLSearchParams();
    payload.append('student_id', userInfo.value.user_id); // 使用 student_id 替代 user_id
    payload.append('course_id', course.value.course_id);

    const response = await axiosInstance.post('/course-registrations/register', payload, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });

    if (response.status === 200 && response.data.status === 200) {
      alert('选课成功')
      // 更新 UI 状态
      isRegistered.value = true
    } else {
      alert(response.data.message || '选课失败')
    }
  } catch (err) {
    console.error('选课时发生错误:', err.message)
    alert('选课失败')
  }
}

// 在组件挂载时获取课程详情
onMounted(() => {
  fetchCourseDetail()
})
</script>

<style scoped lang="less">
.course-detail-container {
  padding: 20px;
}

.course-detail {
  max-width: 1000px;
  margin: 0 auto;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.course-header {
  display: flex;
  flex-direction: row;
  gap: 20px;
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

.select-course-button {
  padding: 10px 20px;
  background-color: #28a745;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.select-course-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.course-tabs {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.tab-button {
  padding: 10px 20px;
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.tab-button.active {
  background-color: #007bff;
  color: #fff;
  border-color: #007bff;
}

.tab-content {
  margin-top: 20px;
}

.course-description-section {
  font-size: 1em;
  color: #555;
}

.comments-section-wrapper {
  /* 可以根据需要添加样式 */
}

.notes-section-wrapper {
  /* 可以根据需要添加样式 */
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

@media (max-width: 768px) {
  .course-header {
    flex-direction: column;
    align-items: center;
  }

  .course-image {
    width: 100%;
    height: auto;
  }

  .course-info {
    width: 100%;
    align-items: center;
    text-align: center;
  }

  .course-meta {
    flex-direction: column;
    gap: 10px;
  }

  .select-course-button {
    width: 100%;
  }
}
</style>
