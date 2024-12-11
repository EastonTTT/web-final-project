<template>
  <t-layout style="margin: 20px;">
  <t-aside>
    <t-menu theme="light" :value="menuValue" style="margin-right: 50px" height="550px">
      <template #logo>
        <img width="136" class="logo" src="https://www.tencent.com/img/index/menu_logo_hover.png" alt="logo" />
      </template>

      <t-menu-item value="my-info" style="height: 60px" @click="goToPersonal">
        <template #icon>
          <t-icon name="user-circle" />
        </template>
        个人信息
      </t-menu-item>

      <t-menu-item value="account-manage" style="height: 60px" @click="goToAccountManage">
        <template #icon>
          <t-icon name="server" />
        </template>
        账号管理
      </t-menu-item>

      <t-menu-item value="course-manage" style="height: 60px" @click="goToCoursesManage">
        <template #icon>
          <t-icon name="root-list" />
        </template>
        课程管理
      </t-menu-item>

      <t-menu-item value="my-courses" style="height: 60px" @click="goToMyCourses">
        <template #icon>
          <t-icon name="play-circle" />
        </template>
        我的课程
      </t-menu-item>

      <t-menu-item value="my-discussion" style="height: 60px" @click="goToMyDiscussion">
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
const menuValue = ref('my-info')

function goToAccountManage(){
  router.push({name:'accountManage'})
  menuValue.value = 'account-manage'
}

function goToPersonal(){
  router.push({name:'myInfo'})
  menuValue.value = 'my-info'
}

function goToMyCourses(){
  router.push({name:'myCourses'})
  menuValue.value = 'my-courses'
}

function goToMyDiscussion(){
  router.push({name:'myDiscussion'})
  menuValue.value = 'my-discussion'
}

function goToCoursesManage(){
  router.push({name:'coursesManage'})
  menuValue.value = 'course-manage'

}
</script>

<style lang="less" scoped>

</style>
