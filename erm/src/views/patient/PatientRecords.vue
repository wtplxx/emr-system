<template>
  <div class="page-box">
    <!-- 页面标题 -->
    <div class="page-header">
      <h3>我的档案</h3>
    </div>

    <!-- 筛选栏 -->
    <div class="search-card">
      <el-form :inline="true" :model="searchForm" size="medium">
        <el-form-item label="就诊科室">
          <el-input v-model="searchForm.office" placeholder="请输入科室" style="width:160px" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-card">
      <el-table :data="tableData" border stripe v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="档案编号" width="100"></el-table-column>
        <el-table-column prop="doctorHospital" label="就诊医院" width="160"></el-table-column>
        <el-table-column prop="doctorOffice" label="就诊科室" width="120"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template slot-scope="scope">{{ scope.row.createTime | timeFormat }}</template>
        </el-table-column>
        <el-table-column label="状态" width="90">
          <template slot-scope="scope">
            <el-tag :type="scope.row.affirm === 1 ? 'success' : 'warning'" size="small">
              {{ scope.row.affirm === 1 ? '已确认' : '待确认' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空状态 -->
      <div class="empty-state" v-if="!loading && tableData.length === 0">
        <el-empty description="暂无数据"></el-empty>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrap" v-if="tableData.length > 0">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </div>

    <!-- 查看档案弹窗 -->
    <el-dialog title="档案详情" :visible.sync="viewDialog" width="520px">
      <div class="info-card" v-if="viewData">
        <div class="info-row"><span class="label">档案编号：</span><span>{{ viewData.id }}</span></div>
        <div class="info-row"><span class="label">患者姓名：</span><span>{{ viewData.patientName }}</span></div>
        <div class="info-row"><span class="label">联系电话：</span><span>{{ viewData.patientPhone }}</span></div>
        <div class="info-row"><span class="label">性别：</span><span>{{ viewData.patientSex === 1 ? '男' : '女' }}</span></div>
        <div class="info-row"><span class="label">年龄：</span><span>{{ viewData.patientAge }}</span></div>
        <div class="info-row"><span class="label">就诊医院：</span><span>{{ viewData.doctorHospital }}</span></div>
        <div class="info-row"><span class="label">就诊科室：</span><span>{{ viewData.doctorOffice }}</span></div>
        <div class="info-row"><span class="label">主诉：</span><span>{{ viewData.chiefComplaint || '—' }}</span></div>
        <div class="info-row"><span class="label">现病史：</span><span>{{ viewData.history || '—' }}</span></div>
        <div class="info-row"><span class="label">体格检查：</span><span>{{ viewData.physicalExam || '—' }}</span></div>
        <div class="info-row"><span class="label">辅助检查：</span><span>{{ viewData.examination || '—' }}</span></div>
        <div class="info-row"><span class="label">医生诊断：</span><span>{{ viewData.description || '—' }}</span></div>
        <div class="info-row"><span class="label">处方：</span><span>{{ viewData.prescription || '—' }}</span></div>
        <div class="info-row"><span class="label">随访建议：</span><span>{{ viewData.followup || '—' }}</span></div>
        <div class="info-row"><span class="label">备注：</span><span>{{ viewData.remark || '—' }}</span></div>
        <div class="info-row"><span class="label">创建时间：</span><span>{{ viewData.createTime }}</span></div>
        <div class="info-row"><span class="label">状态：</span>
          <el-tag :type="viewData.affirm === 1 ? 'success' : 'warning'" size="small">
            {{ viewData.affirm === 1 ? '已确认' : '待确认' }}
          </el-tag>
        </div>
      </div>
      <span slot="footer">
        <el-button @click="viewDialog = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PatientRecords',
  data() {
    return {
      searchForm: { office: '' },
      tableData: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
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
      const params = {
        phone: String(user.phone),
        office: this.searchForm.office,
        pageNo: this.currentPage,
        pageSize: this.pageSize
      }
      this.$axios.get('/record/search', { params }).then(resp => {
        const data = resp.data || {}
        this.tableData = data.records || []
        this.total = data.total || 0
      }).catch(() => {
        this.$message.error('查询失败')
      }).finally(() => { this.loading = false })
    },
    handleSearch() { this.currentPage = 1; this.fetchData() },
    handleReset() { this.searchForm = { office: '' }; this.currentPage = 1; this.fetchData() },
    handleSizeChange(val) { this.pageSize = val; this.currentPage = 1; this.fetchData() },
    handleCurrentChange(val) { this.currentPage = val; this.fetchData() },
    handleView(row) {
      this.viewData = row
      this.viewDialog = true
    }
  }
}
</script>

<style scoped>
.page-box { padding: 24px; }
.page-header { margin-bottom: 16px; }
.page-header h3 { margin: 0; font-size: 20px; color: #303133; }
.search-card { background: #fff; border-radius: 8px; padding: 16px 20px; margin-bottom: 16px; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.table-card { background: #fff; border-radius: 8px; padding: 16px; box-shadow: 0 1px 4px rgba(0,0,0,0.04); }
.empty-state { padding: 40px 0; }
.pagination-wrap { margin-top: 16px; text-align: right; }
.info-card { background: #f5f7fa; border-radius: 8px; padding: 16px 20px; }
.info-row { line-height: 30px; font-size: 14px; }
.info-row .label { color: #909399; display: inline-block; width: 80px; text-align: right; margin-right: 12px; }
</style>
