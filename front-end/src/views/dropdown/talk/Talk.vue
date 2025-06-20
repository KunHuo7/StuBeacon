<script setup>
import { ref, onMounted, reactive, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useThemeStore } from '@/stores/theme.js';
import { message } from 'ant-design-vue';
import { get, post } from '@/net/index.js';
import { formatDate } from "@/util/DateFormattor.js";
import Talk_card from "@/components/zuJian/card/talk_card.vue";

const router = useRouter();
const themeStore = useThemeStore();
const [messageApi, contextHolder] = message.useMessage();

// 话题数据
const topics = reactive({
  list: []
});

// 悬停的话题ID
const hoveredTopicId = ref(null);

// 本地搜索与过滤（前端过滤）
const filteredTopics = computed(() => {
  if (!isFiltering.value || !topics.list.length) return topics.list;
  
  return topics.list.filter(topic => {
    if (filterType.value === 'keyword') {
      const keyword = searchKeyword.value.toLowerCase().trim();
      return (
        topic.talk.title.toLowerCase().includes(keyword) || 
        topic.talk.content.toLowerCase().includes(keyword) ||
        (topic.talk.tags && topic.talk.tags.some(tag => tag.toLowerCase().includes(keyword))) ||
        (topic.username && topic.username.toLowerCase().includes(keyword))
      );
    } else if (filterType.value === 'tag') {
      // 从URL参数获取标签或使用存储的标签
      const currentTag = filterTag.value;
      return topic.talk.tags && topic.talk.tags.includes(currentTag);
    } else if (filterType.value === 'author') {
      // 按作者名搜索
      const authorName = filterAuthor.value.toLowerCase().trim();
      return topic.username && topic.username.toLowerCase().includes(authorName);
    }
    return true;
  });
});

// 将话题列表分组
const groupedTopics = computed(() => {
  // 使用过滤后的话题列表
  const topicsToUse = filteredTopics.value;
  
  // 计算话题权重并排序
  const sortedTopics = [...topicsToUse].sort((a, b) => {
    // 计算权重
    const getWeight = (topic) => {
      const now = new Date();
      const createTime = new Date(topic.talk.createTime);
      const hoursSinceCreation = (now - createTime) / (1000 * 60 * 60);
      
      // 基础互动值：浏览量 * 0.3 + 点赞数 * 0.3 + 评论数 * 0.2
      const interactionScore = topic.talk.views * 0.3 + topic.talk.likes * 0.3 + topic.talk.comments * 0.2;
      
      // 最小互动阈值，确保有一定互动才能获得时间加成
      const minInteractionThreshold = 2;
      
      // 时间因子（24小时内递减）
      const timeFactor = hoursSinceCreation < 24 ? (24 - hoursSinceCreation) / 24 : 0;
      
      // 综合权重计算：
      // 1. 基础互动分数
      // 2. 如果互动超过阈值，才能获得时间加成（且加成与互动成正比）
      // 3. 确保新内容至少有基本互动才能上榜
      let weight = interactionScore;
      
      if (interactionScore >= minInteractionThreshold) {
        // 时间加成，最多提升30%的权重，且只对有互动的内容有效
        const timeBonus = interactionScore * 0.3 * timeFactor;
        weight += timeBonus;
      }
      
      return weight;
    };
    
    return getWeight(b) - getWeight(a);
  });
  
  // 获取前三名话题
  const topThreeTopics = sortedTopics.slice(0, 3);
  const remainingTopics = sortedTopics.slice(3);
  
  const pattern = [3, 2]; // 每行显示的卡片数量模式（除第一行外）
  const result = [];
  
  // 第一行放置权重第一的话题
  if (topThreeTopics.length > 0) {
    result.push([topThreeTopics[0]]);
  }
  
  // 第二行放置权重第二和第三的话题
  if (topThreeTopics.length > 1) {
    result.push(topThreeTopics.slice(1, 3));
  }
  
  // 处理剩余话题
  let currentIndex = 0;
  let patternIndex = 0;
  
  while (currentIndex < remainingTopics.length) {
    const rowCount = pattern[patternIndex % pattern.length];
    const row = remainingTopics.slice(currentIndex, currentIndex + rowCount);
    
    if (row.length > 0) {  // 只添加非空行
      result.push(row);
    }
    
    currentIndex += rowCount;
    patternIndex++;
  }
  
  return result;
});

// 加载状态
const loading = ref(false);

// 当前是否在搜索/筛选模式
const isFiltering = ref(false);
const filterType = ref(''); // 'keyword' 或 'tag'
const filterTag = ref(''); // 当前筛选的标签
const filterAuthor = ref(''); // 按作者名搜索

