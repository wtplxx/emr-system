<template>
  <div class="page-box">
    <div class="dashboard-header">
      <div>
        <h3>医生工作台</h3>
        <p class="greeting">欢迎回来，{{ stats.doctorName || '医生' }} | {{ stats.hospital }} {{ stats.office }}</p>
      </div>
    </div>

    <el-row :gutter="20" style="margin-bottom:24px">
      <el-col :span="6" v-for="card in statCards" :key="card.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value">{{ card.value }}</div>
          <div class="stat-label">{{ card.label }}</div>
          <div class="stat-icon" :style="{ color: card.color }">{{ card.icon }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="section-card">
      <div slot="header" class="section-header">
        <span>📋 今日患者 ({{ todayAppts.length }})</span>
        <el-tag size="small" type="warning">待就诊: {{ pendingCount }}</el-tag>
      </div>
      <el-table :data="todayAppts" border stripe v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="挂号编号" width="90"></el-table-column>
        <el-table-column label="患者姓名" width="100">
          <template slot-scope="scope">{{ patientNames[scope.row.patientId] || '加载中...' }}</template>
        </el-table-column>
        <el-table-column prop="timeSlot" label="时段" width="70"></el-table-column>
        <el-table-column prop="chiefComplaint" label="主诉" min-width="200"></el-table-column>
        <el-table-column label="状态" width="90">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 0 ? 'warning' : 'success'" size="small">
              {{ scope.row.status === 0 ? '待就诊' : '已完成' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="showDetail(scope.row)">查看详情</el-button>
            <el-button type="text" size="small" @click="quickCreate(scope.row)">快速建档</el-button>
            <el-button v-if="scope.row.status === 0" type="text" size="small" style="color:#67C23A" @click="completeAppt(scope.row)">接诊</el-button>
            <el-button v-if="scope.row.status === 0" type="text" size="small" style="color:#F56C6C" @click="cancelAppt(scope.row)">取消</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="empty-state" v-if="!loading && todayAppts.length === 0">
        <el-empty description="今日暂无挂号患者"></el-empty>
      </div>
    </el-card>

    <!-- 查看详情弹窗 -->
    <el-dialog title="患者详情" :visible.sync="detailDialog" width="420px">
      <div class="info-card" v-if="detailRow">
        <div class="info-row"><span class="label">挂号编号：</span><span>{{ detailRow.id }}</span></div>
        <div class="info-row"><span class="label">患者姓名：</span><span>{{ patientNames[detailRow.patientId] || '—' }}</span></div>
        <div class="info-row"><span class="label">就诊科室：</span><span>{{ detailRow.office }}</span></div>
        <div class="info-row"><span class="label">就诊日期：</span><span>{{ detailRow.appointmentDate }}</span></div>
        <div class="info-row"><span class="label">时间段：</span><span>{{ detailRow.timeSlot }}</span></div>
        <div class="info-row"><span class="label">主诉：</span><span>{{ detailRow.chiefComplaint || '—' }}</span></div>
      </div>
      <span slot="footer">
        <el-button @click="detailDialog = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'DoctorDashboard',
  data() {
    return {
      loading: false,
      stats: {},
      todayAppts: [],
      patientNames: {},
      detailDialog: false,
      detailRow: null
    }
  },
  computed: {
    statCards() {
      return [
        { label: '今日挂号', value: this.stats.todayAppointments || 0, icon: '📋', color: '#409EFF' },
        { label: '待就诊', value: this.stats.pendingAppointments || 0, icon: '⏳', color: '#E6A23C' },
        { label: '总病历数', value: this.stats.totalRecords || 0, icon: '📄', color: '#67C23A' },
        { label: '已确认', value: this.stats.confirmedRecords || 0, icon: '✅', color: '#909399' }
      ]
    },
    pendingCount() {
      return this.todayAppts.filter(a => a.status === 0).length
    }
  },
  created() {
    const user = this.$store.state.user
    if (!user) { this.$router.push('/login'); return }
    this.fetchDashboard()
  },
  methods: {
    fetchDashboard() {
      this.loading = true
      const doctorId = this.$store.state.user.id
      Promise.all([
        this.$axios.get('/dashboard/doctor/' + doctorId),
        this.$axios.get('/appointment/doctor/' + doctorId, { params: { status: 0 } })
      ]).then(([statsResp, apptResp]) => {
        this.stats = statsResp.data || {}
        this.todayAppts = apptResp.data || []
        this.loadPatientNames()
      }).catch(() => this.$message.error('获取数据失败'))
        .finally(() => { this.loading = false })
    },
    loadPatientNames() {
      const ids = [...new Set(this.todayAppts.map(a => a.patientId))]
      ids.forEach(id => {
        this.$axios.get('/patient/info/' + id).then(resp => {
          if (resp.data) this.$set(this.patientNames, id, resp.data.name || '患者')
        }).catch(() => {})
      })
    },
    completeAppt(row) {
      this.$confirm('确认该患者已完成就诊？', '提示').then(() => {
        this.$axios.post('/appointment/status/' + row.id, { status: 1 }).then(() => {
          this.$message.success('已标记为已完成')
          this.fetchDashboard()
        }).catch(() => this.$message.error('操作失败'))
      }).catch(() => {})
    },
    cancelAppt(row) {
      this.$confirm('取消该挂号？', '提示', { type: 'warning' }).then(() => {
        this.$axios.post('/appointment/status/' + row.id, { status: 2 }).then(() => {
          this.$message.info('已取消')
          this.fetchDashboard()
        }).catch(() => this.$message.error('操作失败'))
      }).catch(() => {})
    },
    showDetail(row) {
      this.detailRow = row
      this.detailDialog = true
    },
    quickCreate(row) {
      const patientName = this.patientNames[row.patientId] || ''
      this.$router.push({ path: '/doctor/create', query: { patientName: patientName } })
    }
  }
}
</script>

<style scoped>
.page-box { padding: 24px; }
.dashboard-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
.dashboard-header h3 { margin: 0; font-size: 22px; color: #303133; }
.greeting { color: #909399; font-size: 14px; margin: 6px 0 0 0; }
.stat-card { position: relative; overflow: hidden; }
.stat-value { font-size: 36px; font-weight: 700; color: #303133; }
.stat-label { font-size: 14px; color: #909399; margin-top: 4px; }
.stat-icon { position: absolute; right: 20px; top: 20px; font-size: 40px; opacity: 0.3; }
.section-card { margin-top: 8px; }
.section-header { display: flex; justify-content: space-between; align-items: center; }
.empty-state { padding: 40px 0; }
.info-card { background: #f5f7fa; border-radius: 8px; padding: 16px 20px; }
.info-row { line-height: 30px; font-size: 14px; }
.info-row .label { color: #909399; display: inline-block; width: 80px; text-align: right; margin-right: 12px; }
</style>
