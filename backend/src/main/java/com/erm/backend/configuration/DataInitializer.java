package com.erm.backend.configuration;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.erm.backend.entity.Appointment;
import com.erm.backend.entity.Doctor;
import com.erm.backend.entity.Patient;
import com.erm.backend.entity.Record;
import com.erm.backend.service.AppointmentService;
import com.erm.backend.service.DoctorService;
import com.erm.backend.service.PatientService;
import com.erm.backend.service.RecordService;
import com.erm.backend.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应用启动时初始化测试数据。
 * 密码统一为 123456（BCrypt 加密）。
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private AppointmentService appointmentService;

    @Override
    public void run(String... args) {
        if (doctorService.count() > 0) {
            return;
        }

        String encodedPassword = PasswordUtils.encode("123456");

        // ========== 创建医生 ==========
        Doctor doc1 = new Doctor();
        doc1.setName("张医生");
        doc1.setPhone(13800000001L);
        doc1.setPassword(encodedPassword);
        doc1.setHospital("市第一人民医院");
        doc1.setOffice("内科");
        doc1.setEmployeeNo("DOC001");
        doc1.setTitleLevel(3);
        doc1.setLicenseNo("110138000000001");
        doctorService.save(doc1);

        Doctor doc2 = new Doctor();
        doc2.setName("李医生");
        doc2.setPhone(13800000002L);
        doc2.setPassword(encodedPassword);
        doc2.setHospital("市第一人民医院");
        doc2.setOffice("外科");
        doc2.setEmployeeNo("DOC002");
        doc2.setTitleLevel(2);
        doc2.setLicenseNo("110138000000002");
        doctorService.save(doc2);

        Doctor doc3 = new Doctor();
        doc3.setName("王医生");
        doc3.setPhone(13800000003L);
        doc3.setPassword(encodedPassword);
        doc3.setHospital("市第一人民医院");
        doc3.setOffice("儿科");
        doc3.setEmployeeNo("DOC003");
        doc3.setTitleLevel(1);
        doc3.setLicenseNo("110138000000003");
        doctorService.save(doc3);

        Doctor doc4 = new Doctor();
        doc4.setName("陈医生");
        doc4.setPhone(13800000004L);
        doc4.setPassword(encodedPassword);
        doc4.setHospital("市第一人民医院");
        doc4.setOffice("眼科");
        doc4.setEmployeeNo("DOC004");
        doc4.setTitleLevel(3);
        doc4.setLicenseNo("110138000000004");
        doctorService.save(doc4);

        // ========== 创建患者 ==========
        Patient pat1 = new Patient();
        pat1.setName("王患者");
        pat1.setPhone(13900000001L);
        pat1.setPassword(encodedPassword);
        pat1.setSex(1);
        pat1.setAge(35);
        pat1.setIdCard("110101199001011234");
        pat1.setBloodType("A");
        pat1.setAllergyHistory("青霉素过敏");
        pat1.setEmergencyPhone("13900000010");
        pat1.setAddress("北京市朝阳区建国路88号");
        patientService.save(pat1);

        Patient pat2 = new Patient();
        pat2.setName("赵患者");
        pat2.setPhone(13900000002L);
        pat2.setPassword(encodedPassword);
        pat2.setSex(2);
        pat2.setAge(28);
        pat2.setIdCard("110101199501012345");
        pat2.setBloodType("B");
        pat2.setEmergencyPhone("13900000020");
        pat2.setAddress("北京市海淀区中关村大街1号");
        patientService.save(pat2);

        Patient pat3 = new Patient();
        pat3.setName("刘患者");
        pat3.setPhone(13900000003L);
        pat3.setPassword(encodedPassword);
        pat3.setSex(1);
        pat3.setAge(45);
        pat3.setIdCard("110101198001011234");
        pat3.setBloodType("O");
        pat3.setEmergencyPhone("13900000030");
        patientService.save(pat3);

        // ========== 创建病历记录 ==========
        Record rec1 = new Record();
        rec1.setDoctorId(doc1.getId());
        rec1.setPatientId(pat1.getId());
        rec1.setChiefComplaint("咳嗽、咳痰三天，伴有发热38.5℃");
        rec1.setHistory("患者三天前受凉后出现咳嗽、咳痰，痰为白色粘痰，不易咳出。伴有发热，体温最高38.5℃。自服感冒药效果不佳。");
        rec1.setPhysicalExam("T:38.2℃, P:88次/分, R:20次/分, BP:120/80mmHg。咽部充血，双肺呼吸音粗，未闻及干湿性啰音。");
        rec1.setExamination("血常规：WBC 11.2×10^9/L, N 78%。胸片：双肺纹理增粗。");
        rec1.setDescription("急性上呼吸道感染");
        rec1.setAffirm(1);
        rec1.setIcdCode("J06.900");
        rec1.setPrescription("1. 阿莫西林胶囊 0.5g tid×7d\n2. 复方甘草口服液 10ml tid×5d\n3. 对乙酰氨基酚片 0.5g 发热时服用");
        rec1.setFollowup("服药后如症状加重或3天无改善，建议复查血常规。多饮水，注意休息。");
        rec1.setVisitNo("VIS20260715001");
        rec1.setOffice("内科");
        rec1.setHospital("市第一人民医院");
        rec1.setDoctorSignature(doc1.getName());
        rec1.setCreatetime(LocalDateTime.of(2026, 7, 15, 9, 30));
        recordService.save(rec1);

        Record rec2 = new Record();
        rec2.setDoctorId(doc2.getId());
        rec2.setPatientId(pat1.getId());
        rec2.setChiefComplaint("右下腹疼痛2天");
        rec2.setHistory("患者两天前无明显诱因出现右下腹持续性疼痛，无放射痛。伴有恶心，无呕吐。食欲减退。");
        rec2.setPhysicalExam("T:37.8℃, P:90次/分。右下腹麦氏点压痛阳性，反跳痛阳性。");
        rec2.setExamination("血常规：WBC 13.5×10^9/L, N 82%。腹部B超：阑尾增粗，直径约0.8cm。");
        rec2.setDescription("急性阑尾炎");
        rec2.setAffirm(1);
        rec2.setIcdCode("K35.900");
        rec2.setPrescription("收住院手术治疗：\n1. 急诊行阑尾切除术\n2. 术后抗感染治疗\n3. 禁食水，补液支持");
        rec2.setFollowup("术后7天拆线，门诊复查。注意伤口护理，避免剧烈运动1个月。");
        rec2.setVisitNo("VIS20260710001");
        rec2.setOffice("外科");
        rec2.setHospital("市第一人民医院");
        rec2.setDoctorSignature(doc2.getName());
        rec2.setCreatetime(LocalDateTime.of(2026, 7, 10, 14, 0));
        recordService.save(rec2);

        Record rec3 = new Record();
        rec3.setDoctorId(doc1.getId());
        rec3.setPatientId(pat2.getId());
        rec3.setChiefComplaint("头痛、鼻塞、流涕3天");
        rec3.setHistory("患者三天前出现头痛、鼻塞、流清涕，伴有咽痛。无发热。");
        rec3.setPhysicalExam("T:36.8℃。咽部轻度充血，双肺呼吸音清。");
        rec3.setDescription("普通感冒");
        rec3.setAffirm(1);
        rec3.setIcdCode("J00");
        rec3.setPrescription("1. 感冒清热颗粒 1袋 bid×3d\n2. 维生素C片 0.2g tid×3d");
        rec3.setFollowup("注意休息，多饮水。如症状加重及时就诊。");
        rec3.setVisitNo("VIS20260712001");
        rec3.setOffice("内科");
        rec3.setHospital("市第一人民医院");
        rec3.setDoctorSignature(doc1.getName());
        rec3.setCreatetime(LocalDateTime.of(2026, 7, 12, 10, 0));
        recordService.save(rec3);

        // ========== 创建挂号记录 ==========
        Appointment appt1 = new Appointment();
        appt1.setPatientId(pat1.getId());
        appt1.setDoctorId(doc1.getId());
        appt1.setOffice("内科");
        appt1.setHospital("市第一人民医院");
        appt1.setAppointmentDate(LocalDate.now());
        appt1.setTimeSlot("上午");
        appt1.setChiefComplaint("复查血常规，近期仍有咳嗽");
        appt1.setStatus(0);
        appointmentService.save(appt1);

        Appointment appt2 = new Appointment();
        appt2.setPatientId(pat2.getId());
        appt2.setDoctorId(doc2.getId());
        appt2.setOffice("外科");
        appt2.setHospital("市第一人民医院");
        appt2.setAppointmentDate(LocalDate.now());
        appt2.setTimeSlot("上午");
        appt2.setChiefComplaint("术后复查");
        appt2.setStatus(0);
        appointmentService.save(appt2);

        Appointment appt3 = new Appointment();
        appt3.setPatientId(pat3.getId());
        appt3.setDoctorId(doc1.getId());
        appt3.setOffice("内科");
        appt3.setHospital("市第一人民医院");
        appt3.setAppointmentDate(LocalDate.now());
        appt3.setTimeSlot("下午");
        appt3.setChiefComplaint("高血压复诊，近一周血压控制不佳");
        appt3.setStatus(0);
        appointmentService.save(appt3);

        System.out.println("========================================");
        System.out.println("  ✓ 测试数据初始化完成");
        System.out.println("  ✓ 4 位医生（内科/外科/儿科/眼科）");
        System.out.println("  ✓ 3 位患者（已含过敏史、地址等完整信息）");
        System.out.println("  ✓ 3 条病历记录（含完整诊断、处方、检查报告）");
        System.out.println("  ✓ 3 条今日挂号记录");
        System.out.println("  密码统一为 123456");
        System.out.println("========================================");
    }
}
