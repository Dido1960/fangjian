package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ejiaoyi.common.enums.BidProtype;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 标段信息
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
@TableName("bid_section")
public class BidSection implements Serializable {

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
     * 招标项目主键ID
     */
    @TableField("TENDER_PROJECT_ID")
    private Integer tenderProjectId;

    /**
     * 标段编号
     */
    @TableField("BID_SECTION_CODE")
    private String bidSectionCode;

    /**
     * 标段名称
     */
    @TableField("BID_SECTION_NAME")
    private String bidSectionName;

    /**
     * 标段内容
     */
    @TableField("BID_SECTION_CONTENT")
    private String bidSectionContent;

    /**
     * 标段分类代码
     */
    @TableField("BID_CLASSIFY_CODE")
    private String bidClassifyCode;

    /**
     * 合同估算价(单位：元)
     */
    @TableField("CONTRACT_RECKON_PRICE")
    private String contractReckonPrice;

    /**
     * 投标人资格条件
     */
    @TableField("BIDDER_QUALIFICATION")
    private String bidderQualification;

    /**
     * 计划开工日期
     */
    @TableField("PLAN_START_DATE")
    private String planStartDate;

    /**
     * 工期
     */
    @TableField("LIMITE_TIME")
    private String limiteTime;

    /**
     * 开标状态 参见数据字典status
     */
    @TableField("BID_OPEN_STATUS")
    private Integer bidOpenStatus;

    /**
     * 评标状态 参见数据字典status
     */
    @TableField("EVAL_STATUS")
    private Integer evalStatus;

    /**
     * 报价得分计算状态 参见数据字典status
     */
    @TableField("PRICE_RECORD_STATUS")
    private Integer priceRecordStatus;

    /**
     * 报价得分修改状态 参见数据字典status
     */
    @TableField("UPDATE_SCORE_STATUS")
    private Integer updateScoreStatus;

    /**
     * 报价得分修改状态 参见数据字典status
     */
    @TableField("UPDATE_SCORE_REASON")
    private String updateScoreReason;

    /**
     * 行政区划主键ID
     */
    @TableField("REG_ID")
    private Integer regId;

    /**
     * 数据来源 参见数据字典dataSource
     */
    @TableField("DATA_FROM")
    private Integer dataFrom;

    /**
     * 招标文件解密状态 0：失败  1：成功
     */
    @TableField("DOCK_TENDER_DECRY_STATUS")
    private Integer dockTenderDecryStatus;

    /**
     * 是否网上开标 1：网上开标，0：现场开标
     */
    @TableField("BID_OPEN_ONLINE")
    private Integer bidOpenOnline;

    /**
     * 是否远程异地评标 1：远程评标，0：现场评标
     */
    @TableField("REMOTE_EVALUATION")
    private Integer remoteEvaluation;

    /**
     * 开标结束时间
     */
    @TableField("BID_OPEN_END_TIME")
    private String bidOpenEndTime;

    /**
     * 评标开始时间
     */
    @TableField("EVAL_START_TIME")
    private String evalStartTime;

    /**
     * 评标结束时间
     */
    @TableField("EVAL_END_TIME")
    private String evalEndTime;

    /**
     * 流标状态 0:未做流标状态修改 1：流标 2：不流标
     */
    @TableField("CANCEL_STATUS")
    private Integer cancelStatus;

    /**
     * 流标原因
     */
    @TableField("CANCEL_REASON")
    private String cancelReason;

    /**
     * 废标状态 1：废标
     */
    @TableField("SCRAP_STATUS")
    private Integer scrapStatus;

    /**
     * 废标原因
     */
    @TableField("SCRAP_REASON")
    private String scrapReason;

    /**
     * 项目进行状态 1:暂停
     */
    @TableField("BID_PAUSE_STATUS")
    private Integer bidPauseStatus;

    /**
     * UKey硬件序列号
     */
    @TableField("UKEY_SERIAL_NUM")
    private String ukeySerialNum;

    /**
     * 评标报告生成状态 1：已生成
     */
    @TableField("EVAL_PDF_GENERATE_STATUS")
    private Integer evalPdfGenerateStatus;

    /**
     * 项目复议状态 1:复议
     */
    @TableField("REEVAL_FLAG")
    private Integer reevalFlag;

