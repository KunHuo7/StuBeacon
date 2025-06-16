<script setup>
import { useRouter } from 'vue-router'
import { ref, onMounted } from 'vue'

const router = useRouter()
const goHome = () => {
  router.push('/')
}

const isVisible = ref(false)
onMounted(() => {
  setTimeout(() => {
    isVisible.value = true
  }, 100)
})
</script>

<template>
  <div class="error-container">
    <div class="error-content" :class="{ 'visible': isVisible }">
      <div class="error-number">
        <span class="number">4</span>
        <div class="planet">
          <div class="ring"></div>
        </div>
        <span class="number">4</span>
      </div>
      <p class="error-text">哎呀！页面好像迷路了</p>
      <p class="error-subtext">它可能去太空旅行了...(请检查链接之后再输入)</p>
      <el-button type="primary" class="home-button" @click="goHome">
        <i class="el-icon-house"></i> 带我回家
      </el-button>
    </div>
  </div>
</template>

<style scoped>
.error-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  //background: linear-gradient(135deg, #1a2a6c, #b21f1f, #fdbb2d);
  background-size: 400% 400%;
  animation: gradientBG 15s ease infinite;
}

.error-content {
  text-align: center;
  opacity: 0;
  transform: translateY(20px);
  transition: all 0.8s ease;
}

.error-content.visible {
  opacity: 1;
  transform: translateY(0);
}

.error-number {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
}

.number {
  font-size: 120px;
  font-weight: bold;
  color: #3c7eca;
  text-shadow: 0 0 10px rgba(145, 135, 135, 0.5);
  //animation: float 3s ease-in-out infinite;
}

.planet {
  width: 60px;
  height: 60px;
  background: #ffd700;
  border-radius: 50%;
  margin: 0 20px;
  position: relative;
  animation: rotate 10s linear infinite;
}

.ring {
  position: absolute;
  width: 120px;
  height: 20px;
  background: rgba(255,255,255,0.2);
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%) rotate(45deg);
}

.error-text {
  font-size: 24px;
  color: #040404;
  margin-bottom: 10px;
  animation: fadeIn 1s ease-in-out;
}

.error-subtext {
  font-size: 16px;
  color: rgba(18, 10, 10, 0.8);
  margin-bottom: 30px;
  animation: fadeIn 1s ease-in-out 0.5s both;
}

.home-button {
  padding: 12px 30px;
  font-size: 16px;
  border-radius: 25px;
  background: rgba(255,255,255,0.2);
  border: 2px solid #81b8b7;
  color: #0c0c0c;
  transition: all 0.3s ease;
}

.home-button:hover {
  background: rgba(255,255,255,0.3);
  transform: scale(1.05);
}

@keyframes gradientBG {
  0% { background-position: 0% 50% }
  50% { background-position: 100% 50% }
  100% { background-position: 0% 50% }
}

@keyframes float {
  0%, 100% { transform: translateY(0) }
  50% { transform: translateY(-20px) }
}

@keyframes rotate {
  from { transform: rotate(0deg) }
  to { transform: rotate(360deg) }
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px) }
  to { opacity: 1; transform: translateY(0) }
}
</style>