<template>
  <div class="published-courses-container">
    <h1 class="title">已发布的课程</h1>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">加载中...</div>

    <!-- 课程列表 -->
    <div v-else>
      <div v-if="courses.length === 0" class="no-courses">
        您尚未发布任何课程。
      </div>

      <div v-else class="courses-list">
        <div v-for="course in courses" :key="course.course_id" class="course-item">
          <!-- 课程图片 -->
          <img :src="getImageUrl(course.course_image)" alt="课程图片" class="course-image" />

          <!-- 课程详情 -->
          <div class="course-details">
            <h2>{{ course.course_name }}</h2>
            <p>{{ course.course_description }}</p>
            <p><strong>类型:</strong> {{ course.course_type }}</p>
            <p><strong>状态:</strong> {{ course.status }}</p>
            <p><strong>开始时间:</strong> {{ formatDate(course.start_time) }}</p>
            <p><strong>结束时间:</strong> {{ formatDate(course.end_time) }}</p>

            <!-- 课程操作按钮 -->
            <div class="buttons">
              <button class="btn modify-btn" @click="openModifyModal(course)">修改课程</button>
              <button class="btn withdraw-btn" @click="withdrawCourse(course)">撤回发布</button>
              <button class="btn delete-btn" @click="deleteCourse(course)">删除课程</button>
              <button class="btn assign-btn" @click="openAssignModal(course)">布置作业</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 修改课程模态窗口 -->
    <div v-if="isModifyModalVisible" class="modal-overlay" @click.self="closeModifyModal">
      <div class="modal-content">
        <h2>修改课程</h2>
        <form @submit.prevent="submitCourseModification">
          <div class="form-group">
            <label>课程名称 <span class="required">*</span>：</label>
            <input v-model="currentCourse.course_name" type="text" required />
          </div>
          <div class="form-group">
            <label>课程简介 <span class="required">*</span>：</label>
            <textarea v-model="currentCourse.course_description" required></textarea>
          </div>
          <div class="form-group">
            <label>课程类型 <span class="required">*</span>：</label>
            <select v-model="currentCourse.course_type" required>
              <option value="理学·工学">理学·工学</option>
              <option value="计算机">计算机</option>
              <option value="教育·语言">教育·语言</option>
              <option value="文学·艺术">文学·艺术</option>
              <option value="创业·职场">创业·职场</option>
              <option value="哲史·文化">哲史·文化</option>
              <option value="经济·管理">经济·管理</option>
              <option value="医学">医学</option>
              <option value="心理学">心理学</option>
              <option value="社会·法律">社会·法律</option>
              <option value="农学">农学</option>
            </select>
          </div>
          <div class="form-group">
            <label>课程图片：</label>
            <input
              type="file"
              @change="handleModifyFileChange"
              accept="image/*"
              ref="modifyFileInput"
            />
            <!-- 显示预览和删除按钮 -->
            <div v-if="modifyImagePreview" class="image-preview">
              <img :src="modifyImagePreview" alt="课程图片预览" />
              <button type="button" @click="removeModifyImage" class="delete-btn">删除图片</button>
            </div>
          </div>
          <div class="checkbox-group">
            <div class="checkbox-item">
              <label>开启评论区：</label>
              <input type="checkbox" v-model="currentCourse.allow_comments" />
            </div>
            <div class="checkbox-item">
              <label>开启笔记区：</label>
              <input type="checkbox" v-model="currentCourse.allow_notes" />
            </div>
          </div>
          <div class="form-group">
            <label>课程状态 <span class="required">*</span>：</label>
            <select v-model="currentCourse.status" required>
              <option value="未开课">未开课</option>
              <option value="已开课">已开课</option>
              <option value="已结束">已结束</option>
            </select>
          </div>
          <div class="form-group">
            <label>课程开始时间 <span class="required">*</span>：</label>
            <input type="datetime-local" v-model="currentCourse.start_time" required/>
          </div>
          <div class="form-group">
            <label>课程结束时间 <span class="required">*</span>：</label>
            <input type="datetime-local" v-model="currentCourse.end_time" required/>
          </div>
          <div class="form-actions">
            <button type="button" class="btn cancel-btn" @click="closeModifyModal">取消</button>
            <button type="submit" class="btn submit-btn">保存修改</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 布置作业模态窗口 -->
    <div v-if="isAssignModalVisible" class="modal-overlay" @click.self="closeAssignModal">
      <div class="modal-content">
        <h2>布置作业 - {{ currentCourse.course_name }}</h2>
        <form @submit.prevent="submitAssignment">
          <div class="form-group">
            <label>作业标题 <span class="required">*</span>：</label>
            <input v-model="newAssignment.title" type="text" required />
          </div>
          <div class="form-group">
            <label>作业描述：</label>
            <textarea v-model="newAssignment.description"></textarea>
          </div>
          <div class="form-group">
            <label>截止日期 <span class="required">*</span>：</label>
            <input type="datetime-local" v-model="newAssignment.due_date" required/>
          </div>
          <div class="form-actions">
            <button type="button" class="btn cancel-btn" @click="closeAssignModal">取消</button>
            <button type="submit" class="btn submit-btn" >布置作业</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { ref, onMounted } from 'vue';
