<template>
  <el-row class="demo-avatar">
    <el-col :span="12">
      <div class="demo-basic--circle">
          <el-avatar :size="size" :src="circleUrl" style="margin: 8px 15px;"/>
      </div>
    </el-col>
  </el-row>
</template>
<script lang="ts" setup>
import { reactive, toRefs } from 'vue'

const state = reactive({
  circleUrl:
    'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
  size: '' as const,
})

const { circleUrl,size } = toRefs(state)
</script>

<style scoped>
.demo-basic .demo-basic--circle {
  display: flex;
  justify-content: space-between;
  align-items: center;
}


</style>
