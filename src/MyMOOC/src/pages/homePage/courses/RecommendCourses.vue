<template>
  <div class="recommend-courses">
    <div class="title-container">
      <span>课程推荐</span>
       <h1>Advanced Course Recommendations</h1>
    </div>
    <div class="container">
      <div class="course-container" v-for="image in images" :key="image.course_id" >
       <div class="img-container" >
        <img height="150px" :src="image.url" :alt="image.url" style="border-radius: 10px" >
        <h3>{{image.course_name}}</h3>
        <p>{{image.teacher}}</p>
       </div>
    </div>
    </div>

    <t-divider />
  </div>
  <!-- </div> -->
</template>

<script lang="ts" setup>
import { ref } from 'vue';
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
    const response = await MyAxios.myGetting('/courses/getRecommend');
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

  .recommend-courses {
  position: relative;
  z-index: 1;
  margin: 20px auto;
  width: 1300px;
  // height:500px;

  .title-container {
  text-align: center;
  padding: 20px;
  transition: all 0.3s ease; /* 平滑过渡效果 */
}

.title-container h1,span  {
  font-family: 'Segoe UI', sans-serif;
  color: #0047AB; /* 深蓝色 */
  font-size: 3em;
  position: relative;
  padding-bottom: 10px;
  text-shadow: 1px 1px 5px rgba(255, 215, 0, 0.75); /* 添加金色炫光 */
  transition: color 0.3s ease, text-shadow 0.3s ease; /* 平滑过渡效果 */
}

.title-container h1:hover {
  color: rgb(74, 110, 194); /* 悬停时改变字体颜色为金色 */
  text-shadow: 0px 0px 8px rgba(255, 215, 0, 1); /* 悬停时增强炫光效果 */
}

.title-container h1::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 0;
  height: 2px;
  background-color: rgb(178, 214, 255);
  animation: slideIn 5s ease forwards;
  transition: width 0.3s ease; /* 平滑过渡效果 */
}

/* 下划线滑动效果 */
@keyframes slideIn {
  from {
    width: 0;
  }
  to {
    width: 100%;
  }
}

}
  .recommend-courses::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('@/assets/20170925173411_E5zjd.jpg');
  background-size: cover;
  opacity: 0.2; /* 设置背景图片的透明度为50% */
  z-index: -1;
}

.course-container{
  justify-content: space-around;
  margin: 20px 5px 0 5px;
  .img-container{
    margin: 0 25px 30px;
    text-align: center;
    overflow: hidden;
    border-radius: 10px;
    width: 265px;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    display: flex;
    flex-direction: column;
    img{
      // object-fit: contain;
      width: 100%; /* 使图片宽度填满容器 */
      height: 100%; /* 使图片高度填满容器 */
      object-fit: cover; /* 覆盖容器，保持图片的宽高比 */

    }
  }
  &:hover .img-container {
    transform: scale(1.1); /* 鼠标悬停时放大图片 */
    box-shadow: 0 0 20px rgba(0,0,0,0.5); /* 添加阴影 */
    }
}

.container{
  display: flex;
  flex-wrap: wrap;
}
</style>
