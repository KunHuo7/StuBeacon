<template>

  <div class="all_page">
  <div class="w-full h-[1000px] overflow-hidden shadow-lg dark:shadow-gray-700/30 p-2 flex flex-row relative">
    <!-- 左侧功能区域 -->
    <div class="w-[25%] h-full relative rounded-l-xl ml-20 bg-gradient-to-b from-blue-300 to-indigo-300 dark:from-gray-800 dark:to-gray-900 overflow-hidden">
      <!-- 背景装饰元素 -->
      <div class="absolute inset-0 z-0 overflow-hidden">
        <div class="absolute top-[-50px] right-[-50px] w-[200px] h-[200px] rounded-full bg-white/10 blur-xl"></div>
        <div class="absolute bottom-[-30px] left-[-30px] w-[150px] h-[150px] rounded-full bg-indigo-500/20 dark:bg-indigo-500/10 blur-lg"></div>
        <div class="absolute top-1/3 left-1/4 w-2 h-2 bg-white rounded-full shadow-lg shadow-white/50 dark:shadow-white/30"></div>
        <div class="absolute top-2/3 right-1/3 w-3 h-3 bg-white rounded-full shadow-lg shadow-white/50 dark:shadow-white/30"></div>
        <div class="absolute top-1/2 left-1/2 w-4 h-4 bg-white rounded-full animate-pulse shadow-lg shadow-white/50 dark:shadow-white/30"></div>
      </div>
      
      <!-- 默认内容 - 当没有activeMenu时显示 -->
      <div v-if="!activeMenu" class="relative z-10 flex flex-col items-center justify-center h-full p-6 text-white dark:text-gray-100">
        <div class="w-24 h-24 mb-6 rounded-full bg-white/20 dark:bg-white/10 flex items-center justify-center">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
          </svg>
        </div>
        <h2 class="text-2xl font-bold mb-3 text-center">校园地图导航</h2>
        <p class="text-sm opacity-80 text-center mb-8">使用右侧菜单选择功能，探索校园各个位置</p>
        
        <div class="grid grid-cols-2 gap-4 w-full">
          <div class="bg-white/10 dark:bg-white/5 p-3 rounded-lg backdrop-blur-sm flex flex-col items-center hover:bg-white/20 dark:hover:bg-white/10 transition-all cursor-pointer" @click="handleMenuSelect('locations')">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            <span class="text-xs font-medium">校园位置</span>
          </div>
          <div class="bg-white/10 dark:bg-white/5 p-3 rounded-lg backdrop-blur-sm flex flex-col items-center hover:bg-white/20 dark:hover:bg-white/10 transition-all cursor-pointer" @click="handleMenuSelect('route')">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
            </svg>
            <span class="text-xs font-medium">路线规划</span>
          </div>
          <div class="bg-white/10 dark:bg-white/5 p-3 rounded-lg backdrop-blur-sm flex flex-col items-center hover:bg-white/20 dark:hover:bg-white/10 transition-all cursor-pointer" @click="handleMenuSelect('controls')">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            <span class="text-xs font-medium">地图控件</span>
          </div>
          <div class="bg-white/10 dark:bg-white/5 p-3 rounded-lg backdrop-blur-sm flex flex-col items-center hover:bg-white/20 dark:hover:bg-white/10 transition-all cursor-pointer" @click="geolocation.value && geolocation.value.getCurrentPosition()">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 12a9 9 0 01-9 9m9-9a9 9 0 00-9-9m9 9H3m9 9a9 9 0 01-9-9m9 9c1.657 0 3-4.03 3-9s-1.343-9-3-9m0 18c-1.657 0-3-4.03-3-9s1.343-9 3-9m-9 9a9 9 0 019-9" />
            </svg>
            <span class="text-xs font-medium">我的位置</span>
          </div>
        </div>
      </div>

      <!-- 功能面板 -->
      <div
        v-if="activeMenu"
        class="absolute top-0 right-0 w-full max-h-full bg-white dark:bg-gray-800 rounded-l-xl shadow-lg dark:shadow-gray-900/50 z-[99] transition-all duration-300 ease-in-out overflow-y-auto flex flex-col animate-fadeIn"
      >
        <div class="flex justify-between items-center p-4 border-b border-gray-100 dark:border-gray-700">
          <h3 class="text-lg font-semibold text-gray-800 dark:text-gray-100 m-0">
            {{ activeMenu === 'controls' ? '地图控件' :
               activeMenu === 'locations' ? '校园位置' :
               activeMenu === 'route' ? '路线规划' : '校园地图功能' }}
          </h3>
          <button
            class="w-8 h-8 rounded-full flex items-center justify-center bg-gray-100 dark:bg-gray-700 border-none cursor-pointer transition-all duration-200 ease-in-out hover:bg-gray-200 dark:hover:bg-gray-600"
            @click="closeActiveMenu">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-500 dark:text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <div class="p-3">
          <!-- 地图控件面板 -->
          <div v-if="activeMenu === 'controls'" class="animate-fadeIn">
            <MapControls
              :scaleControl="scaleControl"
              :toolBarControl="toolBarControl"
              :controlBarControl="controlBarControl"
              :overViewControl="overViewControl"
              v-model:showScale="showScale"
              v-model:showToolBar="showToolBar"
              v-model:showControlBar="showControlBar"
              v-model:showOverView="showOverView"
            />
          </div>

          <!-- 校园位置面板 -->
          <div v-if="activeMenu === 'locations'" class="animate-fadeIn">
            <SearchAddress
              :map="map"
              :customLocations="customLocations"
              @panel-toggle="handlePanelToggle"
              @select-location="handleSelectLocation"
            />
          </div>

          <!-- 路线规划面板 -->
          <div v-if="activeMenu === 'route'" class="animate-fadeIn">
            <RoutePlanner
              :map="map"
              :driving="driving"
              :geolocation="geolocation"
              @panel-toggle="handlePanelToggle"
              @marker-clear="clearMarker"
            />
          </div>
        </div>
      </div>
    </div>
    <!-- 地图容器 -->
    <div id="container" class="map_style w-4/5 h-full transition-all duration-300 ease-in-out rounded-r-xl shadow-inner dark:shadow-gray-900/30" :class="{ 'w-full': !showSidebar }"></div>
    
    <!-- 侧边菜单栏 -->
    <div class="h-full transition-all duration-300 ease-in-out" :class="{ 'w-1/5': showSidebar, 'w-0': !showSidebar }">
      <ModernMenu @menu-select="handleMenuSelect" v-if="showSidebar" />
    </div>



    <!-- 菜单栏 - 移动端触发按钮 -->
    <div class="fixed bottom-6 right-6 md:hidden z-50">
      <button
        @click="toggleMenu"
        class="w-14 h-14 rounded-full bg-gradient-to-r from-blue-600 to-indigo-700 dark:from-gray-700 dark:to-gray-900 flex items-center justify-center shadow-lg dark:shadow-gray-900/50 hover:shadow-xl transition-all duration-300 ease-in-out transform hover:scale-105 active:scale-95"
      >
        <svg xmlns="http://www.w3.org/2000/svg" class="h-7 w-7 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16m-7 6h7" />
        </svg>
      </button>
    </div>
  </div>
