<script>
export default {
  name: "UserHomePage",
  data() {
    return {
      username: '用户'
    }
  },
  created() {
    const user = localStorage.getItem('loginUser')
    if (user) {
      try {
        const u = JSON.parse(user)
        this.username = u.realname || u.username || '用户'
      } catch (e) {
        this.username = '用户'
      }
    }
  },
  methods: {
    handleSelect(path) {
      this.$router.push(path)
    },
    handleLogout() {
      localStorage.removeItem('loginUser')
      this.$message.success('已退出登录')
      this.$router.push('/')
    }
  }
}
</script>

<template>
  <div class="common-layout" style="height: 100%">
    <el-container style="height: 100%">
      <el-header class="container-header">
        <div class="header-content">
          <div class="logo-section">
            <span class="logo-text">🏥 智慧护理</span>
          </div>
          <div class="header-title">智慧护理服务平台 - 为您提供专业居家养老服务</div>
          <div class="header-user">
            <el-dropdown>
              <span class="user-info">
                <span class="avatar">👤</span>
                <span class="username">{{ username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>个人中心</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-container style="height: 100%">
        <el-aside width="220px" class="container-aside">
          <el-menu
            default-active="1"
            class="el-menu-vertical-demo"
            background-color="#ffffff"
            text-color="#555"
            active-text-color="#1890ff"
          >
            <!-- 预约服务 -->
            <el-sub-menu index="1">
              <template #title>
                <span class="menu-icon">📅</span>
                <span>预约服务</span>
              </template>
              <el-menu-item index="1-1" @click="handleSelect('/home/appointment')">
                <span class="menu-icon">📝</span> 在线预约
              </el-menu-item>
            </el-sub-menu>

            <!-- 护理服务 -->
            <el-sub-menu index="2">
              <template #title>
                <span class="menu-icon">🏥</span>
                <span>护理服务</span>
              </template>
              <el-menu-item index="2-1" @click="handleSelect('/home/nursing-service')">
                <span class="menu-icon">💉</span> 上门护理
              </el-menu-item>
              <el-menu-item index="2-2" @click="handleSelect('/home/home-bed')">
                <span class="menu-icon">🛏️</span> 居家病床
              </el-menu-item>
              <el-menu-item index="2-3" @click="handleSelect('/home/patient-visit')">
                <span class="menu-icon">🩺</span> 上门巡诊
              </el-menu-item>
            </el-sub-menu>

            <!-- AI智能 -->
            <el-sub-menu index="3">
              <template #title>
                <span class="menu-icon">🤖</span>
                <span>AI智能</span>
              </template>
              <el-menu-item index="3-1" @click="handleSelect('/home/smart-recognition')">
                <span class="menu-icon">📷</span> 智能识别
              </el-menu-item>
              <el-menu-item index="3-2" @click="handleSelect('/home/voice-interaction')">
                <span class="menu-icon">🎤</span> 语音交互
              </el-menu-item>
              <el-menu-item index="3-3" @click="handleSelect('/home/ai-consult')">
                <span class="menu-icon">💬</span> AI咨询
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-main class="container-main">
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.container-header {
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
  text-align: center;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  position: relative;
  z-index: 10;
}
.header-content {
  display: flex;
  align-items: center;
  height: 100%;
  color: #fff;
}
.logo-section {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-right: 20px;
}
.logo-text {
  font-size: 18px;
  font-weight: 700;
  color: #fff;
  letter-spacing: 0.5px;
}
.header-title {
  flex: 1;
  text-align: center;
  font-size: 15px;
  font-weight: 400;
  color: rgba(255,255,255,0.9);
}
.header-user {
  margin-left: auto;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 20px;
  transition: background 0.25s;
  color: #fff;
}
.user-info:hover {
  background: rgba(255,255,255,0.15);
}
.avatar { font-size: 18px; }
.username { font-size: 14px; }
.container-aside {
  background: #fff !important;
  border-right: 1px solid #f0f0f5;
  overflow-y: auto;
}
.container-main {
  background: #f7f8fc;
  padding: 24px;
  overflow-y: auto;
}
.menu-icon {
  font-size: 16px;
  margin-right: 8px;
  display: inline-block;
  width: 20px;
  text-align: center;
}
</style>
