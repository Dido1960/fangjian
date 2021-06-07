package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.mapper.BidderOpenInfoMapper;
import com.ejiaoyi.common.service.IBidderOpenInfoService;
import com.ejiaoyi.common.service.IBidderService;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengjunhong
 * @since 2020-7-17
 */
@Service
public class BidderOpenInfoServiceImpl extends BaseServiceImpl implements IBidderOpenInfoService {

    @Autowired
    BidderOpenInfoMapper bidderOpenInfoMapper;

    @Autowired
    IBidderService bidderService;

    @Autowired
    private IFDFSService fdfsService;

    @Override
    public List<BidderOpenInfo> listBidderOpenInfo(Integer bid, boolean isQualification) {
        return bidderOpenInfoMapper.listBidderOpenInfo(bid, isQualification);
    }

    @Override
    @CacheEvict(value = CacheName.BIDDER_OPEN_INFO, key = "#bidderOpenInfo.id")
    public boolean updateBidderOpenInfo(BidderOpenInfo bidderOpenInfo) {
        QueryWrapper<BidderOpenInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidderOpenInfo.getBidSectionId());
        queryWrapper.eq("BIDDER_ID", bidderOpenInfo.getBidderId());
        if (bidderOpenInfo.getTenderRejection() != null) {
            // 标书被拒绝，修改IS_PASS_BID_OPEN（default:1） 为0
            if (bidderOpenInfo.getTenderRejection() == 1) {
                // 修改 是否通过开标
                bidderService.updateBiddersIsPassBidOpen( bidderOpenInfo.getBidderId(), 0);
            }
            if (bidderOpenInfo.getTenderRejection() == 0){
                // 修改 通过开标
                bidderService.updateBiddersIsPassBidOpen( bidderOpenInfo.getBidderId(), 1);
            }
        }

