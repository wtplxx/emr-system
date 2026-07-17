package com.erm.backend.controller;

import com.erm.backend.VO.ResultVO;
import com.erm.backend.entity.Patient;
import com.erm.backend.form.LoginForm;
import com.erm.backend.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping("/login")
    public ResultVO login(@RequestBody LoginForm loginForm) {
        return this.patientService.login(loginForm);
    }

    @PostMapping("/create")
    public ResultVO create(@RequestBody Patient patient) {
        ResultVO result = new ResultVO();
        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            result.setCode(-1); result.setData("name_required"); return result;
        }
        if (patient.getPhone() == null) {
            result.setCode(-1); result.setData("phone_required"); return result;
        }
        if (patient.getPassword() == null || patient.getPassword().isEmpty()) {
            patient.setPassword("123456");
        }
        patient.setPassword(com.erm.backend.utils.PasswordUtils.encode(patient.getPassword()));
        boolean ok = patientService.save(patient);
        result.setCode(ok ? 0 : -1);
        result.setData(ok ? "success" : "创建失败");
        return result;
    }

    /** 更新患者资料 */
    @PostMapping("/update")
    public ResultVO update(@RequestBody Patient patient) {
        ResultVO result = new ResultVO();
        boolean ok = patientService.updateById(patient);
        result.setCode(ok ? 0 : -1);
        result.setData(ok ? "success" : "更新失败");
        return result;
    }

    /** 获取患者基本信息 */
    @GetMapping("/info/{id}")
    public Patient info(@PathVariable Integer id) {
        Patient patient = patientService.getById(id);
        if (patient == null) return null;
        patient.setPassword(null);
        return patient;
    }

    /** 修改密码 */
    @PostMapping("/password")
    public ResultVO password(@RequestBody Map<String, String> params) {
        ResultVO result = new ResultVO();
        Integer id = Integer.valueOf(params.get("id"));
        String oldPwd = params.get("oldPassword");
        String newPwd = params.get("newPassword");

        Patient patient = patientService.getById(id);
        if (patient == null) {
            result.setCode(-1); result.setData("用户不存在"); return result;
        }
        if (!com.erm.backend.utils.PasswordUtils.matches(oldPwd, patient.getPassword())) {
            result.setCode(-1); result.setData("原密码错误"); return result;
        }

        patient.setPassword(com.erm.backend.utils.PasswordUtils.encode(newPwd));
        patientService.updateById(patient);
        result.setCode(0);
        result.setData("success");
        return result;
    }
}