</div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed, watch } from "vue";
import AMapLoader from "@amap/amap-jsapi-loader";
import MapControls from "./MapControls.vue";
import SearchAddress from "./SearchAddress.vue";
import RoutePlanner from "./RoutePlanner.vue";
import ModernMenu from "@/components/zuJian/ModernMenu.vue";
import { useThemeStore } from "@/stores/theme";
const themeStore = useThemeStore();

const map = ref(null);
const scaleControl = ref(null);
const toolBarControl = ref(null);
const controlBarControl = ref(null);
const overViewControl = ref(null);

// 搜索和路线规划相关变量
const placeSearch = ref(null);
const driving = ref(null);
const geolocation = ref(null);
const marker = ref(null);
const infoWindow = ref(null);

// 响应式控制状态
const showScale = ref(false);
const showToolBar = ref(false);
const showControlBar = ref(false);
const showOverView = ref(true);

// 自定义位置数据
const customLocations = ref([
  { 
    name: "安徽信息工程学院", 
    location: [118.631987, 31.136777],
    address: "安徽省芜湖市弋江区高教园区",
    description: "安徽信息工程学院是一所民办普通本科高校", 
    image: "https://via.placeholder.com/150x100/4a90e2/ffffff?text=校园图片" 
  },
  { 
    name: "图书馆", 
    location: [118.633, 31.137],
    address: "校园中心区域", 
    description: "藏书丰富的现代化图书馆", 
    image: "https://via.placeholder.com/150x100/27ae60/ffffff?text=图书馆"
  },
  { 
    name: "学生宿舍", 
    location: [118.630, 31.135],
    address: "校园生活区", 
    description: "学生住宿区域，设施完善", 
    image: "https://via.placeholder.com/150x100/8e44ad/ffffff?text=学生宿舍" 
  },
  { 
    name: "食堂", 
    location: [118.632, 31.138],
    address: "校园生活服务区", 
    description: "提供多样化的餐饮选择", 
    image: "https://via.placeholder.com/150x100/e67e22/ffffff?text=食堂" 
  },
  { 
    name: "体育馆", 
    location: [118.634, 31.136],
    address: "校园体育中心", 
    description: "室内综合性体育场馆", 
    image: "https://via.placeholder.com/150x100/e74c3c/ffffff?text=体育馆" 
  },
]);

