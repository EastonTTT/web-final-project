<template>
  <div class="footer">
    <div class="logo"><img src="../assets/uooc.png" alt="uooc-logo" width="200px"></div>
    <div class="text-content"></div>
    <t-space break-line>
    <span style="color: aliceblue;">正直</span>
    <t-divider layout="vertical" />
    <span>进取</span>
    <t-divider layout="vertical" />
    <span>协作</span>
    <t-divider layout="vertical" dashed />
    <span>创造</span>
  </t-space>
  </div>
</template>

<script lang="ts" setup>

</script>

<style lang="less" scoped>
  .footer{
    display: flex;
    background-color: #313131;
    .text-content{
      span{
        color: white;
      }

    }
  }
</style>
