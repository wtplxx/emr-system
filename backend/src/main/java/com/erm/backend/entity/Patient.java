package com.erm.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 患者表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 姓名 */
    private String name;

    /** 手机号(登录账号) */
    private Long phone;

    /** 登录密码(BCrypt加密) */
    private String password;

    /** 性别 1男 2女 */
    private Integer sex;

    /** 年龄 */
    private Integer age;

    /** 患者公钥 */
    private String publickey;

    /** 身份证号 */
    private String idCard;

    /** 血型 */
    private String bloodType;

    /** 过敏史 */
    private String allergyHistory;

    /** 紧急联系人电话 */
    private String emergencyPhone;

    /** 地址 */
    private String address;
}