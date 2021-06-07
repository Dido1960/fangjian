package com.ejiaoyi.common.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.BidderException;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.mapper.BidderExceptionMapper;
import com.ejiaoyi.common.service.IBidderExceptionService;
import com.ejiaoyi.common.service.IBidderOpenInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/23 17:56
 */
@Service
public class BidderExceptionServiceImpl extends BaseServiceImpl implements IBidderExceptionService {
    @Autowired
    private BidderExceptionMapper bidderExceptionMapper;
    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Override
    public Integer addBidderException(BidderException bidderException) {
        return bidderExceptionMapper.insert(bidderException);
    }

    @Override
    public Integer updateBidderException(BidderException bidderException) {
        return bidderExceptionMapper.updateById(bidderException);
    }

    @Override
    public BidderException getExceptionReason(BidderOpenInfo bidderOpenInfo, int exceType) {
        QueryWrapper<BidderException> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID",bidderOpenInfo.getBidSectionId());
        wrapper.eq("BIDDER_ID",bidderOpenInfo.getBidderId());
        wrapper.eq("EXCEPTION_TYPE",exceType);
        wrapper.eq("ENABLED",1);
        List<BidderException> list = bidderExceptionMapper.selectList(wrapper);
        if (list.size()>0){
            return list.get(list.size()-1);
        }
        return null;
    }

    @Override
    public BidderException getExceptionReasonByReason(BidderException bidderException) {
        QueryWrapper<BidderException> wrapper = new QueryWrapper<>();
        if (bidderException.getId()!=null){
            return bidderExceptionMapper.selectById(bidderException.getId());
        }else {
            if (bidderException.getBidSectionId()!=null){
                wrapper.eq("BID_SECTION_ID",bidderException.getBidSectionId());
            }
            if (bidderException.getBidderId()!=null){
                wrapper.eq("BIDDER_ID",bidderException.getBidderId());
            }
            if (bidderException.getExceptionType()!=null){
                wrapper.eq("EXCEPTION_TYPE",bidderException.getExceptionType());
            }
            wrapper.eq("ENABLED",1);
            List<BidderException> list = bidderExceptionMapper.selectList(wrapper);
            if (list.size()>0){
                return list.get(list.size()-1);
            }else {
                return bidderException;
            }
        }
    }

    @Override
    public Integer saveOrUpdateReason(Integer userId, String userName, BidderException bidderException) {
        //获取投标人id
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfoById(bidderException.getBidderOpenInfoId());
        bidderException.setBidderId(bidderOpenInfo.getBidderId());
        bidderException.setEnabled(1);
        String exceptionReason = bidderException.getExceptionReason();
        //条件查询异常信息，防止多次插入
        bidderException = this.getExceptionReasonByReason(bidderException);
        //插入reason
        bidderException.setExceptionReason(exceptionReason);

        //获取登录人信息
        bidderException.setOperatorId(userId);
        bidderException.setOperatorName(userName);
        bidderException.setOperatTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        if (bidderException.getId() == null) {
            return this.addBidderException(bidderException);
        } else {
            return this.updateBidderException(bidderException);
        }
    }
}
