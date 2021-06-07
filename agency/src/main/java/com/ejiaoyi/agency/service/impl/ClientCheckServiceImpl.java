package com.ejiaoyi.agency.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.constant.PublishBiddersState;
import com.ejiaoyi.common.dto.EndingCheck;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.mapper.BidderOpenInfoMapper;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.agency.service.IClientCheckService;
import com.ejiaoyi.agency.support.AuthUser;
import com.ejiaoyi.agency.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/21 09:52
 */

@Service
public class ClientCheckServiceImpl extends BaseServiceImpl implements IClientCheckService {

    @Autowired
    private IBidSectionService bidSectionService;


    @Autowired
    private ILineStatusService lineStatusService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private BidderOpenInfoMapper bidderOpenInfoMapper;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Autowired
    private IBidderExceptionService bidderExceptionService;

    @Autowired
    private IFDFSService fdfsService;

    /**
     * 是否结束身份检查
     *
     * @param bidSectionId
     * @return true结束
     */
    @Override
    public Boolean isEndCheck(Integer bidSectionId) {
        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        if (CommonUtil.isEmpty(lineStatus.getBidderCheckStatus()) || lineStatus.getBidderCheckStatus() == 1 || lineStatus.getBidderCheckStatus() == 0) {
            if (CommonUtil.isEmpty(lineStatus.getBidderCheckStatus()) || lineStatus.getBidderCheckStatus() == 0) {
                LineStatus ls = LineStatus.builder()
                        .id(lineStatus.getId())
                        .bidSectionId(bidSectionId)
                        .bidderCheckStatus(1)
                        .build();
                lineStatusService.updateLineStatus(ls);
            }
            return false;
        }
        return true;
    }

