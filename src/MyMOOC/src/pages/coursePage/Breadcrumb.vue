<template>
  <t-breadcrumb>
    <t-breadcrumb-item :to="{ path: '/' }"
      ><template #icon><t-icon name="bookmark" /></template>首页</t-breadcrumb-item
    >
    <t-breadcrumb-item :to="{ path: '/courses' }"
      ><template #icon><t-icon name="bookmark" /></template>课程</t-breadcrumb-item
    >
  </t-breadcrumb>
</template>
