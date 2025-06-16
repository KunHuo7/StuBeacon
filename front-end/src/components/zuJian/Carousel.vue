<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import xx from '@/assets/images/学校.png'
import library from '@/assets/images/图书馆.png'
import Building from '@/assets/images/教学楼.png'
import PE from '@/assets/images/体育馆.png'
import activity from '@/assets/images/活动中心.png'

const images = ref([
  {
    url: xx,
    title: '校园风光',
    description: '美丽的校园环境',
    stats: {
      '37万㎡': '建筑面积',
      '40%': '绿化率',
      '200+': '设施数量',
      'A 级': '环境评级'
    }
  },
  {
    url: library,
    title: '图书馆',
    description: '知识的殿堂',
    stats: {
      '156万册': '馆藏图书',
      '2000个': '阅览座位',
      '6层': '楼层数',
      '8:00-22:00': '开放时间'
    }
  },
  {
    url: Building,
    title: '实验楼',
    description: '创新的摇篮',
    stats: {
      '89个': '实验室数',
      '3000台': '设备数量',
      '150+': '在研项目',
      '省级': '科研水平'
    }
  },
  {
    url: PE,
    title: '体育馆',
    description: '运动的天地',
    stats: {
      '12000㎡': '场馆面积',
      '8个': '运动场地',
      '1000+': '器材配置',
      '15个': '功能区域'
    }
  },
  {
    url: activity,
    title: '大学生活动中心',
    description: '文化的殿堂',
    stats: {
      '30个': '活动室数',
      '1000人': '容纳人数',
      '50+': '社团数量',
      '20+': '活动类型'
    }
  }
])

const currentSlide = ref(0)
const isHovering = ref(false)

const nextSlide = () => {
  currentSlide.value = (currentSlide.value + 1) % images.value.length
}

let autoSlideInterval

const startAutoSlide = () => {
  if (!isHovering.value) {
    autoSlideInterval = setInterval(nextSlide, 5000)
  }
}

const stopAutoSlide = () => {
  clearInterval(autoSlideInterval)
}

onMounted(() => {
  startAutoSlide()
})

onUnmounted(() => {
  stopAutoSlide()
})
</script>

<template>
  <div class="container mx-auto px-4 py-8">
    <div class="relative w-[80%] mx-auto h-[600px] overflow-hidden rounded-2xl shadow-2xl"
         @mouseenter="isHovering = true; stopAutoSlide()"
         @mouseleave="isHovering = false; startAutoSlide()">
      <transition-group name="carousel">
        <div v-for="(image, index) in images"
             :key="index"
             v-show="currentSlide === index"
             class="absolute inset-0">
          <img :src="image.url"
               :alt="image.title"
               class="w-full h-full object-cover brightness-90"
          />
          <div class="absolute inset-0 bg-gradient-to-t from-gray-900/60 via-transparent to-transparent"></div>
          <div class="absolute bottom-0 left-0 right-0 text-center pb-16">
            <h2 class="text-4xl font-bold text-white mb-4 animate-fade-in-up">
              {{ image.title }}
            </h2>
            <p class="text-xl text-white/90 mb-6">{{ image.description }}</p>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-8 max-w-4xl mx-auto">
              <div v-for="(value, key) in image.stats"
                   :key="key"
                   class="bg-white/10 backdrop-blur-sm p-4 rounded-xl border border-white/20">
                <div class="text-2xl font-bold text-blue-300">{{ value }}</div>
                <div class="text-sm text-gray-200 mt-1 capitalize">{{ key }}</div>
              </div>
            </div>
          </div>
        </div>
      </transition-group>

      <!-- Navigation arrows -->
      <button @click="currentSlide = (currentSlide - 1 + images.length) % images.length"
              class="absolute left-4 top-1/2 transform -translate-y-1/2 bg-black/30 hover:bg-black/50 text-white p-2 rounded-full transition-colors duration-300">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
        </svg>
      </button>
      <button @click="nextSlide"
              class="absolute right-4 top-1/2 transform -translate-y-1/2 bg-black/30 hover:bg-black/50 text-white p-2 rounded-full transition-colors duration-300">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
        </svg>
      </button>
    </div>
  </div>
</template>

<style scoped>

.animate-fade-in-up {
  animation: fadeInUp 0.8s ease-out;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}


</style>