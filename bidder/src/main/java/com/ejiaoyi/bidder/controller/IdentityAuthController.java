package com.ejiaoyi.bidder.controller;

import cn.hutool.core.date.DateUtil;
import com.ejiaoyi.common.service.IAlipayIdentityAuthService;
import com.ejiaoyi.bidder.service.IBidFileService;
import com.ejiaoyi.bidder.service.IIdentityAuthService;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.BlockchainType;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RandomStrUtil;
import com.ejiaoyi.common.util.ThreadUtlis;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 身份认证认证
 * @Auther: Make
 * @Date: 2020-8-3 13:10
 */

@RestController
@RequestMapping("/identityAuth")
public class IdentityAuthController {
    @Autowired
    private IIdentityAuthService identityAuthService;

    @Autowired
    private IAlipayIdentityAuthService alipayIdentityAuthService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Autowired
    private ILineStatusService lineStatusService;

    @Autowired
    private IBidderFileInfoService bidderFileInfoService;

    @Autowired
    private IBidFileService bidFileService;

    @Autowired
    private IBsnChainInfoService bsnChainInfoService;

    /**
     * @Description 跳转信息录入页面
     * @Author liuguoqiang
     * @Date 2020-8-4 17:26
     */
    @RequestMapping("/msgInputPage")
    public ModelAndView msgInputPage(Integer bidSectionId, Integer bidderId) {
        ModelAndView mav = new ModelAndView("/joinBid/identityAuth/msgInput");
        Bidder bidder = bidderService.getBidderById(bidderId);
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        //查询开标时间段
        Map<String, String> times = bidFileService.getBidSectionTimes(bidSectionId);

        //判断是否需要展示区块链
        BsnChainInfo lastBsnChainInfo = bsnChainInfoService.getLastBsnChainInfo(bidderId, BlockchainType.BIDDER_ATTORNEY);
        Boolean isUpChain = !CommonUtil.isEmpty(lastBsnChainInfo) && !CommonUtil.isEmpty(lastBsnChainInfo.getQueryAddress())
                && (Enabled.YES.getCode().equals(bidderOpenInfo.getAuthentication())|| Enabled.YES.getCode().equals(bidderOpenInfo.getUrgentSigin()));

        mav.addObject("bidSectionId", bidSectionId);
        mav.addObject("bidderId", bidderId);
        mav.addObject("bidder", bidder);
        mav.addObject("bidderOpenInfo", bidderOpenInfo);
        mav.addObject("lineStatus", lineStatus);
        mav.addObject("bidderFileInfo", bidderFileInfo);
        mav.addObject("times", times);
        mav.addObject("lastBsnChainInfo", lastBsnChainInfo);
        mav.addObject("isUpChain", isUpChain);
        return mav;
    }

    /**
     * @Description 保存投标人委托信息，完成
     * @Author liuguoqiang
     * @Date 2020-8-5 10:16
     */
    @RequestMapping("saveMsg")
    @UserLog(value = "'用户投标开标信息:bidderOpenInfo：'+#bidderOpenInfo", dmlType = DMLType.UPDATE)
    public Boolean saveMsg(BidderOpenInfo bidderOpenInfo) {
        boolean result = 1 == bidderOpenInfoService.updateById(bidderOpenInfo);
        if (result){
            ThreadUtlis.run(() -> bsnChainInfoService.bidderAttorney(bidderOpenInfo));
        }
        return result;
    }

    /**
     * @Description 跳转二维码页面，封装二维码数据
     * @Author liuguoqiang
     * @Date 2020-8-5 11:33
     */
    @RequestMapping("/identityAuthPage")
    public ModelAndView identityAuthPage(Integer bidSectionId, Integer bidderId) {
        ModelAndView mav = new ModelAndView("/joinBid/identityAuth/identityAuth");
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        mav.addObject("bidSectionId", bidSectionId);
        mav.addObject("bidderId", bidderId);
        mav.addObject("bidderOpenInfo", bidderOpenInfo);
        return mav;
    }

