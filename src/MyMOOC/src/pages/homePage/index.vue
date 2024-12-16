<template>
  <div class="page">
    <div class="breadcrumb">
    <breadcrumb />
    </div>
    <carousel />
   <t-divider align="left" style="margin:0 0 10px 0;"><el-icon size="18px">
      <Collection />
     </el-icon> <t-link theme="primary" underline @click="goToCourses" style="margin-top: 0;;">
      <template #prefix-icon>
        <link-icon />
      </template>
      更多精品课程等你来探索~
      </t-link></t-divider>
     <recommend-courses></recommend-courses>
     <popular-courses/>
     <latest-coures/>


  </div>


</template>

<script setup lang="ts">
import Carousel from './Carousel.vue'
import Breadcrumb from './Breadcrumb.vue';
// import btn from '@/pages/homePage/login/btn.vue';
import axiosInstance from '@/utils/request/Axios';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import RecommendCourses from './courses/RecommendCourses.vue';
import func from '../../../vue-temp/vue-editor-bridge';
import PopularCourses from './courses/PopularCourses.vue';
import LatestCoures from './courses/LatestCoures.vue';
// import{Result} from "../resultModel.ts";
const router = useRouter();
const result = ref();

async function handleClick() {
  result.value = await axiosInstance.myGetting('/home');
  console.log('the result of .. is', result.value)
}

function goToCourses(){
  router.push('/courses')
}


</script>

<style scoped lang="less">
.page{
  position: relative;
  z-index: 1;
}
.page::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('@/assets/20170925173411_E5zjd.jpg');
  background-size: cover;
  opacity: 0.4; /* 设置背景图片的透明度% */
  z-index: -1;
}
</style>
