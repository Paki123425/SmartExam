import { createRouter, createWebHistory } from 'vue-router'

import Question from '../views/Question.vue'
import Paper from '../views/Paper.vue'
import Dashboard from '../views/Dashboard.vue'
import Subject from '../views/Subject.vue'

const routes = [
  {
    path: '/',
    redirect: '/dashboard',
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { title: '可视化大屏' },
  },
  {
    path: '/questions',
    name: 'Question',
    component: Question,
    meta: { title: '题库管理' },
  },
  {
    path: '/subjects',
    name: 'Subject',
    component: Subject,
    meta: { title: '课程管理' },
  },
  {
    path: '/paper',
    name: 'Paper',
    component: Paper,
    meta: { title: '组卷中心' },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router