// 获取话题列表
const fetchTopics = (params = {}) => {
  loading.value = true;
  
  // 检查是否在筛选模式
  if (params.keyword || params.tag || params.author) {
    isFiltering.value = true;
    filterType.value = params.keyword ? 'keyword' : params.tag ? 'tag' : 'author';
    
    // 如果是标签筛选，存储当前标签
    if (params.tag) {
      filterTag.value = params.tag;
    }
    if (params.author) {
      filterAuthor.value = params.author;
    }
  } else {
    isFiltering.value = false;
    filterType.value = '';
    filterTag.value = '';
    filterAuthor.value = '';
  }
  
  get('/api/talk/list', params, (message, data) => {
    topics.list = data || [];
    // console.log(`获取到话题列表: ${topics.list.length}条, 搜索参数:`, params);
    loading.value = false;
  }, (error) => {
    console.error('获取话题列表失败:', error);
    loading.value = false;
    messageApi.error('获取话题列表失败，请稍后重试');
  });
};

// 返回全部话题
const resetFilter = () => {
  searchKeyword.value = '';
  isFiltering.value = false;
  filterType.value = '';
  filterTag.value = '';
  filterAuthor.value = '';
  
  // 清除URL中的tag参数
  const url = new URL(window.location.href);
  url.searchParams.delete('tag');
  window.history.pushState({}, '', url);
  
  fetchTopics();
  messageApi.success('已显示全部话题');
};

// 处理话题点击
const handleTopicClick = (topicId) => {
  router.push(`/talk/detail/${topicId}`);
};

// 创建新话题
const createNewTopic = () => {
  router.push('/talk/create');
};

// 搜索关键词
const searchKeyword = ref('');

// 处理搜索
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    // 判断是否以"@"开头，如果是则视为作者搜索
    if (searchKeyword.value.trim().startsWith('@')) {
      const authorName = searchKeyword.value.trim().substring(1); // 去掉@符号
      if (authorName) {
        fetchTopics({ author: authorName });
        // messageApi.info(`搜索作者: ${authorName}`);
      } else {
        fetchTopics();
      }
    } else {
      // 普通关键词搜索
      fetchTopics({ keyword: searchKeyword.value.trim() });
      // messageApi.info(`搜索: ${searchKeyword.value.trim()}`);
    }
  } else {
    fetchTopics();
  }
};

// 处理标签点击
const handleTagClick = (tag) => {
  // 将标签添加到URL参数中，供本地过滤使用
  const url = new URL(window.location.href);
  url.searchParams.set('tag', tag);
  window.history.pushState({}, '', url);
  
  fetchTopics({ tag: tag });
  // messageApi.info(`按标签筛选: ${tag}`);
};

// 截断文本，限制字数
const truncateText = (text, limit = 30) => {
  if (!text) return '';
  return text.length > limit ? text.substring(0, limit) + '...' : text;
};

// 处理鼠标进入
const handleMouseEnter = (topicId) => {
  hoveredTopicId.value = topicId;
};

// 处理鼠标离开
const handleMouseLeave = () => {
  hoveredTopicId.value = null;
};

// 处理作者点击
const handleAuthorClick = (username) => {
  if (!username) return;
  searchKeyword.value = `@${username}`;
  fetchTopics({ author: username });
  // messageApi.info(`搜索作者: ${username}`);
};

onMounted(() => {
  fetchTopics();
});
</script>

<template>
  <div class="grid-cols-[3fr,8fr]">
  <contextHolder />
  <div class="min-h-screen transition-colors duration-500 py-8">
    <div class="container mx-auto px-4">
      <!-- 页面标题 -->
      <div class="mb-12 text-center animate-fade-in relative">
        <!-- 背景光效 -->
        <div class="absolute inset-0 -z-10 bg-gradient-to-r from-blue-100/30 to-purple-100/30 dark:from-blue-900/20 dark:to-purple-900/20 blur-2xl opacity-60"></div>



