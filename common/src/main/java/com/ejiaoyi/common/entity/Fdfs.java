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
 * FastDFS文件
 * </p>
 *
 * @author lesgod
 * @since 2020-05-14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("fdfs")
public class Fdfs implements Serializable {

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
     * 文件标记
     */
    @TableField("MARK")
    private String mark;

    /**
     * 文件组名
     */
    @TableField("DFS_GROUP")
    private String dfsGroup;

    /**
     * 文件地址
     */
    @TableField("DFS_ADDRESS")
    private String dfsAddress;

    /**
     * 文件访问地址
     */
    @TableField("URL")
    private String url;

    /**
     * 后缀名
     */
    @TableField("SUFFIX")
    private String suffix;

    /**
     * 文件名
     */
    @TableField("NAME")
    private String name;

    /**
     * 文件Hash值
     */
    @TableField("FILE_HASH")
    private String fileHash;

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
     * wav音频时长
     */
    @TableField("WAV_DURATION")
    private Integer wavDuration;


    /**************************************  自定义字段 ********************************/

    /**
     * 文件字节流
     */
    @TableField(exist = false)
    private byte[] bytes;

}
