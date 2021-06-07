package com.ejiaoyi.api.controller.mobilePhone;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.api.service.IStatisticalDataService;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.XinIdConstant;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.IAlipayIdentityAuthService;
import com.ejiaoyi.common.service.IBidderOpenInfoService;
import com.ejiaoyi.common.service.IXinIdIdentityAuthService;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

/**
 * @Description: 手机接口身份认证请求
 * @Auther: liuguoqiang
 * @Date: 2021-5-21 16:21
 */
@RestController
@RequestMapping("/auth")
public class PhoneAuthController {
    @Autowired
    private IStatisticalDataService statisticalDataService;
    @Autowired
    private IAlipayIdentityAuthService alipayIdentityAuthService;
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
     * @return
     */
    @RequestMapping("/toAliIdentityAuthPage")
    @UserLog(value = "'跳转支付宝身份认证页面：认证服务号：'+#certifyId+',标段ID：'+#sectionToken+',投标人ID：'+#bidderToken", dmlType = DMLType.ACCESS)
    public ModelAndView bidFilePage(String certifyId, Integer sectionToken, Integer bidderToken) {
        ModelAndView mav = new ModelAndView("/auth/toAlipayIdentityAuth");

        // 校验二维码有效性
        boolean invalid = statisticalDataService.verifyQRCodeInvalid(sectionToken);
        if (!invalid) {
            mav.addObject("invalid", 0);
            return mav;
        }

        // 校验该投标人是否完成了身份认证
        boolean completeAuth = statisticalDataService.verifyCompleteAuth(bidderToken);
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
        ModelAndView mav = new ModelAndView("/auth/toXinIdIdentityAuth");
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderToken, sectionToken);
        String clientName = bidderOpenInfo.getClientName();
        String clientIdcard = bidderOpenInfo.getClientIdcard();

        // 校验二维码有效性
        boolean invalid = statisticalDataService.verifyQRCodeInvalid(sectionToken);
        if (!invalid) {
            mav.addObject("invalid", false);
            return mav;
        }

        // 校验该投标人是否完成了身份认证
        boolean completeAuth = statisticalDataService.verifyCompleteAuth(bidderToken);
        if (completeAuth) {
            mav.addObject("completeAuth", true);
            return mav;
        }

        if (StringUtil.isNotEmpty(clientName) && StringUtil.isNotEmpty(clientIdcard) && StringUtil.isNotEmpty(ticketId)) {
            mav.addObject("ticketId", ticketId);

            String callBakUrl = statisticalDataService.getAuthCallBakUrl(bidderOpenInfo.getId(), 1);
            String url = xinIdIdentityAuthService.getH5URL(ticketId, clientIdcard, clientName, callBakUrl);
            mav.addObject("url", url);
        }
        return mav;
    }

    @RequestMapping("/aliCall")
    @UserLog(value = "'支付宝认证结果：投标人ID：'+#boiId", dmlType = DMLType.UPDATE)
    public ModelAndView phoneCallBackPage(Integer boiId) {
        ModelAndView mav = new ModelAndView("/auth/phoneCallBack");
        //查询boi数据
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfoById(boiId);
        BidderOpenInfo newBoi = new BidderOpenInfo();
        newBoi.setId(boiId);

        int authStatus;

        //支付宝认证结果
        if (alipayIdentityAuthService.queryCertify(bidderOpenInfo.getTicketNo())) {
            authStatus = 1;
            newBoi.setSqrPicUrl("/img/alSuccess.png");
        } else {
            authStatus = 0;
        }

        //更新认证结果
        newBoi.setAuthentication(authStatus);
        newBoi.setAuthTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        newBoi.setSigninTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        bidderOpenInfoService.updateById(newBoi);

        mav.addObject("boiId", boiId);
        mav.addObject("authStatus", authStatus);

        return mav;
    }

    @RequestMapping("/xCall")
    @UserLog(value = "'信ID认证结果：投标人ID：'+#id", dmlType = DMLType.UPDATE)
    public void xinCallBack(Integer id) {
        //查询boi数据
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfoById(id);
        BidderOpenInfo newBoi = new BidderOpenInfo();
        newBoi.setId(id);
        //认证状态(0:认证失败 1信Id认证成功  2支付宝认证成功)
        Integer authStatus = null;
        String ticketNo = bidderOpenInfo.getTicketNo();
        //信ID判断回话状态为over修改认证结果
        if (XinIdConstant.OVER.equals(xinIdIdentityAuthService.getH5SessionStatus(ticketNo))) {
            String h5ResState = xinIdIdentityAuthService.getH5ResState(ticketNo);
            if (StringUtil.isNotEmpty(h5ResState)) {
                newBoi.setSqrPicUrl("/img/alSuccess.png");
                authStatus = 1;
            } else {
                authStatus = 0;
            }
            //更新认证结果
            newBoi.setAuthentication(authStatus);
            newBoi.setAuthTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
            newBoi.setSigninTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
            bidderOpenInfoService.updateById(newBoi);
        }
    }
}
