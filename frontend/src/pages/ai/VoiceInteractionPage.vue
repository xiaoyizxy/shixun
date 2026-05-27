<script>
import axios from 'axios'

export default {
  name: "VoiceInteractionPage",
  data() {
    return {
      isListening: false,
      transcript: '',
      inputText: '',
      responses: [],
      status: 'idle',
      recognition: null
    }
  },
  mounted() {
    this.initSpeechRecognition()
  },
  methods: {
    initSpeechRecognition() {
      const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition
      if (SpeechRecognition) {
        this.recognition = new SpeechRecognition()
        this.recognition.lang = 'zh-CN'
        this.recognition.interimResults = false
        this.recognition.continuous = false
        this.recognition.onresult = (event) => {
          const text = event.results[0][0].transcript
          this.transcript = text
          this.isListening = false
          this.handleRecognized(text)
        }
        this.recognition.onerror = () => {
          this.isListening = false
          this.status = 'idle'
          this.$message.warning('语音识别失败，请使用文字输入')
        }
        this.recognition.onend = () => {
          this.isListening = false
          if (this.status === 'listening') {
            this.status = 'idle'
          }
        }
      }
    },
    toggleListening() {
      if (this.isListening) {
        this.stopListening()
      } else {
        this.startListening()
      }
    },
    startListening() {
      if (this.recognition) {
        this.isListening = true
        this.status = 'listening'
        this.recognition.start()
      } else {
        // 浏览器不支持语音识别，使用模拟
        this.simulateListening()
      }
    },
    simulateListening() {
      this.isListening = true
      this.status = 'listening'
      this.transcript = ''

      setTimeout(() => {
        const phrases = [
          '请帮我查一下今天的护理安排',
          '张奶奶的血压是多少',
          '预约明天上午的上门巡诊',
          '有哪些护理项目可以选择'
        ]
        const phrase = phrases[Math.floor(Math.random() * phrases.length)]
        this.transcript = phrase
        this.isListening = false
        this.handleRecognized(phrase)
      }, 2000)
    },
    stopListening() {
      if (this.recognition) {
        this.recognition.stop()
      }
      this.isListening = false
      this.status = 'idle'
    },
    async sendText() {
      const text = this.inputText.trim()
      if (!text) return
      this.transcript = text
      this.inputText = ''
      await this.handleRecognized(text)
    },
    async handleRecognized(text) {
      this.status = 'processing'
      this.responses.push({ role: 'user', content: text })

      try {
        const res = await axios.get('/ai/customer/chat', {
          params: { userId: 'default_user', message: text }
        })
        const reply = res.data?.data || '抱歉，我暂时无法回答。'
        this.responses.push({ role: 'assistant', content: reply })
      } catch (e) {
        this.responses.push({ role: 'assistant', content: '网络异常，请稍后重试。' })
      } finally {
        this.status = 'idle'
      }
    },
    clearHistory() {
      this.responses = []
      this.transcript = ''
      this.inputText = ''
    }
  }
}
</script>

