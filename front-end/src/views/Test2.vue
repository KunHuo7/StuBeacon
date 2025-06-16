<template>
  <div class="chat-container">
    <h1>LLM 聊天客户端</h1>

    <!-- 消息输入 -->
    <div class="input-section">
      <textarea v-model="message" placeholder="输入消息..."></textarea>
      <input v-model="imageUrl" type="text" placeholder="图片URL (仅多模态需要)" class="image-input">
    </div>

    <!-- 模式选择 -->
    <div class="mode-select">
      <button @click="mode = 'text'">普通文本</button>
      <button @click="mode = 'json'">JSON响应</button>
      <button @click="mode = 'stream'">流式响应</button>
      <button @click="mode = 'multi'">多模态</button>
    </div>

    <!-- 发送按钮 -->
    <button @click="sendMessage" class="send-btn">发送</button>

    <!-- 响应展示 -->
    <div class="response-area">
      <h3>响应内容：</h3>
      <div v-if="isStreaming" class="streaming-response">
        <p v-for="(chunk, index) in streamChunks" :key="index">{{ chunk }}</p>
      </div>
      <pre v-else-if="response">{{ response }}</pre>
      <p v-else class="placeholder">等待响应...</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

// 响应数据
const message = ref('')
const imageUrl = ref('')
const response = ref(null)
const streamChunks = ref([])
const isStreaming = ref(false)
const mode = ref('text') // text/json/stream/multi

const API_BASE = 'http://localhost:8080' // 根据后端地址修改

const sendMessage = async () => {
  try {
    response.value = null
    streamChunks.value = []

    let url = ''
    const params = new URLSearchParams()

    // 根据模式构建请求
    switch (mode.value) {
      case 'text':
        url = `${API_BASE}/llm/chat`
        params.append('message', message.value)
        break
      case 'json':
        url = `${API_BASE}/llm/chat2`
        params.append('message', message.value)
        break
      case 'stream':
        url = `${API_BASE}/llm/chat3`
        params.append('message', message.value)
        handleStreamResponse(url, params)
        return
      case 'multi':
        url = `${API_BASE}/llm/multi`
        params.append('message', message.value)
        params.append('imageUrl', imageUrl.value)
        break
    }

    const { data } = await axios.get(url, { params })
    response.value = data
  } catch (err) {
    console.error('请求失败:', err)
    response.value = `错误: ${err.response?.data || err.message}`
  }
}

// 处理流式响应
const handleStreamResponse = async (url, params) => {
  isStreaming.value = true
  try {
    const eventSource = new EventSource(`${url}?${params.toString()}`)

    eventSource.onmessage = (event) => {
      streamChunks.value.push(event.data)
    }

    eventSource.onerror = () => {
      eventSource.close()
      isStreaming.value = false
    }
  } catch (err) {
    isStreaming.value = false
    console.error('流式请求失败:', err)
  }
}
</script>

<style scoped>
.chat-container {
  max-width: 800px;
  margin: 2rem auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.input-section {
  margin-bottom: 1rem;
}

textarea {
  width: 100%;
  height: 100px;
  padding: 10px;
  margin-bottom: 10px;
}

.image-input {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
}

.mode-select {
  display: flex;
  gap: 10px;
  margin-bottom: 1rem;
}

button {
  padding: 8px 16px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background: #0056b3;
}

.response-area {
  margin-top: 20px;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  min-height: 200px;
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
}

.streaming-response p {
  margin: 4px 0;
  color: #666;
}

.placeholder {
  color: #999;
}
</style>