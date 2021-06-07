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
 * 字典表
 * </p>
 *
 * @author fengjunhong
 * @since 2020/4/30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wordbook")
public class Wordbook implements Serializable {

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
     * TOP KEY
     */
    @TableField("BOOK_TOP_KEY")
    private String bookTopKey;

    /**
     * PARENT KEY
     */
    @TableField("BOOK_PARENT_KEY")
    private String bookParentKey;

    /**
     * KEY
     */
    @TableField("BOOK_KEY")
    private String bookKey;

    /**
     * VALUE
     */
    @TableField("BOOK_VALUE")
    private String bookValue;
}
