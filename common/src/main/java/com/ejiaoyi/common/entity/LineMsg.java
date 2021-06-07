package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId; 
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.List;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 网上开标消息
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
@TableName("line_msg")
public class LineMsg implements Serializable {

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
     * 投标人主键ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 发送人名称
     */
    @TableField("SEND_NAME")
    private String sendName;

    /**
     * 消息类型 0：系统消息 1：投标人消息 2：代理机构消息
     */
    @TableField("ROLE_TYPE")
    private Integer roleType;

    /**
     * 是否质疑信息 1：质疑信息
     */
    @TableField("QUESTION")
    private Integer question;

    /**
     * 内容信息
     */
    @TableField("MESSAGE")
    private String message;

    /**
     * 回复人
     */
    @TableField("BACK_NAME")
    private String backName;

    /**
     * 回复消息
     */
    @TableField("BACK_MSG")
    private String backMsg;

    /**
     * 对复会是否有异议 1：有异议
     */
    @TableField("RESUME")
    private Integer resume;

    /**
     * 有异议时，上传的附件id
     */
    @TableField("OBJECTION_FILE_ID")
    private Integer objectionFileId;

    /******************************自定义字段******************************/

    /**
     * 有异议时，上传的附件地址
     */
    @TableField(exist = false)
    private String objectionUrl;

    /**
     * 消息阅读情况
     */
    @TableField(exist = false)
    private List<LineMsgRead> lineMsgReads;
}
