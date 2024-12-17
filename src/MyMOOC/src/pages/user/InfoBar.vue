<!-- 个人中心炫光背景上的内容部分 -->
<template>
  <div class="info-container">
      <el-avatar :size="150"
        src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" style="margin-right: 100px;"/>
    <div class="info">
      <h2>Welcome: {{ username }}</h2>
      <h3>Your identity is: {{ identity }}</h3>
    </div>
  </div>
</template>

<script lang="ts" setup>

import {computed} from 'vue';
import { ca } from 'vuetify/locale';

const loginRecord = JSON.parse(localStorage.getItem('LoginRecord') || '{}')

const username = loginRecord.username;
const role = loginRecord.role;

const identity = computed(() => {
  switch(role){
    case 1:
      return '学生';
    case 2:
      return '教师';
    case 3:
      return '管理员'
  }
})
</script>

<style lang="less" scoped>
  .info-container{
    display: flex;
    .info{
      h2{
        font-size: 60px;
        margin: 10px auto;
      }
      h3{
        font-size: 35px;
      }
    }
  }
</style>
