package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 区块信息表
 * </p>
 *
 * @author samzqr
 * @since 2020-08-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bsn_chain_info")
public class BsnChainInfo implements Serializable {

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
     * 投标人息主键ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 标段id
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 区块数据类型（1：招标文件存根  2：开标记录表  3投标文件解密记录）
     */
    @TableField("TYPE")
    private Integer type;

    /**
     * 区块数据id
     */
    @TableField("TX_ID")
    private String txId;

    /**
     * 返回数据状态码
     */
    @TableField("STATUS")
    private String status;

    /**
     * 返回key值
     */
    @TableField("BASE_KEY")
    private String baseKey;

    /**
     * 数据请求地址
     */
    @TableField("QUERY_ADDRESS")
    private String queryAddress;


}
