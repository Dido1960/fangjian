package com.ejiaoyi.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 手机扫码登录信息
 *
 * @author Mike
 * @since 2021/3/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MobilePhoneScanCodeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 二维码登录地址
     */
    @JsonProperty("address")
    @JSONField(name = "address")
    private String address;

    /**
     * 验证编码
     */
    @JsonProperty("verifyId")
    @JSONField(name = "verifyId")
    private String verifyId;

    /**
     * 请求状态
     */
    private Boolean httpStatus;

    /**
     * 认证登录结果所需token
     */
    private String token;
}
