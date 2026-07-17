<template>
  <div class="layout-container">
    <!-- 左侧菜单 -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>健康档案管理</h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        class="sidebar-menu"
        background-color="#1d2a3a"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/doctor/dashboard">
          <i class="el-icon-s-home"></i>
          <span>工作台</span>
        </el-menu-item>
        <el-menu-item index="/doctor/records">
          <i class="el-icon-document"></i>
          <span>档案检索</span>
          <el-badge :value="badgeCounts.recordPending" :hidden="badgeCounts.recordPending === 0" class="menu-badge" />
        </el-menu-item>
        <el-menu-item index="/doctor/create">
          <i class="el-icon-plus"></i>
          <span>新建档案</span>
        </el-menu-item>
        <el-menu-item index="/personal">
          <i class="el-icon-user"></i>
          <span>个人中心</span>
        </el-menu-item>
      </el-menu>
      <div class="sidebar-footer">
        <span>{{ user?.name || '医生' }}</span>
        <el-button type="text" class="logout-btn" @click="handleLogout">退出</el-button>
      </div>
    </aside>

    <!-- 右侧内容区 -->
    <main class="main-area">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: 'DoctorLayout',
  data() {
    return {
      badgeCounts: { recordPending: 0 }
    }
  },
  computed: {
    user() { return this.$store.state.user },
    activeMenu() { return this.$route.path }
  },
  created() {
    this.fetchBadgeCounts()
  },
  watch: {
    '$route'() { this.fetchBadgeCounts() }
  },
  methods: {
    handleLogout() {
      this.$store.commit('clearUser')
      this.$router.push('/login')
    },
    fetchBadgeCounts() {
      this.$axios.get('/record/count', { params: { affirm: 0 } }).then(resp => {
        this.badgeCounts.recordPending = (resp.data && resp.data.count) || 0
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.layout-container { display: flex; min-height: 100vh; }
.sidebar {
  width: 220px;
  background: #1d2a3a;
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0; left: 0; bottom: 0;
  z-index: 100;
}
.sidebar-header {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}
.sidebar-header h2 {
  color: #fff; font-size: 18px; margin: 0; letter-spacing: 2px;
}
.sidebar-menu { flex: 1; border-right: none; }
.sidebar-footer {
  padding: 16px 20px;
  border-top: 1px solid rgba(255,255,255,0.1);
  color: #bfcbd9; font-size: 14px;
  display: flex; justify-content: space-between; align-items: center;
}
.logout-btn { color: #bfcbd9 !important; font-size: 13px; }
.logout-btn:hover { color: #409EFF !important; }
.main-area {
  flex: 1; margin-left: 220px;
  background: #f0f2f5; min-height: 100vh;
}
.menu-badge { margin-left: 12px; }
.menu-badge >>> .el-badge__content { margin-top: 10px; }
</style>