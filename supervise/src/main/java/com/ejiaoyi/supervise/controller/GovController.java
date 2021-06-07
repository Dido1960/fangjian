package com.ejiaoyi.supervise.controller;

import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.BidFileConstant;
import com.ejiaoyi.common.constant.LiveBroadCastConstant;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.constant.TenderFileConstant;
import com.ejiaoyi.common.dto.BidOpenFlowSituationDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.*;
import com.ejiaoyi.supervise.service.IGovService;
import com.ejiaoyi.supervise.support.AuthUser;
import com.ejiaoyi.supervise.support.CurrentUserHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 主管部门前端控制器
 *
 * @author Make
 * @date 2020/8/27 15:24
 */
@RestController
@RequestMapping("/gov")
public class GovController extends BaseController {

    /**
     * anyChat音视频通讯插件 配置所需ip
     */
    @Value("${remote-eval.moniter.ip}")
    private String ip;

    /**
     * anyChat音视频通讯插件 配置所需端口port
     */
    @Value("${remote-eval.moniter.port}")
    private String port;

    @Autowired
    private IRegService regService;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private IBidSectionRelateService bidSectionRelateService;

    @Autowired
    private ISiteService siteService;

    @Autowired
    private IMonitorService monitorService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private ITenderProjectService tenderProjectService;

    @Autowired
    private IGovService govService;

    @Autowired
    private ILineMsgService lineMsgService;

    @Autowired
    private ITenderProjectService projectService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private LatiLongitudeService latiLongitudeService;

    /**
     * 设置用户操作的bidsetionId
     *
     * @param bidSectionId
     * @return
     */
    @PostMapping("/setBidSectionId")
    public void setBidSectionId(Integer bidSectionId) {
        CurrentUserHolder.getUser().setBidSectionId(bidSectionId);
    }

    /**
     * 远程异地评标
     * 请求AnyChat监控室界面
     *
     * @return
     */
    @RequestMapping("/anyChatScreen")
    public ModelAndView anyChatScreen() {
        ModelAndView mav = new ModelAndView();

        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        boolean eval = !CommonUtil.isEmpty(bidSection.getBidOpenStatus()) && Status.END.getCode().equals(bidSection.getBidOpenStatus());
        if (eval) {
            // 展示开标室监控
            // 主场信息
            Reg homeReg = regService.getRegById(bidSectionRelate.getRegId());
            Site homeSite = siteService.getSiteById(bidSectionRelate.getHomeEvalSite());

            // 客场信息
            Reg awayReg = regService.getRegById(bidSectionRelate.getAwayRegId());
            Site awaySite = siteService.getSiteById(bidSectionRelate.getAwayEvalSite());

            mav.setViewName("/anyChat/evalMonitor");
            mav.addObject("bidSection", bidSection);
            mav.addObject("ip", ip);
            mav.addObject("port", port);
            mav.addObject("homeReg", homeReg);
            mav.addObject("homeSite", homeSite);
            mav.addObject("awayReg", awayReg);
            mav.addObject("awaySite", awaySite);
        } else {
            // 未开始评标，展示开标室监控
            Reg homeReg = regService.getRegById(bidSection.getRegId());
            Site homeOpenSite = siteService.getSiteById(bidSectionRelate.getHomeOpenSite());

            mav.setViewName("/anyChat/openBidMonitor");
            mav.addObject("bidSection", bidSection);
            mav.addObject("homeReg", homeReg);
            mav.addObject("homeOpenSite", homeOpenSite);
        }

        return mav;
    }

    @RequestMapping("/checkAnyChatData")
    public JsonData checkAnyChatData(){
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        JsonData result = new JsonData();
        result.setCode("1");
        if (CommonUtil.isEmpty(bidSectionRelate.getRegId())
                || CommonUtil.isEmpty(bidSectionRelate.getHomeEvalSite())
                || CommonUtil.isEmpty(bidSectionRelate.getAwayRegId())
                || CommonUtil.isEmpty(bidSectionRelate.getAwayEvalSite())){
            result.setCode("2");
            result.setMsg("未配置主客场信息！");
        }
        return result;
    }

