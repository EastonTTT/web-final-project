<template>
  <div class="common-layout">
    <el-container>
      <el-header height="250px">
        <div class="header-img">
          <img src="@/assets/latest-title.png" alt="latest-header" width="100%" >
        </div>
      </el-header>
      <el-container>
        <el-aside width="350px">
          <img src="@/assets/latest-courses.png" alt="latest-courses-img" width="350px">
        </el-aside>

        <el-main>
          <div class="course-container">
            <div class="img-container" v-for="image in images" :key="image.id">
              <img :src="image.url" :alt="image.alt">
              <h3>latest coures</h3>
              <p>latest courses info</p>
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
  .course-container{
  display: flex;
  flex-wrap: wrap;
  overflow: hidden;
  justify-content: space-around;
  .img-container{
    margin: 10px 0 50px 0;
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


  .header-img{
    text-align: center;
  }

}




</style>
