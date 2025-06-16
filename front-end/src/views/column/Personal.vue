<script setup>
import { onMounted, reactive, ref, computed, watch } from "vue";
import { get, post } from "@/net/index.js";
import { formatDate } from "@/util/DateFormattor.js";
import { message } from "ant-design-vue";
import { useUserStore } from '@/stores/userStore.js';
import ModifyPi from "@/components/zuJian/ModifyPi.vue";
import ShowPi from "@/components/zuJian/ShowPi.vue";
import router from "@/router/index.js";
import axios from "axios";
import {baseURL} from "@/main.js";

// 初始化状态和API
const userStore = useUserStore();
const [messageApi, contextHolder] = message.useMessage();
const avatarInput = ref(null);  // 添加avatarInput的ref定义

// 响应式状态管理
const options = reactive({
  selected: {
    notificationType: 2 // 默认显示"我的"
  },
  currentNotifications: [],
  unreadCounts: {
    like: 0,
    comment: 0,
    total: 0
  }
});

// 显示退出登录确认弹窗的状态
const showLogoutConfirm = ref(false);

// 退出登录函数
const showLogoutDialog = () => {
  showLogoutConfirm.value = true;
};

const confirmLogout = () => {
  localStorage.removeItem('authToken');
  userStore.$reset();
  messageApi.success('已成功退出登录');
  showLogoutConfirm.value = false;
  router.push('/auth');
};

const cancelLogout = () => {
  showLogoutConfirm.value = false;
};

// 阻止弹窗内容点击冒泡到遮罩层
const stopPropagation = (event) => {
  event.stopPropagation();
};

// 通知数据
const commentNotifications = ref([]);
const deletedTalkNotifications = ref([]); // 新增话题删除通知数据

// 添加个人简介数据
const personalInfo = reactive({
  sex: null,
  age: null,
  hobby: '',
  major: '',
  type: '',
  introduction: '',
  contact: ''
});

// 处理菜单项点击
const buttonClickHandler = (buttonId) => {
  options.selected.notificationType = buttonId;
  if (buttonId === 3) {
    fetchFollowNotifications();
    fetchDeletedTalkNotifications(); // 获取话题删除信息
  } else if (buttonId === 4) {
    fetchCommentNotifications();
  } else {
    fetchNotificationsByType(buttonId - 1);
  }
};

// 获取评论通知数据
const fetchCommentNotifications = () => {
  fetchNotificationsByType(2); // 评论通知类型为2
};

// 获取关注通知数据
const fetchFollowNotifications = () => {
  fetchNotificationsByType(3); // 关注通知类型为3
};

// 获取话题删除通知数据
const fetchDeletedTalkNotifications = () => {
  get('/api/user/getNotifications', {
    type: 1 // 假设删除通知类型为1，实际需要根据后端定义调整
  }, (message, data) => {
    if (data) {
      // 过滤出与话题删除相关的通知
      const talkDeleteNotifications = data.filter(noti => 
        noti.content.includes('删除') && noti.content.includes('话题')
      );
      deletedTalkNotifications.value = talkDeleteNotifications;
    }
  }, () => {
    messageApi.error('获取话题删除通知失败');
  });
};

// 获取通知数据
const fetchNotificationsByType = (type) => {
  get('/api/user/getNotifications', {
        type: type
      },
      (message, data) => {
        if (type === 0) { // 点赞通知
          // 过滤掉与评论相关的点赞通知
          const likeNotifications = data ? data.filter(noti =>
            !noti.content.includes('评论') && !noti.content.includes('回复')
          ) : [];
          options.currentNotifications = likeNotifications;
          options.unreadCounts.like = likeNotifications.filter(noti => !noti.isRead).length;
        } else if (type === 2) { // 评论通知
          // 先保存原始评论通知
          const originalComments = data || [];

          // 获取点赞通知中与评论相关的通知
          get('/api/user/getNotifications', { type: 0 },
            (msg, likeData) => {
              const commentRelatedLikes = likeData ? likeData.filter(noti =>
                noti.content.includes('评论') || noti.content.includes('回复')
              ) : [];

              // 合并两类通知
              const allCommentNotifications = [...originalComments, ...commentRelatedLikes];

              // 按时间排序，最新的排在前面
              allCommentNotifications.sort((a, b) =>
                new Date(b.createTime) - new Date(a.createTime)
              );

              commentNotifications.value = allCommentNotifications;
              options.unreadCounts.comment = allCommentNotifications.filter(noti => !noti.isRead).length;
            },
            () => {
              // 如果获取点赞通知失败，只使用原始评论通知
              commentNotifications.value = originalComments;
              options.unreadCounts.comment = originalComments.filter(noti => !noti.isRead).length;
            }
          );
        }
      },
      () => {
        messageApi.error('获取通知失败，请稍后重试');
      });
};

