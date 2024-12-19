<template>
  <index/>
</template>

<script setup lang="ts">
import index from './layouts/index.vue';
import { reactive,provide } from 'vue';
import { getLoginRecord } from './pages/homePage/login/LoginRecord';

const loginRecord = reactive(getLoginRecord());
provide('loginRecord',loginRecord);
// console.log(loginRecord)
</script>


<style scoped>
</style>