    /**
     * 获取大华监控登录信息
     *
     * @param regCode 行政区划代码
     * @return 监控信息
     */
    @RequestMapping("/getDHLoginInfo")
    public Monitor getDHLoginInfo(String regCode) {
        return monitorService.getMonitorByRegCode(regCode);
    }

    /**
     * 解析大华SDK组织结构信息
     * @param orgInfoXml 组织结构信息xml
     * @param roomName 房间名
     * @return
     */
    @RequestMapping("/parseDpSdkOrgXml")
    public Map<String, List<Object>> parseDpSdkOrgXml(String orgInfoXml, String roomName) {
        orgInfoXml = StringEscapeUtils.unescapeHtml4(orgInfoXml);
        return DpSdkXmlUtil.parseDpSdkOrgXml(orgInfoXml, roomName);
    }

    /**
     * 远程异地评标
     * 请求AnyChat监控室界面
     *
     * @param bidSectionId 标段编号
     * @return
     */
    @RequestMapping("/expertEval")
    public ModelAndView expertEval(Integer bidSectionId) {
        ModelAndView mav = new ModelAndView();
        Random random = new Random();
        int i = random.nextInt(3);
        int isChairMan = 0;
        if (i == 1) {
            isChairMan=1;
        }
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        mav.setViewName("/anyChat/expertEval");
        mav.addObject("bidSection", bidSection);
        mav.addObject("expertName", "专家" + i);
        mav.addObject("chairMan", isChairMan);
        mav.addObject("ip", ip);
        mav.addObject("port", port);

        return mav;
    }

    /**
     * 远程异地评标
     * 请求AnyChat监控室界面
     *
     * @param bidSectionId 标段编号
     * @return
     */
    @RequestMapping("/expert")
    public ModelAndView expert(Integer bidSectionId) {
        ModelAndView mav = new ModelAndView();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        mav.setViewName("/anyChat/expert");
        mav.addObject("bidSection", bidSection);

        return mav;
    }

