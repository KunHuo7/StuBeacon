<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
const goLogin = () => {
  router.push('/auth')
}
</script>

<template>
  <div class="error-container">
    <div class="error-content">
      <h1>401</h1>
      <p>抱歉，您没有权限访问此页面</p>
      <el-button type="primary" @click="goLogin">去登录</el-button>
    </div>
  </div>
</template>

<style scoped>
.error-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.error-content {
  text-align: center;
}

h1 {
  font-size: 72px;
  color: #E6A23C;
  margin-bottom: 20px;
}

p {
  font-size: 18px;
  color: #606266;
  margin-bottom: 30px;
}
</style>