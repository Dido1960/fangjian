package com.ejiaoyi.common.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.constant.BidFileConstant;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.HttpResponseDTO;
import com.ejiaoyi.common.dto.clearV3.CancelBidderDTO;
import com.ejiaoyi.common.dto.clearV3.ClearProjectDTO;
import com.ejiaoyi.common.dto.clearV3.ProjectDTO;
import com.ejiaoyi.common.dto.quantity.GetToken;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.DesUtil;
import com.ejiaoyi.common.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 调用清标V3.0 服务
 *
 * @author fengjunhong
 * @version 1.0
 * @date 2021-4-21 14:31
 */
@Service
public class ClearBidV3ServiceImpl implements IClearBidV3Service {

    @Autowired
    SgServiceImpl sgService;

    @Autowired
    TenderDocServiceImpl tenderDocService;

    /**
     * 清标基础 URL
     */
    @Value("${clear-v3.apiUrl}")
    private String clearApiUrl;

    /**
     * 清标基础 URL
     */
    @Value("${clear-v3.getToken}")
    private String getToken;

    /**
     * 清标服务创建人
     */
    @Value("${clear-v3.createUser}")
    private String clearCreateUser;

    /**
     * 清标业务code 前缀
     */
    @Value("${clear-v3.ywCodePrefix}")
    private String ywCodePrefix;

    /**
     * 平台码
     */
    @Value("${clear-v3.platform}")
    private String platform;

    /**
     * api密匙
     */
    @Value("${clear-v3.apiKey}")
    private String apiKey;

    /**
     * 创建项目
     */
    @Value("${clear-v3.createProject}")
    private String createProject;

    /**
     * 价格分数通知
     */
    @Value("${clear-v3.priceScore-notify}")
    private String priceScoreNotify;

    /**
     * 更新项目信息
     */
    @Value("${clear-v3.updateProject}")
    private String updateProject;

    /**
     * 废除投标人
     */
    @Value("${clear-v3.cancelBidders}")
    private String cancelBidders;

    /**
     * 价格分计算状态
     */
    @Value("${clear-v3.priceScoreCalcStatus}")
    private String priceScoreCalcStatus;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidderFileInfoService bidderFileInfoService;

    @Autowired
    private ApiPushLogServiceImpl apiPushLogService;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private IBidderFileUploadService bidderFileUploadService;
    @Autowired
    private Environment env;

