<script>
import axios from 'axios'

export default {
  name: "StatisticsPage",
  data() {
    return {
      period: 'week',
      kpiCards: [],
      serviceData: [],
      maxCount: 0,
      weeklyData: [],
      weeklyMax: 0,
      quality: {},
      recentActivities: [
        { time: '14:30', content: '系统中暂无动态数据', type: '' }
      ]
    }
  },
  mounted() {
    this.fetchDashboard()
  },
  methods: {
    async fetchDashboard() {
      try {
        const res = await axios.get('/statistics/dashboard')
        if (res.data.code === 200) {
          const d = res.data.data
          this.kpiCards = d.kpiCards || []
          this.serviceData = d.serviceData || []
          this.maxCount = Math.max(...this.serviceData.map(s => s.count || 0), 1)
          this.weeklyData = d.weeklyData || []
          this.weeklyMax = Math.max(...this.weeklyData.map(w => w.count || 0), 1)
          this.quality = d.quality || {}
        }
      } catch { this.$message.error('加载统计数据失败') }
    }
  }
}
</script>

<template>
  <div class="stats-page">
    <div class="page-header">
      <div>
        <h2>数据统计</h2>
        <p>运营数据概览与服务统计分析</p>
      </div>
      <el-radio-group v-model="period" size="small">
        <el-radio-button value="week">本周</el-radio-button>
        <el-radio-button value="month">本月</el-radio-button>
        <el-radio-button value="quarter">本季度</el-radio-button>
      </el-radio-group>
    </div>

    <!-- KPI 卡片 -->
    <div class="kpi-row">
      <div v-for="kpi in kpiCards" :key="kpi.label" class="kpi-card">
        <div class="kpi-value">{{ kpi.value }}</div>
        <div class="kpi-label">{{ kpi.label }}</div>
        <div class="kpi-change" :class="kpi.trend">
          <span v-if="kpi.trend === 'up'">↑</span>
          <span v-else-if="kpi.trend === 'up'">↓</span>
          <span v-else>→</span>
          {{ kpi.change }}
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <!-- 服务分布 -->
      <div class="chart-card">
        <h3>服务项目分布</h3>
        <div class="bar-chart">
          <div v-for="item in serviceData" :key="item.name" class="bar-row">
            <span class="bar-label">{{ item.name }}</span>
            <div class="bar-track">
              <div
                class="bar-fill"
                :style="{ width: (item.count / maxCount * 100) + '%', background: item.color }"
              ></div>
            </div>
            <span class="bar-num">{{ item.count }}</span>
          </div>
        </div>
      </div>

      <!-- 周趋势 -->
      <div class="chart-card">
        <h3>每日服务量趋势</h3>
        <div class="bar-chart">
          <div v-for="item in weeklyData" :key="item.day" class="bar-row">
            <span class="bar-label">{{ item.day }}</span>
            <div class="bar-track">
              <div
                class="bar-fill"
                :style="{ width: (item.count / weeklyMax * 100) + '%', background: '#1890ff' }"
              ></div>
            </div>
            <span class="bar-num">{{ item.count }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 动态与统计 -->
    <div class="bottom-grid">
      <!-- 最近动态 -->
      <div class="chart-card">
        <h3>最近动态</h3>
        <div class="activity-list">
          <div v-for="(act, idx) in recentActivities" :key="idx" class="activity-item">
            <div class="act-dot" :class="act.type"></div>
            <div class="act-content">{{ act.content }}</div>
            <div class="act-time">{{ act.time }}</div>
          </div>
        </div>
      </div>

      <!-- 满意度 -->
      <div class="chart-card">
        <h3>护理质量评估</h3>
        <div class="quality-grid">
          <div class="quality-item">
            <div class="q-circle" style="--progress: 96">
              <svg viewBox="0 0 100 100">
                <circle cx="50" cy="50" r="42" class="q-bg" />
                <circle cx="50" cy="50" r="42" class="q-fill q-green" />
              </svg>
              <span class="q-num">96%</span>
            </div>
            <span class="q-label">服务准时率</span>
          </div>
          <div class="quality-item">
            <div class="q-circle" style="--progress: 92">
              <svg viewBox="0 0 100 100">
                <circle cx="50" cy="50" r="42" class="q-bg" />
                <circle cx="50" cy="50" r="42" class="q-fill q-blue" />
              </svg>
              <span class="q-num">92%</span>
            </div>
            <span class="q-label">操作规范率</span>
          </div>
          <div class="quality-item">
            <div class="q-circle" style="--progress: 98">
              <svg viewBox="0 0 100 100">
                <circle cx="50" cy="50" r="42" class="q-bg" />
                <circle cx="50" cy="50" r="42" class="q-fill q-purple" />
              </svg>
              <span class="q-num">98.5%</span>
            </div>
            <span class="q-label">患者满意度</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.stats-page { padding: 0; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.page-header h2 { font-size: 22px; color: #1a1a2e; margin: 0 0 4px; }
.page-header p { color: #888; font-size: 14px; }

.kpi-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.kpi-card {
  background: #fff;
  border-radius: 14px;
  padding: 22px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.kpi-value {
  font-size: 32px;
  font-weight: 700;
  color: #1a1a2e;
  margin-bottom: 4px;
}

.kpi-label { font-size: 13px; color: #888; margin-bottom: 6px; }

.kpi-change {
  font-size: 13px;
  font-weight: 500;
}

.kpi-change.up { color: #52c41a; }
.kpi-change.stable { color: #888; }

.charts-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 16px;
}

.chart-card {
  background: #fff;
  border-radius: 14px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.chart-card h3 {
  font-size: 15px;
  color: #1a1a2e;
  margin: 0 0 20px;
  font-weight: 600;
}

.bar-chart { display: flex; flex-direction: column; gap: 10px; }

.bar-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.bar-label { font-size: 12px; color: #777; width: 75px; text-align: right; flex-shrink: 0; }

.bar-track {
  flex: 1;
  height: 22px;
  background: #f5f5f9;
  border-radius: 6px;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  border-radius: 6px;
  transition: width 0.6s ease;
  min-width: 4px;
}

.bar-num { font-size: 12px; color: #999; width: 24px; text-align: right; flex-shrink: 0; }

.bottom-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 0;
  border-bottom: 1px solid #f5f5f9;
}

.activity-item:last-child { border-bottom: none; }

.act-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.act-dot.complete { background: #52c41a; }
.act-dot.appointment { background: #1890ff; }
.act-dot.alert { background: #ff4d4f; }
.act-dot.schedule { background: #faad14; }

.act-content { flex: 1; font-size: 13px; color: #555; line-height: 1.4; }
.act-time { font-size: 12px; color: #bbb; flex-shrink: 0; }

.quality-grid {
  display: flex;
  justify-content: space-around;
  padding: 10px 0;
}

.quality-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.q-circle {
  position: relative;
  width: 90px;
  height: 90px;
}

.q-circle svg {
  transform: rotate(-90deg);
}

.q-bg {
  fill: none;
  stroke: #f0f0f5;
  stroke-width: 8;
}

.q-fill {
  fill: none;
  stroke-width: 8;
  stroke-linecap: round;
  stroke-dasharray: 264;
  stroke-dashoffset: calc(264 * (1 - var(--progress) / 100));
}

.q-green { stroke: #52c41a; }
.q-blue { stroke: #1890ff; }
.q-purple { stroke: #722ed1; }

.q-num {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 700;
  color: #1a1a2e;
}

.q-label { font-size: 12px; color: #888; }

@media (max-width: 1024px) {
  .kpi-row { grid-template-columns: repeat(2, 1fr); }
  .charts-grid, .bottom-grid { grid-template-columns: 1fr; }
}
</style>
