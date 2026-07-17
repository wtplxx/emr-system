<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1>健康档案管理系统</h1>
        <p>账号登录</p>
      </div>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="loginForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" show-password placeholder="请输入密码"></el-input>
        </el-form-item>

        <el-form-item label="">
          <el-radio-group v-model="type">
            <el-radio label="doctor">医生用户</el-radio>
            <el-radio label="patient">个人用户</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="medium"
            :loading="loading"
            @click="handleLogin"
            class="login-btn"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      loginForm: { phone: '', password: '' },
      type: 'doctor',
      loginRules: {
        phone: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
        password: [{ required: true, message: '密码不能为空', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true
          let _this = this
          const loginUrl = _this.type === 'doctor' ? '/doctor/login' : '/patient/login'
          _this.$axios.post(loginUrl, _this.loginForm)
            .then(resp => {
              _this.loading = false
              const res = resp.data
              if (res.code === -1) {
                _this.$alert(res.data || '用户不存在', '提示', { confirmButtonText: '确定' })
              } else if (res.code === -2) {
                _this.$alert('密码错误', '提示', { confirmButtonText: '确定' })
              } else if (res.code === 0) {
                _this.$alert('登录成功', '提示', {
                  confirmButtonText: '确定',
                  callback() {
                    localStorage.setItem('token', res.token)
                    _this.$store.commit('setUser', {
                      ...res.data,
                      role: _this.type
                    })
                    if (_this.type === 'doctor') {
                      _this.$router.push('/doctor/dashboard')
                    } else {
                      _this.$router.push('/patient/records')
                    }
                  }
                })
              }
            })
            .catch(err => {
              _this.loading = false
              _this.$alert('接口请求失败，请检查后端是否启动', '错误')
              console.error(err)
            })
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #e0eafc 0%, #cfdef3 100%);
  padding: 20px;
}
.login-box {
  width: 380px;
  background: #ffffff;
  border-radius: 16px;
  padding: 40px 35px 35px 35px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}
.login-header {
  text-align: center;
  margin-bottom: 30px;
}
.login-header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 6px 0;
  letter-spacing: 2px;
}
.login-header p {
  font-size: 14px;
  color: #909399;
  margin: 0;
}
.login-btn {
  width: 100%;
  font-size: 16px;
  border-radius: 8px;
  letter-spacing: 4px;
  margin-top: 6px;
}
</style>