    /**
     * 生成认证二维码
     *
     * @param bidSectionId
     * @param bidderId
     * @param authType     选择的认证类型 1：信ID 2：支付宝
     * @Description
     * @Author liuguoqiang
     * @Date 2020-8-5 14:29
     */
    @RequestMapping("/madeQR")
    @UserLog(value = "'生成签到二维码:标段id：'+#bidSectionId+',投标人id：'+#bidderId+',验证类型：'+#anthType", dmlType = DMLType.UPDATE)
    public Map<String, String> madeQR(Integer bidSectionId, Integer bidderId, Integer authType) {
        Map<String, String> map = new HashMap<>();
        //获取投标人开标数据
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        //判断是否填写委托人信息
        String clientName = bidderOpenInfo.getClientName();
        String clientIdcard = bidderOpenInfo.getClientIdcard();
        if (StringUtil.isNotEmpty(clientName) && StringUtil.isNotEmpty(clientIdcard)) {
            BidderOpenInfo newBoi = new BidderOpenInfo();
            newBoi.setId(bidderOpenInfo.getId());
            //生成订单码（32位）
            String orderNo = "qskj" + RandomStrUtil.getOrderNoByCunt(28);

            String url = null;
            //选择认证类型
            if (authType == 1) {
                //信ID认证
                newBoi.setTicketNo(orderNo);
                url = "/phoneScan/toXinIdIdentityAuthPage?ticketId=" + orderNo
                        + "&sectionToken=" + bidSectionId
                        + "&bidderToken=" + bidderId;
            } else {
                //支付宝认证
                //获取回调地址
                String callBakUrl = identityAuthService.getCallBakUrl(bidderOpenInfo.getId(), authType);
                String certifyId = alipayIdentityAuthService.authInitialize(orderNo, clientIdcard, clientName, callBakUrl);

                newBoi.setTicketNo(certifyId);

                url = "/phoneScan/alipay/toAlipayIdentityAuthPage?certifyId=" + certifyId
                        + "&sectionToken=" + bidSectionId
                        + "&bidderToken=" + bidderId;
            }
            //更新查询订单号
            bidderOpenInfoService.updateById(newBoi);
            map.put("url", url);
        }
        return map;
    }

    /**
     * @Description 查询认证状态是否更新为认证成功
     * @Author liuguoqiang
     * @Date 2020-8-6 16:42
     */
    @RequestMapping("/isAuthPass")
    public Boolean isAuthPass(Integer boiId) {
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfoById(boiId);
        return !CommonUtil.isEmpty(bidderOpenInfo.getAuthentication()) && bidderOpenInfo.getAuthentication() == 1;
    }

    /**
     * @Description 跳转认证结束页面
     * @Author liuguoqiang
     * @Date 2020-8-6 17:08
     */
    @RequestMapping("/identityAuthEnd")
    public ModelAndView identityAuthEnd(Integer bidSectionId, Integer bidderId) {
        ModelAndView mav = new ModelAndView("/joinBid/identityAuth/identityAuthEnd");
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);

        //判断是否需要展示区块链
        BsnChainInfo lastBsnChainInfo = bsnChainInfoService.getLastBsnChainInfo(bidderId, BlockchainType.BIDDER_ATTORNEY);
        Boolean isUpChain = !CommonUtil.isEmpty(lastBsnChainInfo) && !CommonUtil.isEmpty(lastBsnChainInfo.getQueryAddress())
                && (Enabled.YES.getCode().equals(bidderOpenInfo.getAuthentication())|| Enabled.YES.getCode().equals(bidderOpenInfo.getUrgentSigin()));

