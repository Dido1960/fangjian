package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.ElectBidderResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 企业推选结果 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IElectBidderResultService extends IService<ElectBidderResult> {

    /**
     * 获取当前标段所有推选结果
     * @param bidSectionId 标段主键
     * @return
     */
    List<ElectBidderResult> listElectBidderResult(Integer bidSectionId);

}
