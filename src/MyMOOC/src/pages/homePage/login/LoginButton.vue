<template>
  <div>
    <el-button type="primary" @click="dialogFormVisible = true">登录</el-button>

    <el-dialog v-model="dialogFormVisible" width="400">

      <div class="login-form">
        <h2>用户登录</h2>
        <login-form/>
          <div class="links">
            <forget-password-link/>
            <register-link></register-link>
          </div>
        </div>

    </el-dialog>
  </div>
</template>
<script lang="ts" setup>


</script>

<script lang="ts" setup>
import { reactive, ref } from 'vue'
import { useRouter,useRoute } from 'vue-router';
import RegisterLink from './RegisterLink.vue'
import LoginForm from './LoginForm.vue'
import ForgetPasswordLink from './ForgetPasswordLink.vue'

const dialogTableVisible = ref(false)
const dialogFormVisible = ref(false)
const formLabelWidth = '140px'


</script>

<style lang="less" scoped>
  .login-form{
    margin: 10px 10px;
    h2{
      text-align:center;
      margin: 10px;
    }
    .links{
      display: flex;
      margin-top: 10px;
      justify-content: space-between;
    }
  }
</style>
