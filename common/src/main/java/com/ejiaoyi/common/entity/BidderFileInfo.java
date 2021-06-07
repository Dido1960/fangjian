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
 * 投标文件信息
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-09-30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bidder_file_info")
public class BidderFileInfo implements Serializable {

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
     * 投标人上传的GEF源文件ID,对应fdfs表主键
     */
    @TableField("GEF_ID")
    private Integer gefId;

    /**
     * GEF投标文件Hash值
     */
    @TableField("GEF_HASH")
    private String gefHash;

    /**
     * 投标人上传的SGEF源文件ID,对应fdfs表主键
     */
    @TableField("SGEF_ID")
    private Integer sgefId;

    /**
     * SGEF加密投标文件Hash值
     */
    @TableField("SGEF_HASH")
    private String sgefHash;

    /**
     * 存根文件上传ID
     */
    @TableField("CZR_ID")
    private Integer czrId;

    /**
     * 存根文件Hash值
     */
    @TableField("CZR_HASH")
    private String czrHash;

    /**
     * 投标文件核验成功后上传的回执单ID,对应fdfs表主键
     */
    @TableField("RECEIPT_ID")
    private Integer receiptId;

    /**
     * 加密锁的ID
     */
    @TableField("CERT_ID")
    private String certId;

    /**
     * 加密因子
     */
    @TableField("CIPHER")
    private String cipher;

    /**
     * 锁类型
     */
    @TableField("CA_TYPE")
    private String caType;

    /**
     * 投标文件硬件码
     */
    @TableField("PCID")
    private String pcid;

    /**
     * 投标文件硬盘码
     */
    @TableField("DISKID")
    private String diskid;

    /**
     * 投标文件网卡MAC
     */
    @TableField("MACID")
    private String macid;

    /**
     * 投标工程量清单文件标识码
     */
    @TableField("XML_UID")
    private String xmlUid;

    /**
     * 投标工程量清单文件解析服务号
     */
    @TableField("XML_SERVICE_NUMBER")
    private String xmlServiceNumber;

    /**
     * 商务文件 上传状态 （0：未上传 1：成功 2：失败）/纸质标代表 纸质标投标文件PDF
     */
    @TableField("BUSINESS_STATUS")
    private Integer businessStatus;

    /**
     * 技术文件 上传状态 （0：未上传 1：成功 2：失败）
     */
    @TableField("TECHNICAL_STATUS")
    private Integer technicalStatus;

    /**
     * 资格证明 上传状态 （0：未上传 1：成功 2：失败）
     */
    @TableField("QUALIFICATIONS_STATUS")
    private Integer qualificationsStatus;

    /**
     * 工程量清单PDF（施工类特有） 上传状态 （0：未上传 1：成功 2：失败）
     */
    @TableField("CHECKLIST_STATUS")
    private Integer checklistStatus;

    /**
     * 其他文件 上传状态 （0：未上传 1：成功 2：失败）
     */
    @TableField("OTHER_STATUS")
    private Integer otherStatus;

    /**
     * 工程量清单XML（施工类特有） 上传状态 （0：未上传 1：成功 2：失败）
     */
    @TableField("CHECKLIST_XML_STATUS")
    private Integer checklistXmlStatus;

    /**
     * 所有投标文件 上传状态 （0：未上传 1：成功 2：失败）
     */
    @TableField("ALL_FILE_STATUS")
    private Integer allFileStatus;

    /**
     * 投标文件解密路径
     */
    @TableField("FILE_UNZIP_PATH")
    private String fileUnzipPath;

    @TableField(exist = false)
    private String bidderName;
}
