package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.entity.BidderReviewResult;
import com.ejiaoyi.common.entity.BidderReviewResultDeduct;
import com.ejiaoyi.common.entity.BidderReviewResultScore;
import com.ejiaoyi.common.entity.ExpertReviewSingleItem;
import com.ejiaoyi.common.mapper.BidderReviewResultDeductMapper;
import com.ejiaoyi.common.service.IBidderReviewResultDeductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 企业评审grade单项的评审结果表 扣分制 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
public class BidderReviewResultDeductServiceImpl extends ServiceImpl<BidderReviewResultDeductMapper, BidderReviewResultDeduct> implements IBidderReviewResultDeductService {

    @Autowired
    private BidderReviewResultDeductMapper bidderReviewResultDeductMapper;

    @Override
    @RedissonLock(key = "'bidderReviewResultDeduct' + #newResult.bidderId+'_'+#newResult.bidSectionId+'_'+#newResult.gradeId")
    public Integer saveOrUpdateResultDeduct(BidderReviewResultDeduct newResult) {
        if (CommonUtil.isEmpty(newResult.getId())) {
            BidderReviewResultDeduct result = getBidderReviewResultDeduct(newResult);
            if (null == result) {
                bidderReviewResultDeductMapper.insert(newResult);
            } else {
                newResult.setId(result.getId());
                bidderReviewResultDeductMapper.updateById(newResult);
            }
        } else {
            bidderReviewResultDeductMapper.updateById(newResult);
        }
        return newResult.getId();
    }

    @Override
    public BidderReviewResultDeduct getBidderReviewResultDeduct(BidderReviewResultDeduct bidderReviewResultDeduct) {
        QueryWrapper<BidderReviewResultDeduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(null != bidderReviewResultDeduct.getId(), "ID", bidderReviewResultDeduct.getId());
        queryWrapper.eq(null != bidderReviewResultDeduct.getBidderId(), "BIDDER_ID", bidderReviewResultDeduct.getBidderId());
        queryWrapper.eq(null != bidderReviewResultDeduct.getGradeId(), "GRADE_ID", bidderReviewResultDeduct.getGradeId());
        queryWrapper.eq(null != bidderReviewResultDeduct.getBidSectionId(), "BID_SECTION_ID", bidderReviewResultDeduct.getBidSectionId());

        return bidderReviewResultDeductMapper.selectOne(queryWrapper);
    }

    @Override
    public String getTotalScore(String[] gradeIds, Integer reviewType, Integer bidderId) {
        return bidderReviewResultDeductMapper.getTotalScore(gradeIds, reviewType, bidderId);
    }

    @Override
    public Integer deleteByGradeIds(Integer[] gradeIds) {
        QueryWrapper<BidderReviewResultDeduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("GRADE_ID",gradeIds);
        return bidderReviewResultDeductMapper.delete(queryWrapper);
    }
}
