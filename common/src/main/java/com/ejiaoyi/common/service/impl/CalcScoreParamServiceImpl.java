package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.CalcScoreParam;
import com.ejiaoyi.common.mapper.CalcScoreParamMapper;
import com.ejiaoyi.common.service.ICalcScoreParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 计算报价得分参数 服务实现类
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-12-01
 */
@Service
public class CalcScoreParamServiceImpl extends BaseServiceImpl implements ICalcScoreParamService {

    @Autowired
    private CalcScoreParamMapper calcScoreParamMapper;

    @Override
    public CalcScoreParam getCalcScoreParamBySectionId(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null !");
        QueryWrapper<CalcScoreParam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return calcScoreParamMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer updateCalcScoreParam(CalcScoreParam calcScoreParam) {
        Assert.notNull(calcScoreParam, "param calcScoreParam can not be null !");
        Assert.notNull(calcScoreParam.getId(), "param id can not be null !");
        return calcScoreParamMapper.updateById(calcScoreParam);
    }

    @Override
    public Integer insertCalcScoreParam(CalcScoreParam calcScoreParam) {
        Assert.notNull(calcScoreParam, "param calcScoreParam can not be null !");
        calcScoreParamMapper.insert(calcScoreParam);
        return calcScoreParam.getId();
    }
}
