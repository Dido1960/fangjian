package com.ejiaoyi.expert.dto;

import com.ejiaoyi.common.entity.BidderQuantity;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 清标进度DTO
 * @author Make
 * @since 2020-12-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClearBidProcessDTO implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * 总进度百分比
     */
    public String totalPercentage;

    /**
     * 已完成数量
     **/
    public int completeNum;

    /**
     * 未完成数量
     **/
    public int notCompleteNum;

    /**
     * 投标人工程量清单服务信息集合
     **/
    public List<BidderQuantity> bidderQuantities;

}
