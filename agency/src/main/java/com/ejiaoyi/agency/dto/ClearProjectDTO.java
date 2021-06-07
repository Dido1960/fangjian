package com.ejiaoyi.agency.dto;

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
     * 创建人
     */
    private String creatUserName;

    /**
     * 业务code前缀
     */
    private String ywCodePrefix;

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
     * 投标人名称
     */
    private List<String> bidderNames;

    /**
     * 投标人URL
     */
    private List<String> bidderXmlUrls;
}
