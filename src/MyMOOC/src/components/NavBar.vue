<template>
  <div class="nav-bar">
    <el-menu :default-active="presentIndex" class="el-menu-demo" mode="horizontal" :ellipsis="false"
      @select="handleSelect" style="height: 80px;">
      <!-- logo -->
      <el-menu-item index="0">
        <img style="width: 100px" src="../assets/uooc.png" alt="Element logo" />
      </el-menu-item>
      <!-- 表项 -->
      <el-menu-item :index="homePageIndex"><el-text class="mx-1" size="large">首页</el-text></el-menu-item>

      <el-menu-item :index="aboutUsIndex"><el-text class="mx-1" size="large">关于我们</el-text></el-menu-item>
      <!-- 课程下拉列表 -->
      <el-menu-item :index="courseIndex"><nav-dropdown /></el-menu-item>

      <!-- 搜索框 -->
      <el-input v-model="searchInput" style="width: 240px;height:40px;margin-top: 20px;" size="large"
        placeholder="Please Input" :prefix-icon="Search" />
      <el-button type="primary" :icon="Search" size="small" style="margin:auto 10px">Search</el-button>
      <div class="my-button" v-if="!loginRecord.isLogged" style="margin-top: 23px;"><login-button /></div>
      <!-- <el-avatar v-if="LogStatus.isLogged" size="large" style="margin: 10px; cursor: pointer" @click="goToUser"
        src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" /> -->

      <!-- 头像 -->
      <el-dropdown >
        <span class="el-dropdown-link">
          <el-avatar v-if="loginRecord.isLogged" size="large" style="margin: 10px; cursor: pointer"
        src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="a" @click="goToUser">个人中心</el-dropdown-item>
            <el-dropdown-item command="b" divided @click="logout">退出登录</el-dropdown-item>
            <el-dropdown-item command="c" divided>Action 3</el-dropdown-item>

          </el-dropdown-menu>
        </template>
      </el-dropdown>

    </el-menu>
  </div>

</template>

<script lang="ts" setup>

// IMPORT AREA:
import { inject, ref, watchEffect } from 'vue'
import { Search } from '@element-plus/icons-vue'
import Avatar from './Avatar.vue';
import NavDropdown from './NavDropdown.vue';
import { useRouter, useRoute } from 'vue-router';
import func from '../../vue-temp/vue-editor-bridge';
import LoginButton from '@/pages/homePage/login/LoginButton.vue';
// import { LogStatus } from '@/pages/homePage/login/LogStatus';
import { getLoginRecord,setLoginRecord,clearLoginRecord,defaultLoginRecord } from '@/pages/homePage/login/LoginRecord';
import { flattedChildren } from 'element-plus/es/utils/index.mjs';
import { ElNotification } from 'element-plus'


const router = useRouter()
const route = useRoute()

interface LoginRecord {
  user_id: number | null;
  username: string | null;
  role: number | null;
  isLogged: boolean;
}

const loginRecord = inject<LoginRecord>('loginRecord',defaultLoginRecord)

// PARAMS AREA:
const homePageIndex = ref('1')
const aboutUsIndex = ref('2')
const courseIndex = ref('3')
const presentIndex = ref(homePageIndex.value)
const searchInput = ref('')
const ifLogin = ref(false)

// FUNCTIONS AREA:
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
  switch (key) {
    case homePageIndex.value:
      router.push('/home');
      console.log('jump to home...')
      break;
    case aboutUsIndex.value:
      router.push('/about');
      break;
    case courseIndex.value:
      router.push('/courses')
      console.log('jump to courses...')
      break;
  }
}

watchEffect(() => {
  if (route.path === '/home') {
    presentIndex.value = homePageIndex.value;
  } else if (route.path === '/about') {
    presentIndex.value = aboutUsIndex.value;
  } else if (route.path === '/courses') {
    presentIndex.value = courseIndex.value;
  } else {
    presentIndex.value = '0'; // 默认或未匹配情况
  }
});

function goToUser() {
  console.log('Avatar click.')
  router.push('/user')
}

function logout(){
  loginRecord.isLogged = false;
  loginRecord.user_id = null;
  loginRecord.username = null;
  loginRecord.role = null;
  clearLoginRecord();

  //如果当前页面是个人中心的话，则跳转到主页面
  if(router.currentRoute.value.path.indexOf('user') !== -1){
    ElNotification({
    title: 'Info',
    message: '需要登录才能访问个人中心哦',
    type: 'info',
  })
    router.push({name: 'HomePage'})
  }

}

</script>

<style lang="less" scoped>
.nav-bar {
  .el-menu--horizontal>.el-menu-item:nth-child(4) {
    margin-right: auto;
  }

  .my-button {
    margin: 12px 5px;
  }

  .el-dropdown-link:focus {
      outline: none;
}

}
</style>
