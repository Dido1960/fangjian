package com.ejiaoyi.api.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.asymmetric.Sign;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.api.dto.*;
import com.ejiaoyi.api.exception.APIException;
import com.ejiaoyi.api.service.IStatisticalDataService;
import com.ejiaoyi.common.constant.*;
import com.ejiaoyi.common.dto.RestPageInfo;
import com.ejiaoyi.common.dto.statistical.*;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.mapper.BidSectionMapper;
import com.ejiaoyi.common.mapper.BidderMapper;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.*;
import com.ejiaoyi.common.util.*;
import io.swagger.models.auth.In;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 数据统计对接处理 服务实现类
 *
 * @author Mike
 * @since 2021/03/23
 */
@Slf4j
@Service
public class StatisticalDataServiceImpl extends BaseServiceImpl implements IStatisticalDataService {
    @Value("${message.room-id}")
    private String roomId;
    @Value("${message.bidder-id-pre}")
    private String bidderIdPre;

    @Autowired
    private BidSectionServiceImpl bidSectionService;
    @Autowired
    private RegServiceImpl regService;
    @Autowired
    private BidderServiceImpl bidderService;
    @Autowired
    private BidderMapper bidderMapper;
    @Autowired
    private BidSectionMapper bidSectionMapper;
    @Autowired
    private IFDFSService fdfsService;
    @Autowired
    private IBidderFileInfoService bidderFileInfoService;
    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;
    @Autowired
    private IBsnChainInfoService bsnChainInfoService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private ILineStatusService lineStatusService;
    @Autowired
    private OperationMaintenanceTrackRecordServiceImpl operationMaintenanceTrackRecordService;
    @Autowired
    private ITenderProjectService tenderProjectService;
    @Autowired
    private IUploadFileService uploadFileService;
    @Autowired
    private IAlipayIdentityAuthService alipayIdentityAuthService;


