package com.erm.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 病历授权表
 * </p>
 *
 * @author admin
 * @since 2026-06-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Authorize implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 授权记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 授权医生ID
     */
    private Integer doctorId;

    /**
     * 被授权患者ID
     */
    private Integer patientId;

    /**
     * 授权的病历ID
     */
    private Integer recordId;

    /**
     * 授权时间
     */
    private LocalDateTime authTime;

    /**
     * 授权过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 授权状态 1有效 0失效
     */
    private Integer status;


}