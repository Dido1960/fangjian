package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.BidSection;

import java.util.Map;

/**
 * 手写签名服务类接口
 *
 * @author zhangshaoyi
 * @since 2020-10-26
 */
public interface ISignatureService {

    /**
     * 获取签名信息
     * @param bidSectionId
     * @param index
     * @return
     */
    Map<String, Object> getPdfSignInfo(Integer bidSectionId,Integer expertUserId, Integer index);


    /**
     * 专家签名是否结束
     * @param bidSectionId
     * @return
     */
    Boolean expertSigarEnd(Integer bidSectionId);

    /**
     * 获取文件中定义的签名位置信息
     *
     * @param path
     * @param index
     * @param expertName
     * @return
     */
   String getPositionJson(String path, Integer index, String expertName, Integer expertId, BidSection bidSection);

    /**
     * 手写板专家签名
     * @param json
     * @param expertId
     * @param bidSectionId
     * @param expertSigarImage
     * @return
     * @throws Exception
     */
    Boolean saveJsonReport(String json, Integer expertId, Integer bidSectionId, String expertSigarImage) throws Exception;

    /**
     * 签名PDf信息
     * @param bidSectionId
     * @return
     * @throws Exception
     */
    Boolean signrPdf(Integer bidSectionId) throws Exception;

    /**
     * 删除签名信息
     * @param bidSectionId 标段信息
     */
    void updateExpertSigarStatus(Integer bidSectionId);

//    /**
//     * 修改专家签名状态
//     * @param expertUserIds 专家主键列表
//     */
//    void updateExpertSigarStatus(String[] expertUserIds);


}
