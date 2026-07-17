package com.erm.backend.controller;

import com.erm.backend.VO.ResultVO;
import com.erm.backend.entity.Doctor;
import com.erm.backend.form.LoginForm;
import com.erm.backend.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/login")
    public ResultVO login(@RequestBody LoginForm loginForm) {
        return this.doctorService.login(loginForm);
    }

    @PostMapping("/update")
    public ResultVO update(@RequestBody Doctor doctor) {
        ResultVO result = new ResultVO();
        boolean ok = doctorService.updateById(doctor);
        result.setCode(ok ? 0 : -1);
        result.setData(ok ? "success" : "更新失败");
        return result;
    }

    /** 获取医生基本信息 */
    @GetMapping("/info/{id}")
    public Doctor info(@PathVariable Integer id) {
        Doctor doctor = doctorService.getById(id);
        if (doctor == null) return null;
        doctor.setPassword(null);
        return doctor;
    }

    /** 修改密码 */
    @PostMapping("/password")
    public ResultVO password(@RequestBody Map<String, String> params) {
        ResultVO result = new ResultVO();
        Integer id = Integer.valueOf(params.get("id"));
        String oldPwd = params.get("oldPassword");
        String newPwd = params.get("newPassword");

        Doctor doctor = doctorService.getById(id);
        if (doctor == null) {
            result.setCode(-1); result.setData("用户不存在"); return result;
        }
        if (!com.erm.backend.utils.PasswordUtils.matches(oldPwd, doctor.getPassword())) {
            result.setCode(-1); result.setData("原密码错误"); return result;
        }

        doctor.setPassword(com.erm.backend.utils.PasswordUtils.encode(newPwd));
        doctorService.updateById(doctor);
        result.setCode(0);
        result.setData("success");
        return result;
    }
}
