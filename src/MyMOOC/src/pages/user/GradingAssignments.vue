<template>
  <div class="grading-assignments-container">
    <h2>作业批改</h2>
    <div v-if="loading" class="loading">加载中...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="submissions.length === 0" class="no-submissions">
      当前没有需要批改的作业。
    </div>
    <div v-else>
      <el-table :data="paginatedSubmissions" style="width: 100%" stripe>
        <el-table-column prop="submission_id" label="提交ID" width="100"></el-table-column>
        <el-table-column prop="assignment_title" label="作业名称"></el-table-column>
        <el-table-column prop="student_name" label="学生姓名"></el-table-column>
        <el-table-column prop="course_name" label="所属课程"></el-table-column>
        <el-table-column prop="submit_time" label="提交时间"></el-table-column>
        <el-table-column label="分数" width="150">
          <template #default="scope">
            <span>
              <span v-if="scope.row.grade !== null && scope.row.grade !== undefined">
                <el-tag :type="getGradeTagType(scope.row.grade)">
                  {{ scope.row.grade }}分 ({{ getEvaluation(scope.row.grade) }})
                </el-tag>
              </span>
              <span v-else>
                <el-tag type="info">未评分</el-tag>
              </span>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="openGradeModal(scope.row)"
            >
              批改
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        style="margin-top: 20px; text-align: center;"
        @current-change="handlePageChange"
        :current-page="currentPage"
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="submissions.length">
      </el-pagination>
    </div>

    <!-- 批改作业模态框 -->
    <el-dialog
      title="批改作业"
      v-model="showGradeModal"
      width="50%">
      <div v-if="selectedSubmission">
        <p><strong>作业名称：</strong>{{ selectedSubmission.assignment_title }}</p>
        <p><strong>学生姓名：</strong>{{ selectedSubmission.student_name }}</p>
        <p><strong>所属课程：</strong>{{ selectedSubmission.course_name }}</p>
        <p><strong>提交时间：</strong>{{ formatDate(selectedSubmission.submit_time) }}</p>
        <p><strong>作业内容：</strong></p>
        <p>{{ selectedSubmission.text_content }}</p>
        <p><strong>附件：</strong>
        <a v-if="selectedSubmission.content_url"
          :href="getFileDownloadLink(selectedSubmission.content_url)"
          target="_blank"
          download>
          {{ getFileName(selectedSubmission.content_url) }}
        </a>
        <span v-else>无</span>
        </p>
        <el-form :model="gradeForm" label-width="80px">
          <el-form-item label="分数" :required="true">
            <el-input-number v-model="gradeForm.grade" :min="0" :max="100" label="分数"></el-input-number>
          </el-form-item>
          <el-form-item label="反馈">
            <el-input type="textarea" v-model="gradeForm.feedback" placeholder="请输入反馈"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeModal">取消</el-button>
          <el-button type="primary" @click="submitGrade">提交</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import axiosInstance from '@/utils/request/Axios.ts';
import {
  ElTable,
  ElTableColumn,
  ElButton,
  ElPagination,
  ElDialog,
  ElTag,
  ElForm,
  ElFormItem,
  ElInput,
  ElInputNumber
} from 'element-plus';
import 'element-plus/dist/index.css';
import { getLoginRecord } from '../homePage/login/LoginRecord'; // 确保路径正确

// 用户信息（假设从登录记录获取教师ID）
const userInfo = ref(getLoginRecord());

// 响应式变量
interface Submission {
  submission_id: number;
  assignment_id: number;
  student_id: number;
  submit_time: string;
  content_url: string | null;
  text_content: string | null;
  grade: number | null;
  feedback: string | null;
  assignment_title: string;
  course_name: string;
  student_name: string;
}

const submissions = ref<Submission[]>([]);
const loading = ref(true);
const error = ref<string | null>(null);

// 分页变量
const currentPage = ref(1);
const pageSize = ref(10);

// 批改作业模态框控制
const showGradeModal = ref(false);
const selectedSubmission = ref<Submission | null>(null);

// 批改表单数据
const gradeForm = ref({
  grade: undefined as number | undefined,
  feedback: ''
});


// 获取文件下载链接
const getFileDownloadLink = (fileUrl: string) => {
  // 如果存储在服务器的路径是相对路径，这里将其转为完整的URL
  return `http://localhost:5173/src/assets/assignmentSubmissions/${fileUrl}`;
};

// 获取文件名
const getFileName = (fileUrl: string) => {
  // 从文件路径中提取文件名
  const parts = fileUrl.split('/');
  return parts[parts.length - 1];
};

