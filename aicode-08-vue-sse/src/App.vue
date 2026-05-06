<script setup lang="ts">
import { ref, onUnmounted } from 'vue'

// 响应式数据
const question = ref('')
const answer = ref('')
const isLoading = ref(false)
const eventSource = ref<EventSource | null>(null)
const hasReceivedData = ref(false) // 标记是否已接收到数据

// 发送SSE请求
const sendQuestion = () => {
  if (!question.value.trim()) {
    alert('请输入问题')
    return
  }

  // 重置状态
  answer.value = ''
  isLoading.value = true
  hasReceivedData.value = false

  // 关闭之前的连接
  if (eventSource.value) {
    eventSource.value.close()
  }

  // 构造请求 URL
  const baseUrl = 'http://localhost:8080' // 后端服务地址
  const url = `${baseUrl}/ai/dashChat?question=${encodeURIComponent(question.value)}`

  // 创建 EventSource 连接
  eventSource.value = new EventSource(url)

  // 监听消息事件
  eventSource.value.onmessage = (event) => {
    // 接收流式数据并累加显示
    answer.value += event.data
    hasReceivedData.value = true
  }

  // 监听连接打开事件
  eventSource.value.onopen = () => {
    console.log('SSE 连接已建立')
  }

  // 监听错误事件
  eventSource.value.onerror = (error) => {
    const es = eventSource.value
    // readyState: 0=CONNECTING, 1=OPEN, 2=CLOSED
    const readyState = es?.readyState ?? -1

    // 如果已经接收到数据，说明是正常结束，不报错
    // 如果 readyState 为 2（CLOSED），也是正常结束
    if (hasReceivedData.value || readyState === 2) {
      // 正常结束，静默处理
    } else {
      // 连接阶段或打开阶段出错，且没有接收到数据，才是真正错误
      console.error('SSE 连接错误:', error, 'readyState:', readyState, 'hasReceivedData:', hasReceivedData.value)
    }

    isLoading.value = false

    if (es) {
      es.close()
    }
  }
}

// 停止请求
const stopRequest = () => {
  if (eventSource.value) {
    eventSource.value.close()
    isLoading.value = false
    console.log('SSE连接已关闭')
  }
}

// 组件卸载时清理连接
onUnmounted(() => {
  if (eventSource.value) {
    eventSource.value.close()
  }
})
</script>

<template>
  <div class="chat-container">
    <div class="header">
      <h1>SSE 流式对话 Demo</h1>
    </div>

    <div class="chat-box">\      <!-- 输入区域 -->
      <div class="input-area">
        <textarea
            v-model="question"
            placeholder="请输入您的问题..."
            :disabled="isLoading"
            rows="3"
        ></textarea>
        <div class="button-group">
          <button
              @click="sendQuestion"
              :disabled="isLoading || !question.trim()"
              class="send-btn"
          >
            {{ isLoading ? '发送中...' : '发送' }}
          </button>
          <button
              @click="stopRequest"
              :disabled="!isLoading"
              class="stop-btn"
          >
            停止
          </button>
        </div>
      </div>

      <!-- 回答区域 -->
      <div class="answer-area">
        <div class="answer-header">
          <h3>AI 回答:</h3>
          <span v-if="isLoading" class="loading-indicator">正在接收...</span>
        </div>
        <div class="answer-content">
          <pre v-if="answer">{{ answer }}</pre>
          <div v-else class="placeholder">
            {{ isLoading ? '等待回复...' : '暂无回复内容' }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.chat-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.header h1 {
  color: #333;
  margin: 0;
}

.chat-box {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  overflow: hidden;
}

.input-area {
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.input-area textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  resize: vertical;
  font-size: 14px;
  margin-bottom: 15px;
}

.input-area textarea:focus {
  outline: none;
  border-color: #409eff;
}

.button-group {
  display: flex;
  gap: 10px;
}

.send-btn, .stop-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.send-btn {
  background-color: #409eff;
  color: white;
}

.send-btn:hover:not(:disabled) {
  background-color: #66b1ff;
}

.send-btn:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

.stop-btn {
  background-color: #f56c6c;
  color: white;
}

.stop-btn:hover:not(:disabled) {
  background-color: #f78989;
}

.stop-btn:disabled {
  background-color: #fab6b6;
  cursor: not-allowed;
}

.answer-area {
  padding: 20px;
}

.answer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.answer-header h3 {
  margin: 0;
  color: #333;
}

.loading-indicator {
  color: #409eff;
  font-size: 14px;
}

.answer-content {
  min-height: 100px;
  background-color: #f5f5f5;
  border-radius: 5px;
  padding: 15px;
}

.answer-content pre {
  margin: 0;
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: inherit;
  line-height: 1.6;
}

.placeholder {
  color: #999;
  font-style: italic;
  text-align: center;
  padding: 30px 0;
}

/* 响应式设计 */
@media (max-width: 600px) {
  .chat-container {
    padding: 10px;
  }

  .button-group {
    flex-direction: column;
  }

  .answer-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}
</style>