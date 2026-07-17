package com.erm.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;

@Data
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer patientId;
    private Integer doctorId;
    private String office;
    private String hospital;
    private LocalDate appointmentDate;
    private String timeSlot;
    private String chiefComplaint;
    private Integer status;
    private LocalDateTime createTime;
}
