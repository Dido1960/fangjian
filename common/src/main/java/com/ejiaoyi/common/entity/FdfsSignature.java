package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * fdfs文件签章记录表
 * </p>
 *
 * @author Mike
 * @since 2021-04-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fdfs_signature")
public class FdfsSignature implements Serializable {

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
     * 原来路径
     */
    @TableField("OLD_PATH")
    private String oldPath;

    /**
     * 现路径
     */
    @TableField("NOW_PATH")
    private String nowPath;

    /**
     * FDFS文件ID
     */
    @TableField("FDFS_ID")
    private Integer fdfsId;

    /**
     * 签章人名字
     */
    @TableField("USER_NAME")
    private String userName;


}
