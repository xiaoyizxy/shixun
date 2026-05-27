<script>
import axios from 'axios'

export default {
  name: "UserMessage",
  data() {
    return {
      users: [],
      searchKeyword: '',
      dialogVisible: false,
      dialogTitle: '添加用户',
      form: { username: '', realname: '', role: '', phone: '', email: '', password: '' },
      isEdit: false,
      roles: ['超级管理员', '护理管理', '医生', '运营管理', '护理人员']
    }
  },
  computed: {
    filteredUsers() {
      if (!this.searchKeyword) return this.users
      return this.users.filter(u => u.username.includes(this.searchKeyword) || (u.realname || '').includes(this.searchKeyword) || (u.role || '').includes(this.searchKeyword))
    }
  },
  mounted() {
    this.fetchUsers()
  },
  methods: {
    async fetchUsers() {
      try {
        const res = await axios.get('/user/list')
        if (res.data.code === 200) this.users = res.data.data
      } catch { this.$message.error('加载用户数据失败') }
    },
    handleAdd() {
      this.dialogTitle = '添加用户'
      this.isEdit = false
      this.form = { username: '', realname: '', role: '', phone: '', email: '', password: '' }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑用户'
      this.isEdit = true
      this.form = { ...row, password: '' }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确定要删除该用户吗？', '提示', { type: 'warning' })
        .then(async () => {
          try {
            await axios.delete(`/user/${row.id}`)
            this.$message.success('删除成功')
            this.fetchUsers()
          } catch { this.$message.error('删除失败') }
        })
        .catch(() => {})
    },
    async handleSubmit() {
      try {
        if (this.isEdit) {
          await axios.put('/user', this.form)
          this.$message.success('修改成功')
        } else {
          await axios.post('/user', this.form)
          this.$message.success('添加成功')
        }
        this.dialogVisible = false
        this.fetchUsers()
      } catch { this.$message.error('操作失败') }
    }
  }
}
</script>

<template>
  <div class="user-page">
    <div class="page-header">
      <div>
        <h2>用户信息</h2>
        <p>管理系统用户账号、角色和权限</p>
      </div>
      <el-button type="primary" round @click="handleAdd">+ 添加用户</el-button>
    </div>

    <!-- 搜索 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input v-model="searchKeyword" placeholder="搜索用户名/姓名/角色" clearable prefix-icon="search" style="width: 280px" />
      </div>
      <span class="result-count">共 {{ filteredUsers.length }} 个用户</span>
    </div>

    <!-- 用户表格 -->
    <div class="table-card">
      <el-table :data="filteredUsers" stripe style="width: 100%" :header-cell-style="{ background: '#fafbfe', color: '#555', fontWeight: 500 }">
        <el-table-column prop="username" label="用户名" width="130" />
        <el-table-column prop="realname" label="姓名" width="120" />
        <el-table-column prop="role" label="角色" width="130">
          <template #default="{ row }">
            <el-tag :type="row.role === '超级管理员' ? 'danger' : row.role === '护理管理' ? 'warning' : 'primary'" size="small" effect="plain" round>
              {{ row.role }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="createTime" label="创建时间" width="130" sortable />
        <el-table-column prop="status" label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'" size="small" effect="plain" round>
              {{ row.status === 'active' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" text type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" text type="primary">权限</el-button>
            <el-button size="small" text type="danger" @click="handleDelete(row)" :disabled="row.role === '超级管理员'">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 用户弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="480px" destroy-on-close>
      <el-form :model="form" label-width="80px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="姓名"><el-input v-model="form.realname" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="角色">
          <el-select v-model="form.role" style="width: 100%" placeholder="选择角色">
            <el-option v-for="r in roles" :key="r" :label="r" :value="r" />
          </el-select>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="电话"><el-input v-model="form.phone" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="密码" v-if="!isEdit">
          <el-input v-model="form.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="新密码" v-else>
          <el-input v-model="form.password" type="password" show-password placeholder="留空则不修改" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.user-page { padding: 0; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.page-header h2 { font-size: 22px; color: #1a1a2e; margin: 0 0 4px; }
.page-header p { color: #888; font-size: 14px; }

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
</style>
