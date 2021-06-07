package com.ejiaoyi.agency.controller;

import com.alibaba.fastjson.JSON;
import com.ejiaoyi.common.constant.MessageType;
import com.ejiaoyi.common.constant.UserType;
import com.ejiaoyi.common.dto.ClinetCheckOne;
import com.ejiaoyi.common.dto.EndingCheck;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.agency.service.IClientCheckService;
import com.ejiaoyi.agency.support.AuthUser;
import com.ejiaoyi.agency.support.CurrentUserHolder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * @Description:委托人身份检查
 * @Auther: liuguoqiang
 * @Date: 2020/7/17 09:48
 */

@RestController
@RequestMapping("/clientCheck")
public class ClientCheckController {
    @Autowired
    private IClientCheckService clientCheckService;
    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IBidderExceptionService bidderExceptionService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;
    @Autowired
    private IMessageService messageService;

    /**
     * 身份检查列表页
     * @param id
     * @return
     */
    @RequestMapping("/clientCheckPage")
    public ModelAndView clientCheckPage(Integer id){
        ModelAndView mav = new ModelAndView("/openBid/clientCheck/clientCheck");
        //投标人公布是否已经结束检查
        Boolean isPublishBidderEnd =  clientCheckService.isPublishBidderEnd(id);

        //判断是否已经结束检查
        Boolean isEndCheck =  clientCheckService.isEndCheck(id);
        //判断标段状态是否为资格预审
        Boolean isA10Bid = clientCheckService.isA10Bid(id);
        //获取投标人列表
        List<Bidder> list =  clientCheckService.listCheckBidder(id);

        //初始化列表检查状态
        for (Bidder bidder : list) {
            clientCheckService.checkStatusInfo(bidder.getBidderOpenInfo().getId());
        }

        //判断当前列表的检查人数与未检查人数
        Map<String,Integer> checkNum = clientCheckService.getCheckNumByList(list);

        mav.addObject("bidSectionId",id);
        mav.addObject("isEndCheck",isEndCheck);
        mav.addObject("isA10Bid",isA10Bid);
        mav.addObject("list",list);
        mav.addObject("bidderNum",list.size());
        mav.addObject("checkNum",checkNum);
        mav.addObject("isPublishBidderEnd",isPublishBidderEnd);

        return mav;
    }

    /**
     * 检查页面
     * @param clientCheckOne
     * @return
     */
    @RequestMapping("/clientCheckOnePage")
    public ModelAndView clientCheckOnePage(ClinetCheckOne clientCheckOne) {
        ModelAndView mav = new ModelAndView("/openBid/clientCheck/clientCheckOne");
        //判断是否是A10
        clientCheckOne.setIsA10Bid(clientCheckService.isA10Bid(clientCheckOne.getBidSectionId()));
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfoById(clientCheckOne.getBidderIdTo());
        mav.addObject("clientCheckOne",clientCheckOne);
        mav.addObject("bidderOpenInfo",bidderOpenInfo);
        return mav;
    }

    /**
     * 获取页面初始化数据
     * @param clinetCheckOne 请求数据
     * @return 获取页面初始化数据
     */
    @RequestMapping("/getInitData")
    public ClinetCheckOne getInitData(ClinetCheckOne clinetCheckOne) {
        //修改当前bio为已检查
        clientCheckService.updateClientCheck(clinetCheckOne.getBidderIdTo(),clinetCheckOne.getCheckType(),clinetCheckOne.getPassType());
        //查询需要跳转的招标人数据
        clinetCheckOne.setBidderOpenInfo(clientCheckService.getBidderOpenInfoById(clinetCheckOne.getBidderIdTo()));
        //获取需要跳转的页面前后招标人
        clinetCheckOne.setLastBoi(clientCheckService.getAroundBoi(clinetCheckOne.getBidSectionId(),
                clinetCheckOne.getBidderIdTo(),
                -1));
        clinetCheckOne.setNextBoi(clientCheckService.getAroundBoi(clinetCheckOne.getBidSectionId(),
                clinetCheckOne.getBidderIdTo(),
                1));

        //更新检查人数
        //获取投标人列表
        List<Bidder> list =  clientCheckService.listCheckBidder(clinetCheckOne.getBidSectionId());
        //判断当前列表的检查人数与未检查人数
        Map<String,Integer> checkNum = clientCheckService.getCheckNumByList(list);
        clinetCheckOne.setNotCheck(checkNum.get("notCheck"));
        clinetCheckOne.setIsCheck(checkNum.get("isCheck"));

        //判断是否是A10
        clinetCheckOne.setIsA10Bid(clientCheckService.isA10Bid(clinetCheckOne.getBidSectionId()));
        //判断是否有异常理由
        clinetCheckOne.setIdentityBidderException(clientCheckService.getExceptionReson(clinetCheckOne.getBidderOpenInfo(),1));
        clinetCheckOne.setMarginPayBidderException(clientCheckService.getExceptionReson(clinetCheckOne.getBidderOpenInfo(),2));

        return clinetCheckOne;
    }


