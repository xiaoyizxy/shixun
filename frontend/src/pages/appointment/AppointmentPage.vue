<script>
import axios from 'axios'

export default {
  name: "AppointmentPage",
  data() {
    return {
      activeStep: 0,
      preselectedService: '', // 从护理服务页传入的服务名称
      form: {
        elderName: '',
        elderPhone: '',
        elderAge: '',
        elderAddress: '',
        serviceType: '',
        appointmentDate: '',
        appointmentTime: '',
        staffName: '',
        remark: ''
      },
      serviceTypes: [
        { value: 'nursing', label: '上门护理', icon: '🏥', price: 150 },
        { value: 'visit', label: '上门巡诊', icon: '🩺', price: 100 },
        { value: 'rehab', label: '康复理疗', icon: '💪', price: 200 },
        { value: 'injection', label: '打针输液', icon: '💉', price: 80 },
        { value: 'care', label: '生活照料', icon: '🤲', price: 120 },
        { value: 'other', label: '其他服务', icon: '📋', price: 50 }
      ],
      // 护理项目名称 → 服务类型映射
      serviceNameMap: {
        '压疮护理': 'nursing', '静脉输液': 'injection', '肌肉注射': 'injection',
        '更换尿管': 'nursing', '更换胃管': 'nursing', 'PICC维护': 'nursing',
        '康复训练': 'rehab', '中医按摩': 'rehab', '术后换药': 'nursing',
        '生活护理': 'care'
      },
      timeSlots: [
        { value: '08:00-09:00', label: '08:00 - 09:00' },
        { value: '09:00-10:00', label: '09:00 - 10:00' },
        { value: '10:00-11:00', label: '10:00 - 11:00' },
        { value: '14:00-15:00', label: '14:00 - 15:00' },
        { value: '15:00-16:00', label: '15:00 - 16:00' },
        { value: '16:00-17:00', label: '16:00 - 17:00' }
      ],
      staffList: [],
      recentAppointments: [],
      rules: {
        elderName: [{ required: true, message: '请输入老人姓名', trigger: 'blur' }],
        elderPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        elderAddress: [{ required: true, message: '请输入服务地址', trigger: 'blur' }],
        serviceType: [{ required: true, message: '请选择服务类型', trigger: 'change' }],
        appointmentDate: [{ required: true, message: '请选择预约日期', trigger: 'change' }],
        appointmentTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }]
      }
    }
  },
  mounted() {
    this.fetchStaff()
    this.fetchRecent()
    // 如果从护理服务页跳转过来，预选服务类型
    const queryService = this.$route.query.service
    const queryCategory = this.$route.query.category
    if (queryService) {
      this.preselectedService = queryService
      // 优先用映射表匹配服务类型，其次用 category
      const mapped = this.serviceNameMap[queryService]
      if (mapped) {
        this.form.serviceType = mapped
      } else if (queryCategory) {
        this.form.serviceType = queryCategory
      } else {
        this.form.serviceType = 'other'
      }
    }
  },
  methods: {
    async fetchStaff() {
      try {
        const res = await axios.get('/staff/list')
        if (res.data.code === 200) {
          this.staffList = (res.data.data || []).map(s => ({ id: s.id, name: s.name, title: s.title, rating: s.rating }))
        }
      } catch {}
    },
    async fetchRecent() {
      try {
        const res = await axios.get('/appointment/recent')
        if (res.data.code === 200) {
          this.recentAppointments = (res.data.data || []).map(a => {
            const svc = this.serviceTypes.find(s => s.value === a.serviceType)
            return {
              id: a.id,
              name: a.elderName,
              phone: a.elderPhone,
              service: svc ? svc.label : a.serviceType,
              serviceIcon: svc ? svc.icon : '📋',
              date: a.appointmentDate,
              time: a.appointmentTime,
              staff: a.staffName,
              address: a.elderAddress,
              status: a.status
            }
          })
        }
      } catch {}
    },
    nextStep() {
      if (this.activeStep < 2) this.activeStep++
    },
    prevStep() {
      if (this.activeStep > 0) this.activeStep--
    },
    async submitAppointment() {
      try {
        // 格式化日期为 YYYY-MM-DD，处理空值
        const payload = { ...this.form }
        if (payload.elderAge === '' || payload.elderAge === null) {
          payload.elderAge = null
        }
        // el-date-picker 返回 Date 对象，需要格式化为字符串
        if (payload.appointmentDate instanceof Date) {
          const d = payload.appointmentDate
          payload.appointmentDate = d.getFullYear() + '-' +
            String(d.getMonth() + 1).padStart(2, '0') + '-' +
            String(d.getDate()).padStart(2, '0')
        }
        await axios.post('/appointment', payload)
        this.$message.success('预约提交成功！我们会尽快与您联系确认。')
        this.activeStep = 0
        this.form = {
          elderName: '', elderPhone: '', elderAge: '', elderAddress: '',
          serviceType: '', appointmentDate: '', appointmentTime: '', staffName: '', remark: ''
        }
        this.fetchRecent()
      } catch { this.$message.error('预约提交失败') }
    },
    disabledDate(time) {
      return time.getTime() < Date.now() - 8.64e7
    },
    statusTagType(status) {
      if (status === 'completed') return 'success'
      if (status === 'pending') return 'warning'
      if (status === 'confirmed') return 'primary'
      if (status === 'in-progress') return ''
      if (status === 'cancelled') return 'info'
      if (status === 'upcoming') return ''
      return 'info'
    },
    statusLabel(status) {
      const map = { completed: '已完成', pending: '待处理', confirmed: '已确认', 'in-progress': '服务中', upcoming: '即将服务', cancelled: '已取消' }
      return map[status] || status
    },
    // 确认预约 → 生成服务订单
    async confirmAppointment(item) {
      try {
        await this.$confirm(`确认为 "${item.name}" 的预约生成服务订单吗？`, '确认预约', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'info'
        })
        const res = await axios.put(`/appointment/${item.id}/confirm`)
        if (res.data.code === 200) {
          this.$message.success('预约已确认，服务订单已生成')
          this.fetchRecent()
        } else {
          this.$message.error(res.data.msg || '操作失败')
        }
      } catch {
        // 取消
      }
    }
  }
}
</script>

