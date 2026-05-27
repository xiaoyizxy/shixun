<script>
import axios from 'axios'

export default {
  name: "SmartRecognitionPage",
  data() {
    return {
      imageUrl: '',
      uploading: false,
      result: null,
      analysisType: '皮肤创面',
      description: '',
      typeOptions: [
        { label: '皮肤创面分析', value: '皮肤创面' },
        { label: '药品识别', value: '药品识别' },
        { label: '体征数据分析', value: '体征数据分析' },
        { label: '康复评估', value: '康复评估' },
        { label: '营养状况', value: '营养状况' }
      ],
      history: []
    }
  },
  methods: {
    handleUpload(file) {
      this.uploading = true
      const reader = new FileReader()
      reader.onload = (e) => {
        this.imageUrl = e.target.result
        this.uploading = false
        this.result = null
      }
      reader.readAsDataURL(file.raw || file)
      return false
    },
    async startAnalysis() {
      if (!this.description.trim()) {
        this.$message.warning('请描述需要分析的内容')
        return
      }
      this.uploading = true
      this.result = null
      try {
        const res = await axios.get('/ai/customer/analyze', {
          params: {
            userId: 'default_user',
            type: this.analysisType,
            description: this.description
          }
        })
        const text = res.data?.data || '分析失败，请重试'
        const parsed = this.parseAnalysisResult(text)
        this.result = parsed
        this.history.unshift({
          id: Date.now(),
          name: this.analysisType + '分析',
          date: new Date().toLocaleString('zh-CN'),
          result: parsed.detail?.substring(0, 30) || text.substring(0, 30),
          confidence: 85
        })
      } catch (e) {
        this.$message.error('分析请求失败，请稍后重试')
      } finally {
        this.uploading = false
      }
    },
    parseAnalysisResult(text) {
      const detailMatch = text.match(/\*\*详细描述\*\*[：:]?\s*([\s\S]*?)(?=\d+\.\s*\*\*|\*\*护理建议|$)/)
      const adviceMatch = text.match(/\*\*护理建议\*\*[：:]?\s*([\s\S]*?)$/)
      return {
        type: this.analysisType,
        detail: detailMatch ? detailMatch[1].trim() : text,
        advice: adviceMatch ? adviceMatch[1].trim() : '请参考详细描述中的建议。'
      }
    },
    clearImage() {
      this.imageUrl = ''
      this.result = null
      this.description = ''
    }
  }
}
</script>