<!--        <div class="relative inline-block mb-4">-->
<!--          <div class="absolute -bottom-2 left-1/2 w-24 -translate-x-1/2 h-1 bg-gradient-to-r from-blue-400/60 to-purple-400/60 dark:from-blue-600/50 dark:to-purple-600/50 rounded-full"></div>-->
<!--        </div>-->

        <!-- 副标题 -->
        <p class="text-lg md:text-xl text-gray-600 dark:text-gray-300/90 max-w-2xl mx-auto leading-relaxed animate-slide-up animation-delay-300">
    <span class="inline-block bg-white/80 dark:bg-gray-800/80 px-2 rounded-lg backdrop-blur-sm border border-gray-100/50 dark:border-gray-700/50">
      分享你的想法，参与精彩讨论
    </span>
        </p>

      </div>

      <!-- 搜索和发布区域 -->
      <div class="flex flex-row justify-center items-center mb-6 gap-4 w-full max-w-5xl mx-auto animate-fade-in" style="animation-delay: 100ms;">
        <div class="relative w-2/3 group">
          <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400 group-hover:text-blue-500 transition-colors duration-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </div>
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="搜索话题、标签或使用@搜索作者..."
            class="w-full pl-10 pr-8 py-3 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800 text-gray-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500 transition-all duration-300 shadow-sm hover:shadow-md"
            @keyup.enter="handleSearch"
          />
          <button
            @click="handleSearch"
            class="absolute right-2 top-1/2 transform -translate-y-1/2 bg-blue-500 hover:bg-blue-600 text-white p-1 rounded-md transition-all duration-300 opacity-80 hover:opacity-100"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
            </svg>
          </button>
        </div>
        <button
          @click="createNewTopic"
          class="px-6 py-3 bg-gradient-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 text-white font-medium rounded-lg transition-all duration-300 flex items-center justify-center gap-2 shadow-md hover:shadow-lg transform hover:-translate-y-1 active:translate-y-0 whitespace-nowrap"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          发布新话题
        </button>
      </div>


      <!-- 筛选状态提示和重置按钮 -->
      <div v-if="isFiltering" class="mb-6 flex items-center justify-between px-4 py-3 bg-blue-50 dark:bg-blue-900/30 rounded-lg border border-blue-100 dark:border-blue-800 animate-fade-in">
        <div class="flex items-center text-blue-700 dark:text-blue-300">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
          </svg>
          <span>
            <span class="font-medium">
              {{ filterType === 'keyword' ? '搜索' : filterType === 'tag' ? '标签筛选' : '按作者名搜索' }}:
            </span>
            {{ filterType === 'keyword' ? searchKeyword : filterType === 'tag' ? filterTag.value : filterAuthor.value }}
            (共找到 {{ filteredTopics.length }} 个结果)
          </span>
        </div>
        <button 
          @click="resetFilter"
          class="flex items-center text-sm bg-white dark:bg-gray-800 px-3 py-1 rounded-md border border-blue-200 dark:border-blue-700 text-blue-600 dark:text-blue-400 hover:bg-blue-600 hover:text-white dark:hover:bg-blue-700 transition-colors duration-300"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
          </svg>
          显示全部话题
        </button>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="flex justify-center items-center py-12 animate-fade-in">
        <div class="relative">
          <div class="animate-ping absolute inline-flex h-full w-full rounded-full bg-blue-400 opacity-30"></div>
          <div class="animate-spin relative rounded-full h-16 w-16 border-4 border-blue-200 border-t-blue-500 border-r-blue-500"></div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else-if="filteredTopics.length === 0" class="text-center py-16 animate-fade-in">
        <svg xmlns="http://www.w3.org/2000/svg" class="h-20 w-20 mx-auto text-gray-300 dark:text-gray-600 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M3 4a1 1 0 011-1h16a1 1 0 011 1v2.586a1 1 0 01-.293.707l-6.414 6.414a1 1 0 00-.293.707V17l-4 4v-6.586a1 1 0 00-.293-.707L3.293 7.293A1 1 0 013 6.586V4z" />
        </svg>
        <p class="text-gray-500 dark:text-gray-400 text-lg font-medium">
          {{ isFiltering 
            ? `未找到与"${filterType === 'keyword' ? searchKeyword : filterType === 'tag' ? filterTag.value : filterAuthor.value}"相关的话题` 
            : '暂无话题，快来发布第一个话题吧！' }}
        </p>
        <div class="mt-4 flex justify-center gap-4">
          <button
            v-if="isFiltering"
            @click="resetFilter"
            class="px-6 py-2 bg-blue-100 dark:bg-blue-900 text-blue-700 dark:text-blue-300 font-medium rounded-lg transition-all duration-300 hover:bg-blue-200 dark:hover:bg-blue-800"
          >
            显示全部话题
          </button>
        <button
          @click="createNewTopic"
            class="px-6 py-2 bg-blue-100 dark:bg-blue-900 text-blue-700 dark:text-blue-300 font-medium rounded-lg transition-all duration-300 hover:bg-blue-200 dark:hover:bg-blue-800"
        >
          立即发布
        </button>
        </div>
      </div>


      <!-- 话题列表 -->
      <div v-else class="flex flex-col gap-6 w-[85%] max-w-6xl mx-auto">
        <!-- 浏览量第一的话题卡片 -->
        <div v-if="groupedTopics[0] && groupedTopics[0][0]" class="relative h-56">
          <div 
            class="w-full h-full  p-1.5 rounded-xl shadow-lg transform transition-all duration-500 hover:scale-[1.02] cursor-pointer"
            @click="handleTopicClick(groupedTopics[0][0].talk.id)"
            @mouseenter="handleMouseEnter(groupedTopics[0][0].talk.id)"
            @mouseleave="handleMouseLeave"
          >
            <div class="relative w-full h-full bg-white dark:bg-gray-700 rounded-lg p-6 overflow-hidden">
              <!-- 热门标记 -->
              <div class="absolute -top-2 -right-2 w-24 h-24">
                <div class="absolute transform rotate-45 bg-gradient-to-r from-yellow-400 to-yellow-600 text-white font-bold py-1 right-[-35px] top-[20px] w-[120px] text-center text-sm shadow-md">
                  热门话题
                </div>
              </div>
              
              <!-- 浏览量标记 -->
              <div class="absolute top-4 left-4 bg-yellow-100 dark:bg-yellow-700 px-3 py-1 rounded-full flex items-center gap-2 shadow-sm">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 text-yellow-600 dark:text-yellow-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                  </svg>
                <span class="text-yellow-600 dark:text-yellow-300 font-medium text-sm">{{ groupedTopics[0][0].talk.views > 999 ? '999+' : groupedTopics[0][0].talk.views }} 浏览</span>
              </div>

              <div class="flex flex-col h-full pt-8">
                <div class="flex-grow">
                  <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-4 line-clamp-1 pr-16">
                    {{ groupedTopics[0][0].talk.title }}
                  </h2>
                  <p class="text-gray-700 dark:text-gray-200 mb-4 line-clamp-2 text-lg">
                    {{ truncateText(groupedTopics[0][0].talk.content, 50) }}
                  </p>
                </div>

                <div class="flex flex-wrap gap-2 mb-4">
                  <span
                    v-for="tag in groupedTopics[0][0].talk.tags"
                    :key="tag"
                    @click.stop="handleTagClick(tag)"
                    class="px-3 py-1 bg-blue-100 dark:bg-blue-700 text-blue-800 dark:text-blue-200 text-sm font-medium rounded-full cursor-pointer hover:bg-blue-200 dark:hover:bg-blue-600 transition-colors duration-300 hover:scale-105 shadow-sm"
                  >
                    {{ tag }}
                </span>
                </div>

                <div class="flex justify-between items-center">
                  <div class="flex items-center gap-2">
                    <span class="font-medium text-blue-600 dark:text-blue-300 cursor-pointer hover:underline" @click.stop="handleAuthorClick(groupedTopics[0][0].username)">{{ groupedTopics[0][0].username }}</span>
                  </div>
                  <div class="flex items-center gap-4 text-sm text-gray-600 dark:text-gray-300">
                    <span class="flex items-center gap-1">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                  </svg>
                      {{ groupedTopics[0][0].talk.likes }}
                </span>
                    <span class="flex items-center gap-1">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                  </svg>
                      {{ groupedTopics[0][0].talk.comments }}
                    </span>
                    <span class="flex items-center gap-1">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                      </svg>
                      {{ formatDate(groupedTopics[0][0].talk.createTime) }}
                </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 其他话题卡片行 -->
        <div 
          v-for="(row, rowIndex) in groupedTopics.slice(1)" 
          :key="rowIndex"
          class="relative h-52 overflow-hidden"
        >
          <div class="flex justify-between w-full h-full gap-4">
            <div
              v-for="(topic, topicIndex) in row"
              :key="topic.talk.id"
              class="flex-1 flex flex-col justify-start items-stretch h-[95%] p-4 bg-white dark:bg-gray-700 rounded-lg cursor-pointer shadow-md transition-all duration-600 ease-in-out hover:bg-gray-50 dark:hover:bg-gray-600 border border-blue-100 dark:border-blue-800 relative"
              :class="[
                hoveredTopicId === topic.talk.id
                  ? 'opacity-100 flex-[1.2] shadow-lg shadow-blue-100 dark:shadow-gray-900/50 border-blue-300 dark:border-blue-600'
                  : 'opacity-70 flex-[0.8]'
              ]"
              @click="handleTopicClick(topic.talk.id)"
              @mouseenter="handleMouseEnter(topic.talk.id)"
              @mouseleave="handleMouseLeave"
            >
              <!-- 排名标记部分 -->
              <div
                v-if="rowIndex === 0 && topicIndex < 2"
                class="absolute -top-2 -right-2 w-24 h-24 overflow-hidden"
              >
                <!-- 排名装饰带 -->
                <div 
                  class="absolute transform rotate-45 text-white font-bold py-1 right-[-35px] top-[20px] w-[120px] text-center text-sm shadow-md"
                  :class="[
                    topicIndex === 0 
                      ? 'bg-gradient-to-r from-gray-300 via-gray-400 to-gray-500' 
                      : 'bg-gradient-to-r from-amber-500 via-amber-600 to-amber-700'
                  ]"
                >
                  {{ topicIndex === 0 ? '第二名' : '第三名' }}
                </div>
              </div>
              
              <div class="flex flex-col h-full">
                <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-1 line-clamp-1">
                  {{ topic.talk.title }}
                </h2>
                <p class="text-gray-700 dark:text-gray-200 mb-2 line-clamp-2 flex-grow">
                  {{ truncateText(topic.talk.content) }}
                </p>
                <div class="flex flex-wrap gap-1 mb-2">
              <span
                v-for="tag in topic.talk.tags"
                :key="tag"
                @click.stop="handleTagClick(tag)"
                    class="px-1.5 py-0.5 bg-blue-100 dark:bg-blue-700 text-blue-800 dark:text-blue-200 text-xs font-medium rounded-full cursor-pointer hover:bg-blue-200 dark:hover:bg-blue-600 transition-colors duration-300 shadow-sm"
              >
                {{ tag }}
              </span>
            </div>
                
                <!-- 互动信息区域 (简化版) -->
                <div class="flex flex-col gap-1">
                  <!-- 用户和日期 -->
                  <div class="flex justify-between items-center text-xs text-gray-600 dark:text-gray-300">
                    <div class="flex items-center gap-1">
                      <span class="font-medium text-blue-600 dark:text-blue-300 cursor-pointer hover:underline" @click.stop="handleAuthorClick(topic.username)">{{ topic.username }}</span>
                    </div>
                    <div class="flex items-center gap-1">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                      <span>{{ formatDate(topic.talk.createTime) }}</span>
                    </div>
                  </div>
                  
                  <!-- 互动数据 -->
                  <div class="flex items-center justify-between text-xs text-gray-600 dark:text-gray-300 pt-1 border-t border-gray-100 dark:border-gray-600">
                    <span class="flex items-center gap-0.5 group">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 group-hover:text-blue-500 transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                      </svg>
                      <span class="group-hover:text-blue-500 transition-colors">{{ topic.talk.views }}</span>
                    </span>
                    <span class="flex items-center gap-0.5 group">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 group-hover:text-red-500 transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                      </svg>
                      <span class="group-hover:text-red-500 transition-colors">{{ topic.talk.likes }}</span>
                    </span>
                    <span class="flex items-center gap-0.5 group">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3 group-hover:text-green-500 transition-colors" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
                      </svg>
                      <span class="group-hover:text-green-500 transition-colors">{{ topic.talk.comments }}</span>
                    </span>
                    <span class="flex items-center gap-0.5 text-blue-500 dark:text-blue-300 font-medium text-[10px] hover:underline">
                      详情
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                      </svg>
              </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  </div>
