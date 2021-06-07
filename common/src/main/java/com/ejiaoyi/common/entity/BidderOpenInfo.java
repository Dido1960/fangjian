package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 投标人开标信息
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
@TableName("bidder_open_info")
public class BidderOpenInfo implements Serializable {

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
     * 投标人息主键ID
     */
    @TableField("BIDDER_ID")
    private Integer bidderId;

    /**
     * 文件上传时间
     */
    @TableField("UPFILE_TIME")
    private String upfileTime;

    /**
     * 法人或授权委托人姓名
     */
    @TableField("CLIENT_NAME")
    private String clientName;

    /**
     * 法人或授权委托人身份证号
     */
    @TableField("CLIENT_IDCARD")
    private String clientIdcard;

    /**
     * 电话号码
     */
    @TableField("CLIENT_PHONE")
    private String clientPhone;

    /**
     * 身份验证订单号
     */
    @TableField("TICKET_NO")
    private String ticketNo;

    /**
     * 二维码地址
     */
    @TableField("QR_PIC_URL")
    private String qrPicUrl;

    /**
     * 头像地址
     */
    @TableField("SQR_PIC_URL")
    private String sqrPicUrl;

    /**
     * 验证结果 1:验证通过，其他均为失败验证结果
     */
    @TableField("AUTHENTICATION")
    private Integer authentication;

    /**
     * 验证时间
     */
    @TableField("AUTH_TIME")
    private String authTime;

    /**
     * 验证ID
     */
    @TableField("REQUEST_ID")
    private String requestId;

    /**
     * 授权委托书上传id
     */
    @TableField("SQWTS_FILE_ID")
    private Integer sqwtsFileId;

    /**
     * 授权委托书名称
     */
    @TableField("SQWTS_FILE_NAME")
    private String sqwtsFileName;

    /**
     * 紧急签到
     */
    @TableField("URGENT_SIGIN")
    private Integer urgentSigin;

    /**
     * 法人或授权委托书人照片上传id
     */
    @TableField("SQWTS_PNG_FILE_ID")
    private Integer sqwtsPngFileId;

    /**
     * 签到时间
     */
    @TableField("SIGNIN_TIME")
    private String signinTime;

    /**
     * CA序列号(招标人CA)
     */
    @TableField("TENDER_CASN")
    private String tenderCasn;

    /**
     * CA序列号(投标人CA)
     */
    @TableField("BID_CASN")
    private String bidCasn;

    /**
     * 费率
     */
    @TableField("RATE")
    private String rate;

    /**
     * 工程质量(施工)
     */
    @TableField("QUALITY")
    private String quality;

    /**
     * 未签到 1:迟到 2:弃标 9:其它
     */
    @TableField("NOT_CHECKIN")
    private Integer notCheckin;

    /**
     * 未签到原因(notCheckin=9时存在)
     */
    @TableField("NOT_CHECKIN_REASON")
    private String notCheckinReason;

    /**
     * 投标文件解密状态(0:未解密;1:已解密;2:解密失败)
     */
    @TableField("DECRYPT_STATUS")
    private Integer decryptStatus;

    /**
     * 招标解密状态(0:未解密;1:已解密;2:解密失败)
     */
    @TableField("TENDER_DECRYPT_STATUS")
    private Integer tenderDecryptStatus;

    /**
     * 解密时间开始时间
     */
    @TableField("DECRYPT_START_TIME")
    private String decryptStartTime;

    /**
     * 解密时间结束时间
     */
    @TableField("DECRYPT_END_TIME")
    private String decryptEndTime;

    /**
     * 投标保证金缴纳状态(1:已缴纳;0:未缴纳)
     */
    @TableField("MARGIN_PAY_STATUS")
    private Integer marginPayStatus;

    /**
     * 投标人身份检查 1:符合 0:不符合
     */
    @TableField("BIDDER_IDENTITY_STATUS")
    private Integer bidderIdentityStatus;

    /**
     * 密封性检查 1:密封 0:破损
     */
    @TableField("SEAL_STATUS")
    private Integer sealStatus;

    /**
     * 标书拒绝 1:拒绝
     */
    @TableField("TENDER_REJECTION")
    private Integer tenderRejection;

    /**
     * 标书拒绝理由
     */
    @TableField("TENDER_REJECTION_REASON")
    private String tenderRejectionReason;

    /**
     * 投标报价确定 1:已确定
     */
    @TableField("PRICE_DETERMINE")
    private Integer priceDetermine;

    /**
     * 开标一览表确定 1:已确定
     */
    @TableField("DOC_DETERMINE")
    private Integer docDetermine;

    /**
     * 开标一览表确定时间 1:已确定
     */
    @TableField("DOC_DETERMINE_TIME")
    private String docDetermineTime;

    /**
     * 质询异议状态 1:无异议 2：有异议 0: 未做选择
     */
    @TableField("DISSENT_STATUS")
    private Integer dissentStatus;

    /**
     * 复会确定 1:无异议 2：有异议 0: 未做选择
     */
    @TableField("RESUME_DETERMINE")
    private Integer resumeDetermine;

    /**
     * 是否完成身份检查，1：是：0否
     */
    @TableField("IS_CLIENT_CHECK")
    private Integer isClientCheck;

    /**
     * 投标报价
     */
    @TableField("BID_PRICE")
    private String bidPrice;

    /**
     * 投标报价类型
     */
    @TableField("PRICE_TYPE")
    private String bidPriceType;

    /**
     * 投标工期
     */
    @TableField("TIME_LIMIT")
    private String timeLimit;

    /**
     * 是否被提出群聊  0: 默认  1:剔除
     */
    @TableField("KICK_STATUS")
    private Integer kickStatus;

    /******************************自定义字段******************************/

    /**
     * 投标人名称
     */
    @TableField(exist = false)
    private String bidderName;

    /**
     * 是否通过开标（即能够进入评标系统）
     */
    @TableField(exist = false)
    private Integer isPassBidOpen;

    /**
     * 紧急签到上传照片地址
     */
    @TableField(exist = false)
    private String photoUrl;

    /**
     * 保证金异常原因
     */
    @TableField(exist = false)
    private BidderException marginPayReason;

    /**
     * 身份认证异常原因
     */
    @TableField(exist = false)
    private BidderException identityReason;

    /**
     * 文件信息
     */
    @TableField(exist = false)
    private BidderFileInfo bidderFileInfo;


    /**
     * 授权委托书地址
     */
    @TableField(exist = false)
    private String sqwtsMark;

    /**
     * 委托书 fdfsId
     */
    @TableField(exist = false)
    private Integer sqwtsFdfsId;

    /**
     * 保证金缴纳状态
     *
     * @return 启用状态名称
     */
    public String getMarginPay() {
        return this.marginPayStatus == null ? "未缴纳" :
                this.marginPayStatus == 0 ? "未缴纳" :
                        this.marginPayStatus == 1 ? "已缴纳" :
                                this.marginPayStatus == 2 ? "保函缴纳" :
                                        "未缴纳";
    }


}
