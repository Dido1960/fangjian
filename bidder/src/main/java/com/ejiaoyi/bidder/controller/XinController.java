package com.ejiaoyi.bidder.controller;

import cn.hutool.core.date.DateUtil;
import com.ejiaoyi.bidder.service.IIdentityAuthService;
import com.ejiaoyi.common.service.IXinIdIdentityAuthService;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.XinIdConstant;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.IBidderOpenInfoService;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


/**
 * @Description: 信ID认证回调控制器
 * @Auther: liuguoqiang
 * @Date: 2020-8-7 14:54
 */
@RestController
@RequestMapping("/xin")
public class XinController {
    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;
    @Autowired
    private IXinIdIdentityAuthService xinIdIdentityAuthService;
    @Autowired
    private IIdentityAuthService identityAuthService;

    @RequestMapping("/call")
    @UserLog(value = "'信ID认证结果：投标人ID：'+#id", dmlType = DMLType.UPDATE)
    public void xinCallBack(Integer id) {
        //查询boi数据
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfoById(id);
        BidderOpenInfo newBoi = new BidderOpenInfo();
        newBoi.setId(id);
        //认证状态(0:认证失败 1信Id认证成功  2支付宝认证成功)
        Integer anthStatus = null;
        String ticketNo = bidderOpenInfo.getTicketNo();
        //信ID判断回话状态为over修改认证结果
        if (XinIdConstant.OVER.equals(xinIdIdentityAuthService.getH5SessionStatus(ticketNo))) {
            String h5ResState = xinIdIdentityAuthService.getH5ResState(ticketNo);
            if (StringUtil.isNotEmpty(h5ResState)) {
                newBoi.setSqrPicUrl("/img/alSuccess.png");
//                newBoi.setSqrPicUrl(identityAuthService.uploadPhoto(h5ResState,id));
                anthStatus = 1;
            } else {
                anthStatus = 0;
            }
            //更新认证结果
            newBoi.setAuthentication(anthStatus);
            newBoi.setAuthTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
            newBoi.setSigninTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
            bidderOpenInfoService.updateById(newBoi);
        }
        newBoi.getClientName();
    }
}
