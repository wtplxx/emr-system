<template>
  <header class="app-header">
    <div class="header-left">
      <h2>健康档案管理系统</h2>
    </div>
    <div class="header-right">
      <span class="user-info">👨‍⚕️ {{ user?.name || '未知用户' }}</span>
      <el-button type="text" class="header-btn" @click="$router.push('/personal')">个人中心</el-button>
      <el-button type="text" class="header-btn" @click="handleLogout">退出</el-button>
    </div>
  </header>
</template>

<script>
export default {
  name: 'AppHeader',
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  methods: {
    handleLogout() {
      this.$store.commit('clearUser')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(135deg, #409EFF, #2d7fd3);
  color: #fff;
  padding: 0 30px;
  height: 60px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.12);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}
.header-left h2 {
  margin: 0;
  font-size: 20px;
  letter-spacing: 2px;
  font-weight: 600;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}
.user-info {
  font-size: 15px;
}
.header-btn {
  color: #fff !important;
  font-size: 14px;
}
.header-btn:hover { opacity: 0.8; }
</style>