    /**
     * 判断是否为资格预审
     *
     * @param bidSectionId
     * @return true是资格预审
     */
    @Override
    public Boolean isA10Bid(Integer bidSectionId) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        return BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode());
    }

    /**
     * 获取投标人列表
     *
     * @param bidSectionId
     * @return
     */
    @Override
    public List<Bidder> listCheckBidder(Integer bidSectionId) {
        return bidderService.listBiddersForCheck(bidSectionId);
    }

    /**
     * 通过投标人列表获取当前检查以及未检查人数
     *
     * @return mao isCheck: 检查人数; notCheck: 未检查人数
     */
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

    /**
     * 更新身份检查
     *
     * @param boiId     更新的BidderOpenInfoId
     * @param checkType 更新的类型 identity:身份验证   marginPay：保证金验证
     * @param passType  是否通过
     * @return
     */
    @Override
    public Integer updateClientCheck(Integer boiId, String checkType, Integer passType) {
        BidderOpenInfo bidderOpenInfo = new BidderOpenInfo();
        bidderOpenInfo.setId(boiId);
        if ("identity".equals(checkType)) {
            bidderOpenInfo.setBidderIdentityStatus(passType);
        }
        if ("marginPay".equals(checkType)) {
            bidderOpenInfo.setMarginPayStatus(passType);
        }
        bidderOpenInfo.setIsClientCheck(1);
        return bidderOpenInfoMapper.updateById(bidderOpenInfo);
    }

    @Override
    public BidderOpenInfo getBidderOpenInfoById(Integer bioId) {
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoMapper.selectById(bioId);
        Bidder bidder = bidderService.getBidderById(bidderOpenInfo.getBidderId());
        bidderOpenInfo.setBidderName(bidder.getBidderName());
        if (!CommonUtil.isEmpty(bidderOpenInfo.getSqwtsFileId())){
            bidderOpenInfo.setSqwtsMark(fdfsService.getMarkByUpload(bidderOpenInfo.getSqwtsFileId()));
        }
        // 如果是紧急签到获取头像地址
        if (bidderOpenInfo.getUrgentSigin() != null && bidderOpenInfo.getUrgentSigin() == 1) {
            String urlByUpload = fdfsService.getUrlByUpload(bidderOpenInfo.getSqwtsPngFileId());
            bidderOpenInfo.setPhotoUrl(urlByUpload);
        }
        return bidderOpenInfo;
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
        if (i == -1) {
            wrapper.orderByDesc("ID");
            wrapper.lt("ID", bidderIdTo);
        } else {
            wrapper.gt("ID", bidderIdTo);
        }
        wrapper.and(sql ->sql.isNull("NOT_CHECKIN").or().eq("NOT_CHECKIN",3));
        List<BidderOpenInfo> list = bidderOpenInfoMapper.selectList(wrapper);
        if (list.size() > 0) {
            BidderOpenInfo bidderOpenInfo = list.get(0);
            Bidder bidder = bidderService.getBidderById(bidderOpenInfo.getBidderId());
            bidderOpenInfo.setBidderName(bidder.getBidderName());
            return bidderOpenInfo;
        }
        return null;
    }

    /**
     * 通过bio的标段id，投标人id，异常类型获取异常理由，查不到返回null;
     *
     * @param bidderOpenInfo
     * @param exceType
     * @return
     */
    @Override
    public BidderException getExceptionReson(BidderOpenInfo bidderOpenInfo, int exceType) {
        return bidderExceptionService.getExceptionReason(bidderOpenInfo, exceType);
    }

    @Override
    public boolean updateCheckStatus(LineStatus lineStatus) {
        lineStatusService.updateFileUploadOrsigninStatus(lineStatus.getBidSectionId());
        LineStatus ls = lineStatusService.getLineStatusBySectionId(lineStatus.getBidSectionId());
        LineStatus updateLs = LineStatus.builder()
                .id(ls.getId())
                .bidSectionId(ls.getBidSectionId())
                .bidderCheckStatus(lineStatus.getBidderCheckStatus())
                .build();
        return lineStatusService.updateLineStatus(updateLs);
    }

    @Override
    public Integer addTenderReason(BidderOpenInfo bidderOpenInfo) {
        return bidderOpenInfoService.updateBidderOpenInfoById(bidderOpenInfo);
    }

    @Override
    public Integer checkStatusInfo(Integer bidderIdTo) {
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoMapper.selectById(bidderIdTo);
        if (bidderOpenInfo.getBidderIdentityStatus() == null || bidderOpenInfo.getMarginPayStatus() == null) {
            if (bidderOpenInfo.getBidderIdentityStatus() == null) {
                bidderOpenInfo.setBidderIdentityStatus(1);
            }
            if (bidderOpenInfo.getMarginPayStatus() == null) {
                bidderOpenInfo.setMarginPayStatus(1);
            }
            return bidderOpenInfoMapper.updateById(bidderOpenInfo);
        }
        return 0;
    }

    @Override
    public List<BidderOpenInfo> getBoiListForReason(EndingCheck endingCheck) {
        List<Bidder> bidderList = bidderService.listBiddersForCheck(endingCheck.getBidSectionId());
        List<BidderOpenInfo> list = new ArrayList<>();
        for (Bidder bidder : bidderList) {
            list.add(bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidder.getBidSectionId()));
        }
        ArrayList<BidderOpenInfo> resultList = new ArrayList<>();
        if (list.size() > 0) {
            for (BidderOpenInfo bidderOpenInfo : list) {
                Bidder bidder = bidderService.getBidderById(bidderOpenInfo.getBidderId());
                bidderOpenInfo.setBidderName(bidder.getBidderName());

                BidderException bidderException = new BidderException();
                bidderException.setBidSectionId(endingCheck.getBidSectionId());
                bidderException.setBidderId(bidderOpenInfo.getBidderId());
                if (!endingCheck.getIsA10Bid()) {
                    if (bidderOpenInfo.getMarginPayStatus() == 0) {
                        bidderException.setExceptionType(2);
                        BidderException exce = bidderExceptionService.getExceptionReasonByReason(bidderException);
                        if (exce != null) {
                            bidderOpenInfo.setMarginPayReason(exce);
                        }
                    }
                }
                if (bidderOpenInfo.getBidderIdentityStatus() == 0) {
                    bidderException.setExceptionType(1);
                    BidderException exce = bidderExceptionService.getExceptionReasonByReason(bidderException);
                    if (exce != null) {
                        bidderOpenInfo.setIdentityReason(exce);
                    }
                }
                if (bidderOpenInfo.getBidderIdentityStatus() == 0 || (!endingCheck.getIsA10Bid() && bidderOpenInfo.getMarginPayStatus() == 0)) {
                    resultList.add(bidderOpenInfo);
                }
            }
            return resultList;
        }
        return null;
    }

    @Override
    public Boolean saveReasonList(List<BidderException> list) {
        Integer result = 0;
        if (list.size() > 0) {
            for (BidderException bidderException : list) {
                if (bidderException.getId() != null) {
                    result += bidderExceptionService.updateBidderException(bidderException);
                } else {
                    AuthUser user = CurrentUserHolder.getUser();
                    bidderException.setOperatorId(user.getUserId());
                    bidderException.setOperatorName(user.getName());
                    bidderException.setOperatTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
                    bidderException.setEnabled(1);
                    result += bidderExceptionService.addBidderException(bidderException);
                }
            }
        }
        return result == list.size() && result != 0;
    }

    @Override
    public Boolean isPublishBidderEnd(Integer bidSectionId) {
        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        Integer publishBidderStatus = lineStatus.getPublishBidderStatus();
        return PublishBiddersState.ENDCHECK.equals(publishBidderStatus);
    }
}
