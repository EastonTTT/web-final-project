<template>

  <div class="popular-course">
    <div class="title-pic"><img src="@/assets/result.png" alt="pic" height="200px" ></div>
    <div class="course-container">
      <div class="img-container" v-for="image in images" :key="image.id">
        <img :src="image.url" :alt="image.alt">
        <h3>image title</h3>
        <p>school / teacher</p>

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
  .title-pic{
    text-align: center;
    img{
      opacity: 0.7;
    }
  }
  .course-container{
    display: flex;
    margin: 20px 0;
    animation: move 15ws infinite;
    &:hover{
        animation-play-state: paused;
      }
    .img-container{
      margin: 25px;
      border: 1px solid #000;
      // display: flex;
      overflow: hidden;
      width: 300px;
      flex-shrink: 0;
      img{
        width: 300px;
        object-fit: contain; /* 图片内容以保持比例的方式被裁剪以适应内容框 */

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