    /**
     * 消息导出
     *
     * @return
     */
    @RequestMapping("/msgExport")
    public void msgExport(HttpServletResponse response) {
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        OutputStream out = null;
        FileInputStream in = null;
        String uuid = UUID.randomUUID().toString();
        String ftlPath = FileUtil.getProjectResourcePath() + File.separator + "ftl" + File.separator + "msgExport" + File.separator + "msgExport.ftl";
        String outPdfPath = FileUtil.getEvalReportFilePath(String.valueOf(bidSectionId)) + File.separator + uuid + ".pdf";

        try {
            Map<String, Object> data = new HashMap<>();
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            List<LineMsg> lineMsgs = lineMsgService.listLineMsg(bidSectionId);
            data.put("bidSection", bidSection);
            data.put("lineMsgs", lineMsgs);

            PDFUtil.generatePdf(ftlPath, outPdfPath, data, false);
            response.setContentType("application/pdf");
            in = new FileInputStream(new File(outPdfPath));
            out = response.getOutputStream();
            byte[] b = new byte[1024];
            while ((in.read(b)) != -1) {
                out.write(b);
            }
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                FileUtil.deleteFile(outPdfPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 进入项目信息页面
     *
     * @return
     */
    @RequestMapping("/projectInfoPage")
    public ModelAndView projectInfoPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/projectInfo/projectInfo");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        String bidOpenTotalTime;
        String bidEvalTotalTime;
        if (StringUtils.isNotEmpty(bidSection.getBidOpenEndTime())) {
            long openTimeDiff = DateTimeUtil.getTimeDiff(tenderDoc.getBidOpenTime(), bidSection.getBidOpenEndTime(),
                    TimeUnit.MILLISECONDS, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
            bidOpenTotalTime = DateTimeUtil.parseTime(openTimeDiff);
        } else {
            bidOpenTotalTime = "开标未结束";
        }

        // 评标总用时
        if (StringUtils.isNotEmpty(bidSection.getEvalStartTime())
                && StringUtils.isNotEmpty(bidSection.getEvalEndTime())) {
            long evalTimeDiff = DateTimeUtil.getTimeDiff(bidSection.getEvalStartTime(), bidSection.getEvalEndTime(),
                    TimeUnit.MILLISECONDS, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
            bidEvalTotalTime = DateTimeUtil.parseTime(evalTimeDiff);
        } else {
            bidEvalTotalTime = "评标未结束";
        }
        List<Bidder> bidders = govService.listBiddersWithVeto(bidSectionId);
        mav.addObject("bidOpenTotalTime", bidOpenTotalTime);
        mav.addObject("bidEvalTotalTime", bidEvalTotalTime);
        mav.addObject("user", user);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("tenderProject", tenderProject);
        mav.addObject("bidders", bidders);
        return mav;
    }

    /**
     * 进入开标流程页面
     *
     * @return
     */
    @RequestMapping("/baseBidOpenPage")
    public ModelAndView baseBidOpenPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidOpening/baseBidOpen");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        mav.addObject("user", user);
        mav.addObject("bidSection", bidSection);
        return mav;
    }

    /**
     * 查看所有投标人的标书
     * @return
     */
    @RequestMapping("/viewAllBidderPdf")
    public ModelAndView viewAllBidderPdf() {
        ModelAndView mav = new ModelAndView("/progressOfBid/projectInfo/viewAllBidderPdf");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);

        mav.addObject("tenderFile", TenderFileConstant.TECHNICAL_FILE);
        mav.addObject("bidFile", BidFileConstant.TENDER_DOC);
        mav.addObject("bidders", bidders);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 获取投标文件工程量清单的url
     *
     * @return
     */
    @RequestMapping("/getListUrl")
    public Map<String ,String > getListUrl(Integer bidderId) {
        String mark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidderId + BidFileConstant.ENGINEER_QUANTITY_LIST_XML;
        Map<String ,String > data = new HashMap<>();
        data.put("url",fdfsService.getUrlByMark(mark));
        return data;
    }

    /**
     * 标书下载
     *
     * @param response http相应请求
     * @param bidderId 投标人id
     * @throws IOException
     */
    @RequestMapping("/downloadTender")
    @UserLog(value = "'文件下载'", dmlType = DMLType.DOWNLOAD)
    public void downloadTender(HttpServletResponse response, Integer bidderId, Integer envelope) throws IOException {
        //投标人投标文件mark
        String tenderMark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidderId + TenderFileConstant.BUSINESS_FILE;
        OutputStream out = response.getOutputStream();
        String url = fdfsService.getUrlByMark(tenderMark);
        out.write(fdfsService.downloadByUrl(url));

    }

    /**
     * 获取当前标段的开标流程的完成情况
     *
     * @return
     */
    @RequestMapping("/listBidOpenFlowSituation")
    public List<BidOpenFlowSituationDTO> listBidOpenFlowSituation() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        return govService.listBidOpenFlowSituation(bidSectionId);
    }


    /**
     * 展示pdf合成进度
     * @return
     */
    @RequestMapping("/showSysInfoPage")
    public ModelAndView showSysInfoPage(){
        return new ModelAndView("/common/showSysInfo");
    }

    /**
     * 直播页面
     *
     * @return
     */
    @RequestMapping("/toLivePage")
    public ModelAndView toLivePage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/live/live");
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
     * 获取当前评标环节
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping(value = "/getNowEvalFlow", method = {RequestMethod.POST}, produces = {"text/plain;charset=UTF-8"})
    public String getNowEvalFlow(Integer bidSectionId) {
        return govService.getNowEvalFlow(bidSectionId);
    }

    /**
     * 获取当前标段
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/getBidSectionById")
    public BidSection getBidSectionById(Integer bidSectionId) {
        return bidSectionService.getBidSectionById(bidSectionId);
    }
}
