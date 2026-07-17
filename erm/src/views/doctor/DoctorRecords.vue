<template>
  <div class="page-box">
    <!-- 页面标题 -->
    <div class="page-header">
      <h3>档案检索</h3>
    </div>

    <!-- 搜索表单 -->
    <div class="search-card">
      <el-form :inline="true" :model="searchForm" size="medium">
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
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 结果表格 -->
    <div class="table-card">
      <el-table :data="tableData" border stripe v-loading="loading" style="width:100%">
        <el-table-column prop="id" label="档案编号" width="100"></el-table-column>
        <el-table-column prop="patientName" label="患者姓名" width="120"></el-table-column>
        <el-table-column prop="patientPhone" label="联系电话" width="140"></el-table-column>
        <el-table-column prop="doctorOffice" label="就诊科室" width="120"></el-table-column>
        <el-table-column prop="doctorHospital" label="就诊医院" width="150"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template slot-scope="scope">{{ scope.row.createTime | timeFormat }}</template>
        </el-table-column>
        <el-table-column label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.affirm === 1 ? 'success' : 'info'" size="small">
              {{ scope.row.affirm === 1 ? '已确认' : '待确认' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleApply(scope.row)">申请授权</el-button>
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" style="color:#67C23A" @click="handleConfirm(scope.row)" v-if="scope.row.affirm !== 1">确认</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 空状态 -->
      <div class="empty-state" v-if="!loading && tableData.length === 0">
        <el-empty description="暂无数据"></el-empty>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrap" v-if="total > 0">
        <el-pagination
          background
          layout="total, prev, pager, next, jumper"
          :current-page="pageNo"
          :page-size="pageSize"
          :total="total"
          @current-change="handlePageChange">
        </el-pagination>
      </div>
    </div>

    <!-- 申请授权弹窗 -->
    <el-dialog title="申请授权" :visible.sync="authDialog" width="420px">
      <div>
        <p>确认向以下档案发起授权申请？</p>
        <div class="info-card">
          <div class="info-row"><span class="label">档案编号：</span><span>{{ authTarget?.id }}</span></div>
          <div class="info-row"><span class="label">患者姓名：</span><span>{{ authTarget?.patientName }}</span></div>
          <div class="info-row"><span class="label">就诊科室：</span><span>{{ authTarget?.doctorOffice }}</span></div>
        </div>
        <p style="color:#909399;font-size:13px">申请后等待患者确认，通过后即可查看完整档案。</p>
      </div>
      <span slot="footer">
        <el-button @click="authDialog = false">取消</el-button>
        <el-button type="primary" @click="submitApply" :loading="applying">确认申请</el-button>
      </span>
    </el-dialog>

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
        <div class="info-row"><span class="label">医生诊断：</span><span>{{ viewData.description || '—' }}</span></div>
        <div class="info-row"><span class="label">备注：</span><span>{{ viewData.remark || '—' }}</span></div>
        <div class="info-row"><span class="label">创建时间：</span><span>{{ viewData.createTime }}</span></div>
        <div class="info-row"><span class="label">状态：</span>
          <el-tag :type="viewData.affirm === 1 ? 'success' : 'info'" size="small">
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
  name: 'DoctorRecords',
  data() {
    return {
      searchForm: { name: '', phone: '', office: '' },
      tableData: [],
      loading: false,
      pageNo: 1,
      pageSize: 10,
      total: 0,
      authDialog: false,
      authTarget: null,
      applying: false,
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
      const params = { ...this.searchForm, pageNo: this.pageNo, pageSize: this.pageSize }
      this.$axios.get('/record/search', { params }).then(resp => {
        const data = resp.data || {}
        this.tableData = data.records || []
        this.total = data.total || 0
      }).catch(() => {
        this.$message.error('查询失败')
      }).finally(() => { this.loading = false })
    },
    handleSearch() { this.pageNo = 1; this.fetchData() },
    handleReset() { this.searchForm = { name: '', phone: '', office: '' }; this.pageNo = 1; this.fetchData() },
    handlePageChange(page) { this.pageNo = page; this.fetchData() },
    handleApply(row) {
      this.authTarget = row
      this.authDialog = true
    },
    submitApply() {
      this.applying = true
      this.$axios.post('/authorize/apply', {
        doctorId: this.$store.state.user.id,
        patientId: this.authTarget.patientId,
        recordId: this.authTarget.id
      }).then(resp => {
        if (resp.data && resp.data.data === 'already_applied') {
          this.$message.warning('已申请过授权，请勿重复申请')
        } else {
          this.$message.success('授权申请已发送，等待患者确认')
        }
        this.authDialog = false
      }).catch(() => {
        this.$message.error('申请失败')
      }).finally(() => { this.applying = false })
    },
    handleConfirm(row) {
      this.$confirm('确认该档案信息无误？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => {
        this.$axios.post('/record/confirm/' + row.id).then(resp => {
          if (resp.data && resp.data.code === 0) {
            this.$message.success('已确认')
            this.fetchData()
          } else {
            this.$message.error('确认失败')
          }
        }).catch(() => this.$message.error('确认失败'))
      }).catch(() => {})
    },
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
.pagination-wrap { display: flex; justify-content: flex-end; padding-top: 16px; }
.info-card { background: #f5f7fa; border-radius: 8px; padding: 16px 20px; }
.info-row { line-height: 30px; font-size: 14px; }
.info-row .label { color: #909399; display: inline-block; width: 80px; text-align: right; margin-right: 12px; }
</style>
