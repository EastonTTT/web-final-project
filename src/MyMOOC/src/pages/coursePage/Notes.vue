<!-- src/components/Notes.vue -->
<template>
  <div class="notes-section">
    <!-- 添加笔记表单 -->
    <div v-if="course.allow_notes" class="add-note">
      <h2>添加笔记</h2>
      <textarea v-model="newNoteContent" placeholder="请输入您的笔记..." rows="4"></textarea>
      <button @click="addNote" :disabled="!canAddNote">提交笔记</button>
    </div>

    <!-- 笔记列表 -->
    <div class="notes-list">
      <h2>我的笔记</h2>
      <div v-if="notes.length === 0">暂无笔记</div>
      <div v-else>
        <div v-for="note in notes" :key="note.note_id" class="note-item">
          <div class="note-header">
            <img :src="getImageUrl(note.user_image)" alt="User Avatar" class="user-avatar" />
            <span class="username">{{ note.username }}</span>
            <span class="note-time">{{ formatDate(note.create_at) }}</span>
            <button
              v-if="note.user_id === userInfo.user_id"
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
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axiosInstance from '@/utils/request/Axios.ts'
import { getLoginRecord } from '../homePage/login/LoginRecord'

// Props
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

// 新笔记内容
const newNoteContent = ref('')

// 笔记列表
const notes = ref([])

// 存储用户ID到用户名和头像的映射，避免重复请求
const userNamesMap = ref({})

// 获取当前用户的所有笔记
const fetchNotes = async () => {
  try {
    const response = await axiosInstance.get(`/notes/course/${props.courseId}/user/${userInfo.value.user_id}`)
    console.log('Fetch Notes Response:', response)

    if (response.status === 200 && response.data.status === 200) {
      const fetchedNotes = response.data.data

      // 为每条笔记获取用户名，并设置默认头像
      for (const note of fetchedNotes) {
        if (!note.user_id) {
          // 处理没有 user_id 的笔记
          note.username = '未知用户'
          note.user_image = 'default_avatar.jpg'
          continue
        }

        if (!userNamesMap.value[note.user_id]) {
          try {
            const userResponse = await axiosInstance.get(`/username/${note.user_id}`)
            console.log(userResponse)
            if (userResponse.status === 200 && userResponse.data.status === 200) {
              userNamesMap.value[note.user_id] = {
                username: userResponse.data.message,
                user_image: '20241203155134.jpg' // 始终使用默认头像
              }
            } else {
              userNamesMap.value[note.user_id] = {
                username: '未知用户',
                user_image: '20241203155134.jpg' // 默认头像
              }
            }
          } catch (err) {
            console.error(`获取用户 ${note.user_id} 信息时发生错误:`, err.message)
            userNamesMap.value[note.user_id] = {
              username: '未知用户',
              user_image: '20241203155134.jpg' // 默认头像
            }
          }
        }

        note.username = userNamesMap.value[note.user_id].username
        note.user_image = userNamesMap.value[note.user_id].user_image
      }

      notes.value = fetchedNotes
    } else {
      console.error('获取笔记失败:', response.data.message)
    }
  } catch (err) {
    console.error('获取笔记时发生错误:', err.message)
  }
}

// 添加笔记
const addNote = async () => {
  if (!newNoteContent.value.trim()) {
    alert('笔记内容不能为空')
    return
  }
  try {
    const payload = {
      course_id: props.courseId,
      user_id: userInfo.value.user_id, // 确保包含 user_id
      content: newNoteContent.value
    }
    const response = await axiosInstance.post(`/notes/action?action=add`, payload)
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
  if (!confirm('确定要删除这条笔记吗？')) {
    return
  }
  try {
    const payload = {
      note_id: noteId
    }
    const response = await axiosInstance.post(`/notes/action`, payload, {
      params: {
        action: 'delete',
        userId: userInfo.value.user_id
      }
    })
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

// 检查是否可以添加笔记
const canAddNote = computed(() => {
  return userInfo.value.isLogged && props.course.allow_notes
})

onMounted(() => {
  fetchNotes()
})
</script>

<style scoped lang="less">
.notes-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.add-note {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.add-note textarea {
  width: 100%;
  padding: 10px;
  resize: vertical;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.add-note button {
  align-self: flex-end;
  padding: 8px 16px;
  background-color: #28a745;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.add-note button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.notes-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.note-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  background-color: #f9f9f9;
}

.note-header {
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

.note-time {
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

.note-content {
  font-size: 1em;
  color: #555;
}
</style>
