package com.ejiaoyi.common.dto;

import com.ejiaoyi.common.entity.BidderException;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:作为身份检查的返回实体，存储数据
 * @Auther: liuguoqiang
 * @Date: 2020/7/22 11:22
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientCheckOneDTO implements Serializable {
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
     * 当前需要修改的投标人,该id对应BidderOpenInfo主键
     */
    private Integer bidderIdNow;

    /**
     * 需要跳转的投标人，该id对应BidderOpenInfo主键
     */
    private Integer bidderIdTo;

    /**
     * 更新的类型 identity:身份验证   marginPay：保证金验证
     */
    private String checkType;

    /**
     * 是否通过验证
     */
    private Integer passType;

    /**
     * 当前以处理人数
     */
    private Integer isCheck;

    /**
     * 当前未处理人数
     */
    private Integer notCheck;

    /**
     * 需要跳转的投标人数据
     */
    private BidderOpenInfo bidderOpenInfo;

    /**
     * 需要跳转的招标，上一个投标人
     */
    private BidderOpenInfo lastBoi;

    /**
     * 需要跳转的招标，下一个投标人
     */
    private BidderOpenInfo nextBoi;

    /**
     * 对应的身份异常理由
     */
    private BidderException identityBidderException;

    /**
     * 对应的保证金异常理由
     */
    private BidderException marginPayBidderException;

    /**
     * 是否为查看状态
     */
    private Boolean isShow;

}
