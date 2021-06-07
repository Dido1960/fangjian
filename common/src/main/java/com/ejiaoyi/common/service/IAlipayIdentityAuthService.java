package com.ejiaoyi.common.service;

/**
 * 支付宝身份认证服务类
 *
 * @author Make
 * @since 2020-08-04
 */
public interface IAlipayIdentityAuthService {

    /**
     * 业务准备，身份认证初始化
     *
     * @param outerOrderNo 请求的唯一标识
     * @param idCard 验证人的真实姓名
     * @param name 验证人的证件号码
     * @param callBackUrl 回调地址
     * @return
     */
    String authInitialize(String outerOrderNo, String idCard, String name, String callBackUrl);

    /**
     * 通过认证唯一标识开始身份认证
     *
     * @param certifyId 认证唯一标识 (由认证初始化方法产生)
     * @return 认证url
     */
    String startCertify(String certifyId);

    /**
     * 通过认证唯一标识查询身份认证结果
     *
     * @param certifyId 认证唯一标识 (由认证初始化方法产生)
     * @return 认证是否成功
     */
    boolean queryCertify(String certifyId);

}
