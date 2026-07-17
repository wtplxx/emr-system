SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS doctor;
CREATE TABLE doctor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    phone BIGINT NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    hospital VARCHAR(100),
    office VARCHAR(50),
    publickey TEXT,
    employee_no VARCHAR(50),
    title_level INT,
    license_no VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS patient;
CREATE TABLE patient (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    phone BIGINT NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    sex INT,
    age INT,
    publickey TEXT,
    id_card VARCHAR(18),
    blood_type VARCHAR(10),
    allergy_history TEXT,
    emergency_phone VARCHAR(20),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS record;
CREATE TABLE record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    createtime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    chief_complaint VARCHAR(500),
    history TEXT,
    physical_exam TEXT,
    examination TEXT,
    description TEXT,
    remark TEXT,
    description_enbyp TEXT,
    remark_enbyp TEXT,
    affirm INT DEFAULT 0,
    icd_code VARCHAR(20),
    prescription TEXT,
    followup TEXT,
    doctor_signature VARCHAR(255),
    visit_no VARCHAR(50),
    office VARCHAR(50),
    hospital VARCHAR(100),
    CONSTRAINT fk_record_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    CONSTRAINT fk_record_patient FOREIGN KEY (patient_id) REFERENCES patient(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS authorize;
CREATE TABLE authorize (
    id INT PRIMARY KEY AUTO_INCREMENT,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    record_id INT NOT NULL,
    auth_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expire_time TIMESTAMP,
    status INT DEFAULT 0,
    CONSTRAINT fk_auth_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id),
    CONSTRAINT fk_auth_patient FOREIGN KEY (patient_id) REFERENCES patient(id),
    CONSTRAINT fk_auth_record FOREIGN KEY (record_id) REFERENCES record(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS appointment;
CREATE TABLE appointment (
    id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT,
    office VARCHAR(50) NOT NULL COMMENT '就诊科室',
    hospital VARCHAR(100) COMMENT '就诊医院',
    appointment_date DATE NOT NULL COMMENT '预约日期',
    time_slot VARCHAR(20) NOT NULL COMMENT '时间段：上午/下午',
    chief_complaint VARCHAR(500) COMMENT '主诉',
    status INT DEFAULT 0 COMMENT '0待就诊 1已完成 2已取消',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_appt_patient FOREIGN KEY (patient_id) REFERENCES patient(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS department;
CREATE TABLE department (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    hospital_id INT,
    sort_order INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS operation_log;
CREATE TABLE operation_log (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    user_role VARCHAR(20) NOT NULL,
    action VARCHAR(100) NOT NULL,
    target_type VARCHAR(50),
    target_id INT,
    ip_address VARCHAR(50),
    operate_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    detail TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS=1;
