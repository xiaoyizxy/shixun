<script>
import axios from 'axios'

export default {
  name: "NursingServicePage",
  data() {
    return {
      searchKeyword: '',
      activeCategory: 'all',
      categories: [
        { key: 'all', label: '全部' },
        { key: 'basic', label: '基础护理' },
        { key: 'rehab', label: '康复理疗' },
        { key: 'special', label: '专科护理' },
        { key: 'life', label: '生活照料' }
      ],
      services: []
    }
  },
  computed: {
    filteredServices() {
      let list = this.services
      if (this.activeCategory !== 'all') {
        list = list.filter(s => s.category === this.activeCategory)
      }
      if (this.searchKeyword) {
        const kw = this.searchKeyword.toLowerCase()
        list = list.filter(s => s.name.includes(kw) || (s.description && s.description.includes(kw)))
      }
      return list
    }
  },
  mounted() {
    this.fetchServices()
  },
  methods: {
    async fetchServices() {
      try {
        const res = await axios.get('/nursing-service/list')
        if (res.data.code === 200) this.services = res.data.data
      } catch { this.$message.error('加载服务数据失败') }
    },
    handleBooking(service) {
      // 根据当前路由判断是管理端还是用户端
      const isAdmin = this.$route.path.startsWith('/admin')
      const base = isAdmin ? '/admin/appointment' : '/home/appointment'
      this.$router.push({ path: base, query: { service: service.name, category: service.category } })
    },
    splitTags(tags) {
      if (!tags) return []
      return tags.split(',')
    }
  }
}
</script>

<template>
  <div class="nursing-page">
    <div class="page-header">
      <h2>上门护理</h2>
      <p>专业护理团队，足不出户享受优质护理服务</p>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索护理项目..."
        prefix-icon="search"
        clearable
        class="search-input"
      />
    </div>

    <!-- 分类筛选 -->
    <div class="category-tabs">
      <span
        v-for="cat in categories"
        :key="cat.key"
        class="category-tab"
        :class="{ active: activeCategory === cat.key }"
        @click="activeCategory = cat.key"
      >{{ cat.label }}</span>
    </div>

    <!-- 护理服务列表 -->
    <div class="service-grid">
      <div v-for="service in filteredServices" :key="service.id" class="service-card">
        <div class="card-header">
          <div>
            <h3>{{ service.name }}</h3>
            <div class="tags" v-if="service.tags">
              <span v-for="tag in splitTags(service.tags)" :key="tag" class="tag">{{ tag }}</span>
            </div>
          </div>
          <div class="price">
            <span class="amount">¥{{ service.price }}</span>
            <span class="unit">/{{ service.unit }}</span>
          </div>
        </div>
        <p class="card-desc">{{ service.description }}</p>
        <div class="card-footer">
          <span class="duration">
            <span class="dot"></span>
            {{ service.duration }}
          </span>
          <el-button type="primary" size="default" round @click="handleBooking(service)">立即预约</el-button>
        </div>
      </div>
    </div>

    <div v-if="filteredServices.length === 0" class="empty-state">
      <p>暂无符合条件的护理项目</p>
    </div>
  </div>
</template>

<style scoped>
.nursing-page {
  padding: 0;
}

.page-header {
  margin-bottom: 24px;
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

.search-bar {
  margin-bottom: 20px;
}

.search-input {
  max-width: 400px;
}

.category-tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.category-tab {
  padding: 6px 18px;
  border-radius: 20px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.25s;
  background: #f5f5f9;
  color: #666;
  user-select: none;
}

.category-tab:hover {
  background: #e8eef5;
  color: #1890ff;
}

.category-tab.active {
  background: #1890ff;
  color: #fff;
}

.service-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 16px;
}

.service-card {
  background: #fff;
  border-radius: 14px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  transition: all 0.25s ease;
  border: 1px solid transparent;
}

.service-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0,0,0,0.06);
  border-color: #e8eef5;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.card-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: #1a1a2e;
  margin: 0 0 6px;
}

.tags {
  display: flex;
  gap: 6px;
}

.tag {
  font-size: 10px;
  padding: 2px 8px;
  border-radius: 10px;
  background: #fff3e0;
  color: #e65100;
  font-weight: 500;
}

.tag:first-child {
  background: #e6f4ff;
  color: #1890ff;
}

.price {
  text-align: right;
  flex-shrink: 0;
}

.amount {
  font-size: 24px;
  font-weight: 700;
  color: #f5222d;
}

.unit {
  font-size: 13px;
  color: #999;
}

.card-desc {
  color: #777;
  font-size: 13px;
  line-height: 1.6;
  margin-bottom: 18px;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 16px;
  border-top: 1px solid #f5f5f9;
}

.duration {
  font-size: 13px;
  color: #888;
  display: flex;
  align-items: center;
  gap: 6px;
}

.dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #52c41a;
}

.empty-state {
  text-align: center;
  padding: 80px 0;
  color: #bbb;
}

@media (max-width: 1024px) {
  .service-grid {
    grid-template-columns: 1fr;
  }
}
</style>
