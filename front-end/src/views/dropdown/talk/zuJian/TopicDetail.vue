<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useThemeStore } from '@/stores/theme.js';
import { message } from 'ant-design-vue';
import { get, post, del } from '@/net/index.js';
import { formatDate } from "@/util/DateFormattor.js";
import {useUserStore} from "@/stores/userStore.js";
import {baseURL} from "@/main.js";
import CustomConfirmDialog from '@/components/zuJian/TanChuang/CustomConfirmDialog.vue';

const userStore = useUserStore();
const route = useRoute();
const router = useRouter();
const themeStore = useThemeStore();
const [messageApi, contextHolder] = message.useMessage();

const topicId = route.params.id;
const newComment = ref('');

// 检查当前用户是否有权限删除话题
const canDeleteTopic = computed(() => {
  const isAdmin = userStore.user?.manager === 1;
  const isAuthor = userStore.user?.id === topicData.talk.authorId;
  return isAdmin || isAuthor;
});

// 话题数据
const topicData = reactive({
  talk: {
    id: parseInt(topicId),
    title: '',
    content: '',
    views: 0,
    likes: 0,
    comments: 0,
    createTime: '',
    tags: []
  },
  username: '',
  avatar: '',
  isLiked: false,
  comments: []
});

// 获取话题详情
const fetchTopicDetail = () => {
  get(`/api/talk/detail`, { id: topicId }, (message, data) => {
    if (data) {
      // 更新话题数据
      topicData.talk = data.talk;
      topicData.username = data.username;
      topicData.avatar = data.avatar;
      topicData.isLiked = data.isLiked;

      // 加载评论
      fetchComments();
    } else {
      messageApi.error('获取话题详情失败');
    }
  }, (error) => {
    messageApi.error(error || '获取话题详情失败');
  });
};

// 获取评论
const fetchComments = () => {
  get(`/api/talk/comments`, { talkId: topicId }, (message, data) => {
    topicData.comments = data || [];
  });
};

// 处理点赞话题
const handleLikeTopic = () => {
  // 确定请求URL
  const url = topicData.isLiked ? '/api/talk/unlike' : '/api/talk/like';

  // 使用post函数发送请求
  post(url, {
    id: topicId
  }, (message) => {
    if (topicData.isLiked) {
      // 取消点赞
      topicData.talk.likes--;
      topicData.isLiked = false;
      messageApi.warning(message || '已取消点赞');
    } else {
      // 点赞
      topicData.talk.likes++;
      topicData.isLiked = true;
      messageApi.success(message || '点赞成功');
    }
  }, (message) => {
    messageApi.warning(message || '操作失败');
  }, (message) => {
    messageApi.error(message || '操作失败，请稍后重试');
  });
};

// 提交评论
const submitComment = () => {
  if (!newComment.value.trim()) {
    messageApi.warning('评论内容不能为空');
    return;
  }

  // 使用post函数发送请求
  post('/api/talk/comment', {
    talkId: topicId,
    content: newComment.value
  }, (message) => {
    // 清空评论框
    newComment.value = '';
    messageApi.success(message || '评论成功');

    // 重新加载评论
    fetchComments();

    // 更新评论数
    topicData.talk.comments++;
  }, (message) => {
    messageApi.warning(message || '评论失败');
  }, (message) => {
    messageApi.error(message || '评论失败，请稍后重试');
  });
};

// 返回话题列表
const goBack = () => {
  router.push('/talk');
};

// 删除对话框状态
const deleteModalVisible = ref(false);
const deleteCommentModalVisible = ref(false);
const currentCommentId = ref(null);

// 删除话题
const deleteTopic = () => {
  deleteModalVisible.value = true;
};

// 确认删除话题
const confirmDelete = () => {
  del('/api/talk/delete', { id: topicId }, (message) => {
    messageApi.success(message || '话题删除成功');
    // 删除成功后返回话题列表
    setTimeout(() => {
      router.push('/talk');
    }, 1500);
  }, (message) => {
    messageApi.warning(message || '删除失败');
  }, (message) => {
    messageApi.error(message || '操作失败，请稍后重试');
  });
};

// 检查用户是否有权限删除评论
const canDeleteComment = (comment) => {
  const isAdmin = userStore.user?.manager === 1;
  const isCommentAuthor = comment.comment.authorId === userStore.user?.id;
  const isTalkAuthor = topicData.talk.authorId === userStore.user?.id;
  return isAdmin || isCommentAuthor || isTalkAuthor;
};

// 处理删除评论按钮点击
const handleDeleteComment = (commentId) => {
  currentCommentId.value = commentId;
  deleteCommentModalVisible.value = true;
};

// 确认删除评论
const confirmDeleteComment = () => {
  if (!currentCommentId.value) return;
  
  del('/api/talk/comment/delete', { id: currentCommentId.value }, (message) => {
    messageApi.success(message || '评论删除成功');
    // 更新评论数
    topicData.talk.comments--;
    // 重新加载评论
    fetchComments();
  }, (message) => {
    messageApi.warning(message || '删除失败');
  }, (message) => {
    messageApi.error(message || '操作失败，请稍后重试');
  });
};

