package com.erm.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erm.backend.VO.ResultVO;
import com.erm.backend.entity.Appointment;
import com.erm.backend.entity.Doctor;
import com.erm.backend.mapper.AppointmentMapper;
import com.erm.backend.mapper.DoctorMapper;
import com.erm.backend.service.AppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Override
    public ResultVO createAppointment(Map<String, Object> params) {
        ResultVO result = new ResultVO();

        if (params.get("patientId") == null) {
            result.setCode(-1); result.setData("请先登录"); return result;
        }
        if (params.get("office") == null) {
            result.setCode(-1); result.setData("请选择就诊科室"); return result;
        }
        if (params.get("appointmentDate") == null) {
            result.setCode(-1); result.setData("请选择预约日期"); return result;
        }
        if (params.get("timeSlot") == null) {
            result.setCode(-1); result.setData("请选择时间段"); return result;
        }

        Appointment appt = new Appointment();
        appt.setPatientId(Integer.valueOf(params.get("patientId").toString()));
        appt.setOffice((String) params.get("office"));
        appt.setHospital((String) params.get("hospital"));
        appt.setAppointmentDate(LocalDate.parse(params.get("appointmentDate").toString()));
        appt.setTimeSlot((String) params.get("timeSlot"));
        appt.setChiefComplaint((String) params.get("chiefComplaint"));
        appt.setStatus(0);

        // 自动匹配该科室的医生
        if (params.get("doctorId") != null) {
            appt.setDoctorId(Integer.valueOf(params.get("doctorId").toString()));
        } else {
            QueryWrapper<Doctor> qw = new QueryWrapper<>();
            qw.eq("office", appt.getOffice());
            qw.last("LIMIT 1");
            Doctor doc = doctorMapper.selectOne(qw);
            if (doc != null) {
                appt.setDoctorId(doc.getId());
                if (appt.getHospital() == null) {
                    appt.setHospital(doc.getHospital());
                }
            }
        }

        boolean ok = this.save(appt);
        result.setCode(ok ? 0 : -1);
        result.setData(ok ? "挂号成功" : "挂号失败");
        return result;
    }

    @Override
    public List<Appointment> getPatientAppointments(Integer patientId) {
        QueryWrapper<Appointment> qw = new QueryWrapper<>();
        qw.eq("patient_id", patientId);
        qw.orderByDesc("appointment_date");
        return this.list(qw);
    }

    @Override
    public List<Appointment> getDoctorAppointments(Integer doctorId, Integer status) {
        QueryWrapper<Appointment> qw = new QueryWrapper<>();
        qw.eq("doctor_id", doctorId);
        if (status != null) {
            qw.eq("status", status);
        }
        qw.orderByDesc("appointment_date");
        return this.list(qw);
    }

    @Override
    public List<Map<String, Object>> getDepartmentList() {
        // 从医生表提取不重复的科室
        List<Doctor> doctors = doctorMapper.selectList(null);
        Set<String> offices = doctors.stream()
                .map(Doctor::getOffice)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<Map<String, Object>> deptList = new ArrayList<>();
        int id = 1;
        for (String office : offices) {
            Map<String, Object> dept = new HashMap<>();
            dept.put("id", id++);
            dept.put("name", office);
            dept.put("description", office + "——提供专业的诊疗服务");
            deptList.add(dept);
        }
        return deptList;
    }

    @Override
    public List<Map<String, Object>> getDoctorsByOffice(String office) {
        QueryWrapper<Doctor> qw = new QueryWrapper<>();
        qw.eq("office", office);
        List<Doctor> doctors = doctorMapper.selectList(qw);

        return doctors.stream().map(d -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", d.getId());
            m.put("name", d.getName());
            m.put("hospital", d.getHospital());
            m.put("office", d.getOffice());
            m.put("titleLevel", d.getTitleLevel());
            String title = switch (d.getTitleLevel() != null ? d.getTitleLevel() : 0) {
                case 1 -> "主任医师";
                case 2 -> "副主任医师";
                case 3 -> "主治医师";
                case 4 -> "住院医师";
                default -> "医师";
            };
            m.put("title", title);
            return m;
        }).collect(Collectors.toList());
    }
}
