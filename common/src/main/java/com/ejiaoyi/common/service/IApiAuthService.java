package com.ejiaoyi.common.service;

/**
 * <p>
 * API接口认证信息 服务类
 * </p>
 *
 * @author Z0001
 * @since 2020-04-01
 */
public interface IApiAuthService extends IBaseService {

    /**
     * 分页获取API授权信息
     *
     * @param apiName  接口名称
     * @param platform 平台授权码
     * @param apiKey   API授权码
     * @param remark   授权说明
     * @param enabled  启用状态
     * @return API授权信息列表JSON
     */
    String pagedApiAuth(String apiName, String platform, String apiKey, String remark, Integer enabled);

    /**
     * 生成接口授权信息
     *
     * @param apiName 接口名称
     * @param remark  启用状态
     * @return 是否生成成功
     */
    boolean addApiAuth(String apiName, String remark);

    /**
     * 修改接口授权信息启用状态
     *
     * @param id      主键
     * @param enabled 启用状态
     * @return 是否修改成功
     */
    boolean updateApiAuthEnabled(Integer id, Integer enabled);

    /**
     * API接口认证
     *
     * @param apiName  API NAME
     * @param platform 平台授权码
     * @param apiKey   API KEY
     * @return 是否认证成功
     */
    boolean authentication(String apiName, String platform, String apiKey);
}
