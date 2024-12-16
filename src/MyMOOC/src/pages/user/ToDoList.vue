<template>
  <div class="todo-assignments-container">
    <h2>待办事项</h2>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="assignments.length === 0" class="no-assignments">
      您还没有需要完成的作业。
    </div>
    <div v-else>
      <div class="assignments-grid">
        <div
          v-for="assignment in assignments"
          :key="assignment.assignment_id"
          class="assignment-card"
        >
          <h3 class="assignment-title">{{ assignment.title }}</h3>
          <p class="assignment-description">{{ assignment.description }}</p>
          <p class="assignment-meta">
            课程: {{ getCourseName(assignment.course_id) }}
          </p>
          <p class="assignment-meta">
            截止日期: {{ formatDate(assignment.due_date) }}
          </p>

          <!-- 根据状态显示按钮 -->
          <button
            v-if="canSubmitAssignment(assignment)"
            @click="openSubmitModal(assignment)"
            class="submit-assignment-button"
          >
            {{ submitButtonText(assignment) }}
          </button>

          <div
            v-if="assignment.score !== null || assignment.feedback !== null"
            class="assignment-feedback"
          >
            <p><strong>分数：</strong>{{ assignment.score !== null ? assignment.score : '未评分' }}</p>
            <p><strong>反馈：</strong>{{ assignment.feedback !== null ? assignment.feedback : '暂无反馈' }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 提交或更新作业模态框 -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeSubmitModal">
      <div class="modal-content">
        <h3>{{ isUpdateMode ? '更新作业提交' : '提交作业' }}</h3>
        <p><strong>作业标题：</strong>{{ selectedAssignment.title }}</p>
        <p><strong>作业描述：</strong>{{ selectedAssignment.description }}</p>
        <p><strong>所属课程：</strong>{{ getCourseName(selectedAssignment.course_id) }}</p>
        <p><strong>截止日期：</strong>{{ formatDate(selectedAssignment.due_date) }}</p>

        <form @submit.prevent="submitAssignmentAction" class="submission-form">
          <div class="form-group">
            <label for="textContent">文本内容：</label>
            <textarea
              id="textContent"
              v-model="submission.textContent"
              rows="4"
              placeholder="在此输入作业内容"
            ></textarea>
          </div>
          <div class="form-group">
            <label for="fileUpload">上传文件：</label>
            <input type="file" id="fileUpload" @change="handleFileUpload" />
          </div>
          <div class="form-actions">
            <button type="button" class="cancel-button" @click="closeSubmitModal" :disabled="submitting">
              取消
            </button>
            <button type="submit" class="submit-button" :disabled="submitting">
              {{ submitting ? '提交中...' : (isUpdateMode ? '更新作业提交' : '提交') }}
            </button>
          </div>
        </form>
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
const assignments = ref([])
const loading = ref(true)
const error = ref(null)

// 模态框控制
const showModal = ref(false)
const selectedAssignment = ref(null)

// 提交作业表单数据
const submission = ref({
  textContent: '',
  fileName: null
})
const submitting = ref(false)

// 是否更新模式：当已提交过且仍可提交时则为true
const isUpdateMode = ref(false)

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
        assignments.value = []
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

// 获取所有作业
const fetchAssignments = async () => {
  try {
    if (courses.value.length === 0) {
      assignments.value = []
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
      console.error('获取提交记录失败:', submissionsResponse.data.message)
    }

    const now = new Date()

    // 结合提交记录，标记已提交，并过滤已评分或过期的作业
    assignments.value = allAssignments.map(assignment => {
      const submission = submissions.find(sub => sub.assignment_id === assignment.assignment_id)
      const dueDate = new Date(assignment.due_date)
      const isPastDue = now > dueDate
      const isGraded = submission && submission.grade !== null

      // 仅展示未评过分且未过期的作业
      if (isGraded || isPastDue) {
        return null
      }

      return {
        ...assignment,
        submitted: submission ? true : false,
        score: submission ? submission.grade : null,
        feedback: submission ? submission.feedback : null,
        submission_id: submission ? submission.submission_id : null // 添加 submission_id
      }
    }).filter(assignment => assignment !== null)
  } catch (err) {
    error.value = err.message || '获取作业失败'
    console.error('获取作业时发生错误:', err.message)
  }
}

const fetchData = async () => {
  await fetchRegisteredCourses()
  await fetchAssignments()
}

const openSubmitModal = (assignment) => {
  selectedAssignment.value = assignment
  submission.value.textContent = ''
  submission.value.fileName = null
  showModal.value = true
  // 判断是否为更新模式
  isUpdateMode.value = assignment.submitted === true && !isOverdue(assignment) && assignment.score === null
}

const closeSubmitModal = () => {
  if (!submitting.value) {
    showModal.value = false
    selectedAssignment.value = null
    submission.value.textContent = ''
    submission.value.fileName = null
    isUpdateMode.value = false
  }
}

// 根据截止时间判断是否过期
const isOverdue = (assignment) => {
  const now = new Date()
  const dueDate = new Date(assignment.due_date)
  return now > dueDate
}

// 判断是否可以提交或更新作业
const canSubmitAssignment = (assignment) => {
  // 若已有评分或已过截止日期，不可提交
  if (assignment.score !== null || isOverdue(assignment)) {
    return false
  }
  // 没有评分且未过截止时间可提交
  return true
}

// 根据状态显示按钮文本
const submitButtonText = (assignment) => {
  // 若已提交过，未评分且未过期 => "更新作业提交"
  if (assignment.submitted === true && assignment.score === null && !isOverdue(assignment)) {
    return "更新作业提交"
  }
  // 若从未提交过且未过期 => "提交作业"
  if (assignment.submitted === false && !isOverdue(assignment)) {
    return "提交作业"
  }
  // 若评分已存在或已过期则不会显示按钮，此处仅返回空字符串
  return ""
}

// 文件选择
const handleFileUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    // TODO: 需要先将文件上传到后端或CDN，然后获取文件路径
    // 此处简化为假设文件已上传至 /assets/assignmentSubmissions/
    submission.value.fileName = file.name
  } else {
    submission.value.fileName = null
  }
}

