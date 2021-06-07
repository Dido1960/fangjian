package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 总承包报价得分的计算过程数据
 *
 * @author Z0001
 * @since 2020-07-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("general_contract_calc")
public class GeneralContractCalc implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 数据时间戳
     */
    @TableField("INSERT_TIME")
    private String insertTime;

    /**
     * 标段主键ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 最高价
     */
    @TableField("MAX_PRICE")
    private String maxPrice;

    /**
     * 最低价
     */
    @TableField("MIN_PRICE")
    private String minPrice;

    /**
     * 去掉最高价和最低价的合价
     */
    @TableField("SUM_PRICE")
    private String sumPrice;

    /**
     * 算术平均值计算数量
     */
    @TableField("SIZE")
    private String size;

    /**
     * 评标基准价
     */
    @TableField("PRICE_AVG")
    private String priceAvg;

    /**
     * 版本号(对应招标文件version)
     */
    @TableField("VERSION")
    private Integer version;

    /******************************自定义字段******************************/
}