        mav.addObject("bidderOpenInfo", bidderOpenInfo);
        mav.addObject("bidSectionId", bidSectionId);
        mav.addObject("bidderId", bidderId);
        mav.addObject("lastBsnChainInfo", lastBsnChainInfo);
        mav.addObject("isUpChain", isUpChain);
        return mav;
    }

    /**
     * @Description 跳转紧急签到页面
     * @Author liuguoqiang
     * @Date 2020-8-7 17:14
     */
    @RequestMapping("/urgentSiginPage")
    public ModelAndView urgentSiginPage(Integer bidSectionId, Integer bidderId) {
        ModelAndView mav = new ModelAndView("/joinBid/identityAuth/urgentSigin");
        if (bidderId != null && bidSectionId != null) {
            BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
            Bidder bidder = bidderService.getBidderById(bidderId);
            mav.addObject("bidder", bidder);
            mav.addObject("bidderOpenInfo", bidderOpenInfo);
        }
        mav.addObject("bidSectionId", bidSectionId);
        mav.addObject("bidderId", bidderId);
        return mav;
    }

    /**
     * @Description 更新为紧急签到
     * @Author liuguoqiang
     * @Date 2020-8-10 11:10
     */
    @RequestMapping("/updateUrgentSigin")
    @UserLog(value = "'投标人紧急签到:标段id：'+#bidderOpenInfo.bidSectionId+',投标人id：'+#bidderOpenInfo.bidderId", dmlType = DMLType.UPDATE)
    public Boolean updateUrgentSigin(BidderOpenInfo bidderOpenInfo) {
        //判断时间
        if (!identityAuthService.verifyQRCodeInvalid(bidderOpenInfo.getBidSectionId())){
            return false;
        }
        bidderOpenInfo.setUrgentSigin(1);
        bidderOpenInfo.setSigninTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        return bidderOpenInfoService.updateById(bidderOpenInfo) == 1;
    }

    /**
     * @Description 紧急签到成功页面
     * @Author liuguoqiang
     * @Date 2020-8-10 11:24
     */
    @RequestMapping("/urgentSiginEndPage")
    public ModelAndView urgentSiginEndPage(Integer bidSectionId, Integer bidderId) {
        ModelAndView mav = new ModelAndView("/joinBid/identityAuth/urgentSiginEnd");
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);

        //判断是否需要展示区块链
        BsnChainInfo lastBsnChainInfo = bsnChainInfoService.getLastBsnChainInfo(bidderId, BlockchainType.BIDDER_ATTORNEY);
        Boolean isUpChain = !CommonUtil.isEmpty(lastBsnChainInfo) && !CommonUtil.isEmpty(lastBsnChainInfo.getQueryAddress())
                && (Enabled.YES.getCode().equals(bidderOpenInfo.getAuthentication())|| Enabled.YES.getCode().equals(bidderOpenInfo.getUrgentSigin()));

        mav.addObject("bidderOpenInfo", bidderOpenInfo);
        mav.addObject("bidSectionId", bidSectionId);
        mav.addObject("bidderId", bidderId);
        mav.addObject("lastBsnChainInfo", lastBsnChainInfo);
        mav.addObject("isUpChain", isUpChain);
        return mav;
    }

    /**
     * @Description 撤销紧急签到
     * @Author liuguoqiang
     * @Date 2020-8-10 11:10
     */
    @RequestMapping("/cancelUrgentSigin")
    @UserLog(value = "'投标人撤销紧急签到:标段id：'+#bidderOpenInfo.bidSectionId+',投标人id：'+#bidderOpenInfo.bidderId", dmlType = DMLType.UPDATE)
    public Boolean cancelUrgentSigin(BidderOpenInfo bidderOpenInfo) {
        bidderOpenInfo.setUrgentSigin(0);
        bidderOpenInfo.setSigninTime("");
        return bidderOpenInfoService.updateById(bidderOpenInfo) == 1;
    }

    @RequestMapping("/cancelIdentity")
    @UserLog(value = "'投标人撤销身份认证签到:标段id：'+#bidderOpenInfo.bidSectionId+',投标人id：'+#bidderOpenInfo.bidderId", dmlType = DMLType.UPDATE)
    public Boolean cancelIdentity(BidderOpenInfo bidderOpenInfo) {
        BidderOpenInfo build = BidderOpenInfo.builder()
                .id(bidderOpenInfo.getId())
                .sqrPicUrl("")
                .authentication(0)
                .authTime("")
                .signinTime("").build();
        return bidderOpenInfoService.updateById(build) == 1;
    }

    /**
     * @Description 清空订单列表
     * @Author      liuguoqiang
     * @Date        2020-8-18 10:18
     */
    @RequestMapping("/clearTicketNo")
    @UserLog(value = "'投标人返回委托信息提交页面:bidderOpenInfoId：'+#bidderOpenInfo.id", dmlType = DMLType.UPDATE)
    public Boolean clearTicketNo(BidderOpenInfo bidderOpenInfo){
        bidderOpenInfo.setTicketNo("");
        return bidderOpenInfoService.updateById(bidderOpenInfo) == 1;
    }
}