    @Override
    public String createClearServer(BidSection section, TenderDoc tenderDoc) throws Exception {
        // 业务code
        String ywCode = getYwCode(ywCodePrefix, section.getId());
        // 获得令牌
        String token = getToken();
        // 开标结束 - 推送清标所需数据
        // 1、招标工程量清单
        String quantityXmlMark = File.separator + ProjectFileTypeConstant.TENDER_DOC + File.separator + tenderDoc.getDocFileId() + BidFileConstant.ENGINEER_QUANTITY_LIST_XML;
        Fdfs quantityXml = fdfsService.getFdfsByMark(quantityXmlMark);
        // 2、投标人数据
        List<String> bidderIds = new ArrayList<>();
        List<String> bidderNames = new ArrayList<>();
        // 投标人xml路径
        List<String> bidderXmlUrls = new ArrayList<>();
        // 获取有效投标人
        List<Bidder> bidders = bidderService.listPassBidOpenBidder(section.getId());
        String outIp = env.getProperty("fdfs.address");
        String innerIp = env.getProperty("fdfs.intranet-address");
        if (StringUtils.isEmpty(innerIp)) {
            innerIp = outIp;
        }
        for (Bidder bidder : bidders) {
            bidderIds.add(String.valueOf(bidder.getId()));
            bidderNames.add(bidder.getBidderName());
            // 投标人xml url
            String tbXmlUrl = bidderFileUploadService.getTbXmlUrl(section.getId(), bidder);
            bidderXmlUrls.add(tbXmlUrl.replace(outIp, innerIp));
        }
        // 投标人主键
        String bidderIdStr = bidderIds.stream().collect(Collectors.joining(","));
        // 投标人名称（多个逗号隔开）
        String bidderNameStr = bidderNames.stream().collect(Collectors.joining(","));
        // 投标xml
        String tbXmlStr = bidderXmlUrls.stream().collect(Collectors.joining(","));

        // 4、构建数据
        ClearProjectDTO projectDTO = ClearProjectDTO.builder()
                .token(token)
                .bidSectionId(section.getId())
                .code(section.getBidSectionCode())
                .projectName(section.getBidSectionName())
                .priceScoreNotify(priceScoreNotify)
                .creatUserName(clearCreateUser)
                .ywCode(ywCode)
                .projectName(section.getBidSectionName())
                .floatPoint(tenderDoc.getFloatPoint())
                .controlPrice(tenderDoc.getControlPrice())
                .measuresControlPrice("0")
                .zbXmlUrl(quantityXml.getUrl())
                .bidderNames(bidderNameStr)
                .bidderIds(bidderIdStr)
                .bidderXmlUrls(tbXmlStr)
                .build();
        // 创建清标服务接口
        String url = clearApiUrl + createProject;

        // 推送数据
        // 数据加密
        String encyData = DesUtil.encrypt(JSONObject.toJSONString(projectDTO));
        Map param = new HashMap<>();
        param.put("token", token);
        param.put("encyData", encyData);
        HttpResponseDTO httpResponseDTO = HttpClientUtil.postRaw(url, JSONObject.toJSONString(param));
        // 解构接口响应数据
        JSONObject resultJson = JSONObject.parseObject(httpResponseDTO.getContent());

        if (!"200".equals(String.valueOf(resultJson.getString("code")))) {
            throw new Exception(String.valueOf(resultJson.getString("msg")));
        }

        // 记录业务code
        bidSectionService.updateBidSectionById(BidSection.builder()
                .id(section.getId())
                .ywCode(ywCode)
                .build());

        ApiPushLog apiPushLog = ApiPushLog.builder()
                .apiUri(url)
                .apiParams(String.valueOf(projectDTO))
                .apiRemark("创建清标服务")
                .createApiTime(LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA)))
                .responseCode(httpResponseDTO.getCode())
                .responseContent(httpResponseDTO.getContent())
                .build();
        // 记录日志
        apiPushLogService.addLog(apiPushLog);
        return ywCode;
    }

    @Override
    public void cancelBidders(BidSection section) {
        // 获得令牌
        String token = getToken();
        // 废标投标人
        List<Integer> bidderIds = new ArrayList<>();
        // 1、获取初步评审结束  ==> 废标投标人
        List<Bidder> noPassFirstBidders = null;
        // 否决投标人Id字符串
        String bidderIdStr = null;

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(section.getId());
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        boolean firstStepEnd = sgService.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
        if (firstStepEnd) {
            // 初步评审结束，发送废除投标人请求
            noPassFirstBidders = bidderService.listNoPassFirstStepBidder(section.getId());

            List<Integer> canBidderList = noPassFirstBidders.stream().map(Bidder::getId).collect(Collectors.toList());
            bidderIds.addAll(canBidderList);
            // 否决投标人Id字符串
            bidderIdStr = bidderIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
        // 3、推送数据
        try {
            String url = clearApiUrl + cancelBidders;
            // 加密
            if (CommonUtil.isEmpty(bidderIdStr)) {
                bidderIdStr = "-1";
            }
            String encyData = DesUtil.encrypt(bidderIdStr);
            CancelBidderDTO cancelBidderDTO = CancelBidderDTO.builder()
                    .token(token)
                    .ywCode(section.getYwCode())
                    .bidderIds(encyData)
                    .build();

            HttpResponseDTO httpResponseDTO = HttpClientUtil.postRaw(url, JSONObject.toJSONString(cancelBidderDTO));
            ApiPushLog apiPushLog = ApiPushLog.builder()
                    .apiUri(url)
                    .apiParams(String.valueOf(cancelBidderDTO))
                    .apiRemark("否决投标人名单")
                    .createApiTime(LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA)))
                    .responseCode(httpResponseDTO.getCode())
                    .responseContent(httpResponseDTO.getContent())
                    .build();
            // 4、记录日志
            apiPushLogService.addLog(apiPushLog);
            // 否决投标人操作，是否完成
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getToken() {
        String url = clearApiUrl + getToken;
        // 平台码、授权码
        GetToken getToken = GetToken.builder()
                .platform(platform)
                .apiKey(apiKey)
                .build();
        // 请求token
        HttpResponseDTO httpResponseDTO = HttpClientUtil.postRaw(url, JSONObject.toJSONString(getToken));
        // 解构接口响应数据
        JSONObject resultJson = JSONObject.parseObject(httpResponseDTO.getContent());
        if (String.valueOf(ApiResultCode.SUCCESS.getCode()).equals(resultJson.getString("code"))) {
            return resultJson.getString("data");
        }
        return null;
    }

    @Override
    public String getYwCode(String prefix, Integer bidSectionId) {
        return prefix + "_" + bidSectionId + "_" + DateTimeUtil.getInternetTime(TimeFormatter.YYYYHHDDHHMMSS)
                + RandomUtil.randomNumbers(6);
    }

    @Override
    public String getShowClearModule(TenderDoc tenderDoc) {
        // 0(控制价分析),1(措施费分析),2(错漏项分析),3(算术性分析),4(零负报价分析),5(取费基础分析)
        /**
         * 必选：0(控制价分析),3(算术性分析)
         *
         * 可选：2(错漏项分析),4(零负报价分析),5(取费基础分析)
         *
         * 地区选择(【金昌特有】)：1(措施费分析)
         */
        StringBuilder stringBuilder = new StringBuilder("0,3");
        // 错漏项
        Integer structureStatus = tenderDoc.getStructureStatus();
        if (Status.PROCESSING.getCode().equals(structureStatus)) {
            stringBuilder.append(",2");
        }
        // 零负报价分析
        Integer priceStatus = tenderDoc.getPriceStatus();
        if (Status.PROCESSING.getCode().equals(priceStatus)) {
            stringBuilder.append(",4");
        }
        // 取费基础
        Integer fundBasisStatus = tenderDoc.getFundBasisStatus();
        if (Status.PROCESSING.getCode().equals(fundBasisStatus)) {
            stringBuilder.append(",5");
        }
        return stringBuilder.toString();
    }

    @Override
    public void setClearModule(String ywCode, String clearModule) {
        String url = clearApiUrl + updateProject;
        String token = getToken();
        // 更新项目信息
        ProjectDTO projectDTO = ProjectDTO.builder()
                .token(token)
                .ywCode(ywCode)
                .clearModule(clearModule)
                .build();

        // 发送请求
        HttpResponseDTO httpResponseDTO = HttpClientUtil.postRaw(url, JSONObject.toJSONString(projectDTO));
        // 解构接口响应数据
        JSONObject resultJson = JSONObject.parseObject(httpResponseDTO.getContent());
        ApiPushLog apiPushLog = ApiPushLog.builder()
                .apiUri(url)
                .apiParams(String.valueOf(projectDTO))
                .apiRemark("设置项目需要清标显示模块")
                .createApiTime(LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA)))
                .responseCode(httpResponseDTO.getCode())
                .responseContent(httpResponseDTO.getContent())
                .build();
        // 4、记录日志
        apiPushLogService.addLog(apiPushLog);
    }

    @Override
    public Integer priceScoreFlag(String ywCode) {
        String url = clearApiUrl + priceScoreCalcStatus;
        String token = getToken();

        // 请求价格分计算状态
        ProjectDTO projectDTO = ProjectDTO.builder()
                .token(token)
                .ywCode(ywCode)
                .build();

        // 请求token
        HttpResponseDTO httpResponseDTO = HttpClientUtil.postRaw(url, JSONObject.toJSONString(projectDTO));
        // 解构接口响应数据
        JSONObject resultJson = JSONObject.parseObject(httpResponseDTO.getContent());
        // 解构接口响应数据
        ApiPushLog apiPushLog = ApiPushLog.builder()
                .apiUri(url)
                .apiParams(String.valueOf(projectDTO))
                .apiRemark("价格分是否计算完成")
                .createApiTime(LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA)))
                .responseCode(httpResponseDTO.getCode())
                .responseContent(httpResponseDTO.getContent())
                .build();
        // 4、记录日志
        apiPushLogService.addLog(apiPushLog);
        if (String.valueOf(ApiResultCode.SUCCESS.getCode()).equals(resultJson.getString("code"))) {
            return Integer.valueOf(resultJson.getString("data"));
        }
        return StatusEnum.WAIT.getStatus();
    }

}
