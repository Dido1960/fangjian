package com.ejiaoyi.agency.service;

/**
 * <p>
 * 开标结果推送 服务类
 * </p>
 *
 * @author Mike
 * @since 2020-01-22
 */
public interface IPushOpenResultService {
    /**
     * 推送开标结果给酒泉
     * @param bidSectionId 标段编号
     */
    void pushOpenResultForJQ(Integer bidSectionId) throws Exception;
}
