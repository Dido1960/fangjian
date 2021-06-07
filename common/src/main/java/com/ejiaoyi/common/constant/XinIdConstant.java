package com.ejiaoyi.common.constant;
/**
 * 江苏一证通身份认证工具类
 */
public class XinIdConstant {

    /**
     * appKey
     */
    public static final String APPKEY = "cv7obMqC4QiiCaR3";

    /**
     * appSecret
     */
    public static final String SECRETKEY = "QqG5GaJeEr561xrlXrjLnDP9oBLvHrYI";

    /**
     * 需要回跳的目标地址，一般指定为商户业务页面
     */
    public static final String RETURN_URL = "http://www.ejiaoyi.xin";

    /**
     * 成功标志
     */
    public static final String STATE_SUCCESS = "success";


    //认证回话状态
    /**
     * 未认证
     */
    public static final String CREATE = "CREATE";
    /**
     * 认证中
     */
    public static final String ACTION = "ACTION";
    /**
     * 完成认证
     */
    public static final String OVER = "OVER";
    /**
     * 已失效
     */
    public static final String INVALID = "INVALID";

    public interface RequestUrl {
        /****
         * 获取H5地址
         * **/
        String H5_GET_URL = "http://iv.unitid.cn/api/router/rest";
        /***
         * 获取H5验证结果信息
         * **/
        String H5_GET_STATE = "http://iv.unitid.cn/api/router/rest";

        String H5_GET_SESSONSTATUS = "http://api.spiderid.cn/api/router/rest";
    }
}
