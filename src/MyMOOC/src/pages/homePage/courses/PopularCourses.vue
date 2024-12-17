<template>

  <div class="popular-course">
    <div class="title-pic"><img src="@/assets/result.png" alt="pic" height="200px" ></div>
    <div class="container">
      <div class="course-container" v-for="image in images" :key="image.id">
      <div class="img-container" >
        <img :src="image.url" :alt="image.alt">
        <h3>image title</h3>
        <p>school / teacher</p>
      </div>

    </div>
    </div>


  </div>

</template>

<script lang="ts" setup>
import { ref } from 'vue';


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
.popular-course{
  margin: 30px 0;
  .title-pic{
    text-align: center;
    img{
      opacity: 0.7;
    }
  }

.container{
  display: flex;
  animation: move 15s infinite;
  &:hover{
        animation-play-state: paused;
      }
  .course-container{
    display: flex;
    margin-top: 20px;

    .img-container{
      margin: 25px;
      border: 1px solid #000;
      // display: flex;
      overflow: hidden;
      width: 300px;
      flex-shrink: 0;
      transition: transform 0.3s ease, box-shadow 0.3s ease;
      img{
        width: 300px;
        object-fit: contain; /* 图片内容以保持比例的方式被裁剪以适应内容框 */
      }
    }
    &:hover .img-container {
    transform: scale(1.1); /* 鼠标悬停时放大图片 */
    box-shadow: 0 0 20px rgba(0,0,0,0.5); /* 添加阴影 */
    }
  }
}

}

@keyframes move{
  50%{
    transform: translate(-1400px);
  }

  100%{
    transform: translate(0);
  }
}
</style>
