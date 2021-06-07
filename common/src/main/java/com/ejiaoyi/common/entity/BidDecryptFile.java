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
 * 投标文件解密表
 * </p>
 *
 * @author samzqr
 * @since 2020-08-14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bid_decrypt_file")
public class BidDecryptFile implements Serializable {

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
     * 文件标记
     */
    @TableField("MARK")
    private String mark;

    /**
     * 投标文件解密成功后上传,对应fdfs表主键
     */
    @TableField("FDFS_ID")
    private Integer fdfsId;

    /**
     * 文件被读取次数
     */
    @TableField("CALL_COUNT")
    private String callCount;


}
