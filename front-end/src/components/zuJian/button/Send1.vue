<template>
  <button class="button" @click="handleClick" @focus="handleFocus" ref="buttonRef" :class="{'is-sent': isSent, 'fit-content': fitContent}" :style="buttonStyle">
    <div class="outline"></div>
    <div class="state state--default">
      <div class="icon">
        <slot name="default-icon">
          <svg xmlns="http://www.w3.org/2000/svg"
               width="26" height="28"
               viewBox="0 0 1024 1024">
            <path fill="#000000" d="M934.32 65.904c10.432 0 17.776 1.938 21.6 3.41c4.592 12.224 10.753 56.031-34.528 101.343L690.4 401.633l1.664 28.656c3.504 59.968 10 167.44 15.6 259.567c4.944 82 9.633 159.44 9.936 166.032c.16 4.529.225 5.601-3.999 10.689c-9.44 11.472-27.056 30.912-41.904 47.024c-23.024-62.032-71.408-193.057-98.128-266.4l-34.336-94.368l-71.024 71.024l-130.608 125.584l-18.192 18.16l-.56 25.68c-.432 20.496-.336 57.28-.288 89.712c.064 22.592.129 43.12-.031 54.432c-.288.528 4.368 1.152 3.936 1.904c-2.784-4.464-5.776-9.28-8.944-14.288c-26.336-42-62.784-100.096-73.904-118.224l-8.128-13.28l-13.344-8.065c-48.528-29.311-102.288-63.151-135.088-84.287c1.136-.656 2.063 2.816 2.815 2.415h2.128c10.32 0 27.376.224 46.496.496c25.008.336 53.376.752 75.088.752c8.32 0 15.712-.064 21.664-.192l25.68-.592l18.16-18.16l125.744-129.712l70.784-70.752l-93.935-34.56c-70.592-25.967-205.808-76.464-269.056-100.224c16.223-14.944 35.775-32.688 47.183-42.129c3.184-2.624 5.665-3.967 7.376-3.967l2.256.064c7.056.336 94.688 6.064 179.407 11.6c89.936 5.872 191.44 12.496 249.151 16.16l28.848 1.808l231.024-231.04c32.448-32.4 64.32-37.248 80.449-37.248m.001-63.997c-37.808 0-84.222 14.526-125.678 55.998L598.035 268.497c-118.624-7.504-422.432-27.6-429.968-27.808a101 101 0 0 0-4.88-.129c-10.256 0-27.968 1.968-48.128 18.624c-23.664 19.569-73.008 65.97-73.008 65.97c-11.904 11.935-17.936 26.719-16.496 40.623c.88 8.4 5.44 23.712 26.064 31.777c12.528 4.912 211.904 79.504 303.969 113.376L229.844 640.642c-5.569.128-12.465.192-20.257.192c-38.336 0-97.776-1.248-121.601-1.248c-3.152 0-5.68 0-7.473.064c-7.248.224-22.256-3.344-61.84 29.744l-2.816 2.624C3.985 683.89 1.201 695.73.945 703.554c-.256 8.064 1.904 19.68 13.568 29.024c7.008 5.664 96.848 63.184 170.527 107.68c17.665 28.817 98.945 158 103.185 165.008c6.193 10.464 16.32 16.432 28.433 16.816h1.008c11.776 0 23.872-5.84 35.712-17.344c33.504-39.184 28.88-55.407 29.023-62.224c.528-21.376-.368-111.936.4-147.84l130.592-125.6c33.376 91.68 106.336 289.008 111.216 301.567c8.128 20.624 23.44 25.153 31.84 26c1.376.16 2.785.225 4.16.225c12.625 0 25.712-5.936 36.432-16.655c0 0 46.256-49.088 65.904-72.976c19.68-23.872 18.913-44.256 18.529-53.872c-.16-6.656-18.689-308.816-25.569-426.816L966.561 215.89c74.657-74.689 62.785-164.688 35.057-192.368c-12.24-12.304-37.024-21.615-67.297-21.616z"/>
          </svg>
        </slot>
      </div>
      <p>
        <span v-for="(char, index) in defaultText" :key="`default-${index}`" :style="{animationDelay: index * 0.03 + 's'}" class="char-span">
          {{ char }}
        </span>
      </p>
    </div>
    <div class="state state--sent">
      <div class="icon">
        <slot name="sent-icon">
          <svg xmlns="http://www.w3.org/2000/svg" 
               width="26" height="28" 
               viewBox="0 0 24 24">
            <path fill="currentColor" d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
          </svg>
        </slot>
      </div>
      <p>
        <span v-for="(char, index) in sentText" :key="`sent-${index}`" :style="{animationDelay: (index + 5) * 0.03 + 's'}" class="char-span char-sent">
          {{ char }}
        </span>
      </p>
    </div>
  </button>
</template>

