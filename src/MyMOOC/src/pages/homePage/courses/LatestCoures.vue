<template>
  <div class="common-layout">
    <el-container >
      <el-header height="225px">
        <div class="header-img">
          <img src="@/assets/latest-title.png" alt="latest-header" width="100%" >
        </div>
      </el-header>
      <el-container style="margin: 0 40px; background-color: white;">
        <el-aside width="350px">
          <img src="@/assets/latest-courses.png" alt="latest-courses-img" width="100%">
        </el-aside>

        <el-main>
          <div class="container">
            <div class="course-container" v-for="image in images" :key="image.course_id">
             <div class="img-container">
              <router-link :to="`/courses/${image.course_id}`">
                <img :src="image.url" :alt="image.course_name" style="border-radius: 10px;">
              </router-link>
              <h3>{{image.course_name}}</h3>
              <p>{{image.teacher}}</p>
            </div>
           </div>
          </div>

        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import MyAxios from '@/utils/request/Axios';
import { MessagePlugin } from 'tdesign-vue-next';

interface dataType{
  url: string
  course_id: number
  course_name: string
  teacher: string
}

let images = ref<dataType[]>([]);

getImage()

async function getImage(){
  try{
    const response = await MyAxios.myGetting('/courses/getLatest');
    console.log(response)
    if(response.status === 200){
      images.value = response.data.map(item => ({
      url: '/assets/' + item.course_image,
      course_id: item.course_id,
      course_name: item.course_name,
      teacher: item.userName,
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

<style lang="less" scoped>
.container{
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  .course-container{
    margin: 20px 20px 0;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
  .img-container{
    margin-top: 20px;
    // border: 1px solid #000;
    text-align: center;
    overflow: hidden;
    width: 255px;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    border-radius: 10px;

    // height: 300px;
    img{
      // object-fit: contain;
      width: 100%; /* 使图片宽度填满容器 */
      height: 100%; /* 使图片高度填满容器 */
      object-fit: cover; /* 覆盖容器，保持图片的宽高比 */
    }
    display: flex;
    flex-direction: column;
  }
  &:hover .img-container {
    transform: scale(1.1); /* 鼠标悬停时放大图片 */
    box-shadow: 0 0 20px rgba(0,0,0,0.5); /* 添加阴影 */
    }


  .header-img{
    text-align: center;
  }

}
}





</style>
