<script>
import axios from 'axios'

export default {
  name: "PatientVisitPage",
  data() {
    return {
      visits: [],
      doctors: ['全部医生'],
      activeDoctor: '全部医生',
      todayVisits: 0
    }
  },
  computed: {
    filteredVisits() {
      if (this.activeDoctor === '全部医生') return this.visits
      return this.visits.filter(v => v.doctor === this.activeDoctor)
    }
  },
  mounted() {
    this.fetchVisits()
    this.fetchStats()
  },
  methods: {
    async fetchVisits() {
      try {
        const params = {}
        if (this.activeDoctor !== '全部医生') params.doctor = this.activeDoctor
        const res = await axios.get('/visit/list', { params })
        if (res.data.code === 200) {
          this.visits = res.data.data
          this.doctors = ['全部医生', ...new Set(res.data.data.map(v => v.doctor))]
        }
      } catch { this.$message.error('加载巡诊数据失败') }
    },
    async fetchStats() {
      try {
        const res = await axios.get('/visit/stats')
        if (res.data.code === 200) this.todayVisits = res.data.today || 0
      } catch {}
    },
    getStatusText(status) {
      return status === 'completed' ? '已完成' : status === 'upcoming' ? '即将上门' : '进行中'
    },
    getStatusType(status) {
      return status === 'completed' ? 'success' : status === 'upcoming' ? 'warning' : ''
    }
  }
}
</script>

<template>
  <div class="visit-page">
    <div class="page-header">
      <div>
        <h2>上门巡诊</h2>
        <p>专业医生上门诊疗，足不出户享受优质医疗服务</p>
      </div>
      <el-button type="primary" round>新建巡诊</el-button>
    </div>

    <!-- 今日概览 -->
    <div class="today-card">
      <div class="today-info">
        <span class="today-icon">🩺</span>
        <div>
          <div class="today-count">今日巡诊 <strong>{{ todayVisits }}</strong> 例</div>
          <div class="today-date">{{ new Date().toLocaleDateString('zh-CN', { month:'long', day:'numeric', weekday:'long' }) }}</div>
        </div>
      </div>
      <div class="today-doctors">
        <span v-for="d in ['王医生', '李医生']" :key="d" class="doctor-tag">
          <span class="doctor-dot"></span>
          {{ d }}
        </span>
      </div>
    </div>

    <!-- 医生筛选 -->
    <div class="filter-bar">
      <span
        v-for="d in doctors"
        :key="d"
        class="filter-tab"
        :class="{ active: activeDoctor === d }"
        @click="activeDoctor = d"
      >{{ d }}</span>
    </div>

    <!-- 巡诊列表 -->
    <div class="visit-list">
      <div v-for="visit in filteredVisits" :key="visit.id" class="visit-card">
        <div class="visit-time">
          <div class="time-date">{{ visit.visitDate }}</div>
          <div class="time-slot">{{ visit.timeSlot }}</div>
        </div>
        <div class="visit-body">
          <div class="visit-main">
            <div class="visit-name">{{ visit.elderName }} <span class="visit-age">{{ visit.age }}岁</span></div>
            <div class="visit-symptom">主诉：{{ visit.symptom }}</div>
            <div v-if="visit.diagnosis" class="visit-diagnosis">诊断：{{ visit.diagnosis }}</div>
          </div>
          <div class="visit-meta">
            <el-tag :type="getStatusType(visit.status)" size="small" effect="plain" round>
              {{ getStatusText(visit.status) }}
            </el-tag>
            <span class="visit-doctor">👨‍⚕️ {{ visit.doctor }}</span>
          </div>
        </div>
        <div class="visit-actions">
          <el-button size="small" round>查看病历</el-button>
          <el-button v-if="visit.status === 'completed'" size="small" round type="primary">复诊安排</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.visit-page { padding: 0; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.page-header h2 { font-size: 22px; color: #1a1a2e; margin: 0 0 4px; }
.page-header p { color: #888; font-size: 14px; }

.today-card {
  background: linear-gradient(135deg, #e6f4ff 0%, #f0f5ff 100%);
  border-radius: 14px;
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.today-info { display: flex; align-items: center; gap: 14px; }
.today-icon { font-size: 36px; }
.today-count { font-size: 15px; color: #555; }
.today-count strong { color: #1890ff; font-size: 22px; margin: 0 4px; }
.today-date { font-size: 12px; color: #888; margin-top: 2px; }

.today-doctors { display: flex; gap: 12px; }

.doctor-tag {
  background: #fff;
  border-radius: 20px;
  padding: 6px 14px;
  font-size: 13px;
  color: #555;
  display: flex;
  align-items: center;
  gap: 6px;
}

.doctor-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #52c41a;
}

.filter-bar { display: flex; gap: 8px; margin-bottom: 20px; }

.filter-tab {
  padding: 6px 18px;
  border-radius: 20px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.25s;
  background: #f5f5f9;
  color: #666;
  user-select: none;
}

.filter-tab:hover { background: #e8eef5; color: #1890ff; }
.filter-tab.active { background: #1890ff; color: #fff; }

.visit-list { display: flex; flex-direction: column; gap: 12px; }

.visit-card {
  background: #fff;
  border-radius: 14px;
  padding: 20px 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  transition: all 0.25s ease;
}

.visit-card:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 16px rgba(0,0,0,0.06);
}

.visit-time {
  text-align: center;
  min-width: 80px;
  padding-right: 20px;
  border-right: 1px solid #f0f0f5;
}

.time-date { font-size: 12px; color: #888; }
.time-slot { font-size: 18px; font-weight: 700; color: #1a1a2e; margin-top: 2px; }

.visit-body { flex: 1; }

.visit-name { font-size: 15px; font-weight: 600; color: #1a1a2e; margin-bottom: 4px; }
.visit-age { font-size: 12px; color: #888; font-weight: 400; margin-left: 6px; }
.visit-symptom { font-size: 13px; color: #777; }
.visit-diagnosis { font-size: 13px; color: #52c41a; margin-top: 2px; }

.visit-meta { display: flex; align-items: center; gap: 12px; margin-top: 8px; }
.visit-doctor { font-size: 12px; color: #888; }

.visit-actions { display: flex; gap: 8px; flex-shrink: 0; }

@media (max-width: 1024px) {
  .visit-card { flex-direction: column; align-items: flex-start; }
  .visit-time { border-right: none; padding-right: 0; text-align: left; display: flex; gap: 10px; align-items: center; }
  .visit-actions { width: 100%; }
}
</style>