<script>
export default {
  name: 'SendMessageButton',
  props: {
    defaultText: {
      type: String,
      default: 'Send Message'
    },
    sentText: {
      type: String,
      default: 'Sent!'
    },
    primaryColor: {
      type: String,
      default: '#4f46e5'
    },
    buttonWidth: {
      type: String,
      default: '220px'
    },
    minWidth: {
      type: String,
      default: null
    },
    maxWidth: {
      type: String,
      default: null
    },
    buttonHeight: {
      type: String,
      default: '70px'
    },
    minHeight: {
      type: String,
      default: null
    },
    maxHeight: {
      type: String,
      default: null
    },
    fitContent: {
      type: Boolean,
      default: false
    },
    borderRadius: {
      type: String,
      default: '16px'
    },
    fontSize: {
      type: String,
      default: '18px'
    },
    animationDuration: {
      type: Number,
      default: 3000
    },
    to: {
      type: [String, Object],
      default: null
    },
    external: {
      type: Boolean,
      default: false
    },
    target: {
      type: String,
      default: '_blank'
    },
    navigationDelay: {
      type: Number,
      default: 800
    }
  },
  data() {
    return {
      isSent: false
    }
  },
  computed: {
    buttonStyle() {
      const styles = {}
      
      if (this.fitContent) {
        styles.width = 'auto'
        styles.minWidth = this.minWidth || this.buttonWidth
      } else {
        styles.width = this.buttonWidth
      }
      
      if (this.minWidth) styles.minWidth = this.minWidth
      if (this.maxWidth) styles.maxWidth = this.maxWidth
      
      styles.height = this.buttonHeight
      
      if (this.minHeight) styles.minHeight = this.minHeight
      if (this.maxHeight) styles.maxHeight = this.maxHeight
      
      return styles
    }
  },
  methods: {
    handleClick() {
      this.$emit('send')
      
      if (this.to) {
        this.navigate()
      }
    },
    handleFocus() {
      this.isSent = true
      setTimeout(() => {
        this.isSent = false
        if (this.buttonRef) {
          this.buttonRef.blur()
        }
      }, this.animationDuration)
    },
    navigate() {
      setTimeout(() => {
        if (this.external) {
          window.open(this.to, this.target)
        } else if (typeof this.to === 'string') {
          if (this.$router) {
            this.$router.push(this.to)
          } else {
            window.location.href = this.to
          }
        } else if (typeof this.to === 'object') {
          if (this.$router) {
            this.$router.push(this.to)
          }
        }
      }, this.navigationDelay)
    }
  }
}
</script>

<style scoped>
.button {
  --primary: v-bind(primaryColor);
  --neutral-1: #ffffff;
  --neutral-2: #f3f4f6;
  --radius: v-bind(borderRadius);
  --shadow-color: rgba(0, 0, 0, 0.15);
  --animation-delay-unit: 0.03s;
  --font-size: v-bind(fontSize);
  
  /* 基础样式 */
  cursor: pointer;
  border-radius: var(--radius);
  text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  border: none;
  background: linear-gradient(to bottom, var(--neutral-1), var(--neutral-2));
  box-shadow:
      0 1px 1px rgba(255, 255, 255, 0.4),
      0 12px 24px var(--shadow-color),
      0 4px 8px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  padding: 20px 22px;
  font-family: "Inter", system-ui, sans-serif;
  font-style: normal;
  font-size: var(--font-size);
  font-weight: 600;
  letter-spacing: -0.2px;
  overflow: hidden;
  will-change: transform, box-shadow;
}

.button.fit-content {
  width: auto;
  padding-left: 35px;
  padding-right: 35px;
}

.button:hover {
  transform: translateY(-2px) scale(1.02);
  box-shadow:
      0 2px 2px rgba(255, 255, 255, 0.4),
      0 18px 32px var(--shadow-color),
      0 8px 12px rgba(0, 0, 0, 0.1);
}

.button:active {
  transform: scale(0.98);
  box-shadow:
      0 0 1px 2px rgba(255, 255, 255, 0.3),
      0 10px 3px -3px rgba(0, 0, 0, 0.2);
}

.button:after {
  content: "";
  position: absolute;
  inset: 0;
  border-radius: var(--radius);
  border: 2.5px solid transparent;
  background:
      linear-gradient(var(--neutral-1), var(--neutral-2)) padding-box,
      linear-gradient(to bottom, rgba(0, 0, 0, 0.1), rgba(0, 0, 0, 0.45))
      border-box;
  z-index: 0;
  transition: all 0.3s ease;
}

.button:hover::after {
  transform: scale(1.05, 1.1);
  box-shadow: inset 0 -1px 3px 0 rgba(255, 255, 255, 1);
}

.button::before {
  content: "";
  inset: 7px 6px 6px 6px;
  position: absolute;
  background: linear-gradient(to top, var(--neutral-1), var(--neutral-2));
  border-radius: calc(var(--radius) - 5px);
  filter: blur(0.5px);
  z-index: 2;
}

/* 状态容器样式 */
.state {
  padding-left: 29px;
  z-index: 2;
  display: flex;
  position: relative;
}

.state p {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0;
}

