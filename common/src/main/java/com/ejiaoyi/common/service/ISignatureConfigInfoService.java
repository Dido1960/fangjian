package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.SignatureConfigInfo;

/**
 * <p>
 * 签章配置信息（回执单签章） 服务类
 * </p>
 *
 * @author Mike
 * @since 2021-02-18
 */
public interface ISignatureConfigInfoService {

    /**
     * 根据区划信息获取签章印模信息
     * @param regNo 区划代码
     * @return
     */
    SignatureConfigInfo getSignatureConfigInfoByRegNo(String regNo);

    /**
     * 分页查询
     * @param signatureConfigInfo
     * @return
     */
    String pagedSignatureConfigInfo(SignatureConfigInfo signatureConfigInfo);

    /**
     * 更新
     * @param signatureConfigInfo
     * @return
     */
    Integer updateSignatureConfigInfo(SignatureConfigInfo signatureConfigInfo);

    /**
     * 添加
     * @param signatureConfigInfo
     * @return
     */
    Integer addSignatureConfigInfo(SignatureConfigInfo signatureConfigInfo);

    /**
     * 删除
     * @param ids
     * @return
     */
    Integer deleteSignatureConfigInfo(Integer[] ids);

    /**
     * 通过id获取
     * @param id
     * @return
     */
    SignatureConfigInfo getSignatureConfigInfoById(Integer id);
}
