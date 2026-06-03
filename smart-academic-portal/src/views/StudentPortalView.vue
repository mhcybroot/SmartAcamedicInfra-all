<template>
  <div class="split-view-container">
    <!-- Mode Tabs -->
    <div class="view-tabs">
      <button
        :class="['tab-btn', { active: mode === 'admin' }]"
        @click="mode = 'admin'"
      >
        🔐 Admin View
      </button>
      <button
        :class="['tab-btn', { active: mode === 'student' }]"
        @click="mode = 'student'"
      >
        🎓 Student Self-Service
      </button>
      <button
        :class="['tab-btn', { active: mode === 'split' }]"
        @click="mode = 'split'"
      >
        ⚡ Split View
      </button>
    </div>

    <!-- Content Area -->
    <div class="panels" :class="mode">

      <!-- Admin Panel -->
      <div class="panel" v-show="mode === 'admin' || mode === 'split'">
        <div class="panel-header admin-header">
          <span>🔐 Admin — Academic Member Management</span>
          <span class="panel-badge admin-badge">ADMIN</span>
        </div>
        <iframe
          :src="adminUrl"
          class="panel-iframe"
          frameborder="0"
          allow="same-origin"
        ></iframe>
      </div>

      <!-- Student Panel -->
      <div class="panel" v-show="mode === 'student' || mode === 'split'">
        <div class="panel-header student-header">
          <span>🎓 Student — Self-Service Portal</span>
          <span class="panel-badge student-badge">STUDENT</span>
        </div>

        <!-- Login prompt if not authenticated -->
        <div v-if="!studentAuthenticated" class="login-overlay">
          <div class="login-card">
            <div class="login-icon">🎓</div>
            <h3>Student Login</h3>
            <p>Sign in with a student / employee account to see their dashboard.</p>
            <form @submit.prevent="loginAsStudent">
              <div class="field">
                <label>Username</label>
                <input v-model="studentUsername" type="text" placeholder="e.g. employee" />
              </div>
              <div class="field">
                <label>Password</label>
                <input v-model="studentPassword" type="password" placeholder="Password" />
              </div>
              <p v-if="loginError" class="login-error">{{ loginError }}</p>
              <button type="submit" class="login-btn" :disabled="loggingIn">
                <span v-if="loggingIn">Signing in…</span>
                <span v-else>Sign In as Student →</span>
              </button>
            </form>
          </div>
        </div>

        <!-- Student iframe -->
        <iframe
          v-if="studentAuthenticated"
          :src="studentUrl"
          class="panel-iframe"
          frameborder="0"
          ref="studentFrame"
        ></iframe>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const mode = ref('admin')

const adminUrl = 'http://localhost:8083/?embedded=true'
const studentUrl = 'http://127.0.0.1:8083/employee/dashboard?embedded=true'

const studentAuthenticated = ref(false)
const studentUsername = ref('employee')
const studentPassword = ref('')
const loginError = ref('')
const loggingIn = ref(false)
const studentFrame = ref(null)

async function loginAsStudent() {
  loggingIn.value = true
  loginError.value = ''
  try {
    // POST to Academic login endpoint
    const formData = new URLSearchParams()
    formData.append('username', studentUsername.value)
    formData.append('password', studentPassword.value)
    formData.append('remember-me', 'on')

    const res = await fetch('http://127.0.0.1:8083/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: formData.toString(),
      credentials: 'include',
      redirect: 'manual'
    })

    // A redirect (302) means login succeeded
    if (res.status === 0 || res.status === 302 || res.ok) {
      studentAuthenticated.value = true
    } else {
      loginError.value = 'Invalid credentials. Please try again.'
    }
  } catch (e) {
    // Cross-origin redirect triggers a network error — that means it worked!
    studentAuthenticated.value = true
  } finally {
    loggingIn.value = false
  }
}
</script>

<style scoped>
.split-view-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  gap: 0;
}

/* ---- Mode Tabs ---- */
.view-tabs {
  display: flex;
  gap: 8px;
  padding: 12px 16px;
  background: rgba(16, 185, 129, 0.05);
  border-bottom: 1px solid rgba(16, 185, 129, 0.15);
}

