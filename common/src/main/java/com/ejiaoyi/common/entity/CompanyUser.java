package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 企业用户
 * </p>
 *
 * @author lesgod
 * @since 2020-05-09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("company_user")
public class CompanyUser implements Serializable {

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
     * 姓名
     */
    @TableField("NAME")
    private String name;

    /**
     * 登录名
     */
    @TableField("LOGIN_NAME")
    private String loginName;

    /**
     * 登录密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 统一社会信用代码
     */
    @TableField("CODE")
    private String code;

    /**
     * 单位地址
     */
    @TableField("ADDRESS")
    private String address;

    /**
     * 联系人
     */
    @TableField("LINK_MAN")
    private String linkMan;

    /**
     * 联系电话
     */
    @TableField("PHONE")
    private String phone;

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
     * 启用状态
     */
    @TableField("ENABLED")
    private Integer enabled;


    /**
     * 企业所在地
     */
    @TableField("LOCAL")
    private String local;


    /**
     * 企业所在地
     */
    @TableField("LOCAL_DETAIL")
    private String localDetail;
    /**
     * 头像id
     */
    @TableField("PHOTO_ID")
    private Integer photoId;

    /**
     * 上传文件id
     */
    @TableField("FILE_IDS")
    private String fileId;

    /**
     * 上传文件id
     * eg: 多个主键用逗号分隔 1,2,3
     */
    @TableField("LICENSE")
    private String license;


    /**
     * 用户头像的路径
     */
    @TableField(exist = false)
    private String url;


    /**
     * 用户文件的路径
     */
    @TableField(exist = false)
    private List<String> fileUrls;

    // =============用户自定义属性===========================

    /**
     * CA绑定id(对应UserCert的id)
     */
    @TableField(exist = false)
    private Integer bindCertId;

    /**
     * 用户类型名称
     */
    @TableField(exist = false)
    private String userTypeName;


    /**
     * 营业执照
     */
    @TableField(exist = false)
    private List<String> listLicense;


    /**
     * 注册次数
     */
    @TableField(exist = false)
    private Integer certCount;

    // =======================自定义get=====================================

    /**
     * 用户类型
     *
     * @return
     */
    public String getUserTypeName() {
        return "企业用户";
    }

}