// 清空通知
const clearNotification = (type) => {
  post('/api/user/clearNotification', {
    type: type
  }, (message) => {
    messageApi.success(message || '通知已清空');
    if (type === 2) {
      fetchFollowNotifications();
    } else if (type === 3) {
      fetchCommentNotifications();
    } else {
      fetchNotificationsByType(type);
    }
  }, () => {
    messageApi.error('清空通知失败，请稍后重试');
  });
};

// 获取未读消息数
const fetchUnreadCounts = () => {
  get('/api/user/myinfo', {},
    (message, data) => {
      options.unreadCounts.total = data?.unReadNotiCnt || 0;
      // 获取点赞和评论的未读消息数
      fetchNotificationsByType(0); // 点赞
      fetchNotificationsByType(2); // 评论
    },
    () => {
      messageApi.error('获取未读消息数失败');
    });
};

// 跳转到相关话题
const goToTopic = (topicId) => {
  if (!topicId) {
    messageApi.warning('没有关联的话题');
    return;
  }
  router.push(`/talk/detail/${topicId}`);
};

// 标记单条消息为已读
const markAsRead = (notificationId) => {
  post('/api/user/readNotification', {
    id: notificationId
  }, (message) => {
    messageApi.success(message || '已标记为已读');
    fetchUnreadCounts();
    if (options.selected.notificationType === 1) {
      fetchNotificationsByType(0);
    } else if (options.selected.notificationType === 3) {
      fetchNotificationsByType(2);
    }
  }, () => {
    messageApi.error('操作失败，请稍后重试');
  });
};

// 标记所有消息为已读
const markAllAsRead = () => {
  const type = options.selected.notificationType === 1 ? 0 : 2; // 将前端类型转换为后端类型
  post('/api/user/clearNotification', {
    type: type
  }, (message) => {
    messageApi.success(message || '已全部标记为已读');
    fetchUnreadCounts();
    if (options.selected.notificationType === 1) {
      fetchNotificationsByType(0);
    } else if (options.selected.notificationType === 3) {
      fetchNotificationsByType(2);
    }
  }, () => {
    messageApi.error('操作失败，请稍后重试');
  });
};

// 从通知内容中提取话题ID
const extractTalkId = (notification) => {
  // 如果通知对象已经包含话题相关字段，直接返回
  if (notification.talkId) return notification.talkId;
  if (notification.topicId) return notification.topicId;

  // 通过正则表达式从通知内容中提取话题ID
  // 例如："用户xxx点赞了你的话题《xxx》(ID: 123)"
  const idMatch = notification.content?.match(/ID[:：]\s*(\d+)/i);
  if (idMatch && idMatch[1]) {
    return parseInt(idMatch[1]);
  }
  
  // 提取话题标题，通过API查询ID
  const titleMatch = notification.content?.match(/[《"](.*?)[》"]/);
  if (titleMatch && titleMatch[1]) {
    // 对于包含标题但没有ID的通知，异步获取ID并存储在通知对象中
    const title = titleMatch[1];
    
    // 如果之前没有查询过，开始异步查询
    if (!notification.queryingTitle) {
      notification.queryingTitle = true;
      
      get('/api/talk/searchByTitle', { title: title }, (message, data) => {
        if (data && data.id) {
          // 存储话题ID以便下次直接使用
          notification.talkId = data.id;
          // 强制更新视图
          options.currentNotifications = [...options.currentNotifications];
          commentNotifications.value = [...commentNotifications.value];
        }
      });
    }
  }

  return null;
};