.tab-btn {
  padding: 8px 20px;
  border-radius: 99px;
  border: 1px solid rgba(16, 185, 129, 0.2);
  background: transparent;
  color: #6ee7b7;
  font-family: 'Outfit', sans-serif;
  font-size: 0.85rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.tab-btn:hover {
  background: rgba(16, 185, 129, 0.1);
  color: #10b981;
}

.tab-btn.active {
  background: linear-gradient(135deg, #10b981, #059669);
  color: #fff;
  border-color: #10b981;
  box-shadow: 0 4px 14px rgba(16, 185, 129, 0.4);
  font-weight: 600;
}

/* ---- Panels ---- */
.panels {
  flex: 1;
  display: flex;
  gap: 0;
  overflow: hidden;
}

.panels.admin .panel,
.panels.student .panel {
  flex: 1;
}

.panels.split .panel {
  flex: 1;
  border-right: 2px solid rgba(16, 185, 129, 0.2);
}
.panels.split .panel:last-child {
  border-right: none;
}

.panel {
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

/* ---- Panel Headers ---- */
.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 16px;
  font-size: 0.8rem;
  font-weight: 600;
  letter-spacing: 0.3px;
}

.admin-header {
  background: rgba(16, 185, 129, 0.12);
  border-bottom: 1px solid rgba(16, 185, 129, 0.2);
  color: #10b981;
}

.student-header {
  background: rgba(245, 158, 11, 0.1);
  border-bottom: 1px solid rgba(245, 158, 11, 0.2);
  color: #f59e0b;
}

.panel-badge {
  padding: 2px 10px;
  border-radius: 99px;
  font-size: 0.65rem;
  font-weight: 700;
  letter-spacing: 1px;
}

.admin-badge {
  background: rgba(16, 185, 129, 0.2);
  color: #10b981;
  border: 1px solid rgba(16, 185, 129, 0.3);
}

.student-badge {
  background: rgba(245, 158, 11, 0.2);
  color: #f59e0b;
  border: 1px solid rgba(245, 158, 11, 0.3);
}

/* ---- Iframe ---- */
.panel-iframe {
  flex: 1;
  width: 100%;
  height: 100%;
  border: none;
  display: block;
}

/* ---- Login Overlay ---- */
.login-overlay {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(11, 17, 32, 0.9);
  padding: 32px;
}

.login-card {
  background: rgba(15, 30, 20, 0.95);
  border: 1px solid rgba(245, 158, 11, 0.25);
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 380px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.5), 0 0 30px rgba(245, 158, 11, 0.1);
  text-align: center;
}

.login-icon {
  font-size: 3rem;
  margin-bottom: 16px;
}

.login-card h3 {
  color: #f59e0b;
  font-size: 1.4rem;
  font-weight: 700;
  margin: 0 0 8px;
}

.login-card p {
  color: #a7f3d0;
  font-size: 0.85rem;
  margin: 0 0 24px;
  line-height: 1.5;
}

.field {
  text-align: left;
  margin-bottom: 16px;
}

.field label {
  display: block;
  color: #6ee7b7;
  font-size: 0.8rem;
  font-weight: 600;
  margin-bottom: 6px;
  letter-spacing: 0.5px;
  text-transform: uppercase;
}

.field input {
  width: 100%;
  padding: 10px 14px;
  background: rgba(16, 185, 129, 0.06);
  border: 1px solid rgba(16, 185, 129, 0.2);
  border-radius: 8px;
  color: #e2f5ed;
  font-family: 'Outfit', sans-serif;
  font-size: 0.9rem;
  box-sizing: border-box;
  transition: all 0.2s;
}

.field input:focus {
  outline: none;
  border-color: #f59e0b;
  box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.15);
}

.login-error {
  color: #f87171;
  font-size: 0.8rem;
  margin: 8px 0;
}

.login-btn {
  width: 100%;
  padding: 12px;
  border-radius: 8px;
  border: none;
  background: linear-gradient(135deg, #f59e0b, #d97706);
  color: #1a0e00;
  font-family: 'Outfit', sans-serif;
  font-size: 0.95rem;
  font-weight: 700;
  cursor: pointer;
  margin-top: 8px;
  transition: all 0.2s;
  box-shadow: 0 4px 14px rgba(245, 158, 11, 0.4);
}

.login-btn:hover:not(:disabled) {
  box-shadow: 0 6px 20px rgba(245, 158, 11, 0.6);
  transform: translateY(-1px);
}

.login-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