<template>
  <div class="appointment-page">
    <div class="page-header">
      <h2>在线预约</h2>
      <p>为家中老人预约专业护理服务</p>
    </div>

    <div class="content-grid">
      <!-- 左侧：预约流程 -->
      <div class="main-section">
        <el-steps :active="activeStep" finish-status="success" align-center class="steps-bar">
          <el-step title="选择服务" />
          <el-step title="填写信息" />
          <el-step title="确认提交" />
        </el-steps>

        <!-- 步骤1：选择服务 -->
        <div v-show="activeStep === 0" class="step-content">
          <!-- 护理服务预选提示 -->
          <div v-if="preselectedService" class="preselect-banner">
            <span class="preselect-icon">💡</span>
            <span>您选择了 <strong>{{ preselectedService }}</strong>，已自动匹配服务类型，请确认后继续</span>
          </div>
          <div class="service-cards">
            <div
              v-for="service in serviceTypes"
              :key="service.value"
              class="service-card"
              :class="{ active: form.serviceType === service.value }"
              @click="form.serviceType = service.value"
            >
              <span class="service-icon">{{ service.icon }}</span>
              <span class="service-label">{{ service.label }}</span>
              <span class="service-price">¥{{ service.price }}/次</span>
            </div>
          </div>
          <div v-if="form.serviceType" class="form-row animate-in">
            <el-form-item label="预约日期">
              <el-date-picker
                v-model="form.appointmentDate"
                type="date"
                placeholder="选择日期"
                :disabled-date="disabledDate"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="预约时间">
              <el-select v-model="form.appointmentTime" placeholder="选择时间段" style="width: 100%">
                <el-option v-for="slot in timeSlots" :key="slot.value" :label="slot.label" :value="slot.value" />
              </el-select>
            </el-form-item>
          </div>
          <div class="step-actions">
            <el-button type="primary" @click="nextStep" :disabled="!form.serviceType">下一步</el-button>
          </div>
        </div>

        <!-- 步骤2：填写信息 -->
        <div v-show="activeStep === 1" class="step-content animate-in">
          <el-form :model="form" :rules="rules" label-width="90px" class="info-form">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="老人姓名" prop="elderName">
                  <el-input v-model="form.elderName" placeholder="请输入姓名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="联系电话" prop="elderPhone">
                  <el-input v-model="form.elderPhone" placeholder="请输入电话" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="年龄">
                  <el-input-number v-model="form.elderAge" :min="1" :max="120" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="护理人员">
                  <el-select v-model="form.staffName" placeholder="请选择（可选）" style="width: 100%">
                    <el-option
                      v-for="staff in staffList"
                      :key="staff.id"
                      :label="`${staff.name} - ${staff.title} ⭐${staff.rating}`"
                      :value="staff.name"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="服务地址" prop="elderAddress">
              <el-input v-model="form.elderAddress" type="textarea" :rows="2" placeholder="请输入详细地址" />
            </el-form-item>
            <el-form-item label="备注说明">
              <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="如有特殊需求请备注" />
            </el-form-item>
          </el-form>
          <div class="step-actions">
            <el-button @click="prevStep">上一步</el-button>
            <el-button type="primary" @click="nextStep">下一步</el-button>
          </div>
        </div>

        <!-- 步骤3：确认提交 -->
        <div v-show="activeStep === 2" class="step-content animate-in">
          <div class="confirm-card">
            <div class="confirm-item">
              <span class="confirm-label">服务类型</span>
              <span class="confirm-value">{{ serviceTypes.find(s => s.value === form.serviceType)?.icon }} {{ serviceTypes.find(s => s.value === form.serviceType)?.label }}</span>
            </div>
            <div class="confirm-item" style="background: #fff8f0; border-radius: 8px; padding: 12px 16px; margin: -4px 0;">
              <span class="confirm-label">预计费用</span>
              <span class="confirm-value" style="color: #f5222d; font-size: 20px; font-weight: 700;">
                ¥{{ serviceTypes.find(s => s.value === form.serviceType)?.price || 0 }}
              </span>
            </div>
            <div class="confirm-item">
              <span class="confirm-label">预约时间</span>
              <span class="confirm-value">{{ form.appointmentDate }} {{ form.appointmentTime }}</span>
            </div>
            <div class="confirm-item">
              <span class="confirm-label">老人姓名</span>
              <span class="confirm-value">{{ form.elderName }}</span>
            </div>
            <div class="confirm-item">
              <span class="confirm-label">联系电话</span>
              <span class="confirm-value">{{ form.elderPhone }}</span>
            </div>
            <div class="confirm-item">
              <span class="confirm-label">服务地址</span>
              <span class="confirm-value">{{ form.elderAddress }}</span>
            </div>
            <div class="confirm-item" v-if="form.remark">
              <span class="confirm-label">备注说明</span>
              <span class="confirm-value">{{ form.remark }}</span>
            </div>
          </div>
          <div class="step-actions">
            <el-button @click="prevStep">上一步</el-button>
            <el-button type="primary" @click="submitAppointment">确认提交</el-button>
          </div>
        </div>
      </div>

      <!-- 右侧：近期预约 -->
      <div class="side-section">
        <div class="side-card">
          <div class="side-card-header">
            <h3>📋 预约记录</h3>
            <el-button text size="small" @click="fetchRecent">刷新</el-button>
          </div>
          <div v-if="recentAppointments.length === 0" class="empty-hint">暂无预约记录</div>
          <div class="recent-list" v-else>
            <div v-for="item in recentAppointments" :key="item.id" class="recent-item">
              <div class="recent-top">
                <span class="recent-icon">{{ item.serviceIcon }}</span>
                <div class="recent-info">
                  <span class="recent-name">{{ item.name }}</span>
                  <span class="recent-service">{{ item.service }}</span>
                </div>
                <el-tag :type="statusTagType(item.status)" size="small">
                  {{ statusLabel(item.status) }}
                </el-tag>
              </div>
              <div class="recent-detail">
                <span>📅 {{ item.date }} {{ item.time }}</span>
                <span v-if="item.staff">👩‍⚕️ {{ item.staff }}</span>
              </div>
              <div class="recent-meta" v-if="item.phone">
                <span>📞 {{ item.phone }}</span>
              </div>
              <div class="recent-actions" v-if="item.status === 'pending'">
                <el-button size="small" type="primary" plain round @click="confirmAppointment(item)">
                  ✅ 确认生成订单
                </el-button>
              </div>
            </div>
          </div>
        </div>
        <div class="side-card tips-card">
          <h3>预约须知</h3>
          <ul>
            <li>请提前1天预约服务</li>
            <li>服务范围：市区内均可上门</li>
            <li>如需取消请提前2小时通知</li>
            <li>紧急情况请拨打客服电话</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.appointment-page {
  padding: 0;
  height: 100%;
}

