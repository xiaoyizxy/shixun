<script>
export default {
  name: "SettingPage",
  data() {
    return {
      activeTab: 'basic',
      basicForm: {
        systemName: '智慧护理服务平台',
        contactPhone: '400-888-9999',
        serviceTime: '08:00 - 20:00',
        serviceScope: '全市各区均可上门',
        maxAppointmentDays: 7,
        cancelDeadline: 2
      },
      notifForm: {
        smsNotify: true,
        appPush: true,
        emailNotify: false,
        alertThreshold: 30,
        dailyReportTime: '18:00'
      }
    }
  },
  methods: {
    handleSave() {
      this.$message.success('设置保存成功')
    }
  }
}
</script>

<template>
  <div class="setting-page">
    <div class="page-header">
      <h2>系统设置</h2>
      <p>配置系统基本参数和运行规则</p>
    </div>

    <div class="setting-card">
      <el-tabs v-model="activeTab" class="setting-tabs">
        <el-tab-pane label="基本设置" name="basic">
          <el-form :model="basicForm" label-width="130px" class="setting-form">
            <el-form-item label="系统名称">
              <el-input v-model="basicForm.systemName" />
            </el-form-item>
            <el-form-item label="客服电话">
              <el-input v-model="basicForm.contactPhone" />
            </el-form-item>
            <el-form-item label="服务时间">
              <el-select v-model="basicForm.serviceTime" style="width: 200px">
                <el-option label="08:00 - 20:00" value="08:00 - 20:00" />
                <el-option label="07:00 - 22:00" value="07:00 - 22:00" />
                <el-option label="全天候" value="全天候" />
              </el-select>
            </el-form-item>
            <el-form-item label="服务范围">
              <el-input v-model="basicForm.serviceScope" type="textarea" :rows="2" />
            </el-form-item>
            <el-form-item label="最大预约天数">
              <el-input-number v-model="basicForm.maxAppointmentDays" :min="1" :max="30" />
              <span class="form-tip">天内可提前预约</span>
            </el-form-item>
            <el-form-item label="取消截止时间">
              <el-input-number v-model="basicForm.cancelDeadline" :min="1" :max="24" />
              <span class="form-tip">小时前可免费取消</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="通知设置" name="notification">
          <el-form :model="notifForm" label-width="130px" class="setting-form">
            <el-form-item label="短信通知">
              <el-switch v-model="notifForm.smsNotify" />
            </el-form-item>
            <el-form-item label="APP推送">
              <el-switch v-model="notifForm.appPush" />
            </el-form-item>
            <el-form-item label="邮件通知">
              <el-switch v-model="notifForm.emailNotify" />
            </el-form-item>
            <el-form-item label="预警阈值">
              <el-input-number v-model="notifForm.alertThreshold" :min="5" :max="120" />
              <span class="form-tip">分钟，服务超时预警</span>
            </el-form-item>
            <el-form-item label="日报发送时间">
              <el-time-select v-model="notifForm.dailyReportTime" start="08:00" step="00:30" end="20:00" placeholder="选择时间" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="安全设置" name="security">
          <el-form label-width="130px" class="setting-form">
            <el-form-item label="登录锁定策略">
              <el-radio-group :model-value="'3'">
                <el-radio value="3">连续登录失败3次锁定</el-radio>
                <el-radio value="5">连续登录失败5次锁定</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="会话超时">
              <el-select :model-value="'30'" style="width: 200px">
                <el-option label="30分钟" value="30" />
                <el-option label="60分钟" value="60" />
                <el-option label="120分钟" value="120" />
              </el-select>
            </el-form-item>
            <el-form-item label="密码强度">
              <el-select :model-value="'medium'" style="width: 200px">
                <el-option label="低（6位以上）" value="low" />
                <el-option label="中（8位+字母数字）" value="medium" />
                <el-option label="高（8位+大小写+特殊字符）" value="high" />
              </el-select>
            </el-form-item>
            <el-form-item label="数据备份">
              <el-switch :model-value="true" />
              <span class="form-tip">每日自动备份数据库</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<style scoped>
.setting-page { padding: 0; }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 22px; color: #1a1a2e; margin: 0 0 4px; }
.page-header p { color: #888; font-size: 14px; }

.setting-card {
  background: #fff;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.setting-tabs { max-width: 640px; }

.setting-form { margin-top: 16px; }

.form-tip {
  font-size: 12px;
  color: #aaa;
  margin-left: 10px;
}
</style>
