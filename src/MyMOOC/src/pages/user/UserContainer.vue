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

      <t-menu-item value="account-manage" style="height: 60px" @click="goToAccountManage" v-if="loginRecord?.role === 2">
        <template #icon>
          <t-icon name="server" />
        </template>
        账号管理
      </t-menu-item>

      <t-menu-item value="course-manage" style="height: 60px" @click="goToCoursesManage" v-if="loginRecord?.role===2">
        <template #icon>
          <t-icon name="root-list" />
        </template>
        课程管理
      </t-menu-item>

      <t-menu-item value="my-courses" style="height: 60px" @click="goToMyCourses" v-if="loginRecord?.role===1">
        <template #icon>
          <t-icon name="play-circle" />
        </template>
        我的课程
      </t-menu-item>

      <t-menu-item value="my-discussion" style="height: 60px" @click="goToMyDiscussion" v-if="loginRecord?.role===1">
        <template #icon>
          <t-icon name="edit-1" />
        </template>
        我的讨论
      </t-menu-item>

      <t-menu-item value="recommend-course" style="height: 60px" @click="goToRecommendCourses" v-if="loginRecord?.role===3">
        <template #icon>
          <t-icon name="edit-1" />
        </template>
        课程推荐
      </t-menu-item>
    </t-menu>
  </t-aside>
  <t-layout>
    <t-content style="background-color: #f9f9f9;">
      <router-view/>
    </t-content>
  </t-layout>
</t-layout>
</template>

<script lang="ts" setup>
import { useRoute,useRouter } from 'vue-router';
import {ref,inject} from 'vue'
const router = useRouter()
const menuValue = ref('my-info')

interface LoginRecord {
  user_id: number | null;
  username: string | null;
  role: number | null;
  isLogged: boolean;
}

const loginRecord = inject<LoginRecord>('loginRecord')


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

function goToRecommendCourses(){
  router.push({name:'recommendCourses'})
  menuValue.value = 'recommend-course'
}
</script>

<style lang="less" scoped>

</style>
