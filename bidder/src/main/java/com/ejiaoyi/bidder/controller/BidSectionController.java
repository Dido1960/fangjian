package com.ejiaoyi.bidder.controller;

import com.ejiaoyi.bidder.support.AuthUser;
import com.ejiaoyi.bidder.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.ExecuteCode;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.TimeUnit;


/**
 * 项目标段信息 controller
 *
 * @author yyb
 * @since 2020-7-31
 */
@RestController
@RequestMapping("/bidder/bidSection")
public class BidSectionController {
    @Autowired
    IBidSectionService bidSectionService;
    @Autowired
    IBidderService bidderService;
    @Autowired
    private IBidSectionRelateService bidSectionRelateService;
    @Autowired
    private ITenderProjectService tenderProjectService;
    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;
    @Autowired
    private IFDFSService fdfsService;
    @Value("${message.bidder-id-pre}")
    private String bidderIdPre;
    /**
     * 获取标段列表
     * @param bidSection 标段
     * @return
     */
    @RequestMapping(value = "/listBidSection", produces = {"text/html;charset=utf-8"})
    public String listBidSection(BidSection bidSection) {
        AuthUser user = CurrentUserHolder.getUser();
        return bidSectionService.listJoinBidSection(bidSection, user.getCode());
    }

    /**
     * 获取 项目标段数目
     * @return
     */
    @RequestMapping("/getProjectTotal")
    public Integer getProjectTotal(BidSection bidSection) {
        AuthUser user = CurrentUserHolder.getUser();
        return bidSectionService.getProjectTotal(bidSection, user.getCode());
    }

    /**
     * 添加投标人开标信息(我要参标)
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/addBidderOpenInfo")
    @UserLog(value = "'添加投标人参标信息: bidSectionId='+#bidSectionId", dmlType = DMLType.INSERT)
    public JsonData addBidderOpenInfo(Integer bidSectionId) {
        AuthUser user = CurrentUserHolder.getUser();
        JsonData result = new JsonData();
        String userCode = user.getCode();
        String key = "BID_SECTION_ID_" + bidSectionId + "_BIDDER_ORG_CODE_" + userCode;
        Object bidderObj = RedisUtil.get(key);
        try {
            if (bidderObj == null) {
                RedisUtil.set(key, Status.NOT_START.getCode(), 300);
                Boolean addFlag = bidderService.addBidderOpenInfo(bidSectionId, userCode , user.getName());
                if (addFlag){
                    Bidder bidder = bidderService.getBidder(userCode,bidSectionId).get(0);
                    result.setCode(ExecuteCode.SUCCESS.getCode().toString());
                    result.setMsg("参标成功！");
                    result.setData(bidder.getId());
                } else {
                    result.setCode(ExecuteCode.FAIL.getCode().toString());
                    result.setMsg("参标失败！");
                }
                RedisUtil.delete(key);
            } else {
                result.setCode(ExecuteCode.FAIL.getCode().toString());
                result.setMsg("该投标人已参标，请勿频繁请求！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ExecuteCode.FAIL.getCode().toString());
            result.setMsg(e.getMessage());
            RedisUtil.delete(key);
        }

        return result;
    }

    /**
     * 进入复会
     *
     * @return
     */
    @RequestMapping("/joinMeeting")
    public ModelAndView joinMeeting() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView mav = new ModelAndView();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        // 当前时间
        String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        // 复会倒计时
        String resumeTime = bidSection.getResumeTime().replace("/","-");
        long countDownTime = DateTimeUtil.getTimeDiff(nowTime, resumeTime, TimeUnit.SECONDS, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        if (countDownTime<=0){
            // 重定向至，复会主页面
            mav.setViewName("redirect:/bidder/bidSection/joinMeetingPage");
            return mav;
        }

        mav.setViewName("/meeting/resumpCountDown");
        mav.addObject("user", user);
        mav.addObject("bidSection", bidSection);
        mav.addObject("countDownTime", countDownTime);
        mav.addObject("tenderProject", tenderProject);
        return mav;
    }

    /**
     * 跳转复会页面
     *
     * @return
     */
    @RequestMapping("/joinMeetingPage")
    public ModelAndView joinMeetingPage() {
        ModelAndView mav = new ModelAndView("/meeting/index");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        // 获取复会报告
        BidSectionRelate relate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        Fdfs resumeReport = fdfsService.getFdfdById(relate.getResumptionReportId());



        mav.addObject("user",user);
        mav.addObject("bidder", bidderService.getBidderById(user.getBidderId()));
        mav.addObject("bidSection",bidSection);
        mav.addObject("relate",relate);
        mav.addObject("resumeReport",resumeReport);
        mav.addObject("tenderProject",tenderProject);
        return mav;
    }

    /**
     *  当前复会状态
     *
     * @return
     */
    @RequestMapping("/resumptionStatus")
    public JsonData resumptionStatus() {
        JsonData jsonData = new JsonData();
        try {
            AuthUser user = CurrentUserHolder.getUser();
            Integer bidSectionId = user.getBidSectionId();
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            jsonData.setCode(ExecuteCode.SUCCESS.getCode().toString());
            jsonData.setMsg("查询成功");
            jsonData.setData(bidSection);

        } catch (Exception e) {
            e.printStackTrace();
            jsonData.setCode(ExecuteCode.FAIL.getCode().toString());
            jsonData.setMsg(e.getMessage());
        }
        return jsonData;
    }

    /**
     * 更新异议状态
     */
    @RequestMapping("/updateDissentStatus")
    @UserLog(value = "'投标人更新异议状态：标段ID：'+#bidSectionId+'，投标人ID：'+#bidderId+'，更新状态：'+#status", dmlType = DMLType.UPDATE)
    public boolean updateDissentStatus(Integer bidSectionId, Integer bidderId, Integer status){
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        return bidderOpenInfoService.updateDissentStatus(BidderOpenInfo.builder().id(bidderOpenInfo.getId()).resumeDetermine(status).build(), bidSectionId) == 1;
    }

}