    /**
     * 评标审核状态 0：未申请 1：申请中 2：通过
     */
    @TableField("EVAL_REVIEW_STATUS")
    private String evalReviewStatus;

    /**
     * 委托人身份检查状态 1:检查结束
     */
    @TableField("CHECK_STATUS")
    private String checkStatus;

    /**
     * 是否纸质标 1:纸质标
     */
    @TableField("PAPER_EVAL")
    private String paperEval;

    /**
     * 不见面直播房间号
     */
    @TableField("LIVE_ROOM")
    private String liveRoom;

    /**
     * 设置签到开标时间前面多少分钟，默认1440分钟(一天)
     */
    @TableField("SIGN_IN_START_TIME_LEFT")
    private Integer signInStartTimeLeft;

    /**
     * 复会状态 参见数据字典status
     */
    @TableField("RESUME_STATUS")
    private Integer resumeStatus;

    /**
     * 复会时间
     */
    @TableField("RESUME_TIME")
    private String resumeTime;

    /**
     * 监管主键ID(即GOV_USER主键ID)
     */
    @TableField("MANAGER_ID")
    private Integer managerId;

    /**
     * 是否删除
     */
    @TableField("DELETE_FLAG")
    private Integer deleteFlag;

    /**
     * 业务code
     */
    @TableField("YW_CODE")
    private String ywCode;

    /**
     * 是否开启下载
     */
    @TableField("START_DOWN_LOAD")
    private Integer startDownLoad;

    /******************************自定义字段******************************/

    /**
     * 标段类型名称
     */
    @TableField(exist = false)
    private String bidClassifyName;

    /**
     * 开标时间
     */
    @TableField(exist = false)
    private String bidOpenTime;

    /**
     * 开标状态名称
     */
    @TableField(exist = false)
    private String bidOpenStatusName;

    /**
     * 评标状态名称
     */
    @TableField(exist = false)
    private String evalStatusName;

    /**
     * 招标控制价
     */
    @TableField(exist = false)
    private String controlPrice;

    /**
     * 开标地点
     */
    @TableField(exist = false)
    private String bidOpenPlace;

    /**
     * 当前投标人是否参标
     */
    @TableField(exist = false)
    private String isJoinBid;

    /**
     * 当前投标人Id
     */
    @TableField(exist = false)
    private Integer bidderId;

    /**
     * 代理单点用户Id
     */
    @TableField(exist = false)
    private Integer agencyId;

    /**
     * 招标代理机构
     */
    @TableField(exist = false)
    private String agencyName;

    /**
     * 标段关联信息
     */
    @TableField(exist = false)
    private BidSectionRelate bidSectionRelate;

    /**
     * 标段对应doc
     */
    @TableField(exist = false)
    private TenderDoc tenderDoc;

    /**
     * 几天内开标项目
     */
    @TableField(exist = false)
    private String scopeOpenNumDay;

    /**
     * 代理机构名称
     */
    @TableField(exist = false)
    private String tenderAgencyName;

    /**
     * 代理机构联系电话
     */
    @TableField(exist = false)
    private String tenderAgencyPhone;

    /**
     * 是否老系统项目（1：老系统项目）
     */
    @TableField(exist = false)
    private String isOldProject;

    /**
     * 标段对应专家列表
     */
    @TableField(exist = false)
    private List<ExpertUser> expertList;

    /**
     * 用于监管查询标段 是否绑定最高权限（1：绑定，  0：不绑定）
     */
    @TableField(exist = false)
    private Integer leader;
    /******************************自定义方法******************************/

    public String getBidOpenStatusName() {
        return this.bidOpenStatus == null ? "" :
                this.bidOpenStatus == 0 ? "未开始" :
                        this.bidOpenStatus == 1 ? "进行中" :
                                this.bidOpenStatus == 2 ? "结束" : "";
    }

    public String getEvalStatusName() {
        return this.evalStatus == null ? "" :
                this.evalStatus == 0 ? "未开始" :
                        this.evalStatus == 1 ? "进行中" :
                                this.evalStatus == 2 ? "结束" : "";
    }

    /**
     * 标段分类转换为中文
     * @return
     */
    public String getBidClassifyName(){
         return BidProtype.getProtypeChineseName(this.bidClassifyCode);
    }


}