<template>
  <div class="recognition-page">
    <div class="page-header">
      <h2>AI 智能识别</h2>
      <p>上传图片进行智能分析，支持皮肤创面、药品、体征数据识别</p>
    </div>

    <div class="content-grid">
      <!-- 左侧：上传区 -->
      <div class="main-section">
        <div class="upload-area" v-if="!imageUrl">
          <el-upload
            drag
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handleUpload"
            accept="image/*"
          >
            <div class="upload-content">
              <div class="upload-icon">📷</div>
              <div class="upload-text">拖拽或点击上传图片</div>
              <div class="upload-hint">支持 JPG、PNG 格式，文件大小不超过 10MB</div>
            </div>
          </el-upload>
        </div>

        <div v-else class="preview-section">
          <div class="preview-image">
            <img :src="imageUrl" alt="预览" />
          </div>
          <div class="analysis-config">
            <div class="config-row">
              <span class="config-label">分析类型</span>
              <el-select v-model="analysisType" size="default" style="width: 200px">
                <el-option
                  v-for="opt in typeOptions"
                  :key="opt.value"
                  :label="opt.label"
                  :value="opt.value"
                />
              </el-select>
            </div>
            <div class="config-row">
              <span class="config-label">补充描述</span>
              <el-input
                v-model="description"
                type="textarea"
                :rows="3"
                placeholder="请描述图片中的情况，例如：创面大小、颜色、位置等..."
              />
            </div>
            <div class="config-actions">
              <el-button @click="clearImage" round>重新上传</el-button>
              <el-button type="primary" @click="startAnalysis" :loading="uploading" round>
                开始分析
              </el-button>
            </div>
          </div>
        </div>

        <!-- 识别结果 -->
        <div v-if="result" class="result-card animate-in">
          <div class="result-header">
            <span class="result-icon">✅</span>
            <span class="result-title">AI 识别结果</span>
          </div>
          <div class="result-body">
            <div class="result-item">
              <span class="result-label">分析类型</span>
              <el-tag size="small" effect="plain">{{ result.type }}</el-tag>
            </div>
            <div class="result-item">
              <span class="result-label">详细描述</span>
              <span class="result-value">{{ result.detail }}</span>
            </div>
            <div class="result-item">
              <span class="result-label">护理建议</span>
              <span class="result-value advice">{{ result.advice }}</span>
            </div>
          </div>
        </div>

        <div v-if="uploading" class="loading-state">
          <span class="loading-spinner"></span>
          AI 正在分析中...
        </div>
      </div>

      <!-- 右侧：识别历史 -->
      <div class="side-section">
        <div class="side-card">
          <h3>识别历史</h3>
          <div class="history-list">
            <div v-for="item in history" :key="item.id" class="history-item">
              <div class="history-info">
                <span class="history-name">{{ item.name }}</span>
                <span class="history-date">{{ item.date }}</span>
              </div>
              <div class="history-result">
                <span class="result-text">{{ item.result }}</span>
                <span class="confidence">{{ item.confidence }}%</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.recognition-page { padding: 0; }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 22px; color: #1a1a2e; margin: 0 0 4px; }
.page-header p { color: #888; font-size: 14px; }

.content-grid {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 24px;
  align-items: start;
}

.main-section { display: flex; flex-direction: column; gap: 16px; }

.upload-area {
  background: #fff;
  border-radius: 16px;
  padding: 48px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.upload-content { text-align: center; }

.upload-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.upload-text {
  font-size: 15px;
  color: #555;
  margin-bottom: 8px;
}

.upload-hint {
  font-size: 12px;
  color: #bbb;
}

.preview-section {
  background: #fff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.preview-image {
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 16px;
}

.preview-image img {
  width: 100%;
  max-height: 240px;
  object-fit: contain;
  background: #fafbfe;
  border-radius: 10px;
}

.analysis-config {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.config-row {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.config-label {
  font-size: 13px;
  color: #888;
  min-width: 70px;
  padding-top: 6px;
  flex-shrink: 0;
}

.config-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 4px;
}

.preview-actions { text-align: center; }

.result-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  border-left: 4px solid #52c41a;
}

.result-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f5f5f9;
}

.result-icon { font-size: 20px; }
.result-title { font-weight: 600; color: #1a1a2e; font-size: 15px; }

.result-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 10px 0;
  font-size: 14px;
}

.result-label { color: #888; min-width: 70px; flex-shrink: 0; }
.result-value { color: #555; line-height: 1.5; }
.result-value.advice { color: #1890ff; font-weight: 500; }

.loading-state {
  text-align: center;
  padding: 32px;
  color: #888;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid #e5e7eb;
  border-top-color: #1890ff;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin { to { transform: rotate(360deg); } }

.side-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.side-card h3 { font-size: 16px; color: #1a1a2e; margin: 0 0 16px; font-weight: 600; }

.history-item {
  padding: 14px 0;
  border-bottom: 1px solid #f5f5f9;
}

.history-item:last-child { border-bottom: none; }

.history-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.history-name { font-size: 13px; font-weight: 500; color: #333; }
.history-date { font-size: 11px; color: #bbb; }

.history-result {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.result-text { font-size: 13px; color: #52c41a; }
.confidence { font-size: 12px; color: #1890ff; font-weight: 500; }

.animate-in { animation: fadeUp 0.35s ease; }

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 1024px) {
  .content-grid { grid-template-columns: 1fr; }
}
</style>
