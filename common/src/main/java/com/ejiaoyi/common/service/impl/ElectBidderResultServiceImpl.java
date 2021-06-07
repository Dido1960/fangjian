package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.ElectBidderResult;
import com.ejiaoyi.common.mapper.ElectBidderResultMapper;
import com.ejiaoyi.common.service.IElectBidderResultService;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 企业推选结果 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
public class ElectBidderResultServiceImpl extends ServiceImpl<ElectBidderResultMapper, ElectBidderResult> implements IElectBidderResultService {

    @Autowired
    ElectBidderResultMapper electBidderResultMapper;

    @Override
    public List<ElectBidderResult> listElectBidderResult(Integer bidSectionId) {
        if (CommonUtil.isEmpty(bidSectionId)){
            QueryWrapper<ElectBidderResult> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("BID_SECTION_ID",bidSectionId);
            return electBidderResultMapper.selectList(queryWrapper);
        }
        return null;
    }
}
