<!-- 个人中心页面汇总部分 -->
<template>
  <breadcrumb style="margin: 10px;"/>
  <header>
    <info-bar></info-bar>
  </header>
  <user-container/>
</template>

<script lang="ts" setup>
import { inject } from 'vue';
import { useRouter } from 'vue-router';
import { defaultLoginRecord } from '../homePage/login/LoginRecord';
import Breadcrumb from './Breadcrumb.vue';
import UserContainer from './UserContainer.vue';
import InfoBar from './InfoBar.vue';
import { ElNotification } from 'element-plus'

const router = useRouter();
interface LoginRecord {
  user_id: number | null;
  username: string | null;
  role: number | null;
  isLogged: boolean;
}

const loginRecord = inject<LoginRecord>('loginRecord',defaultLoginRecord)
if(loginRecord?.isLogged === false){
  ElNotification({
    title: 'Error',
    message: '需要登录才能访问个人主页！',
    type: 'error',
  })
  router.push({name:'HomePage'});
}
</script>

<style lang="less" scoped>

@keyframes gradient-slide {
  0% {
    background-position: 0% 50%;
  }
  100% {
    background-position: 100% 50%;
  }
}

header {
  height: 220px; /* 容器高度为视窗高度 */
  display: flex;
  align-items: center;
  justify-content: center;
  color: white; /* 文字颜色设置为白色以保证可读性 */
  font-size: 24px; /* 文字大小 */
  text-align: center; /* 文字居中 */

  /* 设定背景渐变并应用动画 */
  background: linear-gradient(270deg, #bbfcd9, #80abf5, #bbfcd9, #80abf5);
  background-size: 800% 800%; /* 增大背景尺寸以扩展渐变区域 */
  animation: gradient-slide 10s linear infinite; /* 应用动画，持续时间、平滑过渡和无限循环 */
}

</style>