// 菜单控制变量
const activeMenu = ref(null);
const showMenu = ref(false);
const showSidebar = ref(true);

// 面板显示控制
const activePanel = ref(null);

// 使用计算属性动态获取地图样式
const mapStyle = computed(() => {
  return themeStore.isDark ? 'amap://styles/dark' : 'amap://styles/normal';
});

// 监听主题变化，重新设置地图样式
watch(() => themeStore.isDark, (newVal) => {
  if (map.value) {
    map.value.setMapStyle(newVal ? 'amap://styles/dark' : 'amap://styles/normal');
  }
});

onMounted(() => {
  window._AMapSecurityConfig = {
    securityJsCode: "29625d6fc84ae1d3214b3cf6996a8e7d",
  };

  AMapLoader.load({
    key: "950245064fc7eeaf946b07515b79d37f",
    version: "2.0",
    plugins: [
      "AMap.Scale",
      "AMap.ToolBar",
      "AMap.ControlBar",
      "AMap.HawkEye",
      "AMap.PlaceSearch",
      "AMap.Driving",
      "AMap.Geolocation",
      "AMap.InfoWindow"
    ],
  }).then((AMap) => {

    // 初始化地图
    map.value = new AMap.Map("container", {
      viewMode: "3D",
      zoom: 17.5,
      center: [118.632521,31.136482],
      mapStyle: mapStyle.value, // 使用计算属性获取当前主题对应的地图样式
      features: ['bg', 'road', 'building', 'point'],
    });
    
    // 添加地图加载完成事件
    map.value.on('complete', function() {
      console.log('地图加载完成');
    });
    
    // 初始化控件
    scaleControl.value = new AMap.Scale({
      visible: showScale.value
    });
    toolBarControl.value = new AMap.ToolBar({
      visible: showToolBar.value,
      position: { top: "110px", right: "40px" }
    });
    controlBarControl.value = new AMap.ControlBar({
      visible: showControlBar.value,
      position: { top: "10px", right: "10px" }
    });
    overViewControl.value = new AMap.HawkEye({
      visible: showOverView.value
    });

    // 添加控件到地图
    map.value.addControl(scaleControl.value);
    map.value.addControl(toolBarControl.value);
    map.value.addControl(controlBarControl.value);
    map.value.addControl(overViewControl.value);
    
    // 初始化定位插件
    geolocation.value = new AMap.Geolocation({
      enableHighAccuracy: true, // 是否使用高精度定位，默认:true
      timeout: 10000, // 超过10秒后停止定位，默认：无穷大
      maximumAge: 0, // 定位结果缓存0毫秒，默认：0
      convert: true, // 自动偏移坐标，偏移后的坐标为高德坐标，默认：true
      showButton: false, // 显示定位按钮，默认：true
      buttonPosition: 'LB', // 定位按钮停靠位置，默认：'LB'，左下角
      buttonOffset: new AMap.Pixel(10, 20), // 定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
      showMarker: true, // 定位成功后在定位到的位置显示点标记，默认：true
      showCircle: true, // 定位成功后用圆圈表示定位精度范围，默认：true
      panToLocation: true, // 定位成功后将定位到的位置作为地图中心点，默认：true
      zoomToAccuracy: true // 定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
    });
    
    map.value.addControl(geolocation.value);

    // 初始化搜索和路线规划功能
    placeSearch.value = new AMap.PlaceSearch({
      city: '芜湖',
      pageSize: 5,
      pageIndex: 1
    });
    
    // 初始化驾车规划服务
    driving.value = new AMap.Driving({
      map: map.value,
      panel: false,
      policy: AMap.DrivingPolicy.LEAST_TIME // 最快捷模式
    });

    driving.value = new AMap.Driving({
      policy: AMap.DrivingPolicy.LEAST_TIME,
      map: map.value
    });

    geolocation.value = new AMap.Geolocation({
      enableHighAccuracy: true,
      timeout: 10000,
      zoomToAccuracy: true
    });

    map.value.addControl(geolocation.value);

    // 初始化信息窗口
    infoWindow.value = new AMap.InfoWindow({
      offset: new AMap.Pixel(0, -30),
      closeWhenClickMap: true
    });
  });
});