    @Override
    public List<ProjectDataDTO> listProjectData(GetProjectDataParam getProjectDataParam) {
        List<BidSection> bidSections = bidSectionService.listStatisticalBidSection(BidSection.builder()
                .bidOpenTime(getProjectDataParam.getFilterTime())
                .build());
        List<ProjectDataDTO> projectDataDTOS = new ArrayList<>();

        String bidOpenTotalTime;
        String bidEvalTotalTime;
        for (BidSection bidSection : bidSections) {
            // 开标总用时
            if (StringUtils.isNotEmpty(bidSection.getBidOpenEndTime())) {
                long openTimeDiff = DateTimeUtil.getTimeDiff(bidSection.getBidOpenTime(), bidSection.getBidOpenEndTime(),
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

            Reg reg = regService.getRegById(bidSection.getRegId());

            // 统计投标人数（有效数/总数）
            List<Bidder> allBidderList = bidderService.listAllSimpleBidders(bidSection.getId(), false);
            List<Bidder> passBidOpenBidderList = bidderService.listPassBidOpenSimpleBidder(bidSection.getId());

            // 运维情况
            OperationMaintenanceTrackRecord operationMaintenance = operationMaintenanceTrackRecordService.getOperationMaintenanceBySectionId(bidSection.getId());
            OperationMaintenanceDTO operationMaintenanceDTO = null;
            if (operationMaintenance != null) {
                operationMaintenanceDTO = OperationMaintenanceDTO.builder()
                        .abnormal(operationMaintenance.getAbnormal())
                        .abnormalTime(operationMaintenance.getAbnormalTime())
                        .operationMaintenanceName(operationMaintenance.getOperationMaintenanceName())
                        .remark(operationMaintenance.getRemark())
                        .build();
            }

            // 封装数据
            projectDataDTOS.add(ProjectDataDTO.builder()
                    .bidSectionName(bidSection.getBidSectionName())
                    .bidSectionCode(bidSection.getBidSectionCode())
                    .bidType(bidSection.getBidClassifyName())
                    .bidOpenTime(bidSection.getBidOpenTime())
                    .bidOpenTotalTime(bidOpenTotalTime)
                    .bidEvalTotalTime(bidEvalTotalTime)
                    .regName(reg.getRegName())
                    .totalBidderNum(allBidderList.size())
                    .effectiveBidderNum(passBidOpenBidderList.size())
                    .operationMaintenanceDTO(operationMaintenanceDTO)
                    .build());
        }
        return projectDataDTOS;
    }

    @Override
    public StatisticalDataDTO getStatisticalInfo(GetStatisticalDataParam getStatisticalDataParam) {
        BidSection build = BidSection.builder().build();
        if (StringUtils.isNotEmpty(getStatisticalDataParam.getRegName())) {
            Reg reg = regService.getRegByRegName(getStatisticalDataParam.getRegName());
            if (reg == null) {
                throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配到相应的区划信息，错误内容：" + getStatisticalDataParam.getRegName(), null);
            }

            build.setRegId(reg.getId());
        }

        if (StringUtils.isNotEmpty(getStatisticalDataParam.getBidSectionClassifyCode())) {
            BidProtype bidProtype = BidProtype.getBidProtypeByCode(getStatisticalDataParam.getBidSectionClassifyCode());
            if (bidProtype == null) {
                throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配到相应的标段类型，错误内容：" + getStatisticalDataParam.getBidSectionClassifyCode(), null);
            }
            build.setBidClassifyCode(getStatisticalDataParam.getBidSectionClassifyCode());
        }

        if (StringUtils.isNotEmpty(getStatisticalDataParam.getFilterTime())) {
            build.setBidOpenTime(getStatisticalDataParam.getFilterTime());
        }

        int totalBidSectionNum = 0;
        int normalBidSectionNum = 0;
        int abnormalBidSectionNum = 0;
        StatisticalDataDTO statisticalDataDTO = new StatisticalDataDTO();
        if (Status.PROCESSING.getCode().toString().equals(getStatisticalDataParam.getFilterType())) {
            List<BidTypeDetailDTO> bidTypeDetailDTOS = bidSectionService.statisticalBidSectionByBidType(build);
            statisticalDataDTO.setBidTypeDetailDTOList(bidTypeDetailDTOS);
            for (BidTypeDetailDTO bidTypeDetailDTO : bidTypeDetailDTOS) {
                totalBidSectionNum = totalBidSectionNum + bidTypeDetailDTO.getTotalBidSectionNum();
                normalBidSectionNum = normalBidSectionNum + bidTypeDetailDTO.getNormalBidSectionNum();
                abnormalBidSectionNum = abnormalBidSectionNum + bidTypeDetailDTO.getAbnormalBidSectionNum();
            }
        } else if (Status.END.getCode().toString().equals(getStatisticalDataParam.getFilterType())) {
            List<RegDetailDTO> regDetailDTOS = bidSectionService.statisticalBidSectionByReg(build);
            for (RegDetailDTO regDetailDTO : regDetailDTOS) {
                totalBidSectionNum = totalBidSectionNum + regDetailDTO.getTotalBidSectionNum();
                normalBidSectionNum = normalBidSectionNum + regDetailDTO.getNormalBidSectionNum();
                abnormalBidSectionNum = abnormalBidSectionNum + regDetailDTO.getAbnormalBidSectionNum();
            }
            statisticalDataDTO.setRegDetailDTOList(regDetailDTOS);
        }

        statisticalDataDTO.setTotalBidSectionNum(totalBidSectionNum);
        statisticalDataDTO.setNormalBidSectionNum(normalBidSectionNum);
        statisticalDataDTO.setAbnormalBidSectionNum(abnormalBidSectionNum);
        return statisticalDataDTO;
    }

    @Override
    public RestPageInfo<BidNewsDTO> pageBidNewsByDate(GetBidDataParam getBidDataParam) {
        Integer page = getBidDataParam.getPage();
        Integer limit = getBidDataParam.getLimit();
        Page<BidNewsDTO> pageQuery = new Page<>(page, limit);
        BidSection section = BidSection.builder().build();
        if (!CommonUtil.isEmpty(getBidDataParam.getBidOpenScopeDay())) {
            section.setScopeOpenNumDay(getBidDataParam.getBidOpenScopeDay().toString());
        } else {
            section.setScopeOpenNumDay("1");
        }
        if (StringUtils.isNotEmpty(getBidDataParam.getProjectCodeOrName())) {
            section.setBidSectionName(getBidDataParam.getProjectCodeOrName());
            section.setBidSectionCode(getBidDataParam.getProjectCodeOrName());
        }
        if (!CommonUtil.isEmpty(getBidDataParam.getBidOpenStatus())) {
            section.setBidOpenStatus(getBidDataParam.getBidOpenStatus());
        }
        List<BidNewsDTO> bidNewsDTOS = bidSectionMapper.pageBidNewsByDate(pageQuery, section);
        return new RestPageInfo<>(pageQuery.getTotal(), limit, page, bidNewsDTOS);

    }

    @Override
    public String getReceiptInfo(Integer bidderId) {
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        //判已经上传了文件,获取回执单信息
        if (bidderFileInfo != null && bidderFileInfo.getReceiptId() != null) {
            Fdfs receiptFile = fdfsService.getFdfdById(bidderFileInfo.getReceiptId());
            return receiptFile != null ? receiptFile.getUrl() : null;
        }
        return null;
    }

    @Override
    public RestResultVO<String> processBidderSignInfo(GetBidderSignInfo getBidderSignInfo) {
        RestResultVO<String> result = new RestResultVO<>();
        try {
            Integer bidSectionId = getBidderSignInfo.getBidSectionId();
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            Integer bidderId = getBidderSignInfo.getBidderId();
            BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);

            //认证时间判断
            if (!Enabled.YES.getCode().equals(judgeSingTime(bidSection.getSignInStartTimeLeft(), tenderDoc.getBidOpenTime()))) {
                result.setCode(PhoneResponseState.FAIL);
                result.setMsg("当前时间不可签到");
                return result;
            }

            //委托书 获取及下载
            String customPath = FileUtil.getCustomFilePath() + ProjectFileTypeConstant.API_PHONE;
            String fdfsUploadPath = customPath + File.separator + bidderId;
            UUID uuid = UUID.randomUUID();
            String fileName = uuid + ".pdf";
            String path = fdfsUploadPath + File.separator + ProjectFileTypeConstant.API_PHONE;
            String outPath = path + File.separator + fileName;
            downloadFile(getBidderSignInfo.getSqwtsUrl(), fileName, path);
            //构造FastDfs 文件Mark
            String mark = File.separator + ProjectFileTypeConstant.API_PHONE +
                    File.separator + bidderId + File.separator + fileName;
            //文件 上传
            Integer sqwtsUploadId = fileUpload(outPath, mark);

            //数据更新
            BidderOpenInfo update = BidderOpenInfo.builder()
                    .id(bidderOpenInfo.getId())
                    .clientName(getBidderSignInfo.getClientName())
                    .clientIdcard(getBidderSignInfo.getClientIdCard())
                    .clientPhone(getBidderSignInfo.getClientPhone())
                    .sqwtsFileId(sqwtsUploadId)
                    .build();

            //生成 认证地址
            String authUrl = getAuthUrl(getBidderSignInfo, update);

            bidderOpenInfoService.updateBidderOpenInfoById(update);
            ThreadUtlis.run(() -> bsnChainInfoService.bidderAttorney(update));
            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("数据接收成功");
            result.setData(authUrl);

        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<String> getPriceOrFloat(Integer bidSectionId, Integer type) {
        RestResultVO<String> result = new RestResultVO();
        try {
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            String data;
            if (CommonUtil.isEmpty(type) || type == 1) {
                data = tenderDoc.getControlPrice();
            }else {
                data = tenderDoc.getFloatPoint();
            }
            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("数据获取成功");
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<BidOpenDataDTO> getBidOpenFlowSituation(GetBidderCommonInfo getBidderCommonInfo) {
        RestResultVO<BidOpenDataDTO> result = new RestResultVO();
        BidOpenDataDTO data = new BidOpenDataDTO();
        try {
            //开始时间
            long startTime = System.currentTimeMillis();

            Integer bidderId = getBidderCommonInfo.getBidderId();
            Integer bidSectionId = getBidderCommonInfo.getBidSectionId();

            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);

            //获取当前标段类型的 开标流程列表
            BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
            List<BidOpenProcessDTO> list = listBidOpenProcessByBp(bidProtype);

            //遍历流程表 判断流程进行情况
            for (BidOpenProcessDTO process : list) {
                Integer processStatus;
                switch (process.getProcess()) {
                    case BIDDER_LIST:
                        processStatus = Status.END.getCode().equals(lineStatus.getPublishBidderStatus())
                                && Status.END.getCode().equals(lineStatus.getBidderCheckStatus())
                                ? Status.END.getCode() : Status.NOT_START.getCode();
                        break;
                    case DESTROY:
                        processStatus = CommonUtil.isEmpty(lineStatus.getDecryptionStatus()) ? Status.NOT_START.getCode() : lineStatus.getDecryptionStatus();
                        break;
                    case FLOAT:
                        processStatus = CommonUtil.isEmpty(tenderDoc.getFloatPoint()) ? Status.NOT_START.getCode() : Status.END.getCode();
                        break;
                    case PRICE:
                    case HIGHEST_PRICE:
                        processStatus = CommonUtil.isEmpty(tenderDoc.getControlPrice()) ? Status.NOT_START.getCode() : Status.END.getCode();
                        break;
                    case CONFIRM:
                        processStatus = Status.END.getCode().equals(lineStatus.getDecryptionStatus())
                                ? (Enabled.YES.getCode().equals(boi.getPriceDetermine())
                                ? Status.END.getCode() : Status.PROCESSING.getCode()) : Status.NOT_START.getCode();
                        break;
                    case ANALYSIS:
                    case HIGHEST_ANALYSIS:
                        processStatus = Enabled.YES.getCode().equals(boi.getPriceDetermine()) ? Status.END.getCode() : Status.NOT_START.getCode();
                        break;
                    case OPEN_TABLE:
                        processStatus = Enabled.YES.getCode().equals(boi.getPriceDetermine()) ? Status.PROCESSING.getCode() : Status.NOT_START.getCode();
                        break;
                    case QUESTION:
                        processStatus = CommonUtil.isEmpty(lineStatus.getQuestionStatus()) ? Status.NOT_START.getCode() : lineStatus.getQuestionStatus();
                        break;
                    case OPEN_END:
                        processStatus = bidSection.getBidOpenStatus();
                        break;
                    default:
                        processStatus = Status.NOT_START.getCode();
                        break;
                }
                process.setProcessStatus(processStatus);
            }
            data.setProcessList(list);
            data.setTenderRejection(boi.getTenderRejection());
            data.setTenderRejectionReason(boi.getTenderRejectionReason());

            //结束时间
            long endTime = System.currentTimeMillis();

            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("请求时间：" + (endTime - startTime) + "ms");
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public List<BidderListDTO> listBidders(Integer bidSectionId) {
        return bidderMapper.listBiddersForPhone(bidSectionId);
    }

    @Override
    public RestResultVO<ControlPriceAnalysisToDTO> getControlPriceAnalysis(Integer bidSectionId) {
        RestResultVO<ControlPriceAnalysisToDTO> result = new RestResultVO();
        try {
            ControlPriceAnalysisToDTO data = new ControlPriceAnalysisToDTO();
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            List<Bidder> bidders = bidderService.listDecrySuccessBidder(bidSectionId, false);
            String controlPrice = tenderDoc.getControlPrice();
            List<ControlPriceAnalysisDTO> list = new ArrayList<>();
            for (Bidder bidder : bidders) {
                String bidPrice = bidder.getBidderOpenInfo().getBidPrice();
                ControlPriceAnalysisDTO cpa = new ControlPriceAnalysisDTO();
                cpa.setBidderId(bidder.getId());
                cpa.setBidderName(bidder.getBidderName());
                cpa.setBidPrice(bidPrice);
                if (!bidPrice.contains("%") && StringUtils.isNotEmpty(controlPrice) && StringUtils.isNotEmpty(bidPrice)) {
                    cpa.setResult(Double.parseDouble(controlPrice) >= Double.parseDouble(bidPrice) ? 1 : 2);
                } else {
                    cpa.setResult(2);
                }
                list.add(cpa);
            }
            data.setControlPrice(tenderDoc.getControlPrice());
            data.setBidders(list);
            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("数据获取成功");
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public List<ConfirmBidOpenRecordDTO> getConfirmBidOpenRecord(Integer bidSectionId) {
        return bidderMapper.confirmBidOpenRecordForPhone(bidSectionId);
    }

    @Override
    public RestResultVO<Boolean> confirmBidderPrice(GetBidderCommonInfo getBidderCommonInfo) {
        RestResultVO<Boolean> result = new RestResultVO();
        try {
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(getBidderCommonInfo.getBidderId(), getBidderCommonInfo.getBidSectionId());
            Integer update = bidderOpenInfoService.updateById(BidderOpenInfo.builder()
                    .id(boi.getId())
                    .priceDetermine(Enabled.YES.getCode())
                    .build());
            result.setCode(Enabled.YES.getCode().equals(update) ? PhoneResponseState.SUCCESS : PhoneResponseState.FAIL);
            result.setMsg(Enabled.YES.getCode().equals(update) ? "报价确认成功" : "报价确认失败");
            result.setData(Enabled.YES.getCode().equals(update));
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<Boolean> startDecrypt(GetBidderDecryptParam getBidderDecryptParam) {
        RestResultVO<Boolean> result = new RestResultVO();
        try {
            Integer bidderId = getBidderDecryptParam.getBidderId();
            Integer bidSectionId = getBidderDecryptParam.getBidSectionId();
            Integer fileId = getBidderDecryptParam.getFileId();
            Integer fileType = getBidderDecryptParam.getFileType();

            if (BidderFileType.SGEF.equals(fileType)) {
                result.setCode(PhoneResponseState.FAIL);
                result.setMsg("无法解密加密文件");
                result.setData(false);
                return result;
            }

            if (BidderFileType.PHONE_SGEF.equals(fileType) && CommonUtil.isEmpty(getBidderDecryptParam.getPhoneCertNo())) {
                result.setCode(PhoneResponseState.FAIL);
                result.setMsg("手机证书为空，无法解密手机加密文件");
                result.setData(false);
                return result;
            }

            Boolean isStart = BidderFileType.GEF.equals(fileType) ? bidderService.decrypt(fileId, bidderId, bidSectionId, fileType.toString(), "", "0")
                    : BidderFileType.PHONE_SGEF.equals(fileType) ? bidderService.phoneDecrypt(fileId, bidderId, bidSectionId, getBidderDecryptParam.getPhoneCertNo())
                    : false;

            result.setCode(isStart ? PhoneResponseState.SUCCESS : PhoneResponseState.FAIL);
            result.setMsg(isStart ? "开始解密成功" : "开始解密失败");
            result.setData(isStart);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<DecryptSituationDTO> getDecryptSituation(GetBidderCommonInfo getBidderCommonInfo) {
        RestResultVO<DecryptSituationDTO> result = new RestResultVO<>();
        DecryptSituationDTO data = new DecryptSituationDTO();
        try {
            Integer bidSectionId = getBidderCommonInfo.getBidSectionId();
            Integer bidderId = getBidderCommonInfo.getBidderId();
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);

            // 查看缓存
            Integer decryptRedisStatus = (Integer) RedisUtil.get("bidSection_" + bidSectionId + "_BidFileDecrypt_" + bidderId);
            boolean isDecrypting = !CommonUtil.isEmpty(decryptRedisStatus);
            data.setDecryptStatus(isDecrypting ? FileState.ING : boi.getDecryptStatus());
            if (isDecrypting) {
                Integer queueCount = (Integer) RedisUtil.get("bidSection:" + bidSectionId);
                data.setQueueCount(queueCount);
            } else {
                // 获取解密错误信息
                String redisKey = "BID_SECTION_" + bidSectionId + "_DECRYPT_MSG:" + bidderId;
                String decryptMsgRedis = (String) RedisUtil.get(redisKey);
                if (!CommonUtil.isEmpty(decryptMsgRedis)) {
                    data.setDecryptFailMsg(decryptMsgRedis);
                    RedisUtil.delete(redisKey);
                }

                //解密完成 计算解密时间
                if (FileState.SUCCESS.equals(boi.getDecryptStatus())) {
                    data.setDecryptTime(DateTimeUtil.parseTime(DateTimeUtil.getTimeDiff(boi.getDecryptStartTime(), boi.getDecryptEndTime(), TimeFormatter.YYYY_HH_DD_HH_MM_SS) * 1000));
                }
            }
            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("请求成功");
            result.setData(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<BidSectionInformationDTO> getBidSectionInformation(Integer bidSectionId) {
        RestResultVO<BidSectionInformationDTO> result = new RestResultVO<>();
        BidSectionInformationDTO data = new BidSectionInformationDTO();

        try {
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);

            Reg reg = regService.getRegById(bidSection.getRegId());
            //关于区划所属 省市区 判断
            String tradingCenter;
            if (reg.getParentId() != -1) {
                Reg parentReg = regService.getRegById(reg.getParentId());
                tradingCenter = reg.getRegName() + "本级交易中心";
                if (parentReg.getParentId() != -1) {
                    tradingCenter = reg.getRegName() + "交易中心";
                }
            } else {
                tradingCenter = reg.getRegName() + "本级交易中心";
            }
            data.setTradingCenter(tradingCenter);

            data.setBiddingUnit(tenderProject.getTendererName());
            data.setProjectAddress(reg.getRegName());
            data.setBidOpenTime(tenderDoc.getBidOpenTime());
            data.setProjectName(tenderProject.getTenderProjectName());
            data.setProjectCode(tenderProject.getTenderProjectCode());
            data.setTenderer(tenderProject.getTendererName());
            data.setAgencyMechanism(tenderProject.getTenderAgencyName());
            data.setTenderCount(tenderDoc.getRepresentativeCount() + "人");
            data.setExpertCount(tenderDoc.getExpertCount() + "人");
            data.setTenderMethod(tenderProject.getTenderMode());
            data.setEvalMethod(tenderDoc.getEvaluationMethod());
            data.setBidSectionName(bidSection.getBidSectionName());
            data.setBidSectionCode(bidSection.getBidSectionCode());
            data.setTenderCategory(BidProtype.getProtypeChineseName(bidSection.getBidClassifyCode()));
            data.setBidContent(tenderProject.getTenderContent());
            data.setBidOpenStatus(bidSection.getBidOpenStatus());
            data.setLiveRoom(bidSection.getLiveRoom());
            data.setResumeStatus(bidSection.getResumeStatus());
            data.setAgentPhone(bidSection.getTenderAgencyPhone());
            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("项目信息获取成功！");
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("项目信息获取失败！");
        }
        return result;
    }

    @Override
    public RestResultVO<EnterSignInDTO> enterSignIn(BidSectionOpenParam bidSectionOpenParam) {
        RestResultVO<EnterSignInDTO> result = new RestResultVO<>();
        EnterSignInDTO data = new EnterSignInDTO();
        if (CommonUtil.isEmpty(bidSectionOpenParam.getBidderOrgCode())) {
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("统一社会信用代码为空");
            return result;
        }
        try {
            //投标人参标查询
            List<Bidder> bidders = bidderService.getBidder(bidSectionOpenParam.getBidderOrgCode(), bidSectionOpenParam.getBidSectionId());
            if (CommonUtil.isEmpty(bidders)) {
                data.setParticipateStatus(Status.NOT_START.getCode());
                result.setCode(PhoneResponseState.SUCCESS);
                result.setMsg("当前投标人未参标");
                result.setData(data);
                return result;
            }
            Bidder bidder = bidders.get(0);
            data.setParticipateStatus(Status.PROCESSING.getCode());
            data.setBidderId(bidder.getId());

            //投标人 文件上传状态
            BidderOpenInfo boi = bidder.getBidderOpenInfo();
            BidderFileInfo bfi = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
            if (CommonUtil.isEmpty(bfi.getReceiptId())) {
                data.setFileUploadStatus(Status.NOT_START.getCode());
                result.setCode(PhoneResponseState.SUCCESS);
                result.setMsg("当前投标人未上传投标文件");
                result.setData(data);
                return result;
            }

            //投标人回执单地址
            data.setFileUploadStatus(Status.PROCESSING.getCode());
            Fdfs receipt = fdfsService.getFdfdById(bfi.getReceiptId());
            data.setReceiptUrl(receipt.getUrl());

            //投标人签到状态
            data.setSignStatus(Status.PROCESSING.getCode().equals(boi.getUrgentSigin()) ? Status.END.getCode()
                    : Status.PROCESSING.getCode().equals(boi.getAuthentication()) ? Status.PROCESSING.getCode()
                    : Status.NOT_START.getCode());

            //是否可以签到
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionOpenParam.getBidSectionId());
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionOpenParam.getBidSectionId());
            data.setCanDoSignStatus(judgeSingTime(bidSection.getSignInStartTimeLeft(), tenderDoc.getBidOpenTime()));

            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("投标人数据获取成功");
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<String> getTenderPDF(Integer bidSectionId) {
        RestResultVO<String> result = new RestResultVO<>();
        try {
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            // 招标文件pdf
            String tenderPdfMark = File.separator + ProjectFileTypeConstant.TENDER_DOC + File.separator + tenderDoc.getDocFileId() + BidFileConstant.TENDER_DOC;
            Fdfs tenderPdf = fdfsService.getFdfsByMark(tenderPdfMark);
            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("招标PDF获取成功");
            result.setData(tenderPdf.getUrl());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public boolean verifyQRCodeInvalid(Integer bidSectionId) {
        if (bidSectionId == null) {
            return false;
        }
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String bidOpenTime = tenderDoc.getBidOpenTime();
        String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        return DateTimeUtil.compareDate(bidOpenTime, nowTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) == 1;
    }

    @Override
    public boolean verifyCompleteAuth(Integer bidderId) {
        Bidder bidder = bidderService.getBidderById(bidderId);
        Integer bidSectionId = bidder.getBidSectionId();
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        // 若已经身份认证成功或者使用了紧急签到，表示已完成认证
        return !CommonUtil.isEmpty(bidderOpenInfo) && ((bidderOpenInfo.getAuthentication() != null && bidderOpenInfo.getAuthentication() == 1)
                || (bidderOpenInfo.getUrgentSigin() != null && bidderOpenInfo.getUrgentSigin() == 1));
    }

    @Override
    public String getAuthCallBakUrl(Integer boiId, Integer optAuthWay) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String url = "";
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String ctx = request.getContextPath();
            String base = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + ctx;
            if (Enabled.YES.getCode().equals(optAuthWay)) {
                url = base + "/auth/xCall?id=" + boiId;
            } else {
                url = base + "/auth/aliCall?boiId=" + boiId;
                try {
                    url = URLEncoder.encode(url, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

        }
        return url;
    }

    @Override
    public RestResultVO<Integer> getBidderAuthResult(BidSectionOpenParam bidSectionOpenParam) {
        RestResultVO<Integer> result = new RestResultVO<>();
        try {
            if (CommonUtil.isEmpty(bidSectionOpenParam.getBidderOrgCode())) {
                result.setCode(PhoneResponseState.FAIL);
                result.setMsg("投标人ID不可为空");
                return result;
            }
            BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidSectionOpenParam.getBidderId(), bidSectionOpenParam.getBidSectionId());

            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("请求成功");
            result.setData(bidderOpenInfo.getAuthentication());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<List<BidderFileDTO>> getDecryptFile(GetBidderCommonInfo getBidderCommonInfo) {
        RestResultVO<List<BidderFileDTO>> result = new RestResultVO<>();
        List<BidderFileDTO> data = new ArrayList<>();
        try {
            BidderFileInfo bfi = bidderFileInfoService.getBidderFileInfoByBidderId(getBidderCommonInfo.getBidderId());

            if (!CommonUtil.isEmpty(bfi.getGefId())) {
                Fdfs gefFile = fdfsService.getFdfsByUpload(bfi.getGefId());
                data.add(BidderFileDTO.builder()
                        .fileId(bfi.getGefId())
                        .fileName(gefFile.getName())
                        .fileType(BidderFileType.GEF)
                        .build());
            }
            if (!CommonUtil.isEmpty(bfi.getSgefId())) {
                Fdfs sgefFile = fdfsService.getFdfsByUpload(bfi.getSgefId());
                data.add(BidderFileDTO.builder()
                        .fileId(bfi.getGefId())
                        .fileName(sgefFile.getName())
                        .fileType(PassWordConstant.PHONE_CA.equals(bfi.getCaType()) ? BidderFileType.PHONE_SGEF : BidderFileType.SGEF)
                        .build());
            }

            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("请求成功");
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<ConfirmBidderPriceDTO> getConfirmBidderPriceData(GetBidderCommonInfo getBidderCommonInfo) {
        RestResultVO<ConfirmBidderPriceDTO> result = new RestResultVO<>();
        ConfirmBidderPriceDTO data = new ConfirmBidderPriceDTO();
        try {
            Bidder bidder = bidderService.getBidderById(getBidderCommonInfo.getBidderId());
            BidderOpenInfo boi = bidder.getBidderOpenInfo();
            data.setBidPrice(boi.getBidPrice());
            data.setBigBidPrice(ConvertMoney.moneyToChinese(boi.getBidPrice()));
            data.setCompanyName(bidder.getBidderName());
            data.setSqwtrName(boi.getClientName());
            data.setIdCard(boi.getClientIdcard());
            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("请求成功");
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<Integer> getQuestionStatus(GetBidderCommonInfo getBidderCommonInfo) {
        RestResultVO<Integer> result = new RestResultVO<>();
        try {
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(getBidderCommonInfo.getBidderId(), getBidderCommonInfo.getBidSectionId());
            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("请求成功");
            result.setData(boi.getDissentStatus());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<Boolean> updateQuestionStatus(UpdateBidderParam updateBidderParam) {
        RestResultVO<Boolean> result = new RestResultVO<>();
        try {
            if (CommonUtil.isEmpty(updateBidderParam.getDissentStatus())){
                result.setCode(PhoneResponseState.FAIL);
                result.setMsg("异议状态不可为空");
                return result;
            }
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(updateBidderParam.getBidderId(), updateBidderParam.getBidSectionId());
            Integer update = bidderOpenInfoService.updateBidderOpenInfoById(BidderOpenInfo.builder()
                    .id(boi.getId())
                    .dissentStatus(updateBidderParam.getDissentStatus())
                    .build());

            result.setCode(Enabled.YES.getCode().equals(update) ? PhoneResponseState.SUCCESS : PhoneResponseState.FAIL);
            result.setMsg(Enabled.YES.getCode().equals(update) ? "修改成功" : "修改失败");
            result.setData(Enabled.YES.getCode().equals(update));
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<GroupChatDTO> getGroupChatData(GetBidderCommonInfo getBidderCommonInfo) {
        RestResultVO<GroupChatDTO> result = new RestResultVO<>();
        GroupChatDTO data = new GroupChatDTO();
        try {
            BidSection bidSection = bidSectionService.getBidSectionById(getBidderCommonInfo.getBidSectionId());
            Bidder bidder = bidderService.getBidderById(getBidderCommonInfo.getBidderId());
            BidderOpenInfo boi = bidder.getBidderOpenInfo();

            data.setRoomId(roomId + "_" + getBidderCommonInfo.getBidSectionId());
            data.setRoomName(bidSection.getBidSectionName());
            data.setUserId(bidderIdPre + "_" + getBidderCommonInfo.getBidderId());
            data.setUserName(bidder.getBidderName());
            data.setRole("2");
            data.setKickStatus(boi.getKickStatus());
            if (!CommonUtil.isEmpty(boi.getClientIdcard())) {
                String gender = boi.getClientIdcard().substring(16, 17);
                if (Integer.parseInt(gender) % 2 == 1) {
                    data.setSex(1);
                } else {
                    data.setSex(0);
                }
            } else {
                data.setSex(1);
            }
            result.setCode(PhoneResponseState.SUCCESS);
            result.setMsg("获取成功");
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    @Override
    public RestResultVO<String> getBlockchainUrl(GetBidderCommonInfo getBidderCommonInfo) {
        RestResultVO<String> result = new RestResultVO<>();
        try {
            BsnChainInfo lastBsnChainInfo = bsnChainInfoService.getLastBsnChainInfo(getBidderCommonInfo.getBidderId(), BlockchainType.BID_OPENING_RECORD);
            boolean empty = CommonUtil.isEmpty(lastBsnChainInfo);
            result.setCode(empty ? PhoneResponseState.FAIL : PhoneResponseState.SUCCESS);
            result.setMsg(empty ? "暂无区块信息" : "获取成功");
            result.setData(empty ? "" : lastBsnChainInfo.getQueryAddress());
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(PhoneResponseState.FAIL);
            result.setMsg("数据获取异常");
        }
        return result;
    }

    /**
     * 是否可以签到
     *
     * @param periodSign  签到时间段
     * @param bidOpenTime 开标时间
     * @return 0：签到时间之前 1：可以签到  2：签到时间之后
     * @throws ParseException 时间转换异常
     */
    private Integer judgeSingTime(Integer periodSign, String bidOpenTime) throws ParseException {
        int diff = 0;
        if (periodSign != null) {
            diff = periodSign * 60 * 1000;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode());
        Date parse = sdf.parse(bidOpenTime);
        String startTime = sdf.format(parse.getTime() - diff);
        String nowTime = DateUtil.formatLocalDateTime(LocalDateTime.now());
        int compareStart = DateTimeUtil.compareDate(nowTime, startTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        int compareEnd = DateTimeUtil.compareDate(nowTime, bidOpenTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        return compareStart == -1 ? Status.NOT_START.getCode()
                : (compareStart == 1 && compareEnd == -1) ? Status.PROCESSING.getCode()
                : Status.END.getCode();
    }

    /**
     * 外网文件下载
     *
     * @param urlString 下载地址
     * @param filename  文件名称
     * @param savePath  保存路径
     */
    private static void downloadFile(String urlString, String filename, String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 输入流
        InputStream is = con.getInputStream();
        ;
        //设置请求超时为5s
        con.setConnectTimeout(5 * 1000);
        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 创建文件地址
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        //文件输出流
        OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭流
        os.close();
        is.close();
    }

    /**
     * 文件上传 返回 uploadFileId
     *
     * @param filePath 文件地址
     * @param mark     上传mark
     * @return uploadFileId
     */
    private Integer fileUpload(String filePath, String mark) throws Exception {
        File file = new File(filePath);
        Fdfs fdfs = fdfsService.upload(file, mark);
        // 附件信息存库,作为文件快速索引存在
        UploadFile uploadFile = UploadFile.builder()
                .name(fdfs.getName())
                .path(mark)
                .suffix(fdfs.getSuffix())
                .byteSize(fdfs.getByteSize())
                .readSize(fdfs.getReadSize())
                .build();
        uploadFileService.insert(uploadFile);
        return uploadFile.getId();
    }

    /**
     * 获取 身份认证地址
     *
     * @param getBidderSignInfo 请求数据
     * @param update            需要更新的对象
     * @return
     */
    private String getAuthUrl(GetBidderSignInfo getBidderSignInfo, BidderOpenInfo update) {
        //生成订单码（32位）
        String orderNo = "qskj" + RandomStrUtil.getOrderNoByCunt(28);
        String url;
        //选择认证类型
        if (Enabled.YES.getCode().equals(getBidderSignInfo.getOptAuthWay())) {
            //信ID认证
            update.setTicketNo(orderNo);
            url = "/api/auth/toXinIdIdentityAuthPage?ticketId=" + orderNo
                    + "&sectionToken=" + getBidderSignInfo.getBidSectionId()
                    + "&bidderToken=" + getBidderSignInfo.getBidderId();
        } else {
            //支付宝认证
            //获取回调地址
            String callBakUrl = getAuthCallBakUrl(update.getId(), getBidderSignInfo.getOptAuthWay());
            String certifyId = alipayIdentityAuthService.authInitialize(orderNo, getBidderSignInfo.getClientIdCard(), getBidderSignInfo.getClientName(), callBakUrl);

            update.setTicketNo(certifyId);

            url = "/api/auth/toAliIdentityAuthPage?certifyId=" + certifyId
                    + "&sectionToken=" + getBidderSignInfo.getBidSectionId()
                    + "&bidderToken=" + getBidderSignInfo.getBidderId();
        }
        return url;
    }

    /**
     * 通过标段类型，顺序获取开标流程列表
     *
     * @param bidProtype 标段类型
     * @return 流程列表
     */
    public List<BidOpenProcessDTO> listBidOpenProcessByBp(BidProtype bidProtype) {
        ArrayList<BidOpenProcessDTO> result = new ArrayList<>();
        switch (bidProtype) {
            case CONSTRUCTION:
                // 施工
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.PRICE).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.FLOAT).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.BIDDER_LIST).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.DESTROY).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.CONFIRM).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.ANALYSIS).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.OPEN_TABLE).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.QUESTION).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.OPEN_END).build());
                break;
            case EPC:
                //施工总承包
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.HIGHEST_PRICE).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.BIDDER_LIST).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.DESTROY).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.CONFIRM).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.HIGHEST_ANALYSIS).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.OPEN_TABLE).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.QUESTION).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.OPEN_END).build());
                break;
            case QUALIFICATION:
                //资格预审
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.BIDDER_LIST).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.DESTROY).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.OPEN_TABLE).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.QUESTION).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.OPEN_END).build());
                break;
            case DESIGN:
            case INVESTIGATION:
            case SUPERVISION:
            case ELEVATOR:
            default:
                // 其他
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.BIDDER_LIST).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.DESTROY).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.CONFIRM).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.OPEN_TABLE).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.QUESTION).build());
                result.add(BidOpenProcessDTO.builder().process(BidOpenProcessPhone.OPEN_END).build());
                break;
        }
        return result;
    }
}
