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
            :disabled="!canSelectCourse"
            @click="handleSelectCourse"
          >
            {{ selectButtonText }}
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
        <button
          :class="['tab-button', currentTab === 'comments' ? 'active' : '']"
          @click="currentTab = 'comments'"
        >
          评论区
        </button>
      </div>

      <!-- 课程介绍或评论区 -->
      <div class="tab-content">
        <!-- 课程介绍 -->
        <div v-if="currentTab === 'description'" class="course-description-section">
          <strong>课程简介：</strong>{{ course.course_description }}
        </div>

        <!-- 评论区 -->
        <div v-else-if="currentTab === 'comments'" class="comments-section">
          <!-- 添加评论表单 -->
          <div class="add-comment">
            <h2>添加评论</h2>
            <textarea v-model="newCommentContent" placeholder="请输入您的评论..." rows="4"></textarea>
            <button @click="addComment" :disabled="!canAddComment">提交评论</button>
          </div>

          <!-- 评论列表 -->
          <div class="comments-list">
            <h2>所有评论</h2>
            <div v-if="comments.length === 0">暂无评论</div>
            <div v-else>
              <div v-for="comment in comments" :key="comment.comment_id" class="comment-item">
                <div class="comment-header">
                  <img :src="getImageUrl(comment.user_image)" alt="User Avatar" class="user-avatar" />
                  <span class="username">{{ comment.username }}</span>
                  <span class="comment-time">{{ formatDate(comment.create_at) }}</span>
                  <button
                    v-if="comment.user_id === userInfo.user_id"
                    class="delete-button"
                    @click="deleteComment(comment.comment_id)"
                  >
                    删除
                  </button>
                </div>
                <div class="comment-content">{{ comment.content }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axiosInstance from '@/utils/request/Axios.ts'
import Breadcrumb from './Breadcrumb.vue'
import { getLoginRecord } from '../homePage/login/LoginRecord'

// 获取路由参数
const route = useRoute()
const courseId = route.params.courseId

// 定义响应式变量
const course = ref(null)
const loading = ref(true)
const error = ref(null)
const currentTab = ref('description') // 'description' 或 'comments'

// 用户信息
const userInfo = ref(getLoginRecord())

// 新评论内容
const newCommentContent = ref('')

// 评论列表
const comments = ref([])

// 存储用户ID到用户名和头像的映射，避免重复请求
const userNamesMap = ref({})

// 获取课程详情
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

// 获取所有评论
const fetchComments = async () => {
  try {
    const response = await axiosInstance.get(`/comments/course/${courseId}`)
    console.log('Fetch Comments Response:', response); // 调试日志

    if (response.status === 200 && response.data.status === 200) {
      const fetchedComments = response.data.data

      // 为每条评论获取用户名，并设置默认头像
      for (const comment of fetchedComments) {
        if (!comment.user_id) {
          // 处理没有 user_id 的评论
          comment.username = '未知用户'
          comment.user_image = '20241203155134.jpg'
          continue
        }

        if (!userNamesMap.value[comment.user_id]) {
          try {
            const userResponse = await axiosInstance.get(`/username/${comment.user_id}`)
            console.log(userResponse);
            if (userResponse.status === 200 && userResponse.data.status === 200) {
              userNamesMap.value[comment.user_id] = {
                username: userResponse.data.message,
                user_image: '20241203155134.jpg' // 始终使用默认头像
              }
            } else {
              userNamesMap.value[comment.user_id] = {
                username: '未知用户',
                user_image: '20241203155134.jpg' // 默认头像
              }
            }
          } catch (err) {
            console.error(`获取用户 ${comment.user_id} 信息时发生错误:`, err.message)
            userNamesMap.value[comment.user_id] = {
              username: '未知用户',
              user_image: 'http://localhost:5173/src/assets/20241203155134.jpg' // 默认头像
            }
          }
        }

        comment.username = userNamesMap.value[comment.user_id].username
        comment.user_image = userNamesMap.value[comment.user_id].user_image
      }

      comments.value = fetchedComments
    } else {
      console.error('获取评论失败:', response.data.message)
    }
  } catch (err) {
    console.error('获取评论时发生错误:', err.message)
  }
}



// 添加评论
const addComment = async () => {
  if (!newCommentContent.value.trim()) {
    alert('评论内容不能为空')
    return
  }
  try {
    const payload = {
      course_id: course.value.course_id,
      user_id: userInfo.value.user_id, // 确保包含 user_id
      content: newCommentContent.value
    }
    const response = await axiosInstance.post(`/comments/action?action=add`, payload)
    if (response.status === 200 && response.data.status === 200) {
      alert('评论添加成功')
      newCommentContent.value = ''
      fetchComments()
    } else {
      alert(response.data.message || '添加评论失败')
    }
  } catch (err) {
    console.error('添加评论时发生错误:', err.message)
    alert('添加评论失败')
  }
}

// 删除评论
const deleteComment = async (commentId) => {
  if (!confirm('确定要删除这条评论吗？')) {
    return
  }
  try {
    const payload = {
      comment_id: commentId
    }
    const response = await axiosInstance.post(`/comments/action?action=delete`, payload)
    if (response.status === 200 && response.data.status === 200) {
      alert('评论删除成功')
      fetchComments()
    } else {
      alert(response.data.message || '删除评论失败')
    }
  } catch (err) {
    console.error('删除评论时发生错误:', err.message)
    alert('删除评论失败')
  }
}

// 格式化日期
const formatDate = (datetime) => {
  const date = new Date(datetime)
  return date.toLocaleString()
}

// 获取图片URL
const getImageUrl = (relativeUrl) => {
  const baseUrl = 'http://localhost:5173/src/assets/' // 修改为正确的基础URL
  return relativeUrl ? `${baseUrl}${relativeUrl}` : 'http://localhost:5173/assets/default.jpg' // 替换为默认图片URL
}

// 处理选课按钮逻辑
const canSelectCourse = computed(() => {
  if (userInfo.value.role === 1) { // 学生
    return course.value.status === '未开课' || course.value.status === '进行中'
  }
  return false
})

const selectButtonText = computed(() => {
  if (userInfo.value.role === 1) { // 学生
    if (course.value.status === '未开课') {
      return '选课 (未开课)'
    } else if (course.value.status === '进行中') {
      return '选课 (进行中)'
    }
  }
  return '不可选课'
})

// 选课按钮点击处理
const handleSelectCourse = async () => {
  try {
    // 假设有一个API端点用于选课，例如 POST /courses/select
    const payload = {
      course_id: course.value.course_id,
      user_id: userInfo.value.user_id
    }
    const response = await axiosInstance.post(`/courses/select`, payload)
    if (response.status === 200 && response.data.status === 200) {
      alert('选课成功')
      // 这里可以更新按钮状态或其他逻辑
    } else {
      alert(response.data.message || '选课失败')
    }
  } catch (err) {
    console.error('选课时发生错误:', err.message)
    alert('选课失败')
  }
}

// 检查是否可以添加评论
const canAddComment = computed(() => {
  return userInfo.value.isLogged && course.value.allow_comments
})

onMounted(() => {
  fetchCourseDetail().then(() => {
    if (course.value) {
      fetchComments()
    }
  })
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

.comments-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.add-comment {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.add-comment textarea {
  width: 100%;
  padding: 10px;
  resize: vertical;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.add-comment button {
  align-self: flex-end;
  padding: 8px 16px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-comment button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.comment-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  background-color: #f9f9f9;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 50%;
}

.username {
  font-weight: bold;
  color: #333;
}

.comment-time {
  margin-left: auto;
  font-size: 0.9em;
  color: #888;
}

.delete-button {
  margin-left: 10px;
  padding: 5px 10px;
  background-color: #dc3545;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: #c82333;
}

.comment-content {
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
