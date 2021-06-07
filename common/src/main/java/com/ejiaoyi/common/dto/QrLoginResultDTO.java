package com.ejiaoyi.common.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ejiaoyi.common.entity.CompanyUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 手机扫码登录结果
 *
 * @author Mike
 * @since 2021/3/12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrLoginResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求结果状态码
     */
    @JsonProperty("code")
    @JSONField(name = "code")
    private String code;

    /**
     * 请求结果信息
     */
    @JsonProperty("msg")
    @JSONField(name = "msg")
    private String msg;

    /**
     * 用户信息
     */
    private CompanyUser companyUser;

    /**
     * 手机证书信息
     */
    private List<PhoneCertDTO> phoneCertDTOList;
}
