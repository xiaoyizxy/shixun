<template>
  <div class="register-page">
    <div class="left-section">
      <div class="logo-section">
        <a href="/" class="logo-link">
          <span>🏥</span>
          <span>智慧护理</span>
        </a>
      </div>
      <div class="hero-section">
        <h2>加入智慧护理平台</h2>
        <p>为家中老人预约专业护理服务，享受便捷的居家养老服务</p>
        <div class="feature-list">
          <div class="feature-item">📅 在线预约护理服务</div>
          <div class="feature-item">🤖 AI智能健康咨询</div>
          <div class="feature-item">👨‍⚕️ 专业护理人员上门</div>
          <div class="feature-item">📊 健康数据实时监测</div>
        </div>
      </div>
      <div class="blur-circle blur-circle-1"></div>
      <div class="blur-circle blur-circle-2"></div>
    </div>
    <div class="right-section">
      <div class="form-wrapper">
        <div class="form-header">
          <h1 class="form-title">创建账号</h1>
          <p class="form-subtitle">填写信息注册新账号</p>
        </div>
        <form @submit.prevent="handleRegister" class="register-form">
          <div class="form-group">
            <label class="form-label">用户名</label>
            <input v-model="form.username" type="text" placeholder="请输入用户名" class="form-input" required />
          </div>
          <div class="form-group">
            <label class="form-label">密码</label>
            <div class="password-wrapper">
              <input v-model="form.password" :type="showPwd ? 'text' : 'password'" placeholder="至少6位密码" class="form-input" required />
              <button type="button" class="pwd-toggle" @click="showPwd = !showPwd">
                {{ showPwd ? '🙈' : '👁️' }}
              </button>
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">姓名</label>
            <input v-model="form.realname" type="text" placeholder="请输入真实姓名" class="form-input" />
          </div>
          <div class="form-group">
            <label class="form-label">手机号</label>
            <input v-model="form.phone" type="text" placeholder="请输入手机号" class="form-input" />
          </div>
          <div v-if="errorMsg" class="error-alert">{{ errorMsg }}</div>
          <button type="submit" class="submit-btn" :disabled="loading">
            {{ loading ? '注册中...' : '注 册' }}
          </button>
        </form>
        <div class="login-link">
          已有账号？ <a href="/#/">立即登录</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: "RegisterPage",
  data() {
    return {
      form: { username: '', password: '', realname: '', phone: '' },
      showPwd: false,
      loading: false,
      errorMsg: ''
    }
  },
  methods: {
    async handleRegister() {
      if (!this.form.username || !this.form.password) {
        this.errorMsg = '用户名和密码不能为空'
        return
      }
      if (this.form.password.length < 6) {
        this.errorMsg = '密码至少6位'
        return
      }
      this.loading = true
      this.errorMsg = ''
      try {
        const res = await axios.post('/register', this.form)
        if (res.data.code === 200) {
          this.$message.success('注册成功！请登录')
          this.$router.push('/')
        } else {
          this.errorMsg = res.data.msg || '注册失败'
        }
      } catch {
        this.errorMsg = '网络异常，请稍后重试'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.register-page {
  display: grid;
  grid-template-columns: 1fr 1fr;
  min-height: 100vh;
}
.left-section {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  padding: 3rem;
  color: #fff;
}
.logo-section { position: relative; z-index: 20; margin-bottom: 3rem; }
.logo-link {
  display: flex; align-items: center; gap: 0.5rem;
  font-size: 1.25rem; font-weight: 700;
  text-decoration: none; color: #fff;
}
.hero-section { position: relative; z-index: 20; }
.hero-section h2 { font-size: 2rem; margin-bottom: 0.75rem; }
.hero-section p { font-size: 1rem; opacity: 0.85; margin-bottom: 2rem; line-height: 1.6; }
.feature-list { display: flex; flex-direction: column; gap: 0.75rem; }
.feature-item { font-size: 0.95rem; opacity: 0.9; }
.blur-circle {
  position: absolute; border-radius: 50%; filter: blur(80px);
}
.blur-circle-1 { top: 10%; right: 20%; width: 12rem; height: 12rem; background: rgba(255,255,255,0.15); }
.blur-circle-2 { bottom: 15%; left: 10%; width: 16rem; height: 16rem; background: rgba(255,255,255,0.1); }
.right-section {
  display: flex; align-items: center; justify-content: center;
  padding: 2rem; background: #fff;
}
.form-wrapper { width: 100%; max-width: 400px; }
.form-header { text-align: center; margin-bottom: 2rem; }
.form-title { font-size: 1.75rem; font-weight: 700; color: #1a1a2e; margin-bottom: 0.5rem; }
.form-subtitle { font-size: 0.875rem; color: #888; }
.register-form { display: flex; flex-direction: column; gap: 1rem; }
.form-group { display: flex; flex-direction: column; gap: 0.5rem; }
.form-label { font-size: 0.875rem; font-weight: 500; color: #555; }
.form-input {
  width: 100%; height: 2.75rem; padding: 0 0.9rem;
  border: 1.5px solid #e5e7eb; border-radius: 0.5rem;
  font-size: 0.95rem; outline: none; transition: border-color 0.2s; box-sizing: border-box;
}
.form-input:focus { border-color: #1890ff; }
.password-wrapper { position: relative; }
.password-wrapper .form-input { padding-right: 2.5rem; }
.pwd-toggle {
  position: absolute; right: 0.75rem; top: 50%; transform: translateY(-50%);
  background: none; border: none; cursor: pointer; font-size: 1.1rem;
}
.error-alert {
  padding: 0.75rem; font-size: 0.875rem; color: #dc2626;
  background: rgba(220,38,38,0.08); border: 1px solid rgba(220,38,38,0.2); border-radius: 0.5rem;
}
.submit-btn {
  width: 100%; height: 2.75rem; background: #1890ff; color: #fff;
  border: none; border-radius: 0.5rem; font-size: 1rem; font-weight: 500;
  cursor: pointer; transition: all 0.25s;
}
.submit-btn:hover { background: #096dd9; transform: translateY(-1px); box-shadow: 0 4px 12px rgba(24,144,255,0.3); }
.submit-btn:disabled { opacity: 0.6; cursor: not-allowed; }
.login-link { margin-top: 1.5rem; text-align: center; font-size: 0.875rem; color: #888; }
.login-link a { color: #1890ff; text-decoration: none; font-weight: 500; }
.login-link a:hover { text-decoration: underline; }
@media (max-width: 768px) {
  .register-page { grid-template-columns: 1fr; }
  .left-section { display: none; }
}
</style>