        return bidderOpenInfoMapper.update(bidderOpenInfo, queryWrapper) == 1;
    }

    @Override
    public BidderOpenInfo getBidderOpenInfo(BidderOpenInfo bidderOpenInfo) {
        QueryWrapper<BidderOpenInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidderOpenInfo.getBidSectionId());
        queryWrapper.eq("BIDDER_ID", bidderOpenInfo.getBidderId());
        return bidderOpenInfoMapper.selectOne(queryWrapper);
    }

    @Override
    @CacheEvict(value = CacheName.BIDDER_OPEN_INFO, key = "#bidderOpenInfo.id")
    public Integer updateBidderOpenInfoById(BidderOpenInfo bidderOpenInfo) {
        BidderOpenInfo boi = bidderOpenInfoMapper.selectById(bidderOpenInfo.getId());
        if ("".equals(bidderOpenInfo.getTenderRejectionReason())) {
            bidderOpenInfo.setTenderRejectionReason(null);
        }
        // 标书被拒绝，修改IS_PASS_BID_OPEN（defaul:1） 为0
        if (bidderOpenInfo.getTenderRejection() != null && bidderOpenInfo.getTenderRejection() == 1) {
            bidderService.updateBiddersIsPassBidOpen(bidderOpenInfo.getBidderId(), 0);
        }
        //标书拒绝，撤销更新IS_PASS_BID_OPEN为1
        if (bidderOpenInfo.getTenderRejection() != null && bidderOpenInfo.getTenderRejection() == 0) {
            // 修改 是否通过开标
            bidderService.updateBiddersIsPassBidOpen(bidderOpenInfo.getBidderId(), 1);
        }
        return bidderOpenInfoMapper.updateById(bidderOpenInfo);
    }

    @Override
    public BidderOpenInfo getBidderOpenInfo(Integer bidderId, Integer bidSectionId) {
        QueryWrapper<BidderOpenInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.eq("BIDDER_ID", bidderId);
        return bidderOpenInfoMapper.selectOne(queryWrapper);
    }

    @Override
    @CacheEvict(value = CacheName.BIDDER_OPEN_INFO, key = "#bidderOpenInfo.id")
    public Integer updateById(BidderOpenInfo bidderOpenInfo) {
        return bidderOpenInfoMapper.updateById(bidderOpenInfo);
    }

    @Override
    public BidderOpenInfo getBidderOpenInfoById(Integer id) {
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoMapper.selectById(id);
        Bidder bidder = bidderService.getBidderById(bidderOpenInfo.getBidderId());
        bidderOpenInfo.setBidderName(bidder.getBidderName());
        try {
            // 获取委托书地址
            if (!CommonUtil.isEmpty(bidderOpenInfo.getSqwtsFileId())) {
                Fdfs fdfs = fdfsService.getFdfsByUpload(bidderOpenInfo.getSqwtsFileId());
                bidderOpenInfo.setSqwtsFdfsId(fdfs.getId());
            }
            // 如果是紧急签到获取头像地址
            if (bidderOpenInfo.getUrgentSigin() != null && bidderOpenInfo.getUrgentSigin() == 1) {
                String mark = fdfsService.getUrlByUpload(bidderOpenInfo.getSqwtsPngFileId());
                bidderOpenInfo.setPhotoUrl(mark);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bidderOpenInfo;
    }

    @Override
    public Integer insert(BidderOpenInfo bidderOpenInfo) {
        return bidderOpenInfoMapper.insert(bidderOpenInfo);
    }

    @Override
    public Integer coutListBidderOpenInfo(Integer bidSectionId, List<Integer> bidderIds, int decryptStatus) {
        QueryWrapper<BidderOpenInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.in("BIDDER_ID", bidderIds.toArray());
        queryWrapper.eq("DECRYPT_STATUS", decryptStatus);
        return bidderOpenInfoMapper.selectCount(queryWrapper);
    }

    @Override
    public Integer updateClientCheck(Integer id, String checkType, Integer passType) {
        BidderOpenInfo bidderOpenInfo = new BidderOpenInfo();
        bidderOpenInfo.setId(id);
        if ("identity".equals(checkType)) {
            bidderOpenInfo.setBidderIdentityStatus(passType);
        }
        if ("marginPay".equals(checkType)) {
            bidderOpenInfo.setMarginPayStatus(passType);
        }
        bidderOpenInfo.setIsClientCheck(1);
        return bidderOpenInfoMapper.updateById(bidderOpenInfo);
    }

    /**
     * 获取当前boi前后boi，
     *
     * @param bidSectionId
     * @param bidderIdTo
     * @param i            -1获取前boi，1获取后boi;
     * @return
     */
    @Override
    public BidderOpenInfo getAroundBoi(Integer bidSectionId, Integer bidderIdTo, int i) {
        QueryWrapper<BidderOpenInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("NOT_CHECKIN", 3);
        if (i == -1) {
            wrapper.orderByDesc("ID");
            wrapper.lt("ID", bidderIdTo);
        } else {
            wrapper.gt("ID", bidderIdTo);
        }
        List<BidderOpenInfo> list = bidderOpenInfoMapper.selectList(wrapper);
        if (list.size() > 0) {
            BidderOpenInfo bidderOpenInfo = list.get(0);
            Bidder bidder = bidderService.getBidderById(bidderOpenInfo.getBidderId());
            bidderOpenInfo.setBidderName(bidder.getBidderName());
            return bidderOpenInfo;
        }
        return null;
    }

    @Override
    public Map<String, Integer> getCheckNumByList(List<Bidder> list) {
        Map<String, Integer> checkNumMap = new HashMap<>();
        checkNumMap.put("isCheck", 0);
        checkNumMap.put("notCheck", 0);
        for (Bidder bidder : list) {
            BidderOpenInfo bidderOpenInfo = bidder.getBidderOpenInfo();
            if (bidderOpenInfo.getIsClientCheck() == 1) {
                checkNumMap.replace("isCheck", checkNumMap.get("isCheck") + 1);
            } else {
                checkNumMap.replace("notCheck", checkNumMap.get("notCheck") + 1);
            }
        }
        return checkNumMap;
    }

    @Override
    public List<BidderOpenInfo> listBidderOpenInfo(List<Bidder> bidders) {
        if (!CommonUtil.isEmpty(bidders)) {
            List<BidderOpenInfo> bidderOpenInfos = new ArrayList<>();
            for (Bidder bidder : bidders) {
                QueryWrapper<BidderOpenInfo> wrapper = new QueryWrapper<>();
                wrapper.eq("BIDDER_ID", bidder.getId());
                BidderOpenInfo bidderOpenInfo = bidderOpenInfoMapper.selectOne(wrapper);
                bidderOpenInfo.setBidderName(bidder.getBidderName());
                // 获取开标信息
                bidderOpenInfos.add(bidderOpenInfo);
            }
            return bidderOpenInfos;
        }
        return null;
    }

    @Override
    public void updateByBidderId(BidderOpenInfo bidderOpenInfo) {
        UpdateWrapper<BidderOpenInfo> wrapper = new UpdateWrapper<>();
        wrapper.eq("BIDDER_ID", bidderOpenInfo.getBidderId());
        bidderOpenInfoMapper.update(bidderOpenInfo, wrapper);
    }

    @Override
    @Cacheable(value = CacheName.BIDDER_OPEN_INFO, key = "'meetingBidderList_' + #bidSectionId", unless = "#result==null")
    public List<Bidder> listBidderForMeeting(Integer bidSectionId) {
        return bidderService.listPassBidOpenBidder(bidSectionId);
    }

    @Override
    @CacheEvict(value = CacheName.BIDDER_OPEN_INFO, key = "'meetingBidderList_' + #bidSectionId")
    public Integer updateDissentStatus(BidderOpenInfo bidderOpenInfo, Integer bidSectionId) {
        Assert.notNull(bidderOpenInfo.getId(), "param id can not be null!");
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        return bidderOpenInfoMapper.updateById(bidderOpenInfo);
    }

    @Override
    public Integer selectDecryptStatusCount(Integer bidSectionId, Integer decryptStatus) {
        return bidderOpenInfoMapper.selectDecryptStatusCount(bidSectionId, decryptStatus);
    }

}
