<script>
import axios from 'axios'

export default {
  name: "DataDictPage",
  data() {
    return {
      dicts: [],
      expandedDict: '',
      searchKeyword: ''
    }
  },
  computed: {
    filteredDicts() {
      if (!this.searchKeyword) return this.dicts
      return this.dicts.filter(d => d.name.includes(this.searchKeyword) || d.code.includes(this.searchKeyword))
    }
  },
  mounted() {
    this.fetchDicts()
  },
  methods: {
    async fetchDicts() {
      try {
        const res = await axios.get('/dict/list')
        if (res.data.code === 200) this.dicts = res.data.data
      } catch { this.$message.error('加载字典数据失败') }
    },
    toggleExpand(code) {
      this.expandedDict = this.expandedDict === code ? '' : code
    }
  }
}
</script>

<template>
  <div class="dict-page">
    <div class="page-header">
      <div>
        <h2>数据字典</h2>
        <p>管理系统中的枚举字典和数据字典项</p>
      </div>
      <el-button type="primary" round>+ 新建字典</el-button>
    </div>

    <!-- 搜索 -->
    <div class="toolbar">
      <el-input v-model="searchKeyword" placeholder="搜索字典名称/编码" clearable prefix-icon="search" style="width: 280px" />
    </div>

    <!-- 字典列表 -->
    <div class="dict-list">
      <div v-for="dict in filteredDicts" :key="dict.code" class="dict-card">
        <div class="dict-header" @click="toggleExpand(dict.code)">
          <div class="dict-title">
            <span class="dict-icon">📖</span>
            <div>
              <div class="dict-name">{{ dict.name }}</div>
              <div class="dict-code">{{ dict.code }}</div>
            </div>
          </div>
          <div class="dict-actions">
            <el-button size="small" text type="primary" @click.stop>添加项</el-button>
            <el-button size="small" text type="primary" @click.stop>编辑</el-button>
            <span class="expand-icon" :class="{ expanded: expandedDict === dict.code }">▾</span>
          </div>
        </div>
        <div v-show="expandedDict === dict.code" class="dict-items">
          <div v-for="item in dict.items" :key="item.id" class="dict-item">
            <span class="item-label">{{ item.label }}</span>
            <span class="item-value">{{ item.value }}</span>
            <span class="item-sort">排序: {{ item.sort }}</span>
            <el-tag size="small" effect="plain" round type="success">启用</el-tag>
            <div class="item-actions">
              <el-button size="small" text type="primary">编辑</el-button>
              <el-button size="small" text type="danger">删除</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dict-page { padding: 0; }

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.page-header h2 { font-size: 22px; color: #1a1a2e; margin: 0 0 4px; }
.page-header p { color: #888; font-size: 14px; }

.toolbar { margin-bottom: 20px; }

.dict-list { display: flex; flex-direction: column; gap: 12px; }

.dict-card {
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  overflow: hidden;
}

.dict-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 24px;
  cursor: pointer;
  transition: background 0.2s;
  user-select: none;
}

.dict-header:hover { background: #fafbfe; }

.dict-title { display: flex; align-items: center; gap: 14px; }

.dict-icon { font-size: 24px; }

.dict-name { font-size: 15px; font-weight: 600; color: #1a1a2e; }
.dict-code { font-size: 12px; color: #888; margin-top: 2px; font-family: monospace; }

.dict-actions { display: flex; align-items: center; gap: 4px; }

.expand-icon {
  font-size: 14px;
  color: #999;
  transition: transform 0.25s;
  margin-left: 8px;
}

.expand-icon.expanded { transform: rotate(180deg); }

.dict-items {
  border-top: 1px solid #f5f5f9;
  padding: 8px 24px 16px;
  animation: fadeDown 0.25s ease;
}

@keyframes fadeDown {
  from { opacity: 0; max-height: 0; }
  to { opacity: 1; max-height: 500px; }
}

.dict-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 10px 0;
  border-bottom: 1px solid #fafafd;
}

.dict-item:last-child { border-bottom: none; }

.item-label { font-weight: 500; color: #333; min-width: 100px; font-size: 14px; }
.item-value { color: #888; font-size: 13px; font-family: monospace; background: #f5f5f9; padding: 2px 8px; border-radius: 4px; }
.item-sort { color: #bbb; font-size: 12px; }

.item-actions { margin-left: auto; }

@media (max-width: 1024px) {
  .dict-item { flex-wrap: wrap; gap: 8px; }
}
</style>
