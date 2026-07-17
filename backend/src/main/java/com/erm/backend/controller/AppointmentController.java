package com.erm.backend.controller;

import com.erm.backend.VO.ResultVO;
import com.erm.backend.entity.Appointment;
import com.erm.backend.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    /** 患者挂号 */
    @PostMapping("/create")
    public ResultVO create(@RequestBody Map<String, Object> params) {
        return appointmentService.createAppointment(params);
    }

    /** 患者查看自己的挂号记录 */
    @GetMapping("/my/{patientId}")
    public List<Appointment> myAppointments(@PathVariable Integer patientId) {
        return appointmentService.getPatientAppointments(patientId);
    }

    /** 医生查看自己的接诊列表 */
    @GetMapping("/doctor/{doctorId}")
    public List<Appointment> doctorAppointments(
            @PathVariable Integer doctorId,
            @RequestParam(required = false) Integer status) {
        return appointmentService.getDoctorAppointments(doctorId, status);
    }

    /** 获取科室列表 */
    @GetMapping("/departments")
    public List<Map<String, Object>> departments() {
        return appointmentService.getDepartmentList();
    }

    /** 根据科室获取医生列表 */
    @GetMapping("/doctors")
    public List<Map<String, Object>> doctorsByOffice(@RequestParam String office) {
        return appointmentService.getDoctorsByOffice(office);
    }

    /** 更新挂号状态（接诊/取消） */
    @PostMapping("/status/{id}")
    public ResultVO updateStatus(@PathVariable Integer id, @RequestBody Map<String, Integer> params) {
        ResultVO result = new ResultVO();
        Appointment appt = appointmentService.getById(id);
        if (appt == null) {
            result.setCode(-1); result.setData("not_found"); return result;
        }
        appt.setStatus(params.get("status"));
        appointmentService.updateById(appt);
        result.setCode(0);
        result.setData("success");
        return result;
    }
}
