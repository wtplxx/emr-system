import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/login.vue'

import DoctorLayout from '../views/doctor/DoctorLayout.vue'
import DoctorDashboard from '../views/doctor/DoctorDashboard.vue'
import DoctorRecords from '../views/doctor/DoctorRecords.vue'
import RecordCreate from '../views/RecordCreate.vue'

import PatientLayout from '../views/patient/PatientLayout.vue'
import PatientRecords from '../views/patient/PatientRecords.vue'
import PatientAuth from '../views/patient/PatientAuth.vue'
import PatientAppointment from '../views/patient/PatientAppointment.vue'
import PatientApptList from '../views/patient/PatientApptList.vue'

import PersonalCenter from '../views/PersonalCenter.vue'

Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', name: '登录', component: Login },

  {
    path: '/doctor',
    component: DoctorLayout,
    children: [
      { path: '', redirect: 'dashboard' },
      { path: 'dashboard', name: '工作台', component: DoctorDashboard },
      { path: 'records', name: '档案检索', component: DoctorRecords },
      { path: 'create', name: '新建档案', component: RecordCreate }
    ]
  },

  {
    path: '/patient',
    component: PatientLayout,
    children: [
      { path: '', redirect: 'records' },
      { path: 'records', name: '我的档案', component: PatientRecords },
      { path: 'appointment', name: '预约挂号', component: PatientAppointment },
      { path: 'appointments', name: '我的挂号', component: PatientApptList },
      { path: 'auth', name: '授权管理', component: PatientAuth }
    ]
  },

  { path: '/personal', name: '个人中心', component: PersonalCenter }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 全局路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const user = localStorage.getItem('user')

  if (to.path === '/login') {
    next()
    return
  }

  if (!token || !user) {
    next('/login')
    return
  }

  let userData
  try {
    userData = JSON.parse(user)
  } catch (e) {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    next('/login')
    return
  }
  if (to.path.startsWith('/doctor') && userData.role !== 'doctor') {
    next('/patient/records')
    return
  }
  if (to.path.startsWith('/patient') && userData.role !== 'patient') {
    next('/doctor/records')
    return
  }

  next()
})

export default router