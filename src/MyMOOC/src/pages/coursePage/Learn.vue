<!-- src/components/Learn.vue -->
<template>
  <div class="learn-container">
    <!-- 顶部：Breadcrumb -->
    <Breadcrumb style="margin: 10px;" />

    <!-- 分界线 -->
    <hr class="divider" />

    <!-- 课程名称 -->
    <div class="course-title">
      <h1>{{ course.course_name }}</h1>
    </div>

    <!-- 分界线 -->
    <hr class="divider" />

    <!-- 内容区域 -->
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="content">
      <!-- 左侧：课程简介 -->
      <div class="course-description">
        <h2>课程简介</h2>
        <p>{{ course.course_description }}</p>
      </div>

      <!-- 右侧：操作选项 -->
      <div class="actions">
        <button @click="toggleNotes">
          {{ showNotes ? '隐藏笔记' : '发布笔记' }}
        </button>
        <button @click="toggleMaterials">
          {{ showMaterials ? '隐藏课件' : '课件下载' }}
        </button>

        <!-- 发布笔记区域 -->
        <div v-if="showNotes" class="notes-section">
          <h3>发布笔记</h3>
          <textarea v-model="newNoteContent" placeholder="请输入您的笔记..." rows="4"></textarea>
          <button @click="addNote" :disabled="!canAddNote">提交笔记</button>

          <!-- 笔记列表 -->
          <div class="notes-list">
            <h4>我的笔记</h4>
            <div v-if="notes.length === 0">暂无笔记</div>
            <div v-else>
              <div v-for="note in notes" :key="note.note_id" class="note-item">
                <div class="note-header">
                  <span class="username">{{ note.username }}</span>
                  <span class="note-time">{{ formatDate(note.create_at) }}</span>
                  <button
                    v-if="isOwner(note)"
                    class="delete-button"
                    @click="deleteNote(note.note_id)"
                  >
                    删除
                  </button>
                </div>
                <div class="note-content">{{ note.content }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 课件下载区域 -->
        <div v-if="showMaterials" class="materials-section">
          <h3>课件下载</h3>
          <div v-if="materials.length === 0">暂无课件</div>
          <ul>
            <li v-for="material in materials" :key="material.material_id">
              <a :href="getDownloadUrl(material.file_path)" target="_blank">{{ material.material_name }}</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axiosInstance from '@/utils/request/Axios.ts'
import Breadcrumb from './Breadcrumb.vue'
import { getLoginRecord } from '@/pages/homePage/login/LoginRecord'

// 获取路由参数
const route = useRoute()
const courseId = route.params.courseId

// 定义响应式变量
const course = ref({})
const loading = ref(true)
const error = ref(null)

// 用户信息，确保初始化为一个对象
const userInfo = ref(getLoginRecord() || {
  isLogged: false,
  user_id: null,
  username: '',
})

// 笔记相关
const newNoteContent = ref('')
const notes = ref([])
const showNotes = ref(false)
const canAddNote = computed(() => {
  return userInfo.value.isLogged && course.value.allow_notes
})

// 课件相关
const materials = ref([])
const showMaterials = ref(false)

// 获取课程详情
const fetchCourseDetail = async () => {
  try {
    const response = await axiosInstance.get(`/courses/course/${courseId}`)
    console.log('课程详情响应:', response) // 添加的日志
    if (response.status === 200 && response.data.status === 200) {
      course.value = response.data.data
    } else {
      error.value = response.data.message || '获取课程详情失败'
    }
  } catch (err) {
    console.error('获取课程详情时发生错误:', err)
    error.value = err.message || '获取课程详情失败'
  }
  loading.value = false
}

// 获取用户的笔记
const fetchNotes = async () => {
  if (!userInfo.value || !userInfo.value.user_id) {
    console.error('用户未登录或 user_id 不存在')
    return
  }
  try {
    const response = await axiosInstance.get(`/notes/course/${courseId}/user/${userInfo.value.user_id}`)
    console.log('获取笔记响应:', response) // 添加的日志
    if (response.status === 200 && response.data.status === 200) {
      notes.value = response.data.data.map(note => ({
        ...note,
        username: userInfo.value.username // 确保返回的数据中包含用户名
      }))
    } else {
      console.error('获取笔记失败:', response.data.message)
    }
  } catch (err) {
    console.error('获取笔记时发生错误:', err.message)
  }
}

// 添加笔记
const addNote = async () => {
  if (!userInfo.value || !userInfo.value.user_id) {
    alert('您尚未登录，无法添加笔记')
    return
  }
  if (!newNoteContent.value.trim()) {
    alert('笔记内容不能为空')
    return
  }
  try {
    const payload = {
      course_id: courseId,
      user_id: userInfo.value.user_id, // 确保包含 user_id
      content: newNoteContent.value
    }
    console.log('添加笔记负载:', payload) // 调试用
    const response = await axiosInstance.post(`/notes/action?action=add`, payload)
    console.log('添加笔记响应:', response) // 添加的日志
    if (response.status === 200 && response.data.status === 200) {
      alert('笔记添加成功')
      newNoteContent.value = ''
      fetchNotes()
    } else {
      alert(response.data.message || '添加笔记失败')
    }
  } catch (err) {
    console.error('添加笔记时发生错误:', err.message)
    alert('添加笔记失败')
  }
}

// 删除笔记
const deleteNote = async (noteId) => {
  if (!userInfo.value || !userInfo.value.user_id) {
    alert('您尚未登录，无法删除笔记')
    return
  }
  if (!confirm('确定要删除这条笔记吗？')) {
    return
  }

  const payload = {
    note_id: noteId
  }

  try {
    const response = await axiosInstance.post(`/notes/action`, payload, {
      params: {
        action: 'delete',
        userId: userInfo.value.user_id
      }
    })
    console.log('删除笔记响应:', response) // 添加的日志
    if (response.status === 200 && response.data.status === 200) {
      alert('笔记删除成功')
      fetchNotes()
    } else {
      alert(response.data.message || '删除笔记失败')
    }
  } catch (err) {
    console.error('删除笔记时发生错误:', err.message)
    alert('删除笔记失败')
  }
}

// 获取课件
const fetchMaterials = async () => {
  try {
    const response = await axiosInstance.get(`/courseware/course/${courseId}`)
    console.log('获取课件响应:', response) // 添加的日志
    if (response.status === 200 && response.data.status === 200) {
      materials.value = response.data.data
    } else {
      console.error('获取课件失败:', response.data.message)
    }
  } catch (err) {
    console.error('获取课件时发生错误:', err.message)
  }
}

// 切换笔记显示
const toggleNotes = () => {
  showNotes.value = !showNotes.value
  if (showNotes.value && notes.value.length === 0) {
    fetchNotes()
  }
}

// 切换课件显示
const toggleMaterials = () => {
  showMaterials.value = !showMaterials.value
  if (showMaterials.value && materials.value.length === 0) {
    fetchMaterials()
  }
}

// 格式化日期
const formatDate = (datetime) => {
  const date = new Date(datetime)
  return date.toLocaleString()
}

// 获取下载链接
const getDownloadUrl = (filePath) => {
  return `/assets/${filePath}`
}

// 检查笔记所有者
const isOwner = (note) => {
  return userInfo.value.isLogged && note.user_id === userInfo.value.user_id
}

onMounted(() => {
  fetchCourseDetail()
  console.log('用户信息:', userInfo.value) // 调试用
})
</script>

<style scoped lang="less">
.learn-container {
  padding: 20px;
}

.course-title {
  text-align: center;
  margin-bottom: 10px;
}

.divider {
  border: none;
  border-top: 2px solid #007bff;
  margin-bottom: 20px;
}

.content {
  display: flex;
  gap: 20px;
}

.course-description {
  flex: 2;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.actions {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.actions button {
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.actions button:hover {
  background-color: #0056b3;
}

.notes-section, .materials-section {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
}

.notes-section textarea {
  width: 100%;
  padding: 10px;
  resize: vertical;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.notes-section button {
  margin-top: 10px;
  padding: 8px 16px;
  background-color: #28a745;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.notes-section button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.notes-section button:hover:not(:disabled) {
  background-color: #218838;
}

.notes-list {
  margin-top: 20px;
}

.note-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  background-color: #f9f9f9;
  margin-bottom: 10px;
}

.note-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.username {
  font-weight: bold;
  color: #333;
}

.note-time {
  font-size: 0.9em;
  color: #888;
}

.delete-button {
  padding: 5px 10px;
  background-color: #dc3545;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.delete-button:hover {
  background-color: #c82333;
}

.note-content {
  font-size: 1em;
  color: #555;
}

.materials-section ul {
  list-style: none;
  padding: 0;
}

.materials-section li {
  margin-bottom: 10px;
}

.materials-section a {
  color: #007bff;
  text-decoration: none;
}

.materials-section a:hover {
  text-decoration: underline;
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
  .content {
    flex-direction: column;
  }

  .actions {
    flex-direction: row;
    justify-content: space-around;
  }

  .actions button {
    flex: 1;
    margin: 0 5px;
  }
}
</style>