onMounted(() => {
  fetchTopicDetail();
});
</script>

<template>
  <contextHolder />
  <div class="min-h-screen bg-gradient-to-b  transition-all duration-500 py-8">
    <div class="container max-w-4xl mx-auto px-4">
      <!-- 返回按钮 -->
      <div class="mb-6 flex justify-between items-center">
        <button
            @click="goBack"
            class="group flex items-center text-blue-600 dark:text-blue-400 hover:text-blue-800 dark:hover:text-blue-300 font-medium transition-all duration-300"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 transform group-hover:-translate-x-1 transition-transform duration-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
          </svg>
          返回话题列表
        </button>
        
        <!-- 删除话题按钮 -->
        <button
          v-if="canDeleteTopic"
          @click="deleteTopic"
          class="flex items-center gap-2 px-4 py-2 bg-red-50 dark:bg-red-900/20 text-red-600 dark:text-red-400 border border-red-200 dark:border-red-800 rounded-lg hover:bg-red-100 dark:hover:bg-red-900/40 transition-colors duration-300"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
          </svg>
          删除话题
        </button>
      </div>

      <!-- 话题详情 -->
      <div class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl overflow-hidden border-0 mb-8 transition-all duration-300 hover:shadow-2xl">
        <!-- 作者信息栏 -->
        <div class="flex items-center p-6 border-b border-gray-100 dark:border-gray-700">
          <div class="w-12 h-12 rounded-full bg-blue-100 dark:bg-blue-900 overflow-hidden mr-4 flex items-center justify-center text-blue-600 dark:text-blue-300">
            <img v-if="topicData.avatar" :src="`http://localhost:8080/${topicData.avatar}`" alt="用户头像" class="w-full h-full object-cover" />
            <img v-else :src="'https://img95.699pic.com/element/40109/0194.png_300.png'" alt="用户头像" class="w-full h-full object-cover" />
          </div>
          <div>
            <h3 class="font-semibold text-lg text-blue-600 dark:text-blue-400">{{ topicData.username }}</h3>
            <p class="text-sm text-gray-500 dark:text-gray-400">{{ formatDate(topicData.talk.createTime) }}</p>
          </div>
          <div class="ml-auto flex items-center space-x-4">
            <div class="flex items-center text-gray-500 dark:text-gray-400">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
              </svg>
              <span>{{ topicData.talk.views }}</span>
            </div>
          </div>
        </div>

        <div class="p-6">
          <div class="mb-6">
            <h1 class="text-3xl font-bold text-gray-900 dark:text-white mb-5">{{ topicData.talk.title }}</h1>

            <div class="flex flex-wrap gap-2 mb-6">
              <span
                  v-for="tag in topicData.talk.tags"
                  :key="tag"
                  class="px-3 py-1 bg-blue-50 dark:bg-blue-900/40 text-blue-600 dark:text-blue-300 text-xs font-medium rounded-full border border-blue-100 dark:border-blue-800 transition-all duration-300 hover:bg-blue-100 dark:hover:bg-blue-800"
              >
                {{ tag }}
              </span>
            </div>
          </div>

          <div class="mb-8">
            <p class="text-gray-700 dark:text-gray-200 whitespace-pre-line leading-relaxed text-lg">{{ topicData.talk.content }}</p>
          </div>

          <div class="flex justify-end">
            <button
                @click="handleLikeTopic"
                class="flex items-center gap-2 px-5 py-2.5 rounded-full transition-all duration-300 transform hover:scale-105"
                :class="topicData.isLiked ? 'bg-red-50 dark:bg-red-900/30 text-red-600 dark:text-red-300 border border-red-200 dark:border-red-800' : 'bg-gray-50 dark:bg-gray-700 text-gray-600 dark:text-gray-300 hover:bg-red-50 dark:hover:bg-red-900/30 border border-gray-200 dark:border-gray-600'"
            >
              <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-5 w-5 transition-all duration-300"
                  :class="topicData.isLiked ? 'text-red-500 fill-current' : ''"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
              >
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
              </svg>
              <span>{{ topicData.isLiked ? '已点赞' : '点赞' }} ({{ topicData.talk.likes }})</span>
            </button>
          </div>
        </div>
      </div>

      <!-- 评论区 -->
      <div class="bg-white dark:bg-gray-800 rounded-2xl shadow-xl border-0 mb-8">
        <div class="p-6 border-b border-gray-100 dark:border-gray-700">
          <h2 class="text-2xl font-bold text-gray-900 dark:text-white flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 mr-2 text-blue-500 dark:text-blue-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z" />
            </svg>
            评论区 <span class="ml-2 text-lg font-normal text-gray-500 dark:text-gray-400">({{ topicData.talk.comments }})</span>
          </h2>
        </div>

        <!-- 评论输入框 -->
        <div class="p-6 bg-gray-50 dark:bg-gray-900">
          <textarea
              v-model="newComment"
              rows="3"
              placeholder="分享你的想法..."
              class="w-full px-4 py-3 rounded-xl border border-gray-200 dark:border-gray-700 bg-white dark:bg-gray-800 text-gray-900 dark:text-white placeholder-gray-500 dark:placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all duration-300 resize-none"
          ></textarea>
          <div class="flex justify-end mt-3">
            <button
                @click="submitComment"
                class="px-6 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-full transition-all duration-300 transform hover:scale-105 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 dark:focus:ring-offset-gray-900 shadow-md"
            >
              发表评论
            </button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="divide-y divide-gray-100 dark:divide-gray-700">
          <div v-if="topicData.comments.length === 0" class="text-center py-12">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-gray-300 dark:text-gray-600 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
            </svg>
            <p class="text-gray-500 dark:text-gray-400 text-lg">暂无评论，来发表第一条评论吧！</p>
          </div>

          <div
              v-for="comment in topicData.comments"
              :key="comment.comment.id"
              class="p-5 hover:bg-gray-50 dark:hover:bg-gray-800/30 transition-colors duration-300"
          >
            <div class="flex">
              <!-- 头像 -->
              <div class="flex-shrink-0 mr-3">
                <div class="w-10 h-10 rounded-full overflow-hidden border border-gray-200 dark:border-gray-700">
                  <img v-if="comment.avatar" :src=baseURL+comment.avatar alt="用户头像" class="w-full h-full object-cover" />
                  <img v-else :src="'https://img95.699pic.com/element/40109/0194.png_300.png'" alt="用户头像" class="w-full h-full object-cover" />
                </div>
              </div>
              
              <!-- 评论内容 -->
              <div class="flex-1">
                <!-- 用户信息行 -->
                <div class="flex items-center justify-between mb-1">
                  <div class="flex items-center">
                    <span class="font-medium text-gray-900 dark:text-gray-100">{{ comment.author }}</span>
                    <span 
                      v-if="comment.author === topicData.username" 
                      class="ml-2 text-xs px-1.5 py-0.5 bg-blue-100 dark:bg-blue-900/50 text-blue-700 dark:text-blue-300 rounded-sm"
                    >
                      作者
                    </span>
                    <span v-if="userStore.user?.manager === 1"
                          class="ml-2 text-xs px-1.5 py-0.5 bg-orange-100 dark:bg-orange-900/30 text-orange-600 dark:text-orange-300 rounded-sm"
                    >
                      管理员
                    </span>
                    <span v-else
                          class="ml-2 text-xs px-1.5 py-0.5 bg-orange-100 dark:bg-orange-900/30 text-orange-600 dark:text-orange-300 rounded-sm"
                    >
                      学生
                    </span>
                  </div>
                  <div class="flex items-center">
                    <span class="text-xs text-gray-500 dark:text-gray-400 mr-2">{{ formatDate(comment.comment.createTime) }}</span>
                    <!-- 删除评论按钮 -->
                    <button 
                      v-if="canDeleteComment(comment)"
                      @click="handleDeleteComment(comment.comment.id)" 
                      class="text-gray-400 hover:text-red-500 dark:text-gray-500 dark:hover:text-red-400 transition-colors duration-300"
                      title="删除评论"
                    >
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                      </svg>
                    </button>
                  </div>
                </div>
                <!-- 评论内容 -->
                <div class="text-gray-700 dark:text-gray-300 leading-relaxed break-words border border-gray-100 dark:border-gray-700
                            rounded-xl  bg-gray-50 dark:bg-gray-800/50 p-2 ">
                  {{ comment.comment.content }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 删除确认对话框 -->
  <CustomConfirmDialog
    v-model:visible="deleteModalVisible"
    title="删除确认"
    content="您确定要删除此话题吗？此操作不可撤销，删除后话题将永久消失。"
    type="danger"
    confirm-text="确认删除"
    cancel-text="取消"
    @confirm="confirmDelete"
  />

  <!-- 评论删除确认对话框 -->
  <CustomConfirmDialog
    v-model:visible="deleteCommentModalVisible"
    title="删除确认"
    content="您确定要删除此评论吗？此操作不可撤销，删除后评论将永久消失。"
    type="danger"
    confirm-text="确认删除"
    cancel-text="取消"
    @confirm="confirmDeleteComment"
  />
</template>

<style scoped>
/* 明暗主题过渡效果 */
* {
  transition: background-color 0.3s, color 0.3s, border-color 0.3s, transform 0.3s, box-shadow 0.3s;
}

/* 自定义暗色模式背景色 */
.dark .bg-gray-750 {
  background-color: #1f2937;
}

/* 文本样式 */
.whitespace-pre-line {
  white-space: pre-line;
}

/* 卡片悬浮效果 */
.shadow-xl {
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.dark .shadow-xl {
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.3), 0 10px 10px -5px rgba(0, 0, 0, 0.2);
}

.hover\:shadow-2xl:hover {
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
}

.dark .hover\:shadow-2xl:hover {
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
}

/* 评论框激活状态 */
textarea:focus {
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
}

.dark textarea:focus {
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.3);
}

/* 评论文本样式 */
.break-words {
  word-break: break-word;
}

/* 确保评论内容正确换行 */
.whitespace-pre-wrap {
  white-space: pre-wrap;
}
</style>