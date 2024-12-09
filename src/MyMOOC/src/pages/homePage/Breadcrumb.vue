<template>
  <div class="breadcrumb">
    <t-breadcrumb>
    <t-breadcrumb-item :to="{ path: '/' }">
      <template #icon><t-icon name="bookmark" /></template>
         首页
      </t-breadcrumb-item
    >
  </t-breadcrumb>
  </div>


</template>

<style lang="less" scoped>
.breadcrumb{
  // background-color: #f9f8f8;
  padding: 10px;
}
</style>
