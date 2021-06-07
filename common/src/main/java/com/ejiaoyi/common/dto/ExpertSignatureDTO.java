package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 某环节的投标人结果汇总表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ExpertSignatureDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 专家id
     */
    private Integer expertId;

    /**
     * 专家名字
     */
    private String expertName;

    /**
     * 签名图片base64值
     */
    private String imageBase64;

    /**
     * 专家索引
     */
    private Integer expertIndex;

    /**
     * 专家匹配的关键字
     */
    private List<KeyWordRuleDTO> keyWordRules;

}
