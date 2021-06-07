package com.ejiaoyi.api.dto;

import com.ejiaoyi.common.enums.DockApiCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Api 返回类
 *
 * @author Z0001
 * @since 2020-5-9
 */
@Data
public class RestResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "状态码", position = 1, example = "200")
    private Integer code;

    @ApiModelProperty(value = "状态码描述信息", position = 2, example = "成功")
    private String msg;

    @ApiModelProperty(value = "返回数据对象", position = 3, example = "返回对的数据，为JSON对象")
    private T data;

    public RestResultVO() {
    }


    public RestResultVO(DockApiCode apiCode) {
        this.msg = apiCode.getMsg();
        this.code = apiCode.getCode();
    }

    public RestResultVO(DockApiCode apiCode, T data) {
        this.msg = apiCode.getMsg();
        this.code = apiCode.getCode();
        this.data = data;
    }

    public RestResultVO(DockApiCode apiCode, String remark, T data) {
        if (remark != null) {
            this.msg = apiCode.getMsg()+"【"+remark+"】";
        }else{
            this.msg = apiCode.getMsg();
        }

        this.code = apiCode.getCode();
        this.data = data;
    }

    /**
     * 补充信息
     **/
    public void setCode(DockApiCode code, String remark) {
        if (code != null) {
            this.code = code.getCode();
            if (remark != null) {
                this.msg = remark;
            }

        }
    }

    /**
     * 补充信息
     **/
    public void setMsg(String remark) {
        this.msg = remark;
    }


    /**
     * 补充信息
     **/
    public RestResultVO<T> success(T data) {
        return new RestResultVO<T>(DockApiCode.SUCCESS, data);
    }
}
