package com.ejiaoyi.common.dto;

/**
 * json网络传输格式
 *
 * @author fengjunhong
 */
public class JsonData {

    /**
     * 网络的传输代码
     */
    private String code;
    /**
     * 返回信息
     */
    private String msg;
    /**
     * 数据
     */
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
