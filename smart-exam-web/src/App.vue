<script setup>
import { useRoute, useRouter } from 'vue-router'
import { computed } from 'vue'

const route = useRoute()
const router = useRouter()

const activeMenu = computed(() => route.path)

const handleMenuSelect = (index) => {
  if (index !== route.path) {
    router.push(index)
  }
}
</script>

<template>
  <el-container class="app-layout">
    <el-header class="app-header">
      <div class="logo-area">
        <span class="logo-text">SmartExam</span>
        <span class="logo-sub">智能考试管理系统</span>
      </div>
      <el-menu
        mode="horizontal"
        :default-active="activeMenu"
        :ellipsis="false"
        @select="handleMenuSelect"
        class="app-menu"
      >
        <el-menu-item index="/dashboard">可视化大屏</el-menu-item>
        <el-menu-item index="/subjects">课程管理</el-menu-item>
        <el-menu-item index="/questions">题库管理</el-menu-item>
        <el-menu-item index="/paper">组卷中心</el-menu-item>
      </el-menu>
    </el-header>

    <el-main class="app-main">
      <router-view />
    </el-main>
  </el-container>
</template>

<style scoped>
.app-layout {
  min-height: 100vh;
  background: #f5f7fa;
}

.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 0 32px;
}

.logo-area {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
}

.logo-sub {
  font-size: 12px;
  color: #909399;
}

.app-menu {
  border-bottom: none;
}

.app-main {
  padding: 20px 24px;
}
</style>
