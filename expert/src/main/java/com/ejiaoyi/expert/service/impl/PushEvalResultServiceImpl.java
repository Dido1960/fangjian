package com.ejiaoyi.expert.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.dto.BidderEvalResultDTO;
import com.ejiaoyi.common.dto.HttpResponseDTO;
import com.ejiaoyi.common.dto.PushReportDTO;
import com.ejiaoyi.common.dto.PushResultInfoDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.impl.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.HttpClientUtil;
import com.ejiaoyi.expert.service.IPushEvalResultService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 评标结果推送 服务实现类
 * </p>
 *
 * @author Mike
 * @since 2020-12-31
 */
@Service
@Slf4j
public class PushEvalResultServiceImpl extends BaseServiceImpl implements IPushEvalResultService {
    /**
     * 兰州国泰新点对接接口地址
     */
    @Value("${dock.gtxd.lz.url}")
    private String lzGtXdDockUrl;

    /**
     * 酒泉国泰新点对接接口地址
     */
    @Value("${dock.gtxd.jq.url}")
    private String jqGtXdDockUrl;

    /**
     * 国泰对接校验字段前缀
     */
    private static String SIGN_DATA_PREFIX = "epoint@f3d2eb1b-7c2f-4b95-bd78-88805e5a7cd6";
    /**
     * 国泰对接校验字段后缀
     */
    private static String SIGN_DATA_SUFFIX = "##";

    /**
     * 国泰评标报告私钥
     */
    private static final String GT_REPORT_SECRET_KEY = "17dc4c81789684c7";
    /**
     * 国泰评标报告IV
     */
    private static final String GT_REPORT_IV = "05b5b2da42ef4543";

    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private TenderProjectServiceImpl tenderProjectService;

    @Autowired
    private BidSectionServiceImpl bidSectionService;

    @Autowired
    private TenderDocServiceImpl tenderDocService;

    @Autowired
    private DockPushLogServiceImpl dockPushLogService;

    @Autowired
    private EvalResultSgServiceImpl evalResultSgService;

    @Autowired
    private EvalResultEpcServiceImpl evalResultEpcService;

    @Autowired
    private EvalResultJlServiceImpl evalResultJlService;

    @Autowired
    private BidderServiceImpl bidderService;

    @Autowired
    private CandidateSuccessServiceImpl candidateSuccessService;

    @Autowired
    private BidSectionRelateServiceImpl bidSectionRelateService;

    @Autowired
    private FDFSServiceImpl fdfsService;

