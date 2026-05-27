import {createWebHashHistory, createRouter} from 'vue-router'

// 页面组件懒加载
const LoginPage = () => import('../pages/LoginPage.vue')
const RegisterPage = () => import('../pages/RegisterPage.vue')
const MainPage = () => import('../pages/MainPage.vue')
const UserHomePage = () => import('../pages/UserHomePage.vue')

const AppointmentPage = () => import('../pages/appointment/AppointmentPage.vue')
const NursingServicePage = () => import('../pages/nursing/NursingServicePage.vue')
const HomeBedPage = () => import('../pages/service/HomeBedPage.vue')
const PatientVisitPage = () => import('../pages/service/PatientVisitPage.vue')
const SmartRecognitionPage = () => import('../pages/ai/SmartRecognitionPage.vue')
const VoiceInteractionPage = () => import('../pages/ai/VoiceInteractionPage.vue')
const AIConsultPage = () => import('../pages/ai/AIConsultPage.vue')
const ElderManagePage = () => import('../pages/elder/ElderManagePage.vue')
const StaffManagePage = () => import('../pages/staff/StaffManagePage.vue')
const OrderManagePage = () => import('../pages/order/OrderManagePage.vue')
const StatisticsPage = () => import('../pages/statistics/StatisticsPage.vue')
const SettingPage = () => import('../pages/system/SettingPage.vue')
const DataDictPage = () => import('../pages/system/DataDictPage.vue')
const UserMessage = () => import('../pages/system/UserMessage.vue')

const routes = [
    // 登录
    {
        path: '/',
        name: 'login',
        component: LoginPage
    },
    // 注册
    {
        path: '/register',
        name: 'register',
        component: RegisterPage
    },
    // ======================= 用户端（普通用户） =======================
    {
        path: '/home',
        name: 'home',
        component: UserHomePage,
        redirect: '/home/appointment',
        meta: { role: 'user' },
        children: [
            { path: 'appointment', name: 'home-appointment', component: AppointmentPage },
            { path: 'nursing-service', name: 'home-nursing-service', component: NursingServicePage },
            { path: 'home-bed', name: 'home-bed', component: HomeBedPage },
            { path: 'patient-visit', name: 'home-patient-visit', component: PatientVisitPage },
            { path: 'smart-recognition', name: 'home-smart-recognition', component: SmartRecognitionPage },
            { path: 'voice-interaction', name: 'home-voice-interaction', component: VoiceInteractionPage },
            { path: 'ai-consult', name: 'home-ai-consult', component: AIConsultPage }
        ]
    },
    // ======================= 管理端（管理员） =======================
    {
        path: '/admin',
        name: 'admin',
        component: MainPage,
        redirect: '/admin/appointment',
        meta: { role: 'admin' },
        children: [
            // 预约服务
            { path: 'appointment', name: 'admin-appointment', component: AppointmentPage },
            // 护理服务
            { path: 'nursing-service', name: 'admin-nursing-service', component: NursingServicePage },
            { path: 'home-bed', name: 'admin-home-bed', component: HomeBedPage },
            { path: 'patient-visit', name: 'admin-patient-visit', component: PatientVisitPage },
            // AI智能
            { path: 'smart-recognition', name: 'admin-smart-recognition', component: SmartRecognitionPage },
            { path: 'voice-interaction', name: 'admin-voice-interaction', component: VoiceInteractionPage },
            { path: 'ai-consult', name: 'admin-ai-consult', component: AIConsultPage },
            // 信息管理（仅管理端）
            { path: 'elder-manage', name: 'admin-elder-manage', component: ElderManagePage },
            { path: 'staff-manage', name: 'admin-staff-manage', component: StaffManagePage },
            // 业务管理（仅管理端）
            { path: 'order-manage', name: 'admin-order-manage', component: OrderManagePage },
            { path: 'statistics', name: 'admin-statistics', component: StatisticsPage },
            // 系统管理（仅管理端）
            { path: 'system/setting', name: 'admin-system-setting', component: SettingPage },
            { path: 'system/dict', name: 'admin-system-dict', component: DataDictPage },
            { path: 'system/user', name: 'admin-system-user', component: UserMessage }
        ]
    },
    // 兼容旧路由 /index → admin 默认页
    {
        path: '/index',
        redirect: '/admin/appointment'
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
    const loginUser = localStorage.getItem('loginUser')
    let user = null
    if (loginUser) {
        try {
            user = JSON.parse(loginUser)
        } catch (e) {
            user = null
        }
    }

    // 未登录 → 只能访问登录和注册
    if (!user) {
        if (to.path === '/' || to.path === '/register') {
            next()
        } else {
            next('/')
        }
        return
    }

    // 已登录时，/ 和 /register 重定向到对应首页
    if (to.path === '/' || to.path === '/register') {
        if (user.role === 'admin') {
            next('/admin/appointment')
        } else {
            next('/home/appointment')
        }
        return
    }

    // 角色权限控制：普通用户不能访问 /admin
    if (to.path.startsWith('/admin') && user.role !== 'admin') {
        next('/home/appointment')
        return
    }

    // 管理员不能访问 /home（但可以访问）
    if (to.path.startsWith('/home') && user.role === 'admin') {
        next('/admin/appointment')
        return
    }

    next()
})

export default router
