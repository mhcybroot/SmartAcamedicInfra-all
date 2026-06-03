import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../views/Dashboard.vue'
import IframeView from '../views/IframeView.vue'
import StudentPortalView from '../views/StudentPortalView.vue'

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard
  },
  {
    path: '/results',
    name: 'Results',
    component: IframeView,
    meta: { src: 'http://localhost:9087?embedded=true', title: 'Result Management System' }
  },
  {
    path: '/academic-members',
    name: 'AcademicMembers',
    component: IframeView,
    meta: { src: 'http://localhost:8083?embedded=true', title: 'Academic Member Management' }
  },
  {
    path: '/student-watch',
    name: 'StudentWatch',
    component: IframeView,
    meta: { src: 'http://localhost:8565?embedded=true', title: 'Student Watch & Device Control' }
  },
  {
    path: '/student-portal',
    name: 'StudentPortal',
    component: StudentPortalView,
    meta: { title: 'Student Self-Service Portal' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