    /**
     * 评标结果推送给酒泉
     *
     * @param bidSectionId 标段id
     */
    @Override
    public void pushEvalResultForJQ(Integer bidSectionId) throws Exception {
        // 获取需要进行推送的数据
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        if (!Enabled.YES.getCode().equals(bidSection.getDataFrom())) {
            return;
        }
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        Project project = projectService.getById(tenderProject.getProjectId());
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
        BidSectionRelate bidSectionRelate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        Integer evaluationReportId = bidSectionRelate.getEvaluationReportId();
        Fdfs fdfs = fdfsService.getFdfdById(evaluationReportId);
        String reportPath = FileUtil.getCustomFilePath() + UUID.randomUUID().toString() + ".pdf";
        if (fdfs != null && StringUtils.isNotEmpty(fdfs.getUrl())) {
            // 推送评标报告
            PushReportDTO pushReportDTO = PushReportDTO.builder()
                    .bidNo(bidSection.getBidSectionCode())
                    .fileName(bidSection.getBidSectionName() + "评标报告.pdf")
                    .build();
            byte[] bytes = fdfsService.downloadByUrl(fdfs.getUrl());
            FileUtil.writeFile(bytes, reportPath);
            pushEvalReportForJQ(reportPath, pushReportDTO);
        }

        PushResultInfoDTO pushResultInfoDTO = PushResultInfoDTO.builder()
                .projectCode(project.getProjectCode())
                .projectName(project.getProjectName())
                .bidSectionCode(bidSection.getBidSectionCode())
                .bidSectionName(bidSection.getBidSectionName())
                .isBidOpenEnd(Status.END.getCode().equals(bidSection.getBidOpenStatus()) ? "1" : "0")
                .isBidEvalEnd(Status.END.getCode().equals(bidSection.getEvalStatus()) ? "1" : "0")
                .bidOpenTime(tenderDoc.getBidOpenTime())
                .bidOpenEndTime(bidSection.getBidOpenEndTime())
                .evalStartTime(bidSection.getEvalStartTime())
                .evalEndTime(bidSection.getEvalEndTime())
                .build();

        String bdSencondType = "A11";
        String bidEvalMethod;
        List<BidderEvalResultDTO> bidderEvalResultDTOS = new ArrayList<>();
        switch (bidProtype) {
            case CONSTRUCTION:
                bidEvalMethod = "工程量清单综合记分评标定标办法";
                bidderEvalResultDTOS = listBidderConstructionResultData(bidSectionId);
                break;
            case SUPERVISION:
                bidEvalMethod = "综合计分法（监理）";
                bidderEvalResultDTOS = listBidderSupervisionResultData(bidSectionId);
                break;
            case INVESTIGATION:
                bidEvalMethod = "综合评估法（勘察）";
                bidderEvalResultDTOS = listBidderElectResultData(bidSectionId);
                break;
            case DESIGN:
                bidEvalMethod = "综合评估法（设计）";
                bidderEvalResultDTOS = listBidderElectResultData(bidSectionId);
                break;
            case QUALIFICATION:
                bidEvalMethod = "资格预审合格制";
                bidderEvalResultDTOS = listBidderQualificationResultData(bidSectionId);
                bdSencondType = "A10";
                break;
            case EPC:
                bidderEvalResultDTOS = listBidderEpcResultData(bidSectionId);
                bidEvalMethod = tenderDoc.getEvaluationMethod();
                break;
            case ELEVATOR:
                bidderEvalResultDTOS = listBidderElectResultData(bidSectionId);
            default:
                bidEvalMethod = "其他";
        }

        // 封装数据
        pushResultInfoDTO.setBidEvalMethod(bidEvalMethod);
        pushResultInfoDTO.setBidderEvalResultDTOS(bidderEvalResultDTOS);
        doPushResult(pushResultInfoDTO, bdSencondType);
    }

    /**
     * 通过标段id获取施工类投标人结果数据
     * @param bidSectionId 标段id
     * @return
     */
    private List<BidderEvalResultDTO> listBidderConstructionResultData(Integer bidSectionId) {
        List<Bidder> bidderList = bidderService.listAllBidders(bidSectionId, false);
        List<BidderEvalResultDTO> bidderEvalResultDTOS = new ArrayList<>();
        for (Bidder bidder : bidderList) {
            BidderEvalResultDTO  bidderEvalResultDTO = BidderEvalResultDTO.builder()
                    .bidderOrgCode(bidder.getBidderOrgCode())
                    .bidderName(bidder.getBidderName())
                    .bidderPrice((CommonUtil.isEmpty(bidder.getBidderOpenInfo().getBidPrice()) ? "0" : bidder.getBidderOpenInfo().getBidPrice()))
                    .priceUint("元")
                    .finalPrice((CommonUtil.isEmpty(bidder.getBidderOpenInfo().getBidPrice()) ? "0" : bidder.getBidderOpenInfo().getBidPrice()))
                    .bidderFileSealStatus("完好")
                    .build();
            if (Enabled.YES.getCode().equals(bidder.getIsPassBidOpen())) {
                EvalResultSg evalResultSg = evalResultSgService.getEvalResultSgById(bidSectionId, bidder.getId());
                String timeLimit = bidder.getBidderOpenInfo().getTimeLimit();
                if (!CommonUtil.isEmpty(timeLimit)) {
                    try {
                        timeLimit = timeLimit.replaceAll("日历天", "").replaceAll("日", "").replaceAll("天", "");
                        int day = Integer.parseInt(timeLimit);
                        timeLimit = String.valueOf(day);
                    } catch (Exception e) {
                        log.error("投标工期非数字，转换失败，推送给国泰新点默认传值【-1】");
                        timeLimit = "-1";
                    }
                }
                bidderEvalResultDTO.setConstructionPeriod(timeLimit);
                bidderEvalResultDTO.setQuality(CommonUtil.isEmpty(bidder.getBidderOpenInfo().getQuality())
                        ? null : bidder.getBidderOpenInfo().getQuality());
                bidderEvalResultDTO.setSignTime(CommonUtil.isEmpty(bidder.getBidderOpenInfo().getSigninTime())
                        ? null : bidder.getBidderOpenInfo().getSigninTime());

                if (evalResultSg != null) {
                    bidderEvalResultDTO.setTotalScore(evalResultSg.getTotalScore());
                    bidderEvalResultDTO.setRank(evalResultSg.getOrderNo());
                    bidderEvalResultDTO.setIsAbolish(Enabled.NO.getCode().toString());
                } else {
                    bidderEvalResultDTO.setIsAbolish(Enabled.YES.getCode().toString());
                    bidderEvalResultDTO.setAbolishReason("未通过初步评审");
                }
            } else {
                bidderEvalResultDTO.setSignTime(CommonUtil.isEmpty(bidder.getBidderOpenInfo().getSigninTime())
                        ? null : bidder.getBidderOpenInfo().getSigninTime());
                bidderEvalResultDTO.setIsAbolish(Enabled.YES.getCode().toString());
                bidderEvalResultDTO.setAbolishReason("未通过开标");
            }
            bidderEvalResultDTOS.add(bidderEvalResultDTO);
        }
        return bidderEvalResultDTOS;
    }

