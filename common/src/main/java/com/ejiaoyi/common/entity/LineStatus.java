package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 网上开标状态
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
@TableName("line_status")
public class LineStatus implements Serializable {

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
     * 标段信息主键ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 文件上传状态 1:开始文件上传 2:文件上传结束
     */
    @TableField("UPLOAD_STATUS")
    private Integer uploadStatus;

    /**
     * 签到状态 1:开始签到 2:签到结束
     */
    @TableField("SIGNIN_STATUS")
    private Integer signinStatus;

    /**
     * 公布投标人状态 1:开始 2:结束
     */
    @TableField("PUBLISH_BIDDER_STATUS")
    private Integer publishBidderStatus;

    /**
     * 投标人身份检查状态 1:投标人身份校验 2:投标人身份校验结束
     */
    @TableField("BIDDER_CHECK_STATUS")
    private Integer bidderCheckStatus;

    /**
     * 解密状态 1:开始解密 2:解密结束
     */
    @TableField("DECRYPTION_STATUS")
    private Integer decryptionStatus;

    /**
     * 解密累计用时（秒）
     */
    @TableField("DECRYPTION_TIME")
    private String  decryptionTime;

    /**
     * 质询累计用时（秒）
     */
    @TableField("QUESTION_TIME")
    private String  questionTime;

    /**
     * 复会累计用时（秒）
     */
    @TableField("RESUME_TIME")
    private String  resumeTime;

    /**
     * 解密时间段json字符串
     */
    @TableField("DECRYPTION_PERIODS")
    private String decryptionPeriods;

    /**
     * 质询时间段json字符串
     */
    @TableField("QUESTION_PERIODS")
    private String questionPeriods;

    /**
     * 复会时间段json字符串
     */
    @TableField("RESUME_PERIODS")
    private String resumePeriods;

    /**
     * 质疑状态 1：开始质疑 2：结束质疑
     */
    @TableField("QUESTION_STATUS")
    private Integer questionStatus;

    /**
     * 复会状态 1:开始复会 2:结束复会
     */
    @TableField("RESUME_STATUS")
    private Integer resumeStatus;

    /******************************自定义字段******************************/

    /**
     * 消息
     */
    @TableField(exist = false)
    private String msg;
}
