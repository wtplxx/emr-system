package com.erm.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erm.backend.VO.ResultVO;
import com.erm.backend.entity.Doctor;
import com.erm.backend.form.LoginForm;
import com.erm.backend.mapper.DoctorMapper;
import com.erm.backend.security.JwtUtil;
import com.erm.backend.service.DoctorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Override
    public ResultVO login(LoginForm loginForm) {
        QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", loginForm.getPhone());
        Doctor doctor = this.doctorMapper.selectOne(queryWrapper);
        ResultVO resultVO = new ResultVO();

        if (doctor == null) {
            resultVO.setCode(-1);
        } else if (com.erm.backend.utils.PasswordUtils.matches(loginForm.getPassword(), doctor.getPassword())) {
            resultVO.setCode(0);
            String token = JwtUtil.generateToken(doctor.getId(), "doctor");
            doctor.setPassword(null);
            resultVO.setData(doctor);
            resultVO.setToken(token);
        } else {
            resultVO.setCode(-2);
        }
        return resultVO;
    }
}