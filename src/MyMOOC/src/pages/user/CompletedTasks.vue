<template>
  <div class="completed-assignments-container">
    <h2>已完成任务</h2>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="completedAssignments.length === 0" class="no-assignments">
      您还没有已完成的作业。
    </div>
    <div v-else>
      <div class="assignments-grid">
        <div
          v-for="assignment in completedAssignments"
          :key="assignment.assignment_id"
          class="assignment-card"
        >
          <h3 class="assignment-title">{{ assignment.title }}</h3>
          <p class="assignment-description">{{ assignment.description }}</p>
          <p class="assignment-meta">
            课程: {{ getCourseName(assignment.course_id) }}
          </p>
          <p class="assignment-meta">
            提交日期: {{ formatDate(assignment.submit_time) }}
          </p>

          <button
            @click="openDetailsModal(assignment)"
            class="details-button"
          >
            详情
          </button>
        </div>
      </div>
    </div>

    <!-- 详情模态框 -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeDetailsModal">
      <div class="modal-content">
        <h3>作业详情</h3>
        <p><strong>作业标题：</strong>{{ selectedAssignment.title }}</p>
        <p><strong>作业描述：</strong>{{ selectedAssignment.description }}</p>
        <p><strong>所属课程：</strong>{{ getCourseName(selectedAssignment.course_id) }}</p>
        <p><strong>截止日期：</strong>{{ formatDate(selectedAssignment.due_date) }}</p>
        <p><strong>提交日期：</strong>{{ formatDate(selectedAssignment.submit_time) }}</p>

        <div class="assignment-feedback">
          <p>
            <strong>分数：</strong>
            <span v-if="selectedAssignment.grade !== null">
              {{ selectedAssignment.grade }} ({{ getGradeDescription(selectedAssignment.grade) }})
            </span>
            <span v-else>未评分</span>
          </p>
          <p>
            <strong>反馈：</strong>
            <span v-if="selectedAssignment.feedback !== null">
              {{ selectedAssignment.feedback }}
            </span>
            <span v-else>暂无反馈</span>
          </p>
        </div>

        <button class="close-button" @click="closeDetailsModal">关闭</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axiosInstance from '@/utils/request/Axios.ts'
import { useRouter } from 'vue-router'
import { getLoginRecord } from '../homePage/login/LoginRecord'

// 路由实例用于页面跳转
const router = useRouter()

// 用户信息
const userInfo = ref(getLoginRecord())

// 响应式变量
const courses = ref([])
const completedAssignments = ref([])
const loading = ref(true)
const error = ref(null)

// 模态框控制
const showModal = ref(false)
const selectedAssignment = ref(null)

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
        completedAssignments.value = []
        return
      }
      const courseIds = registrations.map(reg => reg.course_id)
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
      courses.value = courseDetails.filter(course => course !== null)
    } else {
      error.value = response.data.message || '获取已选课程失败'
      console.error('获取已选课程失败:', response.data.message)
    }
  } catch (err) {
    error.value = err.message || '获取已选课程失败'
    console.error('获取已选课程时发生错误:', err.message)
  }
}

