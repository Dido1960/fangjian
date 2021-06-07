package com.ejiaoyi.bidder.controller;

import com.ejiaoyi.bidder.service.IBidFileService;
import com.ejiaoyi.bidder.support.AuthUser;
import com.ejiaoyi.bidder.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.dto.CurrentScheduleDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.BidderFileInfo;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.FileSchedule;
import com.ejiaoyi.common.enums.PaperFileSchedule;
import com.ejiaoyi.common.service.IBidSectionService;
import com.ejiaoyi.common.service.IBidderFileInfoService;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.IUploadFileService;
import com.ejiaoyi.common.service.impl.LineStatusServiceImpl;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020-7-29 16:48
 */

@RestController
@RequestMapping("/bidFile")
public class BidFileController extends BaseController {
    @Autowired
    private IBidFileService bidFileService;
    @Autowired
    private IUploadFileService uploadFileService;
    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IBidderFileInfoService bidderFileInfoService;
    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private LineStatusServiceImpl lineStatusService;

    /**
     * 跳转文件递交页面
     *
     * @param bidSectionId
     * @param bidderId
     * @return
     */
    @RequestMapping("/bidFileUpLoadPage")
    public ModelAndView bidFilePage(Integer bidSectionId, Integer bidderId)  {
        ModelAndView mav = new ModelAndView("/joinBid/bidFile/bidFileUpLoad");
        //数据封装
        //查询bidderOpenInfo数据
        BidderOpenInfo bidderOpenInfo = bidFileService.getBoiByIds(bidSectionId, bidderId);
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        Integer receiptId = bidderFileInfo != null ? bidderFileInfo.getReceiptId() : null;
        //查询开标时间段
        Map<String, String> times = bidFileService.getBidSectionFileUploadTimes(bidSectionId);

        //判已经上传了文件,回显文件
        if (bidderFileInfo != null && bidderFileInfo.getReceiptId() != null) {
            if (bidderFileInfo.getGefId() != null) {
                mav.addObject("gefFile", uploadFileService.getById(bidderFileInfo.getGefId()));
            }
            if (bidderFileInfo.getSgefId() != null) {
                mav.addObject("sgefFile", uploadFileService.getById(bidderFileInfo.getSgefId()));
            }
            if (bidderFileInfo.getCzrId() != null) {
                mav.addObject("czrFile", uploadFileService.getById(bidderFileInfo.getCzrId()));
            }
            Fdfs receiptFile = fdfsService.getFdfdById(bidderFileInfo.getReceiptId());
            mav.addObject("receiptFile", receiptFile);
            mav.addObject("bidderFileInfo", bidderFileInfo);
        }

        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        // 签到时间转换“XX天XX小时XX分钟”
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        Integer signInStartTimeLeft = bidSection.getSignInStartTimeLeft();
        if (!CommonUtil.isEmpty(signInStartTimeLeft)) {
            int days = signInStartTimeLeft / (60 * 24);
            int hours = signInStartTimeLeft / (60) - days * 24;
            int minutes = signInStartTimeLeft - (hours * 60) - (days * 24 * 60);

            mav.addObject("days", days);
            mav.addObject("hours", hours);
            mav.addObject("minutes", minutes);
        }

        mav.addObject("bidSection", bidSection);
        mav.addObject("bidderOpenInfo", bidderOpenInfo);
        mav.addObject("times", times);
        mav.addObject("receiptId", receiptId);

        return mav;
    }

    @RequestMapping("/saveFile")
    @UserLog(value = "'用户校验投标文件:标段id：'+#bidderOpenInfo.bidSectionId+',投标人id：'+#bidderOpenInfo.bidderId", dmlType = DMLType.UPDATE)
    public JsonData saveFile(BidderOpenInfo bidderOpenInfo) {
        String redisKey = "BEFORE_SAVE_FILE_" + bidderOpenInfo.getBidSectionId() + "_" + bidderOpenInfo.getBidderId();
        Object before = RedisUtil.get(redisKey);
        JsonData result = new JsonData();
        if (CommonUtil.isEmpty(before)) {
            return bidFileService.fileJudge(bidderOpenInfo);
        }
        result.setMsg("后台处理中，请勿重复点击!");
        result.setCode(Enabled.NO.getCode().toString());
        return result;
    }

    @RequestMapping("/currentSchedulePage")
    public ModelAndView currentSchedulePage(Integer bidSectionId, Integer bidderId){
        ModelAndView mav = new ModelAndView("/joinBid/bidFile/currentSchedule");
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<CurrentScheduleDTO> currentScheduleDTOS;
        if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
            currentScheduleDTOS = PaperFileSchedule.listPaperCurrentSchedule();
        }else {
            currentScheduleDTOS = FileSchedule.listCurrentSchedule();
        }

