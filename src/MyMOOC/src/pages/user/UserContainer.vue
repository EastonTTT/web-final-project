<template>
  <t-layout style="margin: 20px;">
  <t-aside>
    <t-menu theme="light" value="resource" style="margin-right: 50px" height="550px">
      <template #logo>
        <img width="136" class="logo" src="https://www.tencent.com/img/index/menu_logo_hover.png" alt="logo" />
      </template>
      <t-menu-item value="resource" style="height: 60px">
        <template #icon>
          <t-icon name="server" />
        </template>
        账号管理
      </t-menu-item>
      <t-menu-item value="root" style="height: 60px">
        <template #icon>
          <t-icon name="root-list" />
        </template>
        根目录
      </t-menu-item>
      <t-menu-item value="mail" style="height: 60px">
        <template #icon>
          <t-icon name="mail" />
        </template>
        消息区
      </t-menu-item>
      <t-menu-item value="user-circle" style="height: 60px">
        <template #icon>
          <t-icon name="user-circle" />
        </template>
        个人信息
      </t-menu-item>
      <t-menu-item value="play-circle" style="height: 60px">
        <template #icon>
          <t-icon name="play-circle" />
        </template>
        我的课程
      </t-menu-item>
      <t-menu-item value="edit1" style="height: 60px">
        <template #icon>
          <t-icon name="edit-1" />
        </template>
        我的讨论
      </t-menu-item>
    </t-menu>
  </t-aside>
  <t-layout>
    <t-content>
      <div>Content</div>
    </t-content>
    <t-footer>Copyright @ 2019-{{ new Date().getFullYear() }} Tencent. All Rights Reserved</t-footer>
  </t-layout>
</t-layout>
</template>

<style lang="less" scoped>

</style>
