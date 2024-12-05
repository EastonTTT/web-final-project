<template>
  <nav-bar></nav-bar>
  <router-view/>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import Header from './Header.vue';
import Content from './Content.vue';
import NavBar from '@/components/NavBar.vue';

</script>