    /**
     * 通过fdfs的id跳转到pdf加载iframe
     *
     * @param fileId fdfsId
     * @return
     */
    @RequestMapping("/showPdfIframe")
    public ModelAndView showPdfIframe(Integer fileId) {
        ModelAndView modelAndView = new ModelAndView("openBid/clientCheck/iframe");
        modelAndView.addObject("fileId", fileId);
        return modelAndView;
    }


    /**
     * 通过fdfs的mark获取展示pdf页面
     *
     * @return
     */
    @RequestMapping("/showPdfByIdPage")
    public ModelAndView showPdfByIdPage(Integer fileId) {
        ModelAndView modelAndView = new ModelAndView("openBid/clientCheck/showPdfById");
        Fdfs fdfs = fdfsService.getFdfsByUpload(fileId);
        //加载开标记录表pdf
        if (fdfs != null) {
            modelAndView.addObject("fdfs", fdfs);
        }
        AuthUser user = CurrentUserHolder.getUser();
        modelAndView.addObject("user", user);
        return modelAndView;
    }


    /**
     * 更新检查结果
     * @param boiId
     * @param checkType 检查类型
     * @param passType
     */
    @RequestMapping("/clientCheckThis")
    public Boolean clientCheckThis(Integer boiId,String checkType,Integer passType){
        return clientCheckService.updateClientCheck(boiId,checkType,passType) > 0;
    }

    /**
     * 存储异常原因
     * @param bidderException
     * @return
     */
    @RequestMapping("/saveReason")
    public Boolean saveReason(BidderException bidderException){
        AuthUser user = CurrentUserHolder.getUser();
        return bidderExceptionService.saveOrUpdateReason(user.getUserId(), user.getName(), bidderException) > 0;
    }

    /**
     * 更新是否结束检查
     * @param lineStatus
     * @return
     */
    @RequestMapping("/updateCheckStatus")
    public Boolean updateCheckStatus(LineStatus lineStatus){
        if (Status.END.getCode().equals(lineStatus.getBidderCheckStatus())) {
            messageService.setMessageToRedis(lineStatus.getBidSectionId(), lineStatus.getMsg(), UserType.SYSTEM, MessageType.OTHER);
        }
        return clientCheckService.updateCheckStatus(lineStatus);
    }

    /**
     * 添加标书拒绝原因
     * @param bidderOpenInfo
     * @return
     */
    @RequestMapping("/addTenderReason")
    public Boolean addTenderReason(BidderOpenInfo bidderOpenInfo){
        return 1==clientCheckService.addTenderReason(bidderOpenInfo);
    }

    /**
     * 获取标书拒绝原因
     * @param id
     * @return
     */
    @RequestMapping("/getTenderReason")
    public BidderOpenInfo getTenderReason(Integer id){
        return clientCheckService.getBidderOpenInfoById(id);
    }

    /**
     * @Description 结束检查
     * @Author      liuguoqiang
     * @Date        2020-8-13 10:15
     */
    @RequestMapping("/endingCheck")
    public EndingCheck endingCheck(EndingCheck endingCheck){
        //判断标段状态是否为资格预审
        endingCheck.setIsA10Bid(clientCheckService.isA10Bid(endingCheck.getBidSectionId()));

        //更新检查人数
        //获取投标人列表
        List<Bidder> list =  clientCheckService.listCheckBidder(endingCheck.getBidSectionId());
        //判断当前列表的检查人数与未检查人数
        Map<String,Integer> checkNum = clientCheckService.getCheckNumByList(list);
        endingCheck.setNotCheck(checkNum.get("notCheck"));
        endingCheck.setIsCheck(checkNum.get("isCheck"));
        //
        List<BidderOpenInfo> boiListForReason = clientCheckService.getBoiListForReason(endingCheck);
        if(CollectionUtils.isNotEmpty(boiListForReason) && boiListForReason.size() > 0){
            endingCheck.setListSize(boiListForReason.size());
        }
        return endingCheck;
    }

    /**
     * 异常原因页面
     * @param endingCheck
     * @return
     */
    @RequestMapping("/getExceToReasonPage")
    public ModelAndView getExceToReasonPage(EndingCheck endingCheck) {
        ModelAndView mav = new ModelAndView("/openBid/clientCheck/exceReason");

        //判断标段状态是否为资格预审
        endingCheck.setIsA10Bid(clientCheckService.isA10Bid(endingCheck.getBidSectionId()));

        endingCheck.setBoiList(clientCheckService.getBoiListForReason(endingCheck));

        mav.addObject("endingCheck",endingCheck);
        return mav;
    }

    /**
     * 保存异常原因列表
     * @param str
     * @return
     */
    @RequestMapping("/saveReasonList")
    public Boolean saveReasonList(String str){
        str = StringEscapeUtils.unescapeHtml4(str);
        List<BidderException> list = JSON.parseArray(str, BidderException.class);
        return clientCheckService.saveReasonList(list);
    }
}