// 提交或更新作业
const submitAssignmentAction = async () => {
  if (!selectedAssignment.value) return

  // 表单验证：至少提供文本内容或上传文件
  if (!submission.value.textContent.trim() && !submission.value.fileName) {
    alert('请填写文本内容或上传文件（至少一项）')
    return
  }

  submitting.value = true
  try {
    // 构建 payload，包含 submission_id 如果是更新模式
    const payload = {
      student_id: userInfo.value.user_id,
      course_id: selectedAssignment.value.course_id,
      assignment_id: selectedAssignment.value.assignment_id,
      text_content: submission.value.textContent,
      content_url: submission.value.fileName ? `/assets/assignmentSubmissions/${submission.value.fileName}` : null
    }

    if (isUpdateMode.value) {
      payload.submission_id = selectedAssignment.value.submission_id // 添加 submission_id
    }

    console.log('提交数据:', payload)

    // 判断调用提交或更新接口
    let url = '/assignment-submissions/submit'
    if (isUpdateMode.value) {
      url = '/assignment-submissions/update'
    }

    const response = await axiosInstance.post(url, payload, {
      headers: {
        'Content-Type': 'application/json'
      }
    })

    if (response.status === 200 && response.data.status === 200) {
      alert(isUpdateMode.value ? '作业更新成功' : '作业提交成功')
      // 更新 assignments 中对应作业的状态
      await fetchAssignments()
      closeSubmitModal()
    } else {
      alert(response.data.message || (isUpdateMode.value ? '作业更新失败' : '作业提交失败'))
    }
  } catch (err) {
    console.error(isUpdateMode.value ? '更新作业时发生错误:' : '提交作业时发生错误:', err.message)
    alert(isUpdateMode.value ? '作业更新失败' : '作业提交失败')
  } finally {
    submitting.value = false
    closeSubmitModal()
  }
}

const formatDate = (datetime) => {
  const date = new Date(datetime)
  return date.toLocaleString()
}

// 获取课程名称
const getCourseName = (courseId) => {
  const course = courses.value.find(c => c.course_id === courseId)
  return course ? course.course_name : '未知课程'
}

onMounted(() => {
  fetchData().then(() => {
    loading.value = false
  })
})
</script>

<style scoped lang="less">
.todo-assignments-container {
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

.assignment-feedback {
  margin-top: 10px;
  font-size: 0.9em;
  color: #2c3e50;

  p {
    margin: 2px 0;
  }
}

.submit-assignment-button {
  padding: 10px 15px;
  background-color: #27ae60;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s;
  font-size: 1em;

  &:hover {
    background-color: #219150;
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

.submission-form {
  margin-top: 15px;
}

.form-group {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 5px;
  color: #333;
  font-weight: bold;
}

.form-group textarea,
.form-group input[type="file"] {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 1em;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  gap: 10px;
}

.submit-button {
  padding: 10px 20px;
  background-color: #2980b9;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;

  &:hover {
    background-color: #1c5980;
  }

  &:disabled {
    background-color: #95a5a6;
    cursor: not-allowed;
  }
}

.cancel-button {
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

  &:disabled {
    background-color: #95a5a6;
    cursor: not-allowed;
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
