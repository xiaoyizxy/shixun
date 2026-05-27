<script>
import axios from 'axios'

export default {
  name: "StaffManagePage",
  data() {
    return {
      searchKeyword: '',
      staffList: [],
      staffStats: { total: 0, active: 0, dayShift: 0, nightShift: 0 },
      dialogVisible: false,
      dialogTitle: '添加护理人员',
      form: { name: '', title: '', phone: '', dept: '', schedule: '', cert: '' },
      isEdit: false
    }
  },
  computed: {
    filteredStaff() {
      if (!this.searchKeyword) return this.staffList
      const kw = this.searchKeyword.toLowerCase()
      return this.staffList.filter(s => s.name.includes(kw) || s.dept.includes(kw) || s.title.includes(kw))
    }
  },
  mounted() {
    this.fetchStaff()
    this.fetchStats()
  },
  methods: {
    async fetchStaff() {
      try {
        const res = await axios.get('/staff/list', { params: { keyword: this.searchKeyword || undefined } })
        if (res.data.code === 200) this.staffList = res.data.data
      } catch { this.$message.error('加载人员数据失败') }
    },
    async fetchStats() {
      try {
        const res = await axios.get('/staff/stats')
        if (res.data.code === 200) this.staffStats = res.data
      } catch {}
    },
    handleAdd() {
      this.dialogTitle = '添加护理人员'
      this.isEdit = false
      this.form = { name: '', title: '', phone: '', dept: '', schedule: '', cert: '' }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑护理人员'
      this.isEdit = true
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确定要删除该护理人员吗？', '提示', { type: 'warning' })
        .then(async () => {
          try {
            await axios.delete(`/staff/${row.id}`)
            this.$message.success('删除成功')
            this.fetchStaff(); this.fetchStats()
          } catch { this.$message.error('删除失败') }
        })
        .catch(() => {})
    },
    async handleSubmit() {
      try {
        if (this.isEdit) {
          await axios.put('/staff', this.form)
          this.$message.success('修改成功')
        } else {
          await axios.post('/staff', this.form)
          this.$message.success('添加成功')
        }
        this.dialogVisible = false
        this.fetchStaff(); this.fetchStats()
      } catch { this.$message.error('操作失败') }
    }
  }
}
</script>

<template>
  <div class="staff-page">
    <div class="page-header">
      <div>
        <h2>护理人员管理</h2>
        <p>管理护理人员信息、资质和排班安排</p>
      </div>
      <el-button type="primary" round @click="handleAdd">+ 添加人员</el-button>
    </div>

    <!-- 统计 -->
    <div class="stats-row">
      <div class="stat-card">
        <span class="stat-num">{{ staffStats.total }}</span>
        <span class="stat-txt">在册人员</span>
      </div>
      <div class="stat-card">
        <span class="stat-num green">{{ staffStats.active }}</span>
        <span class="stat-txt">在岗人员</span>
      </div>
      <div class="stat-card">
        <span class="stat-num orange">{{ staffStats.dayShift }}</span>
        <span class="stat-txt">白班人员</span>
      </div>
      <div class="stat-card">
        <span class="stat-num blue">{{ staffStats.nightShift }}</span>
        <span class="stat-txt">夜班人员</span>
      </div>
    </div>

    <!-- 搜索 -->
    <div class="toolbar">
      <el-input v-model="searchKeyword" placeholder="搜索姓名/科室/职称" clearable prefix-icon="search" style="width: 280px" />
      <span class="result-count">共 {{ filteredStaff.length }} 人</span>
    </div>

    <!-- 人员表格 -->
    <div class="table-card">
      <el-table :data="filteredStaff" stripe style="width: 100%" :header-cell-style="{ background: '#fafbfe', color: '#555', fontWeight: 500 }">
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="title" label="职称" width="110">
          <template #default="{ row }">
            <el-tag :type="row.title.includes('主管') ? 'warning' : 'primary'" size="small" effect="plain" round>
              {{ row.title }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="dept" label="科室" width="100" />
        <el-table-column prop="schedule" label="排班" width="90">
          <template #default="{ row }">
            <span :class="row.schedule === '白班' ? 'schedule-day' : 'schedule-night'">{{ row.schedule }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="rating" label="评分" width="90" sortable>
          <template #default="{ row }">
            <span class="rating">⭐ {{ row.rating }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="cert" label="资质证书" min-width="150" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'" size="small" effect="plain" round>
              {{ row.status === 'active' ? '在岗' : '休假' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button size="small" text type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" text type="primary">排班</el-button>
            <el-button size="small" text type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="480px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.name" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="职称"><el-input v-model="form.title" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="科室"><el-input v-model="form.dept" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="排班">
              <el-select v-model="form.schedule" style="width: 100%">
                <el-option label="白班" value="白班" /><el-option label="夜班" value="夜班" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="资质"><el-input v-model="form.cert" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.staff-page { padding: 0; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.page-header h2 { font-size: 22px; color: #1a1a2e; margin: 0 0 4px; }
.page-header p { color: #888; font-size: 14px; }

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
}

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

.result-count { font-size: 13px; color: #aaa; }

.table-card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.schedule-day { color: #1890ff; font-weight: 500; }
.schedule-night { color: #722ed1; font-weight: 500; }

.rating { color: #faad14; font-weight: 500; }

@media (max-width: 1024px) {
  .stats-row { grid-template-columns: repeat(2, 1fr); }
}
</style>
