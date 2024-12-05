<template>
  <el-menu
    :default-active="presentIndex"
    class="el-menu-demo"
    mode="horizontal"
    :ellipsis="false"
    @select="handleSelect"
  >
    <!-- logo -->
    <el-menu-item index="0">
      <img
        style="width: 100px"
        src="../assets/uooc.png"
        alt="Element logo"
      />
    </el-menu-item>
    <!-- 表项 -->
    <el-menu-item :index="homePageIndex"><el-text class="mx-1" size="large">首页</el-text></el-menu-item>

    <el-menu-item :index="aboutUsIndex"><el-text class="mx-1" size="large">关于我们</el-text></el-menu-item>

    <el-menu-item :index="courseIndex"><nav-dropdown/></el-menu-item>

    <!-- 搜索框 -->
    <el-input
      v-model="searchInput"
      style="width: 240px;height:40px;margin-top: 10px;"
      size="large"
      placeholder="Please Input"
      :prefix-icon="Search"
    />
    <el-button type="primary" :icon="Search" size="small" style="margin:auto 10px">Search</el-button>
    <div class="my-button"><el-button type="primary" v-if="!ifLogin">登录</el-button></div>
    <Avatar v-if="ifLogin"></Avatar>
  </el-menu>
</template>

<script lang="ts" setup>
import { ref,watchEffect } from 'vue'
import { Search } from '@element-plus/icons-vue'
import Avatar from './Avatar.vue';
import NavDropdown from './NavDropdown.vue';
import { useRouter,useRoute } from 'vue-router';
const router = useRouter()
const route = useRoute()

const homePageIndex = ref('1')
const aboutUsIndex = ref('2')
const courseIndex = ref('3')
const presentIndex = ref(homePageIndex.value)

const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key,keyPath)
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

// 搜索框
const searchInput = ref('')

const ifLogin = ref(false)
</script>

<style>
.el-menu--horizontal > .el-menu-item:nth-child(4) {
  margin-right: auto;
}
.el-menu-item{
  font-size: 20px;
}
.my-button{
  margin: 12px 5px;
}
</style>
