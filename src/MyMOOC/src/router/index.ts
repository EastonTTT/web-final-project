import HomePage from '@/pages/homePage/index.vue'
import CoursePage from '@/pages/coursePage/index.vue'
import CourseDetail from '@/pages/coursePage/CourseDetail.vue'
import LoginForm from '@/pages/homePage/login/LoginForm.vue'
import { createRouter, createWebHistory } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
import User from '@/pages/user/index.vue'
import MyInfo from '@/pages/user/MyInfo.vue'
import CoursesManage from '@/pages/user/CoursesManage.vue'
import MyCourses from '@/pages/user/MyCourses.vue'
import MyDiscussion from '@/pages/user/MyDiscussion.vue'
import AccountManage from '@/pages/user/AccountManage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'default',
      redirect: 'home',
      children:[
        {
          path:'/home',
          name:'HomePage',
          component:HomePage,
          children:[
          ]
        },
        {
          path:'/courses',
          name:'coursesAll',
          component:CoursePage
        },
        {
          path: '/courses/:courseId',
          name: 'CourseDetail',
          component: CourseDetail,
          props: true
        },
        {
          path:'/user',
          name:'user',
          component:User,
          redirect:{name:'myInfo'},
          children:[
            {
              path:'myCourses',
              name:'myCourses',
              component:User
            },
            {
              path:'myInfo',
              name:'myInfo',
              component:MyInfo
            },
            {
              path:'accountManage',
              name:'accountManage',
              component:User
            },
            {
              path:'coursesManage',
              name:'coursesManage',
              component:CoursesManage
            },
            {
              path:'myCourses',
              name:'myCourses',
              component:MyCourses
            },
            {
              path:'myDiscussion',
              name:'myDiscussion',
              component:MyDiscussion
            },
            {
              path:'accountManage',
              name:'accountManage',
              component:AccountManage
            }
          ]
        }
      ]
    },

  ],
})

export default router
