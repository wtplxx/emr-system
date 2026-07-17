package com.erm.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erm.backend.VO.ResultVO;
import com.erm.backend.entity.Patient;
import com.erm.backend.form.LoginForm;
import com.erm.backend.mapper.PatientMapper;
import com.erm.backend.security.JwtUtil;
import com.erm.backend.service.PatientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public ResultVO login(LoginForm loginForm) {
        QueryWrapper<Patient> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", loginForm.getPhone());
        Patient patient = this.patientMapper.selectOne(queryWrapper);
        ResultVO resultVO = new ResultVO();

        if (patient == null) {
            resultVO.setCode(-1);
        } else if (com.erm.backend.utils.PasswordUtils.matches(loginForm.getPassword(), patient.getPassword())) {
            resultVO.setCode(0);
            String token = JwtUtil.generateToken(patient.getId(), "patient");
            patient.setPassword(null);
            resultVO.setData(patient);
            resultVO.setToken(token);
        } else {
            resultVO.setCode(-2);
        }
        return resultVO;
    }
}