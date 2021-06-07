package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ejiaoyi.common.dto.BidderReviewPointDTO;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 投标人信息
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
@TableName("bidder")
public class Bidder implements Serializable {

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
     * 标段信息主键ID
     */
    @TableField("BID_SECTION_ID")
    private Integer bidSectionId;

    /**
     * 投标人名称
     */
    @TableField("BIDDER_NAME")
    private String bidderName;

    /**
     * 投标人类别
     */
    @TableField("BIDDER_CODE_TYPE")
    private String bidderCodeType;

    /**
     * 投标单位项目负责人(用于对接数据接收)
     */
    @TableField("BID_MANAGER")
    private String bidManager;

    /**
     * 法人或授权委托人(用于对接数据接收)
     */
    @TableField("LEGAL_PERSON")
    private String legalPerson;

    /**
     * 联系电话(用于对接数据接收)
     */
    @TableField("PHONE")
    private String phone;

    /**
     * 投标人统一社会信用代码
     */
    @TableField("BIDDER_ORG_CODE")
    private String bidderOrgCode;

    /**
     * 投标文件附件主键ID（关联UPLOAD_FILE主键ID）
     */
    @TableField("BID_DOC_ID")
    private Integer bidDocId;

    /**
     * 投标文件附件类型 0:gef或tjy  1:sgef或etjy
     */
    @TableField("BID_DOC_TYPE")
    private Integer bidDocType;

    /**
     * 是否通过开标（即能够进入评标系统）
     * 0:不通过  1:通过（默认）
     */
    @TableField("IS_PASS_BID_OPEN")
    private Integer isPassBidOpen;

    /**
     * 数据来源  参见数据字典dataSource
     */
    @TableField("DATA_FROM")
    private Integer dataFrom;

    /**
     * 是否删除
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /**
     * 删除原因
     */
    @TableField("DELETE_REASON")
    private String deleteReason;

    /******************************自定义字段******************************/

    /**
     * 投标人开标信息
     */
    @TableField(exist = false)
    private BidderOpenInfo bidderOpenInfo;

    /**
     * 投标人开标文件信息
     */
    @TableField(exist = false)
    private BidderFileInfo bidderFileInfo;

    /**
     * 投标报价金额（大写）
     */
    @TableField(exist = false)
    private String bidPriceChinese;

    /**
     * 投标报价金额（小写）
     */
    @TableField(exist = false)
    private String bidPrice;

    /**
     * 投标报价类型
     */
    @TableField(exist = false)
    private String bidPriceType;

    /**
     * 投标保证金缴纳状态(1:已缴纳;0:未缴纳)
     */
    @TableField(exist = false)
    private String marginPay;

    /**
     * 投标工期
     */
    @TableField(exist = false)
    private String timeLimit;

    /**
     * 工程质量(施工)
     */
    @TableField(exist = false)
    private String quality;

    /** 专家评审情况
     * 专家对于某个企业的特定打分表是否评审完毕
     */
    @TableField(exist = false)
    private boolean expertReviewStatusForBidder;

    /**
     * 投标人评审点信息
     */
    @TableField(exist = false)
    private List<BidderReviewPointDTO> bidderReviewPointDTOS;

    /**
     * 专家对企业评审单项评审结果集合 (合格制)
     */
    @TableField(exist = false)
    private List<ExpertReviewSingleItem> expertReviewSingleItems;

    /**
     * 专家对企业评审单项评审结果集合 (扣分制)
     */
    @TableField(exist = false)
    private List<ExpertReviewSingleItemDeduct> expertReviewSingleItemDeducts;

    /**
     * 当前grade是否意见一致 0：不合格 1：合格 2：不同意
     */
    @TableField(exist = false)
    private Integer isConsistent;

    /**
     * 某评分标准结果
     */
    @TableField(exist = false)
    private String gradeResult;

    /**
     * 投标文件解密状态(0:未解密;1:已解密;2:解密完成)
     */
    @TableField(exist = false)
    private Integer decryptStatus;

    /**
     * 解密时间 分钟数
     * 根据解密开始时间和结束时间计算
     */
    @TableField(exist = false)
    private String decryptTimeMinute;

    /**
     * 解密时间 秒数
     * 根据解密开始时间和结束时间计算
     */
    @TableField(exist = false)
    private String decryptTimeSecond;

    /**
     * 不合格评审项数量/扣分评审数量
     */
    @TableField(exist = false)
    private int hasUnqualifiedCount;

    /**
     * 初步评审结果（1合格/0不合格）
     */
    @TableField(exist = false)
    private String preReviewResult;

    /**
     * 资格预审详细评审结果（1合格/0不合格）
     */
    @TableField(exist = false)
    private String detailReviewResult;

    /**
     * 资格预审总的评审结果（1合格/0不合格）
     */
    @TableField(exist = false)
    private String reviewResult;

    /**
     *  中标候选人信息
     */
    @TableField(exist = false)
    private CandidateSuccess candidateSuccess;

    /**
     *  勘察设计 前三名的票数
     */
    @TableField(exist = false)
    private List<Integer> voteNums;

    /**
     * 所有专家的评审总分(合格制为该项是否通过，打分制为该项的总分，平均分)
     * 说明：合格制，0/空 不通过；1:通过
     */
    @TableField(exist = false)
    private String totalResult;

    /**
     * 当前企业报价得分
     */
    @TableField(exist = false)
    private QuoteScoreResult quoteScoreResult;

    /**
     * 当前企业修正后得报价得分
     */
    @TableField(exist = false)
    private QuoteScoreResultAppendix quoteScoreResultAppendix;

    /**
     *  勘察设计 投标人获得第一名，所有专家票数
     */
    @TableField(exist = false)
    private Integer oneBidderVotes;

    /**
     *  勘察设计 投标人获得第二名，所有专家票数
     */
    @TableField(exist = false)
    private Integer twoBidderVotes;

    /**
     *  勘察设计 投标人获得第三名，所有专家票数
     */
    @TableField(exist = false)
    private Integer threeBidderVotes;

    /**
     * 投标人被否决的流程
     * 0未被否决 1开标 2评标
     */
    @TableField(exist = false)
    private Integer vetoFlow;

    /**
     * 投标人被否决的理由
     */
    @TableField(exist = false)
    private String vetoReason;

    /**
     * 回执单id
     */
    @TableField(exist = false)
    private Integer receiptFileId;

    /**
     *  质询状态（有无异议）
     *  唱标质询状态 1.有异议 0无异议
     */
    @TableField(exist = false)
    private Integer question;

    /**
     *  质询状态（有无异议）
     *  唱标质询状态 1.有异议 0无异议
     */
    @TableField(exist = false)
    private Integer result;

    /******************************自定义方法******************************/

}