// 获取所有已提交的作业
const fetchCompletedAssignments = async () => {
  try {
    if (courses.value.length === 0) {
      completedAssignments.value = []
      return
    }
    const courseIds = courses.value.map(course => course.course_id)
    const assignmentPromises = courseIds.map(id =>
      axiosInstance.get(`/assignments/course/${id}`).then(res => {
        if (res.status === 200 && res.data.status === 200) {
          return res.data.data
        } else {
          console.error(`获取课程ID ${id} 的作业失败: ${res.data.message}`)
          return []
        }
      }).catch(err => {
        console.error(`请求课程ID ${id} 的作业失败:`, err.message)
        return []
      })
    )

    const assignmentsList = await Promise.all(assignmentPromises)
    const allAssignments = assignmentsList.flat()

    // 获取所有提交记录
    const submissionsResponse = await axiosInstance.get('/assignment-submissions/completed', {
      params: { student_id: userInfo.value.user_id }
    })

    console.log('submissionsResponse:', submissionsResponse);

    let submissions = []
    if (submissionsResponse.status === 200 && submissionsResponse.data.status === 200 && submissionsResponse.data.data) {
      submissions = submissionsResponse.data.data
    } else {
      error.value = submissionsResponse.data.message || '获取提交记录失败'
      console.error('获取提交记录失败:', submissionsResponse.data.message)
    }

    // 结合提交记录，标记已提交的作业
    completedAssignments.value = allAssignments.map(assignment => {
      const submission = submissions.find(sub => sub.assignment_id === assignment.assignment_id)
      if (submission) {
        return {
          ...assignment,
          submission_id: submission.submission_id,
          submit_time: submission.submit_time, // 修正字段名
          grade: submission.grade,
          feedback: submission.feedback
        }
      }
      return null
    }).filter(assignment => assignment !== null)
  } catch (err) {
    error.value = err.message || '获取已完成作业失败'
    console.error('获取已完成作业时发生错误:', err.message)
  }
}

const fetchData = async () => {
  await fetchRegisteredCourses()
  await fetchCompletedAssignments()
}

const openDetailsModal = (assignment) => {
  selectedAssignment.value = assignment
  showModal.value = true
}

const closeDetailsModal = () => {
  showModal.value = false
  selectedAssignment.value = null
}

// 格式化日期
const formatDate = (datetime) => {
  if (!datetime) return '无数据'
  const date = new Date(datetime)
  return date.toLocaleString()
}

// 获取课程名称
const getCourseName = (courseId) => {
  const course = courses.value.find(c => c.course_id === courseId)
  return course ? course.course_name : '未知课程'
}

// 根据分数获取评价
const getGradeDescription = (grade) => {
  if (grade >= 90) return '优秀'
  if (grade >= 80) return '良好'
  if (grade >= 70) return '中等'
  if (grade >= 60) return '及格'
  return '不及格'
}

onMounted(() => {
  fetchData().then(() => {
    loading.value = false
  })
})
</script>

<style scoped lang="less">
.completed-assignments-container {
  padding: 30px;
  background-color: #f0f2f5;
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
.no-assignments {
  text-align: center;
  font-size: 1.2em;
  margin-top: 40px;
}

.error {
  color: #e74c3c;
}

.no-assignments {
  color: #555;
}

.assignments-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.assignment-card {
  background-color: #ffffff;
  border: 1px solid #dcdcdc;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
  display: flex;
  flex-direction: column;
  justify-content: space-between;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 12px rgba(0, 0, 0, 0.2);
  }
}

.assignment-title {
  font-size: 1.5em;
  color: #2c3e50;
  margin-bottom: 10px;
}

.assignment-description {
  font-size: 1em;
  color: #34495e;
  margin-bottom: 10px;
}

.assignment-meta {
  font-size: 0.9em;
  color: #7f8c8d;
  margin-bottom: 15px;
}

.details-button {
  padding: 10px 15px;
  background-color: #2980b9;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
  font-size: 1em;

  &:hover {
    background-color: #1c5980;
    transform: scale(1.05);
  }

  &:active {
    transform: scale(0.95);
  }
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: #fff;
  padding: 25px;
  border-radius: 10px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 5px 15px rgba(0,0,0,0.3);
  position: relative;
}

.modal-content h3 {
  margin-bottom: 15px;
  color: #333;
}

.modal-content p {
  margin-bottom: 10px;
  color: #555;
}

.assignment-feedback {
  margin-top: 15px;
  font-size: 0.9em;
  color: #2c3e50;

  p {
    margin: 2px 0;
  }
}

.close-button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #c0392b;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: #922b21;
  }
}

@media (max-width: 768px) {
  .assignments-grid {
    grid-template-columns: 1fr;
  }

  .modal-content {
    width: 95%;
  }
}
</style>
