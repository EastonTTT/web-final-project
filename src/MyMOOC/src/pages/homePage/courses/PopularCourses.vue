<template>

  <div class="popular-course">
    <div class="title-pic"><img src="@/assets/result.png" alt="pic" height="200px" ></div>
    <div class="container">
      <div class="course-container" v-for="image in images" :key="image.url">
      <div class="img-container" >
        <img :src="image.url" :alt="image.course_name" style="border-radius: 10px;">
        <h3>{{image.course_name}}</h3>
        <p>{{image.teacher}}</p>
      </div>

    </div>
    </div>


  </div>

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
    const response = await MyAxios.myGetting('/courses/getPopular');
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
.popular-course{
  margin: 50px 40px 0 0;
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
      text-align: center;
      margin: 25px;
      // border: 1px solid #000;
      // display: flex;
      border-radius: 10px;
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
