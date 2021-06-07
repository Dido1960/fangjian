package com.ejiaoyi.expert.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.constant.*;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.quantity.QuantityBidder;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.enums.quantity.QuantityServiceVersion;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.BidSectionRelateServiceImpl;
import com.ejiaoyi.common.service.impl.BidderServiceImpl;
import com.ejiaoyi.common.util.*;
import com.ejiaoyi.expert.service.IBidEvalService;
import com.ejiaoyi.expert.service.IConBidEvalService;
import com.ejiaoyi.expert.service.IExpertService;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 专家控制器
 *
 * @author Make
 * @since 2020/9/4
 */
@RestController
@RequestMapping("/expert")
@Slf4j
public class ExpertController extends BaseController {

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
    private IExpertUserService expertUserService;

    @Autowired
    private IBidApplyService bidApplyService;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IExpertService expertService;

    @Autowired
    private IExpertReviewSingleItemService expertReviewSingleItemService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private IBidEvalService bidEvalService;

    @Autowired
    private BidderServiceImpl bidderService;

    @Autowired
    private BidSectionRelateServiceImpl bidSectionRelateService;

    @Autowired
    private IConBidEvalService conBidEvalService;

    /**
     * 根据id获取专家信息
     *
     * @param id 专家id
     * @return
     */
    @RequestMapping("/getExpertUser")
    public ExpertUser getExpertUser(Integer id) {
        if (CommonUtil.isEmpty(id)) {
            AuthUser currentUser = CurrentUserHolder.getUser();
            id = currentUser.getUserId();
        }
        return expertUserService.getExpertUserById(id);
    }

    /**
     * 根据id修改专家信息
     *
     * @param expertUser 专家信息
     * @return
     */
    @RequestMapping("/updateExpertUser")
    public boolean updateExpertUser(ExpertUser expertUser) {
        if (CommonUtil.isEmpty(expertUser.getId())) {
            AuthUser currentUser = CurrentUserHolder.getUser();
            expertUser.setId(currentUser.getUserId());
        }
        return expertUserService.updateExpertById(expertUser);
    }

    /**
     * 同意准守
     *
     * @return
     */
    @RequestMapping("/updateAgreeBook")
    public boolean updateAgreeBook() {
        return expertUserService.updateExpertById(ExpertUser.builder().id(CurrentUserHolder.getUser().getUserId()).avoid(ExpertStatus.CONFIRMED).build());
    }


