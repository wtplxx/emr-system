<template>
  <div class="app-container">

    <!-- 顶部返回栏 -->
    <div class="back-bar">
      <el-button type="text" icon="el-icon-arrow-left" @click="goBack">返回健康档案</el-button>
    </div>

    <div class="page-content">
      <div class="profile-card">
        <!-- 身份摘要 -->
        <div class="profile-header">
          <h2 class="user-name">{{ user?.name || '—' }}</h2>
          <p class="user-role">{{ user?.role === 'patient' ? '患者' : '医生' }}</p>
        </div>

        <!-- 详细信息 -->
        <el-form label-position="right" label-width="120px" class="info-grid">
          <el-row :gutter="40">
            <el-col :span="12">
              <el-form-item label="工号/ID">
                <span>{{ user?.id || '—' }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="所属医院">
                <span>{{ user?.hospital || '—' }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="40">
            <el-col :span="12">
              <el-form-item label="联系电话">
                <span>{{ user?.phone || '—' }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="就诊科室">
                <span>{{ user?.office || '—' }}</span>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

        <!-- 底部按钮 -->
        <div class="profile-actions">
          <el-button type="primary" @click="editDialog = true">编辑资料</el-button>
          <el-button @click="pwdDialog = true">修改密码</el-button>
        </div>
      </div>
    </div>

    <!-- 编辑资料弹窗 -->
    <el-dialog title="编辑资料" :visible.sync="editDialog" width="500px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="editForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="所属医院">
          <el-input v-model="editForm.hospital"></el-input>
        </el-form-item>
        <el-form-item label="就诊科室">
          <el-input v-model="editForm.office"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="editDialog = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </span>
    </el-dialog>

    <!-- 修改密码弹窗 -->
    <el-dialog title="修改密码" :visible.sync="pwdDialog" width="400px">
      <el-form :model="pwdForm" label-width="100px">
        <el-form-item label="原密码">
          <el-input v-model="pwdForm.oldPassword" show-password></el-input>
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPassword" show-password></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="pwdForm.confirmPassword" show-password></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="pwdDialog = false">取消</el-button>
        <el-button type="primary" @click="savePwd">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'PersonalCenter',
  data() {
    return {
      editDialog: false,
      pwdDialog: false,
      editForm: { name: '', phone: '', hospital: '', office: '' },
      pwdForm: { oldPassword: '', newPassword: '', confirmPassword: '' }
    }
  },
  computed: {
    user() { return this.$store.state.user }
  },
  created() {
    if (!this.user) { this.$router.push('/login'); return }
    this.editForm = {
      name: this.user.name || '',
      phone: this.user.phone || '',
      hospital: this.user.hospital || '',
      office: this.user.office || ''
    }
  },
  methods: {
    saveEdit() {
      const role = this.user?.role
      const url = role === 'patient' ? '/patient/update' : '/doctor/update'
      this.$axios.post(url, {
        ...this.editForm,
        id: this.user.id
      }).then(resp => {
        this.$store.commit('setUser', {
          ...this.user,
          ...this.editForm
        })
        this.$message.success('保存成功')
        this.editDialog = false
      }).catch(() => this.$message.error('保存失败'))
    },
    savePwd() {
      if (this.pwdForm.newPassword !== this.pwdForm.confirmPassword) {
        this.$message.warning('两次密码不一致')
        return
      }
      if (!this.pwdForm.oldPassword) {
        this.$message.warning('请输入原密码')
        return
      }
      const role = this.user?.role
      const url = role === 'patient' ? '/patient/password' : '/doctor/password'
      this.$axios.post(url, {
        ...this.pwdForm,
        id: this.user.id
      }).then(resp => {
        if (resp.data && resp.data.code === 0) {
          this.$message.success('密码修改成功')
          this.pwdDialog = false
        } else {
          this.$message.error(resp.data?.data || '修改失败')
        }
      }).catch(() => this.$message.error('修改失败'))
    },
    goBack() {
      const role = this.user?.role
      if (role === 'patient') {
        this.$router.push('/patient/records')
      } else {
        this.$router.push('/doctor/records')
      }
    }
  }
}
</script>

<style scoped>
.app-container { min-height: 100vh; background: #f0f2f5; }
.page-content { max-width: 750px; margin: 0 auto; padding: 40px 24px; }
.profile-card { background: #fff; border-radius: 12px; padding: 40px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.profile-header { text-align: center; padding-bottom: 30px; border-bottom: 1px solid #ebeef5; margin-bottom: 30px; }
.user-name { font-size: 24px; color: #303133; margin: 0 0 6px; }
.user-role { font-size: 14px; color: #909399; margin: 0; }
.info-grid { max-width: 500px; margin: 0 auto; }
.profile-actions { display: flex; justify-content: flex-end; gap: 12px; padding-top: 24px; border-top: 1px solid #ebeef5; margin-top: 24px; }
.back-bar { margin-bottom: 12px; }
.back-bar .el-button { font-size: 14px; color: #409EFF; }
</style>
