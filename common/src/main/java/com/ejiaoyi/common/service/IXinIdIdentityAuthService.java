package com.ejiaoyi.common.service;

/**
 * 江苏一证通身份认证服务类
 *
 * @author Make
 * @since 2020-08-04
 */
public interface IXinIdIdentityAuthService {

    /**
     * 业务准备，身份认证初始化
     *
     * @param ticketId 请求的唯一标识
     * @param idCard   验证人的真实姓名
     * @param name     验证人的证件号码
     * @return
     */
    String getH5URL(String ticketId, String idCard, String name, String callBackUrl);

    /**
     * @param ticketId 认证唯一标识
     * @return 返回会话状态
     * @Description 获取认证会话状态
     * @Author liuguoqiang
     * @Date 2020-8-10 17:45
     */
    String getH5SessionStatus(String ticketId);

    /**
     * 验证地址，验证成功后返回验证的照片地址,否则返回null
     *
     * @param ticketId 认证唯一标识
     * @return 认证url
     */
    String getH5ResState(String ticketId);
}
