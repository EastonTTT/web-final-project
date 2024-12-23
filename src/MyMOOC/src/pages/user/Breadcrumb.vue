<template>
  <t-breadcrumb>
    <t-breadcrumb-item :to="{ path:'/home'}"
      ><template #icon><t-icon name="bookmark" /></template>首页</t-breadcrumb-item
    >
    <t-breadcrumb-item :to="{ path: '/user' }"
      ><template #icon><t-icon name="bookmark" /></template>个人中心</t-breadcrumb-item
    >
  </t-breadcrumb>
</template>