    /**
     * 通过标段id获取监理类投标人结果数据
     * @param bidSectionId 标段id
     * @return
     */
    private List<BidderEvalResultDTO> listBidderSupervisionResultData(Integer bidSectionId) {
        List<Bidder> bidderList = bidderService.listAllBidders(bidSectionId, false);
        List<BidderEvalResultDTO> bidderEvalResultDTOS = new ArrayList<>();
        for (Bidder bidder : bidderList) {
            BidderEvalResultDTO  bidderEvalResultDTO = BidderEvalResultDTO.builder()
                    .bidderOrgCode(bidder.getBidderOrgCode())
                    .bidderName(bidder.getBidderName())
                    .bidderPrice((CommonUtil.isEmpty(bidder.getBidderOpenInfo().getBidPrice()) ? "0" : bidder.getBidderOpenInfo().getBidPrice()))
                    .priceUint("元")
                    .finalPrice((CommonUtil.isEmpty(bidder.getBidderOpenInfo().getBidPrice()) ? "0" : bidder.getBidderOpenInfo().getBidPrice()))
                    .bidderFileSealStatus("完好")
                    .signTime(CommonUtil.isEmpty(bidder.getBidderOpenInfo().getSigninTime()) ? null : bidder.getBidderOpenInfo().getTimeLimit())
                    .build();
            if (Enabled.YES.getCode().equals(bidder.getIsPassBidOpen())) {
                EvalResultJl evalResultJl = evalResultJlService.getEvalResultJl(bidSectionId, bidder.getId());

                String timeLimit = bidder.getBidderOpenInfo().getTimeLimit();
                if (!CommonUtil.isEmpty(timeLimit)) {
                    try {
                        timeLimit = timeLimit.replaceAll("日历天", "").replaceAll("日", "").replaceAll("天", "");
                        int day = Integer.parseInt(timeLimit);
                        timeLimit = String.valueOf(day);
                    } catch (Exception e) {
                        log.error("投标工期非数字，转换失败，推送给国泰新点默认传值【-1】");
                        timeLimit = "-1";
                    }
                }
                bidderEvalResultDTO.setConstructionPeriod(timeLimit);
                bidderEvalResultDTO.setQuality(CommonUtil.isEmpty(bidder.getBidderOpenInfo().getQuality())
                        ? null : bidder.getBidderOpenInfo().getQuality());
                if (evalResultJl != null) {
                    bidderEvalResultDTO.setTechnologyScore(evalResultJl.getTechnicalScore());
                    bidderEvalResultDTO.setBusinessScore(evalResultJl.getBusinessScore());
                    bidderEvalResultDTO.setTotalScore(evalResultJl.getTotalScore());
                    bidderEvalResultDTO.setRank(evalResultJl.getOrderNo());
                    bidderEvalResultDTO.setIsAbolish(Enabled.NO.getCode().toString());
                } else {
                    bidderEvalResultDTO.setIsAbolish(Enabled.YES.getCode().toString());
                    bidderEvalResultDTO.setAbolishReason("未通过初步评审");
                }
            } else {
                bidderEvalResultDTO.setIsAbolish(Enabled.YES.getCode().toString());
                bidderEvalResultDTO.setAbolishReason("未通过开标");
            }
            bidderEvalResultDTOS.add(bidderEvalResultDTO);
        }
        return bidderEvalResultDTOS;
    }