// 缓存学生姓名
const studentNameCache: Record<number, string> = {};

// 获取教师所有作业提交记录
const fetchSubmissions = async () => {
  try {
    console.log('Fetching courses for teacher ID:', userInfo.value.user_id);
    const coursesResponse = await axiosInstance.get(`/courses/teacher/${userInfo.value.user_id}`);
    console.log('Courses Response:', coursesResponse);

    if (coursesResponse.status === 200 && coursesResponse.data.status === 200 && Array.isArray(coursesResponse.data.data)) {
      const courses = coursesResponse.data.data;

      if (courses.length === 0) {
        submissions.value = [];
        loading.value = false;
        return;
      }

      // 创建课程ID到课程名称的映射
      const courseNameMap: Record<number, string> = {};
      courses.forEach((course: any) => {
        courseNameMap[course.course_id] = course.course_name;
      });

      // 2. 获取每个课程的作业
      console.log('Fetching assignments for each course...');
      const assignmentPromises = courses.map((course: any) =>
        axiosInstance.get(`/assignments/course/${course.course_id}`).then(res => {
          console.log(`Assignments Response for Course ID ${course.course_id}:`, res);
          if (res.status === 200 && res.data.status === 200 && Array.isArray(res.data.data)) {
            return res.data.data;
          } else {
            console.error(`获取课程ID ${course.course_id} 的作业失败: ${res.data.message}`);
            return [];
          }
        }).catch(err => {
          console.error(`请求课程ID ${course.course_id} 的作业失败:`, err.message);
          return [];
        })
      );

      const assignmentsList = await Promise.all(assignmentPromises);
      const allAssignments = assignmentsList.flat();
      console.log('作业:', allAssignments);

      if (allAssignments.length === 0) {
        submissions.value = [];
        loading.value = false;
        return;
      }

      // 3. 获取每个作业的提交记录
      console.log('Fetching submissions for each assignment...');
      const submissionPromises = allAssignments.map((assignment: any) =>
        axiosInstance.get(`/assignment-submissions/assignment/${assignment.assignment_id}`).then(res => {
          console.log(`Submissions Response for Assignment ID ${assignment.assignment_id}:`, res);
          if (res.status === 200 && res.data.status === 200 && Array.isArray(res.data.data)) {
            return res.data.data.map((sub: any) => ({
              ...sub,
              assignment_title: assignment.title,
              course_name: courseNameMap[assignment.course_id] || '未知课程',
              submit_time: sub.submit_time,
              student_id: sub.student_id // 确保包含 student_id
            }));
          } else {
            console.error(`获取作业ID ${assignment.assignment_id} 的提交记录失败: ${res.data.message}`);
            return [];
          }
        }).catch(err => {
          console.error(`请求作业ID ${assignment.assignment_id} 的提交记录失败:`, err.message);
          return [];
        })
      );

      const submissionsList = await Promise.all(submissionPromises);
      const allSubmissions = submissionsList.flat();
      console.log('All Submissions:', allSubmissions);

      // 提取唯一的 student_id
      const uniqueStudentIds = Array.from(new Set(allSubmissions.map(sub => sub.student_id)));
      console.log('Unique Student IDs:', uniqueStudentIds);

      // 批量获取 student_name
      const studentNamePromises = uniqueStudentIds.map(id => {
        if (studentNameCache[id]) {
          console.log(`Using cached name for student ID ${id}: ${studentNameCache[id]}`);
          return Promise.resolve({ id, name: studentNameCache[id] });
        } else {
          return axiosInstance.get(`/username/${id}`).then(res => {
            console.log(`Username Response for ID ${id}:`, res);
            if (res.status === 200 && res.data.status === 200) {
              studentNameCache[id] = res.data.message;
              return { id, name: res.data.message };
            } else {
              console.error(`获取用户ID ${id} 的姓名失败: ${res.data.message}`);
              studentNameCache[id] = '未知学生';
              return { id, name: '未知学生' };
            }
          }).catch(err => {
            console.error(`请求用户ID ${id} 的姓名失败:`, err.message);
            studentNameCache[id] = '未知学生';
            return { id, name: '未知学生' };
          });
        }
      });

      const studentNames = await Promise.all(studentNamePromises);
      console.log('Fetched Student Names:', studentNames);

      const studentNameMap: Record<number, string> = {};
      studentNames.forEach(({ id, name }) => {
        studentNameMap[id] = name;
      });

      // 将 student_name 添加到每个提交记录中
      const submissionsWithNames = allSubmissions.map(sub => ({
        ...sub,
        student_name: studentNameMap[sub.student_id] || '未知学生'
      }));
      console.log('All Submissions with Names:', submissionsWithNames);
      submissions.value = submissionsWithNames;
    } else {
      error.value = coursesResponse.data.message || '获取课程列表失败';
      console.error('获取课程列表失败:', coursesResponse.data.message);
    }
  } catch (err: any) {
    error.value = err.message || '获取作业提交记录失败';
    console.error('获取作业提交记录时发生错误:', err.message);
  } finally {
    loading.value = false;
  }
};

