<template>
  <div class="w-full min-h-screen p-4">
    <div class="w-[85%] max-w-6xl mx-auto my-8 flex flex-col gap-6">
      <!-- 第一行 - 1个卡片 -->
      <div class="relative h-44 overflow-hidden">
        <div class="flex justify-center w-full h-full gap-4">
          <div 
            class="w-1/2 flex flex-col justify-start items-stretch h-[90%] p-6 bg-white rounded-lg cursor-pointer shadow-md transition-all duration-600 ease-in-out hover:bg-gray-50"
            :class="[
              selectedOption === '1' 
                ? 'opacity-100 flex-[1.2] bg-gray-50 shadow-lg' 
                : 'opacity-50 flex-[0.8]'
            ]"
            @click="selectedOption = '1'"
          >
            <div class="flex flex-col h-full gap-3">
              <h1 class="text-xl font-bold text-gray-800">标题1</h1>
              <p class="text-base text-gray-600 leading-relaxed flex-grow">内容1</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 第二行 - 2个卡片 -->
      <div class="relative h-44 overflow-hidden">
        <div class="flex justify-between w-full h-full gap-4">
          <div 
            v-for="(item, index) in [{id: '2', title: '标题2', content: '内容2'}, 
                                   {id: '3', title: '标题3', content: '内容3'}]"
            :key="item.id"
            class="flex-1 flex flex-col justify-start items-stretch h-[90%] p-6 bg-white rounded-lg cursor-pointer shadow-md transition-all duration-600 ease-in-out hover:bg-gray-50"
            :class="[
              selectedOption === item.id 
                ? 'opacity-100 flex-[1.2] bg-gray-50 shadow-lg' 
                : 'opacity-50 flex-[0.8]'
            ]"
            @click="selectedOption = item.id"
          >
            <div class="flex flex-col h-full gap-3">
              <h1 class="text-xl font-bold text-gray-800">{{ item.title }}</h1>
              <p class="text-base text-gray-600 leading-relaxed flex-grow">{{ item.content }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 第三行 - 3个卡片 -->
      <div class="relative h-44 overflow-hidden">
        <div class="flex justify-between w-full h-full gap-4">
          <div 
            v-for="(item, index) in [{id: '4', title: '标题4', content: '内容4'}, 
                                   {id: '5', title: '标题5', content: '内容5'},
                                   {id: '6', title: '标题6', content: '内容6'}]"
            :key="item.id"
            class="flex-1 flex flex-col justify-start items-stretch h-[90%] p-6 bg-white rounded-lg cursor-pointer shadow-md transition-all duration-600 ease-in-out hover:bg-gray-50"
            :class="[
              selectedOption === item.id 
                ? 'opacity-100 flex-[1.2] bg-gray-50 shadow-lg' 
                : 'opacity-50 flex-[0.8]'
            ]"
            @click="selectedOption = item.id"
          >
            <div class="flex flex-col h-full gap-3">
              <h1 class="text-xl font-bold text-gray-800">{{ item.title }}</h1>
              <p class="text-base text-gray-600 leading-relaxed flex-grow">{{ item.content }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 第四行 - 2个卡片 -->
      <div class="relative h-44 overflow-hidden">
        <div class="flex justify-between w-full h-full gap-4">
          <div 
            v-for="(item, index) in [{id: '7', title: '标题7', content: '内容7'}, 
                                   {id: '8', title: '标题8', content: '内容8'}]"
            :key="item.id"
            class="flex-1 flex flex-col justify-start items-stretch h-[90%] p-6 bg-white rounded-lg cursor-pointer shadow-md transition-all duration-600 ease-in-out hover:bg-gray-50"
            :class="[
              selectedOption === item.id 
                ? 'opacity-100 flex-[1.2] bg-gray-50 shadow-lg' 
                : 'opacity-50 flex-[0.8]'
            ]"
            @click="selectedOption = item.id"
          >
            <div class="flex flex-col h-full gap-3">
              <h1 class="text-xl font-bold text-gray-800">{{ item.title }}</h1>
              <p class="text-base text-gray-600 leading-relaxed flex-grow">{{ item.content }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 第五行 - 3个卡片 -->
      <div class="relative h-44 overflow-hidden">
        <div class="flex justify-between w-full h-full gap-4">
          <div 
            v-for="(item, index) in [{id: '9', title: '标题9', content: '内容9'}, 
                                   {id: '10', title: '标题10', content: '内容10'},
                                   {id: '11', title: '标题11', content: '内容11'}]"
            :key="item.id"
            class="flex-1 flex flex-col justify-start items-stretch h-[90%] p-6 bg-white rounded-lg cursor-pointer shadow-md transition-all duration-600 ease-in-out hover:bg-gray-50"
            :class="[
              selectedOption === item.id 
                ? 'opacity-100 flex-[1.2] bg-gray-50 shadow-lg' 
                : 'opacity-50 flex-[0.8]'
            ]"
            @click="selectedOption = item.id"
          >
            <div class="flex flex-col h-full gap-3">
              <h1 class="text-xl font-bold text-gray-800">{{ item.title }}</h1>
              <p class="text-base text-gray-600 leading-relaxed flex-grow">{{ item.content }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const selectedOption = ref('1');
</script>