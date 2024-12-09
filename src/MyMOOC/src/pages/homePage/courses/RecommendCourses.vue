<template>
  <div class="recommend-courses">
    <div class="title-container">
      <span>课程推荐</span>
       <h1>Advanced Course Recommendations</h1>
    </div>
    <div class="course-container">
       <div class="img-container" v-for="image in images" :key="image.id">
        <img height="150px" :src="image.url" :alt="image.alt" >
        <h3>course info</h3>
        <p> school / teacher</p>
       </div>
    </div>

    </div>
  <!-- </div> -->
</template>

<script lang="ts" setup>

import { ref } from 'vue';
import ImageGallery from '../ImageGallery.vue';

const images = ref([
      { id: 1, url: 'http://localhost:5173/src/assets/uooc.png' },
      { id: 2, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Another random image' },
      { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
      { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
      { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
      { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
      { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
      { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
])
</script>

<style lang="less" scoped>
  // .recommend-courses{
  //   background: url('../../../assets/20170925173411_E5zjd.jpg');
  // }
  .recommend-courses {
  position: relative;
  z-index: 1;
  margin: 20px 0 50px;
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
  display: flex;
  flex-wrap: wrap;
  overflow: hidden;
  justify-content: space-around;
  .img-container{
    margin: 10px 0;
    border: 1px solid #000;
    text-align: center;
    overflow: hidden;
    width: 300px;
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
}
</style>