    /**
     * 通过标段id获取施工总承包类投标人结果数据
     * @param bidSectionId 标段id
     * @return
     */
    private List<BidderEvalResultDTO> listBidderEpcResultData(Integer bidSectionId) {
        List<Bidder> bidderList = bidderService.listAllBidders(bidSectionId, false);
        List<BidderEvalResultDTO> bidderEvalResultDTOS = new ArrayList<>();
        for (Bidder bidder : bidderList) {
            BidderEvalResultDTO  bidderEvalResultDTO = BidderEvalResultDTO.builder()
                    .bidderOrgCode(bidder.getBidderOrgCode())
                    .bidderName(bidder.getBidderName())
                    .bidderPrice((CommonUtil.isEmpty(bidder.getBidderOpenInfo().getBidPrice()) ? "0" : bidder.getBidderOpenInfo().getBidPrice()))
                    .priceUint("元")
                    .finalPrice((CommonUtil.isEmpty(bidder.getBidderOpenInfo().getBidPrice()) ? "0" : bidder.getBidderOpenInfo().getBidPrice()))
                    .bidderFileSealStatus("完好")
                    .signTime(CommonUtil.isEmpty(bidder.getBidderOpenInfo().getSigninTime()) ? null : bidder.getBidderOpenInfo().getTimeLimit())
                    .build();
            if (Enabled.YES.getCode().equals(bidder.getIsPassBidOpen())) {
                EvalResultEpc evalResultEpc = evalResultEpcService.getEvalResultEpc(bidSectionId, bidder.getId());
                String timeLimit = bidder.getBidderOpenInfo().getTimeLimit();
                if (!CommonUtil.isEmpty(timeLimit)) {
                    try {
                        timeLimit = timeLimit.replaceAll("日历天", "").replaceAll("日", "").replaceAll("天", "");
                        int day = Integer.parseInt(timeLimit);
                        timeLimit = String.valueOf(day);
                    } catch (Exception e) {
                        log.error("投标工期非数字，转换失败，推送给国泰新点默认传值【-1】");
                        timeLimit = "-1";
                    }
                }
                bidderEvalResultDTO.setConstructionPeriod(timeLimit);
                bidderEvalResultDTO.setQuality(CommonUtil.isEmpty(bidder.getBidderOpenInfo().getQuality())
                        ? null : bidder.getBidderOpenInfo().getQuality());
                if (evalResultEpc != null) {
                    bidderEvalResultDTO.setTotalScore(evalResultEpc.getTotalScore());
                    bidderEvalResultDTO.setRank(evalResultEpc.getOrderNo());
                    bidderEvalResultDTO.setIsAbolish(Enabled.NO.getCode().toString());
                } else {
                    bidderEvalResultDTO.setIsAbolish(Enabled.YES.getCode().toString());
                    bidderEvalResultDTO.setAbolishReason("未通过资格审查评审或初步评审");
                }
            } else {
                bidderEvalResultDTO.setIsAbolish(Enabled.YES.getCode().toString());
                bidderEvalResultDTO.setAbolishReason("未通过开标");
            }
            bidderEvalResultDTOS.add(bidderEvalResultDTO);
        }
        return bidderEvalResultDTOS;
    }

