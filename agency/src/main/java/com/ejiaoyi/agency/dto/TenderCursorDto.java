package com.ejiaoyi.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 招标文件唱标内容
 *
 * @author fengjunhong
 * @since 2020-7-20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TenderCursorDto {
    /**
     * 标段名称
     */
    String bidderName;

    /**
     * 标段分类代码
     */
    String bidderCode;

    /**
     * 标段编号
     */
    String bidderNo;

    /**
     * 招标控制价
     */
    String bidderMaxPrice;

    /**
     * 开标地点
     */
    String openPlace;

}
