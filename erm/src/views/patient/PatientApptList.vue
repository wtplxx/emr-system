<template>
  <div class="page-box">
    <div class="page-header"><h3>我的挂号</h3></div>

    <div class="table-card">
      <el-table :data="apptList" border stripe v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="挂号编号" width="90"></el-table-column>
        <el-table-column prop="office" label="就诊科室" width="100"></el-table-column>
        <el-table-column label="就诊日期" width="120">
          <template slot-scope="scope">{{ scope.row.appointmentDate }}</template>
        </el-table-column>
        <el-table-column prop="timeSlot" label="时间段" width="80"></el-table-column>
        <el-table-column prop="chiefComplaint" label="主诉" min-width="180"></el-table-column>
        <el-table-column label="状态" width="90">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 0 ? 'warning' : scope.row.status === 1 ? 'success' : 'info'" size="small">
              {{ scope.row.status === 0 ? '待就诊' : scope.row.status === 1 ? '已完成' : '已取消' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status === 0" type="text" size="small" style="color:#F56C6C" @click="cancelAppt(scope.row)">取消挂号</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="empty-state" v-if="!loading && apptList.length === 0">
        <el-empty description="暂无挂号记录">
          <el-button type="primary" @click="$router.push('/patient/appointment')">去挂号</el-button>
        </el-empty>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PatientApptList',
  data() {
    return { apptList: [], loading: false }
  },
  created() {
    const user = this.$store.state.user
    if (!user) { this.$router.push('/login'); return }
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.loading = true
      this.$axios.get('/appointment/my/' + this.$store.state.user.id).then(resp => {
        this.apptList = resp.data || []
      }).catch(() => this.$message.error('获取挂号记录失败'))
        .finally(() => { this.loading = false })
    },
    cancelAppt(row) {
      this.$confirm('确定取消该挂号？取消后无法恢复。', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => {
        this.$axios.post('/appointment/status/' + row.id, { status: 2 }).then(() => {
          this.$message.success('挂号已取消')
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
.table-card { background: #fff; border-radius: 8px; padding: 16px; }
.empty-state { padding: 40px 0; }
</style>
