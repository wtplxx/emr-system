package com.erm.backend.service;

import com.erm.backend.VO.ResultVO;
import com.erm.backend.entity.Appointment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface AppointmentService extends IService<Appointment> {
    ResultVO createAppointment(Map<String, Object> params);
    List<Appointment> getPatientAppointments(Integer patientId);
    List<Appointment> getDoctorAppointments(Integer doctorId, Integer status);
    List<Map<String, Object>> getDepartmentList();
    List<Map<String, Object>> getDoctorsByOffice(String office);
}
