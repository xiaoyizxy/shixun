<script>
import axios from 'axios'

export default {
  name: "HomeBedPage",
  data() {
    return {
      stats: [
        { label: '总病床数', value: 0, unit: '张', color: '#1890ff', icon: '🛏️' },
        { label: '已入住', value: 0, unit: '张', color: '#52c41a', icon: '👴' },
        { label: '空闲', value: 0, unit: '张', color: '#faad14', icon: '🏠' },
        { label: '待审批', value: 0, unit: '例', color: '#ff4d4f', icon: '📋' }
      ],
      filterStatus: '',
      beds: [],
      // 新增病床弹窗
      addDialogVisible: false,
      addForm: { bedNo: '' },
      // 安排入住弹窗
      checkinDialogVisible: false,
      checkinForm: { id: null, bedNo: '', elderName: '', age: '', careLevel: '', nurse: '', checkInDate: '' },
      // 详情弹窗
      detailDialogVisible: false,
      detailBed: {},
      // 护理记录弹窗
      recordDialogVisible: false,
      recordBed: {},
      // 护理等级选项
      careLevels: ['特级护理', '一级护理', '二级护理', '三级护理'],
      // 护士列表
      nurseList: []
    }
  },
  computed: {
    filteredBeds() {
      if (!this.filterStatus) return this.beds
      return this.beds.filter(b => b.status === this.filterStatus)
    }
  },
  mounted() {
    this.fetchBeds()
    this.fetchStats()
    this.fetchNurses()
  },
  methods: {
    async fetchBeds() {
      try {
        const params = this.filterStatus ? { status: this.filterStatus } : {}
        const res = await axios.get('/bed/list', { params })
        if (res.data.code === 200) this.beds = res.data.data
      } catch { this.$message.error('加载病床数据失败') }
    },
    async fetchStats() {
      try {
        const res = await axios.get('/bed/stats')
        if (res.data.code === 200) {
          const d = res.data.data
          this.stats[0].value = d.total || 0
          this.stats[1].value = d.occupied || 0
          this.stats[2].value = d.vacant || 0
          this.stats[3].value = d.pending || 0
        }
      } catch {}
    },
    async fetchNurses() {
      try {
        const res = await axios.get('/staff/list')
        if (res.data.code === 200) {
          this.nurseList = (res.data.data || []).map(s => s.name)
        }
      } catch {}
    },
    getStatusType(status) {
      return status === 'occupied' ? 'success' : 'info'
    },
    getStatusText(status) {
      return status === 'occupied' ? '已入住' : '空闲'
    },
    // ========== 新增病床 ==========
    showAddDialog() {
      this.addForm = { bedNo: '' }
      this.addDialogVisible = true
    },
    async submitAddBed() {
      if (!this.addForm.bedNo) {
        this.$message.warning('请输入病床号')
        return
      }
      try {
        const res = await axios.post('/bed', this.addForm)
        if (res.data.code === 200) {
          this.$message.success('病床添加成功')
          this.addDialogVisible = false
          this.fetchBeds()
          this.fetchStats()
        }
      } catch { this.$message.error('添加失败') }
    },
    // ========== 安排入住 ==========
    showCheckin(bed) {
      this.checkinForm = {
        id: bed.id,
        bedNo: bed.bedNo,
        elderName: '',
        age: '',
        careLevel: '',
        nurse: '',
        checkInDate: new Date().toISOString().slice(0, 10)
      }
      this.checkinDialogVisible = true
    },
    async submitCheckin() {
      const f = this.checkinForm
      if (!f.elderName || !f.age || !f.careLevel || !f.nurse) {
        this.$message.warning('请填写完整的入住信息')
        return
      }
      try {
        const res = await axios.put('/bed', {
          id: f.id,
          bedNo: f.bedNo,
          elderName: f.elderName,
          age: Number(f.age),
          careLevel: f.careLevel,
          nurse: f.nurse,
          checkInDate: f.checkInDate,
          status: 'occupied'
        })
        if (res.data.code === 200) {
          this.$message.success('入住安排成功')
          this.checkinDialogVisible = false
          this.fetchBeds()
          this.fetchStats()
        }
      } catch { this.$message.error('入住安排失败') }
    },
    // ========== 查看详情 ==========
    showDetail(bed) {
      this.detailBed = { ...bed }
      this.detailDialogVisible = true
    },
    // ========== 护理记录 ==========
    showRecord(bed) {
      this.recordBed = { ...bed }
      this.recordDialogVisible = true
    },
    // ========== 退房 ==========
    async handleCheckout(bed) {
      try {
        await this.$confirm(`确定要为 ${bed.elderName} 办理退房吗？`, '退房确认', {
          confirmButtonText: '确定退房',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const res = await axios.put('/bed', {
          id: bed.id,
          bedNo: bed.bedNo,
          status: 'vacant',
          elderName: null,
          age: 0,
          careLevel: null,
          nurse: null,
          checkInDate: null
        })
        if (res.data.code === 200) {
          this.$message.success('退房成功')
          this.fetchBeds()
          this.fetchStats()
        }
      } catch {
        // 用户取消
      }
    },
    // ========== 删除病床 ==========
    async handleDelete(bed) {
      try {
        await this.$confirm(`确定要删除病床 ${bed.bedNo} 吗？`, '删除确认', {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const res = await axios.delete(`/bed/${bed.id}`)
        if (res.data.code === 200) {
          this.$message.success('删除成功')
          this.fetchBeds()
          this.fetchStats()
        }
      } catch {
        // 用户取消
      }
    },
    // 刷新
    refresh() {
      this.fetchBeds()
      this.fetchStats()
    }
  }
}
</script>

<template>
  <div class="home-bed-page">
    <div class="page-header">
      <div>
        <h2>居家病床</h2>
        <p>家庭病床服务管理，让老人在家享受专业的医疗照护</p>
      </div>
      <el-button @click="refresh" text>🔄 刷新</el-button>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-row">
      <div v-for="stat in stats" :key="stat.label" class="stat-card">
        <div class="stat-icon" :style="{ background: stat.color + '15', color: stat.color }">{{ stat.icon }}</div>
        <div class="stat-info">
          <span class="stat-value" :style="{ color: stat.color }">{{ stat.value }}<small>{{ stat.unit }}</small></span>
          <span class="stat-label">{{ stat.label }}</span>
        </div>
      </div>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-select v-model="filterStatus" placeholder="筛选状态" clearable style="width: 140px" size="default" @change="fetchBeds">
        <el-option label="已入住" value="occupied" />
        <el-option label="空闲" value="vacant" />
      </el-select>
      <div class="toolbar-right">
        <el-button type="primary" round @click="showAddDialog">➕ 新增病床</el-button>
      </div>
    </div>

    <!-- 病床列表 -->
    <div class="bed-grid">
      <div v-for="bed in filteredBeds" :key="bed.id" class="bed-card" :class="{ vacant: bed.status === 'vacant' }">
        <div class="bed-header">
          <span class="bed-no">{{ bed.bedNo }}</span>
          <el-tag :type="getStatusType(bed.status)" size="small" effect="plain" round>
            {{ getStatusText(bed.status) }}
          </el-tag>
        </div>
        <div class="bed-body">
          <!-- 空闲病床 -->
          <div v-if="bed.status === 'vacant'" class="vacant-hint">
            <span class="vacant-icon">🏠</span>
            <span>当前空闲</span>
            <el-button size="small" round type="primary" @click="showCheckin(bed)">安排入住</el-button>
            <el-button size="small" round type="danger" plain @click="handleDelete(bed)">删除</el-button>
          </div>
          <!-- 已入住病床 -->
          <div v-else class="bed-info">
            <div class="info-row">
              <span class="info-label">入住老人</span>
              <span class="info-value">{{ bed.elderName }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">年龄</span>
              <span class="info-value">{{ bed.age }} 岁</span>
            </div>
            <div class="info-row">
              <span class="info-label">护理等级</span>
              <span class="info-value">{{ bed.careLevel }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">责任护士</span>
              <span class="info-value">{{ bed.nurse }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">入住日期</span>
              <span class="info-value">{{ bed.checkInDate }}</span>
            </div>
          </div>
        </div>
        <!-- 已入住病床的操作 -->
        <div class="bed-footer" v-if="bed.status === 'occupied'">
          <el-button size="small" round @click="showDetail(bed)">查看详情</el-button>
          <el-button size="small" round @click="showRecord(bed)">护理记录</el-button>
          <el-button size="small" round type="warning" @click="handleCheckout(bed)">办理退房</el-button>
        </div>
      </div>
    </div>

    <div v-if="filteredBeds.length === 0" class="empty-state">
      <span style="font-size: 48px;">🛏️</span>
      <p>暂无病床数据</p>
    </div>

    <!-- ========== 新增病床弹窗 ========== -->
    <el-dialog v-model="addDialogVisible" title="新增病床" width="420px" destroy-on-close>
      <el-form :model="addForm" label-width="80px">
        <el-form-item label="病床号" required>
          <el-input v-model="addForm.bedNo" placeholder="如：A-109" maxlength="20" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAddBed">确认添加</el-button>
      </template>
    </el-dialog>

    <!-- ========== 安排入住弹窗 ========== -->
    <el-dialog v-model="checkinDialogVisible" title="安排入住" width="520px" destroy-on-close>
      <div class="checkin-bed-badge">病床号：{{ checkinForm.bedNo }}</div>
      <el-form :model="checkinForm" label-width="80px">
        <el-form-item label="老人姓名" required>
          <el-input v-model="checkinForm.elderName" placeholder="请输入老人姓名" />
        </el-form-item>
        <el-form-item label="年龄" required>
          <el-input-number v-model="checkinForm.age" :min="1" :max="120" style="width: 100%" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="护理等级" required>
          <el-select v-model="checkinForm.careLevel" placeholder="请选择护理等级" style="width: 100%">
            <el-option v-for="lv in careLevels" :key="lv" :label="lv" :value="lv" />
          </el-select>
        </el-form-item>
        <el-form-item label="责任护士" required>
          <el-select v-model="checkinForm.nurse" placeholder="请选择责任护士" style="width: 100%" filterable allow-create>
            <el-option v-for="n in nurseList" :key="n" :label="n" :value="n" />
          </el-select>
        </el-form-item>
        <el-form-item label="入住日期">
          <el-date-picker v-model="checkinForm.checkInDate" type="date" placeholder="选择入住日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="checkinDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCheckin">确认入住</el-button>
      </template>
    </el-dialog>

    <!-- ========== 病床详情弹窗 ========== -->
    <el-dialog v-model="detailDialogVisible" title="病床详情" width="450px">
      <div class="detail-section" v-if="detailBed.status === 'occupied'">
        <div class="detail-row">
          <span class="detail-label">病床号</span>
          <span class="detail-value">{{ detailBed.bedNo }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">入住老人</span>
          <span class="detail-value">{{ detailBed.elderName }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">年龄</span>
          <span class="detail-value">{{ detailBed.age }} 岁</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">护理等级</span>
          <span class="detail-value">{{ detailBed.careLevel }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">责任护士</span>
          <span class="detail-value">{{ detailBed.nurse }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">入住日期</span>
          <span class="detail-value">{{ detailBed.checkInDate }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">状态</span>
          <span class="detail-value"><el-tag type="success" size="small">已入住</el-tag></span>
        </div>
      </div>
      <div class="detail-section" v-else>
        <div class="detail-row">
          <span class="detail-label">病床号</span>
          <span class="detail-value">{{ detailBed.bedNo }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">状态</span>
          <span class="detail-value"><el-tag type="info" size="small">空闲</el-tag></span>
        </div>
        <p style="text-align: center; color: #bbb; margin-top: 24px;">该病床当前空闲，可安排老人入住</p>
      </div>
      <template #footer>
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- ========== 护理记录弹窗 ========== -->
    <el-dialog v-model="recordDialogVisible" title="护理记录" width="520px">
      <div class="record-header-info">
        <span><strong>老人：</strong>{{ recordBed.elderName }}</span>
        <span><strong>病床：</strong>{{ recordBed.bedNo }}</span>
        <span><strong>护士：</strong>{{ recordBed.nurse }}</span>
      </div>
      <el-timeline style="margin-top: 20px;">
        <el-timeline-item timestamp="2026-05-27 08:30" placement="top" type="primary">
          <div class="record-card">
            <p class="record-title">晨间护理</p>
            <p class="record-detail">测量生命体征，血压 128/82 mmHg，心率 76次/分，体温 36.5°C。协助洗漱、更换衣物。</p>
          </div>
        </el-timeline-item>
        <el-timeline-item timestamp="2026-05-26 14:00" placement="top" type="success">
          <div class="record-card">
            <p class="record-title">康复训练</p>
            <p class="record-detail">完成30分钟关节活动度训练，患者配合良好，无不适反应。</p>
          </div>
        </el-timeline-item>
        <el-timeline-item timestamp="2026-05-26 08:30" placement="top" type="primary">
          <div class="record-card">
            <p class="record-title">晨间护理</p>
            <p class="record-detail">测量生命体征，血压 130/85 mmHg，心率 72次/分。协助服药、早餐。</p>
          </div>
        </el-timeline-item>
        <el-timeline-item timestamp="2026-05-25 16:00" placement="top" type="warning">
          <div class="record-card">
            <p class="record-title">压疮护理</p>
            <p class="record-detail">骶尾部压疮换药，创面恢复良好，无明显渗液，更换敷料。</p>
          </div>
        </el-timeline-item>
      </el-timeline>
      <template #footer>
        <el-button type="primary" @click="recordDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.home-bed-page { padding: 0; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}
.page-header h2 { font-size: 22px; color: #1a1a2e; margin: 0 0 4px; }
.page-header p { color: #888; font-size: 14px; margin: 0; }

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  border-radius: 14px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  transition: all 0.25s ease;
}
.stat-card:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.06); }

.stat-icon {
  font-size: 28px;
  width: 52px;
  height: 52px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stat-info { display: flex; flex-direction: column; gap: 2px; }

.stat-value {
  font-size: 26px;
  font-weight: 700;
}

.stat-value small {
  font-size: 14px;
  font-weight: 400;
  color: #999;
}

.stat-label {
  font-size: 13px;
  color: #888;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.toolbar-right { display: flex; gap: 8px; }

.bed-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
}

.bed-card {
  background: #fff;
  border-radius: 14px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  transition: all 0.25s ease;
  display: flex;
  flex-direction: column;
}

.bed-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0,0,0,0.06);
}

.bed-card.vacant {
  background: #fafbfe;
  border: 1px dashed #e0e4ee;
}

.bed-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f5f5f9;
}

.bed-no {
  font-size: 16px;
  font-weight: 700;
  color: #1a1a2e;
}

.bed-body { flex: 1; min-height: 100px; }

.vacant-hint {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  color: #bbb;
  font-size: 13px;
  padding: 20px 0;
}

.vacant-icon { font-size: 32px; }

.bed-info { display: flex; flex-direction: column; gap: 8px; }

.info-row {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}

.info-label { color: #888; }
.info-value { color: #333; font-weight: 500; }

.bed-footer {
  display: flex;
  gap: 8px;
  padding-top: 14px;
  border-top: 1px solid #f5f5f9;
  margin-top: 14px;
  flex-wrap: wrap;
}

.empty-state {
  text-align: center;
  padding: 80px 0;
  color: #bbb;
}
.empty-state p { margin-top: 12px; font-size: 15px; }

/* 入住弹窗 */
.checkin-bed-badge {
  background: #e6f4ff;
  color: #1677cc;
  padding: 10px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 20px;
  text-align: center;
}

/* 详情弹窗 */
.detail-section { padding: 8px 0; }
.detail-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #f5f5f9;
  font-size: 14px;
}
.detail-row:last-child { border-bottom: none; }
.detail-label { color: #888; }
.detail-value { color: #333; font-weight: 500; }

/* 护理记录 */
.record-header-info {
  display: flex;
  gap: 20px;
  padding: 12px 16px;
  background: #fafbfe;
  border-radius: 10px;
  font-size: 13px;
  flex-wrap: wrap;
}
.record-card { padding: 4px 0; }
.record-title { font-size: 14px; font-weight: 600; color: #1a1a2e; margin-bottom: 4px; }
.record-detail { font-size: 13px; color: #666; line-height: 1.6; }

@media (max-width: 1024px) {
  .stats-row { grid-template-columns: repeat(2, 1fr); }
  .bed-grid { grid-template-columns: 1fr; }
}
</style>
