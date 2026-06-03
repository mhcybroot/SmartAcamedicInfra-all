<template>
  <div class="app-container">
    <!-- Unified Sidebar -->
    <aside class="sidebar glass-panel">
      <div class="brand">
        <div class="logo-circle"></div>
        <h1>Smart Academic Infrastructure</h1>
      </div>
      
      <nav class="nav-links">
        <router-link to="/" class="nav-item" active-class="active">
          <span class="icon">📊</span> Executive Dashboard
        </router-link>
        <router-link to="/results" class="nav-item" active-class="active">
          <span class="icon">📝</span> Results & Grading
        </router-link>
        <router-link to="/academic-members" class="nav-item" active-class="active">
          <span class="icon">👥</span> Academic Members
        </router-link>
        <router-link to="/student-watch" class="nav-item" active-class="active">
          <span class="icon">🛡️</span> Student Watch
        </router-link>

        <div class="nav-divider">
          <span>Student Access</span>
        </div>

        <router-link to="/student-portal" class="nav-item student-portal-link" active-class="active">
          <span class="icon">🎓</span> Student Portal
        </router-link>

        <div class="nav-divider">
          <span>Demo Tools</span>
        </div>

        <button class="nav-item generate-btn academic-btn" @click="generateAcademicData" :disabled="generatingAcademic">
          <span class="icon" :class="{ spinning: generatingAcademic }">🎲</span>
          <span v-if="!generatingAcademic">Generate Academic Data</span>
          <span v-else>Generating…</span>
        </button>

        <button class="nav-item generate-btn exam-btn" @click="generateExamData" :disabled="generatingExam">
          <span class="icon" :class="{ spinning: generatingExam }">📝</span>
          <span v-if="!generatingExam">Generate Results Data</span>
          <span v-else>Generating…</span>
        </button>

        <button class="nav-item generate-btn watch-btn" @click="generateWatchData" :disabled="generatingWatch">
          <span class="icon" :class="{ spinning: generatingWatch }">🛡️</span>
          <span v-if="!generatingWatch">Generate Watch Data</span>
          <span v-else>Generating…</span>
        </button>
      </nav>

      <div class="sidebar-footer">
        <div class="user-profile">
          <div class="avatar">A</div>
          <div class="user-info">
            <span class="name">Admin User</span>
            <span class="role">System Administrator</span>
          </div>
        </div>
      </div>
    </aside>

    <!-- Main Content Area -->
    <main class="main-content">
      <header class="top-header glass-panel">
        <div class="header-title">{{ currentRouteName }}</div>
        <div class="header-actions">
          <button class="glass-btn primary">Logout</button>
        </div>
      </header>
      
      <div class="content-wrapper">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>

    <!-- Toast Notification -->
    <transition name="toast">
      <div v-if="toast.show" :class="['toast-msg', toast.type]">
        <span class="toast-icon">{{ toast.type === 'success' ? '✅' : '❌' }}</span>
        {{ toast.message }}
      </div>
    </transition>
  </div>
</template>

<script setup>
import { computed, ref, reactive } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const currentRouteName = computed(() => {
  return route.meta.title || route.name || 'Dashboard'
})

// ── Generate Demo Data ────────────────────────────────────────────────
const generatingAcademic = ref(false)
const generatingExam = ref(false)
const generatingWatch = ref(false)
const toast = reactive({ show: false, message: '', type: 'success' })

function showToast(message, type = 'success') {
  toast.message = message
  toast.type = type
  toast.show = true
  setTimeout(() => { toast.show = false }, 4000)
}

async function generateAcademicData() {
  generatingAcademic.value = true
  try {
    const host = window.location.hostname
    const res = await fetch(`http://${host}:7511/api/demo/generate`, { method: 'POST' })
    const data = await res.json()
    if (res.ok) {
      showToast(data.message || `✅ Seeded ${data.seeded} academic members!`, 'success')
    } else {
      showToast('Error: ' + (data.error || 'Unknown error'), 'error')
    }
  } catch (e) {
    showToast('❌ Could not reach Academic backend. Is it running?', 'error')
  } finally {
    generatingAcademic.value = false
  }
}

async function generateExamData() {
  generatingExam.value = true
  try {
    const host = window.location.hostname
    const res = await fetch(`http://${host}:7512/api/demo/generate`, { method: 'POST' })
    const data = await res.json()
    if (res.ok) {
      showToast(data.message || `✅ Seeded student exam results!`, 'success')
    } else {
      showToast('Error: ' + (data.error || 'Unknown error'), 'error')
    }
  } catch (e) {
    showToast('❌ Could not reach Exam/Result backend. Is it running?', 'error')
  } finally {
    generatingExam.value = false
  }
}

async function generateWatchData() {
  generatingWatch.value = true
  try {
    const host = window.location.hostname
    const res = await fetch(`http://${host}:7513/api/demo/generate`, { method: 'POST' })
    const data = await res.json()
    if (res.ok) {
      showToast(data.message || `✅ Seeded Student Watch mock data!`, 'success')
    } else {
      showToast('Error: ' + (data.error || 'Unknown error'), 'error')
    }
  } catch (e) {
    showToast('❌ Could not reach Student Watch backend. Is it running?', 'error')
  } finally {
    generatingWatch.value = false
  }
}
</script>

