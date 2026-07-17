<template>
  <div class="app-container">

    <!-- 核心表单 -->
    <div class="form-card">
      <div class="form-title">新建档案</div>
      <el-form :model="form" label-width="100px" ref="formRef" :rules="formRules">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="sex">
              <el-select v-model="form.sex" placeholder="请选择性别" style="width: 100%">
                <el-option label="男" :value="1"></el-option>
                <el-option label="女" :value="2"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input v-model.number="form.age" placeholder="请输入年龄"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="身份证号">
          <el-input v-model="form.idCard" placeholder="请输入身份证号"></el-input>
        </el-form-item>
        <el-form-item label="血型">
          <el-select v-model="form.bloodType" placeholder="请选择血型" style="width:100%">
            <el-option label="A" value="A"></el-option>
            <el-option label="B" value="B"></el-option>
            <el-option label="AB" value="AB"></el-option>
            <el-option label="O" value="O"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="主诉">
          <el-input
            type="textarea"
            :rows="3"
            v-model="form.chiefComplaint"
            placeholder="请输入主诉内容"
          ></el-input>
        </el-form-item>
        <el-form-item label="现病史">
          <el-input
            type="textarea"
            :rows="3"
            v-model="form.history"
            placeholder="请输入现病史"
          ></el-input>
        </el-form-item>
        <el-form-item label="体格检查">
          <el-input
            type="textarea"
            :rows="2"
            v-model="form.physicalExam"
            placeholder="请输入体格检查结果"
          ></el-input>
        </el-form-item>
        <el-form-item label="辅助检查">
          <el-input
            type="textarea"
            :rows="2"
            v-model="form.examination"
            placeholder="请输入辅助检查结果"
          ></el-input>
        </el-form-item>
        <el-form-item label="医生诊断">
          <el-input
            type="textarea"
            :rows="4"
            v-model="form.diagnosis"
            placeholder="请输入诊断内容"
          ></el-input>
        </el-form-item>
        <el-form-item label="处方">
          <el-input
            type="textarea"
            :rows="3"
            v-model="form.prescription"
            placeholder="请输入处方信息"
          ></el-input>
        </el-form-item>
        <el-form-item label="随访建议">
          <el-input
            type="textarea"
            :rows="2"
            v-model="form.followup"
            placeholder="随访建议"
          ></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            type="textarea"
            :rows="2"
            v-model="form.remark"
            placeholder="备注"
          ></el-input>
        </el-form-item>
      </el-form>
      <div class="form-actions">
        <el-button type="primary" icon="el-icon-plus" size="medium" @click="showConfirm">
          +新建档案
        </el-button>
        <el-button size="medium" @click="resetForm">重置</el-button>
      </div>
    </div>

    <!-- 确认弹窗 -->
    <el-dialog title="确认档案" :visible.sync="dialogVisible" width="520px">
      <div class="confirm-body">
        <!-- 就诊上下文 -->
        <div class="info-card">
          <div class="info-row">
            <span class="info-label">就诊医院：</span>
            <span class="info-value">{{ user?.hospital || '—' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">就诊科室：</span>
            <span class="info-value">{{ user?.office || '—' }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">主治医师：</span>
            <span class="info-value">{{ user?.name || '—' }}</span>
          </div>
        </div>

        <!-- 录入回显 -->
        <div class="review-list">
          <div class="review-item">
            <span class="review-label">姓名</span>
            <span class="review-value">{{ form.name || '—' }}</span>
          </div>
          <div class="review-item">
            <span class="review-label">性别</span>
            <span class="review-value">{{ form.sex === 1 ? '男' : form.sex === 2 ? '女' : '—' }}</span>
          </div>
          <div class="review-item">
            <span class="review-label">年龄</span>
            <span class="review-value">{{ form.age || '—' }}</span>
          </div>
          <div class="review-item">
            <span class="review-label">联系电话</span>
            <span class="review-value">{{ form.phone || '—' }}</span>
          </div>
          <div class="review-item">
            <span class="review-label">诊断</span>
            <span class="review-value">{{ form.diagnosis || '—' }}</span>
          </div>
        </div>
      </div>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'RecordCreate',
  data() {
    return {
      dialogVisible: false,
      form: {
        name: '',
        phone: '',
        sex: null,
        age: null,
        idCard: '',
        bloodType: '',
        chiefComplaint: '',
        history: '',
        physicalExam: '',
        examination: '',
        diagnosis: '',
        prescription: '',
        followup: '',
        remark: ''
      },
      formRules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
        sex: [{ required: true, message: '请选择性别', trigger: 'change' }],
        age: [{ required: true, message: '请输入年龄', trigger: 'blur' }]
      }
    }
  },
  computed: {
    user() {
      return this.$store.state.user
    }
  },
  created() {
    if (!this.user) {
      this.$router.push('/login')
    }
    // 支持从工作台快速建档传入的患者姓名
    if (this.$route.query.patientName) {
      this.form.name = this.$route.query.patientName
    }
  },
  methods: {
    showConfirm() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.dialogVisible = true
        }
      })
    },
    submitForm() {
      this.$axios.post('/record/create', {
        name: this.form.name,
        phone: this.form.phone,
        sex: this.form.sex,
        age: this.form.age,
        idCard: this.form.idCard,
        bloodType: this.form.bloodType,
        chiefComplaint: this.form.chiefComplaint,
        history: this.form.history,
        physicalExam: this.form.physicalExam,
        examination: this.form.examination,
        description: this.form.diagnosis,
        prescription: this.form.prescription,
        followup: this.form.followup,
        remark: this.form.remark,
        doctorId: this.user.id
      }).then(resp => {
        if (resp.data && resp.data.code === 0) {
          this.$message.success('档案创建成功！')
          this.dialogVisible = false
          this.resetForm()
        } else {
          this.$message.error(resp.data?.data || '创建失败')
        }
      }).catch(err => {
        this.$message.error('创建失败，请检查后端服务')
        console.error(err)
      })
    },
    resetForm() {
      this.form = {
        name: '', phone: '', sex: null, age: null,
        idCard: '', bloodType: '',
        chiefComplaint: '', history: '', physicalExam: '', examination: '',
        diagnosis: '', prescription: '', followup: '', remark: ''
      }
      if (this.$refs.formRef) this.$refs.formRef.clearValidate()
    }
  }
}
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  background: #f0f2f5;
  padding: 20px;
}
.form-card {
  background: #fff;
  border-radius: 12px;
  padding: 30px 36px;
  max-width: 800px;
  margin: 0 auto;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
}
.form-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  padding-bottom: 20px;
  margin-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}
.form-actions {
  display: flex;
  gap: 12px;
  padding-top: 10px;
  border-top: 1px solid #ebeef5;
}
.confirm-body { padding: 0 4px; }
.info-card {
  background: #f5f7fa;
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 20px;
}
.info-row {
  display: flex;
  line-height: 28px;
  font-size: 14px;
}
.info-label {
  color: #909399;
  width: 100px;
  flex-shrink: 0;
}
.info-value {
  color: #303133;
  font-weight: 500;
}
.review-list {
  border-top: 1px dashed #dcdfe6;
  padding-top: 16px;
}
.review-item {
  display: flex;
  padding: 6px 0;
  font-size: 14px;
}
.review-label {
  color: #909399;
  width: 100px;
  flex-shrink: 0;
}
.review-value {
  color: #303133;
}
</style>
