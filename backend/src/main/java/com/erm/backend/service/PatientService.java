package com.erm.backend.service;

import com.erm.backend.VO.ResultVO;
import com.erm.backend.entity.Patient;
import com.erm.backend.form.LoginForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 患者表 服务类
 */
public interface PatientService extends IService<Patient> {

    ResultVO login(LoginForm loginForm);
}