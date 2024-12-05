<template>
  <div class="breadcrumb">
    <breadcrumb/>
  </div>

  <carousel/>

</template>

<script setup lang="ts">
import Carousel from './Carousel.vue'
import Breadcrumb from './Breadcrumb.vue';
import btn from '@/pages/homePage/login/btn.vue';

</script>

<style scoped lang="less">
.breadcrumb{
  margin: 10px;
}
</style>
