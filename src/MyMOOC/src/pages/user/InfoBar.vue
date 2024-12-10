<template>
  <div class="info-container">
      <el-avatar :size="150"
        src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" style="margin-right: 100px;"/>
    <div class="info">
      <h2>my-username</h2>
      <span>My courses:</span>
      <el-divider direction="vertical" />
      <span>My study:</span>
      <el-divider direction="vertical" />
      <span>My discussion:</span>
    </div>
  </div>
</template>

<script lang="ts" setup>
</script>

<style lang="less" scoped>
  .info-container{
    display: flex;
    .info{
      h2{
        font-size: 50px;
        margin: 10px auto;
      }
    }
  }
</style>
