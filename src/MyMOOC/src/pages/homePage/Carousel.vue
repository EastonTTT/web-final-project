<!-- 轮播图组件 -->
<template>
  <el-carousel autoplay :interval="5000" type="card" height="450px" loop pause-on-hover >
    <el-carousel-item v-for="image in images" :key="image.course_id">
      <router-link :to="`/courses/${image.course_id}`">
        <img :src="image.url" :alt="image.url" style="width: 100%; height: 100%; object-fit: cover;border-radius: 10px;">
      </router-link>
    </el-carousel-item>
  </el-carousel>
</template>

<script lang="ts" setup>
import MyAxios from '@/utils/request/Axios';
import {ref} from 'vue'
import { MessagePlugin } from 'tdesign-vue-next';

interface dataType{
  url: string
  course_id: number
}

let images = ref<dataType[]>([]);

getImage();

async function getImage(){
  try{
    console.log(name);
    const response = await MyAxios.myGetting('/courses/imageAll');
    console.log(response)
    if(response.status === 200){
      images.value = response.data.map(item => ({
      url: '/assets/' + item.course_image,
      course_id: item.course_id
  }));
      console.log(images.value)
    }else{
      MessagePlugin.error('获取图片失败..');
    }
  }catch(error){
    MessagePlugin.error('获取请求失败..');
  }
}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>
