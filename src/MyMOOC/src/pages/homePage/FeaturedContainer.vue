<!-- 轮播图组件 -->
<template>
  <div class="container">
    <carousel></carousel>
    <course-list class="course-list"/>
  </div>
</template>

<script setup lang="ts">
// import CourseList from './CourseList.vue'
import Carousel from './Carousel.vue'
</script>

<style scoped lang="less">
.container{
  margin: 20px 50px;
  position: relative;
  .course-list{
    position: absolute;
    top: 0;
    left: 0;
    z-index: 10;
  }
}
</style>
