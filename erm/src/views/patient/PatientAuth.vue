<template>
  <div class="page-box">
    <div class="page-header">
      <h3>授权管理</h3>
    </div>

    <div class="table-card">
      <el-table :data="authList" border stripe v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="申请编号" width="90"></el-table-column>
        <el-table-column label="医生姓名" width="120">
          <template slot-scope="scope">{{ doctorMap[scope.row.doctorId]?.name || '—' }}</template>
        </el-table-column>
        <el-table-column label="所属医院" width="150">
          <template slot-scope="scope">{{ doctorMap[scope.row.doctorId]?.hospital || '—' }}</template>
        </el-table-column>
        <el-table-column label="科室" width="120">
          <template slot-scope="scope">{{ doctorMap[scope.row.doctorId]?.office || '—' }}</template>
        </el-table-column>
        <el-table-column prop="recordId" label="档案编号" width="100"></el-table-column>
        <el-table-column label="申请时间" width="180">
          <template slot-scope="scope">{{ scope.row.authTime | timeFormat }}</template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="success" size="small" @click="handleApprove(scope.row)">同意</el-button>
            <el-button type="danger" size="small" @click="handleReject(scope.row)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="empty-state" v-if="!loading && authList.length === 0">
        <el-empty description="暂无授权申请"></el-empty>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PatientAuth',
  data() {
    return {
      authList: [],
      doctorMap: {},
      loading: false
    }
  },
  created() {
    if (!this.$store.state.user) { this.$router.push('/login'); return }
    this.fetchData()
  },
  filters: {
    timeFormat(val) {
      if (!val) return ''
      return val.substring(0, 19).replace('T', ' ')
    }
  },
  methods: {
    fetchData() {
      this.loading = true
      const userId = this.$store.state.user.id
      this.$axios.get('/authorize/pending/' + userId).then(resp => {
        this.authList = resp.data || []
        this.loadDoctorInfo()
      }).catch(() => {
        this.$message.error('获取授权列表失败')
      }).finally(() => { this.loading = false })
    },
    loadDoctorInfo() {
      const doctorIds = [...new Set(this.authList.map(a => a.doctorId))]
      Promise.all(doctorIds.map(id => {
        return this.$axios.get('/doctor/info/' + id).then(resp => {
          this.$set(this.doctorMap, id, resp.data)
        }).catch(() => {})
      }))
    },
    handleApprove(row) {
      this.$axios.post('/authorize/approve/' + row.id).then(() => {
        this.$message.success('已同意授权')
        this.fetchData()
      }).catch(() => this.$message.error('操作失败'))
    },
    handleReject(row) {
      this.$confirm('确定拒绝该授权申请？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => {
        this.$axios.post('/authorize/reject/' + row.id).then(() => {
          this.$message.success('已拒绝授权')
          this.fetchData()
        }).catch(() => this.$message.error('操作失败'))
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.page-box { padding: 24px; }
.page-header { margin-bottom: 16px; }
.page-header h3 { margin: 0; font-size: 20px; color: #303133; }
.table-card { background: #fff; border-radius: 8px; padding: 16px; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.empty-state { padding: 40px 0; }
</style>