    /**
     * 通过标段id获取推选(勘察、设计、电梯)类投标人结果数据
     * @param bidSectionId 标段id
     * @return
     */
    private List<BidderEvalResultDTO> listBidderElectResultData(Integer bidSectionId) {
        List<CandidateSuccess> candidateSuccesses = candidateSuccessService.listCandidateSuccess(bidSectionId);
        List<BidderEvalResultDTO> bidderEvalResultDTOS = new ArrayList<>();
        for (CandidateSuccess candidateSuccess : candidateSuccesses) {
            Bidder bidder = bidderService.getBidderById(candidateSuccess.getBidderId());
            BidderEvalResultDTO  bidderEvalResultDTO = BidderEvalResultDTO.builder()
                    .bidderOrgCode(bidder.getBidderOrgCode())
                    .bidderName(bidder.getBidderName())
                    .bidderPrice((CommonUtil.isEmpty(bidder.getBidderOpenInfo().getBidPrice()) ? "0" : bidder.getBidderOpenInfo().getBidPrice()))
                    .finalPrice((CommonUtil.isEmpty(bidder.getBidderOpenInfo().getBidPrice()) ? "0" : bidder.getBidderOpenInfo().getBidPrice()))
                    .bidderFileSealStatus("完好")
                    .signTime(CommonUtil.isEmpty(bidder.getBidderOpenInfo().getSigninTime()) ? null : bidder.getBidderOpenInfo().getTimeLimit())
                    .build();

            String timeLimit = bidder.getBidderOpenInfo().getTimeLimit();
            if (!CommonUtil.isEmpty(timeLimit)) {
                try {
                    timeLimit = timeLimit.replaceAll("日历天", "").replaceAll("日", "").replaceAll("天", "");
                    int day = Integer.parseInt(timeLimit);
                    timeLimit = String.valueOf(day);
                } catch (Exception e) {
                    log.error("投标工期非数字，转换失败，推送给国泰新点默认传值【-1】");
                    timeLimit = "-1";
                }
            }
            bidderEvalResultDTO.setConstructionPeriod(timeLimit);
            bidderEvalResultDTO.setQuality(CommonUtil.isEmpty(bidder.getBidderOpenInfo().getQuality())
                    ? null : bidder.getBidderOpenInfo().getQuality());

            String price = "0";
            String priceUint = "元";
            String finalPrice = "0";
            String bidPriceType = bidder.getBidderOpenInfo().getBidPriceType();
            String bidPrice = bidder.getBidderOpenInfo().getBidPrice();
            if (CommonUtil.isEmpty(bidPriceType) || "总价".equals(bidPriceType)) {
                if (!CommonUtil.isEmpty(bidPrice)) {
                    price = bidPrice;
                    finalPrice = bidPrice;
                }
            } else {
                price = bidPriceType + bidPrice;
                priceUint = "%";
                finalPrice = bidPriceType + bidPrice;
            }

            bidderEvalResultDTO.setBidderPrice(price);
            bidderEvalResultDTO.setPriceUint(priceUint);
            bidderEvalResultDTO.setFinalPrice(finalPrice);

            bidderEvalResultDTO.setRank(candidateSuccess.getRanking());
            bidderEvalResultDTO.setIsAbolish(Enabled.NO.getCode().toString());

            bidderEvalResultDTOS.add(bidderEvalResultDTO);
        }
        return bidderEvalResultDTOS;
    }

    /**
     * 通过标段id获取资格预审类投标人结果数据
     * @param bidSectionId 标段id
     * @return
     */
    private List<BidderEvalResultDTO> listBidderQualificationResultData(Integer bidSectionId) {
        List<Bidder> bidderList = bidderService.listAllBidders(bidSectionId, false);
        List<BidderEvalResultDTO> bidderEvalResultDTOS = new ArrayList<>();
        for (Bidder bidder : bidderList) {
            BidderEvalResultDTO  bidderEvalResultDTO = BidderEvalResultDTO.builder()
                    .bidderOrgCode(bidder.getBidderOrgCode())
                    .bidderName(bidder.getBidderName())
                    .bidderPrice((CommonUtil.isEmpty(bidder.getBidderOpenInfo().getBidPrice()) ? "0" : bidder.getBidderOpenInfo().getBidPrice()))
                    .priceUint("元")
                    .finalPrice((CommonUtil.isEmpty(bidder.getBidderOpenInfo().getBidPrice()) ? "0" : bidder.getBidderOpenInfo().getBidPrice()))
                    .bidderFileSealStatus("完好")
                    .signTime(CommonUtil.isEmpty(bidder.getBidderOpenInfo().getSigninTime()) ? null : bidder.getBidderOpenInfo().getTimeLimit())
                    .build();
            if (Enabled.YES.getCode().equals(bidder.getIsPassBidOpen())) {
                boolean preliminaryStatus = bidderService.validProcessPassInfo(bidSectionId, bidder.getId(), EvalProcess.PRELIMINARY.getCode());
                boolean detailedStatus = bidderService.validProcessPassInfo(bidSectionId, bidder.getId(), EvalProcess.DETAILED.getCode());
                if (preliminaryStatus && detailedStatus) {
                    bidderEvalResultDTO.setIsPassZGYS("1");
                } else {
                    bidderEvalResultDTO.setIsPassZGYS("0");
                    bidderEvalResultDTO.setIsAbolish(Enabled.YES.getCode().toString());
                    bidderEvalResultDTO.setAbolishReason("未通过初步评审或详细评审");
                }
            } else {
                bidderEvalResultDTO.setIsPassZGYS("0");
                bidderEvalResultDTO.setIsAbolish(Enabled.YES.getCode().toString());
                bidderEvalResultDTO.setAbolishReason("未通过开标");
            }
            bidderEvalResultDTOS.add(bidderEvalResultDTO);
        }
        return bidderEvalResultDTOS;
    }