// 侧边栏菜单项
const sideBarObjects = [
  {
    typeId: 1,
    name: "我的",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-6">
  <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 6a3.75 3.75 0 1 1-7.5 0 3.75 3.75 0 0 1 7.5 0ZM4.501 20.118a7.5 7.5 0 0 1 14.998 0A17.933 17.933 0 0 1 12 21.75c-2.676 0-5.216-.584-7.499-1.632Z" />
</svg>
`,
    id: 2
  },
  {
    typeId: 0,
    name: "点赞",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-8"><path stroke-linecap="round" stroke-linejoin="round" d="M6.633 10.25c.806 0 1.533-.446 2.031-1.08a9.041 9.041 0 0 1 2.861-2.4c.723-.384 1.35-.956 1.653-1.715a4.498 4.498 0 0 0 .322-1.672V2.75a.75.75 0 0 1 .75-.75 2.25 2.25 0 0 1 2.25 2.25c0 1.152-.26 2.243-.723 3.218-.266.558.107 1.282.725 1.282m0 0h3.126c1.026 0 1.945.694 2.054 1.715.045.422.068.85.068 1.285a11.95 11.95 0 0 1-2.649 7.521c-.388.482-.987.729-1.605.729H13.48c-.483 0-.964-.078-1.423-.23l-3.114-1.04a4.501 4.501 0 0 0-1.423-.23H5.904m10.598-9.75H14.25M5.904 18.5c.083.205.173.405.27.602.197.4-.078.898-.523.898h-.908c-.889 0-1.713-.518-1.972-1.368a12 12 0 0 1-.521-3.507c0-1.553.295-3.036.831-4.398C3.387 9.953 4.167 9.5 5 9.5h1.053c.472 0 .745.556.5.96a8.958 8.958 0 0 0-1.302 4.665c0 1.194.232 2.333.654 3.375Z" /></svg>`,
    id: 1
  },
  {
    typeId: 2,
    name: "话题",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-8"><path stroke-linecap="round" stroke-linejoin="round" d="M8.625 12a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H8.25m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0H12m4.125 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm0 0h-.375M21 12c0 4.556-4.03 8.25-9 8.25a9.764 9.764 0 0 1-2.555-.337A5.972 5.972 0 0 1 5.41 20.97a5.969 5.969 0 0 1-.474-.065 4.48 4.48 0 0 0 .978-2.025c.09-.457-.133-.901-.467-1.226C3.93 16.178 3 14.189 3 12c0-4.556 4.03-8.25 9-8.25s9 3.694 9 8.25Z" /></svg>`,
    id: 3
  },
  {
    typeId: 3,
    name: "关于",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-8"><path stroke-linecap="round" stroke-linejoin="round" d="M11.25 11.25l.041-.02a.75.75 0 011.063.852l-.708 2.836a.75.75 0 001.063.853l.041-.021M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9-3.75h.008v.008H12V8.25z" /></svg>`,
    id: 4
  },
  {
    typeId: 4,
    name: "关于",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-8"><path stroke-linecap="round" stroke-linejoin="round" d="M11.25 11.25l.041-.02a.75.75 0 011.063.852l-.708 2.836a.75.75 0 001.063.853l.041-.021M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9-3.75h.008v.008H12V8.25z" /></svg>`,
    id: 5
  },
  {
    typeId: 5,
    name: "关于",
    svg: `<svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="size-8"><path stroke-linecap="round" stroke-linejoin="round" d="M11.25 11.25l.041-.02a.75.75 0 011.063.852l-.708 2.836a.75.75 0 001.063.853l.041-.021M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9-3.75h.008v.008H12V8.25z" /></svg>`,
    id: 6
  },
];

