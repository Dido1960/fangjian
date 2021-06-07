package com.ejiaoyi.bidder.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.service.IAlipayIdentityAuthService;
import com.ejiaoyi.bidder.service.IIdentityAuthService;
import com.ejiaoyi.common.service.IXinIdIdentityAuthService;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.IBidderOpenInfoService;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

/**
 * @Description:用于手机扫描二维码的前端控制器，该控制器请求不在安全框架内
 * @Auther: liuguoqiang
 * @Date: 2020-8-6 14:51
 */
@RestController
@RequestMapping("/phoneScan")
public class PhoneScanController {
    @Autowired
    private IAlipayIdentityAuthService alipayIdentityAuthService;
    @Autowired
    private IIdentityAuthService identityAuthService;
    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;
    @Autowired
    private IXinIdIdentityAuthService xinIdIdentityAuthService;

    /**
     * 跳转支付宝身份认证页面
     *
     * @param certifyId    认证唯一标识 (由认证初始化方法产生)
     * @param sectionToken 标段id
     * @param bidderToken  投标人id
     *                     String ctx = request.getContextPath();
     *                     String base = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + ctx;
     *                     /alipay/toAlipayIdentityAuthPage
     * @return
     */
    @RequestMapping("/alipay/toAlipayIdentityAuthPage")
    @UserLog(value = "'跳转支付宝身份认证页面：认证服务号：'+#certifyId+',标段ID：'+#sectionToken+',投标人ID：'+#bidderToken", dmlType = DMLType.ACCESS)
    public ModelAndView bidFilePage(String certifyId, Integer sectionToken, Integer bidderToken) {
        ModelAndView mav = new ModelAndView("/joinBid/identityAuth/toAlipayIdentityAuth");

        // 校验二维码有效性
        boolean invalid = identityAuthService.verifyQRCodeInvalid(sectionToken);
        if (!invalid) {
            mav.addObject("invalid", 0);
            return mav;
        }

        // 校验该投标人是否完成了身份认证
        boolean completeAuth = identityAuthService.verifyCompleteAuth(bidderToken);
        if (completeAuth) {
            mav.addObject("completeAuth", 1);
            return mav;
        }

        if (StringUtil.isNotEmpty(certifyId)) {
            mav.addObject("certifyId", certifyId);
            String url = alipayIdentityAuthService.startCertify(certifyId);
            mav.addObject("url", url);

        }

        JSONObject bizContent = new JSONObject();
        bizContent.put("certify_id", certifyId);
        String bizContentStr = bizContent.toJSONString();
        mav.addObject("bizContent", bizContentStr);

        return mav;
    }

    /**
     * 跳转信ID身份认证页面
     *
     * @param ticketId     请求的唯一标识 (由我方生成)
     * @param sectionToken 标段id
     * @param bidderToken  投标人id
     * @return
     */
    @RequestMapping("/toXinIdIdentityAuthPage")
    @UserLog(value = "'跳转信ID身份认证页面：认证服务号：'+#ticketId+',标段ID：'+#sectionToken+',投标人ID：'+#bidderToken", dmlType = DMLType.ACCESS)
    public ModelAndView toXinIdIdentityAuthPage(String ticketId, Integer sectionToken, Integer bidderToken) {
        ModelAndView mav = new ModelAndView("/joinBid/identityAuth/toXinIdIdentityAuth");
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderToken, sectionToken);
        String clientName = bidderOpenInfo.getClientName();
        String clientIdcard = bidderOpenInfo.getClientIdcard();

        // 校验二维码有效性
        boolean invalid = identityAuthService.verifyQRCodeInvalid(sectionToken);
        if (!invalid) {
            mav.addObject("invalid", false);
            return mav;
        }

        // 校验该投标人是否完成了身份认证
        boolean completeAuth = identityAuthService.verifyCompleteAuth(bidderToken);
        if (completeAuth) {
            mav.addObject("completeAuth", true);
            return mav;
        }

        if (StringUtil.isNotEmpty(clientName) && StringUtil.isNotEmpty(clientIdcard) && StringUtil.isNotEmpty(ticketId)) {
            mav.addObject("ticketId", ticketId);

            String callBakUrl = identityAuthService.getCallBakUrl(bidderOpenInfo.getId(), 1);
            String url = xinIdIdentityAuthService.getH5URL(ticketId, clientIdcard, clientName, callBakUrl);
            mav.addObject("url", url);
        }
        return mav;
    }

    /**
     * @Description 手机验证后的回调页面
     * @Author liuguoqiang
     * @Date 2020-8-6 14:07
     */
    @RequestMapping("/phoneCallBackPage")
    @UserLog(value = "'支付宝认证结果：投标人ID：'+#boiId", dmlType = DMLType.UPDATE)
    public ModelAndView phoneCallBackPage(Integer boiId, Integer authType) {
        ModelAndView mav = new ModelAndView("/joinBid/identityAuth/phoneCallBack");
        //查询boi数据
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfoById(boiId);
        BidderOpenInfo newBoi = new BidderOpenInfo();
        newBoi.setId(boiId);

        int anthStatus;

        //支付宝认证结果
        if (alipayIdentityAuthService.queryCertify(bidderOpenInfo.getTicketNo())) {
            anthStatus = 1;
            newBoi.setSqrPicUrl("/img/alSuccess.png");
        } else {
            anthStatus = 0;
        }

        //更新认证结果
        newBoi.setAuthentication(anthStatus);
        newBoi.setAuthTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        newBoi.setSigninTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        bidderOpenInfoService.updateById(newBoi);

        mav.addObject("boiId", boiId);
        mav.addObject("authType", authType);
        mav.addObject("anthStatus", anthStatus);

        return mav;
    }
}
