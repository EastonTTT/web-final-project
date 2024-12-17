<template>
  <div class="my-discussion-container">
    <h2>我的评论</h2>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="Object.keys(groupedComments).length === 0" class="no-comments">
      你还没有发表任何评论。
    </div>
    <div v-else>
      <div v-for="(comments, courseId) in groupedComments" :key="courseId" class="course-comments">
        <h3>课程：{{ getCourseName(courseId) }}</h3>
        <el-table :data="comments" style="width: 100%;" stripe>
          <el-table-column prop="content" label="评论内容"></el-table-column>
          <el-table-column prop="create_at" label="评论时间" width="180"></el-table-column>
          <el-table-column label="操作" width="150">
            <template v-slot="scope">
              <el-button
                size="small"
                type="danger"
                @click="deleteComment(scope.row.comment_id)"
                icon="el-icon-delete"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axiosInstance from '@/utils/request/Axios.ts'
import { ElTable, ElTableColumn, ElButton } from 'element-plus'
import { getLoginRecord } from '../homePage/login/LoginRecord'

const loginRecord = getLoginRecord()
const userId = loginRecord.user_id

// 状态变量
const loading = ref(true)
const error = ref(null)
const groupedComments = ref({}) // 分组后的评论数据
const courseMap = ref({}) // 课程ID与课程名称的映射

// 获取评论数据
const fetchUserComments = async () => {
  try {
    const response = await axiosInstance.get(`/comments/user/${userId}`)
    if (response.status === 200 && response.data.status === 200) {
      const comments = response.data.data

      // 重置 groupedComments 以避免重复
      groupedComments.value = {}

      // 按课程分组评论
      comments.forEach(comment => {
        if (!groupedComments.value[comment.course_id]) {
          groupedComments.value[comment.course_id] = []
        }
        groupedComments.value[comment.course_id].push(comment)
      })
    } else {
      error.value = response.data.message || '获取评论失败'
    }
  } catch (err) {
    error.value = '无法加载评论：' + err.message
  } finally {
    loading.value = false
  }
}

// 获取课程数据
const fetchCourses = async () => {
  try {
    const response = await axiosInstance.get('/courses')
    if (response.status === 200 && response.data.status === 200) {
      const courses = response.data.data

      // 重置 courseMap 以避免重复
      courseMap.value = {}

      courses.forEach(course => {
        courseMap.value[course.course_id] = course.course_name
      })
    } else {
      error.value = response.data.message || '获取课程信息失败'
    }
  } catch (err) {
    error.value = '无法加载课程信息：' + err.message
  }
}

// 根据课程ID获取课程名称
const getCourseName = courseId => {
  return courseMap.value[courseId] || '未知课程'
}

// 删除评论
const deleteComment = async (commentId) => {
  if (!confirm('确定要删除这条评论吗？')) {
    return
  }
  try {
    const payload = {
      comment_id: commentId,
      user_id: userId
    }
    const response = await axiosInstance.post('/comments/action?action=delete', payload)

    if (response.status === 200 && response.data.status === 200) {
      alert('评论删除成功')
      await fetchUserComments() // 确保重新获取评论数据
    } else {
      alert(response.data.message || '删除评论失败')
    }
  } catch (err) {
    console.error('删除评论时发生错误:', err.message)
    alert('删除评论失败')
  }
}

// 初始化
onMounted(async () => {
  loading.value = true
  await Promise.all([fetchCourses(), fetchUserComments()])
})
</script>

<style scoped>
.my-discussion-container {
  padding: 20px;
  background-color: #f7f8fc;
  min-height: 100vh;
  font-family: 'Arial', sans-serif;
  box-sizing: border-box;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #34495e;
  font-size: 2.4em;
  font-weight: 700;
}

.loading,
.error,
.no-comments {
  text-align: center;
  font-size: 1.3em;
  margin-top: 50px;
}

.error {
  color: #e74c3c;
}

.no-comments {
  color: #555;
}

.course-comments {
  margin-bottom: 30px;
  background: white;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.course-comments h3 {
  margin-bottom: 20px;
  font-size: 1.6em;
  color: #2c3e50;
}

.el-table {
  width: 100%;
  border-radius: 8px;
}

.el-table th {
  background-color: #f1f1f1;
  font-size: 1.1em;
  color: #2c3e50;
  font-weight: 600;
}

.el-table td {
  font-size: 1em;
  color: #555;
}

.el-button {
  padding: 4px 10px;
  font-size: 0.9em;
  border-radius: 20px;
}

.el-button.danger {
  background-color: #e74c3c;
  border-color: #e74c3c;
}

.el-button.danger:hover {
  background-color: #c0392b;
  border-color: #c0392b;
}
</style>

