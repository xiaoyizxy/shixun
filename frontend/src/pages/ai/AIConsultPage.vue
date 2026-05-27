<script>
import axios from 'axios'

export default {
  name: "AIConsultPage",
  data() {
    return {
      messages: [
        { role: 'assistant', content: '您好！我是AI护理助手，请问有什么可以帮您的？\n\n我可以帮您：\n• 解答护理相关问题\n• 提供健康管理建议\n• 查询护理服务信息\n• 用药提醒与指导', time: '' }
      ],
      inputText: '',
      loading: false,
      autoScroll: true
    }
  },
  mounted() {
    this.scrollToBottom()
  },
  updated() {
    if (this.autoScroll) {
      this.scrollToBottom()
    }
  },
  methods: {
    async sendMessage() {
      const text = this.inputText.trim()
      if (!text || this.loading) return

      this.messages.push({ role: 'user', content: text, time: this.getTime() })
      this.inputText = ''
      this.loading = true

      try {
        const res = await axios.get('/ai/customer/chat', {
          params: { userId: 'default_user', message: text }
        })
        // 后端返回 {code, msg, data}，AI回复在 data 字段
        let reply = res.data?.data || '抱歉，我暂时无法回答这个问题。'
        this.messages.push({ role: 'assistant', content: reply, time: this.getTime() })
      } catch (e) {
        this.messages.push({ role: 'assistant', content: '网络异常，请稍后重试。', time: this.getTime() })
      } finally {
        this.loading = false
      }
    },
    getLocalReply(text) {
      const responses = {
        '护理': '上门护理服务包括压疮护理、静脉输液、肌肉注射、PICC维护等多种项目。您可以通过"上门护理"页面查看详细服务列表并预约。',
        '预约': '您可以通过"在线预约"页面为老人预约护理服务。需要填写老人信息、选择服务类型和时间段。',
        '老人': '我们为老人提供全方位的护理服务，包括健康监测、生活照料、康复训练、医疗护理等。',
        '血压': '建议老年人保持血压在140/90mmHg以下。定期监测血压，遵医嘱服药，保持低盐饮食和适量运动。',
        '吃药': '请遵医嘱按时服药，不要随意停药或增减剂量。建议使用药盒分装，设置服药提醒。'
      }
      for (const [key, val] of Object.entries(responses)) {
        if (text.includes(key)) return val
      }
      return '感谢您的咨询！我是AI护理助手，正在学习中。建议您联系我们的专业护理人员获取更详细的帮助。您也可以拨打客服电话获取即时支持。'
    },
    getTime() {
      return new Date().toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const el = this.$el.querySelector('.messages-container')
        if (el) el.scrollTop = el.scrollHeight
      })
    },
    handleKeydown(e) {
      if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault()
        this.sendMessage()
      }
    }
  }
}
</script>

<template>
  <div class="ai-consult-page">
    <div class="page-header">
      <h2>AI 智能咨询</h2>
      <p>智能护理助手，为您解答护理和健康管理问题</p>
    </div>

    <div class="chat-card">
      <div class="messages-container" ref="chatContainer">
        <div
          v-for="(msg, idx) in messages"
          :key="idx"
          class="message"
          :class="msg.role"
        >
          <div class="message-avatar">
            <span v-if="msg.role === 'assistant'" class="avatar-ai">🤖</span>
            <span v-else class="avatar-user">👤</span>
          </div>
          <div class="message-body">
            <div class="message-bubble">
              <div class="message-text" v-html="msg.content.replace(/\n/g, '<br>')"></div>
            </div>
            <div class="message-time">{{ msg.time }}</div>
          </div>
        </div>

        <div v-if="loading" class="message assistant">
          <div class="message-avatar"><span class="avatar-ai">🤖</span></div>
          <div class="message-body">
            <div class="message-bubble typing">
              <span class="dot"></span><span class="dot"></span><span class="dot"></span>
            </div>
          </div>
        </div>
      </div>

      <div class="input-area">
        <div class="input-wrapper">
          <textarea
            v-model="inputText"
            placeholder="请输入您的问题..."
            @keydown="handleKeydown"
            :disabled="loading"
            rows="2"
          ></textarea>
          <el-button
            type="primary"
            :disabled="!inputText.trim() || loading"
            @click="sendMessage"
            round
            class="send-btn"
          >
            发送
          </el-button>
        </div>
        <div class="input-hint">按 Enter 发送，Shift + Enter 换行</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.ai-consult-page {
  padding: 0;
  height: calc(100vh - 140px);
  display: flex;
  flex-direction: column;
}

.page-header {
  margin-bottom: 20px;
  flex-shrink: 0;
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

.chat-card {
  flex: 1;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message {
  display: flex;
  gap: 12px;
  max-width: 85%;
}

.message.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message.assistant {
  align-self: flex-start;
}

.message-avatar {
  flex-shrink: 0;
  width: 36px;
  height: 36px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  background: #f5f5f9;
}

.message.user .message-avatar {
  background: #e6f4ff;
}

.message-body {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message.user .message-body {
  align-items: flex-end;
}

.message-bubble {
  padding: 12px 16px;
  border-radius: 14px;
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  background: #f5f5f9;
}

.message.user .message-bubble {
  background: #1890ff;
  color: #fff;
}

.message-time {
  font-size: 11px;
  color: #bbb;
  padding: 0 4px;
}

.typing {
  display: flex;
  gap: 5px;
  padding: 14px 20px;
}

.typing .dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #bbb;
  animation: blink 1.4s infinite ease-in-out both;
}

.typing .dot:nth-child(1) { animation-delay: 0s; }
.typing .dot:nth-child(2) { animation-delay: 0.2s; }
.typing .dot:nth-child(3) { animation-delay: 0.4s; }

@keyframes blink {
  0%, 80%, 100% { opacity: 0.3; transform: scale(0.8); }
  40% { opacity: 1; transform: scale(1); }
}

.input-area {
  padding: 16px 24px 20px;
  border-top: 1px solid #f0f0f5;
  flex-shrink: 0;
}

.input-wrapper {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.input-wrapper textarea {
  flex: 1;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 12px 16px;
  font-size: 14px;
  font-family: inherit;
  resize: none;
  outline: none;
  transition: border-color 0.25s;
  line-height: 1.5;
}

.input-wrapper textarea:focus {
  border-color: #1890ff;
  box-shadow: 0 0 0 3px rgba(24,144,255,0.08);
}

.send-btn {
  height: 44px;
  padding: 0 24px;
}

.input-hint {
  font-size: 11px;
  color: #ccc;
  margin-top: 6px;
  padding-left: 4px;
}
</style>
