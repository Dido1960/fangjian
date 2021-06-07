package com.ejiaoyi.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.dto.BidderQuantityScoreDTO;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderQuantityScore;
import com.ejiaoyi.common.enums.ExecuteCode;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.enums.StatusEnum;
import com.ejiaoyi.common.mapper.BidderQuantityScoreMapper;
import com.ejiaoyi.common.service.IBidderQuantityScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.service.IBidderService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedisUtil;
import com.jacob.com.STA;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 投标人工程量清单报价得分结果 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-12-19
 */
@Service
public class BidderQuantityScoreServiceImpl extends ServiceImpl<BidderQuantityScoreMapper, BidderQuantityScore> implements IBidderQuantityScoreService {
    @Autowired
    private BidderFileInfoServiceImpl bidderFileInfoService;
    @Autowired
    private BidderQuantityScoreMapper bidderQuantityScoreMapper;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IBidderQuantityScoreService bidderQuantityScoreService;
    @Autowired
    private BidSectionServiceImpl bidSectionService;

    @Override
    @RedissonLock(key = "'SAVE_SCORE_'+#bidSectionId")
    public void saveBidderQuantityScoreList(Integer bidSectionId, List<BidderQuantityScoreDTO> bidderQuantityScoreDTOS) {
        for (BidderQuantityScoreDTO bidderQuantityScoreDTO : bidderQuantityScoreDTOS) {
            BidderQuantityScore bidderQuantityScore = BidderQuantityScore.builder().build();
            BeanUtil.copyProperties(bidderQuantityScoreDTO, bidderQuantityScore);
//            bidderQuantityScore.setBidderName(bidderQuantityScoreDTO.getName());
            bidderQuantityScore.setTotalScore(bidderQuantityScoreDTO.getScore());
            String bidXmlUid = bidderQuantityScoreDTO.getBidXmlUid();

            if (StringUtils.isNotEmpty(bidXmlUid)) {
                Bidder bidder = bidderFileInfoService.getBidderByXmlUid(bidXmlUid);
                if (bidder != null) {
                    BidderQuantityScore quantityScore = getBidderQuantityScoreByBidderId(bidder.getId());
                    if (quantityScore != null) {
                        bidderQuantityScore.setId(quantityScore.getId());
                        updateById(bidderQuantityScore);
                    } else {
                        bidderQuantityScore.setBidderId(bidder.getId());
                        bidderQuantityScore.setBidSectionId(bidder.getBidSectionId());
                        save(bidderQuantityScore);
                    }
                }
            }
        }
    }

    @Override
    @RedissonLock(key = "'SAVE_SCORE_'+#bidSectionId")
    public void saveBidderQuantityScore(Integer bidSectionId, List<BidderQuantityScore> bidderQuantityScores) {
        for (BidderQuantityScore bidderQuantityScore : bidderQuantityScores) {
            // 获取投标人
            Bidder bidder = bidderService.getClearV3Bidder(bidSectionId, bidderQuantityScore.getBidderId());
            // 投标人id
            bidderQuantityScore.setBidderId(bidder.getId());
            // 投标人名称
            bidderQuantityScore.setBidderName(bidder.getBidderName());

            BidderQuantityScore quantityScore = bidderQuantityScoreService.getBidderQuantityScoreByBidderId(bidder.getId());
                 // 新增
            if (CommonUtil.isEmpty(quantityScore)){
                bidderQuantityScoreMapper.insert(bidderQuantityScore);
            } else {
                // 修改价格分
                QueryWrapper<BidderQuantityScore> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("BID_SECTION_ID", bidSectionId);
                queryWrapper.eq("BIDDER_ID",bidder.getId());
                bidderQuantityScoreMapper.update(bidderQuantityScore,queryWrapper);
            }
        }
        // 更新价格分状态信息
        bidSectionService.updateBidSectionById(BidSection.builder()
                .id(bidSectionId)
                .priceRecordStatus(ExecuteCode.SUCCESS.getCode())
                .build());
    }

    @Override
    public BidderQuantityScore getBidderQuantityScoreByBidderId(Integer bidderId){
        Assert.notNull(bidderId, "param bidderId can not be null!");
        QueryWrapper<BidderQuantityScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BIDDER_ID", bidderId);
        List<BidderQuantityScore> bidderQuantityScores = bidderQuantityScoreMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(bidderQuantityScores)){
            return bidderQuantityScores.get(0);
        }
        return null;

    }

    @Override
    public List<BidderQuantityScore> listBidderQuantityScoreByBidSectionId(Integer bidSectionId){
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<BidderQuantityScore> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return bidderQuantityScoreMapper.selectList(queryWrapper);
    }
}
