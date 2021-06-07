package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 澄清答疑
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
@TableName("clarify_answer")
public class ClarifyAnswer implements Serializable {

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
     * 标段编号
     */
    @TableField("BID_SECTION_CODE")
    private String bidSectionCode;

    /**
     * 标段分类代码
     */
    @TableField("BID_CLASSIFY_CODE")
    private String bidClassifyCode;

    /**
     * 行政区划
     */
    @TableField("REGION_CODE")
    private String regionCode;

    /**
     * 文件类型（1：招标文件  2：澄清文件）
     */
    @TableField("FILE_TYPE")
    private String fileType;

    /**
     * 关联upfiles主键
     */
    @TableField("UPFILES_ID")
    private Integer upfilesId;

    /******************************自定义字段******************************/

    /**
     * 澄清文件的fdfs
     */
    @TableField(exist = false)
    private Fdfs fdfs;

}
