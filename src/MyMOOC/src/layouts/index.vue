<template>
  <div class="page">
    <nav-bar></nav-bar>
    <router-view/>
  </div>

</template>

<script setup lang="ts">
import { ref } from 'vue';
import Header from './Header.vue';
import Content from './Content.vue';
import NavBar from '@/components/NavBar.vue';

</script>

<style lang="less" scoped>
// .page{
//   background-color: #e5e5f7;
//   opacity: 0.3;
//   background: linear-gradient(135deg, #a6ceff55 25%, transparent 25%) -12px 0/ 24px 24px,
//   linear-gradient(225deg, #a6ceff 25%, transparent 25%) -12px 0/ 24px 24px,
//   linear-gradient(315deg, #a6ceff55 25%, transparent 25%) 0px 0/ 24px 24px,
//   linear-gradient(45deg, #a6ceff 25%, #e5e5f7 25%) 0px 0/ 24px 24px;
// }

</style>
