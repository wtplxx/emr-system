package com.erm.backend.controller;

import com.erm.backend.entity.Appointment;
import com.erm.backend.entity.Doctor;
import com.erm.backend.entity.Record;
import com.erm.backend.service.AppointmentService;
import com.erm.backend.service.DoctorService;
import com.erm.backend.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private RecordService recordService;

    /** 医生工作台统计数据 */
    @GetMapping("/doctor/{doctorId}")
    public Map<String, Object> doctorStats(@PathVariable Integer doctorId) {
        Map<String, Object> stats = new LinkedHashMap<>();

        // 今日挂号
        List<Appointment> todayAppts = appointmentService.lambdaQuery()
                .eq(Appointment::getDoctorId, doctorId)
                .eq(Appointment::getAppointmentDate, LocalDate.now())
                .list();
        stats.put("todayAppointments", todayAppts.size());
        stats.put("pendingAppointments", todayAppts.stream().filter(a -> a.getStatus() == 0).count());

        // 总病历数
        long recordCount = recordService.lambdaQuery()
                .eq(Record::getDoctorId, doctorId)
                .count();
        stats.put("totalRecords", recordCount);

        // 已确认病历数
        long confirmedCount = recordService.lambdaQuery()
                .eq(Record::getDoctorId, doctorId)
                .eq(Record::getAffirm, 1)
                .count();
        stats.put("confirmedRecords", confirmedCount);

        // 医生信息
        Doctor doctor = doctorService.getById(doctorId);
        if (doctor != null) {
            stats.put("doctorName", doctor.getName());
            stats.put("hospital", doctor.getHospital());
            stats.put("office", doctor.getOffice());
        }

        return stats;
    }

    /** 患者档案统计 */
    @GetMapping("/patient/{patientId}")
    public Map<String, Object> patientStats(@PathVariable Integer patientId) {
        Map<String, Object> stats = new LinkedHashMap<>();

        long recordCount = recordService.lambdaQuery()
                .eq(Record::getPatientId, patientId)
                .count();
        stats.put("totalRecords", recordCount);

        List<Appointment> appts = appointmentService.lambdaQuery()
                .eq(Appointment::getPatientId, patientId)
                .list();
        stats.put("totalAppointments", appts.size());
        stats.put("pendingAppointments", appts.stream().filter(a -> a.getStatus() == 0).count());

        return stats;
    }
}
