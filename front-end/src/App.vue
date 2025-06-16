<script setup>
import { RouterLink, RouterView } from 'vue-router'
import {useThemeStore} from "@/stores/theme.js";
// import {startSakura} from "@/fullScreenFlower.js";
const themeStore=useThemeStore();
themeStore.initTheme()
</script>

<template>
<!--  <RouterView :class="startSakura"/>-->
  <div class="basic-color">
    <RouterView/>
  </div>

</template>
