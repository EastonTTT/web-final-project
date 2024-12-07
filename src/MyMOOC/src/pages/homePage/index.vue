<template>
  <div class="breadcrumb">
    <breadcrumb/>
  </div>

  <carousel/>

  <button @click="handleClick">点我发送请求</button>

</template>

<script setup lang="ts">
import Carousel from './Carousel.vue'
import Breadcrumb from './Breadcrumb.vue';
// import btn from '@/pages/homePage/login/btn.vue';
import axiosInstance from '@/utils/request/Axios';
import { ref } from 'vue';
// import{Result} from "../resultModel.ts";

const result = ref();

async function handleClick(){
  result.value = await axiosInstance.testGetting('/home');
  console.log('the result of .. is' , result.value)
}


</script>

<style scoped lang="less">
.breadcrumb{
  margin: 10px;
}
</style>
