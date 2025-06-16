<template>
  <div
      ref="chartContainer"
      :style="{ width: width, height: height }"
      class="skill-radar-chart rounded-lg bg-white dark:bg-gray-800 shadow-lg p-4 transition-colors duration-200"
  ></div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts'

// 定义组件的props
const props = defineProps({
  // 图表宽度
  width: {
    type: String,
    default: '100%'
  },
  // 图表高度
  height: {
    type: String,
    default: '100%'
  },
  // 自定义数据
  data: {
    type: Array,
    default: () => [85, 75, 90, 80, 70, 95]
  },
  // 自定义技能名称
  skills: {
    type: Array,
    default: () => ['专业知识', '沟通能力', '团队协作', '问题解决', '创新思维', '学习能力']
  },
  // 最大值
  maxValue: {
    type: Number,
    default: 100
  },
  // 主题色
  themeColor: {
    type: String,
    default: '#3B82F6' // 默认主题色
  }
})

// 图表容器引用
const chartContainer = ref(null)
// 图表实例
let chartInstance = null

// 初始化图表
const initChart = () => {
  if (!chartContainer.value) return

  // 创建图表实例，并根据暗色模式设置主题
  const isDark = document.documentElement.classList.contains('dark')
  chartInstance = echarts.init(chartContainer.value, isDark ? 'dark' : null)

  updateChartOption()
  window.addEventListener('resize', handleResize)

  // 监听暗色模式变化
  const observer = new MutationObserver(() => {
    const isDark = document.documentElement.classList.contains('dark')
    chartInstance.dispose()
    chartInstance = echarts.init(chartContainer.value, isDark ? 'dark' : null)
    updateChartOption()
  })

  observer.observe(document.documentElement, {
    attributes: true,
    attributeFilter: ['class']
  })
}

// 更新图表配置
const updateChartOption = () => {
  if (!chartInstance) return

  const isDark = document.documentElement.classList.contains('dark')
  const textColor = isDark ? '#e5e7eb' : '#374151'
  const axisLineColor = isDark ? 'rgba(255, 255, 255, 0.2)' : 'rgba(0, 0, 0, 0.1)'

  const option = {
    tooltip: {
      trigger: 'item',
      backgroundColor: isDark ? '#374151' : '#ffffff',
      borderColor: isDark ? '#4b5563' : '#e5e7eb',
      textStyle: {
        color: textColor
      }
    },
    radar: {
      shape: 'polygon',
      indicator: props.skills.map(skill => ({
        name: skill,
        max: props.maxValue
      })),
      splitNumber: 4,
      axisName: {
        color: textColor,
        fontSize: 12,
        padding: [3, 5]
      },
      splitArea: {
        areaStyle: {
          color: isDark 
            ? ['rgba(255, 255, 255, 0.02)', 'rgba(255, 255, 255, 0.05)']
            : ['rgba(255, 255, 255, 0.9)', 'rgba(255, 255, 255, 0.95)'],
          shadowColor: isDark ? 'rgba(0, 0, 0, 0.2)' : 'rgba(0, 0, 0, 0.05)',
          shadowBlur: 10
        }
      },
      axisLine: {
        lineStyle: {
          color: axisLineColor
        }
      },
      splitLine: {
        lineStyle: {
          color: axisLineColor
        }
      }
    },
    series: [
      {
        name: '职业技能评估',
        type: 'radar',
        data: [
          {
            value: props.data,
            name: '技能水平',
            symbol: 'circle',
            symbolSize: 6,
            lineStyle: {
              width: 2,
              color: props.themeColor
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: `${props.themeColor}80`
                  },
                  {
                    offset: 1,
                    color: `${props.themeColor}20`
                  }
                ]
              }
            },
            itemStyle: {
              color: props.themeColor
            }
          }
        ]
      }
    ]
  }

  chartInstance.setOption(option)
}

// 处理窗口大小变化
const handleResize = () => {
  chartInstance && chartInstance.resize()
}

// 监听props变化，更新图表
watch(
    () => [props.data, props.skills, props.maxValue, props.themeColor],
    () => {
      updateChartOption()
    },
    { deep: true }
)

// 组件挂载时初始化图表
onMounted(() => {
  initChart()
})

// 组件卸载前清理资源
onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.skill-radar-chart {
  @apply min-h-[300px] w-full transition-all duration-300 ease-in-out;
}
</style>