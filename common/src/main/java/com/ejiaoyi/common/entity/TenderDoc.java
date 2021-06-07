package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 招标文件信息
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
@TableName("tender_doc")
public class TenderDoc implements Serializable {

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
     * 文件编号
     */
    @TableField("DOC_NO")
    private String docNo;

    /**
     * 投标资格
     */
    @TableField("BIDDER_QUALIFICATION")
    private String bidderQualification;

    /**
     * 投标有效期
     */
    @TableField("VALID_PERIOD")
    private String validPeriod;

    /**
     * 投标文件递交方法
     */
    @TableField("BID_DOC_PEFER_TYPE")
    private String bidDocPeferType;

    /**
     * 招标文件费金额(单位：元)
     */
    @TableField("TENDER_DOC_PRICE")
    private String tenderDocPrice;

    /**
     * 投标保证金金额(单位：元)
     */
    @TableField("MARGIN_AMOUNT")
    private String marginAmount;

    /**
     * 控制价(最高限价 单位：元)
     */
    @TableField("CONTROL_PRICE")
    private String controlPrice;

    /**
     * 评标办法
     */
    @TableField("EVALUATION_METHOD")
    private String evaluationMethod;

    /**
     * 投标文件递交截止时间
     */
    @TableField("BID_DOC_REFER_END_TIME")
    private String bidDocReferEndTime;

    /**
     * 开标时间
     */
    @TableField("BID_OPEN_TIME")
    private String bidOpenTime;

    /**
     * 开标地点
     */
    @TableField("BID_OPEN_PLACE")
    private String bidOpenPlace;

    /**
     * 开标方式
     */
    @TableField("BID_OPEN_TYPE")
    private String bidOpenType;

    /**
     * 资格审查方式
     */
    @TableField("QUAL_TYPE")
    private String qualType;

    /**
     * 递交时间
     */
    @TableField("SUBMIT_TIME")
    private String submitTime;

    /**
     * 招标文件ID（关联UPLOAD_FILE主键ID）
     */
    @TableField("DOC_FILE_ID")
    private Integer docFileId;

    /**
     * 关联评标办法grade,多个评标办法间用逗号隔开
     */
    @TableField("GRADE_ID")
    private String gradeId;

    /**
     * 其他评审ID，关联评标办法grade,多个评标办法间用逗号隔开
     */
    @TableField("OTHER_GRADE_ID")
    private String otherGradeId;

    /**
     * 甘肃评标方法中的浮动点(取值范围1.5%、1.25%、1%、0.75%、0.5%、0.25%)
     */
    @TableField("FLOAT_POINT")
    private String floatPoint;

    /**
     * 工程量清单文件标识码
     */
    @TableField("XML_UID")
    private String xmlUid;

    /**
     * 税金系数
     */
    @TableField("TAXES_FACTOR")
    private String taxesFactor;

    /**
     * 结构性分析启用状态 参见数据字典bool
     */
    @TableField("STRUCTURE_STATUS")
    private Integer structureStatus;

    /**
     * 零负报价分析启用状态 参见数据字典bool
     */
    @TableField("PRICE_STATUS")
    private Integer priceStatus;

    /**
     * 雷同项分析启用状态 参见数据字典bool
     */
    @TableField("SIMILAR_ITEM_STATUS")
    private Integer similarItemStatus;

    /**
     * 取费基础分析启用状态 参见数据字典bool
     */
    @TableField("FUND_BASIS_STATUS")
    private Integer fundBasisStatus;

    /**
     * 互保共建启用状态 参见数据字典bool
     */
    @TableField("MUTUAL_SECURITY_STATUS")
    private Integer mutualSecurityStatus;

    /**
     * 信用报告评审启用状态 参见数据字典bool
     */
    @TableField("CREDIT_STATUS")
    private Integer creditStatus;

    /**
     * 招标CA序列号
     */
    @TableField("CERT_NUM")
    private String certNum;

    /**
     * 专家人数
     */
    @TableField("EXPERT_COUNT")
    private Integer expertCount;

    /**
     * 业主代表人数
     */
    @TableField("REPRESENTATIVE_COUNT")
    private Integer representativeCount;

    /**
     * 开标记录表备注
     */
    @TableField("OPEN_BID_RECORD_DES")
    private String openBidRecordDes;

    /**
     * 招标文件版本
     */
    @TableField("VERSION")
    private Integer version;

    /**
     * 是否删除
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /******************************自定义字段******************************/
}
