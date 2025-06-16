<script  setup>
import {onMounted, reactive} from "vue";
import {get} from "@/net/index.js";
import router from "@/router/index.js";

import { ref } from 'vue'

const visible = ref(false)

const options = reactive({
  blogs: []
})
const initData = () => {
  get('/api/blog/getInroduce', {}, (message, data) => {
    options.blogs = data
  })
}

onMounted(() => {
  initData()
})



</script>
<template>
  <div class="grid grid-cols-[1fr,5fr,1fr]">
    <div>

    </div>

    <div>
      <div class="mx-auto  rounded-xl p-4 clickEffect1 border-2 mt-2" v-for="blog in options.blogs" @click="router.push('/view/'+blog.blog.id)">
        <h1 class="text-[#265ca1]">{{ blog.blog.title }}</h1>
        <div class="ddd grid grid-cols-[1fr,14fr]">
          <a-avatar shape="square" class="w-full h-auto"
                    :src="blog.avatar"/>
          <div > <h2 class="text-sm">{{ blog.blog.content }}</h2></div>
        </div>
        <h3 class="text-blue-500">{{ blog.username }}</h3>
      </div>
    </div>

    <div>

    </div>
  </div>
</template>

<style scoped>

</style>