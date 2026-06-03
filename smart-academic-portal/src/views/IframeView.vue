<template>
  <div class="iframe-container glass-panel">
    <!-- Show a loading skeleton while iframe loads -->
    <div class="loader" v-if="loading">
      <div class="spinner" v-if="!showTimeoutWarning"></div>
      <p v-if="!showTimeoutWarning">Connecting to {{ routeTitle }}...</p>
      
      <div class="timeout-warning" v-else>
        <span class="warning-icon">⚠️</span>
        <h3>Connection taking longer than expected</h3>
        <p>Ensure the backend service for <strong>{{ routeTitle }}</strong> is running on port <strong>{{ iframePort }}</strong>.</p>
        <div class="actions">
          <button class="glass-btn primary sm" @click="retryLoad">Retry Connection</button>
          <a :href="iframeSrc" target="_blank" class="glass-btn sm">Open in New Tab</a>
        </div>
      </div>
    </div>
    
    <iframe 
      :key="iframeKey"
      :src="iframeSrc" 
      class="integrated-frame" 
      @load="onLoad"
      title="Integrated App"
      frameborder="0"
    ></iframe>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const loading = ref(true)
const showTimeoutWarning = ref(false)
const iframeKey = ref(0)
let timer = null

const iframeSrc = computed(() => {
  if (!route.meta.src) return ''
  try {
    const url = new URL(route.meta.src)
    url.hostname = window.location.hostname
    return url.toString()
  } catch (e) {
    return route.meta.src
  }
})

const routeTitle = computed(() => {
  return route.meta.title
})

const iframePort = computed(() => {
  try {
    const url = new URL(iframeSrc.value)
    return url.port
  } catch (e) {
    return '80'
  }
})

const startTimeoutTimer = () => {
  clearTimeout(timer)
  showTimeoutWarning.value = false
  timer = setTimeout(() => {
    if (loading.value) {
      showTimeoutWarning.value = true
    }
  }, 10000) // 10 seconds timeout
}

// Reset loading state when route changes
watch(() => route.fullPath, () => {
  loading.value = true
  iframeKey.value++
  startTimeoutTimer()
})

const onLoad = () => {
  loading.value = false
  showTimeoutWarning.value = false
  clearTimeout(timer)
}

const retryLoad = () => {
  loading.value = true
  showTimeoutWarning.value = false
  iframeKey.value++
  startTimeoutTimer()
}

onMounted(() => {
  startTimeoutTimer()
})

onUnmounted(() => {
  clearTimeout(timer)
})
</script>

<style scoped>
.iframe-container {
  flex: 1;
  width: 100%;
  border-radius: 16px;
  position: relative;
  overflow: hidden;
  display: flex;
}

.integrated-frame {
  width: 100%;
  height: 100%;
  border: none;
  background-color: var(--bg-dark); /* Fallback */
}

.loader {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: var(--bg-panel);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  z-index: 10;
  backdrop-filter: blur(10px);
}

.spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  border-top-color: var(--primary);
  animation: spin 1s ease-in-out infinite;
}

.timeout-warning {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  text-align: center;
  max-width: 420px;
  padding: 24px;
  background: rgba(15, 23, 42, 0.6);
  border-radius: 12px;
  border: 1px solid rgba(245, 158, 11, 0.2);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

.warning-icon {
  font-size: 2.5rem;
}

.timeout-warning h3 {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--text-main);
}

.timeout-warning p {
  font-size: 0.9rem;
  color: var(--text-muted);
  line-height: 1.5;
}

.timeout-warning strong {
  color: var(--accent-warning);
}

.actions {
  display: flex;
  gap: 12px;
  margin-top: 8px;
}

.glass-btn.sm {
  padding: 0.5rem 1rem;
  font-size: 0.85rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