.state .icon {
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  margin: auto;
  transform: scale(1.25);
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.state .icon svg {
  overflow: visible;
}

/* 字符动画样式 */
.char-span {
  display: block;
  opacity: 0;
  animation: slideDown 0.8s ease forwards;
  will-change: transform, opacity;
}

.button:hover .char-span {
  opacity: 1;
  animation: wave 0.5s ease forwards;
}

.button:focus .char-span,
.is-sent .char-span {
  opacity: 1;
  animation: disapear 0.6s ease forwards;
}

.char-sent {
  /* 不使用CSS变量，改为在内联样式中设置 */
}

/* 发光轮廓 */
.outline {
  position: absolute;
  border-radius: inherit;
  overflow: hidden;
  z-index: 1;
  opacity: 0;
  transition: opacity 0.4s ease;
  inset: -2px -3.5px;
}

.outline::before {
  content: "";
  position: absolute;
  inset: -100%;
  background: conic-gradient(
      from 180deg,
      transparent 60%,
      var(--primary) 85%,
      transparent 100%
  );
  animation: spin 2.5s linear infinite;
  animation-play-state: paused;
  opacity: 0.7;
}

.button:hover .outline {
  opacity: 1;
}

.button:hover .outline::before {
  animation-play-state: running;
}

/* 状态切换 */
.state--default span:nth-child(4) {
  margin-right: 5px;
}

.state--sent {
  display: none;
}

.state--sent svg {
  transform: scale(1.25);
  margin-right: 8px;
}

.button:focus .state--default,
.is-sent .state--default {
  position: absolute;
}

.button:focus .state--sent,
.is-sent .state--sent {
  display: flex;
}

.button:focus .state--sent .char-span,
.is-sent .state--sent .char-span {
  opacity: 0;
  animation: slideDown 0.8s ease forwards;
}

.button:focus .state--sent .icon svg,
.is-sent .state--sent .icon svg {
  opacity: 0;
  animation: appear 1.2s ease forwards 0.8s;
}

/* 飞机动画 */
.state--default .icon svg {
  animation: land 0.6s ease forwards;
}

.button:hover .state--default .icon {
  transform: rotate(45deg) scale(1.25);
}

.button:focus .state--default svg,
.is-sent .state--default svg {
  animation: takeOff 0.8s linear forwards;
}

.button:focus .state--default .icon,
.is-sent .state--default .icon {
  transform: rotate(0) scale(1.25);
}

/* 飞机尾迹 */
.state--default .icon:before {
  content: "";
  position: absolute;
  top: 50%;
  height: 2px;
  width: 0;
  left: -5px;
  background: linear-gradient(to right, transparent, rgba(0, 0, 0, 0.5));
}

.button:focus .state--default .icon:before,
.is-sent .state--default .icon:before {
  animation: contrail 0.8s linear forwards;
}

/* 关键帧动画 */
@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@keyframes wave {
  30% {
    opacity: 1;
    transform: translateY(4px) translateX(0) rotate(0);
  }
  50% {
    opacity: 1;
    transform: translateY(-4px) translateX(0) rotate(0);
    color: var(--primary);
  }
  100% {
    opacity: 1;
    transform: translateY(0) translateX(0) rotate(0);
  }
}

@keyframes slideDown {
  0% {
    opacity: 0;
    transform: translateY(-20px) translateX(5px) rotate(-90deg);
    color: var(--primary);
    filter: blur(5px);
  }
  30% {
    opacity: 1;
    transform: translateY(4px) translateX(0) rotate(0);
    filter: blur(0);
  }
  50% {
    opacity: 1;
    transform: translateY(-3px) translateX(0) rotate(0);
  }
  100% {
    opacity: 1;
    transform: translateY(0) translateX(0) rotate(0);
  }
}

@keyframes disapear {
  from {
    opacity: 1;
  }
  to {
    opacity: 0;
    transform: translateX(5px) translateY(20px);
    color: var(--primary);
    filter: blur(5px);
  }
}

@keyframes takeOff {
  0% {
    opacity: 1;
  }
  60% {
    opacity: 1;
    transform: translateX(80px) rotate(45deg) scale(2.2);
  }
  100% {
    opacity: 0;
    transform: translateX(180px) rotate(45deg) scale(0);
  }
}

@keyframes land {
  0% {
    transform: translateX(-60px) translateY(30px) rotate(-50deg) scale(2);
    opacity: 0;
    filter: blur(3px);
  }
  100% {
    transform: translateX(0) translateY(0) rotate(0);
    opacity: 1;
    filter: blur(0);
  }
}

@keyframes contrail {
  0% {
    width: 0;
    opacity: 1;
  }
  8% {
    width: 15px;
  }
  60% {
    opacity: 0.7;
    width: 80px;
  }
  100% {
    opacity: 0;
    width: 160px;
  }
}

@keyframes appear {
  0% {
    opacity: 0;
    transform: scale(4) rotate(-40deg);
    color: var(--primary);
    filter: blur(4px);
  }
  30% {
    opacity: 1;
    transform: scale(0.6);
    filter: blur(1px);
  }
  50% {
    opacity: 1;
    transform: scale(1.2);
    filter: blur(0);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}
</style>