package com.ejiaoyi.agency.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.*;
import com.ejiaoyi.common.dto.*;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.*;
import com.ejiaoyi.agency.service.IStaffService;
import com.ejiaoyi.agency.support.AuthUser;
import com.ejiaoyi.agency.support.CurrentUserHolder;
import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 代理机构（招标人）前端控制器
 *
 * @author Make
 * @date 2020/7/6 14:24
 */
@RestController
@RequestMapping("/staff")
public class StaffController extends BaseController {

    /**
     * 文件预览服务器地址
     */
    @Value("${file.view.address}")
    private String fileViewAddress;

    @Autowired
    private IStaffService staffService;

    @Autowired
    private IRegService regService;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private ITenderProjectService tenderProjectService;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private ILineStatusService lineStatusService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IClarifyAnswerService clarifyAnswerService;

    @Autowired
    private IBidderFileInfoService bidderFileInfoService;

    @Autowired
    private ILineMsgService lineMsgService;

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private IBidSectionRelateService bidSectionRelateService;

    @Autowired
    private IBidderFileUploadService bidderFileUploadService;



    private final static long ONE_DAY_SECOND = 3600 * 24;

    /**
     * 跳转到添加项目页面选择页面
     * <p>
     * return
     */
    @RequestMapping("/chooseTypePage")
    public ModelAndView chooseTypePage() {
        return new ModelAndView("/addProject/chooseTypePage");
    }

    /**
     * 跳转到招标文件添加项目页面
     * <p>
     * return
     */
    @RequestMapping("/addProjectByGefPage")
    public ModelAndView addProjectByGefPage() {
        ModelAndView mav = new ModelAndView("/addProject/addProjectByGEF");
        mav.addObject("regs", JSON.toJSONString(regService.listReg(null)));
        return mav;
    }

    /**
     * 新增项目
     *
     * @param bidFileId        上传的招标文件ID
     * @param clarifyFileId    上传的澄清文件ID
     * @param regId            项目归属地
     * @param openBidOnline    网上开标标志
     * @param remoteEvaluation 远程异地评标标志
     * @param bidOpenTime      开标时间
     * @return
     */
    @RequestMapping("/parseProjectInfo")
    @UserLog(value = "'解析招标文件信息: 招标文件上传id：'+#bidFileId+'澄清文件上传id：'+#clarifyFileId+'区划id：'+#regId+'网上开标状态：'+#openBidOnline+'远程异地评标标志：'+#remoteEvaluation+'开标时间：'+#bidOpenTime", dmlType = DMLType.UNKNOWN)
    public JsonData parseProjectInfo(HttpServletRequest request, Integer bidFileId, String clarifyFileId, Integer regId, Integer openBidOnline, Integer remoteEvaluation, String bidOpenTime, String bidDocReferEndTime) {
        JsonData result = new JsonData();
        try {
            Map<String, Object> map = staffService.parseProjectInfo(bidFileId, clarifyFileId, regId, openBidOnline, remoteEvaluation, bidOpenTime, bidDocReferEndTime);
            request.getSession().setAttribute("showMapInfo", map);
            result.setCode(ExecuteCode.SUCCESS.getCode().toString());
            result.setMsg("项目解析成功,请确认招标信息！");
            result.setData(map);
        } catch (CustomException ce) {
            result.setCode(ExecuteCode.FAIL.getCode().toString());
            result.setMsg(ce.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ExecuteCode.FAIL.getCode().toString());
            result.setMsg("招标未解析成功, 项目新增失败!");
        } finally {
            // 文件解析完成，删除本地缓存文件
            /*String customFilePath = FileUtil.getDecryptTenderDocPath(bidFileId.toString());
            FileUtil.removeDir(new File(customFilePath));*/
        }
        return result;
    }

    /**
     * 跳转到确定项目信息页面
     *
     * @return
     */
    @RequestMapping("/confirmProjectInfoPage")
    public ModelAndView confirmProjectInfoPage(HttpServletRequest request, Integer bidSectionId, Integer flag) {
        Map map = (Map) request.getSession().getAttribute("showMapInfo");
        Project project = (Project) map.get("project");
        TenderProject tenderProject = (TenderProject) map.get("tenderProject");
        TenderDoc tenderDoc = (TenderDoc) map.get("tenderDoc");
        BidSection bidSection = (BidSection) map.get("bidSection");
        if (bidSectionId != null) {
            bidSection.setId(bidSectionId);
            TenderProject tp = tenderProjectService.getTenderProjectById(bidSectionService.getBidSectionById(bidSectionId).getTenderProjectId());
            tenderProject.setId(tp.getId());
            project.setId(tp.getProjectId());
            TenderDoc td = tenderDocService.getTenderDocBySectionId(bidSection.getId());
            tenderDoc.setId(td.getId());
        }

        Reg reg = (Reg) map.get("reg");
        Object obj = map.get("clarifyFileId");

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/addProject/confirmProjectInfoPage");

        if (obj != null) {
            String clarifyFileId = (String) obj;
            mav.addObject("clarifyFileId", clarifyFileId);
        }

        mav.addObject("project", project);
        mav.addObject("tenderProject", tenderProject);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("bidSection", bidSection);
        mav.addObject("reg", reg);
        mav.addObject("flag", flag);
        mav.addObject("regs", regService.listReg(null));

        return mav;
    }

