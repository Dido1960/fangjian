package com.ejiaoyi.agency.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.agency.service.IPushOpenResultService;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.HttpResponseDTO;
import com.ejiaoyi.common.dto.PushReportDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.service.impl.*;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.HttpClientUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 开标结果推送 服务实现类
 * </p>
 *
 * @author Mike
 * @since 2020-01-22
 */
@Service
public class PushOpenResultServiceImpl extends BaseServiceImpl implements IPushOpenResultService {
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
    private BidSectionServiceImpl bidSectionService;

    @Autowired
    private DockPushLogServiceImpl dockPushLogService;

    @Autowired
    private FDFSServiceImpl fdfsService;

    /**
     * 评标结果推送给酒泉
     *
     * @param bidSectionId 标段id
     */
    @Override
    public void pushOpenResultForJQ(Integer bidSectionId) throws Exception {
        // 获取需要进行推送的数据
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        if (!Enabled.YES.getCode().equals(bidSection.getDataFrom())) {
            return;
        }
        String mark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + bidSectionId +
                File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + "." + FileType.PDF.getSuffix();

        String url = fdfsService.getUrlByMark(mark);
        String reportPath = FileUtil.getCustomFilePath() + UUID.randomUUID().toString() + ".pdf";
        if (StringUtils.isNotEmpty(url)) {
            // 推送开标记录表
            PushReportDTO pushReportDTO = PushReportDTO.builder()
                    .bidNo(bidSection.getBidSectionCode())
                    .fileName(bidSection.getBidSectionName() + "开标记录表.pdf")
                    .build();
            byte[] bytes = fdfsService.downloadByUrl(url);
            FileUtil.writeFile(bytes, reportPath);
            pushOpenReportForJQ(reportPath, pushReportDTO);
        }
    }


    /**
     * 推送开标记录表
     *
     * @param reportUrl 开标记录表地址
     * @param pushReportDTO 开标记录表推送DTO
     */
    private void pushOpenReportForJQ(String reportUrl, PushReportDTO pushReportDTO) {
        String uri = jqGtXdDockUrl + "/JYTDocument/receiveReportKB";
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
                .apiRemark("酒泉交易中心开标记录表推送-国泰数据接口")
                .createApiTime(LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA)))
                .responseCode(httpResponseDTO.getCode())
                .responseContent(httpResponseDTO.getContent())
                .build();

        dockPushLogService.save(dockPushLog);
    }
}
