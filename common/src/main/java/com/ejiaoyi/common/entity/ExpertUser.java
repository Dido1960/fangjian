package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 评标专家
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
@TableName("expert_user")
public class ExpertUser implements Serializable {

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
     * 拼音 全拼
     */
    @TableField("ALL_SPELLING")
    private String allSpelling;

    /**
     * 拼音 首字母
     */
    @TableField("FIRST_SPELLING")
    private String firstSpelling;

    /**
     * 行政区划主键(用于区分远程异地评标主客场)
     */
    @TableField("REG_ID")
    private Integer regId;

    /**
     * 证件号码
     */
    @TableField("ID_CARD")
    private String idCard;

    /**
     * 启用状态  参见数据字典enabled
     */
    @TableField("ENABLED")
    private Integer enabled;

    /**
     * 标段信息主键ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 评标申请记录主键ID
     */
    @TableField("BID_APPLY_ID")
    private Integer bidApplyId;

    /**
     * 专家名称
     */
    @TableField("EXPERT_NAME")
    private String expertName;

    /**
     * 手机号码
     */
    @TableField("PHONE_NUMBER")
    private String phoneNumber;

    /**
     * 专家单位
     */
    @TableField("COMPANY")
    private String company;

    /**
     * 候选资格状态，用于组长推选 0:失去候选资格)
     */
    @TableField("LEADER_STATUS")
    private String leaderStatus;

    /**
     * 随机密码
     */
    @TableField("PWD")
    private String pwd;

    /**
     * 专家签到时间
     */
    @TableField("CHECKIN_TIME")
    private String checkinTime;

    /**
     * 是否为评标组长
     */
    @TableField("IS_CHAIRMAN")
    private String isChairman;

    /**
     * 专家类别 参见数据字典expertCategory
     */
    @TableField("CATEGORY")
    private Integer category;

    /**
     * 密码明文
     */
    @TableField("PASS_WORD")
    private String passWord;

    /**
     * 职称
     */
    @TableField("PROFESSIONAL")
    private String professional;

    /**
     * 专家状态（包含主动回避和签到  null: 未签到 0:已确认 1:已回避 2:已签到）
     */
    @TableField("AVOID")
    private String avoid;

    /**
     * 回避原因
     */
    @TableField("REASON")
    private String reason;

    /**
     * 是否签名完成，1代表签名结束
     */
    @TableField("SIGNAR")
    private Integer signar;

    /**
     * 数据类型（1:正常推送专家， 2：补抽专家）
     */
    @TableField("DATA_TYPE")
    private String dataType;

    /**
     * 未启用原因(enabled=0时存在)
     */
    @TableField("UNAVAILABLE_REASON")
    private String unavailableReason;

    /******************************自定义字段******************************/

    /**
     * 评委身份中文
     */
    @TableField(exist = false)
    private String categoryName;

    /**
     * 得票数
     */
    @TableField(exist = false)
    private Integer count;

    /** 专家评审情况 （合格制）
     * 专家对于某个企业的特定打分表
     */
    @TableField(exist = false)
    private List<ExpertReviewSingleItem> expertReviewSingleItems;

    /** 专家评审情况 （扣分制）
     * 专家对于某个企业的特定打分表
     */
    @TableField(exist = false)
    private List<ExpertReviewSingleItemDeduct> expertReviewSingleItemDeducts;

    /**
     * 获取专家推选中标候选人
     */
    @TableField(exist = false)
    private List<CandidateResults> candidateResults;

    /**
     * 评委身份转换
     */
    public String getCategoryName() {
        if (this.category == null) {
            return "";
        }
        if (this.category == 1) {
            return "经济标专家";
        }
        if (this.category == 2) {
            return "技术标专家";
        }
        if (this.category == 3) {
            return "业主代表";
        }
        return "";
    }


}
