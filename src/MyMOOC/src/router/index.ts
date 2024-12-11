import HomePage from '@/pages/homePage/index.vue'
import CoursePage from '@/pages/coursePage/index.vue'
import LoginForm from '@/pages/homePage/login/LoginForm.vue'
import { createRouter, createWebHistory } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
import User from '@/pages/user/index.vue'
import MyInfo from '@/pages/user/MyInfo.vue'
import CoursesManage from '@/pages/user/CoursesManage.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'default',
      redirect: '/home',
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
          path:'/user',
          name:'user',
          component:User,
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
              path:'myTODO',
              name:'myTODO',
              component:User
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
            }
          ]
        }
      ]
    },

  ],
})

export default router