// 格式化时间，处理各种可能的时间格式
const formatNotificationTime = (timeData) => {
  if (!timeData) return formatDate(new Date()) + ' (系统时间)';

  try {
    // 如果是字符串日期，直接使用formatDate函数
    if (typeof timeData === 'string') {
      return formatDate(timeData);
    }

    // 如果是数字（时间戳），转换为Date对象
    if (typeof timeData === 'number') {
      return formatDate(new Date(timeData));
    }

    // 处理后端返回的时间对象
    if (typeof timeData === 'object') {
      // 检查是否是Java后端返回的日期对象（包含year, month, date等字段）
      if (timeData.year !== undefined && timeData.month !== undefined && timeData.date !== undefined) {
        const year = timeData.year + 1900; // Java中年份是从1900年开始的
        const month = timeData.month; // Java中月份是从0开始的
        const day = timeData.date;
        const hours = timeData.hours || 0;
        const minutes = timeData.minutes || 0;
        const seconds = timeData.seconds || 0;
        
        const dateStr = `${year}-${month + 1}-${day} ${hours}:${minutes}:${seconds}`;
        return formatDate(new Date(dateStr));
      }
      
      // 检查是否有时间相关属性
      if (timeData.time || timeData.timestamp) {
        return formatDate(new Date(timeData.time || timeData.timestamp));
      }

      // 尝试将对象转为JSON字符串，再解析为日期
      try {
        // 直接使用日期构造函数尝试解析
        const date = new Date(timeData);
        if (!isNaN(date.getTime())) {
          return formatDate(date);
        }
        
        // 如果上面的方法失败，尝试JSON解析
        const dateStr = JSON.stringify(timeData);
        return formatDate(new Date(JSON.parse(dateStr)));
      } catch (e) {
        console.error('无法解析时间对象:', e);
      }
    }

    // 默认返回当前时间，但添加提示这是默认值
    return formatDate(new Date()) + ' (系统时间)';
  } catch (error) {
    console.error('时间格式化错误:', error);
    return formatDate(new Date()) + ' (系统时间)';
  }
};

// 获取个人信息
const fetchPersonalInfo = async () => {
  try {
    if (userStore.user?.id) {
      const response = await axios.get('/api/personal/get', {
        headers: {
          'Authorization': localStorage.getItem('authToken'),
          'UserId': userStore.user.id
        },
        withCredentials: true
      });
      if (response.data && response.data.success) {
        const data = response.data.data;
        Object.assign(personalInfo, data);
      } else {
        messageApi.warning(response.data?.message || '获取个人信息失败');
      }
    }
  } catch (error) {
    console.error('获取个人信息错误:', error);
    messageApi.error(error.response?.data?.message || '获取个人信息失败');
  }
};

// 头像上传逻辑
const uploadAvatar = async (event) => {
  const file = event.target.files[0];
  if (!file) return;

  const formData = new FormData();
  formData.append('image', file);
  
  // 如果用户已有头像，添加旧头像路径用于删除
  if (userStore.user?.avatar) {
    formData.append('oldImagePath', userStore.user.avatar);
  }

  try {
    const response = await axios.post('/api/uploadImg', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': localStorage.getItem('authToken')
      }
    });

    if (response.data && response.data.success) {
      // 更新用户头像
      const avatarPath = response.data.data;
      userStore.user.avatar = avatarPath;
      
      // 发送JSON数据
      const userData = {
        id: userStore.user.id,
        avatar: avatarPath
      };
      
      // 调用更新头像接口
      const updateResponse = await axios.post('/api/user/updateAvatar', userData, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': localStorage.getItem('authToken')
        }
      });

      if (updateResponse.data.success) {
        messageApi.success('头像上传成功');
      } else {
        messageApi.error(updateResponse.data?.message || '头像更新失败');
      }
    } else {
      messageApi.error(response.data?.message || '头像上传失败');
    }
  } catch (error) {
    console.error('头像上传错误:', error);
    messageApi.error(error.response?.data?.message || '头像上传失败');
  }
};

// 处理头像点击事件
const handleAvatarClick = () => {
  avatarInput.value?.click();
};

// 生命周期钩子
onMounted(() => {
  fetchUnreadCounts();
  fetchNotificationsByType(0); // 点赞通知
  fetchNotificationsByType(2); // 评论通知
  fetchPersonalInfo(); // 获取个人信息
});

// 格式化消息数量，大于99显示为99+
const formatUnreadCount = (count) => {
  return count > 99 ? '99+' : count;
};
</script>

