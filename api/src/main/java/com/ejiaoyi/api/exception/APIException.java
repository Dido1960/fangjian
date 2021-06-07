package com.ejiaoyi.api.exception;

import com.ejiaoyi.common.enums.DockApiCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class APIException extends RuntimeException {
    private int code;
    private String msg;
    private Object data;

    public APIException(DockApiCode apiCode) {
        this.code = apiCode.getCode();
        this.msg = apiCode.getMsg();
        this.data = null;
    }


    /***
     * 补充其他信息
     * **/
    public APIException(DockApiCode apiCode, String msg, Object data) {
        super(apiCode.getMsg());
        this.code = apiCode.getCode();
        if(StringUtils.isNotEmpty(msg)){
            this.msg = apiCode.getMsg()+"【"+msg+"】";
        }else{
            this.msg = apiCode.getMsg();
        }
        this.data = data;
    }
}