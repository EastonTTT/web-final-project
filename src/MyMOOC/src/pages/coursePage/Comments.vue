<!-- src/components/Comments.vue -->
<template>
  <div class="comments-section">
    <!-- 添加评论表单 -->
    <div v-if="course.allow_comments" class="add-comment">
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
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axiosInstance from '@/utils/request/Axios.ts'
import { getLoginRecord } from '../homePage/login/LoginRecord'

// Props 接收来自父组件的课程ID和课程信息
const props = defineProps({
  courseId: {
    type: Number,
    required: true
  },
  course: {
    type: Object,
    required: true
  }
})

// 用户信息
const userInfo = ref(getLoginRecord())

// 新评论内容
const newCommentContent = ref('')

// 评论列表
const comments = ref([])

// 存储用户ID到用户名和头像的映射，避免重复请求
const userNamesMap = ref({})

// 获取所有评论
const fetchComments = async () => {
  try {
    const response = await axiosInstance.get(`/comments/course/${props.courseId}`)
    console.log('Fetch Comments Response:', response) // 调试日志

    if (response.status === 200 && response.data.status === 200) {
      const fetchedComments = response.data.data

      // 为每条评论获取用户名，并设置默认头像
      for (const comment of fetchedComments) {
        if (!comment.user_id) {
          // 处理没有 user_id 的评论
          comment.username = '未知用户'
          comment.user_image = 'default_avatar.jpg'
          continue
        }

        if (!userNamesMap.value[comment.user_id]) {
          try {
            const userResponse = await axiosInstance.get(`/username/${comment.user_id}`)
            console.log(userResponse)
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
              user_image: '20241203155134.jpg' // 默认头像
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
      course_id: props.courseId,
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
  return relativeUrl ? `${baseUrl}${relativeUrl}` : `${baseUrl}20241203155134.jpg` // 替换为默认图片URL
}

// 检查是否可以添加评论
const canAddComment = computed(() => {
  return userInfo.value.isLogged && props.course.allow_comments
})

// 在组件挂载时获取评论
onMounted(() => {
  fetchComments()
})
</script>

<style scoped lang="less">
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
</style>
