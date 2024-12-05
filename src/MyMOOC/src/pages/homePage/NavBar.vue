<template>
  <el-menu
    :default-active="activeIndex"
    class="el-menu-demo"
    mode="horizontal"
    :ellipsis="false"
    @select="handleSelect"
  >
    <!-- logo -->
    <el-menu-item index="0">
      <img
        style="width: 100px"
        src="../../assets/20241203155134.jpg"
        alt="Element logo"
      />
    </el-menu-item>
    <!-- 表项 -->
    <el-menu-item index="1"><el-text class="mx-1" size="large">首页</el-text></el-menu-item>

    <el-menu-item index="2"><nav-dropdown/></el-menu-item>

    <el-menu-item index="3"><el-text class="mx-1" size="large">关于我们</el-text></el-menu-item>

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
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'
import Avatar from './Avatar.vue';
import NavDropdown from './NavDropdown.vue';

const activeIndex = ref('1')
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
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
