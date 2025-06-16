<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useThemeStore } from '@/stores/theme.js';
import { message } from 'ant-design-vue';
import { post } from '@/net/index.js';

const router = useRouter();
const themeStore = useThemeStore();
const [messageApi, contextHolder] = message.useMessage();

// 话题表单数据
const topicForm = reactive({
  title: '',
  content: '',
  tags: []
});

// 标签输入
const tagInput = ref('');
const addTag = () => {
  const tag = tagInput.value.trim();
  if (tag && !topicForm.tags.includes(tag)) {
    if (topicForm.tags.length >= 5) {
      messageApi.warning('最多添加5个标签');
      return;
    }
    topicForm.tags.push(tag);
    tagInput.value = '';
  }
};

const removeTag = (index) => {
  topicForm.tags.splice(index, 1);
};

// 提交话题
const submitTopic = () => {
  // 表单验证
  if (!topicForm.title.trim()) {
    messageApi.warning('请输入话题标题');
    return;
  }
  if (!topicForm.content.trim()) {
    messageApi.warning('请输入话题内容');
    return;
  }
  if (topicForm.tags.length === 0) {
    messageApi.warning('请至少添加一个标签');
    return;
  }

  // 使用post函数发送请求，将tags数组转换为逗号分隔的字符串
  post('/api/talk/create', {
    title: topicForm.title,
    content: topicForm.content,
    tags: topicForm.tags.join(',')  // 将标签数组转换为逗号分隔的字符串
  }, (message) => {
    messageApi.success(message || '话题发布成功');
    router.push('/talk');
  }, (message) => {
    messageApi.warning(message || '发布失败');
  }, (message) => {
    messageApi.error(message || '发布失败，请稍后重试');
  });
};

// 取消发布
const cancelCreate = () => {
  router.push('/talk');
};
</script>

<template>
  <contextHolder />
  <div class="min-h-screen bg-gray-50 dark:bg-gray-900 transition-colors duration-500 py-8">
    <div class="container mx-auto px-4">
      <!-- 页面标题 -->
      <div class="mb-8">
        <h1 class="text-2xl font-bold text-gray-900 dark:text-white mb-2">发布新话题</h1>
        <p class="text-gray-600 dark:text-gray-400">分享你的想法，开启一场有意义的讨论</p>
      </div>

      <!-- 话题表单 -->
      <div class="bg-white dark:bg-gray-800 rounded-xl shadow-lg overflow-hidden border border-gray-100 dark:border-gray-700 p-6 mb-6">
        <!-- 标题输入 -->
        <div class="mb-6">
          <label for="title" class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">话题标题</label>
          <input
            id="title"
            v-model="topicForm.title"
            type="text"
            placeholder="请输入话题标题（简洁明了）"
            class="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800 text-gray-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500 transition-colors duration-300"
          />
        </div>

        <!-- 内容输入 -->
        <div class="mb-6">
          <label for="content" class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">话题内容</label>
          <textarea
            id="content"
            v-model="topicForm.content"
            rows="8"
            placeholder="详细描述你的话题，提出你的问题或观点..."
            class="w-full px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800 text-gray-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500 transition-colors duration-300 resize-none"
          ></textarea>
        </div>

        <!-- 标签输入 -->
        <div class="mb-6">
          <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">话题标签</label>
          <div class="flex flex-wrap gap-2 mb-3">
            <span
              v-for="(tag, index) in topicForm.tags"
              :key="index"
              class="px-3 py-1 bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-200 text-xs font-medium rounded-full flex items-center"
            >
              {{ tag }}
              <button 
                @click="removeTag(index)"
                class="ml-1 text-blue-600 dark:text-blue-300 hover:text-blue-800 dark:hover:text-blue-100"
              >
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </span>
          </div>
          <div class="flex">
            <input
              v-model="tagInput"
              type="text"
              placeholder="输入标签，按回车添加"
              class="flex-1 px-4 py-2 rounded-l-lg border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-800 text-gray-900 dark:text-white focus:outline-none focus:ring-2 focus:ring-blue-500 transition-colors duration-300"
              @keyup.enter="addTag"
            />
            <button
              @click="addTag"
              class="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-r-lg transition-colors duration-300"
            >
              添加
            </button>
          </div>
          <p class="mt-2 text-xs text-gray-500 dark:text-gray-400">最多添加5个标签，每个标签按回车或点击添加按钮确认</p>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="flex justify-end gap-4">
        <button
          @click="cancelCreate"
          class="px-6 py-2 bg-gray-200 hover:bg-gray-300 dark:bg-gray-700 dark:hover:bg-gray-600 text-gray-800 dark:text-gray-200 font-medium rounded-lg transition-colors duration-300"
        >
          取消
        </button>
        <button
          @click="submitTopic"
          class="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition-colors duration-300"
        >
          发布话题
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 明暗主题过渡效果 */
* {
  transition: background-color 0.3s, color 0.3s, border-color 0.3s;
}
</style>