// 处理菜单切换
const toggleMenu = () => {
  showMenu.value = !showMenu.value;
  if (!showMenu.value) {
    activeMenu.value = null;
  }
};

// 关闭当前活动菜单
const closeActiveMenu = () => {
  activeMenu.value = null;
};

// 处理菜单项选择
const handleMenuSelect = (menuType) => {
  if (activeMenu.value === menuType) {
    activeMenu.value = null;
  } else {
    activeMenu.value = menuType;
    if (window.innerWidth < 768) {
      showMenu.value = false;
    }
  }
};

// 处理面板切换，确保只有一个面板显示
const handlePanelToggle = (panelType, isVisible) => {
  if (isVisible) {
    activePanel.value = panelType;
  } else if (activePanel.value === panelType) {
    activePanel.value = null;
  }
};

// 处理位置选择
const handleSelectLocation = (location) => {
  // 清除之前的标记
  if (marker.value) {
    map.value.remove(marker.value);
  }

  // 创建自定义标记样式，根据当前主题调整颜色
  const markerContent = `
    <div class="custom-marker">
      <div class="marker-pin" style="background-color: ${themeStore.isDark ? 'rgba(139, 92, 246, 0.9)' : 'rgba(65, 105, 225, 0.9)'}; width: 30px; height: 30px; border-radius: 50% 50% 50% 0; transform: rotate(-45deg); box-shadow: ${themeStore.isDark ? '0 0 10px rgba(139, 92, 246, 0.4)' : '0 0 10px rgba(0, 0, 0, 0.3)'}; position: absolute; left: 0; top: 0;"></div>
      <div class="marker-icon" style="width: 20px; height: 20px; background-color: ${themeStore.isDark ? '#1F2937' : 'white'}; border-radius: 50%; display: flex; justify-content: center; align-items: center; position: absolute; left: 5px; top: 5px; transform: rotate(45deg);">
        <span style="transform: rotate(-45deg); font-weight: bold; color: ${themeStore.isDark ? '#A78BFA' : '#4169e1'}; font-size: 14px;">${location.name.charAt(0)}</span>
      </div>
    </div>
  `;

  // 添加新标记并移动地图
  marker.value = new AMap.Marker({
    position: location.location,
    title: location.name,
    content: markerContent, // 使用自定义HTML作为标记内容
    anchor: 'bottom-center',
    zIndex: 100,
    offset: new AMap.Pixel(0, -15),
    animation: 'AMAP_ANIMATION_DROP' // 添加掉落动画
  });

  map.value.add(marker.value);
  map.value.setCenter(location.location);
  map.value.setZoom(18); // 设置更近的缩放级别

  // 构建信息窗口内容，添加data-theme属性以便CSS选择器
  const infoWindowContent = `
    <div class="info-window" data-theme="${themeStore.isDark ? 'dark' : 'light'}" style="min-width: 220px; font-family: Arial, sans-serif;">
      <div class="info-header" style="background: ${themeStore.isDark ? 'linear-gradient(to right, #4B5563, #6B7280)' : 'linear-gradient(to right, #4169e1, #87CEFA)'}; color: ${themeStore.isDark ? '#E5E7EB' : 'white'}; padding: 8px 12px; border-radius: 8px 8px 0 0; display: flex; align-items: center; justify-content: space-between;">
        <h3 style="margin: 0; font-size: 16px;">${location.name}</h3>
      </div>
      ${location.image ? `<div class="info-image" style="height: 100px; overflow: hidden;">
        <img src="${location.image}" style="width: 100%; height: 100%; object-fit: cover; border-bottom: 1px solid ${themeStore.isDark ? '#4B5563' : '#eee'};">
      </div>` : ''}
      <div class="info-body" style="padding: 10px; background-color: ${themeStore.isDark ? '#1F2937' : 'white'}; color: ${themeStore.isDark ? '#E5E7EB' : '#333'}; border-radius: 0 0 8px 8px;">
        ${location.address ? `<div style="margin-bottom: 5px; display: flex; align-items: center;">
          <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="${themeStore.isDark ? '#A78BFA' : '#4169e1'}" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="margin-right: 5px;"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path><circle cx="12" cy="10" r="3"></circle></svg>
          <span style="font-size: 12px; color: ${themeStore.isDark ? '#9CA3AF' : '#666'};">${location.address}</span>
        </div>` : ''}
        ${location.description ? `<div style="font-size: 13px; color: ${themeStore.isDark ? '#D1D5DB' : '#333'}; margin-top: 5px;">${location.description}</div>` : ''}
      </div>
    </div>
  `;

  // 设置信息窗口内容并打开
  infoWindow.value.setContent(infoWindowContent);
  infoWindow.value.open(map.value, location.location);

  // 添加点击事件，点击标记时打开信息窗口
  marker.value.on('click', () => {
    // 重新生成内容确保主题状态是最新的
    const updatedInfoWindowContent = `
      <div class="info-window" data-theme="${themeStore.isDark ? 'dark' : 'light'}" style="min-width: 220px; font-family: Arial, sans-serif;">
        <div class="info-header" style="background: ${themeStore.isDark ? 'linear-gradient(to right, #4B5563, #6B7280)' : 'linear-gradient(to right, #4169e1, #87CEFA)'}; color: ${themeStore.isDark ? '#E5E7EB' : 'white'}; padding: 8px 12px; border-radius: 8px 8px 0 0; display: flex; align-items: center; justify-content: space-between;">
          <h3 style="margin: 0; font-size: 16px;">${location.name}</h3>
        </div>
        ${location.image ? `<div class="info-image" style="height: 100px; overflow: hidden;">
          <img src="${location.image}" style="width: 100%; height: 100%; object-fit: cover; border-bottom: 1px solid ${themeStore.isDark ? '#4B5563' : '#eee'};">
        </div>` : ''}
        <div class="info-body" style="padding: 10px; background-color: ${themeStore.isDark ? '#1F2937' : 'white'}; color: ${themeStore.isDark ? '#E5E7EB' : '#333'}; border-radius: 0 0 8px 8px;">
          ${location.address ? `<div style="margin-bottom: 5px; display: flex; align-items: center;">
            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="${themeStore.isDark ? '#A78BFA' : '#4169e1'}" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="margin-right: 5px;"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path><circle cx="12" cy="10" r="3"></circle></svg>
            <span style="font-size: 12px; color: ${themeStore.isDark ? '#9CA3AF' : '#666'};">${location.address}</span>
          </div>` : ''}
          ${location.description ? `<div style="font-size: 13px; color: ${themeStore.isDark ? '#D1D5DB' : '#333'}; margin-top: 5px;">${location.description}</div>` : ''}
        </div>
      </div>
    `;
    infoWindow.value.setContent(updatedInfoWindowContent);
    infoWindow.value.open(map.value, location.location);
  });
};

