<template>
  <div class="mytable">
    <el-table :data="tableData" style="width: 100%" height="500" :row-style="{height: '100px'}">
      <el-table-column prop="image" label="课程图片" width="200">
        <template v-slot="{ row }">
          <img :src="row.image" :alt="row.image" style="height: 100px;">
        </template>
      </el-table-column>
      <el-table-column prop="status" label="是否推荐" width="150">
        <template v-slot="{ row }">
          <el-tag effect="dark" round :type="getType(row.status)" size="large">{{ getStatus(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="date" label="开课日期" width="150" />
      <el-table-column fixed prop="courseName" label="课程名称" width="150" />
      <el-table-column prop="courseType" label="课程类别" width="120" />
      <el-table-column fixed="right" label="执行操作" min-width="200">
        <template v-slot="scope">
          <el-button type="primary" size="small" @click="handleRecommend(scope.row.courseName)" plain>
            推荐课程
          </el-button>
          <!-- <el-button type="danger">Danger</el-button> -->
          <el-button type="danger" size="small" @click="handleCancel(scope.row.courseName)" plain>取消推荐</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

</template>

<script lang="ts" setup>

import MyAxios from '@/utils/request/Axios';
import {ref} from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';

interface dataType{
  date: string | null,
  courseName: string | null,
  image: string | null,
  courseType: string | null,
  status: number | null

}

let tableData = ref<dataType[]>([]);

getAllCourses();

async function getAllCourses(){
  try{
    const response = await MyAxios.myGetting('/courses');
    if(response.status === 200){
      MessagePlugin.success('课程信息查询成功');
      tableData.value = response.data.map(item => ({
      date: new Date(item.start_time).toLocaleDateString(),
      courseName: item.course_name,
      image: '/assets/' + item.course_image,
      courseType: item.course_type,
      status: item.is_recommended,
  }));
    }else{
      MessagePlugin.error('查询课程信息：' + response.message)

    }
  }catch(error){
    console.log('查询课程信息失败...');
    MessagePlugin.error('查询课程信息失败')

  }
}

async function handleCancel(courseName:string) {
  try{
    console.log(name);
    const response = await MyAxios.myPosting('/courses/cancelRecommend',{course_name: courseName});
    if(response.status === 200){
      MessagePlugin.success('取消推荐成功!');
      getAllCourses();
    }else{
      MessagePlugin.error('取消推荐失败..');
    }
  }catch(error){
    MessagePlugin.error('取消请求失败..');
  }
}

async function handleRecommend(courseName:string){
  try{
    console.log(name);
    const response = await MyAxios.myPosting('/courses/recommend',{course_name: courseName});
    if(response.status === 200){
      MessagePlugin.success('推荐课程成功!');
      getAllCourses();
    }else{
      MessagePlugin.error('推荐课程失败..');
    }
  }catch(error){
    MessagePlugin.error('推荐请求失败..');
  }
}

function getType(type){
  switch(type){
    case 1:

      return 'success'
    case 0:

      return 'info'
    // default:
    //   return 'info'
  }
}

function getStatus(type){
  switch(type){
    case 1:
      return '已推荐 ✔'
    case 0:
      return '未推荐❌'
    default:
      return '未知状态'
  }
}
</script>

<style>

</style>