<template>
  <div class="voice-page">
    <div class="page-header">
      <h2>AI 语音交互</h2>
      <p>通过语音与AI助手交流，查询信息、预约服务更加便捷</p>
    </div>

    <div class="voice-container">
      <!-- 语音交互区 -->
      <div class="voice-main">
        <div class="mic-area">
          <div
            class="mic-button"
            :class="{ listening: isListening, processing: status === 'processing' }"
            @click="toggleListening"
          >
            <span class="mic-icon">🎤</span>
            <div v-if="isListening" class="mic-pulse"></div>
          </div>
          <div class="mic-status">
            <span v-if="status === 'idle'" class="status-text idle">点击麦克风开始说话</span>
            <span v-else-if="status === 'listening'" class="status-text listening">正在聆听...</span>
            <span v-else-if="status === 'processing'" class="status-text processing">AI 正在处理...</span>
          </div>
          <div v-if="transcript" class="transcript-box">
            <span class="transcript-label">识别文字：</span>
            <span class="transcript-text">{{ transcript }}</span>
          </div>

          <!-- 文字输入备用 -->
          <div class="text-input-row">
            <el-input
              v-model="inputText"
              placeholder="或直接输入文字..."
              :disabled="status === 'processing'"
              @keyup.enter="sendText"
              clearable
            >
              <template #append>
                <el-button :disabled="!inputText.trim() || status === 'processing'" @click="sendText">
                  发送
                </el-button>
              </template>
            </el-input>
          </div>
        </div>
      </div>

      <!-- 对话历史 -->
      <div class="conversation-section" v-if="responses.length">
        <div class="conversation-header">
          <h3>对话记录</h3>
          <el-button text size="small" @click="clearHistory">清空</el-button>
        </div>
        <div class="conversation-list">
          <div
            v-for="(msg, idx) in responses"
            :key="idx"
            class="conversation-item"
            :class="msg.role"
          >
            <span class="conv-role">{{ msg.role === 'user' ? '🗣️ 您' : '🤖 AI' }}</span>
            <span class="conv-content">{{ msg.content }}</span>
          </div>
        </div>
      </div>

      <!-- 提示卡片 -->
      <div class="tips-row" v-if="responses.length === 0">
        <div class="tip-card">
          <span class="tip-icon">💡</span>
          <div class="tip-text">试试说："帮我查一下今天的护理安排"</div>
        </div>
        <div class="tip-card">
          <span class="tip-icon">📋</span>
          <div class="tip-text">试试说："有哪些护理项目可以选择"</div>
        </div>
        <div class="tip-card">
          <span class="tip-icon">🏥</span>
          <div class="tip-text">试试说："预约明天上午的上门巡诊"</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.voice-page { padding: 0; }

.page-header { margin-bottom: 24px; }
.page-header h2 { font-size: 22px; color: #1a1a2e; margin: 0 0 4px; }
.page-header p { color: #888; font-size: 14px; }

.voice-container { display: flex; flex-direction: column; gap: 20px; }

.voice-main {
  background: #fff;
  border-radius: 20px;
  padding: 48px 32px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  text-align: center;
}

.mic-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.mic-button {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: #f5f5f9;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  user-select: none;
}

.mic-button:hover {
  background: #e6f4ff;
  transform: scale(1.05);
}

.mic-button.listening {
  background: #ff4d4f;
}

.mic-button.processing {
  background: #faad14;
}

.mic-icon {
  font-size: 40px;
  position: relative;
  z-index: 1;
}

.mic-pulse {
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: rgba(255,77,79,0.3);
  animation: pulse 1.5s ease-out infinite;
}

@keyframes pulse {
  0% { transform: scale(1); opacity: 1; }
  100% { transform: scale(1.8); opacity: 0; }
}

.status-text {
  font-size: 15px;
  font-weight: 500;
}

.status-text.idle { color: #888; }
.status-text.listening { color: #ff4d4f; }
.status-text.processing { color: #faad14; }

.transcript-box {
  background: #fafbfe;
  border-radius: 12px;
  padding: 12px 20px;
  max-width: 500px;
}

.transcript-label { font-size: 12px; color: #888; }
.transcript-text { font-size: 15px; color: #333; font-weight: 500; }

.text-input-row {
  width: 100%;
  max-width: 500px;
  margin-top: 8px;
}

.conversation-section {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}

.conversation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f5f5f9;
}

.conversation-header h3 { font-size: 15px; color: #1a1a2e; margin: 0; font-weight: 600; }

.conversation-item {
  padding: 12px 0;
  border-bottom: 1px solid #f5f5f9;
  display: flex;
  gap: 10px;
}

.conversation-item:last-child { border-bottom: none; }

.conv-role { font-size: 13px; font-weight: 500; color: #555; flex-shrink: 0; }
.conv-content { font-size: 13px; color: #666; line-height: 1.6; }

.tips-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.tip-card {
  background: #fff;
  border-radius: 14px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  cursor: pointer;
  transition: all 0.25s ease;
}

.tip-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0,0,0,0.06);
}

.tip-icon { font-size: 24px; flex-shrink: 0; }
.tip-text { font-size: 13px; color: #666; line-height: 1.5; }

@media (max-width: 1024px) {
  .tips-row { grid-template-columns: 1fr; }
}
</style>
