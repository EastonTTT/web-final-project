<template>
  <div class="courseware-management">
    <h2>课件管理</h2>

    <!-- 课程选择 -->
    <el-select
      v-model="selectedCourse"
      placeholder="请选择课程"
      style="width: 300px; margin-bottom: 20px;"
      @change="handleCourseChange"
      clearable
    >
      <el-option
        v-for="course in courses"
        :key="course.course_id"
        :label="course.course_name"
        :value="course.course_id"
      ></el-option>
    </el-select>

    <!-- 上传课件区域 -->
    <div class="upload-section" v-if="selectedCourseObject">
      <h4>上传课件到课程：{{ selectedCourseObject.course_name }}</h4>
      <el-upload
        class="upload-demo"
        :auto-upload="false"
        :file-list="fileList"
        multiple
        :on-change="handleChange"
        :before-upload="beforeUpload"
        :accept="acceptedFormats"
        drag
        :limit="1000"
        :on-remove="handleRemove"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">只能上传 PDF、PPT、Word 文件，且总大小不超过 10MB</div>
      </el-upload>
      <el-button type="success" @click="submitUpload" style="margin-top: 10px;">提交上传</el-button>
    </div>

    <!-- 课件列表 -->
    <div class="courseware-list" v-if="courseWares.length > 0">
      <h4>课程课件列表</h4>
      <el-table :data="courseWares" style="width: 100%">
        <!-- 已移除课件ID列 -->
        <el-table-column prop="material_name" label="课件名称"></el-table-column>
        <el-table-column prop="file_path" label="文件">
          <template #default="scope">
            <a :href="getDownloadUrl(scope.row.file_path)" target="_blank">点击下载</a>
          </template>
        </el-table-column>
        <el-table-column prop="created_at" label="创建时间"></el-table-column>
        <el-table-column prop="updated_at" label="更新时间"></el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleUpdate(scope.row)">更新</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.material_id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>


    <!-- 更新课件弹窗 -->
    <el-dialog title="更新课件" v-model="updateDialogVisible">
      <el-form :model="updateForm" label-width="120px">
        <el-form-item label="课件ID">
          <el-input v-model="updateForm.material_id" disabled></el-input>
        </el-form-item>
        <el-form-item label="课件名称">
          <el-input v-model="updateForm.material_name"></el-input>
        </el-form-item>
        <!-- 如果不需要更新 file_path，可以将其设为只读或移除 -->
        <el-form-item label="文件路径">
          <el-input v-model="updateForm.file_path" disabled></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="updateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitUpdate">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>


<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue';
import {
  ElUpload,
  ElButton,
  ElSelect,
  ElOption,
  ElTable,
  ElTableColumn,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElMessage,
  ElMessageBox
} from 'element-plus';
import axiosInstance from '@/utils/request/Axios.ts';
import { getLoginRecord } from '../homePage/login/LoginRecord'; // 确保路径正确

interface CourseDTO {
  course_id: number;
  course_name: string;
  // 其他必要字段
}

interface CourseWareDTO {
  material_id: number | null;
  course_id: number | null;
  material_name: string;
  file_path: string;
  created_at?: string | null;
  updated_at?: string | null;
}