import axiosInstance from '@/utils/request/Axios.ts';
import { getLoginRecord } from '../homePage/login/LoginRecord';

const loginRecord = getLoginRecord();

// 定义 TypeScript 接口
interface Assignment {
  assignment_id: number;
  course_id: number;
  title: string;
  description: string;
  due_date: string; // 使用字符串存储日期时间
  create_at: string;
  updated_at: string;
}

interface Course {
  course_id: number;
  teacher_id: number;
  course_name: string;
  course_description: string;
  course_image: string; // 可以是URL或相对路径
  course_type: string;
  allow_comments: boolean;
  allow_notes: boolean;
  status: string;
  start_time: string; // 使用字符串存储日期时间
  end_time: string; // 使用字符串存储日期时间
}

interface NewAssignment {
  assignment_id: number;
  course_id: number;
  title: string;
  description: string;
  due_date: string;
  create_at: string;
  updated_at: string;
}

export default {
  name: 'PublishedCourses',
  setup() {
    // 响应式数据
    const courses = ref<Course[]>([]);
    const loading = ref<boolean>(true);

    // 模态窗口的显示状态
    const isModifyModalVisible = ref<boolean>(false);
    const isAssignModalVisible = ref<boolean>(false);

    // 当前操作的课程
    const currentCourse = ref<Course>({
      course_id: 0,
      teacher_id: 0,
      course_name: '',
      course_description: '',
      course_image: '',
      course_type: '',
      allow_comments: false,
      allow_notes: false,
      status: '',
      start_time: '',
      end_time: ''
    });

    // 新建作业的数据
    const newAssignment = ref<NewAssignment>({
      assignment_id: 0,
      course_id: 0,
      title: '',
      description: '',
      due_date: '',
      create_at: '',
      updated_at: ''
    });

    // Reactive variables for image preview in modify modal
    const modifyImagePreview = ref<string>('');

    // Reference to the modify file input
    const modifyFileInput = ref<HTMLInputElement | null>(null);

    // 根据老师id获取该老师发布的课程
    const fetchCourses = async () => {
      loading.value = true;
      try {
        const teacherID = loginRecord.user_id;
        const response = await axiosInstance.get(`/courses/teacher/${teacherID}`);
        if (response.status === 200 && response.data.status === 200) {
          courses.value = response.data.data;
        } else {
          alert('获取课程列表失败: ' + response.data.message);
        }
      } catch (error) {
        console.error(error);
        alert('获取课程列表时发生错误');
      } finally {
        loading.value = false;
      }
    };

    // 格式化日期
    const formatDate = (datetime: string) => {
      const date = new Date(datetime);
      return date.toLocaleString();
    };

    // 获取完整的图片 URL
    const getImageUrl = (relativeUrl: string) => {
      // 假设你的静态资源存放在服务器的 'assets' 目录下
      const baseUrl = 'http://localhost:5173/src/assets/';
      // 拼接基础路径与相对路径
      return relativeUrl ? `${baseUrl}${relativeUrl}` : 'default-image-url'; // 替换为默认图片URL
    };

    // 打开修改课程模态窗口
    const openModifyModal = (course: Course) => {
      currentCourse.value = { ...course };
      modifyImagePreview.value = getImageUrl(course.course_image);
      isModifyModalVisible.value = true;
    };

    // 关闭修改课程模态窗口
    const closeModifyModal = () => {
      isModifyModalVisible.value = false;
      // 清除修改图片预览
      modifyImagePreview.value = '';
      currentCourse.value.course_image = '';
      if (modifyFileInput.value) {
        modifyFileInput.value.value = ''; // 重置文件输入
      }
    };

    // 处理修改课程图片的文件变化
    const handleModifyFileChange = (event: Event) => {
      const input = event.target as HTMLInputElement;
      if (input.files && input.files.length > 0) {
        const file = input.files[0];
        const fileName = file.name;
        // 设置图片的相对路径
        currentCourse.value.course_image = `courses/${fileName}`;
        // 更新图片预览
        modifyImagePreview.value = getImageUrl(currentCourse.value.course_image);
      }
    };

    // 移除修改课程图片
    const removeModifyImage = () => {
      currentCourse.value.course_image = '';
      modifyImagePreview.value = '';
      if (modifyFileInput.value) {
        modifyFileInput.value.value = ''; // 重置文件输入
      }
    };

    // 提交课程修改
    const submitCourseModification = async () => {
      try {
        const courseData = {
          action: 'update',
          course_id: currentCourse.value.course_id,
          teacher_id: currentCourse.value.teacher_id,
          course_name: currentCourse.value.course_name,
          course_description: currentCourse.value.course_description,
          course_image: currentCourse.value.course_image, // 相对路径
          course_type: currentCourse.value.course_type,
          allow_comments: currentCourse.value.allow_comments,
          allow_notes: currentCourse.value.allow_notes,
          status: currentCourse.value.status,
          // 保持日期时间字符串不变，直接发送
          start_time: currentCourse.value.start_time || '',
          end_time: currentCourse.value.end_time || ''
        };

        console.log('Submitting Course Modification:', courseData);

        const response = await axiosInstance.myPosting('/courses/action?action=update', courseData);

        if (response.status === 200 && response.data.status === 200) {
          alert(response.data.message);
          closeModifyModal();
          await fetchCourses();
        } else {
          alert('修改课程失败: ' + response.data.message);
        }
      } catch (error) {
        console.error(error);
        alert('修改课程时发生错误');
      }
    };

    // 撤回发布课程（将状态改为"未开课"）
    const withdrawCourse = async (course: Course) => {
      if (!confirm(`确定要撤回发布课程 "${course.course_name}" 吗？`)) {
        return;
      }
      try {
        const updatedCourse = { ...course, status: '未开课' };
        // 保持日期时间字符串不变，直接发送
        updatedCourse.start_time = updatedCourse.start_time || '';
        updatedCourse.end_time = updatedCourse.end_time || '';

        const response = await axiosInstance.myPosting('/courses/action?action=update', updatedCourse);

        if (response.status === 200 && response.data.status === 200) {
          alert(response.data.message);
          await fetchCourses();
        } else {
          alert('撤回发布失败: ' + response.data.message);
        }
      } catch (error) {
        console.error(error);
        alert('撤回发布时发生错误');
      }
    };

    // 删除课程
    const deleteCourse = async (course: Course) => {
      if (!confirm(`确定要删除课程 "${course.course_name}" 吗？这将同时删除该课程的所有作业。`)) {
        return;
      }
      try {
        // 只需要 course_id 来删除课程
        const response = await axiosInstance.myPosting('/courses/action?action=delete', { course_id: course.course_id });

        if (response.status === 200 && response.data.status === 200) {
          alert(response.data.message);
          await fetchCourses();
        } else {
          alert('删除课程失败: ' + response.data.message);
        }
      } catch (error) {
        console.error(error);
        alert('删除课程时发生错误');
      }
    };

    // 打开布置作业模态窗口
    const openAssignModal = (course: Course) => {
      currentCourse.value = { ...course };
      newAssignment.value = {
        assignment_id: 0,
        course_id: course.course_id,
        title: '',
        description: '',
        due_date: '',
        create_at: '',
        updated_at: ''
      };
      isAssignModalVisible.value = true;
    };

    // 关闭布置作业模态窗口
    const closeAssignModal = () => {
      isAssignModalVisible.value = false;
    };

    // 提交新作业
    const submitAssignment = async () => {
      if (!newAssignment.value.title || !newAssignment.value.due_date) {
        alert('请填写作业标题和截止日期');
        return;
      }
      try {
        const assignmentData = {
          course_id: newAssignment.value.course_id,
          title: newAssignment.value.title,
          description: newAssignment.value.description,
          due_date: newAssignment.value.due_date
        };
        const response = await axiosInstance.myPosting('/assignments/action?action=add', assignmentData);
        if (response.status === 200 && response.data.status === 200) {
          alert(response.data.message);
          closeAssignModal();
          await fetchCourses();
        } else {
          alert('布置作业失败: ' + response.data.message);
        }
      } catch (error) {
        console.error(error);
        alert('布置作业时发生错误');
      }
    };

    // 在组件挂载时获取课程列表
    onMounted(async () => {
      await fetchCourses();
    });

    return {
      courses,
      loading,
      isModifyModalVisible,
      isAssignModalVisible,
      currentCourse,
      newAssignment,
      fetchCourses,
      formatDate,
      getImageUrl,
      openModifyModal,
      closeModifyModal,
      submitCourseModification,
      withdrawCourse,
      deleteCourse,
      openAssignModal,
      closeAssignModal,
      submitAssignment,
      modifyImagePreview,
      handleModifyFileChange,
      removeModifyImage,
      modifyFileInput
    };
  }
};
</script>

