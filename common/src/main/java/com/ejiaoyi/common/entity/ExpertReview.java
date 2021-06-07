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
 * <p>
 * 专家评审表
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("expert_review")
public class ExpertReview implements Serializable {

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
     * 评审专家
     */
    @TableField("EXPERT_ID")
    private Integer expertId;

    /**
     * 标段ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 评分标准ID
     */
    @TableField("GRADE_ID")
    private Integer gradeId;

    /**
     * 评审开始时间
     */
    @TableField("START_TIME")
    private String startTime;

    /**
     * 评审结束时间
     */
    @TableField("END_TIME")
    private String endTime;

    /**
     * 初始化状态 参见数据字典bool
     */
    @TableField("INIT_STATUS")
    private Integer initStatus;

    /**
     * 启用状态  参见数据字典enabled
     */
    @TableField("ENABLED")
    private Integer enabled;




    /****************以上主要为系统表字段********************/

    /**
     * 评标分数集合
     */
    @TableField(exist = false)
    private String[] gradeIds;


    /****************以上为自己添加字段********************/

}
