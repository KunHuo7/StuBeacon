<template>
  <div class="bg-white dark:bg-gray-800 rounded-lg shadow-md p-6">
    <h2 class="text-2xl font-bold text-gray-900 dark:text-white mb-6 pb-3 border-b border-gray-200 dark:border-gray-700">
      关于我们
    </h2>
    
    <div class="space-y-6 text-gray-700 dark:text-gray-300">
      <div>
        <h3 class="text-xl font-semibold text-gray-800 dark:text-gray-100 mb-3">学生灯塔 - StuBeacon</h3>
        <p class="mb-4">学生灯塔（StuBeacon）是一个为大学生设计的社交、信息共享和活动参与平台。我们致力于让大学生活更加丰富多彩，帮助同学们更好地分享知识、发现活动、建立联系。</p>
      </div>
      
      <div>
        <h3 class="text-xl font-semibold text-gray-800 dark:text-gray-100 mb-3">我们的愿景</h3>
        <p class="mb-4">成为大学生活中不可或缺的信息灯塔，照亮每个学生的大学旅程，让校园信息更加透明，学生活动更加活跃，社交互动更加便捷。</p>
      </div>
      
      <div>
        <h3 class="text-xl font-semibold text-gray-800 dark:text-gray-100 mb-3">主要功能</h3>
        <ul class="list-disc pl-5 space-y-2">
          <li>活动发布与参与：发布校园活动，轻松报名参加</li>
          <li>帖子分享：分享知识、经验和生活点滴</li>
          <li>社交互动：点赞、评论，与志同道合的同学建立联系</li>
          <li>个人中心：管理个人信息、活动和消息通知</li>
        </ul>
      </div>
      
      <div>
        <h3 class="text-xl font-semibold text-gray-800 dark:text-gray-100 mb-3">联系我们</h3>
        <p>如有任何问题、建议或合作意向，请联系我们：</p>
        <p class="mt-2">
          <span class="font-medium">邮箱：</span>
          <a href="mailto:support@stubeacon.edu" class="text-blue-500 hover:text-blue-700 dark:hover:text-blue-400">
            support@stubeacon.edu
          </a>
        </p>
      </div>
      
      <div class="pt-4 text-sm text-gray-500 dark:text-gray-400 text-center">
        <p>© 2023-{{ new Date().getFullYear() }} StuBeacon. 保留所有权利。</p>
        <p class="mt-1">版本 1.0.0</p>
      </div>
    </div>
  </div>
</template> 