<style scoped>
.published-courses-container {
  padding: 20px;
  font-family: "Microsoft Yahei", sans-serif;
}

.title {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.loading,
.no-courses {
  text-align: center;
  color: #666;
  font-size: 18px;
}

.courses-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.course-item {
  display: flex;
  background: #f9f9f9;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.course-image {
  width: 150px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 20px;
}

.course-details {
  flex: 1;
}

.course-details h2 {
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.course-details p {
  margin: 5px 0;
  color: #555;
}

.buttons {
  margin-top: 10px;
  display: flex;
  gap: 10px;
}

.btn {
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  color: #fff;
  font-size: 14px;
  transition: background-color 0.3s;
}

.modify-btn {
  background-color: #3498db;
}

.modify-btn:hover {
  background-color: #2980b9;
}

.withdraw-btn {
  background-color: #f39c12;
}

.withdraw-btn:hover {
  background-color: #e67e22;
}

.delete-btn {
  background-color: #e74c3c;
}

.delete-btn:hover {
  background-color: #c0392b;
}

.assign-btn {
  background-color: #2ecc71;
}

.assign-btn:hover {
  background-color: #27ae60;
}

/* 修改课程模态窗口样式 */
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
  background: #fff;
  padding: 25px;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.modal-content h2 {
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #333;
}

.required {
  color: red;
  margin-left: 5px;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.form-group textarea {
  resize: vertical;
  min-height: 60px;
}

.checkbox-group {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.checkbox-item {
  display: flex;
  align-items: center;
}

.checkbox-item label {
  margin-right: 10px;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.cancel-btn {
  background-color: #95a5a6;
}

.cancel-btn:hover {
  background-color: #7f8c8d;
}

.submit-btn {
  background-color: #27ae60;
}

.submit-btn:hover {
  background-color: #229954;
}

/* 新增样式 */

.image-preview {
  margin-top: 10px;
  display: flex;
  align-items: center;
}

.image-preview img {
  max-width: 100px;
  max-height: 100px;
  margin-right: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.delete-btn {
  padding: 5px 10px;
  font-size: 14px;
  color: #fff;
  background-color: #ff4d4f;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.delete-btn:hover {
  background-color: #d9363e;
}
</style>
