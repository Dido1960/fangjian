package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;

/**
 * 当前转换pdf
 * @author fengjunhong
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NowConvertPdf implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * 专家
     */
    public String expertName;

    /**
     * 当前转换的模板名称
     */
    public String templateName;

    /**
     * 状态 0,待转换 1,转换成功 2,转换失败
     **/
    public int status;

}