// 清除标记
const clearMarker = () => {
  if (marker.value) {
    map.value.remove(marker.value);
    marker.value = null;
  }
};

onUnmounted(() => {
  if (map.value) {
    map.value.destroy();
    map.value = null;
  }
});
</script>

<style scoped>
.map_style {
  border-radius: 0.75rem;
  transition: all 0.3s ease;
  box-shadow: inset 0 2px 4px 0 rgba(0, 0, 0, 0.06);
}

.dark .map_style {
  box-shadow: inset 0 2px 4px 0 rgba(0, 0, 0, 0.25);
}

/* 地图容器悬停效果 */
.map_style:hover {
  box-shadow: inset 0 4px 6px 0 rgba(0, 0, 0, 0.1);
}

.dark .map_style:hover {
  box-shadow: inset 0 4px 6px 0 rgba(0, 0, 0, 0.3);
}

/* 暗色模式下的信息窗口样式优化 */
:deep(.amap-info-content) {
  @apply dark:bg-gray-800 dark:text-white dark:border-gray-700 rounded-lg overflow-hidden;
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
}

.dark :deep(.amap-info-content) {
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.3), 0 4px 6px -2px rgba(0, 0, 0, 0.2);
}

:deep(.amap-info-close) {
  @apply dark:text-gray-300 dark:hover:text-white;
  top: 8px;
  right: 8px;
  color: rgba(255, 255, 255, 0.8);
  background-color: transparent;
}

