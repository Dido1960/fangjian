package com.ejiaoyi.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 附件
 * </p>
 *
 * @author Z0001
 * @since 2020-03-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("upload_file")
public class UploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 文件uuid
     */
    @TableField("FILE_UID")
    private String fileUid;

    /**
     * 数据时间戳
     */
    @TableField("INSERT_TIME")
    private String insertTime;

    /**
     * 文件名
     */
    @TableField("NAME")
    private String name;

    /**
     * 后缀名
     */
    @TableField("SUFFIX")
    private String suffix;

    /**
     * 路径
     */
    @TableField("PATH")
    private String path;

    /**
     * 字节大小
     */
    @TableField("BYTE_SIZE")
    private Integer byteSize;

    /**
     * 易读性大小(自动转换单位)
     */
    @TableField("READ_SIZE")
    private String readSize;

    /**
     * 文件被调用的次数
     */
    @TableField("CALL_COUNT")
    private String callCount;

    /***自定义字段****/
    /****
     * 公网访问地址
     * **/
    @JsonProperty("url")
    @JSONField(name = "url")
    @ApiModelProperty(value = "公网访问地址", example = "12")
    @TableField(exist = false)
    private String url;
}
