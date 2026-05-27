<script>
import axios from 'axios'

export default {
  name: "OrderManagePage",
  data() {
    return {
      searchKeyword: '',
      filterStatus: '',
      orders: [],
      statuses: [
        { value: 'pending', label: '待接单' },
        { value: 'in-progress', label: '服务中' },
        { value: 'completed', label: '已完成' },
        { value: 'cancelled', label: '已取消' }
      ],
      orderStats: { total: 0, pending: 0, completed: 0, revenue: 0 },
      detailVisible: false,
      currentOrder: null
    }
  },
  computed: {
    filteredOrders() {
      let list = this.orders
      if (this.searchKeyword) {
        const kw = this.searchKeyword.toLowerCase()
        list = list.filter(o =>
          (o.orderNo && o.orderNo.toLowerCase().includes(kw)) ||
          (o.elderName && o.elderName.includes(kw)) ||
          (o.service && o.service.includes(kw))
        )
      }
      if (this.filterStatus) {
        list = list.filter(o => o.status === this.filterStatus)
      }
      return list
    }
  },
  mounted() {
    this.fetchOrders()
    this.fetchStats()
  },
  methods: {
    async fetchOrders() {
      try {
        const params = {}
        if (this.searchKeyword) params.keyword = this.searchKeyword
        if (this.filterStatus) params.status = this.filterStatus
        const res = await axios.get('/order/list', { params })
        if (res.data.code === 200) this.orders = res.data.data
      } catch { this.$message.error('加载订单数据失败') }
    },
    async fetchStats() {
      try {
        const res = await axios.get('/order/stats')
        if (res.data.code === 200) {
          const d = res.data.data
          this.orderStats = {
            total: d.total || 0,
            pending: d.pending || 0,
            completed: d.completed || 0,
            revenue: d.revenue || 0
          }
        }
      } catch {}
    },
    getStatusType(status) {
      return { pending: 'warning', 'in-progress': '', completed: 'success', cancelled: 'info' }[status] || 'info'
    },
    getStatusText(status) {
      return { pending: '待接单', 'in-progress': '服务中', completed: '已完成', cancelled: '已取消' }[status] || status
    },
    // 查看详情
    viewDetail(row) {
      this.currentOrder = row
      this.detailVisible = true
    },
    // 接单：pending → in-progress
    async acceptOrder(row) {
      try {
        await this.$confirm(`确定要接单吗？`, '接单确认', {
          confirmButtonText: '接单',
          cancelButtonText: '取消',
          type: 'info'
        })
        const res = await axios.put(`/order/${row.id}/status?status=in-progress`)
        if (res.data.code === 200) {
          this.$message.success('已接单，服务开始')
          this.refreshAll()
        }
      } catch { /* 取消 */ }
    },
    // 完成：→ completed
    async completeOrder(row) {
      try {
        await this.$confirm('确定该订单已完成吗？', '完成确认', {
          confirmButtonText: '确认完成',
          cancelButtonText: '取消',
          type: 'success'
        })
        const res = await axios.put(`/order/${row.id}/status?status=completed`)
        if (res.data.code === 200) {
          this.$message.success('订单已完成')
          this.refreshAll()
        }
      } catch { /* 取消 */ }
    },
    // 取消订单
    async cancelOrder(row) {
      try {
        await this.$confirm('确定要取消该订单吗？', '取消确认', {
          confirmButtonText: '确定取消',
          cancelButtonText: '返回',
          type: 'warning'
        })
        const res = await axios.put(`/order/${row.id}/status?status=cancelled`)
        if (res.data.code === 200) {
          this.$message.success('订单已取消')
          this.refreshAll()
        }
      } catch { /* 取消 */ }
    },
    refreshAll() {
      this.fetchOrders()
      this.fetchStats()
    }
  }
}
</script>