<style scoped>
.sidebar {
  width: 280px;
  height: calc(100vh - 32px);
  margin: 16px;
  display: flex;
  flex-direction: column;
}

.brand {
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 12px;
  border-bottom: 1px solid var(--border);
}

.logo-circle {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--accent-info));
}

.brand h1 {
  font-size: 1.1rem;
  font-weight: 600;
  line-height: 1.2;
}

.nav-links {
  padding: 24px 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  border-radius: 8px;
  color: var(--text-muted);
  font-weight: 500;
  transition: all 0.3s ease;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.05);
  color: var(--text-main);
  transform: translateX(4px);
}

.nav-item.active {
  background: rgba(99, 102, 241, 0.15);
  color: var(--text-main);
  border-left: 3px solid var(--primary);
}

.nav-divider {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px 16px 8px;
  color: rgba(245, 158, 11, 0.6);
  font-size: 0.65rem;
  font-weight: 700;
  letter-spacing: 1.5px;
  text-transform: uppercase;
}

.nav-divider::before,
.nav-divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: rgba(245, 158, 11, 0.2);
}

.student-portal-link {
  border: 1px solid rgba(245, 158, 11, 0.15);
  color: rgba(245, 158, 11, 0.8);
}

.student-portal-link:hover {
  background: rgba(245, 158, 11, 0.08) !important;
  color: #f59e0b !important;
  border-color: rgba(245, 158, 11, 0.3);
}

.student-portal-link.active {
  background: rgba(245, 158, 11, 0.15) !important;
  color: #f59e0b !important;
  border-left: 3px solid #f59e0b !important;
  border-color: rgba(245, 158, 11, 0.3);
}

.sidebar-footer {
  padding: 24px;
  border-top: 1px solid var(--border);
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--bg-dark);
  border: 2px solid var(--primary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.user-info {
  display: flex;
  flex-direction: column;
}

.user-info .name {
  font-weight: 600;
  font-size: 0.9rem;
}

.user-info .role {
  font-size: 0.75rem;
  color: var(--text-muted);
}

.top-header {
  margin: 16px 16px 0 0;
  padding: 16px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 12px;
}

.header-title {
  font-size: 1.25rem;
  font-weight: 600;
}

.content-wrapper {
  flex: 1;
  padding: 16px 16px 16px 0;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

/* ── Generate Data Button ── */
.generate-btn {
  width: 100%;
  text-align: left;
  cursor: pointer;
  font-family: 'Outfit', sans-serif;
  font-size: 0.82rem;
  font-weight: 500;
  transition: all 0.2s ease;
  margin-bottom: 6px;
}

.academic-btn {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.15), rgba(99, 102, 241, 0.05));
  border: 1px solid rgba(99, 102, 241, 0.3);
  color: #c4b5fd;
}

.academic-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.3), rgba(99, 102, 241, 0.15));
  color: #a78bfa;
  border-color: rgba(99, 102, 241, 0.5);
  box-shadow: 0 4px 14px rgba(99, 102, 241, 0.25);
}

.exam-btn {
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.15), rgba(16, 185, 129, 0.05));
  border: 1px solid rgba(16, 185, 129, 0.3);
  color: #a7f3d0;
}

.exam-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, rgba(16, 185, 129, 0.3), rgba(16, 185, 129, 0.15));
  color: #34d399;
  border-color: rgba(16, 185, 129, 0.5);
  box-shadow: 0 4px 14px rgba(16, 185, 129, 0.25);
}

.watch-btn {
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.15), rgba(6, 182, 212, 0.05));
  border: 1px solid rgba(6, 182, 212, 0.3);
  color: #a5f3fc;
}

.watch-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, rgba(6, 182, 212, 0.3), rgba(6, 182, 212, 0.15));
  color: #22d3ee;
  border-color: rgba(6, 182, 212, 0.5);
  box-shadow: 0 4px 14px rgba(6, 182, 212, 0.25);
}

.generate-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spinning {
  display: inline-block;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to   { transform: rotate(360deg); }
}

/* ── Toast Notification ── */
.toast-msg {
  position: fixed;
  bottom: 24px;
  left: 50%;
  transform: translateX(-50%);
  padding: 14px 24px;
  border-radius: 12px;
  font-size: 0.9rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 10px;
  z-index: 9999;
  box-shadow: 0 8px 32px rgba(0,0,0,0.4);
  backdrop-filter: blur(12px);
  max-width: 480px;
  text-align: center;
}

.toast-msg.success {
  background: rgba(16, 185, 129, 0.9);
  border: 1px solid rgba(16, 185, 129, 0.5);
  color: #fff;
}

.toast-msg.error {
  background: rgba(239, 68, 68, 0.9);
  border: 1px solid rgba(239, 68, 68, 0.5);
  color: #fff;
}

.toast-enter-active, .toast-leave-active {
  transition: all 0.3s ease;
}
.toast-enter-from, .toast-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(20px);
}
</style>