        mav.addObject("bidSectionId",bidSectionId);
        mav.addObject("bidderId",bidderId);
        mav.addObject("currentScheduleDTOS",currentScheduleDTOS);
        return mav;
    }

    @RequestMapping("/getCurrentSchedule")
    public Object getCurrentSchedule(Integer bidSectionId, Integer bidderId){
        String scheduleKey = "CURRENT_SCHEDULE_" + bidSectionId + "_" + bidderId;
        return RedisUtil.get(scheduleKey);
    }


    /**
     * 通过fdfs的id跳转到pdf加载iframe
     *
     * @param mark fdfsId
     * @return
     */
    @RequestMapping("/showPdfIframe")
    public ModelAndView showPdfIframe(String mark) {
        ModelAndView modelAndView = new ModelAndView("/joinBid/bidFile/iframe");
        AuthUser user = CurrentUserHolder.getUser();
        modelAndView.addObject("user",CurrentUserHolder.getUser());
        modelAndView.addObject("mark",mark);
        return modelAndView;
    }

    /**
     * 通过fdfs的id获取文件地址
     *
     * @param fileId 文件id
     * @return
     */
    @RequestMapping("/showPdfById")
    public ModelAndView showPdfById(Integer fileId) {
        ModelAndView modelAndView = new ModelAndView("/showPdfIframe/showPdfById");
        Fdfs fdfs = fdfsService.getFdfdById(fileId);
        //加载开标记录表pdf
        if (fdfs != null) {
            modelAndView.addObject("fdfs", fdfs);
        }
        AuthUser user = CurrentUserHolder.getUser();
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    /**
     * 跳转文件递交页面
     *
     * @param bidSectionId
     * @param bidderId
     * @return
     */
    @RequestMapping("/bidPaperFileUpLoadPage")
    public ModelAndView bidPaperFileUpLoadPage(Integer bidSectionId, Integer bidderId)  {
        ModelAndView mav = new ModelAndView("/joinBid/bidFile/bidPaperFileUpLoadPage");
        //数据封装
        //查询bidderOpenInfo数据
        BidderOpenInfo bidderOpenInfo = bidFileService.getBoiByIds(bidSectionId, bidderId);
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        Integer receiptId = bidderFileInfo != null ? bidderFileInfo.getReceiptId() : null;
        //查询开标时间段
        Map<String, String> times = bidFileService.getBidSectionFileUploadTimes(bidSectionId);

        //判已经上传了文件,回显文件
        if (bidderFileInfo != null && bidderFileInfo.getReceiptId() != null) {
            if (bidderFileInfo.getGefId() != null) {
                mav.addObject("yckFile", uploadFileService.getById(bidderFileInfo.getGefId()));
            }
            if (bidderFileInfo.getSgefId() != null) {
                mav.addObject("syckFile", uploadFileService.getById(bidderFileInfo.getSgefId()));
            }
            if (bidderFileInfo.getCzrId() != null) {
                mav.addObject("czrFile", uploadFileService.getById(bidderFileInfo.getCzrId()));
            }
            Fdfs receiptFile = fdfsService.getFdfdById(bidderFileInfo.getReceiptId());
            mav.addObject("receiptFile", receiptFile);
            mav.addObject("bidderFileInfo", bidderFileInfo);
        }

        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        // 签到时间转换“XX天XX小时XX分钟”
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        Integer signInStartTimeLeft = bidSection.getSignInStartTimeLeft();
        if (!CommonUtil.isEmpty(signInStartTimeLeft)) {
            int days = signInStartTimeLeft / (60 * 24);
            int hours = signInStartTimeLeft / (60) - days * 24;
            int minutes = signInStartTimeLeft - (hours * 60) - (days * 24 * 60);

            mav.addObject("days", days);
            mav.addObject("hours", hours);
            mav.addObject("minutes", minutes);
        }

        mav.addObject("bidSection", bidSection);
        mav.addObject("bidderOpenInfo", bidderOpenInfo);
        mav.addObject("times", times);
        mav.addObject("receiptId", receiptId);

        return mav;
    }

    @RequestMapping("/savePaperFile")
    @UserLog(value = "'用户投标文件上传信息:bidderOpenInfo：'+#bidderOpenInfo", dmlType = DMLType.UPDATE)
    public JsonData savePaperFile(BidderOpenInfo bidderOpenInfo) {
        String redisKey = "BEFORE_SAVE_FILE_" + bidderOpenInfo.getBidSectionId() + "_" + bidderOpenInfo.getBidderId();
        Object before = RedisUtil.get(redisKey);
        JsonData result = new JsonData();
        if (CommonUtil.isEmpty(before)) {
            return bidFileService.paperFileJudge(bidderOpenInfo);
        }
        result.setMsg("后台处理中，请勿重复点击!");
        result.setCode(Enabled.NO.getCode().toString());
        return result;
    }

}