:deep(.amap-info-sharp) {
  @apply dark:border-t-gray-800;
  width: 12px;
  height: 12px;
}

/* 暗色模式下的控件样式 */
:deep(.amap-control-scale) {
  @apply dark:bg-gray-800/70 dark:text-white border dark:border-gray-700 rounded;
  padding: 2px 5px;
}

:deep(.amap-toolbar) {
  @apply dark:bg-gray-800 dark:border-gray-700 rounded-md overflow-hidden;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.dark :deep(.amap-toolbar) {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.2), 0 2px 4px -1px rgba(0, 0, 0, 0.1);
}

:deep(.amap-toolbar-item) {
  @apply dark:text-gray-300 dark:hover:bg-gray-700 transition-colors duration-200;
}

:deep(.amap-controlbar) {
  @apply dark:bg-gray-800/90 dark:border-gray-700 rounded-md overflow-hidden;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
}

.dark :deep(.amap-controlbar) {
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.2), 0 2px 4px -1px rgba(0, 0, 0, 0.1);
}

:deep(.amap-controlbar button) {
  @apply dark:text-gray-300 dark:hover:bg-gray-700 transition-colors duration-200;
}

/* 鹰眼控件样式 */
:deep(.amap-overview-main) {
  @apply dark:border-gray-700;
}

:deep(.amap-overview-open-button) {
  @apply dark:bg-gray-700 dark:text-gray-300 dark:hover:bg-gray-600;
}

:deep(.amap-overview-close-button) {
  @apply dark:bg-gray-700 dark:text-gray-300 dark:hover:bg-gray-600;
}

/* 位置标记样式 */
:deep(.amap-marker-label) {
  @apply dark:bg-gray-800 dark:text-gray-200 dark:border-gray-700 rounded px-2 py-1;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.dark :deep(.amap-marker-label) {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

/* 自定义动画 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.animate-fadeIn {
  animation: fadeIn 0.3s ease-out;
}

/* 标记动画 */
@keyframes pulseMarker {
  0% { transform: scale(1); }
  50% { transform: scale(1.1); }
  100% { transform: scale(1); }
}

:deep(.custom-marker) {
  animation: pulseMarker 2s infinite;
}
</style>