</template>

<style scoped>
/* 明暗主题过渡效果 */
* {
  transition: background-color 0.3s, color 0.3s, border-color 0.3s;
}

/* 卡片悬停过渡效果 */
.flex-1 {
  transition: all 0.9s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 文本截断样式 */
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 动画效果 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideDown {
  from { transform: translateY(-20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

@keyframes slideUp {
  from { transform: translateY(20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

@keyframes float {
  0% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
  100% { transform: translateY(0px); }
}

.animate-fade-in {
  animation: fadeIn 0.6s ease-out forwards;
}

.animate-slide-down {
  animation: slideDown 0.6s ease-out forwards;
}

.animate-slide-up {
  animation: slideUp 0.6s ease-out forwards;
}

.animate-float {
  animation: float 3s ease-in-out infinite;
}

/* 搜索框效果 */
input:focus {
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.3);
}

/* 渐变边框动画 */
@keyframes gradient {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

.bg-gradient-to-r {
  background-size: 200% 200%;
  animation: gradient 6s ease infinite;
}

/* 奖牌闪光动画 */
@keyframes shine {
  0% { opacity: 0.3; }
  50% { opacity: 0.5; }
  100% { opacity: 0.3; }
}

.opacity-30 {
  animation: shine 2s ease-in-out infinite;
}

/* 互动图标悬停效果 */
.group:hover svg {
  transform: scale(1.2);
  transition: transform 0.2s ease;
}
</style>