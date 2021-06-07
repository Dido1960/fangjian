package com.ejiaoyi.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 政府用户
 * </p>
 *
 * @author fengjunhong
 * @since 2020-05-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("gov_user")
public class GovUser implements Serializable {

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
     * 用户所在部门
     */
    @TableField("DEP_ID")
    private Integer depId;

    /**
     * 用户电话
     */
    @TableField("PHONE")
    private String phone;

    /**
     * 是否绑定最高权限（1：绑定，  0：不绑定）
     */
    @TableField("LEADER")
    private Integer leader;

    /**
     * 启用状态
     */
    @TableField("ENABLED")
    @ApiModelProperty(value = "启用状态")
    private Integer enabled;

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
     * 部门名称
     */
    @TableField(exist = false)
    private String depName;

    /**
     * 启用状态名称
     */
    @TableField(exist = false)
    private String enabledName;

    /**
     * 用户所在部门
     */
    @TableField(exist = false)
    private Dep dep;

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
        return "政府用户";
    }


}
