package com.ejiaoyi.common.service;


import com.ejiaoyi.common.entity.CalcScoreParam;

/**
 * <p>
 * 计算报价得分参数 服务类
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-12-01
 */
public interface ICalcScoreParamService {

    /**
     * 根据标段id获取评标基准价参数
     * @param bidSectionId 标段id
     * @return 评标基准价
     */
    CalcScoreParam getCalcScoreParamBySectionId(Integer bidSectionId);

    /**
     * 更新评标基准价参数
     * @param calcScoreParam 评标基准价参数
     * @return 评标基准价
     */
    Integer updateCalcScoreParam(CalcScoreParam calcScoreParam);

    /**
     * 插入基准价参数
     * @param calcScoreParam 数据
     * @return 插入后的ID
     */
    Integer insertCalcScoreParam(CalcScoreParam calcScoreParam);
}
