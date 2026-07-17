package com.erm.backend.VO;

import java.time.LocalDateTime;

public class RecordVO {
    private Integer id;
    
    private Integer doctorId;
    private Integer patientId;
    private String patientName;
    private Long patientPhone;
    private Integer patientSex;
    private Integer patientAge;
    private String idCard;
    private String bloodType;
    private String doctorName;
    private String doctorHospital;
    private String doctorOffice;
    private LocalDateTime createTime;
    private Integer affirm;
    private String chiefComplaint;
    private String history;
    private String physicalExam;
    private String examination;
    private String description;
    private String remark;
    private String icdCode;
    private String prescription;
    private String followup;
    private String visitNo;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getDoctorId() { return doctorId; }
    public void setDoctorId(Integer doctorId) { this.doctorId = doctorId; }
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public Long getPatientPhone() { return patientPhone; }
    public void setPatientPhone(Long patientPhone) { this.patientPhone = patientPhone; }
    public Integer getPatientSex() { return patientSex; }
    public void setPatientSex(Integer patientSex) { this.patientSex = patientSex; }
    public Integer getPatientAge() { return patientAge; }
    public void setPatientAge(Integer patientAge) { this.patientAge = patientAge; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getDoctorHospital() { return doctorHospital; }
    public void setDoctorHospital(String doctorHospital) { this.doctorHospital = doctorHospital; }
    public String getDoctorOffice() { return doctorOffice; }
    public void setDoctorOffice(String doctorOffice) { this.doctorOffice = doctorOffice; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public Integer getAffirm() { return affirm; }
    public void setAffirm(Integer affirm) { this.affirm = affirm; }
    public String getChiefComplaint() { return chiefComplaint; }
    public void setChiefComplaint(String chiefComplaint) { this.chiefComplaint = chiefComplaint; }
    public String getHistory() { return history; }
    public void setHistory(String history) { this.history = history; }
    public String getPhysicalExam() { return physicalExam; }
    public void setPhysicalExam(String physicalExam) { this.physicalExam = physicalExam; }
    public String getExamination() { return examination; }
    public void setExamination(String examination) { this.examination = examination; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public String getIcdCode() { return icdCode; }
    public void setIcdCode(String icdCode) { this.icdCode = icdCode; }
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    public String getFollowup() { return followup; }
    public void setFollowup(String followup) { this.followup = followup; }
    public String getVisitNo() { return visitNo; }
    public void setVisitNo(String visitNo) { this.visitNo = visitNo; }
}