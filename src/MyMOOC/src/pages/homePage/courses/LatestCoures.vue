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
            <div class="course-container" v-for="image in images" :key="image.id">
             <div class="img-container">
              <img :src="image.url" :alt="image.alt">
              <h3>latest coures</h3>
              <p>latest courses info</p>
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
const images = ref([
      { id: 1, url: 'http://localhost:5173/src/assets/uooc.png' },
      { id: 2, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Another random image' },
      { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
      { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
      { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
      { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
      // { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
      // { id: 3, url: 'http://localhost:5173/src/assets/uooc.png', alt: 'Yet another random image' },
])

</script>

<style lang="less" scoped>
.container{
  margin-bottom: 40px;
  display: flex;
  flex-wrap: wrap;
  .course-container{
    margin: 20px 30px 0;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-evenly;
  .img-container{
    margin: 30px 0 20px 0;
    border: 1px solid #000;
    text-align: center;
    overflow: hidden;
    width: 240px;
    transition: transform 0.3s ease, box-shadow 0.3s ease;

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