    /**
     * 保存招标文件信息
     *
     * @param request
     * @param projectInfo 项目信息
     * @return
     */
    @RequestMapping("/saveProjectInfo")
    @UserLog(value = "'保存招标文件信息'", dmlType = DMLType.INSERT)
    public JsonData saveProjectInfo(HttpServletRequest request, @RequestBody ProjectInfoTemp projectInfo) {
        JsonData result = new JsonData();
        try {
            Map<String, Object> map = (Map<String, Object>) request.getSession().getAttribute("showMapInfo");
            CalcScoreParam calcScoreParam = (CalcScoreParam) map.get("calcScoreParam");
            projectInfo.setCalcScoreParam(calcScoreParam);
            boolean flag = staffService.saveProjectInfo(projectInfo);
            if (flag) {
                result.setCode(ExecuteCode.SUCCESS.getCode().toString());
                if (projectInfo.getBidSectionId() == null) {
                    result.setMsg("项目新增成功！");
                } else {
                    result.setMsg("项目修改成功！");
                }
            } else {
                result.setCode(ExecuteCode.FAIL.getCode().toString());
                if (projectInfo.getBidSectionId() == null) {
                    result.setMsg("项目新增失败！");
                } else {
                    result.setMsg("项目修改失败！");
                }
            }
            // 删除session
            request.getSession().removeAttribute("showMapInfo");
        } catch (CustomException e) {
            e.printStackTrace();
            result.setCode(ExecuteCode.FAIL.getCode().toString());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ExecuteCode.FAIL.getCode().toString());
            if (projectInfo.getBidSectionId() == null) {
                result.setMsg("项目新增失败！");
            } else {
                result.setMsg("项目修改失败！");
            }
        }
        return result;
    }

    /**
     * 跳转到确定项目信息页面
     *
     * @return
     */
    @RequestMapping("/createPaperProjectPage")
    public ModelAndView createPaperProjectPage() {
        AuthUser user = CurrentUserHolder.getUser();

        ModelAndView mav = new ModelAndView();
        mav.setViewName("/addProject/createPaperProjectPage");
        mav.addObject("regs", JSON.toJSONString(regService.listReg(null)));
        mav.addObject("user", user);

        return mav;
    }

    /**
     * 进入开标
     *
     * @return
     */
    @RequestMapping("/joinBidOpen")
    public ModelAndView joinBidOpen() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView mav = new ModelAndView();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());

        // 比较当前时间小，判断是否到达开标时间
        // 未到开标时间，跳转开标倒计时，到了开标时间则进入开标界面进行开标
        String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        String bidOpenTime = tenderDoc.getBidOpenTime();
        long countDownTime = DateTimeUtil.getTimeDiff(nowTime, bidOpenTime, TimeUnit.SECONDS, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        if (countDownTime > ONE_DAY_SECOND) {
            mav.setViewName("redirect:/index");
            this.setSystemActionMessage("距开标时间超过24小时，无法进入开标倒计时！", WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
            return mav;
        }

        boolean openBid = DateTimeUtil.compareDate(bidOpenTime, nowTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) == -1;
        if (openBid && StringUtil.isNotEmpty(bidSection.getBidClassifyCode())) {
            mav.setViewName("redirect:/staff/basePage");
            return mav;
        }
        mav.addObject("user", user);
        mav.setViewName("/openBid/openBidCountDown");
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("tenderProject", tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId()));
        mav.addObject("countDownTime", countDownTime);
        mav.addObject("openBid", openBid);
        return mav;
    }

    /**
     * 跳转到开标模板页面
     *
     * @return
     */
    @RequestMapping("/basePage")
    public ModelAndView basePage() {
        ModelAndView mav = new ModelAndView();
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        if (Status.NOT_START.getCode().equals(bidSection.getBidOpenStatus())) {
            bidSectionService.updateBidSectionById(BidSection.builder()
                    .id(bidSectionId)
                    .bidOpenStatus(Status.PROCESSING.getCode())
                    .build());
        }
        //是否是资格预审
        boolean isQualification = BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode());
        lineStatusService.updateFileUploadOrsigninStatus(bidSectionId);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);

        if (Enabled.YES.getCode().equals(bidSection.getBidOpenOnline())) {
            mav.setViewName("/openBid/basePage");
        } else {
            mav.setViewName("/siteOpenBid/baseSitePage");
        }


        mav.addObject("bidSection", bidSection);
        mav.addObject("lineStatus", lineStatus);
        mav.addObject("isQualification", isQualification ? "1" : "");
        mav.addObject("user", CurrentUserHolder.getUser());
        return mav;
    }

    /**
     * 跳转到投标人解密页面
     *
     * @param id 标段id
     * @return
     */
    @RequestMapping("/bidderFileDecryptPage")
    public ModelAndView bidderFileDecryptPage(Integer id) {
        ModelAndView mav = new ModelAndView("/openBid/bidderFile/bidderFileDecryptPage");
        // 等待开标的项目
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        // 招标文件内容
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());
        String lastDecryptStartTime = null;
        // 网上开标状态
        lineStatusService.updateFileUploadOrsigninStatus(id);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(id);
        String decryptionPeriods = lineStatus.getDecryptionPeriods();
        if (!CommonUtil.isEmpty(decryptionPeriods)) {
            JSONArray jArray = JSONArray.parseArray(decryptionPeriods);
            JSONObject jObject = jArray.getJSONObject(jArray.size() - 1);
            lastDecryptStartTime = (String) jObject.get("startTime");
        }
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("lineStatus", lineStatus);
        mav.addObject("lastDecryptStartTime", lastDecryptStartTime);
        mav.addObject("nowTime", DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));
        return mav;
    }

    /**
     * 修改项目页面
     *
     * @return
     */
    @RequestMapping("/modifyProjectPage")
    public ModelAndView modifyProjectPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/addProject/addProjectByGEF");
        List<Reg> regs = regService.listReg(null);
        mav.addObject("regs", JSON.toJSONString(regs));
        return mav;
    }

    /**
     * 检验是否到达开标时间
     *
     * @param id 标段编号
     * @return
     */
    @RequestMapping("/checkModify")
    public JsonData checkModify(Integer id) {
        JsonData jsonData = new JsonData();
        if (staffService.checkModify(id)) {
            jsonData.setCode(ExecuteCode.SUCCESS.getCode().toString());
        } else {
            jsonData.setCode(ExecuteCode.FAIL.getCode().toString());
            jsonData.setMsg("开标时间已到，无法修改项目信息");
        }
        return jsonData;
    }

    @RequestMapping("/editProject")
    public ModelAndView modelAndView(Integer bidSectionId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/addProject/editProject");
        List<Reg> regs = regService.listReg(null);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        mav.addObject("regs", JSON.toJSONString(regs));
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    @RequestMapping("/editProjectTime")
    public JsonData editProjectTime(TenderDoc tenderDoc) {
        boolean b = tenderDocService.updateTenderDocById(tenderDoc);
        JsonData result = new JsonData();
        if (b) {
            result.setCode("2");
            result.setMsg("修改成功");
        } else {
            result.setCode("0");
            result.setMsg("修改失败");
        }
        return result;
    }

    /**
     * 修改招标文件信息
     *
     * @param tenderDoc
     * @return
     */
    @RequestMapping("/updateTenderDoc")
    public boolean updateTenderDoc(TenderDoc tenderDoc) {
        // 获取当前标段下，所有语音
        List<Fdfs> fdfs = fdfsService.listFdfsByMark(ProjectFileTypeConstant.VOICE + "/" + tenderDoc.getBidSectionId() + "/");
        for (Fdfs fdf : fdfs) {
            //删除唱标语音
            fdfsService.deleteByMark(fdf.getMark());
        }
        return tenderDocService.updateTenderDoc(tenderDoc);
    }

    /**
     * 通过id获取当前标段的开标流程的完成情况
     *
     * @param id
     * @return
     */
    @RequestMapping("/listBidOpenProcessComplete")
    public List<String> listBidOpenProcessComplete(Integer id) {
        return staffService.listBidOpenProcessComplete(id);
    }


    /**
     * 跳转到添加控制价
     *
     * @return
     */
    @RequestMapping("/addControlPricePage")
    public ModelAndView addControlPricePage(Integer id) {
        ModelAndView mav = new ModelAndView("/openBid/controlPrice/addControlPrice");
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 跳转施工总承包最高投标限价页面
     *
     * @return
     */
    @RequestMapping("/maxBidPricePage")
    public ModelAndView maxBidPricePage(Integer id) {
        ModelAndView mav = new ModelAndView("/openBid/controlPrice/maxBidPrice");
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 跳转到浮动点抽取页面
     *
     * @return
     */
    @RequestMapping("/floatPointPage")
    public ModelAndView floatPointPage(Integer id) {
        ModelAndView mav = new ModelAndView("/openBid/floatPoint/floatPoint");

        // 等待开标的项目
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());

        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 获取可以参与解密的投标人信息
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/listBiddersForDecrypt")
    public JsonData listBiddersForDecrypt(Integer bidSectionId) {
        JsonData jsonData = new JsonData();
        try {
            // 参与解密的投标人
            List<Bidder> bidders = bidderService.listBiddersForDecrypt(bidSectionId);
            // 当前排队人数
            Integer queueNum = (Integer) RedisUtil.get("bidSection:" + bidSectionId);
            // 解密中的人数
            Integer decryptingNum = (Integer) RedisUtil.get("decrypting:" + bidSectionId);

            Map<String, Object> data = new HashMap<>(3);

            data.put("bidders", bidders);
            data.put("queueCount", queueNum == null ? 0 : queueNum);
            data.put("decryptingCount", decryptingNum == null ? 0 : decryptingNum);

            jsonData.setCode(ExecuteCode.SUCCESS.getCode().toString());
            jsonData.setMsg(bidders.size() == 0 ? "暂无投标人信息" : "");
            jsonData.setData(data);

        } catch (Exception e) {
            e.printStackTrace();
            jsonData.setCode(ExecuteCode.FAIL.getCode().toString());
            jsonData.setMsg(e.getMessage());
        }
        return jsonData;
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
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderOpenInfo.getBidderId());
        bidderFileInfoService.updateById(BidderFileInfo.builder()
                .id(bidderFileInfo.getId())
                .bidderId(bidderFileInfo.getBidderId())
                .build());
        return bidderOpenInfoService.updateBidderOpenInfo(bidderOpenInfo);
    }

    /**
     * 获取投标人开标信息
     *
     * @param bidderOpenInfo 开标信息
     * @return
     */
    @RequestMapping("/getBidderOpenInfo")
    public BidderOpenInfo getBidderOpenInfo(BidderOpenInfo bidderOpenInfo) {
        return bidderOpenInfoService.getBidderOpenInfo(bidderOpenInfo);
    }

    /**
     * 跳转到公布投标人名单页面
     *
     * @return
     */
    @RequestMapping("/publishBidderPage/{fw}")
    public ModelAndView bidderListPage(@PathVariable("fw") Integer fw, Integer id) {
        ModelAndView mav = new ModelAndView("/openBid/publishBidders/endCheck");
        List<Bidder> allBidders = bidderService.listAllBidders(id, false);
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        lineStatusService.updateFileUploadOrsigninStatus(id);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(id);
        // 判断公布投标人状态
        Integer publishBidderStatus = lineStatus.getPublishBidderStatus();
        if (CommonUtil.isEmpty(publishBidderStatus) || PublishBiddersState.RECHECK.equals(publishBidderStatus)) {
            mav.setViewName("/openBid/publishBidders/recheck");
        }
        // 初始化投标人签到情况
        for (Bidder bidder : allBidders) {
            BidderOpenInfo boi = new BidderOpenInfo();
            if (bidder.getBidderOpenInfo().getNotCheckin() == null) {
                boi.setId(bidder.getBidderOpenInfo().getId());
                boi.setNotCheckin(NotCheckinStatus.DEFAULT_ST);
                bidder.getBidderOpenInfo().setNotCheckin(NotCheckinStatus.DEFAULT_ST);
                // 默认 已上传、已签到 --> 已递交
                if (!CommonUtil.isEmpty(bidder.getBidDocId()) && !CommonUtil.isEmpty(bidder.getBidderOpenInfo().getSigninTime())) {
                    boi.setNotCheckin(NotCheckinStatus.SUBMIT);
                    bidder.getBidderOpenInfo().setNotCheckin(NotCheckinStatus.SUBMIT);
                }
                // 默认 未上传、未签到 --> 未递交
                if (CommonUtil.isEmpty(bidder.getBidDocId()) && CommonUtil.isEmpty(bidder.getBidderOpenInfo().getSigninTime())) {
                    boi.setNotCheckin(NotCheckinStatus.OTHER_ST);
                    bidder.getBidderOpenInfo().setNotCheckin(NotCheckinStatus.OTHER_ST);
                    bidderService.updateBiddersIsPassBidOpen(bidder.getId(), 0);
                }
                bidderOpenInfoService.updateBidderOpenInfoById(boi);
            }
        }
        mav.addObject("bidSection", bidSection);
        mav.addObject("allBidders", allBidders);
        mav.addObject("bidderCount", allBidders.size());
        return mav;
    }

    /**
     * 跳转到未递交原因页面
     *
     * @return
     */
    @RequestMapping("/unPushPage")
    public ModelAndView unPushPage(Integer boiId) {
        return new ModelAndView("/openBid/publishBidders/unPushPage", "boiId", boiId);
    }

    /**
     * 跳转到控制价分析页面
     *
     * @return
     */
    @RequestMapping("/controlPriceAnalysisPage")
    public ModelAndView controlPriceAnalysisPage(Integer id) {
        ModelAndView mav = new ModelAndView("/openBid/controlPrice/controlPriceAnalysisPage");
        mav.addObject("tenderDoc", tenderDocService.getTenderDocBySectionId(id));
        mav.addObject("bidSection", bidSectionService.getBidSectionById(id));
        return mav;
    }

    /**
     * 跳转开标记录表页面
     *
     * @return
     */
    @RequestMapping("/bidOpenRecordPage/{flag}")
    public ModelAndView bidOpenRecordPage(Integer id, @PathVariable("flag") Integer flag) {
        ModelAndView mav = new ModelAndView();
        try {
            //判断是否已生成开标记录表
            String mark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + id +
                    File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + "." + FileType.PDF.getSuffix();
            if (flag == 2) {
                //修改开标记录表
                fdfsService.deleteByMark(mark);
                mav.addObject("flag", flag);
            } else {
                Fdfs fdfs = fdfsService.getFdfsByMark(mark);
                //加载开标记录表pdf
                if (fdfs != null) {
                    AuthUser authUser = CurrentUserHolder.getUser();
                    mav.setViewName("/openBid/recordTable/showBidOpenRecord");
                    mav.addObject("mark", mark);
                    mav.addObject("fdfs", fdfs);
                    mav.addObject("user", authUser);
                    return mav;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.setSystemActionMessage("开标记录表加载失败，请重新生成！",
                    WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
        }
        Map<String, Object> data = staffService.getBidOpenRecordData(id, null);
        BidSection bidSection = (BidSection) data.get("bidSection");
        String bidClassifyCode = bidSection.getBidClassifyCode();
        String template = BidProtype.getTemplate(bidClassifyCode);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(id);

        mav.setViewName("/openBid/recordTable/" + template);
        mav.addObject("bidSection", data.get("bidSection"));
        mav.addObject("tenderProject", data.get("tenderProject"));
        mav.addObject("tenderDoc", data.get("tenderDoc"));
        mav.addObject("bidders", data.get("bidders"));
        mav.addObject("lineStatus", lineStatus);
        return mav;
    }

    /**
     * 文件数据页面
     *
     * @param id 标段ID
     * @return
     */
    @RequestMapping("/bidFileUploadPage")
    public ModelAndView bidFileUploadPage(Integer id) {
        ModelAndView mav = new ModelAndView("/openBid/bidFileUpload/bidFileUpload");
        BidSection bidSection = bidSectionService.getBidSectionById(id);

        BidderFileUploadDTO data = bidderFileInfoService.listBidderFileUpload(id);

        mav.addObject("data", data);
        mav.addObject("bidSection", bidSection);
        return mav;
    }

    /**
     * 纸质标 文件数据页面
     *
     * @param id 标段ID
     * @return
     */
    @RequestMapping("/paperBidFileUploadPage")
    public ModelAndView paperBidFileUploadPage(Integer id) {
        ModelAndView mav = new ModelAndView("/openBid/bidFileUpload/paperBidFileUpload");
        BidSection bidSection = bidSectionService.getBidSectionById(id);

        BidderFileUploadDTO data = bidderFileInfoService.listBidderFileUpload(id);

        mav.addObject("data", data);
        mav.addObject("bidSection", bidSection);
        return mav;
    }

    /**
     * 获取上传状态
     *
     * @param bidSectionId 标段ID
     * @return
     */
    @RequestMapping("/getBidderFileUploadData")
    public BidderFileUploadDTO getBidderFileUploadData(Integer bidSectionId) {
        return bidderFileInfoService.listBidderFileUpload(bidSectionId);
    }

    /**
     * 文件重新上传
     *
     * @param bidderId 投标人id
     * @param fileType 需要上传的文件类型
     */
    @RequestMapping("/bidderFileReUpload")
    public Boolean bidderFileReUpload(Integer bidderId, Integer fileType) {
        return bidderFileUploadService.bidderFileReUpload(bidderId, fileType);
    }

    @RequestMapping("/updateAllStatus")
    public boolean updateAllStatus(BidderFileInfo bidderFileInfo) {
        return bidderFileInfoService.updateById(bidderFileInfo) == 1;
    }

    /**
     * 纸质标 文件重新上传
     *
     * @param bidderId 投标人id
     * @param fileType 需要上传的文件类型
     */
    @RequestMapping("/paperBidderFileReUpload")
    public Boolean paperBidderFileReUpload(Integer bidderId, Integer fileType) {
        return bidderFileUploadService.paperBidderFileReUpload(bidderId, fileType);
    }

    /**
     * 开标结束页面
     *
     * @param id 标段id
     * @return
     */
    @RequestMapping("/bidOpenEndPage")
    public ModelAndView bidOpenEndPage(Integer id) {
        return new ModelAndView("/openBid/bidOpenEnd/bidOpenEndPage", "bidSection", bidSectionService.getBidSectionById(id));
    }

    /**
     * 开标结束弹窗页面
     *
     * @return 开标结束弹窗页面
     */
    @RequestMapping("/endSure")
    public ModelAndView bidOpenEndPage() {
        return new ModelAndView("/openBid/bidOpenEnd/endSure");
    }

    /**
     * 检查是否完成开标页面
     *
     * @param id 标段id
     * @return
     */
    @RequestMapping("/checkFinishBidOpenPage")
    public ModelAndView checkFinishBidOpenPage(Integer id) {
        return new ModelAndView("/openBid/bidOpenEnd/checkFinishBidOpenPage", "bidSection", bidSectionService.getBidSectionById(id));
    }

    /**
     * 生成开标记录表（pdf）
     */
    @RequestMapping("/createRecordTable")
    @UserLog(value = "'生成开标记录表'+#bidSectionId", dmlType = DMLType.INSERT)
    public boolean createRecordTable(Integer bidSectionId, String bidOpenPlace) {
        return staffService.createRecordTable(bidSectionId, bidOpenPlace);
    }

    /**
     * 跳转到唱标页面
     *
     * @return
     */
    @RequestMapping("/fileCursorPage")
    public ModelAndView fileCursorPage(Integer id) {
        ModelAndView mav = new ModelAndView("/openBid/fileCursor/fileCursorPage");
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(id);
        // 网上开标状态
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(id);
        String lastQuestionStartTime = null;
        // 网上开标状态
        String questionPeriods = lineStatus.getQuestionPeriods();
        if (!CommonUtil.isEmpty(questionPeriods) && Status.PROCESSING.getCode().equals(lineStatus.getQuestionStatus())) {
            JSONArray jArray = JSONArray.parseArray(questionPeriods);
            JSONObject jObject = jArray.getJSONObject(jArray.size() - 1);
            lastQuestionStartTime = (String) jObject.get("startTime");
        }

        // 加载招标项目、标段信息
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("lineStatus", lineStatus);
        mav.addObject("lastQuestionStartTime", lastQuestionStartTime);
        mav.addObject("nowTime", DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));
        return mav;
    }

    /**
     * 唱标（招标信息+所有投标人信息）
     *
     * @param bId        标段主键
     * @param tenderDto  招标(唱标内容)
     * @param listBidder 投标人对象列表(唱标内容)
     * @return
     */
    @RequestMapping("/tenderFileCursor")
    public JsonData tenderFileCursor(Integer bId, String tenderDto, String listBidder) {
        return staffService.tenderFileCursor(bId, tenderDto, listBidder);
    }

    /**
     * 唱标 （单个投标人语音）
     *
     * @param bidSectionId 标段主键
     * @param bidderDto    投标人DTO
     * @return
     */
    @RequestMapping("bidderFileCursor")
    public JsonData bidderFileCursor(Integer bidSectionId, String bidderDto) {
        return staffService.bidderFileCursor(bidSectionId, bidderDto);
    }

    /**
     * 设置复会时间
     *
     * @param id 标段id
     * @return
     */
    @RequestMapping("/setResumeTime")
    public ModelAndView setResumeTime(Integer id) {
        return new ModelAndView("/openBid/resume/setResumeTime", "bidSection", bidSectionService.getBidSectionById(id));
    }

    /**
     * 复会时间设置页面
     *
     * @param id 标段id
     * @return
     */
    @RequestMapping("/resumeTimePage")
    public ModelAndView resumeTimePage(Integer id) {
        return new ModelAndView("/openBid/resume/resumeTimePage", "bidSection", bidSectionService.getBidSectionById(id));
    }

    /**
     * 获取异常信息内容
     *
     * @param bidSectionId 标段id
     * @param step         异常环节
     * @return
     */
    @RequestMapping("/getExceptionMessage")
    public JsonData getExceptionMessage(Integer bidSectionId, String step) {
        JsonData jsonData = new JsonData();
        Map<String, Object> map = staffService.getExceptionMessage(bidSectionId, step);
        if (CommonUtil.isEmpty(map)) {
            jsonData.setCode(ExecuteCode.FAIL.getCode().toString());
        } else {
            jsonData.setCode(ExecuteCode.SUCCESS.getCode().toString());
            jsonData.setData(map);
        }
        return jsonData;
    }

    /**
     * 检查唱标服务是否启动
     *
     * @return
     */
    @RequestMapping("/checkVoicePlug")
    public boolean checkVoicePlug() {
        return staffService.checkVoicePlug();
    }

    /**
     * 当前标段语音是否存在
     *
     * @return
     */
    @RequestMapping("/thisBidVoiceExist")
    public boolean thisBidVoiceExist(int bidderId) {
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        return staffService.thisBidVoiceExist(bidSectionId, bidderId);
    }

    /**
     * 更新网上开标状态
     *
     * @param lineStatus 网上开标状态
     * @return
     */
    @RequestMapping("/updateLineStatus")
    public Boolean updateLineStatus(LineStatus lineStatus) {
        Boolean aBoolean = lineStatusService.updateLineStatus(lineStatus);
        if (aBoolean) {
            if (Status.PROCESSING.getCode().equals(lineStatus.getDecryptionStatus())) {
                messageService.setMessageToRedis(lineStatus.getBidSectionId(), lineStatus.getMsg(), UserType.SYSTEM, MessageType.DECRYPT);
            } else if (Status.PROCESSING.getCode().equals(lineStatus.getQuestionStatus())) {
                messageService.setMessageToRedis(lineStatus.getBidSectionId(), lineStatus.getMsg(), UserType.SYSTEM, MessageType.QUESTION);
            } else {
                messageService.setMessageToRedis(lineStatus.getBidSectionId(), lineStatus.getMsg(), UserType.SYSTEM, MessageType.OTHER);
            }
        }
        return aBoolean;
    }

    /**
     * 标书拒绝和文件未递交 修改投标人信息
     *
     * @param bidderOpenInfo 开标信息
     * @param isPassBidOpen  是否通过开标
     * @return
     */
    @RequestMapping("/updateBidderInfo")
    public boolean updateBidderInfo(BidderOpenInfo bidderOpenInfo, Integer isPassBidOpen) {
        return bidderService.updateBidderInfo(bidderOpenInfo, isPassBidOpen);
    }

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
     * 跳转文件下载页面
     *
     * @return
     */
    @RequestMapping("/downFiles")
    public ModelAndView downFiles() {
        ModelAndView modelAndView = new ModelAndView("/downFile/downFilePage");
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        Fdfs tenderPdf;
        if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
            tenderPdf = fdfsService.getFdfsByUpload(tenderDoc.getDocFileId());
        } else {
             //招标文件gef
            Fdfs tenderZjf = fdfsService.getFdfsByUpload(tenderDoc.getDocFileId());
            // 招标文件pdf
            String tenderPdfMark = File.separator + ProjectFileTypeConstant.TENDER_DOC + File.separator + tenderDoc.getDocFileId() + BidFileConstant.TENDER_DOC;
            tenderPdf = fdfsService.getFdfsByMark(tenderPdfMark);
            modelAndView.addObject("tenderZjf", tenderZjf);
        }

        if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
            // 招标工程量清单pdf
            String quantityPdfMark = File.separator + ProjectFileTypeConstant.TENDER_DOC + File.separator + tenderDoc.getDocFileId() + BidFileConstant.ENGINEER_QUANTITY_LIST_PDF;
            Fdfs quantityPdf = fdfsService.getFdfsByMark(quantityPdfMark);

            // 招标工程量清单xml
            String quantityXmlMark = File.separator + ProjectFileTypeConstant.TENDER_DOC + File.separator + tenderDoc.getDocFileId() + BidFileConstant.ENGINEER_QUANTITY_LIST_XML;
            Fdfs quantityXml = fdfsService.getFdfsByMark(quantityXmlMark);
            modelAndView.addObject("quantityPdf", quantityPdf);
            modelAndView.addObject("quantityXml", quantityXml);
        }

        // 澄清文件(展示最新的)
        ClarifyAnswer clarifyAnswer = null;
        List<ClarifyAnswer> clarifyAnswers = clarifyAnswerService.listClarifyAnswerBySectionId(bidSectionId);
        if (clarifyAnswers.size() >= 1) {
            clarifyAnswer = clarifyAnswers.get(0);
        }
        // 开标记录表
        String mark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + bidSectionId +
                File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + "." + FileType.PDF.getSuffix();
        Fdfs recordTable = fdfsService.getFdfsByMark(mark);
        // 下载的投标人文件
        List<DownBidderFileDTO> downBidderFileDTOS = null;
        if (BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode())) {
            //如果是资格预审 获取所有投标人的投标Pdf文件
            downBidderFileDTOS = bidderFileInfoService.listDownAllBiddersPdfFileDTO(bidSectionId);
        } else {
            //如果是非资格预审的 获取排名第一名的投标人的pdf文件
            BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
            switch (bidProtype){
                case CONSTRUCTION:
                    downBidderFileDTOS = bidderFileInfoService.listDownFirstBidderPdfFileDTO1(bidSectionId);
                    break;
                case SUPERVISION:
                    downBidderFileDTOS = bidderFileInfoService.listDownFirstBidderPdfFileDTO2(bidSectionId);
                    break;
                case EPC:
                    downBidderFileDTOS = bidderFileInfoService.listDownFirstBidderPdfFileDTO3(bidSectionId);
                    break;
                case DESIGN:
                case ELEVATOR:
                case INVESTIGATION:
                    downBidderFileDTOS = bidderFileInfoService.listDownFirstBidderPdfFileDTO4(bidSectionId);
                    break;
                default:
                    break;
            }
        }

        modelAndView.addObject("tenderPdf", tenderPdf);
        modelAndView.addObject("clarifyAnswer", clarifyAnswer);
        modelAndView.addObject("recordTable", recordTable);
        modelAndView.addObject("downBidderFileDTOS", downBidderFileDTOS);
        modelAndView.addObject("fileViewAddress", fileViewAddress);
        modelAndView.addObject("bidSection", bidSection);
        return modelAndView;
    }

    /**
     * 删除唱标语音
     *
     * @param id 标段id
     * @return
     */
    @RequestMapping("/removeSingingVoice")
    public boolean removeSingingVoice(Integer id) {
        return staffService.removeSingingVoice(id);
    }

    /**
     * 修改招标文件信息
     *
     * @param tenderDoc
     * @return
     */
    @RequestMapping("/updateTenderDocById")
    public boolean updateTenderDocById(TenderDoc tenderDoc) {
        return tenderDocService.updateTenderDocById(tenderDoc);
    }

    /**
     * 获取待唱标投标人列表
     *
     * @param bidSectionId 标段主键
     * @return
     */
    @RequestMapping("/listBidderBySing")
    public List<BidderOpenInfo> listBidderBySing(Integer bidSectionId) {
        List<Bidder> bidders = bidderService.listDecrySuccessBidder(bidSectionId, false);
        return bidderOpenInfoService.listBidderOpenInfo(bidders);
    }

    /**
     * 获取可以参与唱标的投标人列表
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/listBiddersForSing")
    public List<Bidder> listBiddersForSing(Integer bidSectionId) {
        return bidderService.listBiddersForSing(bidSectionId);
    }

    /**
     * 标书决绝理由页面
     */
    @RequestMapping("/bidRejectionPage")
    public ModelAndView bidRejectionPage(Integer flag, Integer bidSectionId, Integer bidderId) {
        ModelAndView mav = new ModelAndView("/openBid/bidderFile/reason");
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        mav.addObject("bidderOpenInfo", bidderOpenInfo);
        mav.addObject("flag", flag);
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
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                FileUtil.deleteFile(outPdfPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 跳转签到设置页面
     *
     * @return
     */
    @RequestMapping("/signSetPage")
    public ModelAndView signSetPage() {
        ModelAndView mav = new ModelAndView("/signSetPage");
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        mav.addObject("bidSection", bidSection);
        return mav;
    }


    /**
     * 保存澄清文件
     *
     * @param id            标段id
     * @param regId         区划id
     * @param clarifyFileId 澄清文件id
     * @return
     */
    @RequestMapping("/saveClarifyFile")
    public Map<String, Object> saveClarifyFile(Integer id, Integer regId, Integer clarifyFileId) {
        Map<String, Object> map = new HashMap<>();
        try {
            UploadFile uploadFile = uploadFileService.getUploadById(clarifyFileId);
            staffService.saveClarifyFileAndData(id, regId, uploadFile);
            map.put("s", true);
        } catch (CustomException e) {
            e.printStackTrace();
            map.put("s", false);
        }

        return map;
    }

    /**
     * 主页设置复会时间
     *
     * @param bidSectionId 标段主键
     * @return
     */
    @RequestMapping("/setMeetingTimePage")
    public ModelAndView setMeetingTimePage(Integer bidSectionId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/addProject/setMeetingTimePage");
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 修改标段信息
     *
     * @param bidSection 标段信息
     * @return
     */
    @RequestMapping("/updateResumeStatus")
    @UserLog(value = "'修改标段：'+#bidSection", dmlType = DMLType.UPDATE)
    public JsonData updateBidSection(BidSection bidSection) {
        // 当前时间
        Integer bidSectionId = bidSection.getId();
        BidSection newBidSection = BidSection.builder()
                .id(bidSectionId)
                .resumeStatus(bidSection.getResumeStatus())
                .build();
        int res = bidSectionService.updateBidSectionById(newBidSection);
        lineStatusService.updateLineStatus(LineStatus.builder()
                .bidSectionId(bidSectionId)
                .resumeStatus(bidSection.getResumeStatus())
                .msg("")
                .build());
        JsonData result = new JsonData();
        if (res > 0) {
            result.setCode("2");
            result.setMsg("修改成功");
        } else {
            result.setCode("0");
            result.setMsg("修改失败");
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
        ModelAndView mav = new ModelAndView("/meeting/index");
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);

        // 参与复会的投标人
        List<Bidder> bidders = bidderOpenInfoService.listBidderForMeeting(bidSectionId);

        // 获取复会报告
        BidSectionRelate relate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        if (!CommonUtil.isEmpty(relate.getResumptionReportId())) {
            Fdfs fdfs = fdfsService.getFdfdById(relate.getResumptionReportId());
            relate.setResumptionReportUrl(CommonUtil.isEmpty(fdfs) ? "" : fdfs.getUrl());
        }

        // 网上开标状态
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        String lastResumeStartTime = null;
        // 网上开标状态
        String resumePeriods = lineStatus.getResumePeriods();
        if (!CommonUtil.isEmpty(resumePeriods) && Status.PROCESSING.getCode().equals(lineStatus.getResumeStatus())) {
            JSONArray jArray = JSONArray.parseArray(resumePeriods);
            JSONObject jObject = jArray.getJSONObject(jArray.size() - 1);
            lastResumeStartTime = (String) jObject.get("startTime");
        }

        mav.addObject("bidSection", bidSection);
        mav.addObject("bidders", bidders);
        mav.addObject("relate", relate);

        mav.addObject("user", user);
        mav.addObject("tenderDoc", tenderDocService.getTenderDocBySectionId(bidSectionId));
        mav.addObject("tenderProject", tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId()));

        mav.addObject("lineStatus", lineStatus);
        mav.addObject("lastResumeStartTime", lastResumeStartTime);
        mav.addObject("nowTime", DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));
        return mav;
    }

    /**
     * 获取标段信息
     *
     * @return
     */
    @RequestMapping("/getBidSection")
    @UserLog(value = "'修改标段信息: bidSection='+#bidSection", dmlType = DMLType.UPDATE)
    public JsonData getBidSection() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        JsonData result = new JsonData();
        if (!CommonUtil.isEmpty(bidSection)) {
            result.setCode("2");
            result.setMsg("bidSection");
            result.setData(bidSection);
        } else {
            result.setCode("0");
            result.setMsg("获取标段信息失败");
        }
        return result;
    }

    /**
     * 参加复会投标人列表
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/listBiddersForMeeting")
    public JsonData listBiddersForMeeting(Integer bidSectionId) {
        JsonData jsonData = new JsonData();
        try {
            // 参与复会的投标人
            List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
            Map<String, Object> data = new HashMap<>(3);
            data.put("bidders", bidders);
            jsonData.setCode(ExecuteCode.SUCCESS.getCode().toString());
            jsonData.setMsg(bidders.size() == 0 ? "暂无投标人信息" : "");
            jsonData.setData(data);

        } catch (Exception e) {
            e.printStackTrace();
            jsonData.setCode(ExecuteCode.FAIL.getCode().toString());
            jsonData.setMsg(e.getMessage());
        }
        return jsonData;
    }

    /**
     * 文件预览页面
     *
     * @param url 文件url
     * @return
     */
    @RequestMapping("previewPDFPage")
    public ModelAndView previewPDFPage(String url) {
        ModelAndView mav = new ModelAndView("/viewPdf/showPDFViewByUrl");
        mav.addObject("pdfUrl", url);
        return mav;
    }

    @RequestMapping("/preRelatedPage")
    public ModelAndView preRelated(Integer bidSectionId) {
        ModelAndView mav = new ModelAndView("/preRelated/preRelated");
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);

        List<BidSection> list = bidSectionService.listBidSectionBySth(BidSection.builder()
                .tenderProjectId(bidSection.getTenderProjectId())
                .bidClassifyCode(BidProtype.QUALIFICATION.getCode())
                .regId(bidSection.getRegId())
                .deleteFlag(0).build());
        if (!CommonUtil.isEmpty(list)) {
            for (BidSection section : list) {
                section.setBidOpenTime(tenderDocService.getTenderDocBySectionId(section.getId()).getBidOpenTime());
            }
        }

        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);

        mav.addObject("bidSection", bidSection);
        mav.addObject("project", tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId()));
        mav.addObject("type", BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode()).getChineseName());
        mav.addObject("list", list);
        mav.addObject("bidSectionRelate", bidSectionRelate);
        return mav;
    }

    @RequestMapping("/preRelated")
    public Boolean preRelated(BidSectionRelate bidSectionRelate) {
        return bidSectionRelateService.updateById(bidSectionRelate);
    }

    @RequestMapping("/getMeetingBidderList")
    public List<Bidder> getMeetingBidderList() {
        AuthUser user = CurrentUserHolder.getUser();
        return bidderOpenInfoService.listBidderForMeeting(user.getBidSectionId());
    }

    /**
     * 判断投标人是否上传完成所有文件
     *
     * @return
     */
    @RequestMapping("/isBidderFileUploadAll")
    public Integer isBidderFileUploadAll() {
        AuthUser user = CurrentUserHolder.getUser();
        BidderFileUploadDTO bidderFileUploadDTO = bidderFileInfoService.listBidderFileUpload(user.getBidSectionId());
        if (!CommonUtil.isEmpty(bidderFileUploadDTO) && bidderFileUploadDTO.getBidderNum().equals(bidderFileUploadDTO.getUploadSuccessNum())) {
            return Enabled.YES.getCode();
        }
        return Enabled.NO.getCode();
    }


    /**
     * 保存纸质标招标文件信息
     *
     * @param request
     * @param projectInfo 项目信息
     * @return
     */
    @RequestMapping("/savePaperProjectInfo")
    @UserLog(value = "'保存纸质标招标文件信息'", dmlType = DMLType.INSERT)
    public JsonData savePaperProjectInfo(HttpServletRequest request, @RequestBody ProjectInfoTemp projectInfo) {
        JsonData result = new JsonData();
        try {
            boolean flag = staffService.savePaperProjectInfo(projectInfo);
            if (flag) {
                result.setCode(ExecuteCode.SUCCESS.getCode().toString());
                if (projectInfo.getBidSectionId() == null) {
                    result.setMsg("项目新增成功！");
                } else {
                    result.setMsg("项目修改成功！");
                }
            } else {
                result.setCode(ExecuteCode.FAIL.getCode().toString());
                if (projectInfo.getBidSectionId() == null) {
                    result.setMsg("项目新增失败！");
                } else {
                    result.setMsg("项目修改失败！");
                }
            }
        } catch (CustomException e) {
            e.printStackTrace();
            result.setCode(ExecuteCode.FAIL.getCode().toString());
            result.setMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ExecuteCode.FAIL.getCode().toString());
            if (projectInfo.getBidSectionId() == null) {
                result.setMsg("项目新增失败！");
            } else {
                result.setMsg("项目修改失败！");
            }
        }
        return result;
    }

    /**
     * 获取有效投标人数量
     *
     * @return
     */
    @RequestMapping("/validBidderCount")
    public Map<String, Object> validBidderCount() {
        Map<String, Object> map = new HashMap<>(3);
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        boolean status = (CollectionUtil.isEmpty(bidders) || bidders.size() < 3) && (CommonUtil.isNull(bidSection.getCancelStatus()) || Enabled.NO.getCode().equals(bidSection.getCancelStatus()));
        map.put("bidSection", bidSection);
        map.put("status", status);
        map.put("bidderSize", bidders.size());
        return map;
    }

    /**
     * 标段流标
     *
     * @param bidSection 更新标段信息内容
     * @return 是否更新成功
     */
    @RequestMapping("/cancelBidSection")
    public boolean cancelBidSection(BidSection bidSection) {
        try {
            Integer bidSectionId = Objects.requireNonNull(CurrentUserHolder.getUser()).getBidSectionId();
            bidSection.setId(bidSectionId);
            bidSection.setBidOpenStatus(Status.END.getCode());
            bidSection.setBidOpenEndTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));
            return bidSectionService.updateBidSectionById(bidSection) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 跳转开标记录表页面
     *
     * @return
     */
    @RequestMapping("/cancelBidReportPage/{flag}")
    public ModelAndView cancelBidReportPage(@PathVariable("flag") Integer flag) {
        ModelAndView mav = new ModelAndView();
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        try {
            //判断是否已生成开标记录表
            String mark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + bidSectionId +
                    File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + "." + FileType.PDF.getSuffix();
            if (flag == 2) {
                //修改开标记录表
                fdfsService.deleteByMark(mark);
                mav.addObject("flag", flag);
            } else {
                String fdfsUrl = fdfsService.getUrlByMark(mark);
                //加载开标记录表pdf
                if (StringUtil.isNotEmpty(fdfsUrl)) {
                    AuthUser authUser = CurrentUserHolder.getUser();
                    mav.setViewName("/openBid/cancelRecordTable/showBidOpenRecord");
                    mav.addObject("mark", mark);
                    mav.addObject("user", authUser);
                    return mav;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.setSystemActionMessage("开标记录表加载失败，请重新生成！",
                    WebSocketMessage.Dialog.MSG, WebSocketMessage.Type.ERROR);
        }
        Map<String, Object> data = staffService.getBidOpenRecordData(bidSectionId, null);
        BidSection bidSection = (BidSection) data.get("bidSection");
        String bidClassifyCode = bidSection.getBidClassifyCode();
        String template = BidProtype.getTemplate(bidClassifyCode);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);

        mav.setViewName("/openBid/cancelRecordTable/" + template);
        mav.addObject("bidSection", data.get("bidSection"));
        mav.addObject("tenderProject", data.get("tenderProject"));
        mav.addObject("tenderDoc", data.get("tenderDoc"));
        mav.addObject("bidders", data.get("bidders"));
        mav.addObject("lineStatus", lineStatus);
        return mav;
    }


    /**
     * 检测是否可以查看签到投标人
     *
     * @param advanceTime 提前多久可以查看
     * @return 是否可以查看签到投标人
     */
    @RequestMapping("/validViewSignInBidder")
    public boolean validViewSignInBidder(Integer advanceTime) {
        try {
            Integer bidSectionId = Objects.requireNonNull(CurrentUserHolder.getUser()).getBidSectionId();
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            if (tenderDoc != null && StringUtils.isNotEmpty(tenderDoc.getBidOpenTime())) {
                String bidOpenTime = tenderDoc.getBidOpenTime();
                if (advanceTime == null) {
                    advanceTime = 0;
                }
                int diff = advanceTime * 60 * 1000;

                SimpleDateFormat sdf = new SimpleDateFormat(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode());
                Date parse = sdf.parse(bidOpenTime);
                String signTime = sdf.format(parse.getTime() - diff);
                String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                long countDownTime = DateTimeUtil.getTimeDiff(nowTime, signTime, TimeUnit.SECONDS, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                return countDownTime < 0;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
