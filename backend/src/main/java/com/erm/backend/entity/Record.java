package com.erm.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 电子病历表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 医生ID */
    private Integer doctorId;

    /** 患者ID */
    private Integer patientId;

    /** 档案创建时间 */
    private LocalDateTime createtime;

    /** 主诉 */
    private String chiefComplaint;

    /** 现病史 */
    private String history;

    /** 体格检查 */
    private String physicalExam;

    /** 辅助检查 */
    private String examination;

    /** 医生诊断(患者公钥加密) */
    private String descriptionEnbyp;

    /** 备注(患者公钥加密) */
    private String remarkEnbyp;

    /** 是否确认 0未确认 1已确认 */
    private Integer affirm;

    /** 诊断ICD编码 */
    private String icdCode;

    /** 处方信息(JSON格式) */
    private String prescription;

    /** 跟进建议 */
    private String followup;

    /** 电子签名 */
    private String doctorSignature;

    /** 就诊号(挂号号) */
    private String visitNo;

    /** 科室(冗余) */
    private String office;

    /** 医院(冗余) */
    private String hospital;

    /** 原始诊断描述(非加密) */
    private String description;

    /** 原始备注(非加密) */
    private String remark;
}