<script setup>
import { defineProps, defineEmits } from 'vue';
import ActivityCard from './ActivityCard.vue';

const props = defineProps({
  activities: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  emptyText: {
    type: String,
    default: '暂无活动'
  },
  showStatus: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['viewActivity']);

const handleCardClick = (id) => {
  emit('viewActivity', id);
};
</script>

<template>
  <div>
    <div v-if="loading" class="flex justify-center items-center py-12">
      <div class="loader"></div>
    </div>
    
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <ActivityCard 
        v-for="activity in activities" 
        :key="activity.id"
        :activity="activity"
        :showStatus="showStatus"
        @card-click="handleCardClick"
      />
      
      <div v-if="activities.length === 0" class="col-span-full text-center py-12">
        <p class="text-gray-500 dark:text-gray-400">{{ emptyText }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.loader {
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-left-color: #3b82f6;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style> 