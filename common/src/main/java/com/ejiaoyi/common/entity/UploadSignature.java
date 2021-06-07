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
 * 上传文件签章记录表
 * </p>
 *
 * @author langwei
 * @since 2020-07-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("upload_signature")
public class UploadSignature implements Serializable {

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
     * 上传文件ID
     */
    @TableField("UPLOAD_ID")
    private Integer uploadId;

    /**
     * 签章人名字
     */
    @TableField("USER_NAME")
    private String userName;


}
