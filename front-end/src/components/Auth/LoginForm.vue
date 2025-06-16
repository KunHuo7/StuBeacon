<script setup>
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net/index.js";
import { message } from 'ant-design-vue';
import router from "@/router/index.js";
const [messageApi, contextHolder] = message.useMessage();
const props=defineProps(['isLoginComponent'])
const emits=defineEmits(["update:isLoginComponent"]);
const goAnotherForm=()=>{
  emits("update:isLoginComponent",false)
}
const a=ref('123')
const showPassword = ref(false);

const loginForm=reactive({
  username:'',
  password:''
})

const loginHandler=()=>{
  post('api/auth/login',{
    username:loginForm.username,
    password:loginForm.password
  },(message,data)=>{
    messageApi.success(message+'3s后为您转到首页')
    localStorage.setItem("authToken",data);
    setTimeout(()=>{
      router.push('/')
    },3000)

  },(message,data)=>{
    messageApi.warning(message)

  },(message,data)=>{
   messageApi.error("请联系管理员")
  })
}
</script>
<template>
  <contextHolder/>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-r from-blue-100 to-indigo-100 loginBg">
    <div class="auth-card bg-white bg-opacity-90 p-8 rounded-2xl shadow-xl w-full max-w-md transform transition-all duration-500 hover:shadow-2xl">
      <div class="text-center mb-8 animate-fade-in">
        <div class="logo-container mb-4">
          <div class="logo-animation">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-indigo-600" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-6-3a2 2 0 11-4 0 2 2 0 014 0zm-2 4a5 5 0 00-4.546 2.916A5.986 5.986 0 0010 16a5.986 5.986 0 004.546-2.084A5 5 0 0010 11z" clip-rule="evenodd" />
            </svg>
          </div>
        </div>
        <h2 class="text-3xl font-bold  mb-2 animate-slide-down">欢迎登录</h2>
        <p class="text-gray-500 text-sm animate-slide-up">请输入您的账号信息</p>
      </div>
      <div class="space-y-6">
        <div class="mb-4 form-item animate-fade-in" style="animation-delay: 100ms;">
          <label for="username" class="block text-sm font-medium text-gray-700 mb-1">账号</label>
          <div class="relative input-container">
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                <path d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z"/>
              </svg>
            </div>
            <input
                type="text"
                id="username"
                v-model="loginForm.username"
                class="pl-10 w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-300 bg-white bg-opacity-80 text-black hover:border-indigo-400"
                placeholder="请输入账号"
                required
            />
            <div class="input-focus-effect"></div>
          </div>
        </div>
        <div class="mb-6 form-item animate-fade-in" style="animation-delay: 200ms;">
          <label for="password" class="block text-sm font-medium text-gray-700 mb-1">密码</label>
          <div class="relative input-container">
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd"/>
              </svg>
            </div>
            <input
                :type="showPassword ? 'text' : 'password'"
                id="password"
                v-model="loginForm.password"
                class="text-black pl-10 w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-300 bg-white bg-opacity-80 hover:border-indigo-400"
                placeholder="请输入密码"
                required
            />
            <div @click="showPassword = !showPassword" class="absolute inset-y-0 right-0 pr-3 flex items-center cursor-pointer">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400 hover:text-gray-600 transition-colors duration-200" viewBox="0 0 20 20" fill="currentColor">
                <path v-if="!showPassword" d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
                <path v-if="!showPassword" fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd" />
                <path v-if="showPassword" fill-rule="evenodd" d="M3.707 2.293a1 1 0 00-1.414 1.414l14 14a1 1 0 001.414-1.414l-1.473-1.473A10.014 10.014 0 0019.542 10C18.268 5.943 14.478 3 10 3a9.958 9.958 0 00-4.512 1.074l-1.78-1.781zm4.261 4.26l1.514 1.515a2.003 2.003 0 012.45 2.45l1.514 1.514a4 4 0 00-5.478-5.478z" clip-rule="evenodd" />
                <path v-if="showPassword" d="M12.454 16.697L9.75 13.992a4 4 0 01-3.742-3.741L2.335 6.578A9.98 9.98 0 00.458 10c1.274 4.057 5.065 7 9.542 7 .847 0 1.669-.105 2.454-.303z" />
              </svg>
            </div>
            <div class="input-focus-effect"></div>
          </div>
        </div>
        <div class="mt-8 form-item animate-fade-in" style="animation-delay: 300ms;">
          <button @click="loginHandler(loginForm.username,loginForm.password)"
              class="login-button w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transform transition-all duration-300 hover:scale-[1.02] font-medium text-lg relative overflow-hidden">
            <span class="relative z-10">登录</span>
          </button>
        </div>
        <div class="mt-6 text-center flex justify-center items-center space-x-1 form-item animate-fade-in" style="animation-delay: 400ms;">
          <span class="text-gray-600 text-sm">还没有账号？</span>

          <a @click="goAnotherForm" class="text-indigo-600 hover:text-indigo-800 font-medium text-sm cursor-pointer transition-all duration-200 hover:underline">立即注册</a>
        </div>
      </div>
    </div>
  </div>

</template>

<style scoped>
.loginBg {
  background-image: url('@/assets/images/学校.jpeg');  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  min-height: 100vh;
  position: relative;
}

.loginBg::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.3), rgba(99, 102, 241, 0.3));
  z-index: 1;
}

.loginBg > div {
  position: relative;
  z-index: 2;
}

input:focus {
  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.2);
  outline: none;
}

button {
  transition: all 0.3s ease;
}

button:active {
  transform: scale(0.98);
}

/* 新增动画效果 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideDown {
  from { transform: translateY(-20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

@keyframes slideUp {
  from { transform: translateY(20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.05); }
  100% { transform: scale(1); }
}

.animate-fade-in {
  animation: fadeIn 0.6s ease-out forwards;
}

.animate-slide-down {
  animation: slideDown 0.6s ease-out forwards;
}

.animate-slide-up {
  animation: slideUp 0.6s ease-out forwards;
}

.logo-animation {
  animation: pulse 2s infinite;
}

.auth-card {
  box-shadow: 0 10px 25px -5px rgba(59, 130, 246, 0.1), 0 10px 10px -5px rgba(99, 102, 241, 0.04);
}

.input-container {
  position: relative;
  overflow: hidden;
}

.input-focus-effect {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background: linear-gradient(90deg, #6366f1, #3b82f6);
  transition: width 0.3s ease;
}

.input-container input:focus + .input-focus-effect {
  width: 100%;
}

.login-button {
  background: linear-gradient(135deg, #6366f1, #3b82f6);
  box-shadow: 0 4px 6px -1px rgba(99, 102, 241, 0.2), 0 2px 4px -1px rgba(99, 102, 241, 0.1);
}

.login-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: all 0.6s;
}

.login-button:hover::before {
  left: 100%;
}

.form-item {
  opacity: 0;
  animation-fill-mode: forwards;
}
</style>