    /**
     * 跳转专家确认评标页面
     *
     * @return
     */
    @RequestMapping("/confirmBidEvalPage")
    public ModelAndView confirmBidEvalPage() {
        ModelAndView mav = new ModelAndView("/selectLeader/confirmBidEvalPage");
        AuthUser user = CurrentUserHolder.getUser();
        ExpertUser expertUser = expertUserService.getExpertUserById(user.getUserId());
        //设置评标组长
        user.setIsChairman(expertUser.getIsChairman());
        Integer bidSectionId = user.getBidSectionId();
        //查询专家组长信息
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        ExpertUser leaderExpertUser = expertUserService.getExpertUserById(bidApply.getChairMan());
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        if (String.valueOf(EvalStatus.PROGRESSING).equals(String.valueOf(bidSection.getEvalStatus()))) {

            return new ModelAndView("redirect:/expert/startEval");
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        mav.addObject("expert", user);
        mav.addObject("leaderExpertUser", leaderExpertUser);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }


    /**
     * 根据id获取标段信息
     *
     * @param id 标段id
     * @return 标段
     */
    @RequestMapping("/getBidSectionById")
    public BidSection getBidSectionById(Integer id) {
        if (CommonUtil.isEmpty(id)) {
            AuthUser currentUser = CurrentUserHolder.getUser();
            assert currentUser != null;
            id = currentUser.getBidSectionId();
        }
        return bidSectionService.getBidSectionById(id);
    }

    @RequestMapping("/sendStartRequest")
    public Boolean sendStartRequest(TenderDoc tenderDoc) {
        Integer bidSectionId = tenderDoc.getBidSectionId();

        BidSection bidSection = BidSection.builder()
                .id(bidSectionId)
                .evalReviewStatus(Status.PROCESSING.getCode().toString())
                .build();
        BidSection section = bidSectionService.getBidSectionById(bidSectionId);

        if (BidProtype.CONSTRUCTION.getCode().equals(section.getBidClassifyCode())) {
            tenderDocService.updateTenderDocById(tenderDoc);
            BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
            bidSectionRelateService.updateBidSectionRelate(BidSectionRelate.builder()
                    .id(bidSectionRelate.getId())
                    .startClearTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                    .build());
        }
        return bidSectionService.updateBidSectionById(bidSection) == 1;
    }

    /**
     * 开始评标
     *
     * @return
     */
    @RequestMapping("/startEval")
    public ModelAndView startEval() {
        AuthUser user = CurrentUserHolder.getUser();
        ModelAndView mav = new ModelAndView("/evalPlan/evalIndex");
        mav.addObject("clearUrl", "http://192.168.31.195:8081/#/clear-home");
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        String bidClassifyCode = bidSection.getBidClassifyCode();
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidClassifyCode);
        // 如果行业监管未审核通过，不进入评标
        if (!Status.END.getCode().toString().equals(bidSection.getEvalReviewStatus())) {
            mav.setViewName("redirect:/expert/confirmBidEvalPage");
            return mav;
        }

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        //未开始评标的情况下修改评标状态
        if (EvalStatus.UNSTART.equals(bidSection.getEvalStatus())) {
            bidSectionService.updateBidSectionById(BidSection.builder()
                    .id(bidSectionId)
                    .evalStatus(EvalStatus.PROGRESSING)
                    .evalStartTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS))
                    .build());
        }

        // 开标完成情况
        boolean bidOpenCompletion;
        switch (bidProtype) {
            case CONSTRUCTION:
                bidOpenCompletion = (bidSection.getBidOpenStatus() != null && Status.END.getCode().equals(bidSection.getBidOpenStatus()));
                mav.addObject("preliminary", bidOpenCompletion);
                if (bidOpenCompletion) {
                    // 初步评审完成情况
                    boolean preliminaryCompletion = expertService.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
                    mav.addObject("detail", preliminaryCompletion);
                    if (preliminaryCompletion) {
                        boolean detailCompletion = expertService.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode());
                        mav.addObject("price", detailCompletion);
                        if (detailCompletion) {
                            boolean priceCompletion = !CommonUtil.isEmpty(bidSection.getPriceRecordStatus())
                                    && ExecuteCode.SUCCESS.getCode().equals(bidSection.getPriceRecordStatus());
                            if (priceCompletion) {
                                if (Enabled.YES.getCode().equals(tenderDoc.getMutualSecurityStatus())) {
                                    // 详细评审完成情况
                                    mav.addObject("other", priceCompletion);
                                    // 其他评审完成情况
                                    String[] otherGradeIds = tenderDoc.getOtherGradeId().split(",");
                                    boolean otherCompletion = expertService.isGroupCompletion(otherGradeIds, EvalProcess.OTHER.getCode());
                                    mav.addObject("result", otherCompletion);
                                } else {
                                    // 详细评审完成情况
                                    mav.addObject("result", priceCompletion);
                                }
                            }
                        }
                    }
                }

                // 如果项目流标了，可以直接进入评标结果
                if (Enabled.YES.getCode().equals(bidSection.getCancelStatus())) {
                    mav.addObject("detail", false);
                    mav.addObject("result", true);
                }
                break;
            case QUALIFICATION:
                bidOpenCompletion = (bidSection.getBidOpenStatus() != null && Status.END.getCode().equals(bidSection.getBidOpenStatus()));
                mav.addObject("preliminary", bidOpenCompletion);
                boolean detailCompletionStatus = false;
                if (bidOpenCompletion) {
                    // 初步评审完成情况
                    boolean preliminaryCompletion = expertService.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
                    mav.addObject("detail", preliminaryCompletion);
                    if (preliminaryCompletion) {
                        detailCompletionStatus = expertService.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode());
                        // 详细评审完成情况
                        mav.addObject("result", detailCompletionStatus);
                    }
                }
                // 如果项目流标了，可以直接进入评标结果
                if (Enabled.YES.getCode().equals(bidSection.getCancelStatus())) {
                    mav.addObject("detail", detailCompletionStatus);
                    mav.addObject("result", true);
                }
                break;
            case SUPERVISION:
            case DESIGN:
            case ELEVATOR:
            case INVESTIGATION:
                bidOpenCompletion = (bidSection.getBidOpenStatus() != null && Status.END.getCode().equals(bidSection.getBidOpenStatus()));
                mav.addObject("preliminary", bidOpenCompletion);
                if (bidOpenCompletion) {
                    // 初步评审完成情况
                    boolean preliminaryCompletion = expertService.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
                    mav.addObject("detail", preliminaryCompletion);
                    if (preliminaryCompletion) {
                        boolean detailCompletion = expertService.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode());
                        // 详细评审完成情况
                        mav.addObject("result", detailCompletion);
                    }
                }
                // 如果项目流标了，可以直接进入评标结果
                if (Enabled.YES.getCode().equals(bidSection.getCancelStatus())) {
                    mav.addObject("detail", false);
                    mav.addObject("result", true);
                }
                break;
            case EPC:
                boolean qualificationCompletion = false;
                boolean preliminaryCompletion = false;
                bidOpenCompletion = (bidSection.getBidOpenStatus() != null && Status.END.getCode().equals(bidSection.getBidOpenStatus()));
                mav.addObject("qualification", bidOpenCompletion);
                if (bidOpenCompletion) {
                    // 资格审查完成情况
                    qualificationCompletion = expertService.isGroupCompletion(gradeIds, EvalProcess.QUALIFICATION.getCode());
                    mav.addObject("preliminary", qualificationCompletion);
                    if (qualificationCompletion) {
                        // 初步评审完成情况
                        preliminaryCompletion = expertService.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
                        mav.addObject("detail", preliminaryCompletion);
                        if (preliminaryCompletion) {
                            boolean detailCompletion = expertService.isGroupCompletion(gradeIds, EvalProcess.DETAILED.getCode());
                            mav.addObject("price", detailCompletion);
                            if (detailCompletion) {
                                // 报价得分计算完成情况
//                                boolean priceStatus = bidSection.getPriceRecordStatus() != null && Enabled.YES.getCode().equals(bidSection.getPriceRecordStatus());
                                // 判断小组是否结束，结束后方可查看评标报告
                                boolean groupEnd = expertService.isGroupCompletion(gradeIds, EvalProcess.CALC_PRICE_SCORE.getCode());
                                mav.addObject("result", groupEnd);
                            }
                        }
                    }
                }
                // 如果项目流标了，可以直接进入评标结果
                if (Enabled.YES.getCode().equals(bidSection.getCancelStatus())) {
                    if (qualificationCompletion && preliminaryCompletion) {
                        mav.addObject("detail", false);
                    } else if (qualificationCompletion) {
                        mav.addObject("preliminary", false);
                    }

                    mav.addObject("result", true);
                }
                break;
            default:
        }

        mav.addObject("expert", user);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }


    /**
     * 修改专家个人评审意见 （合格制）
     *
     * @param expertReviewSingleItem
     * @return
     */
    @RequestMapping("/updateExpertComment")
    public boolean updateExpertComment(ExpertReviewSingleItem expertReviewSingleItem) {
        return expertReviewSingleItemService.updateById(expertReviewSingleItem);
    }


    /**
     * 更新审核情况
     *
     * @return
     */
    @RequestMapping("/updateStartEvalReviewFlag")
    public boolean updateStartEvalReviewFlag(TenderDoc tenderDoc) {
        try {
            Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            /*BidSection section = new BidSection();
            section.setId(bidSection.getId());*/

            if (BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode())) {
                TenderDoc old = tenderDocService.getTenderDocBySectionId(bidSectionId);
                if (Enabled.YES.getCode().equals(old.getMutualSecurityStatus())) {
                    Integer mutualGradeId = conBidEvalService.mutualGradeInit();
                    tenderDoc.setOtherGradeId(mutualGradeId.toString());
                    return tenderDocService.updateTenderDocById(tenderDoc);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取投标文件工程量清单的url
     *
     * @return
     */
    @RequestMapping("/getListUrl")
    public Map<String, String> getListUrl(Integer bidderId) {
        String mark = File.separator + ProjectFileTypeConstant.BIDDER_FILE + File.separator + bidderId + BidFileConstant.ENGINEER_QUANTITY_LIST_XML;
        Map<String, String> data = new HashMap<>();
        data.put("url", fdfsService.getUrlByMark(mark));
        return data;
    }

    /**
     * 远程异地评标
     * 请求AnyChat监控室界面
     *
     * @return
     */
    @RequestMapping("/expertEval")
    public ModelAndView expertEval() {
        ModelAndView mav = new ModelAndView();
        AuthUser user = CurrentUserHolder.getUser();
        BidSection bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());

        mav.setViewName("/anyChat/expertEval");
        mav.addObject("bidSection", bidSection);
        mav.addObject("ip", ip);
        mav.addObject("port", port);
        mav.addObject("expert", user);
        return mav;
    }

    /**
     * 获取有效投标人数量
     *
     * @param evalFlow 评审流程环节
     * @return
     */
    @RequestMapping("/validBidderCount")
    public Map<String, Object> validBidderCount(Integer evalFlow) {
        Map<String, Object> map = new HashMap<>(3);
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<Bidder> bidders = bidEvalService.listQualifiedBidder(bidSectionId, evalFlow);
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
            return bidSectionService.updateBidSectionById(bidSection) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 打印承诺书
     *
     * @return 打印承诺书
     */
    @RequestMapping("/promisePrint")
    public ModelAndView promisePrint() {
        ModelAndView mav = new ModelAndView("/selectLeader/promisePrint");
        LocalDateTime now = LocalDateTime.now();
        mav.addObject("now", now);
        return mav;
    }


    /**
     * 校验清标是否完成
     *
     * @return 是否更新成功
     */
    @RequestMapping("/validClearBidComplete")
    public boolean validClearBidComplete() {
        try {
            Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
            return bidderService.validAllQuantityService(bidSectionId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 展示评标报告合成进度
     *
     * @return
     */
    @RequestMapping("/showSysInfoPage")
    public ModelAndView showSysInfoPage() {
        return new ModelAndView("/common/showSysInfo");
    }

    /**
     * 查询当前评标报告数据生成状态
     *
     * @return
     */
    @RequestMapping("nowBackPdf")
    public Map<String, Object> currentConversionPDF() {
        AuthUser user = CurrentUserHolder.getUser();
        Map<String, Object> map = new HashMap<>();
        Object reportDtos = RedisUtil.get(CacheName.REPORT_FLOWS_DTO + user.getBidSectionId());
        map.put("reportDtos", reportDtos);
        return map;
    }

    /**
     * 创建清标整体服务
     *
     * @return 是否创建成功
     */
    @RequestMapping("/createOverallAnalysis")
    public boolean createOverallAnalysis() {
        try {
            AuthUser user = CurrentUserHolder.getUser();
            Integer bidSectionId = user.getBidSectionId();
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            // 施工更新清标时间
            List<QuantityBidder> quantityBidders = bidderService.listClearQuantityBidder(bidSectionId);
            log.info("招标工程量清单：" + tenderDoc.getXmlUid());

            BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
            String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
            long timeDiff = DateTimeUtil.getTimeDiff(bidSectionRelate.getStartClearTime(), nowTime, TimeUnit.MILLISECONDS, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
            log.info("创建清标时长：" + timeDiff);
            return bidSectionRelateService.updateBidSectionRelate(BidSectionRelate.builder()
                    .id(bidSectionRelate.getId())
                    .clearTotalTime(DateTimeUtil.parseTime(timeDiff))
                    .build()) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("创建清标服务号异常");
            return false;
        }
    }


    /**
     * 创建清标整体服务
     *
     * @return 是否创建成功
     *//*
    @RequestMapping("/test")
    public void test(Integer test) throws InterruptedException {
        String quantityScoreResult = quantityService.getQuantityScoreResult("c58bc964-b2ec-4cfa-a8bf-b9b00ce71ec9", QuantityServiceVersion.V1);
        JSONObject jsonObject = JSONObject.parseObject(quantityScoreResult);
        List<BidderQuantityScoreDTO> bidderQuantityScoreDTOS = JSONObject.parseArray(jsonObject.getString("bidder_quantity_score_list"), BidderQuantityScoreDTO.class);
        bidderQuantityScoreDTOS.sort((r1, r2) -> {
            double diff = r2.getScore().doubleValue() - r1.getScore().doubleValue();
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            } else {
                return 0;
            }
        });
        System.out.println(quantityScoreResult);
        //conBidEvalService.testCalcPriceScore(test);
    }*/


}
