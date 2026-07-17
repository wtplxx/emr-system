package com.erm.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erm.backend.VO.RecordVO;
import com.erm.backend.entity.Doctor;
import com.erm.backend.entity.Patient;
import com.erm.backend.entity.Record;
import com.erm.backend.mapper.PatientMapper;
import com.erm.backend.mapper.RecordMapper;
import com.erm.backend.service.DoctorService;
import com.erm.backend.service.PatientService;
import com.erm.backend.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private DoctorService doctorService;

    @Override
    public Map<String, Object> searchRecords(String name, String phone, String office, int pageNo, int pageSize) {
        Set<Integer> patientIds = null;
        Set<Integer> recordIds = null;

        // 1. 按手机号查找患者ID
        if (phone != null && !phone.isEmpty()) {
            try {
                QueryWrapper<Patient> pw = new QueryWrapper<>();
                pw.eq("phone", Long.parseLong(phone));
                Patient p = patientMapper.selectOne(pw);
                if (p != null) {
                    patientIds = new HashSet<>();
                    patientIds.add(p.getId());
                } else {
                    patientIds = new HashSet<>();
                }
            } catch (NumberFormatException e) {
                patientIds = new HashSet<>();
            }
        }

        // 2. 按姓名模糊查找患者ID
        if (name != null && !name.isEmpty()) {
            QueryWrapper<Patient> pw = new QueryWrapper<>();
            pw.like("name", name);
            List<Patient> patients = patientMapper.selectList(pw);
            if (patients.isEmpty()) {
                patientIds = new HashSet<>();
            } else {
                if (patientIds == null) {
                    patientIds = patients.stream().map(Patient::getId).collect(Collectors.toSet());
                } else {
                    patientIds.retainAll(patients.stream().map(Patient::getId).collect(Collectors.toSet()));
                }
            }
        }

        // 3. 如果指定了患者ID，查找对应的病历记录ID
        if (patientIds != null && !patientIds.isEmpty()) {
            QueryWrapper<Record> qw = new QueryWrapper<>();
            qw.in("patient_id", patientIds);
            List<Record> records = this.list(qw);
            recordIds = records.stream().map(Record::getId).collect(Collectors.toSet());
        } else if (patientIds != null) {
            recordIds = new HashSet<>();
        }

        // 4. 按科室过滤
        if (office != null && !office.isEmpty()) {
            if (recordIds == null || recordIds.isEmpty()) {
                Map<String, Object> emptyResult = new HashMap<>();
                emptyResult.put("records", new ArrayList<>());
                emptyResult.put("total", 0);
                return emptyResult;
            }
            Set<Integer> filteredIds = new HashSet<>();
            List<Record> allRecords = this.list();
            for (Record r : allRecords) {
                if (!recordIds.contains(r.getId())) continue;
                Doctor d = doctorService.getById(r.getDoctorId());
                if (d != null && d.getOffice() != null && d.getOffice().contains(office)) {
                    filteredIds.add(r.getId());
                }
            }
            recordIds = filteredIds;
        }

        // 5. 获取全量结果集用于计算 total
        List<Record> allMatchedRecords;
        if (recordIds != null && !recordIds.isEmpty()) {
            allMatchedRecords = this.listByIds(recordIds);
        } else if (recordIds != null) {
            allMatchedRecords = new ArrayList<>();
        } else {
            QueryWrapper<Record> qw = new QueryWrapper<>();
            qw.orderByDesc("id");
            allMatchedRecords = this.list(qw);
        }

        // 6. 构建 RecordVO 列表
        List<RecordVO> allVO = new ArrayList<>();
        for (Record r : allMatchedRecords) {
            Patient patient = patientService.getById(r.getPatientId());
            if (patient == null) continue;
            Doctor doctor = doctorService.getById(r.getDoctorId());

            RecordVO vo = new RecordVO();
            vo.setId(r.getId());
            vo.setDoctorId(r.getDoctorId());
            vo.setPatientId(r.getPatientId());
            vo.setPatientName(patient.getName());
            vo.setPatientPhone(patient.getPhone());
            vo.setPatientSex(patient.getSex());
            vo.setPatientAge(patient.getAge());
            vo.setIdCard(patient.getIdCard());
            vo.setBloodType(patient.getBloodType());
            vo.setDoctorName(doctor != null ? doctor.getName() : "");
            vo.setDoctorHospital(doctor != null ? doctor.getHospital() : "");
            vo.setDoctorOffice(doctor != null ? doctor.getOffice() : "");
            vo.setCreateTime(r.getCreatetime());
            vo.setAffirm(r.getAffirm());
            vo.setChiefComplaint(r.getChiefComplaint());
            vo.setHistory(r.getHistory());
            vo.setPhysicalExam(r.getPhysicalExam());
            vo.setExamination(r.getExamination());
            vo.setDescription(r.getDescriptionEnbyp());
            vo.setRemark(r.getRemarkEnbyp());
            vo.setIcdCode(r.getIcdCode());
            vo.setPrescription(r.getPrescription());
            vo.setFollowup(r.getFollowup());
            vo.setVisitNo(r.getVisitNo());
            allVO.add(vo);
        }

        // 7. 分页截取
        int total = allVO.size();
        int fromIndex = (pageNo - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<RecordVO> pagedRecords = (fromIndex < total) ? allVO.subList(fromIndex, toIndex) : new ArrayList<>();

        Map<String, Object> result = new HashMap<>();
        result.put("records", pagedRecords);
        result.put("total", total);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFullRecord(Map<String, Object> params) {
        // 1. 先创建患者
        Patient patient = new Patient();
        patient.setName((String) params.get("name"));
        patient.setPhone(Long.valueOf(params.get("phone").toString()));
        patient.setSex(Integer.valueOf(params.get("sex").toString()));
        patient.setAge(Integer.valueOf(params.get("age").toString()));
        patient.setPassword(com.erm.backend.utils.PasswordUtils.encode("123456"));
        patient.setIdCard((String) params.get("idCard"));
        patient.setBloodType((String) params.get("bloodType"));
        patient.setAllergyHistory((String) params.get("allergyHistory"));
        patient.setEmergencyPhone((String) params.get("emergencyPhone"));
        patient.setAddress((String) params.get("address"));
        patientMapper.insert(patient);

        // 2. 再创建病历记录
        Record record = new Record();
        record.setPatientId(patient.getId());
        record.setDoctorId(Integer.valueOf(params.get("doctorId").toString()));
        record.setChiefComplaint((String) params.get("chiefComplaint"));
        record.setHistory((String) params.get("history"));
        record.setPhysicalExam((String) params.get("physicalExam"));
        record.setExamination((String) params.get("examination"));
        record.setDescription((String) params.get("description"));
        record.setRemark((String) params.get("remark"));
        record.setDescriptionEnbyp((String) params.get("description"));
        record.setRemarkEnbyp((String) params.get("remark"));
        record.setAffirm(0);
        record.setCreatetime(LocalDateTime.now());
        record.setIcdCode((String) params.get("icdCode"));
        record.setPrescription((String) params.get("prescription"));
        record.setFollowup((String) params.get("followup"));
        record.setVisitNo((String) params.get("visitNo"));
        recordMapper.insert(record);
    }

    @Override
    public RecordVO getRecordDetail(Integer id) {
        Record r = this.getById(id);
        if (r == null) return null;

        Patient patient = patientService.getById(r.getPatientId());
        Doctor doctor = doctorService.getById(r.getDoctorId());

        RecordVO vo = new RecordVO();
        vo.setId(r.getId());
        vo.setDoctorId(r.getDoctorId());
        vo.setPatientId(r.getPatientId());
        vo.setPatientName(patient != null ? patient.getName() : "");
        vo.setPatientPhone(patient != null ? patient.getPhone() : null);
        vo.setPatientSex(patient != null ? patient.getSex() : null);
        vo.setPatientAge(patient != null ? patient.getAge() : null);
        vo.setIdCard(patient != null ? patient.getIdCard() : null);
        vo.setBloodType(patient != null ? patient.getBloodType() : null);
        vo.setDoctorName(doctor != null ? doctor.getName() : "");
        vo.setDoctorHospital(doctor != null ? doctor.getHospital() : "");
        vo.setDoctorOffice(doctor != null ? doctor.getOffice() : "");
        vo.setCreateTime(r.getCreatetime());
        vo.setAffirm(r.getAffirm());
        vo.setChiefComplaint(r.getChiefComplaint());
        vo.setHistory(r.getHistory());
        vo.setPhysicalExam(r.getPhysicalExam());
        vo.setExamination(r.getExamination());
        vo.setDescription(r.getDescriptionEnbyp());
        vo.setRemark(r.getRemarkEnbyp());
        vo.setIcdCode(r.getIcdCode());
        vo.setPrescription(r.getPrescription());
        vo.setFollowup(r.getFollowup());
        vo.setVisitNo(r.getVisitNo());
        return vo;
    }
}
