<template>
  <div>
    <el-button type="primary" @click="dialogFormVisible = true">登录</el-button>

    <el-dialog v-model="dialogFormVisible" width="500">
      <h2>用户登录</h2>
      <login-form/>
    </el-dialog>
  </div>
</template>
<script lang="ts" setup>


</script>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import LoginForm from './LoginForm.vue'

const dialogTableVisible = ref(false)
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'


</script>
