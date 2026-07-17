package com.erm.backend.service;

import com.erm.backend.VO.ResultVO;
import com.erm.backend.entity.Doctor;
import com.baomidou.mybatisplus.extension.service.IService;
import com.erm.backend.form.LoginForm;

/**
 * <p>
 * 医生表 服务类
 * </p>
 *
 * @author admin
 * @since 2026-06-25
 */
public interface DoctorService extends IService<Doctor> {
    public ResultVO login(LoginForm loginForm);
}
