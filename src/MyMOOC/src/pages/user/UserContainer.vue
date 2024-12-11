<template>
  <t-layout style="margin: 20px;">
  <t-aside>
    <t-menu theme="light" :value="menuValue" style="margin-right: 50px" height="550px">
      <template #logo>
        <img width="136" class="logo" src="https://www.tencent.com/img/index/menu_logo_hover.png" alt="logo" />
      </template>
      <t-menu-item value="account-manage" style="height: 60px" @click="goToAccountManage">
        <template #icon>
          <t-icon name="server" />
        </template>
        账号管理
      </t-menu-item>
      <t-menu-item value="root" style="height: 60px" @click="goToCoursesManage">
        <template #icon>
          <t-icon name="root-list" />
        </template>
        课程管理
      </t-menu-item>
      <t-menu-item value="mail" style="height: 60px" @click="goTo">
        <template #icon>
          <t-icon name="mail" />
        </template>
        消息区
      </t-menu-item>
      <t-menu-item value="my-info" style="height: 60px" @click="goToPersonal">
        <template #icon>
          <t-icon name="user-circle" />
        </template>
        个人信息
      </t-menu-item>
      <t-menu-item value="play-circle" style="height: 60px" @click="goToMyCourses">
        <template #icon>
          <t-icon name="play-circle" />
        </template>
        我的课程
      </t-menu-item>
      <t-menu-item value="edit1" style="height: 60px" @click="goToMyDiscussion">
        <template #icon>
          <t-icon name="edit-1" />
        </template>
        我的讨论
      </t-menu-item>
    </t-menu>
  </t-aside>
  <t-layout>
    <t-content style="background-color: #fff;">
      <router-view/>
    </t-content>
  </t-layout>
</t-layout>
</template>

<script lang="ts" setup>
import { useRoute,useRouter } from 'vue-router';
import {ref} from 'vue'
const router = useRouter()
const menuValue = ref('account-manage')

function goToAccountManage(){
  router.push('')
  menuValue.value = ''
}

function goToPersonal(){
  router.push({name:'myInfo'})
  menuValue.value = 'my-info'
}

function goToMyCourses(){
  router.push('')
  menuValue.value = ''
}

function goToMyDiscussion(){
  router.push('')
  menuValue.value = ''
}

function goTo(){
  router.push('')
  menuValue.value = ''
}
function goToCoursesManage(){
  router.push('coursesManage')
}
</script>

<style lang="less" scoped>

</style>