<template>
  <contextHolder />
  <div class="min-h-screen bg-gray-50 dark:bg-gray-900 py-6">
    <div class="max-w-7xl mx-auto px-4">
      <!-- 顶部用户信息卡片 -->
      <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6 mb-6">
        <div class="flex flex-col md:flex-row items-start md:items-center">
          <div class="flex flex-col md:flex-row items-center mb-4 md:mb-0">
            <input type="file" @change="uploadAvatar" accept="image/*" class="hidden" ref="avatarInput" />
            <div class="relative group">
              <div class="w-[130px] h-[130px] rounded-full overflow-hidden ring-4 ring-blue-100 dark:ring-blue-900 transition-all duration-300 group-hover:ring-blue-200 dark:group-hover:ring-blue-800 transform group-hover:scale-105">
                <img
                    :src="userStore.user?.avatar ? baseURL+userStore.user.avatar: 'https://img95.699pic.com/element/40109/0194.png_300.png'" alt="用户头像"
                    :alt="userStore.user?.username"

                    class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
                />
              </div>
              <div @click="handleAvatarClick"
                  class="hover:brightness-75 absolute -bottom-2 left-1/2 cursor-pointer transform -translate-x-1/2 bg-gradient-to-r from-blue-500 to-purple-600 text-white px-4 py-1 rounded-full text-sm font-medium opacity-0 group-hover:opacity-100 transition-all duration-300 whitespace-nowrap">
                修改头像
              </div>
            </div>
            <div class="md:ml-6 text-center md:text-left">
              <h2 class="text-2xl font-bold text-gray-900 dark:text-white">{{ userStore.user?.username || '用户名' }}</h2>
              <div class="mt-1 flex items-center justify-center md:justify-start">