export default defineComponent({
  name: 'CoursewareManagement',
  components: {
    ElUpload,
    ElButton,
    ElSelect,
    ElOption,
    ElTable,
    ElTableColumn,
    ElDialog,
    ElForm,
    ElFormItem,
    ElInput,
  },
  setup() {
    const loginRecord = getLoginRecord(); // 获取登录记录，包含教师ID

    const courses = ref<CourseDTO[]>([]);
    const selectedCourse = ref<number | null>(null);
    const selectedCourseObject = ref<CourseDTO | null>(null);
    const fileList = ref<any[]>([]);
    const courseWares = ref<CourseWareDTO[]>([]);

    const updateDialogVisible = ref(false);
    const updateForm = ref<CourseWareDTO>({
      material_id: null,
      course_id: null,
      material_name: '',
      file_path: '',
      created_at: null,
      updated_at: null
    });

    const acceptedFormats = '.pdf,.ppt,.pptx,.doc,.docx'; // 允许上传的文件格式

    // 获取当前教师发布的所有课程
    const fetchCourses = async () => {
      console.log('正在获取教师ID为 ' + loginRecord.user_id + ' 的课程');
      try {
        const response = await axiosInstance.get(`/courses/teacher/${loginRecord.user_id}`);
        if (response.status === 200) {
          courses.value = response.data.data;
          console.log('课程获取成功:', courses.value);
        } else {
          ElMessage.error('获取课程列表失败: ' + response.data.message);
          console.error('获取课程失败:', response.data.message);
        }
      } catch (error: any) {
        ElMessage.error('获取课程列表失败');
        console.error('获取课程列表时出错:', error);
      }
    };

    // 处理课程选择变化
    const handleCourseChange = (courseId: number | null) => {
      if (courseId !== null) {
        const course = courses.value.find(c => c.course_id === courseId) || null;
        selectedCourseObject.value = course;
        console.log('选择的课程:', course);
        fetchCourseWares(courseId);
      } else {
        selectedCourseObject.value = null;
        courseWares.value = [];
      }
    };

    // 获取选定课程的课件
    const fetchCourseWares = async (courseId: number) => {
      console.log('正在获取课程ID为 ' + courseId + ' 的课件');
      try {
        const response = await axiosInstance.get(`/courseware/course/${courseId}`);
        if (response.status === 200) {
          // 根据后端返回的数据结构调整
          // 假设后端返回的数据直接是课件数组
          courseWares.value = response.data.data || response.data;
          console.log('课件获取成功:', courseWares.value);
        } else {
          courseWares.value = [];
          ElMessage.error('未找到指定课程的课件: ' + response.data.message);
          console.error('获取课件失败:', response.data.message);
        }
      } catch (error: any) {
        courseWares.value = [];
        ElMessage.error('获取课件失败');
        console.error('获取课件时出错:', error);
      }
    };

    // 处理文件变化
    const handleChange = (file: any, newFileList: any[]) => {
      fileList.value = newFileList;
      console.log('文件列表更新:', fileList.value);
    };

    // 处理文件移除
    const handleRemove = (file: any, fileList: any[]) => {
      fileList.value = fileList;
      console.log('移除文件:', file.name);
    };

    // 上传前的钩子，可以进行文件格式和大小的验证
    const beforeUpload = (file: File) => {
      const isAllowedFormat = [
        'application/pdf',
        'application/vnd.ms-powerpoint',
        'application/vnd.openxmlformats-officedocument.presentationml.presentation',
        'application/msword',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
      ].includes(file.type);
      if (!isAllowedFormat) {
        ElMessage.error('只能上传 PDF、PPT、Word 文件');
        return false;
      }
      // 如果需要限制单个文件大小，可以在这里添加
      return true;
    };

    // 提交上传
    const submitUpload = async () => {
      if (fileList.value.length === 0) {
        ElMessage.warning('请先选择要上传的课件');
        return;
      }
      if (selectedCourse.value === null) {
        ElMessage.warning('请先选择一个课程');
        return;
      }

      // 计算总大小
      const totalSizeMB = fileList.value.reduce((sum, file) => sum + (file.size || 0), 0) / 1024 / 1024;
      if (totalSizeMB > 10) {
        ElMessage.error('所有文件的总大小不能超过 10MB');
        return;
      }

      console.log('正在提交上传，课程ID:', selectedCourse.value);

      if (fileList.value.length === 1) {
        // 单个文件上传
        const file = fileList.value[0].raw;
        const filePath = `courseWare/${file.name}`;
        const payload: Omit<CourseWareDTO, 'created_at' | 'updated_at'> = {
          course_id: selectedCourse.value,
          file_path: filePath,
          material_id: null,
          material_name: file.name
        };

        try {
          const response = await axiosInstance.post('/courseware', payload, {
            headers: {
              'Content-Type': 'application/json'
            }
          });
          if (response.status === 200 && response.data.status === 200) {
            ElMessage.success(`课件 "${file.name}" 上传成功`);
            console.log(`上传成功: ${filePath}`);
            fileList.value = [];
            fetchCourseWares(selectedCourse.value);
          } else {
            ElMessage.error(`课件 "${file.name}" 上传失败: ${response.data.message}`);
            console.error(`上传失败 (${filePath}):`, response.data.message);
          }
        } catch (error: any) {
          ElMessage.error(`课件 "${file.name}" 上传失败`);
          console.error(`上传时出错 (${filePath}):`, error);
        }
      } else {
        // 批量上传
        const payload: Omit<CourseWareDTO, 'created_at' | 'updated_at'>[] = fileList.value.map(fileItem => {
          const file = fileItem.raw;
          return {
            course_id: selectedCourse.value,
            file_path: `courseWare/${file.name}`,
            material_id: null,
            material_name: file.name
          };
        });

        try {
          const response = await axiosInstance.post('/courseware/batch', payload, {
            headers: {
              'Content-Type': 'application/json'
            }
          });
          if (response.status === 200 && response.data.status === 200) {
            ElMessage.success('课件批量上传成功');
            console.log('批量上传成功');
            fileList.value = [];
            fetchCourseWares(selectedCourse.value);
          } else {
            ElMessage.error('课件批量上传失败: ' + response.data.message);
            console.error('批量上传失败:', response.data.message);
          }
        } catch (error: any) {
          ElMessage.error('课件批量上传失败');
          console.error('批量上传时出错:', error);
        }
      }
    };

    // 处理删除课件
    const handleDelete = async (materialId: number) => {
      try {
        await ElMessageBox.confirm('确定要删除该课件吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
        console.log('正在删除课件ID:', materialId);
        const response = await axiosInstance.post('/courseware/delete', materialId, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        if (response.status === 200 && response.data.status === 200) {
          ElMessage.success('课件删除成功');
          console.log('删除成功:', materialId);
          if (selectedCourse.value !== null) {
            fetchCourseWares(selectedCourse.value);
          }
        } else {
          ElMessage.error('课件删除失败: ' + response.data.message);
          console.error('删除失败:', response.data.message);
        }
      } catch (error: any) {
        if (error !== '取消') { // 排除用户取消操作
          ElMessage.error('课件删除失败');
          console.error('删除时出错:', error);
        }
      }
    };

    // 处理更新课件
    const handleUpdate = (courseWare: CourseWareDTO) => {
      updateForm.value = { ...courseWare };
      updateDialogVisible.value = true;
      console.log('准备更新课件:', courseWare);
    };

    // 提交更新课件
    const submitUpdate = async () => {
      if (!updateForm.value.material_id) {
        ElMessage.warning('更新课件时必须提供课件ID');
        return;
      }
      if (!updateForm.value.material_name) {
        ElMessage.warning('课件名称不能为空');
        return;
      }

      // 构建 Payload，仅包含必要的字段
      const payload: Partial<CourseWareDTO> = {
        material_id: updateForm.value.material_id,
        course_id: updateForm.value.course_id,
        material_name: updateForm.value.material_name,
        file_path: updateForm.value.file_path // 如果不需要更新 file_path，可以移除这一行
      };

      console.log('正在提交更新的课件:', payload);
      try {
        const response = await axiosInstance.post('/courseware/update', payload, {
          headers: {
            'Content-Type': 'application/json'
          }
        });
        console.log('更新响应:', response.data);
        if (response.status === 200 && response.data.status === 200) { // 根据后端实际返回的字段调整
          ElMessage.success('课件更新成功');
          console.log('更新成功:', payload);
          updateDialogVisible.value = false;
          if (selectedCourse.value !== null) {
            fetchCourseWares(selectedCourse.value);
          }
        } else {
          ElMessage.error('课件更新失败: ' + response.data.message);
          console.error('更新失败:', response.data.message);
        }
      } catch (error: any) {
        ElMessage.error('课件更新失败');
        console.error('更新时出错:', error);
      }
    };

    // 获取下载链接
    const getDownloadUrl = (filePath: string) => {
      // 确保路径以 '/' 开头，形成绝对路径
      return `/src/assets/${filePath}`; // 根据您的项目结构调整
      // 如果项目中 assets 直接在根目录，可以使用 `/assets/${filePath}`
    };

    onMounted(() => {
      fetchCourses();
    });

    return {
      courses,
      selectedCourse,
      selectedCourseObject,
      fileList,
      courseWares,
      handleCourseChange,
      handleChange,
      handleRemove,
      beforeUpload,
      submitUpload,
      handleDelete,
      handleUpdate,
      updateDialogVisible,
      updateForm,
      submitUpdate,
      acceptedFormats,
      getDownloadUrl
    };
  }
});
</script>

<style scoped>
.courseware-management {
  padding: 20px;
}

h2 {
  margin-bottom: 20px;
  text-align: center;
  color: #333;
}

.upload-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background-color: #f9f9f9;
}

.upload-section h4 {
  margin-bottom: 15px;
}

.courseware-list {
  margin-top: 20px;
}

.courseware-list h4 {
  margin-bottom: 15px;
}

.dialog-footer {
  text-align: right;
}

/* 美化上传组件 */
.upload-demo i.el-icon-upload {
  font-size: 28px;
  color: #409EFF;
}

.upload-demo .el-upload__text em {
  color: #409EFF;
}

.el-upload__tip {
  font-size: 12px;
  color: #999;
}

/* 表格样式优化 */
.el-table .cell a {
  color: #409EFF;
  text-decoration: none;
}

.el-table .cell a:hover {
  text-decoration: underline;
}

/* 更新弹窗样式 */
.el-dialog {
  max-width: 500px;
}

.el-dialog__footer {
  text-align: right;
}
</style>