    /**
     * 推送评标结果
     *
     * @param pushResultInfoDTO 推送的json
     * @param bdSencondType 标段类型(资格后审: A11 资格预审：A10)
     */
    private void doPushResult(PushResultInfoDTO pushResultInfoDTO, String bdSencondType) {
        String uri = jqGtXdDockUrl + "/JiaoYiTongGuoMiInfo/reciveJYTInfo";
        String paramJsonStr = JSONObject.toJSONString(pushResultInfoDTO);
        String encrypt = ejiaoyi.crypto.SM4Util.encryptCBC(paramJsonStr, GT_REPORT_SECRET_KEY, GT_REPORT_IV);
        System.out.println(encrypt);
        // 设置请求参数
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("PBContent", encrypt));
        params.add(new BasicNameValuePair("BdSencondType", bdSencondType));

        // 推送数据
        HttpResponseDTO httpResponseDTO = HttpClientUtil.post(uri, params);
        DockPushLog dockPushLog = DockPushLog.builder()
                .apiUri(uri)
                .apiParams("{'PBContent':'" + encrypt + "','BdSencondType':'" + bdSencondType + "'}")
                .apiParamsLaws("{'PBContent':'" + paramJsonStr + "','BdSencondType':'" + bdSencondType + "'}")
                .apiRemark("酒泉交易中心评标结果推送-国泰数据接口")
                .createApiTime(LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA)))
                .responseCode(httpResponseDTO.getCode())
                .responseContent(httpResponseDTO.getContent())
                .build();

        dockPushLogService.save(dockPushLog);
    }

    /**
     * 推送评标报告
     *
     * @param reportUrl 评标报告地址
     * @param pushReportDTO 评标报告推送DTO
     */
    private void pushEvalReportForJQ(String reportUrl, PushReportDTO pushReportDTO) {
        String uri = jqGtXdDockUrl + "/JYTDocument/receiveReportPB";
        String timeStamp = String.valueOf(System.currentTimeMillis());
        pushReportDTO.setTimeStamp(timeStamp);
        pushReportDTO.setSignData(DigestUtils.md5Hex(SIGN_DATA_PREFIX + timeStamp + SIGN_DATA_SUFFIX));
        String json = JSONObject.toJSONString(pushReportDTO);
        String encryptJson = ejiaoyi.crypto.SM4Util.encryptCBC(json, GT_REPORT_SECRET_KEY, GT_REPORT_IV);
        Map<String, String> filePaths = new HashMap<>(1);
        Map<String, String> params = new HashMap<>(1);
        filePaths.put("file", reportUrl);
        params.put("json", encryptJson);

        // 推送数据
        HttpResponseDTO httpResponseDTO = HttpClientUtil.postFormData(uri, filePaths, params);
        // 删除下载的评标报告
        FileUtil.deleteFile(reportUrl);
        DockPushLog dockPushLog = DockPushLog.builder()
                .apiUri(uri)
                .apiParams("{'file':'" + reportUrl + "','json':'" + encryptJson + "'}")
                .apiParamsLaws("{'file':'" + reportUrl + "','json':'" + json + "'}")
                .apiRemark("酒泉交易中心评标报告推送-国泰数据接口")
                .createApiTime(LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA)))
                .responseCode(httpResponseDTO.getCode())
                .responseContent(httpResponseDTO.getContent())
                .build();

        dockPushLogService.save(dockPushLog);
    }
}
