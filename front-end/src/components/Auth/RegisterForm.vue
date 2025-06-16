<script setup>
import { reactive, ref } from "vue";
import { message } from 'ant-design-vue';
import { post } from "@/net/index.js";
import router from "@/router/index.js";

const [messageApi, contextHolder] = message.useMessage();
defineProps(['isLoginComponent']);
const emits = defineEmits(["update:isLoginComponent"]);
const showPassword = ref(false);

const goAnotherForm = () => {
  emits('update:isLoginComponent', true);  // 切换到登录表单
};

const loginForm = reactive({
  username: '',
  password: '',
  mail: '',
  studentId: ''
});

const validateForm = () => {
  if (!loginForm.username || !loginForm.password) {
    messageApi.warning('用户名和密码不能为空');
    return false;
  }
  if (!loginForm.mail) {
    messageApi.warning('邮箱不能为空');
    return false;
  }
  if (!loginForm.studentId) {
    messageApi.warning('学号不能为空');
    return false;
  }
  
  // 验证邮箱格式
  const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
  if (!emailRegex.test(loginForm.mail)) {
    messageApi.warning('请输入有效的邮箱地址');
    return false;
  }
  
  return true;
};

const registerHandler = () => {
  if (!validateForm()) return;

  // 替换为注册请求
  post('/api/auth/register', {
    username: loginForm.username,
    password: loginForm.password,
    mail: loginForm.mail,
    studentId: loginForm.studentId
  }, (message, data) => {
    messageApi.success(message + ' 3秒后为您切换到登录页');
    // 移除保存token的操作，注册成功后应该去登录
    setTimeout(() => {
      goAnotherForm(); // 使用goAnotherForm方法切换到登录表单，而不是通过路由跳转
    }, 3000);
  }, (message) => {
    messageApi.warning(message);
  }, () => {
    messageApi.error("发生错误，请联系管理员");
  });
};
</script>

<template>
  <contextHolder/>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-r from-blue-100 to-indigo-100 loginBg">
    <div class="auth-card bg-white bg-opacity-90 p-8 rounded-2xl shadow-xl w-full max-w-md transform transition-all duration-500 hover:shadow-2xl">
      <div class="text-center mb-8 animate-fade-in">
        <div class="logo-container mb-4">
          <div class="logo-animation">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto text-indigo-600" viewBox="0 0 20 20" fill="currentColor">
              <path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd" />
            </svg>
          </div>
        </div>
        <h2 class="text-3xl font-bold text-indigo-700 mb-2 animate-slide-down">欢迎注册</h2>
        <p class="text-gray-500 text-sm animate-slide-up">请创建您的账号</p>
      </div>
      <div class="space-y-6">
        <div class="mb-4 form-item animate-fade-in" style="animation-delay: 100ms;">
          <label for="username" class="block text-sm font-medium text-gray-700 mb-1">用户名</label>
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
                class="pl-10 w-full px-4 py-3 border text-black border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-300 bg-white bg-opacity-80 hover:border-indigo-400"
                placeholder="请输入用户名"
                required
            />
            <div class="input-focus-effect"></div>
          </div>
        </div>
        <div class="mb-4 form-item animate-fade-in" style="animation-delay: 150ms;">
          <label for="studentId" class="block text-sm font-medium text-gray-700 mb-1">学号</label>
          <div class="relative input-container">
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                <path d="M10.394 2.08a1 1 0 00-.788 0l-7 3a1 1 0 000 1.84L5.25 8.051a.999.999 0 01.356-.257l4-1.714a1 1 0 11.788 1.838L7.667 9.088l1.94.831a1 1 0 00.787 0l7-3a1 1 0 000-1.838l-7-3zM3.31 9.397L5 10.12v4.102a8.969 8.969 0 00-1.05-.174 1 1 0 01-.89-.89 11.115 11.115 0 01.25-3.762zM9.3 16.573A9.026 9.026 0 007 14.935v-3.957l1.818.78a3 3 0 002.364 0l5.508-2.361a11.026 11.026 0 01.25 3.762 1 1 0 01-.89.89 8.968 8.968 0 00-5.35 2.524 1 1 0 01-1.4 0zM6 18a1 1 0 001-1v-2.065a8.935 8.935 0 00-2-.712V17a1 1 0 001 1z"/>
              </svg>
            </div>
            <input
                type="text"
                id="studentId"
                v-model="loginForm.studentId"
                class="pl-10 w-full px-4 py-3 border text-black border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-300 bg-white bg-opacity-80 hover:border-indigo-400"
                placeholder="请输入学号"
                required
            />
            <div class="input-focus-effect"></div>
          </div>
        </div>
        <div class="mb-4 form-item animate-fade-in" style="animation-delay: 180ms;">
          <label for="mail" class="block text-sm font-medium text-gray-700 mb-1">邮箱</label>
          <div class="relative input-container">
            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" viewBox="0 0 20 20" fill="currentColor">
                <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z" />
                <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z" />
              </svg>
            </div>
            <input
                type="email"
                id="mail"
                v-model="loginForm.mail"
                class="pl-10 w-full px-4 py-3 border text-black border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-300 bg-white bg-opacity-80 hover:border-indigo-400"
                placeholder="请输入邮箱"
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
                class="pl-10 w-full px-4 py-3 border text-black border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-300 bg-white bg-opacity-80 hover:border-indigo-400"
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
          <button @click="registerHandler"
              class="register-button w-full bg-indigo-600 text-white py-3 px-4 rounded-lg hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transform transition-all duration-300 hover:scale-[1.02] font-medium text-lg relative overflow-hidden">
            <span class="relative z-10">注册</span>
          </button>
        </div>
        <div class="mt-6 text-center flex justify-center items-center space-x-1 form-item animate-fade-in" style="animation-delay: 400ms;">
          <span class="text-gray-600 text-sm">已有账号？</span>
          <a @click="goAnotherForm" class="text-indigo-600 hover:text-indigo-800 font-medium text-sm cursor-pointer transition-all duration-200 hover:underline">立即登录</a>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.loginBg {
  background-image: url('@/assets/images/学校.jpeg');
  background-size: cover;
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
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.3), rgba(59, 130, 246, 0.3));
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

.register-button {
  background: linear-gradient(135deg, #6366f1, #3b82f6);
  box-shadow: 0 4px 6px -1px rgba(99, 102, 241, 0.2), 0 2px 4px -1px rgba(99, 102, 241, 0.1);
}

.register-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: all 0.6s;
}

.register-button:hover::before {
  left: 100%;
}

.form-item {
  opacity: 0;
  animation-fill-mode: forwards;
}
</style>
