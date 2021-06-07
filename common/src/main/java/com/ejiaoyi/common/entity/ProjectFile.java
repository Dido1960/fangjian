package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目文件信息
 * </p>
 *
 * @author Make
 * @since 2020-07-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("project_file")
public class ProjectFile implements Serializable {

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
     * 标段主键
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 投标人主键
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * FDFS关联主键
     */
    @TableField("FDFS_ID")
    private Integer fdfsId;

    /**
     * 文件类型
     */
    @TableField("FILE_TYPE")
    private Integer fileType;

    /**
     * 文件
     */
    @TableField("FILE")
    private String file;

    /**
     * 是否删除
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;


}
