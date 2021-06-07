package com.ejiaoyi.admin.service;

/**
 * <p>
 * 评标结果推送 服务类
 * </p>
 *
 * @author Make
 * @since 2020-12-31
 */
public interface IPushEvalResultService {

    /**
     * 推送开标结果给酒泉
     * @param bidSectionId 标段编号
     */
    void pushOpenResultForJQ(Integer bidSectionId) throws Exception;
    /**
     * 推送评标结果给酒泉
     * @param bidSectionId 标段编号
     */
    void pushEvalResultForJQ(Integer bidSectionId) throws Exception;
}
