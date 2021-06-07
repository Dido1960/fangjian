package com.ejiaoyi.bidder.service.impl;

import com.ejiaoyi.bidder.service.*;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 投标人模块服务实现类
 *
 * @author Make
 * @since 2020-08-05
 */
@Service
public class BidderModelServiceImpl extends BaseServiceImpl implements IBidderModelService {

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private ILineStatusService lineStatusService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Override
    public List<String> listOnlineProcessComplete(Bidder bidder) {
        List<String> process = new ArrayList<>();
        BidSection section = bidSectionService.getBidSectionById(bidder.getBidSectionId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidder.getBidSectionId());
        lineStatusService.updateFileUploadOrsigninStatus(bidder.getBidSectionId());
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidder.getBidSectionId());
        bidder = bidderService.getBidderById(bidder.getId());
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidder.getBidSectionId());

        // 投标文件上传
        if (!CommonUtil.isEmpty(bidder.getBidDocId())) {
            process.add(BidderOnlineFlow.BID_FILE.getName());
        }

        // 签到
        if (!CommonUtil.isEmpty(bidderOpenInfo.getSigninTime())) {
            process.add(BidderOnlineFlow.IDENTITY_AUTH.getName());
        }

        if (BidProtype.CONSTRUCTION.getCode().equals(section.getBidClassifyCode())) {
            // 招标控制价
            if (StringUtil.isNotEmpty(tenderDoc.getControlPrice())) {
                process.add(BidderOnlineFlow.CONTROL_PRICE.getName());
            }

            // 浮动点
            if (StringUtil.isNotEmpty(tenderDoc.getFloatPoint())) {
                process.add(BidderOnlineFlow.FLOAT_POINT.getName());
            }
        }

        if (BidProtype.EPC.getCode().equals(section.getBidClassifyCode())) {
            // 招标控制价
            if (StringUtil.isNotEmpty(tenderDoc.getControlPrice())) {
                process.add(BidderOnlineFlow.CONTROL_PRICE.getName());
            }
        }

        // 投标人名单
        boolean bidderCheck = null != lineStatus && (Status.END.getCode().equals(lineStatus.getPublishBidderStatus())
                || Status.PROCESSING.getCode().equals(lineStatus.getPublishBidderStatus()));
        if (bidderCheck) {
            process.add(BidderOnlineFlow.ALL_BIDDERS.getName());
        }

        // 文件上传及解密
        boolean decrypt = null != lineStatus && (Status.PROCESSING.getCode().equals(lineStatus.getDecryptionStatus())
                || Status.END.getCode().equals(lineStatus.getDecryptionStatus()));
        if (decrypt) {
            process.add(BidderOnlineFlow.BIDDER_FILE_DECRYPT.getName());
        }

        // 报价确认
        if (!CommonUtil.isEmpty(bidderOpenInfo.getPriceDetermine()) && bidderOpenInfo.getPriceDetermine() == 1) {
            process.add(BidderOnlineFlow.CONFIRM_BIDDER_PRICE.getName());
            // 控制价分析
            if (null != lineStatus && Status.END.getCode().equals(lineStatus.getDecryptionStatus())) {
                if (BidProtype.CONSTRUCTION.getCode().equals(section.getBidClassifyCode())
                        || BidProtype.EPC.getCode().equals(section.getBidClassifyCode())) {
                    process.add(BidderOnlineFlow.CONTROL_PRICE_ANALYSIS.getName());
                }
            }
        }

        // 开标一览表确认
        if (!CommonUtil.isEmpty(bidderOpenInfo.getDocDetermine()) && bidderOpenInfo.getDocDetermine() == 1) {
            process.add(BidderOnlineFlow.CONFIRM_BID_OPEN_RECORD.getName());
        }

        // 开标结束
        if (Status.END.getCode().equals(section.getBidOpenStatus())) {
            process.add(BidderOnlineFlow.BID_OPEN_END.getName());
            // 复会时间
            if (StringUtil.isNotEmpty(section.getResumeTime())) {
                process.add(BidderOnlineFlow.RESUME_TIME.getName());
            }
        }

        return process;
    }

    @Override
    public Long getQueueTime(String queueStartTime, String queueEndTime) {
        if (CommonUtil.isEmpty(queueStartTime) || CommonUtil.isEmpty(queueEndTime)) {
            return 0L;
        }
        return DateTimeUtil.getTimeDiff(queueStartTime, queueEndTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
    }

    @Override
    public List<String> listBidderBaseFlow(Integer bidSectionId) {
        List<String> process = new ArrayList<>();
        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        process.add("uploadBidFiles");
        // 项目开标是否可以进入
        boolean projectOpen = (lineStatus != null && Status.END.getCode().equals(lineStatus.getSigninStatus()));
        if (projectOpen) {
            process.add("projectOpen");
        }
        // 项目复会是否可以进入
        boolean projectResume = (bidSection != null && Status.END.getCode().equals(bidSection.getBidOpenStatus()));
        if (projectResume) {
            process.add("projectResume");
        }
        return process;
    }

    @Override
    public List<Object> listBidOpenHallBidSection() {
        List<Object> list = new ArrayList<>();
        BidSection bidSection = BidSection.builder()
                .bidOpenOnline(1)
                .scopeOpenNumDay("1")
                .deleteFlag(0)
                .build();
        // 获取今日网上开标开标项目(未开标 + 已开标【包含正在开标的标段】)
        List<BidSection> notOpen = bidSectionService.listBidSectionNoPage(bidSection.setBidOpenStatus(0));
        List<BidSection> ingOpen = bidSectionService.listBidSectionNoPage(bidSection.setBidOpenStatus(1));
        ingOpen.addAll(bidSectionService.listBidSectionNoPage(bidSection.setBidOpenStatus(2)));

        // 如果今日没有开标项目，查询将来和过去的项目
        if (ingOpen.size() == 0 && notOpen.size() == 0) {
            // 获取今日往后(将来)的开标项目到未开标列表
            notOpen = bidSectionService.listBidSectionNoPage(BidSection.builder()
                    .bidOpenOnline(1)
                    .deleteFlag(0)
                    .build());
            // 列举近期三个项目到已开标列表（过去）
            if (notOpen.size() == 0) {
                ingOpen = bidSectionService.listThreeBidSection();
            }
        }
        list.add(ingOpen);
        list.add(notOpen);
        // 展示一项开标项目在页面左侧
        if (ingOpen.size() != 0) {
            list.add(ingOpen.get(0));
        } else if (notOpen.size() != 0) {
            list.add(notOpen.get(0));
        }

        return list;

    }

}
