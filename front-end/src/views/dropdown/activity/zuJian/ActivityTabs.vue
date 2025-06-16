<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  activeTab: {
    type: String,
    required: true
  },
  isLoggedIn: {
    type: Boolean,
    default: false
  },
  isManager: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['tabChange', 'publishClick']);

const changeTab = (tab) => {
  emit('tabChange', tab);
};

const handlePublishClick = () => {
  emit('publishClick');
};
</script>

<template>
  <div class="flex justify-between items-center mb-8">
    <div class="tabs flex space-x-4">
      <button 
        @click="changeTab('all')" 
        :class="{'tab-active': activeTab === 'all'}"
        class="tab-btn"
      >
        全部活动
      </button>
      <button 
        v-if="isLoggedIn"
        @click="changeTab('my')" 
        :class="{'tab-active': activeTab === 'my'}"
        class="tab-btn"
      >
        我发布的
      </button>
      <button 
        v-if="isLoggedIn"
        @click="changeTab('joined')" 
        :class="{'tab-active': activeTab === 'joined'}"
        class="tab-btn"
      >
        我参加的
      </button>
      <button 
        v-if="isManager"
        @click="changeTab('pending')" 
        :class="{'tab-active': activeTab === 'pending'}"
        class="tab-btn manager-tab"
      >
        待审核
      </button>
    </div>
    
    <button 
      v-if="isLoggedIn"
      @click="handlePublishClick()" 
      class="publish-btn"
    >
      {{ activeTab === 'publish' ? '取消发布' : '发布新活动' }}
    </button>
  </div>
</template>

<style scoped>
.tab-btn {
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.3s ease;
  @apply bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-300;
}

.tab-active {
  @apply bg-blue-500 text-white;
}

.manager-tab {
  @apply border border-yellow-400;
}

.manager-tab.tab-active {
  @apply bg-yellow-500;
}

.publish-btn {
  padding: 0.5rem 1.5rem;
  border-radius: 0.5rem;
  font-weight: 500;
  transition: all 0.3s ease;
  @apply bg-gradient-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 text-white;
}
</style>