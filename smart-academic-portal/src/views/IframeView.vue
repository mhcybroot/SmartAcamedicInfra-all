<template>
  <div class="iframe-container glass-panel">
    <!-- Show a loading skeleton while iframe loads -->
    <div class="loader" v-if="loading">
      <div class="spinner"></div>
      <p>Connecting to {{ routeTitle }}...</p>
    </div>
    
    <iframe 
      :src="iframeSrc" 
      class="integrated-frame" 
      @load="onLoad"
      title="Integrated App"
      frameborder="0"
    ></iframe>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const loading = ref(true)

const iframeSrc = computed(() => {
  return route.meta.src
})

const routeTitle = computed(() => {
  return route.meta.title
})

// Reset loading state when route changes
watch(() => route.fullPath, () => {
  loading.value = true
})

const onLoad = () => {
  loading.value = false
}
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

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
