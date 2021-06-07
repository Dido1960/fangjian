package com.ejiaoyi.bidder.controller;

import com.ejiaoyi.bidder.service.*;
import com.ejiaoyi.bidder.support.AuthUser;
import com.ejiaoyi.bidder.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.*;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @Description: 投标人模块主控制层
 * @Auther: liuguoqiang
 * @Date: 2020-8-4 10:11
 */

@RestController
@RequestMapping("/bidderModel")
public class BidderModelController extends BaseController {

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderProjectService tenderProjectService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Autowired
    private IBidderModelService bidderModelService;

    @Autowired
    private ILineStatusService lineStatusService;

    @Autowired
    private ILineMsgService lineMsgService;

    @Autowired
    private IBsnChainInfoService bsnChainInfoService;

    @Autowired
    private IBidderFileInfoService bidderFileInfoService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    LatiLongitudeService latiLongitudeService;

    @Autowired
    IBidSectionRelateService bidSectionRelateService;



    @Value("${message.bidder-id-pre}")
    private String bidderIdPre;


    /**
     * 进入网上开标
     * @return
     */
    @RequestMapping("/bidOpenOnline")
    public ModelAndView bidOpenOnline() {
        Integer bidSectionId=CurrentUserHolder.getUser().getBidSectionId();
        Integer bidderId=CurrentUserHolder.getUser().getBidderId();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("forward:/bidderModel/baseBidderPage");
        mav.addObject("bidSectionId", bidSectionId);
        mav.addObject("bidderId", bidderId);
        return mav;
    }

