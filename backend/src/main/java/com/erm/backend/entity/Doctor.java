package com.erm.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 医生表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 姓名 */
    private String name;

    /** 电话(登录账号) */
    private Long phone;

    /** 登录密码(BCrypt加密) */
    private String password;

    /** 所属医院 */
    private String hospital;

    /** 所属科室 */
    private String office;

    /** 医生公钥 */
    private String publickey;

    /** 工号 */
    private String employeeNo;

    /** 职称 1主任医师 2副主任医师 3主治医师 4住院医师 */
    private Integer titleLevel;

    /** 执业证书号 */
    private String licenseNo;
}