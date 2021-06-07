package com.ejiaoyi.common.dto;

import com.ejiaoyi.common.entity.BidderOpenInfo;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 作为接受检查的数据传递实体
 * @Auther: liuguoqiang
 * @Date: 2020-7-28 09:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndingCheck implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 当前对应标段ID
     */
    private Integer bidSectionId;

    /**
     * 当前标段是否为资格预审
     */
    private Boolean isA10Bid;

    /**
     * 存储原因列表
     */
    private List<BidderOpenInfo> boiList;

    /**
     * 当前以处理人数
     */
    private Integer isCheck;

    /**
     * 当前未处理人数
     */
    private Integer notCheck;

    /**
     * 列表大小
     */
    private Integer listSize;
}