    @RequestMapping("/baseBidderPage")
    public ModelAndView baseBidder() {
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        Integer bidderId = CurrentUserHolder.getUser().getBidderId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        Bidder bidder = bidderService.getBidderById(bidderId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String bidOpenTime = tenderDoc.getBidOpenTime();
        String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        // 当前时间 > 开标时间 显示消息盒子 (-1)
        int result = DateTimeUtil.compareDate(bidOpenTime, nowTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        ModelAndView mav = new ModelAndView("/joinBid/bidderBase/bidderBasePage");
        mav.addObject("tenderProject", tenderProject);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("bidder", bidder);
        mav.addObject("showMessBox",result);
        return mav;
    }

    /**
     * 跳转文件上传和签到页面
     * @return
     */
    @RequestMapping("/uploadBidFiles")
    public ModelAndView uploadBidFiles() {
        ModelAndView mav = new ModelAndView("/joinBid/bidderBase/uploadBase");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        Integer bidderId = user.getBidderId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        Bidder bidder = bidderService.getBidderById(bidderId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        mav.addObject("tenderProject", tenderProject);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("bidder", bidder);
        return mav;
    }

    /**
     * 跳转项目开标页面
     *
     * @return
     */
    @RequestMapping("/projectOpenPage")
    public ModelAndView projectOpenPage() {
        ModelAndView mav = new ModelAndView("/joinBid/bidderBase/projectOpenBase");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        Integer bidderId = user.getBidderId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        Bidder bidder = bidderService.getBidderById(bidderId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        mav.addObject("tenderProject", tenderProject);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("bidder", bidder);
        return mav;
    }

    /**
     * 跳转项目复会页面
     *
     * @return
     */
    @RequestMapping("/projectResumePage")
    public ModelAndView projectResumePage() {
        ModelAndView mav = new ModelAndView("/joinBid/bidderBase/projectResumeBase");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        Integer bidderId = user.getBidderId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        Bidder bidder = bidderService.getBidderById(bidderId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);

        mav.addObject("tenderProject", tenderProject);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("bidder", bidder);
        return mav;
    }

    /**
     * 校验投标人环节能否进入
     * @param bidSectionId 标段id
     * @param flow 流程值
     * @return
     */
    @RequestMapping("/validEnterFlow")
    public JsonData validEnterFlow(Integer bidSectionId, Integer flow) {
        JsonData data = new JsonData();
        data.setCode(ExecuteCode.SUCCESS.getCode().toString());
        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        if (Status.PROCESSING.getCode().equals(flow)) {
            boolean projectOpen = (lineStatus != null && Status.END.getCode().equals(lineStatus.getSigninStatus()));
            if (!projectOpen) {
                data.setCode(ExecuteCode.FAIL.getCode().toString());
                data.setMsg("该环节还未开始!");
            }
        } else if (Status.END.getCode().equals(flow)) {
            boolean projectResume = (bidSection != null && Status.END.getCode().equals(bidSection.getBidOpenStatus()));
            if (!projectResume) {
                data.setCode(ExecuteCode.FAIL.getCode().toString());
                data.setMsg("该环节还未开始!");
            }
        }
        return data;
    }

    /**
     * 获取开标大流程进行情况
     *
     * @param bidSectionId
     * @return
     */
    @RequestMapping("/listBidderBaseFlow")
    public List<String> listBidderBaseFlow(Integer bidSectionId) {
        return bidderModelService.listBidderBaseFlow(bidSectionId);
    }

    /**
     * 投标控制价page
     *
     * @return
     */
    @RequestMapping("/controlPricePage")
    public ModelAndView controlPrice( ) {
        Integer bidSectionId=CurrentUserHolder.getUser().getBidSectionId();
        ModelAndView mav = new ModelAndView("/joinBid/controlPrice/bidControlPrice");
        mav.addObject("tenderDoc", tenderDocService.getTenderDocBySectionId(bidSectionId));
        mav.addObject("bidSection", bidSectionService.getBidSectionById(bidSectionId));
        return mav;
    }

    /**
     * 浮动点
     *
     * @return
     */
    @RequestMapping("/floatPointPage")
    public ModelAndView floatPoint( ) {
        Integer bidSectionId=CurrentUserHolder.getUser().getBidSectionId();
        ModelAndView mav = new ModelAndView("/joinBid/floatPoint/floatPoint");
        mav.addObject("tenderDoc", tenderDocService.getTenderDocBySectionId(bidSectionId));
        return mav;
    }

    /**
     * 跳转投标人列表页面
     * @return
     */
    @RequestMapping("/allBiddersPage")
    public ModelAndView allBidders( ) {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId=user.getBidSectionId();
        ModelAndView mav = new ModelAndView("/joinBid/bidders/allBidders");
        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        Map<String, Object> data = bidderService.getBidder(bidSectionId, user.getCode(), StageConstant.BIDDER_LIST);
        mav.addObject("data", data);
        mav.addObject("lineStatus", lineStatus);
        return mav;
    }

    /**
     * 跳转投标文件解密页面
     *
     * @return
     */
    @RequestMapping("/bidderFileDecryptPage")
    public ModelAndView bidderFileDecrypt() {
        ModelAndView mav = new ModelAndView("/joinBid/bidFile/bidFileDecrypt");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        Integer bidderId = user.getBidderId();
        mav.addObject("bidderId", bidderId);
        Bidder bidder = bidderService.getBidderById(bidderId);
        BidderOpenInfo bidderOpenInfo = bidder.getBidderOpenInfo();
        mav.addObject("bidderOpenInfo", bidderOpenInfo);
        mav.addObject("bidSection", bidSectionService.getBidSectionById(bidSectionId));
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        mav.addObject("bidderFileInfo",bidderFileInfo);
        mav.addObject("phoneCa", PassWordConstant.PHONE_CA);
        if (!CommonUtil.isEmpty(bidderFileInfo)){
            if (!CommonUtil.isEmpty(bidderFileInfo.getGefId())){
                Fdfs gefFile = fdfsService.getFdfsByUpload(bidderFileInfo.getGefId());
                mav.addObject("gefFile",gefFile);
            }
            if (!CommonUtil.isEmpty(bidderFileInfo.getSgefId())){
                Fdfs sgefFile = fdfsService.getFdfsByUpload(bidderFileInfo.getSgefId());
                mav.addObject("sgefFile",sgefFile);
            }
        }
        //进行中获取当前排队人数，计算解密时间；
        Object decryptRedisStatus = RedisUtil.get("bidSection_" + bidSectionId + "_BidFileDecrypt_" + bidderId);
        if (!CommonUtil.isEmpty(decryptRedisStatus)) {
            Object queueCount = RedisUtil.get("bidSection:" + bidSectionId);
            String queueStartTime = (String) RedisUtil.get("bidSection_" + bidSectionId + "_queueStartTime_" + bidderId);
            mav.addObject("queueCount", CommonUtil.isEmpty(queueCount) ? 0 : queueCount);
            mav.addObject("queueTime", bidderModelService.getQueueTime(queueStartTime, DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS)));
        }
        //解密完成计算解密时间
        if (!CommonUtil.isEmpty(bidderOpenInfo.getDecryptStatus()) && bidderOpenInfo.getDecryptStatus() == 1) {
            mav.addObject("decryptTime", bidderModelService.getQueueTime(bidderOpenInfo.getDecryptStartTime(), bidderOpenInfo.getDecryptEndTime()));
        }

        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);

        BsnChainInfo lastBsnChainInfo = bsnChainInfoService.getLastBsnChainInfo(bidderId, BlockchainType.BIDDER_DECLASSIFIED_RECORDS);
        Boolean isUpChain = !CommonUtil.isEmpty(lastBsnChainInfo) && !CommonUtil.isEmpty(lastBsnChainInfo.getQueryAddress()) && bidderOpenInfo.getDecryptStatus() == 1;

        //获取当前标段总共参加解密的投标人，以及已经解密的人
        Map<String, Integer> data = bidderService.getDecryptNum(bidSectionId);

        mav.addObject("isUpChain", isUpChain);
        mav.addObject("lastBsnChainInfo", lastBsnChainInfo);
        mav.addObject("lineStatus", lineStatus);
        mav.addObject("decryptRedisStatus", decryptRedisStatus);
        mav.addObject("decryptNum", data);
        return mav;
    }

    /**
     * 解密成功区块二维码图片
     *
     * @param response xiang ying
     * @param bidderId 投标人id
     */
    @RequestMapping("/imgBinary")
    public void imgBinary(HttpServletResponse response, Integer bidderId) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            BsnChainInfo lastBsnChainInfo = bsnChainInfoService.getLastBsnChainInfo(bidderId, BlockchainType.BIDDER_DECLASSIFIED_RECORDS);
            String content = lastBsnChainInfo.getQueryAddress();
            String qrLogoPath = FileUtil.getProjectResourcePath() + File.separator + "img" + File.separator + "qrLogo.png";
            TwoDimensionCode.encoderQRCode(content, outputStream, FileType.PNG.getSuffix(), 10,qrLogoPath);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 投标文件解密
     *
     * @param fileId   文件id
     * @param bidderId 投标人id
     * @return
     */
    @RequestMapping("/decrypt")
    @UserLog(value = "'投标文件解密: 文件id：'+#fileId+',投标人id：'+#bidderId+',标段id：'+#bidSectionId+',文件类型（0：非证书加密，1：证书加密）：'+#fileType+',私钥：'+#privateKey+',是否互认解密：'+#isOtherCa", dmlType = DMLType.UPDATE)
    public JsonData decrypt(Integer fileId, Integer bidderId, Integer bidSectionId, String fileType, String privateKey, String isOtherCa) {
        JsonData result = new JsonData();
        try {
            bidderService.decrypt(fileId, bidderId, bidSectionId, fileType, privateKey, isOtherCa);
            result.setCode(ExecuteCode.SUCCESS.getCode().toString());
            result.setMsg("文件解密中");
            //获取当前排队人数
            Integer queueCount = (Integer) RedisUtil.get("bidSection:" + bidSectionId);
            result.setData(CommonUtil.isEmpty(queueCount) ? 0 : queueCount);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ExecuteCode.FAIL.getCode().toString());
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 对接投标人文件上传和解密
     *
     * @param fileId   文件id
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/dockUploadBidFile")
    @UserLog(value = "'投标人文件上传:标段id：'+#bidSectionId+',文件id：'+#fileId", dmlType = DMLType.UPDATE)
    public JsonData dockUploadBidFile(Integer fileId, Integer bidSectionId) {
        JsonData result = new JsonData();
        try {
            AuthUser user = CurrentUserHolder.getUser();
            Bidder bidder = bidderService.getBidder(user.getCode(), bidSectionId).get(0);
            bidderService.dockUploadBidFile(fileId, bidder);
            result.setCode(ExecuteCode.SUCCESS.getCode().toString());
            result.setMsg("上传成功,文件解密中");
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ExecuteCode.FAIL.getCode().toString());
            result.setMsg("上传失败");
        }
        return result;
    }

    /**
     * 查询解密情况
     *
     * @param bidderId 投标人id
     * @return
     */
    @RequestMapping("/decryptSiu")
    public JsonData decryptSiu(Integer bidderId, Integer bidSectionId) {
        JsonData result = new JsonData();
        //查看缓存
        Integer decryptRedisStatus = (Integer) RedisUtil.get("bidSection_" + bidSectionId + "_BidFileDecrypt_" + bidderId);

        if (decryptRedisStatus != null) {
            //解密中查询排队人数 返回3 标书解密中
            result.setCode(FileState.ING.toString());
            Integer queueCount = (Integer) RedisUtil.get("bidSection:" + bidSectionId);
            result.setData(CommonUtil.isEmpty(queueCount) ? 0 : queueCount);
            return result;
        }
        // 获取解密错误信息
        String decryptMsg = "BID_SECTION_" + bidSectionId + "_DECRYPT_MSG:" + bidderId;
        String decryptMsgRedis = (String) RedisUtil.get(decryptMsg);
        if (StringUtils.isNotEmpty(decryptMsgRedis)){
            result.setCode(FileState.FAIL.toString());
            result.setMsg(decryptMsgRedis);
            RedisUtil.delete(decryptMsg);
            return result;
        }
        //查询解密状态
        BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        //解密完成 返回解密状态
        result.setCode(boi.getDecryptStatus().toString());
        //解密成功 计算当前解密的解密用时覆盖前端解密用时；
        if (!CommonUtil.isEmpty(boi.getDecryptStatus()) && boi.getDecryptStatus() == 1) {
            result.setData(bidderModelService.getQueueTime(boi.getDecryptStartTime(), boi.getDecryptEndTime()));
        }
        return result;
    }

    /**
     * 跳转报价确认页面
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/confirmBidderPricePage")
    public ModelAndView confirmBidderPrice(Integer bidSectionId) {
        ModelAndView mav = new ModelAndView("/joinBid/confirmPrice/confirmBidderPrice");
        AuthUser user = CurrentUserHolder.getUser();
        List<Bidder> bidders = bidderService.getBidder(user.getCode(), bidSectionId);
        if (bidders.size() > 0) {
            mav.addObject("bidder", bidders.get(0));
        }
        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        mav.addObject("lineStatus", lineStatus);
        return mav;
    }

    /**
     * 确认报价
     *
     * @param bidder 投标人
     * @return
     */
    @RequestMapping("/confirmPrice")
    @UserLog(value = "'确认报价: bidderId='+#bidder.id + ' bidderName=' " +
            "+ #bidder.bidderName + ' clientName=' + #bidder.bidderOpenInfo.clientName " +
            "+ ' clientIdcard=' + #bidder.bidderOpenInfo.clientIdcard " +
            "+ ' bidPrice=' + #bidder.bidPrice " +
            "+ ' bidPriceChinese=' + #bidder.bidPriceChinese", dmlType = DMLType.UPDATE)
    public boolean confirmPrice(Bidder bidder) {
        try {
            return bidderService.confirmPrice(bidder);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 跳转开标一览表确认页面
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/confirmBidOpenRecordPage")
    public ModelAndView confirmBidOpenRecord(Integer bidSectionId) {
        AuthUser user = CurrentUserHolder.getUser();
        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        ModelAndView mav = new ModelAndView("/joinBid/confirmRecord/confirmBidOpenRecord");
        assert user != null;
        Bidder bidder = bidderService.getBidder(user.getCode(), bidSectionId).get(0);
        //查询是否有区块链数据
        BsnChainInfo lastBsnChainInfo = bsnChainInfoService.getLastBsnChainInfo(bidder.getId(), BlockchainType.BID_OPENING_RECORD);
        Boolean isUpChain = !CommonUtil.isEmpty(lastBsnChainInfo) && !CommonUtil.isEmpty(lastBsnChainInfo.getQueryAddress());

        mav.addObject("data", bidderService.getBidder(bidSectionId, user.getCode(), StageConstant.CONFIRM_RECORD));
        mav.addObject("bidder", bidder);
        mav.addObject("bidderIdPre", bidderIdPre);
        mav.addObject("questionStatus", lineStatus.getQuestionStatus());
        mav.addObject("decryptionStatus", lineStatus.getDecryptionStatus());
        mav.addObject("isUpChain", isUpChain);
        mav.addObject("lastBsnChainInfo",lastBsnChainInfo);

        return mav;
    }

    /**
     * 跳转异议页面
     *
     * @param id 投标人id
     * @return
     */
    @RequestMapping("/dissentPage")
    public ModelAndView dissentPage(Integer id) {
        ModelAndView mav = new ModelAndView("/joinBid/confirmRecord/dissentPage");
        Bidder bidder = bidderService.getBidderById(id);
        mav.addObject("bidder", bidder);
        return mav;
    }

    /**
     * 添加网上开标消息(投标人消息)
     *
     * @param lineMsg 网上开标消息
     * @return
     */
    @RequestMapping("/addDissent")
    @UserLog(value = "'新增网上开标消息(投标人消息):'+ #lineMsg", dmlType = DMLType.INSERT)
    public Boolean dissent(LineMsg lineMsg) {
        AuthUser user = CurrentUserHolder.getUser();
        List<Bidder> bidders = bidderService.getBidder(user.getCode(),lineMsg.getBidSectionId());
        if (bidders.size()==0){
            return false;
        }
        Bidder bidder = bidders.get(0);
        lineMsg.setSendName(bidder.getBidderName());
        boolean addFlag = lineMsgService.addLineMsg(lineMsg);
        if (addFlag) {
            //确认开标一览表
            String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(lineMsg.getBidderId(), lineMsg.getBidSectionId());
            BidderOpenInfo bidderOpenInfo = BidderOpenInfo.builder()
                    .id(boi.getId())
                    .docDetermine(1)
                    .docDetermineTime(nowTime)
                    .build();
            bidderOpenInfoService.updateById(bidderOpenInfo);
            //确认开标结果，再次请求区块接口
            ThreadUtlis.run(() -> {
                try {
                    bsnChainInfoService.decryptBsnChainPut(bidder.getBidDocId(), bidder,nowTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return addFlag;
    }

    /**
     * 判断当前用户 有 or无异议
     *
     * @return
     */
    @RequestMapping("/isDissentOrNot")
    @UserLog(value = "'判断当前用户是否点过无异议:标段id：'+#bidSectionId+',投标人id：'+#bidderId", dmlType = DMLType.UPDATE)
    public Boolean isDissentOrNot(Integer bidderId,Integer bidSectionId) {
        BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        return boi.getDissentStatus() == 1;
    }

    /**
     * 通过id获取当前标段的开标流程的完成情况
     *
     * @param bidder
     * @return
     */
    @RequestMapping("/listOnlineProcessComplete")
    public List<String> listOnlineProcessComplete(Bidder bidder) {
        return bidderModelService.listOnlineProcessComplete(bidder);
    }

    /**
     * 跳转控制价分析页面
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/controlPriceAnalysisPage")
    public ModelAndView controlPriceAnalysisPage(Integer bidSectionId) {
        AuthUser user = CurrentUserHolder.getUser();
        ModelAndView mav = new ModelAndView("/joinBid/controlPrice/controlPriceAnalysis");
        List<Bidder> bidders = bidderService.listDecrySuccessBidder(bidSectionId, false);
        assert user != null;
        List<Bidder> currentBidders = bidderService.getBidder(user.getCode(), bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        mav.addObject("bidders", bidders);
        mav.addObject("controlPrice", tenderDoc.getControlPrice());
        mav.addObject("bidSection", bidSectionService.getBidSectionById(bidSectionId));
        if (currentBidders.size()==1){
            mav.addObject("currentBidders", currentBidders.get(0));
        }
        return mav;
    }


    /**
     * 跳转开标结束页面
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/bidOpenEndPage")
    public ModelAndView bidOpenEndPage(Integer bidSectionId) {
        ModelAndView mav = new ModelAndView("/joinBid/bidOpenEnd/bidOpenEnd");
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String bidOpenTime = tenderDoc.getBidOpenTime();
        String bidOpenEndTime = bidSection.getBidOpenEndTime();
        String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        boolean isOpenBid = DateTimeUtil.compareDate(nowTime, bidOpenTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) == 1;
        boolean isOpenBidEnd = true;
        if (StringUtils.isNotEmpty(bidOpenEndTime)) {
            isOpenBidEnd = DateTimeUtil.compareDate(nowTime, bidOpenEndTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) != 1;
        }
        mav.addObject("isOpenBid", isOpenBid);
        mav.addObject("isOpenBidEnd", isOpenBidEnd);
        return mav;
    }

    /**
     * 跳转复会时间页面
     *
     * @param bidSectionId 标段id
     * @param bidderId     投标人id
     * @return
     */
    @RequestMapping("/resumeTimePage")
    public ModelAndView resumeTimePage(Integer bidSectionId, Integer bidderId) {
        ModelAndView mav = new ModelAndView("/joinBid/resume/resumeTime");
        Bidder bidder = bidderService.getBidderById(bidderId);
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);

        mav.addObject("resumeTime", bidSection.getResumeTime());
        mav.addObject("bidder", bidder);
        mav.addObject("bidderOpenInfo", bidderOpenInfo);
        mav.addObject("briefReportUrl", fdfsService.getFdfdById(bidSectionRelate.getResumptionReportId()));
        mav.addObject("lineStatus", lineStatus);
        return mav;
    }

    /**
     * 跳转复会异议页面
     *
     * @param id 投标人id
     * @return
     */
    @RequestMapping("/resumeDissentPage")
    public ModelAndView resumeDissentPage(Integer id) {
        ModelAndView mav = new ModelAndView("/joinBid/resume/resumeDissent");
        mav.addObject("bidder", bidderService.getBidderById(id));
        return mav;
    }

    /**
     * 直播页面
     *
     * @return
     */
    @RequestMapping("/toLivePage")
    public ModelAndView toLivePage() {
        ModelAndView mav = new ModelAndView("/joinBid/live/live");
        AuthUser user = CurrentUserHolder.getUser();
        Integer id = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderProject", tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId()));
        mav.addObject("tenderDoc", tenderDocService.getTenderDocBySectionId(id));
        mav.addObject("liveUrlAddress", LiveBroadCastConstant.LIVE_URL);

        return mav;
    }

    /**
     * 修改 开标信息
     *
     * @param bidderOpenInfo 开标信息
     * @return
     */
    @RequestMapping("/updateBidderOpenInfo")
    @UserLog(value = "'修改开标信息: bidderOpenInfo='+#bidderOpenInfo", dmlType = DMLType.UPDATE)
    public boolean updateBidderOpenInfo(BidderOpenInfo bidderOpenInfo) {
        //查询开标信息id，用于跟新缓存！
        BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidderOpenInfo.getBidderId(), bidderOpenInfo.getBidSectionId());
        bidderOpenInfo.setId(boi.getId());
        return bidderOpenInfoService.updateBidderOpenInfo(bidderOpenInfo);
    }

    /**
     * 设置用户操作的bidsetionId
     *
     * @param bidSectionId
     * @return
     */
    @PostMapping("/setBidSectionIdAndBidderId")
    public void setBidSectionId(Integer bidSectionId,Integer bidderId) {
        CurrentUserHolder.getUser().setBidSectionId(bidSectionId);
        if(bidderId!=null){
            CurrentUserHolder.getUser().setBidderId(bidderId);
        }
    }
    /**
     * 存入坐标
     * @param latiLongitude
     * @return
     */
    @RequestMapping("/saveLatiLongitude")
    public Boolean saveLatiLongitude(LatiLongitude latiLongitude){
        AuthUser user = CurrentUserHolder.getUser();
        latiLongitude.setBidderName(user.getName());
        // 判断是否是游客
        if(user.getName().contains("游客_")){
            latiLongitude.setType(1);
        }else {
            latiLongitude.setType(0);
        }
        return latiLongitudeService.saveLatiLongitude(latiLongitude);
    }

    /**
     * 跳转投标文件解密页面（纸质标）
     *
     * @return
     */
    @RequestMapping("/bidderPaperFileDecryptPage")
    public ModelAndView bidderPaperFileDecrypt() {
        ModelAndView mav = new ModelAndView("/joinBid/bidFile/bidPaperFileDecrypt");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId=user.getBidSectionId();
        Integer bidderId=user.getBidderId();
        mav.addObject("bidderId", bidderId);
        Bidder bidder = bidderService.getBidderById(bidderId);
        BidderOpenInfo bidderOpenInfo = bidder.getBidderOpenInfo();
        mav.addObject("bidderOpenInfo", bidderOpenInfo);
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        mav.addObject("bidderFileInfo",bidderFileInfo);
        if (!CommonUtil.isEmpty(bidderFileInfo)){
            if (!CommonUtil.isEmpty(bidderFileInfo.getGefId())){
                Fdfs yckFile = fdfsService.getFdfsByUpload(bidderFileInfo.getGefId());
                mav.addObject("yckFile",yckFile);
            }
            if (!CommonUtil.isEmpty(bidderFileInfo.getSgefId())){
                Fdfs syckFile = fdfsService.getFdfsByUpload(bidderFileInfo.getSgefId());
                mav.addObject("syckFile",syckFile);
            }
        }
        //进行中获取当前排队人数，计算解密时间；
        Object decryptRedisStatus = RedisUtil.get("bidSection_" + bidSectionId + "_BidFileDecrypt_" + bidderId);
        if (!CommonUtil.isEmpty(decryptRedisStatus)) {
            Object queueCount = RedisUtil.get("bidSection:" + bidSectionId);
            String queueStartTime = (String) RedisUtil.get("bidSection_" + bidSectionId + "_queueStartTime_" + bidderId);
            mav.addObject("queueCount", CommonUtil.isEmpty(queueCount) ? 0 : queueCount);
            mav.addObject("queueTime", bidderModelService.getQueueTime(queueStartTime, DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS)));
        }
        //解密完成计算解密时间
        if (!CommonUtil.isEmpty(bidderOpenInfo.getDecryptStatus()) && bidderOpenInfo.getDecryptStatus() == 1) {
            mav.addObject("decryptTime", bidderModelService.getQueueTime(bidderOpenInfo.getDecryptStartTime(), bidderOpenInfo.getDecryptEndTime()));
        }

        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);

        BsnChainInfo lastBsnChainInfo = bsnChainInfoService.getLastBsnChainInfo(bidderId, BlockchainType.BIDDER_DECLASSIFIED_RECORDS);
        Boolean isUpChain = !CommonUtil.isEmpty(lastBsnChainInfo) && !CommonUtil.isEmpty(lastBsnChainInfo.getQueryAddress()) && bidderOpenInfo.getDecryptStatus() == 1;

        //获取当前标段总共参加解密的投标人，以及已经解密的人
        Map<String, Integer> data = bidderService.getDecryptNum(bidSectionId);

        mav.addObject("isUpChain", isUpChain);
        mav.addObject("lastBsnChainInfo", lastBsnChainInfo);
        mav.addObject("lineStatus", lineStatus);
        mav.addObject("decryptRedisStatus", decryptRedisStatus);
        mav.addObject("decryptNum", data);
        return mav;
    }


    /**
     * 投标文件解密(纸质标)
     * @param fileId 文件id
     * @param bidderId 投标人id
     * @param bidSectionId 标段id
     * @param fileType 文件类型（0：非证书加密，1：证书加密）
     * @param privateKey 私钥
     * @param isOtherCa 是否互认解密
     * @return
     */
    @RequestMapping("/paperDecrypt")
    @UserLog(value = "'投标文件解密: 文件id：'+#fileId+',投标人id：'+#bidderId+',标段id：'+#bidSectionId+',文件类型（0：非证书加密，1：证书加密）：'+#fileType+',私钥：'+#privateKey+',是否互认解密：'+#isOtherCa", dmlType = DMLType.UPDATE)
    public JsonData paperDecrypt(Integer fileId, Integer bidderId, Integer bidSectionId, String fileType, String privateKey, String isOtherCa) {
        JsonData result = new JsonData();
        try {
            bidderService.paperDecrypt(fileId, bidderId, bidSectionId, fileType, privateKey, isOtherCa);
            result.setCode(ExecuteCode.SUCCESS.getCode().toString());
            result.setMsg("文件解密中");
            //获取当前排队人数
            Integer queueCount = (Integer) RedisUtil.get("bidSection:" + bidSectionId);
            result.setData(CommonUtil.isEmpty(queueCount) ? 0 : queueCount);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ExecuteCode.FAIL.getCode().toString());
            result.setMsg(e.getMessage());
        }
        return result;
    }


    /**
     * 手机证书解密投标文件
     *
     * @param fileId   文件id
     * @param bidderId 投标人id
     * @param bidSectionId 标段id
     * @param phoneCertNo 手机证书号
     * @return
     */
    @RequestMapping("/phoneDecrypt")
    @UserLog(value = "'手机证书解密投标文件: 文件id：'+#fileId+',投标人id：'+#bidderId+',标段id：'+#bidSectionId+',手机证书号：'+#phoneCertNo", dmlType = DMLType.UPDATE)
    public JsonData phoneDecrypt(Integer fileId, Integer bidderId, Integer bidSectionId, String phoneCertNo) {
        JsonData result = new JsonData();
        try {
            bidderService.phoneDecrypt(fileId, bidderId, bidSectionId, phoneCertNo);
            result.setCode(ExecuteCode.SUCCESS.getCode().toString());
            result.setMsg("文件解密中");
            //获取当前排队人数
            Integer queueCount = (Integer) RedisUtil.get("bidSection:" + bidSectionId);
            result.setData(CommonUtil.isEmpty(queueCount) ? 0 : queueCount);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ExecuteCode.FAIL.getCode().toString());
            result.setMsg(e.getMessage());
        }
        return result;
    }
}