// 处理分页
const paginatedSubmissions = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return submissions.value.slice(start, end);
});

// 处理页码变化
const handlePageChange = (page: number) => {
  currentPage.value = page;
};

// 打开批改模态框
const openGradeModal = (submission: Submission) => {
  console.log('Opening grade modal for submission:', submission);
  selectedSubmission.value = submission;
  gradeForm.value.grade = submission.grade ?? undefined;
  gradeForm.value.feedback = submission.feedback || '';
  showGradeModal.value = true;
};

// 关闭模态框
const closeModal = () => {
  console.log('Closing grade modal');
  showGradeModal.value = false;
  selectedSubmission.value = null;
  gradeForm.value.grade = undefined;
  gradeForm.value.feedback = '';
};

// 提交评分和反馈
const submitGrade = async () => {
  if (gradeForm.value.grade === undefined) {
    alert('请填写分数');
    return;
  }

  try {
    console.log('Submitting grade for Submission ID:', selectedSubmission.value?.submission_id);
    console.log('Grade Data:', gradeForm.value);

    const response = await axiosInstance.post('/assignment-submissions/grade', null, {
      params: {
        submission_id: selectedSubmission.value?.submission_id,
        grade: gradeForm.value.grade,
        feedback: gradeForm.value.feedback
      }
    });

    console.log('Grade Response:', response);

    if (response.status === 200 && response.data.status === 200) {
      alert('作业批改成功');
      // 刷新提交记录
      await fetchSubmissions();
      closeModal();
    } else {
      alert(response.data.message || '作业批改失败');
    }
  } catch (err: any) {
    console.error('批改作业时发生错误:', err.message);
    alert('作业批改失败');
  }
};

// 根据分数获取评价等级
const getEvaluation = (score: number) => {
  if (score >= 90) return '优秀';
  if (score >= 80) return '良好';
  if (score >= 70) return '中等';
  if (score >= 60) return '及格';
  return '不及格';
};

// 根据分数获取标签类型
const getGradeTagType = (score: number) => {
  if (score >= 90) return 'success';
  if (score >= 80) return 'info';
  if (score >= 70) return 'warning';
  if (score >= 60) return 'danger';
  return 'danger';
};

// 格式化日期时间
const formatDate = (datetime: string) => {
  const date = new Date(datetime);
  return date.toLocaleString();
};

// 初始化数据
onMounted(() => {
  fetchSubmissions();
});
</script>

<style scoped lang="less">
.grading-assignments-container {
  padding: 40px;
  background-color: #ffffff;
  min-height: 100vh;
  font-family: "Microsoft Yahei", sans-serif;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #34495e;
  font-size: 2.5em;
  font-weight: 700;
}

.loading,
.error,
.no-submissions {
  text-align: center;
  font-size: 1.3em;
  margin-top: 50px;
}

.error {
  color: #e74c3c;
}

.no-submissions {
  color: #555;
}

.el-table {
  margin-bottom: 30px;
}

.dialog-footer {
  text-align: right;
}

.el-dialog__header {
  background-color: #3498db;
  color: #fff;
  border-bottom: none;
}

.el-dialog__body {
  padding: 20px;
}

.el-dialog__footer {
  border-top: none;
}

@media (max-width: 1024px) {
  .grading-assignments-container {
    padding: 30px;
  }

  h2 {
    font-size: 2.2em;
    margin-bottom: 25px;
  }
}

@media (max-width: 768px) {
  .grading-assignments-container {
    padding: 20px;
  }

  h2 {
    font-size: 2em;
    margin-bottom: 20px;
  }

  .el-table {
    font-size: 14px;
  }

  .el-dialog__body {
    padding: 15px;
  }

  .el-dialog__footer {
    flex-direction: column;
    align-items: stretch;
  }

  .el-dialog__footer button {
    width: 100%;
    margin-bottom: 10px;
  }
}
</style>
