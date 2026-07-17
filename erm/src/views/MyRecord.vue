<template>
  <div class="app-container">
    <div class="page-content">
      <div class="page-header">
        <h3>我的档案</h3>
        <el-button type="primary" icon="el-icon-s-platform" @click="$router.push('/patient/auth')">授权管理</el-button>
      </div>

      <div class="filter-card">
        <el-form :inline="true" :model="filter">
          <el-form-item label="科室">
            <el-input v-model="filter.office" placeholder="请输入科室" style="width:200px"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <div class="table-card">
        <el-table :data="tableData" border stripe style="width:100%" v-loading="loading">
          <el-table-column prop="id" label="档案编号" width="100"></el-table-column>
          <el-table-column prop="doctorHospital" label="就诊医院" min-width="150"></el-table-column>
          <el-table-column prop="doctorOffice" label="就诊科室" width="150"></el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180">
            <template slot-scope="scope">{{ scope.row.createTime | timeFormat }}</template>
          </el-table-column>
          <el-table-column label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.affirm === 1 ? 'success' : 'warning'" size="small">
                {{ scope.row.affirm === 1 ? '已确认' : '待确认' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="empty-state" v-if="!loading && tableData.length === 0">
          <el-empty description="暂无数据"></el-empty>
        </div>
      </div>
    </div>

    <!-- 查看档案弹窗 -->
    <el-dialog title="档案详情" :visible.sync="viewDialog" width="520px">
      <div class="info-card" v-if="viewData">
        <div class="info-row"><span class="label">档案编号：</span><span>{{ viewData.id }}</span></div>
        <div class="info-row"><span class="label">患者姓名：</span><span>{{ viewData.patientName }}</span></div>
        <div class="info-row"><span class="label">就诊医院：</span><span>{{ viewData.doctorHospital }}</span></div>
        <div class="info-row"><span class="label">就诊科室：</span><span>{{ viewData.doctorOffice }}</span></div>
        <div class="info-row"><span class="label">医生诊断：</span><span>{{ viewData.description || '—' }}</span></div>
        <div class="info-row"><span class="label">备注：</span><span>{{ viewData.remark || '—' }}</span></div>
        <div class="info-row"><span class="label">创建时间：</span><span>{{ viewData.createTime }}</span></div>
      </div>
      <span slot="footer">
        <el-button @click="viewDialog = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'MyRecord',
  data() {
    return {
      filter: { office: '' },
      tableData: [],
      loading: false,
      viewDialog: false,
      viewData: null
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
      const user = this.$store.state.user
      this.$axios.get('/record/search', {
        params: { phone: String(user.phone), office: this.filter.office }
      }).then(resp => {
        this.tableData = resp.data || []
      }).catch(err => {
        console.error('获取数据失败', err)
      }).finally(() => { this.loading = false })
    },
    handleQuery() { this.fetchData() },
    handleReset() { this.filter.office = ''; this.fetchData() },
    handleView(row) {
      this.viewData = row
      this.viewDialog = true
    }
  }
}
</script>

<style scoped>
.app-container { min-height: 100vh; background: #f0f2f5; }
.page-content { max-width: 1200px; margin: 0 auto; padding: 24px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h3 { margin: 0; font-size: 20px; color: #303133; }
.filter-card { background: #fff; border-radius: 8px; padding: 16px 20px; margin-bottom: 16px; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.table-card { background: #fff; border-radius: 8px; padding: 16px; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.empty-state { padding: 40px 0; }
.info-card { background: #f5f7fa; border-radius: 8px; padding: 16px 20px; }
.info-row { line-height: 30px; font-size: 14px; }
.info-row .label { color: #909399; display: inline-block; width: 80px; text-align: right; margin-right: 12px; }
</style>