<!--                类型-->
                <span v-if="userStore.user?.manager === 1" class="bg-red-100 text-red-800 text-xs font-medium px-2.5 py-0.5 rounded-full dark:bg-red-900 dark:text-red-300">
                  管理员
                </span>
                <span v-else class="bg-blue-100 text-blue-800 text-xs font-medium px-2.5 py-0.5 rounded-full dark:bg-blue-900 dark:text-blue-300">
                  学生
                </span>
              </div>
              
              <!-- 个人简介区域 -->
              <div class="mt-3">
                <div class="bg-white dark:bg-gray-800 rounded-lg p-3 shadow-sm transition-all duration-300 hover:shadow-md border border-gray-100 dark:border-gray-700">
                  <div class="flex items-center text-blue-500 dark:text-blue-400 mb-2 border-b border-gray-100 dark:border-gray-700 pb-1">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    <span class="text-sm font-medium">个人简介</span>
                  </div>
                  <div class="bg-gray-50 dark:bg-gray-700/50 rounded p-2">
                    <p class="text-gray-600 dark:text-gray-300 text-sm leading-relaxed">
                      {{ personalInfo.introduction || '这个人很低调，还没有留下简介...' }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="flex-grow"></div>
          
          <!-- 退出登录按钮 -->
          <button @click="showLogoutDialog" class="mt-4 md:mt-0 group flex items-center bg-white dark:bg-gray-700 hover:bg-red-50 dark:hover:bg-red-900/20 px-4 py-2 rounded-lg border border-gray-200 dark:border-gray-600 transition-all shadow-sm hover:shadow">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 mr-2 text-red-500 group-hover:animate-pulse">
              <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 9V5.25A2.25 2.25 0 0013.5 3h-6a2.25 2.25 0 00-2.25 2.25v13.5A2.25 2.25 0 007.5 21h6a2.25 2.25 0 002.25-2.25V15m3 0l3-3m0 0l-3-3m3 3H9" />
            </svg>
            <span class="font-medium text-sm text-red-600 dark:text-red-400">退出登录</span>
          </button>
        </div>
      </div>

      <!-- 主体内容区 -->
      <div class="grid grid-cols-1 md:grid-cols-[220px,1fr] gap-6 min-h-[calc(100vh-220px)]">
        <!-- 左侧导航 -->
        <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6 sticky top-6 h-fit max-h-[calc(100vh-100px)] transition-all duration-300 hover:shadow-lg">
          <h3 class="text-lg font-medium text-gray-900 dark:text-white mb-6 pb-3 border-b border-gray-200 dark:border-gray-700 flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
            个人中心
          </h3>
          <div class="space-y-6">
            <div v-for="sbo in sideBarObjects"
                 :key="sbo.id"
                 @click="buttonClickHandler(sbo.id)"
                 :class="[
                   'flex items-center py-3 px-4 rounded-lg cursor-pointer transition-all duration-200 group relative overflow-hidden',
                   options.selected.notificationType === sbo.id
                     ? 'bg-gradient-to-r from-blue-50 to-blue-100 dark:from-blue-900/30 dark:to-blue-800/20 text-blue-600 dark:text-blue-400 shadow-sm'
                     : 'hover:bg-gray-50 dark:hover:bg-gray-700/50 text-gray-700 dark:text-gray-300'
                 ]">
              <div v-html="sbo.svg" class="w-5 h-5 mr-3 transition-transform duration-200 group-hover:scale-110" 
                   :class="{'text-blue-500 dark:text-blue-400': options.selected.notificationType === sbo.id}">
              </div>
              <span class="font-medium text-sm flex-grow">
                {{ sbo.name }}
              </span>
              <span v-if="(sbo.id === 1 && options.unreadCounts.like > 0) || (sbo.id === 3 && options.unreadCounts.comment > 0)"
                    class="ml-auto px-2 py-0.5 text-xs bg-red-100 dark:bg-red-900/50 text-red-600 dark:text-red-300 rounded-full inline-flex items-center min-w-[1.75rem] justify-center">
                {{ sbo.id === 1 ? formatUnreadCount(options.unreadCounts.like) : formatUnreadCount(options.unreadCounts.comment) }}
              </span>
              <div v-if="options.selected.notificationType === sbo.id" 
                   class="absolute right-0 top-0 h-full w-1 bg-blue-500 dark:bg-blue-400">
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧内容 -->
        <div class="space-y-6 overflow-auto max-h-[calc(100vh-100px)] pr-2 custom-scrollbar">
          <!-- 我的信息 -->
          <div v-if="options.selected.notificationType === 2" class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
            <div class="flex justify-between items-center mb-6">
              <h3 class="text-xl font-semibold text-gray-900 dark:text-white">简介</h3>
              <div><ModifyPi/></div>
            </div>

            <!-- 用户信息表格 -->
            <div class="gap-6">
              <ShowPi/>
            </div>
          </div>

          <!-- 通知列表 -->
          <div v-if="options.selected.notificationType === 1 || options.selected.notificationType === 3" class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
            <div class="flex justify-between items-center mb-4">
              <h3 class="text-xl font-semibold text-gray-900 dark:text-white">
                {{ options.selected.notificationType === 1 ? '点赞通知' : '评论通知' }}
              </h3>
              <button v-if="options.selected.notificationType === 1 "
                  @click="clearNotification(options.selected.notificationType - 1)"
                      class="p-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 transition-all"
                      title="一键清空">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
              </button>
            </div>

            <!-- 话题删除信息 - 仅在评论通知选项下显示 -->
            <div v-if="options.selected.notificationType === 3 && deletedTalkNotifications.length > 0" class="mb-6">
              <h4 class="text-lg font-medium text-red-600 dark:text-red-400 mb-2 flex items-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                </svg>
                话题删除记录
              </h4>
              <div class="space-y-3 max-h-[30vh] overflow-y-auto pr-2 custom-scrollbar border-b border-gray-200 dark:border-gray-700 pb-4">
                <div v-for="notification in deletedTalkNotifications"
                      :key="notification.id"
                      class="py-3 px-4 rounded-md bg-red-50 dark:bg-red-900/10 border border-red-100 dark:border-red-800/30">
                  <div class="flex justify-between items-start">
                    <div class="flex-1">
                      <p class="text-gray-800 dark:text-gray-200">{{ notification.content }}</p>
                      <p class="text-xs text-gray-500 dark:text-gray-400 mt-1">
                        {{ formatNotificationTime(notification.createTime) }}
                      </p>
                    </div>
                  </div>
                </div>
                
                <!-- 无删除通知时显示 -->
                <div v-if="deletedTalkNotifications.length === 0" class="py-4 text-center">
                  <p class="text-gray-500 dark:text-gray-400">暂无话题删除记录</p>
                </div>
              </div>
            </div>

            <div class="space-y-3 max-h-[60vh] overflow-y-auto pr-2 custom-scrollbar">
              <div v-for="notification in options.selected.notificationType === 1 ? options.currentNotifications : commentNotifications"
                   :key="notification.id"
                   class="border-b border-gray-100 dark:border-gray-700 last:border-b-0 py-3 px-4 rounded-md"
                   :class="{ 'bg-blue-50 dark:bg-blue-900/10': !notification.isRead }">
                <div class="flex justify-between items-start">
                  <div class="flex-1">
                    <p class="text-gray-800 dark:text-gray-200">{{ notification.content }}</p>
                    <p class="text-xs text-gray-500 dark:text-gray-400 mt-1">
                      {{ formatNotificationTime(notification.createTime) }}
                    </p>
                  </div>
                  <div class="flex space-x-2 ml-4">
                    <button v-if="!notification.isRead"
                            title="点击已读"
                           @click="markAsRead(notification.notificationId || notification.id); notification.isRead = true"
                           class="p-1.5 text-gray-500 hover:text-blue-600 dark:hover:text-blue-400 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700">
                      <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                      </svg>
                    </button>
                    <button v-if="extractTalkId(notification)"
                            @click="goToTopic(extractTalkId(notification))"
                            title="跳转相关页面"
                            class="p-1.5 text-gray-500 hover:text-blue-600 dark:hover:text-blue-400 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700">
                      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="h-4 w-4">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M15 10.5a3 3 0 1 1-6 0 3 3 0 0 1 6 0Z" />
                        <path stroke-linecap="round" stroke-linejoin="round" d="M19.5 10.5c0 7.142-7.5 11.25-7.5 11.25S4.5 17.642 4.5 10.5a7.5 7.5 0 1 1 15 0Z" />
                      </svg>
                    </button>
                  </div>
                </div>
              </div>
              
              <!-- 无通知时显示 -->
              <div v-if="(options.selected.notificationType === 1) || (options.selected.notificationType === 3 )"
                   class="py-8 text-center">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 mx-auto text-gray-400 mb-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
                </svg>
                <p class="text-gray-500 dark:text-gray-400">已经到底部了~</p>
              </div>
            </div>
          </div>

          <!-- 关于页面 -->
          <div v-if="options.selected.notificationType === 4" class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
            <h3 class="text-xl font-semibold text-gray-900 dark:text-white mb-6">关于学子航标</h3>

            <!-- 平台介绍 -->
            <div class="mb-6">
              <h4 class="text-lg font-medium text-gray-800 dark:text-gray-200 mb-3">平台介绍</h4>
              <p class="text-gray-600 dark:text-gray-400 leading-relaxed">
                学子航标是一个专门面向大学新生的综合性信息平台，致力于帮助新生快速了解和适应大学生活。我们的平台提供以下核心功能：
              </p>
              <ul class="mt-4 space-y-3 text-gray-600 dark:text-gray-400">
                <li class="flex items-start">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500 mr-2 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <span>人才培养与课程规划：提供详细的专业培养方案、课程设置和选课指导</span>
                </li>
                <li class="flex items-start">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500 mr-2 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <span>校园导航：提供教学楼、图书馆、食堂等场所的详细位置信息和导航服务</span>
                </li>
                <li class="flex items-start">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500 mr-2 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <span>校园公告：及时发布学校重要通知、活动信息和新闻动态</span>
                </li>
                <li class="flex items-start">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-blue-500 mr-2 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                  </svg>
                  <span>话题讨论：提供交流平台，让新生能够分享经验、解答疑惑</span>
                </li>
              </ul>
            </div>

            <!-- 联系信息 -->
            <div class="border-t dark:border-gray-700 pt-4 mt-4">
              <h4 class="text-lg font-medium text-gray-800 dark:text-gray-200 mb-3">联系我们</h4>
              <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div class="flex items-center space-x-3 p-3 bg-gray-50 dark:bg-gray-700/50 rounded-lg">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                  </svg>
                  <div>
                    <p class="text-sm text-gray-500 dark:text-gray-400">邮箱</p>
                    <p class="text-gray-700 dark:text-gray-300">123123213</p>
                  </div>
                </div>
                <div class="flex items-center space-x-3 p-3 bg-gray-50 dark:bg-gray-700/50 rounded-lg">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                  </svg>
                  <div>
                    <p class="text-sm text-gray-500 dark:text-gray-400">电话</p>
                    <p class="text-gray-700 dark:text-gray-300">123123132</p>
                  </div>
                </div>
                <div class="flex items-center space-x-3 p-3 bg-gray-50 dark:bg-gray-700/50 rounded-lg">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                  </svg>
                  <div>
                    <p class="text-sm text-gray-500 dark:text-gray-400">QQ群</p>
                    <p class="text-gray-700 dark:text-gray-300">123456789</p>
                  </div>
                </div>
                <div class="flex items-center space-x-3 p-3 bg-gray-50 dark:bg-gray-700/50 rounded-lg">
                  <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-blue-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                  </svg>
                  <div>
                    <p class="text-sm text-gray-500 dark:text-gray-400">位置</p>
                    <p class="text-gray-700 dark:text-gray-300">A6N104</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  
  <!-- 退出登录确认弹窗 -->
  <teleport to="body">
    <div v-if="showLogoutConfirm" 
         @click="cancelLogout"
         class="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center transition-opacity duration-300 animate-fade-in">
      <div @click="stopPropagation"
           class="bg-white dark:bg-gray-800 rounded-xl w-[350px] max-w-[95vw] shadow-2xl
                 border border-gray-200 dark:border-gray-700 
                 transform transition-all duration-300 animate-zoom-in overflow-hidden">
        <!-- 弹窗头部 -->
        <div class="bg-gradient-to-r from-red-50 to-orange-50 dark:from-red-900/30 dark:to-orange-900/30
                   border-b border-gray-200 dark:border-gray-700 p-4
                   flex items-center">
          <div class="bg-red-100 dark:bg-red-900/50 p-2 rounded-full mr-3">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" 
                class="w-6 h-6 text-red-600 dark:text-red-400">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v3.75m-9.303 3.376c-.866 1.5.217 3.374 1.948 3.374h14.71c1.73 0 2.813-1.874 1.948-3.374L13.949 3.378c-.866-1.5-3.032-1.5-3.898 0L2.697 16.126zM12 15.75h.007v.008H12v-.008z" />
            </svg>
          </div>
          <h3 class="text-lg font-bold text-gray-900 dark:text-white">确认退出登录</h3>
        </div>
        
        <!-- 弹窗内容 -->
        <div class="p-5">
          <p class="text-gray-600 dark:text-gray-300 mb-6">
            您确定要退出当前账户吗？退出后需要重新登录。
          </p>
          
          <!-- 按钮组 -->
          <div class="flex justify-end space-x-3">
            <button @click="cancelLogout"
                    class="px-4 py-2 rounded-lg border border-gray-300 dark:border-gray-600
                          text-gray-700 dark:text-gray-300 font-medium
                          hover:bg-gray-100 dark:hover:bg-gray-700
                          transition-colors duration-200">
              取消
            </button>
            <button @click="confirmLogout"
                    class="px-4 py-2 rounded-lg bg-gradient-to-r from-red-500 to-red-600
                          dark:from-red-600 dark:to-red-700
                          text-white font-medium
                          hover:from-red-600 hover:to-red-700
                          dark:hover:from-red-700 dark:hover:to-red-800
                          shadow-sm hover:shadow
                          transition-all duration-200">
              确认退出
            </button>
          </div>
        </div>
      </div>
    </div>
  </teleport>
</template>

<style scoped>
/* 平滑过渡效果 */
.transition-all {
  transition-property: all;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  transition-duration: 300ms;
}

/* 平滑滚动 */
html {
  scroll-behavior: smooth;
}

/* 自定义滚动条 */
.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 3px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

/* 暗黑模式滚动条 */
.dark .custom-scrollbar::-webkit-scrollbar-thumb {
  background: #475569;
}

.dark .custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #64748b;
}

/* 淡入动画 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(8px); }
  to { opacity: 1; transform: translateY(0); }
}

.space-y-3 > *, .space-y-4 > *, .space-y-6 > * {
  animation: fadeIn 0.3s ease-out forwards;
}

/* 弹窗动画 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes zoomIn {
  from { opacity: 0; transform: scale(0.95); }
  to { opacity: 1; transform: scale(1); }
}

.animate-fade-in {
  animation: fadeIn 0.2s ease-out forwards;
}

.animate-zoom-in {
  animation: zoomIn 0.3s ease-out forwards;
}
</style>
