package com.ejiaoyi.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_info")
@ApiModel(value = "用户信息", description = "用户信息")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    @JsonProperty("id")
    @JSONField(name = "id")
    @ApiModelProperty(value = "用户主键", required = true, example = "admin")
    private Integer id;

    /**
     * 数据时间戳
     */
    @TableField("INSERT_TIME")
    @JsonProperty("login_name")
    @JSONField(name = "login_name")
    @ApiModelProperty(value = "用户主键", required = true, example = "admin")
    private String insertTime;

    /**
     * 姓名
     */
    @TableField("NAME")
    @ApiModelProperty(value = "用户姓名")
    private String name;

    /**
     * 登录名
     */
    @TableField("LOGIN_NAME")
    @ApiModelProperty(value = "登录名")
    private String loginName;

    /**
     * 登录密码
     */
    @TableField("PASSWORD")
    @ApiModelProperty(value = "登录密码")
    private String password;

    /**
     * 用户所在地区
     */
    @TableField("REG_ID")
    @ApiModelProperty(value = "用户所在地区")
    private Integer regId;

    /**
     * 拼音 全拼
     */
    @TableField("ALL_SPELLING")
    @ApiModelProperty(value = "拼音 全拼")
    private String allSpelling;

    /**
     * 拼音 首字母
     */
    @TableField("FIRST_SPELLING")
    @ApiModelProperty(value = "拼音 首字母")
    private String firstSpelling;

    /**
     * 用户权限 -1：后台用户（不允许修改）
     */
    @TableField("USER_ROLE_STATUS")
    @ApiModelProperty(value = "用户权限 -1：后台用户（不允许修改）")
    private Integer userRoleStatus;

    /**
     * 启用状态
     */
    @TableField("ENABLED")
    @ApiModelProperty(value = "启用状态")
    private Integer enabled;

    /**
     * 手机号码
     */
    @TableField("PHONE")
    @ApiModelProperty(value = "手机号码")
    private String phone;

    /**
     * 用户头像
     */
    @TableField("USER_FILE_ID")
    @ApiModelProperty(value = "头像")
    private Integer userFileId;

    /************************ 自定义属性 ************************/

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
     * 所属角色名称
     */
    @TableField(exist = false)
    private String roleName;

    /**
     * 所属角色id
     */
    @TableField(exist = false)
    private String roleId;

    /**
     * 区划名称
     */
    @TableField(exist = false)
    private String regName;

    /**
     * 启用状态名称
     */
    @TableField(exist = false)
    private String enabledName;

    /**
     * 用户头像的路径
     */
    @TableField(exist = false)
    private String url;

    /**
     * 区划代码
     */
    @TableField(exist = false)
    private String regNo;

    /*********************************自定义get set***********************/

    /**
     * 启用状态名称
     *
     * @return 启用状态名称
     */
    public String getEnabledName() {
        return this.enabled == null ? "" : this.enabled == 1 ? "启用" : this.enabled == 0 ? "禁用" : "";
    }

    /**
     * 用户类型
     * @return
     */
    public String getUserTypeName(){
        return "系统用户";
    }

}
