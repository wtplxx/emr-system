<template>
  <div class="home-container">
    <header class="top-bar">
      <div class="top-bar-left">
        <h2>健康档案管理系统</h2>
      </div>
      <div class="top-bar-right">
        <span class="user-info">
          👨‍⚕️ {{ user?.name || '未知用户' }}
          <span class="user-dept" v-if="user?.hospital"> | {{ user.hospital }} {{ user.office }}</span>
        </span>
        <el-button type="text" class="logout-btn" @click="handleLogout">退出登录</el-button>
      </div>
    </header>

    <main class="main-content">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="档案检索" name="search">
          <div class="search-box">
            <el-form :inline="true" :model="searchForm" size="small">
              <el-form-item label="姓名">
                <el-input v-model="searchForm.name" placeholder="患者姓名" style="width:160px" clearable></el-input>
              </el-form-item>
              <el-form-item label="电话">
                <el-input v-model="searchForm.phone" placeholder="联系电话" style="width:160px" clearable></el-input>
              </el-form-item>
              <el-form-item label="科室">
                <el-input v-model="searchForm.office" placeholder="就诊科室" style="width:160px" clearable></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="fetchRecords">查询</el-button>
              </el-form-item>
            </el-form>
          </div>
          <el-table :data="recordList" border stripe v-loading="loading" style="width: 100%">
            <el-table-column prop="id" label="档案编号" width="80"></el-table-column>
            <el-table-column prop="patientName" label="患者姓名" width="100"></el-table-column>
            <el-table-column prop="patientPhone" label="电话" width="130"></el-table-column>
            <el-table-column prop="doctorHospital" label="就诊医院" width="150"></el-table-column>
            <el-table-column prop="doctorOffice" label="就诊科室" width="120"></el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180">
              <template slot-scope="scope">{{ scope.row.createTime?.substring(0, 19).replace('T', ' ') }}</template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </main>
  </div>
</template>

<script>
export default {
  name: 'HomeView',
  data() {
    return {
      activeTab: 'search',
      searchForm: { name: '', phone: '', office: '' },
      recordList: [],
      loading: false
    }
  },
  computed: {
    user() { return this.$store.state.user }
  },
  created() {
    if (!this.user) { this.$router.push('/login'); return }
    this.fetchRecords()
  },
  methods: {
    fetchRecords() {
      this.loading = true
      this.$axios.get('/record/search', { params: this.searchForm }).then(resp => {
        this.recordList = resp.data || []
      }).catch(err => {
        console.error('获取数据失败', err)
      }).finally(() => { this.loading = false })
    },
    handleLogout() {
      this.$store.commit('clearUser')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.home-container { min-height: 100vh; background: #f0f2f5; }
.top-bar {
  display: flex; justify-content: space-between; align-items: center;
  background: #409EFF; color: #fff; padding: 0 30px; height: 60px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.top-bar-left h2 { margin: 0; font-size: 20px; letter-spacing: 2px; font-weight: 500; }
.top-bar-right { display: flex; align-items: center; gap: 20px; }
.user-info { font-size: 15px; }
.user-dept { font-size: 13px; opacity: 0.85; }
.logout-btn { color: #fff !important; font-size: 14px; }
.logout-btn:hover { opacity: 0.8; }
.main-content { padding: 24px; max-width: 1200px; margin: 0 auto; }
.search-box { margin-bottom: 16px; }
</style>
