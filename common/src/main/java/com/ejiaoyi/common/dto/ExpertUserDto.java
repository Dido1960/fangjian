package com.ejiaoyi.common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 当前转换所有转换的文件
 * @author fengjunhong
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ExpertUserDto implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * 专家
     */
    public String expertName;

    /**
     * 当前转换的模板名称
     */
    public List<NowConvertPdf> nowConvertPdfs;


}
