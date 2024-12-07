<template>
  <div>
    <el-link :icon="Edit" underline="true" @click="dialogFormVisible = true">还没有注册?请点这里👈</el-link>
    <div>
      <el-dialog v-model="dialogFormVisible" width="500">
        <div class="register-form">
          <h2>用户注册</h2>
          <register-form />
        </div>
      </el-dialog>
    </div>
  </div>


</template>

<script setup lang="ts">
import { Edit, View as IconView } from '@element-plus/icons-vue'
import { useRoute, useRouter } from 'vue-router';
import { ref } from 'vue';
import RegisterForm from './RegisterForm.vue';

const dialogFormVisible = ref(false)

</script>

<style scoped></style>
