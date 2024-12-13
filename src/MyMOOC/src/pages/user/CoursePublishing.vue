<!-- 发布课程 -->

<template>
  <div class="container">
    <h1 class="title">发布新课程</h1>
    <form class="form" @submit.prevent="handleSubmit">
      <div class="form-group">
        <label>课程名称 <span class="required">*</span>：</label>
        <input v-model="form.course_name" type="text" required />
      </div>

      <div class="form-group">
        <label>课程简介 <span class="required">*</span>：</label>
        <textarea v-model="form.course_description" required></textarea>
      </div>

      <div class="form-group">
        <label>课程图片 <span class="required">*</span>：</label>
        <input type="file" @change="handleFileChange" accept="image/*" required />
      </div>

      <div class="form-group">
        <label>课程类型 <span class="required">*</span>：</label>
        <select v-model="form.course_type" required>
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

      <div class="checkbox-group">
        <div class="checkbox-item">
          <label>开启评论区 <span class="required">*</span>：</label>
          <input type="checkbox" v-model="form.allow_comments" />
        </div>
        <div class="checkbox-item">
          <label>开启笔记区 <span class="required">*</span>：</label>
          <input type="checkbox" v-model="form.allow_notes" />
        </div>
      </div>

      <div class="form-group">
        <label>课程状态 <span class="required">*</span>：</label>
        <select v-model="form.status" required>
          <option value="未开课">未开课</option>
          <option value="已开课">已开课</option>
          <option value="已结束">已结束</option>
        </select>
      </div>

      <div class="form-group">
        <label>课程开始时间 <span class="required">*</span>：</label>
        <input type="datetime-local" v-model="form.start_time" required/>
      </div>

      <div class="form-group">
        <label>课程结束时间 <span class="required">*</span>：</label>
        <input type="datetime-local" v-model="form.end_time" required/>
      </div>

      <button type="submit" class="submit-btn">发布课程</button>
    </form>
  </div>
</template>

<script lang="ts">
import { ref } from 'vue'
import axiosInstance from '@/utils/request/Axios.ts';
import { getLoginRecord } from '../homePage/login/LoginRecord';
import { getLineAndCharacterOfPosition } from 'typescript';

const loginRecord = getLoginRecord();
export default {
  setup(){
    const form = ref({
      course_name: '',
      course_description: '',
      course_image: '', // 最终只存文件相对路径，如：asset/image/filename.png
      course_type: '计算机',
      allow_comments: false,
      allow_notes: false,
      status: '未开课', // 默认状态为未开课
      start_time: '',
      end_time: ''
    });

    const handleFileChange = (event: Event) => {
      const input = event.target as HTMLInputElement;
      if (input.files && input.files.length > 0) {
        const file = input.files[0];
        const fileName = file.name;
        form.value.course_image = `course/${fileName}`;
      }
    }

    const handleSubmit = async () => {
      const courseData = {
        teacher_id: loginRecord.user_id,
        course_name: form.value.course_name,
        course_description: form.value.course_description,
        course_image: form.value.course_image,
        course_type: form.value.course_type,
        allow_comments: form.value.allow_comments,
        allow_notes: form.value.allow_notes,
        status: form.value.status,
        start_time: form.value.start_time ? new Date(form.value.start_time) : null,
        end_time: form.value.end_time ? new Date(form.value.end_time) : null,
      };
      const response = await axiosInstance.myPosting('/courses/action?action=add', courseData);

      if(response.status === 200 && response.data.status === 200){
        alert(response.data.message);
        location.reload();
      } else {
        alert('发布失败：' + response.data.message);
      }
    }

    return {
      form,
      handleSubmit,
      handleFileChange
    }
  }
}
</script>

<style scoped>
.container {
  max-width: 650px;
  margin: 60px auto;
  padding: 30px 40px;
  background: linear-gradient(135deg, #f0f9ff, #e0f7fa);
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0,0,0,0.1);
  font-family: "Microsoft Yahei", sans-serif;
}

.title {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 28px;
  font-weight: bold;
  letter-spacing: 1px;
}

.form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 20px;
  display: flex;
  flex-direction: column;
  position: relative;
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
  font-size: 15px;
  color: #333;
  font-weight: 500;
  margin-bottom: 0;
}

.required {
  color: red;
  margin-left: 5px;
  font-size: 14px;
}

label {
  margin-bottom: 8px;
  font-size: 15px;
  color: #333;
  font-weight: 500;
}

input,
textarea,
select {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.3s;
  font-family: inherit;
  background: #fff;
}

textarea {
  resize: vertical;
  min-height: 60px;
}

input:focus,
textarea:focus,
select:focus {
  border-color: #409eff;
  box-shadow: 0 0 5px rgba(64,158,255,0.3);
}

.submit-btn {
  padding: 12px 20px;
  font-size: 16px;
  font-weight: bold;
  color: #fff;
  background: #409eff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  align-self: flex-end;
  transition: background 0.3s;
}

.submit-btn:hover {
  background: #337ecc;
}

.submit-btn:disabled {
  background: #cccccc;
  cursor: not-allowed;
}
</style>
