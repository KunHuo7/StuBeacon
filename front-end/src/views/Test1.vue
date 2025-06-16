<script setup>
import Send1 from "@/components/zuJian/button/Send1.vue";
</script>

<template>
  <!-- 基本用法 -->
  <h3>基本用法</h3>
  <Send1 />
  
  <!-- 自定义文本 -->
  <h3>自定义文本</h3>
  <Send1 
    defaultText="开启旅程"
    sentText="出发！"
    primaryColor="#FF6B6B"
  />
  
  <!-- 自定义图标和文本 -->
  <h3>自定义图标和文本</h3>
  <Send1 
    defaultText="提交表单" 
    sentText="提交成功" 
    primaryColor="#4CAF50"
    buttonWidth="240px"
  >
    <!-- 自定义默认图标 -->
    <template #default-icon>
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
        <path fill="currentColor" d="M21,14V15C21,16.91 19.93,18.57 18.35,19.41L19,22H17L16.5,20C16.33,20 16.17,20 16,19.97L15.5,22H13.5L14.15,19.41C12.57,18.57 11.5,16.91 11.5,15V14H21M19.5,13.5H13V12.5A3.35,3.35 0 0,0 16.35,9.15A3.35,3.35 0 0,0 13,5.85V4.92C13,4.46 13.23,4 13.5,3.75C13.5,3.75 14.43,3 16.5,3C18.57,3 19.5,3.75 19.5,3.75C19.77,4 20,4.46 20,4.92V5.85A3.35,3.35 0 0,0 16.65,9.15A3.35,3.35 0 0,0 20,12.5V13.5H19.5M3,14H10.5V15A5.5,5.5 0 0,0 16,20.5V21H13.5V19.67C13,19.34 12.5,19 12.11,18.5H10.5V18.5C9,18.5 7.8,17.69 7.16,16.5H3V14Z" />
      </svg>
    </template>
    
    <!-- 自定义发送后图标 -->
    <template #sent-icon>
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
        <path fill="currentColor" d="M12,2A10,10 0 0,1 22,12A10,10 0 0,1 12,22A10,10 0 0,1 2,12A10,10 0 0,1 12,2M12,4A8,8 0 0,0 4,12A8,8 0 0,0 12,20A8,8 0 0,0 20,12A8,8 0 0,0 12,4M11,16.5L6.5,12L7.91,10.59L11,13.67L16.59,8.09L18,9.5L11,16.5Z" />
      </svg>
    </template>
  </Send1>
  
  <!-- 完全自定义样式 -->
  <h3>完全自定义样式</h3>
  <Send1 
    defaultText="下载文件" 
    sentText="下载中..." 
    primaryColor="#2196F3"
    buttonWidth="200px"
    buttonHeight="60px"
    borderRadius="8px"
    fontSize="16px"
  >
    <template #default-icon>
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
        <path fill="currentColor" d="M5,20H19V18H5M19,9H15V3H9V9H5L12,16L19,9Z" />
      </svg>
    </template>
    
    <template #sent-icon>
      <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
        <path fill="currentColor" d="M13,2.03C17.73,2.5 21.5,6.25 21.95,11C22.5,16.5 18.5,21.38 13,21.93V19.93C16.64,19.5 19.5,16.61 19.96,12.97C20.5,8.58 17.39,4.59 13,4.05V2.05L13,2.03M11,2.06V4.06C9.57,4.26 8.22,4.84 7.1,5.74L5.67,4.26C7.19,3 9.05,2.25 11,2.06M4.26,5.67L5.69,7.1C4.8,8.23 4.24,9.58 4.05,11H2.05C2.25,9.04 3,7.19 4.26,5.67M2.06,13H4.06C4.24,14.42 4.81,15.77 5.69,16.9L4.27,18.33C3.03,16.81 2.26,14.96 2.06,13M7.1,18.37C8.23,19.25 9.58,19.82 11,20V22C9.04,21.79 7.18,21 5.67,19.74L7.1,18.37M12,16.5L7.5,12H11V8H13V12H16.5L12,16.5Z" />
      </svg>
    </template>
  </Send1>
  
  <!-- 自定义宽高设置 -->
  <h3>自定义宽高设置</h3>
  
  <!-- 固定宽高 -->
  <div class="button-row">
    <Send1 
      defaultText="固定宽高" 
      sentText="已设置" 
      primaryColor="#E91E63"
      buttonWidth="180px"
      buttonHeight="55px"
    />
  
    <!-- 最小宽度限制 -->
    <Send1 
      defaultText="最小宽度" 
      sentText="已设置" 
      primaryColor="#673AB7"
      minWidth="150px"
      buttonWidth="auto"
      :fitContent="true"
    />
  
    <!-- 最大宽度限制 -->
    <Send1 
      defaultText="最大宽度限制示例文本" 
      sentText="已设置" 
      primaryColor="#3F51B5"
      maxWidth="200px"
      :fitContent="true"
    />
  </div>
  
  <!-- 高度设置 -->
  <div class="button-row">
    <!-- 较小高度 -->
    <Send1 
      defaultText="较小高度" 
      sentText="已设置" 
      primaryColor="#00BCD4"
      buttonHeight="45px"
    />
    
    <!-- 较大高度 -->
    <Send1 
      defaultText="较大高度" 
      sentText="已设置" 
      primaryColor="#FFC107"
      buttonHeight="85px"
    />
    
    <!-- 最小最大高度限制 -->
    <Send1 
      defaultText="高度范围限制" 
      sentText="已设置" 
      primaryColor="#8BC34A"
      minHeight="60px"
      maxHeight="90px"
    />
  </div>
  
  <!-- 自适应内容宽度 -->
  <div class="button-row">
    <Send1 
      defaultText="短文本" 
      sentText="OK" 
      primaryColor="#FF5722"
      :fitContent="true"
    />
    
    <Send1 
      defaultText="较长的自适应内容文本示例" 
      sentText="成功" 
      primaryColor="#9E9E9E"
      :fitContent="true"
    />
    
    <Send1 
      defaultText="自定义内边距" 
      sentText="完成" 
      primaryColor="#607D8B"
      :fitContent="true"
      style="padding-left: 50px; padding-right: 50px;"
    />
  </div>
  
  <!-- 页面跳转功能 -->
  <h3>页面跳转功能</h3>
  
  <!-- 内部路由跳转 -->
  <Send1 
    defaultText="跳转到首页" 
    sentText="正在跳转..." 
    primaryColor="#9C27B0"
    :to="'/'"
  />
  
  <!-- 外部链接跳转 -->
  <Send1 
    defaultText="前往百度" 
    sentText="正在打开..." 
    primaryColor="#FF9800"
    to="https://www.baidu.com"
    :external="true"
  />
  
  <!-- 使用对象形式的路由配置 -->
  <Send1 
    defaultText="跳转并传参" 
    sentText="跳转中..." 
    primaryColor="#009688"
    :to="{ path: '/other', query: { id: 123 } }"
  />
  
  <!-- 自定义事件处理 -->
  <Send1 
    defaultText="自定义处理" 
    sentText="处理中..." 
    primaryColor="#F44336"
    @send="customNavigate"
  />
  
  <!-- 自定义延迟跳转 -->
  <h3>自定义延迟跳转</h3>
  
  <!-- 快速跳转 (300ms) -->
  <Send1 
    defaultText="快速跳转" 
    sentText="即将跳转..." 
    primaryColor="#00BCD4"
    to="https://www.bing.com"
    :external="true"
    :navigationDelay="300"
  />
  
  <!-- 中等延迟 (1500ms) -->
  <Send1 
    defaultText="稍等片刻" 
    sentText="准备中..." 
    primaryColor="#795548"
    to="https://www.github.com"
    :external="true"
    :navigationDelay="1500"
  />
  
  <!-- 长延迟 (3000ms) -->
  <Send1 
    defaultText="长时间等待" 
    sentText="请稍候..." 
    primaryColor="#607D8B"
    to="https://www.google.com"
    :external="true"
    :navigationDelay="3000"
  />
</template>

<script>
// 为了在setup外部定义方法，增加这个部分
export default {
  methods: {
    customNavigate() {
      alert('你可以在这里自定义导航逻辑！')
      // 其他自定义处理...
    }
  }
}
</script>

<style scoped>
h3 {
  margin-top: 30px;
  margin-bottom: 15px;
  color: #333;
}

.button-row {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-bottom: 15px;
}
</style>