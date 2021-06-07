package com.ejiaoyi.common.dto.clearV3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 清标 项目信息
 * @author fengjunhong
 * @version 1.0
 * @date 2021-4-21 15:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClearProjectDTO {

    /**
     * 令牌
     */
    private String token;

    /**
     * 价格分计算回调地址(内网地址即可)
     */
    private String priceScoreNotify;

    /**
     * 标段主键
     */
    private Integer bidSectionId;

    /**
     * 标段编号
     */
    private String code;;

    /**
     * 创建人
     */
    private String creatUserName;

    /**
     * 业务code
     */
    private String ywCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 浮动点（%）
     */
    private String floatPoint;

    /**
     * 招标控制价
     */
    private String controlPrice;

    /**
     * 措施费控制价
     */
    private  String measuresControlPrice;

    /**
     * 招标xml url
     */
    private  String zbXmlUrl;

    /**
     * 投标人主键
     */
    private String bidderIds;

    /**
     * 投标人名称
     */
    private String bidderNames;

    /**
     * 投标人统一社会信用代码
     */
    private String bidderOrgCodes;

    /**
     * 投标人URL
     */
    private String bidderXmlUrls;
}
