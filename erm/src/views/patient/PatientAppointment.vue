<template>
  <div class="page-box">
    <div class="page-header"><h3>预约挂号</h3></div>

    <div class="step-card" v-if="step === 1">
      <div class="step-title"><span class="step-badge">1</span> 选择就诊科室</div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="dept in departments" :key="dept.id" style="margin-bottom:12px">
          <el-card :class="['dept-card', { active: form.office === dept.name }]" shadow="hover" @click.native="selectDept(dept.name)">
            <div class="dept-icon">
              <i :class="deptIcon(dept.name)"></i>
            </div>
            <div class="dept-name">{{ dept.name }}</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="step-card" v-if="step === 2">
      <div class="step-title"><span class="step-badge">2</span> 选择医生</div>
      <el-tag type="info" closable @close="step = 1" style="margin-bottom:16px">当前科室：{{ form.office }}</el-tag>
      <el-table :data="doctors" border stripe v-loading="loadingDoctors">
        <el-table-column prop="name" label="医生姓名" width="120"></el-table-column>
        <el-table-column prop="title" label="职称" width="120"></el-table-column>
        <el-table-column prop="hospital" label="所属医院"></el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button type="primary" size="small" @click="selectDoctor(scope.row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="step-card" v-if="step === 3">
      <div class="step-title"><span class="step-badge">3</span> 填写挂号信息</div>
      <el-tag type="info" closable @close="step = 2" style="margin-bottom:16px">医生：{{ form.doctorName }}（{{ form.office }}）</el-tag>
      <el-form :model="form" label-width="120px" :rules="rules" ref="apptForm">
        <el-form-item label="就诊日期" prop="appointmentDate">
          <el-date-picker v-model="form.appointmentDate" type="date" placeholder="选择日期"
            :picker-options="{ disabledDate: d => d < new Date() }" style="width:100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="就诊时段" prop="timeSlot">
          <el-radio-group v-model="form.timeSlot">
            <el-radio-button label="上午">上午 8:00-12:00</el-radio-button>
            <el-radio-button label="下午">下午 13:30-17:00</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="病情描述">
          <el-input type="textarea" :rows="3" v-model="form.chiefComplaint" placeholder="请简要描述您的主要症状..."></el-input>
        </el-form-item>
      </el-form>
      <div class="form-actions">
        <el-button type="primary" size="medium" @click="submitAppt" :loading="submitting">确认挂号</el-button>
        <el-button size="medium" @click="resetForm">重新选择</el-button>
      </div>
    </div>

    <div class="step-card" v-if="step === 4">
      <div class="success-result">
        <i class="el-icon-success"></i>
        <h3>挂号成功！</h3>
        <div class="result-info">
          <div class="info-row"><span class="label">就诊科室：</span><span>{{ form.office }}</span></div>
          <div class="info-row"><span class="label">就诊医生：</span><span>{{ form.doctorName }}</span></div>
          <div class="info-row"><span class="label">就诊日期：</span><span>{{ form.appointmentDate }}</span></div>
          <div class="info-row"><span class="label">就诊时段：</span><span>{{ form.timeSlot }}</span></div>
        </div>
        <el-button type="primary" @click="$router.push('/patient/appointments')">查看我的挂号</el-button>
        <el-button @click="resetAll">继续挂号</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PatientAppointment',
  data() {
    return {
      step: 1,
      departments: [],
      doctors: [],
      loadingDoctors: false,
      submitting: false,
      form: {
        office: '',
        doctorId: null,
        doctorName: '',
        appointmentDate: '',
        timeSlot: '',
        chiefComplaint: ''
      },
      rules: {
        appointmentDate: [{ required: true, message: '请选择就诊日期', trigger: 'change' }],
        timeSlot: [{ required: true, message: '请选择就诊时段', trigger: 'change' }]
      }
    }
  },
  created() {
    if (!this.$store.state.user) { this.$router.push('/login'); return }
    this.fetchDepartments()
  },
  methods: {
    fetchDepartments() {
      this.$axios.get('/appointment/departments').then(resp => { this.departments = resp.data || [] })
    },
    deptIcon(name) {
      const map = { '内科': 'el-icon-document', '外科': 'el-icon-scissors', '儿科': 'el-icon-user', '眼科': 'el-icon-search' }
      return map[name] || 'el-icon-menu'
    },
    selectDept(office) {
      this.form.office = office
      this.loadingDoctors = true
      this.$axios.get('/appointment/doctors', { params: { office } }).then(resp => {
        this.doctors = resp.data || []
        this.step = 2
      }).finally(() => { this.loadingDoctors = false })
    },
    selectDoctor(doc) {
      this.form.doctorId = doc.id
      this.form.doctorName = doc.name
      this.form.hospital = doc.hospital
      this.step = 3
    },
    submitAppt() {
      this.$refs.apptForm.validate(valid => {
        if (!valid) return
        this.submitting = true
        this.$axios.post('/appointment/create', {
          patientId: this.$store.state.user.id,
          doctorId: this.form.doctorId,
          office: this.form.office,
          hospital: this.form.hospital,
          appointmentDate: this.form.appointmentDate,
          timeSlot: this.form.timeSlot,
          chiefComplaint: this.form.chiefComplaint
        }).then(resp => {
          if (resp.data && resp.data.code === 0) {
            this.step = 4
          } else {
            this.$message.error(resp.data?.data || '挂号失败')
          }
        }).catch(() => this.$message.error('挂号失败')).finally(() => { this.submitting = false })
      })
    },
    resetForm() { this.step = 1; this.form.office = ''; this.form.appointmentDate = ''; this.form.timeSlot = ''; this.form.chiefComplaint = '' },
    resetAll() { this.step = 1; this.form = { office: '', doctorId: null, doctorName: '', appointmentDate: '', timeSlot: '', chiefComplaint: '', hospital: '' } }
  }
}
</script>

<style scoped>
.page-box { padding: 24px; max-width: 900px; margin: 0 auto; }
.step-card {
  background: #fff; border-radius: 12px; padding: 30px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}
.step-title { font-size: 18px; font-weight: 600; color: #303133; margin-bottom: 20px; }
.step-badge {
  display: inline-block; width: 26px; height: 26px; line-height: 26px; text-align: center;
  background: #409EFF; color: #fff; border-radius: 50%; font-size: 14px; margin-right: 8px;
}
.dept-card {
  cursor: pointer; text-align: center; transition: all 0.2s;
  border: 2px solid transparent;
}
.dept-card.active { border-color: #409EFF; background: #ecf5ff; }
.dept-card:hover { transform: translateY(-2px); }
.dept-icon { font-size: 32px; color: #409EFF; margin-bottom: 8px; }
.dept-name { font-size: 14px; color: #303133; }
.success-result { text-align: center; padding: 30px 0; }
.success-result i { font-size: 64px; color: #67C23A; }
.success-result h3 { font-size: 22px; color: #303133; margin: 16px 0; }
.result-info { display: inline-block; text-align: left; margin: 16px auto; }
.result-info .info-row { line-height: 32px; font-size: 14px; }
.result-info .label { color: #909399; width: 100px; display: inline-block; }
.form-actions { margin-top: 20px; display: flex; gap: 12px; }
</style>
