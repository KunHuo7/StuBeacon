<script setup>
const quickLinks = [
  {
    icon: '📝',
    title: '新生入学安排',
    description: '了解日常任务',
    link: '/about/admission-guide',
    gradient: 'gradient-1'
  },
  {
    icon: '🏠',
    title: '学校一览',
    description: '查看教学楼位置信息',
    link: '/map',
    gradient: 'gradient-2'
  },
  {
    icon: '📚',
    title: '课程规划',
    description: '了解课程',
    link: '/courses',
    gradient: 'gradient-3'
  },
  {
    icon: '🎓',
    title: '个人信息',
    description: '查看个人信息',
    link: '/personal',
    gradient: 'gradient-4'
  }
]
</script>

<template>
  <div class="p-1">
    <div class="cards-grid ">
      <div v-for="link in quickLinks" :key="link.title" class="container gap-y-4 ">
        <div class="card">
          <!-- Front Side -->
          <div class="front" :class="link.gradient">
            <div class="icon-container">
              <span class="icon">{{ link.icon }}</span>
            </div>
            <p class="front-heading">{{ link.title }}</p>
            <p class="front-content">{{ link.description }}</p>
          </div>

          <!-- Back Side -->
          <div class="back items-center gap-x-2" :class="link.gradient" @click="$router.push(link.link)">
            <div class="back-content">点击查看</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

.cards-grid {
  max-width: 1400px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  justify-items: center;
}

.container {
  width: 300px;
  height: 280px;
  perspective: 900px;
}

.card {
  height: 70%;
  width: 100%;
  position: relative;
  transition: transform 1500ms;
  transform-style: preserve-3d;
  border-radius: 15rem;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.3);
}

.container:hover > .card {
  cursor: pointer;
  transform: rotateY(180deg) rotateZ(180deg);
}

.front,
.back {
  height: 100%;
  width: 100%;
  border-radius: 2rem;
  position: absolute;
  backface-visibility: hidden;
  color: #f0f1ff;
  display: flex;
  justify-content: center;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.back {
  transform: rotateY(180deg) rotateZ(180deg);
}

.gradient-1 {
  background: linear-gradient(-45deg, #f8255d 0%, #0ffff7 100%);
}

.gradient-2 {
  background: linear-gradient(-45deg, #FF6B6B 0%, #4ECDC4 100%);
}

.gradient-3 {
  background: linear-gradient(-45deg, #FFB347 0%, #ffcc33 100%);
}

.gradient-4 {
  background: linear-gradient(-45deg, #45B7D1 0%, #D53F8C 100%);
}

.icon-container {
  font-size: 40px;
}

.front-heading {
  font-size: 28px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  font-weight: bold;
  z-index: 999;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.front-content {
  font-size: 20px;
  z-index: 999;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3);
}

.back-content {
  padding: 20px;
  font-size: 22px;
  text-align: center;
  line-height: 1.6;
  text-decoration: none;
  color: #f0f1ff;
}

.img {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  border-radius: 2rem;
  object-fit: cover;
}

@keyframes gradientAnimation {
  0% {
    background-position: 0 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}

.front,
.back {
  background-size: 200% 200%;
  animation: gradientAnimation 5s ease infinite;
}


@media (max-width: 1200px) {
  .cards-grid {
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  }
}

@media (max-width: 768px) {
  .card {
    border-radius: 14px;
  }

  .container {
    width: 200px;
    height: 140px;
  }

  .front-heading {
    font-size: 24px;
  }

  .front-content {
    font-size: 18px;
  }

  .back-content {
    font-size: 18px;
    padding: 15px;
  }
}

@media (max-width: 480px) {
  .card {
    border-radius: 13px;
  }

  .page-container {
    padding: 1rem;
  }

  .container {
    width: 260px;
    height: 182px;
  }
}
</style>
