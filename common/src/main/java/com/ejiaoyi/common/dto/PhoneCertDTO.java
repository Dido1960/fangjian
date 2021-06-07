package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 手机证书订单
 * </p>
 *
 * @author Mike
 * @since 2020-12-23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneCertDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 企业证书会有此BJCAID,非企业证书无此ID
     */
    private String entId;

    /**
     * 签章图片base64
     */
    private String fileDate;

    /**
     * 锁号
     */
    private String keyId;

    /**
     * 证书类型 0 按次使用 1年用户
     */
    private String keyType;

    /**
     * 剩余次数
     */
    private String remainingTime;

    /**
     * 证书名称
     */
    private String name;

    /**
     * 签名证书
     */
    private String signCert;

    /**
     * 证书序列号
     */
    private String signCertSn;

    /**
     * 证书类型 1：企业 2个人
     */
    private Integer type;
}
