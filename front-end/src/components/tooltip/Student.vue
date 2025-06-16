<script setup>
import { useUserStore } from '@/stores/userStore.js';

const userStore = useUserStore();

const type = '计算机与软件工程学院';
const major = '计算机科学与技术';
const grade = '2021级';
const name = '张三';
const id = '20210001';
</script>

<template>
  <div class="content1">
    <div class="bg-white shadow-lg rounded-lg p-6 dark:shadow-blue-950">
      <div class="rounded-xl overflow-hidden">
        <img :src="userStore.user?.avatar ? `http://localhost:8080/${userStore.user.avatar}` : 'https://img95.699pic.com/element/40109/0194.png_300.png'" alt="学生头像" class="avatar w-20 h-20 rounded-2xl object-cover mx-auto">
      </div>
      <h2 class="mt-4 text-2xl font-bold text-center">个人信息</h2>
      <p class="mt-2 text-gray-700 text-justify text-sm">
        学院：{{ type }}
      </p>
      <p class="mt-2 text-gray-700 text-justify text-sm">
        专业：{{ major }}
      </p>
      <p class="mt-2 text-gray-700 text-justify text-sm">
        班级：{{ grade }}
      </p>
      <p class="mt-2 text-gray-700 text-justify text-sm">
        姓名：{{ name }}
      </p>
      <p class="mt-2 text-gray-700 text-justify text-sm">
        学号：{{ id }}
      </p>
<!--      <p class="mt-2 text-gray-600 text-lg text-center">-->
<!--        2222-->
<!--      </p>-->
    </div>
  </div>
</template>

<style scoped>

</style>