import HomePage from '@/pages/homePage/index.vue'
import CoursePage from '@/pages/coursePage/index.vue'
import LoginForm from '@/pages/homePage/login/LoginForm.vue'
import { createRouter, createWebHistory } from 'vue-router'
import NavBar from '@/components/NavBar.vue'


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
      ]
    },

  ],
})

export default router