<template>
  <div class="order-page">
    <div class="page-header">
      <div>
        <h2>服务订单管理</h2>
        <p>查看和管理所有护理服务订单的状态</p>
      </div>
      <el-button @click="refreshAll" text>🔄 刷新</el-button>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card">
        <span class="stat-num">{{ orderStats.total }}</span>
        <span class="stat-txt">总订单</span>
      </div>
      <div class="stat-card">
        <span class="stat-num orange">{{ orderStats.pending }}</span>
        <span class="stat-txt">待接单</span>
      </div>
      <div class="stat-card">
        <span class="stat-num green">{{ orderStats.completed }}</span>
        <span class="stat-txt">已完成</span>
      </div>
      <div class="stat-card">
        <span class="stat-num blue">¥{{ orderStats.revenue }}</span>
        <span class="stat-txt">服务收入</span>
      </div>
    </div>

    <!-- 搜索筛选 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input v-model="searchKeyword" placeholder="搜索订单号/老人/服务" clearable prefix-icon="search" style="width: 260px" @keyup.enter="fetchOrders" />
        <el-select v-model="filterStatus" placeholder="订单状态" clearable style="width: 140px" @change="fetchOrders">
          <el-option v-for="s in statuses" :key="s.value" :label="s.label" :value="s.value" />
        </el-select>
        <el-button @click="fetchOrders">搜索</el-button>
      </div>
      <span class="result-count">共 {{ filteredOrders.length }} 个订单</span>
    </div>

    <!-- 订单表格 -->
    <div class="table-card">
      <el-table :data="filteredOrders" stripe style="width: 100%" :header-cell-style="{ background: '#fafbfe', color: '#555', fontWeight: 500 }">
        <el-table-column prop="orderNo" label="订单编号" width="190">
          <template #default="{ row }">
            <span class="order-no">{{ row.orderNo || row.id }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="elderName" label="老人姓名" width="100" />
        <el-table-column prop="service" label="服务项目" width="140" />
        <el-table-column prop="staff" label="护理人员" width="100">
          <template #default="{ row }">
            {{ row.staff || '未分配' }}
          </template>
        </el-table-column>
        <el-table-column prop="date" label="服务日期" width="120" sortable />
        <el-table-column prop="amount" label="金额" width="100" sortable>
          <template #default="{ row }">
            <span class="amount-text">¥{{ row.amount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" size="small" effect="plain" round>
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" text type="primary" @click="viewDetail(row)">详情</el-button>
            <el-button v-if="row.status === 'pending'" size="small" type="primary" @click="acceptOrder(row)">接单</el-button>
            <el-button v-if="row.status === 'in-progress'" size="small" type="success" @click="completeOrder(row)">完成</el-button>
            <el-button v-if="row.status === 'pending'" size="small" text type="danger" @click="cancelOrder(row)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="filteredOrders.length === 0" class="empty-state">
        <span style="font-size: 40px;">📦</span>
        <p>暂无订单记录</p>
      </div>
    </div>

    <!-- 订单详情弹窗 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="500px" destroy-on-close>
      <div class="detail-card" v-if="currentOrder">
        <div class="detail-row">
          <span class="d-label">订单编号</span>
          <span class="d-value order-no">{{ currentOrder.orderNo || currentOrder.id }}</span>
        </div>
        <div class="detail-row" v-if="currentOrder.appointmentId">
          <span class="d-label">关联预约</span>
          <span class="d-value">#{{ currentOrder.appointmentId }}</span>
        </div>
        <div class="detail-row">
          <span class="d-label">老人姓名</span>
          <span class="d-value">{{ currentOrder.elderName }}</span>
        </div>
        <div class="detail-row">
          <span class="d-label">服务项目</span>
          <span class="d-value">{{ currentOrder.service }}</span>
        </div>
        <div class="detail-row">
          <span class="d-label">护理人员</span>
          <span class="d-value">{{ currentOrder.staff || '未分配' }}</span>
        </div>
        <div class="detail-row">
          <span class="d-label">服务日期</span>
          <span class="d-value">{{ currentOrder.date }}</span>
        </div>
        <div class="detail-row">
          <span class="d-label">服务金额</span>
          <span class="d-value amount">¥{{ currentOrder.amount }}</span>
        </div>
        <div class="detail-row">
          <span class="d-label">订单状态</span>
          <el-tag :type="getStatusType(currentOrder.status)" size="small" effect="plain" round>
            {{ getStatusText(currentOrder.status) }}
          </el-tag>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.order-page { padding: 0; }

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
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 14px;
  padding: 18px 20px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  display: flex;
  flex-direction: column;
  gap: 4px;
  transition: all 0.25s ease;
}
.stat-card:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.06); }

.stat-num { font-size: 28px; font-weight: 700; color: #1a1a2e; }
.stat-num.green { color: #52c41a; }
.stat-num.orange { color: #faad14; }
.stat-num.blue { color: #1890ff; }
.stat-txt { font-size: 13px; color: #888; }

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.toolbar-left { display: flex; gap: 12px; align-items: center; }

.result-count { font-size: 13px; color: #aaa; }

.table-card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.amount-text { color: #f5222d; font-weight: 500; }

.order-no {
  font-family: 'Courier New', monospace;
  font-size: 12px;
  color: #1677cc;
}

.empty-state {
  text-align: center;
  padding: 60px 0;
  color: #bbb;
}
.empty-state p { margin-top: 10px; }

.detail-card { display: flex; flex-direction: column; gap: 14px; padding: 8px 0; }

.detail-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f5f5f9;
}
.detail-row:last-child { border-bottom: none; }

.d-label { color: #888; font-size: 14px; }
.d-value { font-weight: 500; color: #333; font-size: 14px; }
.d-value.amount { color: #f5222d; font-size: 18px; font-weight: 700; }

@media (max-width: 1024px) {
  .stats-row { grid-template-columns: repeat(2, 1fr); }
  .toolbar { flex-direction: column; gap: 12px; align-items: stretch; }
}
</style>