.page-header {
  margin-bottom: 28px;
}

.page-header h2 {
  font-size: 22px;
  color: #1a1a2e;
  margin: 0 0 4px;
}

.page-header p {
  color: #888;
  font-size: 14px;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 24px;
  align-items: start;
}

.main-section {
  background: #fff;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.steps-bar {
  margin-bottom: 36px;
}

.step-content {
  min-height: 320px;
}

.service-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 28px;
}

.service-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 24px 16px;
  border: 2px solid #f0f0f5;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.25s ease;
  background: #fafbfe;
}

.service-card:hover {
  border-color: #a5d6ff;
  background: #f0f7ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(24,144,255,0.08);
}

.service-card.active {
  border-color: #1890ff;
  background: #e6f4ff;
  box-shadow: 0 4px 12px rgba(24,144,255,0.15);
}

.service-icon {
  font-size: 32px;
}

.service-label {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.service-price {
  font-size: 13px;
  color: #f5222d;
  font-weight: 600;
  margin-top: 2px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 8px;
}

.step-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 24px;
  border-top: 1px solid #f0f0f5;
  margin-top: 24px;
}

.confirm-card {
  background: #fafbfe;
  border-radius: 12px;
  padding: 24px;
}

.confirm-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f5;
}

.confirm-item:last-child {
  border-bottom: none;
}

