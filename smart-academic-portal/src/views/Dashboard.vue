<template>
  <div class="dashboard">
    <div class="stats-grid">
      <div class="stat-card glass-panel">
        <div class="stat-icon info">👥</div>
        <div class="stat-content">
          <h3>Total Students</h3>
          <div class="value">12,450</div>
          <div class="trend positive">↑ 2.4% this semester</div>
        </div>
      </div>
      
      <div class="stat-card glass-panel">
        <div class="stat-icon primary">👨‍🏫</div>
        <div class="stat-content">
          <h3>Academic Members</h3>
          <div class="value">485</div>
          <div class="trend neutral">Active Staff & Faculty</div>
        </div>
      </div>

      <div class="stat-card glass-panel">
        <div class="stat-icon success">📅</div>
        <div class="stat-content">
          <h3>Attendance Rate</h3>
          <div class="value">94.2%</div>
          <div class="trend positive">↑ 1.1% today</div>
        </div>
      </div>

      <div class="stat-card glass-panel">
        <div class="stat-icon warning">🛡️</div>
        <div class="stat-content">
          <h3>Active Devices</h3>
          <div class="value">8,932</div>
          <div class="trend negative">14 alerts detected</div>
        </div>
      </div>
    </div>

    <div class="charts-row">
      <div class="chart-panel glass-panel">
        <div class="panel-header">
          <h3>Recent Activity Logs</h3>
        </div>
        <div class="panel-body list-view">
          <div class="log-item" v-for="i in 5" :key="i">
            <div class="log-indicator"></div>
            <div class="log-details">
              <span class="log-title">New grade posted by Dr. Smith</span>
              <span class="log-time">{{ i * 15 }} mins ago</span>
            </div>
          </div>
        </div>
      </div>
      
      <div class="chart-panel glass-panel">
        <div class="panel-header">
          <h3>System Health</h3>
        </div>
        <div class="panel-body center-flex">
          <div class="health-ring">
            <span class="score">99%</span>
            <span class="label">Uptime</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 24px;
  height: 100%;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 24px;
}

.stat-card {
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  transition: transform 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  background: rgba(255, 255, 255, 0.05);
}

.stat-icon.info { color: var(--accent-info); border: 1px solid rgba(59, 130, 246, 0.2); }
.stat-icon.primary { color: var(--primary); border: 1px solid rgba(99, 102, 241, 0.2); }
.stat-icon.success { color: var(--accent-success); border: 1px solid rgba(16, 185, 129, 0.2); }
.stat-icon.warning { color: var(--accent-warning); border: 1px solid rgba(245, 158, 11, 0.2); }

.stat-content h3 {
  font-size: 0.85rem;
  color: var(--text-muted);
  font-weight: 500;
  margin-bottom: 4px;
}

.stat-content .value {
  font-size: 1.75rem;
  font-weight: 700;
  margin-bottom: 4px;
}

.stat-content .trend {
  font-size: 0.75rem;
}

.trend.positive { color: var(--accent-success); }
.trend.negative { color: var(--accent-danger); }
.trend.neutral { color: var(--text-muted); }

.charts-row {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
  flex: 1;
}

.chart-panel {
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  overflow: hidden;
}

.panel-header {
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
}

.panel-header h3 {
  font-size: 1.1rem;
  font-weight: 600;
}

.panel-body {
  padding: 24px;
  flex: 1;
  overflow-y: auto;
}

.center-flex {
  display: flex;
  align-items: center;
  justify-content: center;
}

.health-ring {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  border: 12px solid var(--accent-success);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  box-shadow: inset 0 0 20px rgba(16, 185, 129, 0.2), 0 0 20px rgba(16, 185, 129, 0.2);
}

.health-ring .score {
  font-size: 2rem;
  font-weight: 700;
}

.health-ring .label {
  font-size: 0.8rem;
  color: var(--text-muted);
}

.list-view {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.log-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.02);
  transition: background 0.3s ease;
}

.log-item:hover {
  background: rgba(255, 255, 255, 0.05);
}

.log-indicator {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--primary);
  box-shadow: 0 0 10px var(--primary);
}

.log-details {
  display: flex;
  flex-direction: column;
}

.log-title {
  font-weight: 500;
  font-size: 0.95rem;
}

.log-time {
  font-size: 0.75rem;
  color: var(--text-muted);
}
</style>
