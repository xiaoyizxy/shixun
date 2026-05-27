<script>
import axios from 'axios'

export default {
  name: "ElderManagePage",
  data() {
    return {
      searchKeyword: '',
      filterLevel: '',
      elders: [],
      careLevels: ['特级护理', '一级护理', '二级护理', '三级护理'],
      dialogVisible: false,
      dialogTitle: '添加老人信息',
      form: { name: '', age: '', gender: '', phone: '', address: '', careLevel: '', nurse: '' },
      isEdit: false,
      stats: { total: 0, active: 0 }
    }
  },
  computed: {
    filteredElders() {
      let list = this.elders
      if (this.searchKeyword) {
        list = list.filter(e => e.name.includes(this.searchKeyword) || e.phone.includes(this.searchKeyword))
      }
      if (this.filterLevel) {
        list = list.filter(e => e.careLevel === this.filterLevel)
      }
      return list
    },
    activeCount() { return this.stats.active },
    totalCount() { return this.stats.total }
  },
  mounted() {
    this.fetchElders()
    this.fetchStats()
  },
  methods: {
    async fetchElders() {
      try {
        const res = await axios.get('/elder/list', {
          params: { keyword: this.searchKeyword || undefined, careLevel: this.filterLevel || undefined }
        })
        if (res.data.code === 200) this.elders = res.data.data
      } catch { this.$message.error('加载老人数据失败') }
    },
    async fetchStats() {
      try {
        const res = await axios.get('/elder/stats')
        if (res.data.code === 200) this.stats = res.data
      } catch {}
    },
    handleAdd() {
      this.dialogTitle = '添加老人信息'
      this.isEdit = false
      this.form = { name: '', age: '', gender: '', phone: '', address: '', careLevel: '', nurse: '' }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑老人信息'
      this.isEdit = true
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确定要删除该老人信息吗？', '提示', { type: 'warning' })
        .then(async () => {
          try {
            await axios.delete(`/elder/${row.id}`)
            this.$message.success('删除成功')
            this.fetchElders(); this.fetchStats()
          } catch { this.$message.error('删除失败') }
        })
        .catch(() => {})
    },
    async handleSubmit() {
      try {
        if (this.isEdit) {
          await axios.put('/elder', this.form)
          this.$message.success('修改成功')
        } else {
          await axios.post('/elder', this.form)
          this.$message.success('添加成功')
        }
        this.dialogVisible = false
        this.fetchElders(); this.fetchStats()
      } catch { this.$message.error('操作失败') }
    },
    getLevelTag(level) {
      const map = { '特级护理': 'danger', '一级护理': 'warning', '二级护理': 'primary', '三级护理': 'success' }
      return map[level] || 'info'
    }
  }
}
</script>

<template>
  <div class="elder-page">
    <div class="page-header">
      <div>
        <h2>老人信息管理</h2>
        <p>管理在册老人基本信息、护理等级和服务记录</p>
      </div>
      <el-button type="primary" round @click="handleAdd">+ 添加老人</el-button>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card">
        <span class="stat-num">{{ totalCount }}</span>
        <span class="stat-txt">在册老人</span>
      </div>
      <div class="stat-card">
        <span class="stat-num green">{{ activeCount }}</span>
        <span class="stat-txt">在住老人</span>
      </div>
      <div class="stat-card">
        <span class="stat-num orange">{{ elders.filter(e => e.careLevel === '特级护理').length }}</span>
        <span class="stat-txt">特级护理</span>
      </div>
      <div class="stat-card">
        <span class="stat-num blue">{{ elders.filter(e => e.careLevel === '一级护理').length }}</span>
        <span class="stat-txt">一级护理</span>
      </div>
    </div>

    <!-- 搜索筛选 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input v-model="searchKeyword" placeholder="搜索姓名/电话" clearable prefix-icon="search" style="width: 240px" />
        <el-select v-model="filterLevel" placeholder="护理等级" clearable style="width: 140px">
          <el-option v-for="lvl in careLevels" :key="lvl" :label="lvl" :value="lvl" />
        </el-select>
      </div>
      <span class="result-count">共 {{ filteredElders.length }} 条记录</span>
    </div>

    <!-- 老人列表 -->
    <div class="table-card">
      <el-table :data="filteredElders" stripe style="width: 100%" :header-cell-style="{ background: '#fafbfe', color: '#555', fontWeight: 500 }">
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="70" />
        <el-table-column prop="age" label="年龄" width="70" sortable />
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="address" label="住址" min-width="180" show-overflow-tooltip />
        <el-table-column prop="careLevel" label="护理等级" width="120">
          <template #default="{ row }">
            <el-tag :type="getLevelTag(row.careLevel)" size="small" effect="plain" round>{{ row.careLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="nurse" label="责任护士" width="100" />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'" size="small" effect="plain" round>
              {{ row.status === 'active' ? '在住' : '已出院' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button size="small" text type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" text type="primary">健康档案</el-button>
            <el-button size="small" text type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="姓名"><el-input v-model="form.name" placeholder="请输入姓名" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别">
              <el-select v-model="form.gender" style="width: 100%" placeholder="请选择">
                <el-option label="男" value="男" /><el-option label="女" value="女" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="年龄"><el-input-number v-model="form.age" :min="1" :max="120" style="width: 100%" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电话"><el-input v-model="form.phone" placeholder="请输入电话" /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="住址"><el-input v-model="form.address" placeholder="请输入住址" /></el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="护理等级">
              <el-select v-model="form.careLevel" style="width: 100%" placeholder="请选择">
                <el-option v-for="lvl in careLevels" :key="lvl" :label="lvl" :value="lvl" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="责任护士"><el-input v-model="form.nurse" placeholder="请输入护士" /></el-form-item>
          </el-col>
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
.elder-page { padding: 0; }

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

.toolbar-left { display: flex; gap: 12px; }

.result-count { font-size: 13px; color: #aaa; }

.table-card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

@media (max-width: 1024px) {
  .stats-row { grid-template-columns: repeat(2, 1fr); }
  .toolbar { flex-direction: column; gap: 12px; align-items: stretch; }
}
</style>