.confirm-label {
  color: #888;
  font-size: 14px;
}

.confirm-value {
  font-weight: 500;
  color: #1a1a2e;
  font-size: 14px;
}

.side-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  margin-bottom: 16px;
}

.side-card h3 {
  font-size: 16px;
  color: #1a1a2e;
  margin: 0 0 16px;
  font-weight: 600;
}

.side-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.side-card-header h3 {
  margin: 0;
}

.empty-hint {
  text-align: center;
  color: #bbb;
  font-size: 13px;
  padding: 24px 0;
}

.recent-item {
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f9;
}

.recent-item:last-child {
  border-bottom: none;
}

.recent-top {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}

.recent-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.recent-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
  flex: 1;
  min-width: 0;
}

.recent-name {
  font-weight: 500;
  font-size: 14px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.recent-service {
  font-size: 12px;
  color: #888;
}

.recent-detail {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  font-size: 12px;
  color: #999;
  padding-left: 26px;
  margin-bottom: 4px;
}

.recent-meta {
  font-size: 12px;
  color: #bbb;
  padding-left: 26px;
}

.recent-time {
  font-size: 12px;
  color: #aaa;
}

.recent-actions {
  margin-top: 8px;
  padding-left: 26px;
}

.tips-card ul {
  margin: 0;
  padding: 0 0 0 18px;
  font-size: 13px;
  color: #777;
  line-height: 2;
}

.animate-in {
  animation: fadeUp 0.35s ease;
}

.preselect-banner {
  background: linear-gradient(135deg, #e6f4ff 0%, #f0f7ff 100%);
  border: 1px solid #a5d6ff;
  border-radius: 10px;
  padding: 12px 16px;
  margin-bottom: 20px;
  font-size: 13px;
  color: #1677cc;
  display: flex;
  align-items: center;
  gap: 8px;
}

.preselect-icon {
  font-size: 18px;
  flex-shrink: 0;
}

.preselect-banner strong {
  color: #0d5fa8;
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 1024px) {
  .content-grid {
    grid-template-columns: 1fr;
  }

  .service-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
