package com.ejiaoyi.agency.service.impl;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.constant.*;
import com.ejiaoyi.common.dto.*;
import com.ejiaoyi.common.dto.quantity.DoService;
import com.ejiaoyi.common.dto.quantity.GetServiceResult;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.enums.quantity.PathType;
import com.ejiaoyi.common.enums.quantity.QuantityServiceVersion;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.mapper.*;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.*;
import com.ejiaoyi.common.util.*;
import com.ejiaoyi.agency.dto.BidderCursorDto;
import com.ejiaoyi.agency.dto.TenderCursorDto;
import com.ejiaoyi.agency.service.IStaffService;
import com.ejiaoyi.agency.support.AuthUser;
import com.ejiaoyi.agency.support.CurrentUserHolder;
import com.ejiaoyi.agency.util.OnlineVoiceUtil;
import jodd.util.CollectionUtil;
import jodd.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.Get;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 代理机构（招标人） 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-07-07
 */
@Service
public class StaffServiceImpl extends BaseServiceImpl implements IStaffService {

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private ILineStatusService lineStatusService;

    @Autowired
    private RegServiceImpl regService;

    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private TenderProjectServiceImpl tenderProjectService;

    @Autowired
    private BidSectionServiceImpl bidSectionService;

    @Autowired
    private TenderDocServiceImpl tenderDocService;

    @Autowired
    private BidSectionRelateServiceImpl bidSectionRelateService;

    @Autowired
    private ClarifyAnswerServiceImpl clarifyAnswerService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidderFileInfoService bidderFileInfoService;

    @Autowired
    private ICalcScoreParamService calcScoreParamService;

    @Autowired
    private BidderQuantityServiceImpl bidderQuantityService;

    @Autowired
    private Environment env;

    @Override
    public Map<String, Object> parseProjectInfo(Integer bidFileId, String clarifyFileId, Integer regId, Integer openBidOnline, Integer remoteEvaluation, String bidOpenTime, String bidDocReferEndTime) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String customFilePath = FileUtil.getDecryptTenderDocPath(bidFileId.toString());
        FileUtil.createDir(customFilePath);
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        UploadFile gef = uploadFileService.getUploadById(bidFileId);
        String outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + gef.getSuffix();
        String localPath = FileUtil.getCustomFilePath() + gef.getPath();
        boolean unzip = false;
        try {
            // 如果招标文件本地被缓存过，将不在进行下载
            if (!new File(localPath).exists()) {
                Fdfs fdfs = fdfsService.downloadByUpload(bidFileId);
                // 文件下载
                FileUtil.writeFile(fdfs.getBytes(), outPath);
                //downloadService.multiThreadDownload(bidFilePath, outPath);
                unzip = CompressUtil.unzip(outPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
            } else {
                unzip = CompressUtil.unzip(localPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("招标文件解析失败");
        } finally {
            if (!unzip) {
                throw new CustomException("招标文件解析失败");
            } else {
                // 将所有附件上传到文件服务器
                Boolean aBoolean = fdfsService.uploadProjectFile(ProjectFileTypeConstant.TENDER_DOC, bidFileId, new File(unzipPath));
                if (!aBoolean) {
                    throw new CustomException("招标文件上传失败");
                }
            }
        }

        // 解析招标项目相关信息
        String bidRelateInfoPath = unzipPath + BidFileConstant.CONFIG_XML;
        Map<String, String> bidRelateInfo = BidXmlUtil.parseBidRelateInfo(bidRelateInfoPath);
        // 判断服务类型是否该工程类型
        if (!ServiceType.CONSTUCTION.getName().equals(bidRelateInfo.get(BidRelateConstant.SERVICE_TYPE))) {
            throw new CustomException("非房屋建设与市政基础招标文件");
        }

        // 项目类型代码
        String bidClassifyCode = bidRelateInfo.get(BidRelateConstant.TENDER_TYPE);
        BidProtype bidProtype = BidProtype.getBidProtypeName(bidClassifyCode);

        // 解析招标项目信息
        String bidProjectInfoPath = unzipPath + BidFileConstant.TENDER_INFO;
        Map<String, String> bidProjectInfo = BidXmlUtil.parseBidDetailInfo(bidProjectInfoPath, bidProtype);
        // 将招标文件中信息封装到实体类中
        Project project = Project.builder()
                .projectCode(bidProjectInfo.get(BidDetailConstant.PROJECT_CODE))
                .projectName(bidProjectInfo.get(BidDetailConstant.PROJECT_NAME))
                .regId(regId)
                .build();

        String tenderProjectCode = bidProjectInfo.get(BidDetailConstant.TENDER_PROJECT_CODE);
        String tenderProjectName = bidProjectInfo.get(BidDetailConstant.TENDER_PROJECT_NAME);
        if (StringUtil.isEmpty(tenderProjectCode)) {
            tenderProjectCode = bidProjectInfo.get(BidDetailConstant.PROJECT_CODE);
        }
        if (StringUtil.isEmpty(tenderProjectName)) {
            tenderProjectName = bidProjectInfo.get(BidDetailConstant.PROJECT_NAME);
        }
        TenderProject tenderProject = TenderProject.builder()
                .tenderProjectName(tenderProjectName)
                .tenderProjectCode(tenderProjectCode)
                .tendererName(bidProjectInfo.get(BidDetailConstant.CO_NAME_INV))
                .tenderAgencyCode(bidProjectInfo.get(BidDetailConstant.IPB_CODE))
                .tenderAgencyName(bidProjectInfo.get(BidDetailConstant.IPB_NAME))
                .tenderMode(bidProjectInfo.get(BidDetailConstant.BID_METHOD))
                .regId(regId)
                .build();

        String bidSectionCode = bidProjectInfo.get(BidDetailConstant.SECT_CODE);
        String bidSectionName = bidProjectInfo.get(BidDetailConstant.SECT_NAME);
        if (StringUtil.isEmpty(bidSectionCode)) {
            bidSectionCode = bidProjectInfo.get(BidDetailConstant.PROJECT_CODE);
        }
        if (StringUtil.isEmpty(bidSectionName)) {
            bidSectionName = bidProjectInfo.get(BidDetailConstant.PROJECT_NAME);
        }
        BidSection bidSection = BidSection.builder()
                .bidSectionCode(bidSectionCode)
                .bidSectionName(bidSectionName)
                .bidClassifyCode(bidClassifyCode)
                .remoteEvaluation(remoteEvaluation)
                .bidOpenOnline(openBidOnline)
                .regId(regId)
                .build();


        // 评标办法信息
        String evalMethodPath = unzipPath + BidFileConstant.BID_EVALUATION_METHOD;
        ParseEvalMethodDTO parseEvalMethodDTO = gradeService.addParseEvalMethodInfo(evalMethodPath);

        String xmlUid = null;
        if (BidProtype.CONSTRUCTION.equals(bidProtype)) {
            String pathType = env.getProperty("quantity.path-type");
            PathType thisPathType = PathType.getEnum(pathType);

            String url = null;
            String md5 = null;
            switch (thisPathType){
                case NETWORK:
                    // 工程量清单
                    String listXmlPath = File.separator + ProjectFileTypeConstant.TENDER_DOC + File.separator + bidFileId + BidFileConstant.ENGINEER_QUANTITY_LIST_XML;
                    //Fdfs fdfs = fdfsService.getOneByMark(listXmlPath);
                    Fdfs fdfs = fdfsService.downloadByMark(listXmlPath);
                    //xmlUid = quantityService.parseQuantity(fdfs.getUrl(), fdfs.getFileHash().toLowerCase(), PathType.NETWORK, QuantityServiceVersion.V1);
                    String outIp = env.getProperty("fdfs.address");
                    String innerIp = env.getProperty("fdfs.intranet-address");
                    if (StringUtils.isEmpty(innerIp)) {
                        innerIp = outIp;
                    }
                    url = fdfs.getUrl().replace(outIp, innerIp);
                    md5 = DigestUtils.md5Hex(fdfs.getBytes());
                    break;
                case LOCAL:
                    url = unzipPath + BidFileConstant.ENGINEER_QUANTITY_LIST_XML;
                    md5 = FileUtil.getMD5(url);
                    break;
                default:
            }

        }

        Integer representativeCount = null;
        Integer expertCount = null;
        if (StringUtil.isNotEmpty(bidProjectInfo.get(BidDetailConstant.TENDER_REPRE))) {
            representativeCount = Integer.valueOf(bidProjectInfo.get(BidDetailConstant.TENDER_REPRE));
        }

        if (StringUtil.isNotEmpty(bidProjectInfo.get(BidDetailConstant.EXPERT))) {
            expertCount = Integer.valueOf(bidProjectInfo.get(BidDetailConstant.EXPERT));
        }

        TenderDoc tenderDoc = TenderDoc.builder()
                .gradeId(parseEvalMethodDTO.getGradeIds())
                .docFileId(bidFileId)
                .bidDocReferEndTime(bidProjectInfo.get(BidDetailConstant.BID_DOC_REFER_END_TIME))
                .evaluationMethod(bidProjectInfo.get(BidDetailConstant.BID_EVAL_METHOD))
                .bidOpenTime(bidOpenTime)
                .xmlUid(xmlUid)
                .representativeCount(representativeCount)
                .expertCount(expertCount)
                .build();
        if (!CommonUtil.isEmpty(bidDocReferEndTime)) {
            tenderDoc.setBidDocReferEndTime(bidDocReferEndTime);
        }
        FileUtil.deleteFile(localPath);
        Reg reg = regService.getRegById(regId);
        map.put("project", project);
        map.put("bidSection", bidSection);
        map.put("tenderDoc", tenderDoc);
        map.put("tenderProject", tenderProject);
        map.put("clarifyFileId", clarifyFileId);
        map.put("calcScoreParam", parseEvalMethodDTO.getCalcScoreParam());
        map.put("reg", reg);
        return map;
    }

    @Override
    public boolean saveProjectInfo(ProjectInfoTemp projectInfo) throws Exception {
        Project project = Project.builder()
                .id(projectInfo.getProjectId())
                .projectCode(projectInfo.getProjectCode())
                .projectName(projectInfo.getProjectName())
                .regId(projectInfo.getRegId())
                .build();

        TenderProject tenderProject = TenderProject.builder()
                .id(projectInfo.getTenderProjectId())
                .projectId(projectInfo.getProjectId())
                .tenderProjectCode(StringUtil.isEmpty(projectInfo.getTenderProjectCode()) ? projectInfo.getProjectCode() : projectInfo.getTenderProjectCode())
                .tenderProjectName(StringUtil.isEmpty(projectInfo.getTenderProjectName()) ? projectInfo.getProjectName() : projectInfo.getTenderProjectName())
                .tenderAgencyPhone(projectInfo.getTenderAgencyPhone())
                .tendererName(projectInfo.getTendererName())
                .tenderAgencyName(projectInfo.getTenderAgencyName())
                .regId(projectInfo.getRegId())
                .tenderOrganizeForm(projectInfo.getTenderOrganizeForm())
                .tenderMode(projectInfo.getTenderMode())
                .build();

        BidSection bidSection = BidSection.builder()
                .id(projectInfo.getBidSectionId())
                .bidSectionCode(projectInfo.getBidSectionCode())
                .bidSectionName(projectInfo.getBidSectionName())
                .bidClassifyCode(projectInfo.getBidClassifyCode())
                .remoteEvaluation(projectInfo.getRemoteEvaluation())
                .bidOpenOnline(projectInfo.getBidOpenOnline())
                .regId(projectInfo.getRegId())
                .liveRoom("FJSZ" + projectInfo.getBidClassifyCode() + DateTimeUtil.getInternetTime(TimeFormatter.PAY_YYYY_HH_DD_HH_MM_SS))
                .build();

        TenderDoc tenderDoc = TenderDoc.builder()
                .id(projectInfo.getTenderDocId())
                .gradeId(projectInfo.getGradeId())
                .docFileId(projectInfo.getDocFileId())
                .bidDocReferEndTime(projectInfo.getBidDocReferEndTime())
                .bidOpenTime(projectInfo.getBidOpenTime())
                .xmlUid(projectInfo.getXmlUid())
                .representativeCount(projectInfo.getRepresentativeCount())
                .expertCount(projectInfo.getExpertCount())
                .evaluationMethod(projectInfo.getEvaluationMethod())
                .marginAmount(projectInfo.getMarginAmount())
                .build();

        CalcScoreParam calcScoreParam = projectInfo.getCalcScoreParam();

        Integer bidSectionId;
        //修改项目信息
        if (bidSection.getId() != null) {
            bidSectionId = modifyProjectInfo(project, tenderProject, tenderDoc, bidSection);
        } else {
            Integer clarifyFileId = projectInfo.getClarifyFileId();
            bidSectionId = this.saveProjectInfo(project, tenderProject, tenderDoc, bidSection, calcScoreParam);
            lineStatusService.insertLineStatusByBidSectionId(bidSectionId);
            if (clarifyFileId != null) {
                //保存项目
                UploadFile clarifyFile = uploadFileService.getUploadById(clarifyFileId);

                // 保存记录
                clarifyAnswerService.save(ClarifyAnswer.builder()
                        .bidSectionId(bidSectionId)
                        .fileType("2")
                        .bidSectionCode(bidSection.getBidSectionCode())
                        .upfilesId(clarifyFile.getId())
                        .regionCode(regService.getRegById(projectInfo.getRegId()).getRegNo())
                        .bidClassifyCode(projectInfo.getBidClassifyCode())
                        .build());
            }
        }
        return bidSectionId != null;
    }

    @Override
    public boolean checkModify(Integer bidSectionId) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        if (1 == bidSection.getDockTenderDecryStatus()) {
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);

            if (tenderDoc != null) {
                String bidOpenTime = tenderDoc.getBidOpenTime();
                String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                return DateTimeUtil.compareDate(nowTime, bidOpenTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS) != 1;
            }
        }

        return true;
    }

    @Override
    public List<String> listBidOpenProcessComplete(Integer id) {
        List<String> process = new ArrayList<>();
        BidSection section = bidSectionService.getBidSectionById(id);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(id);
        lineStatusService.updateFileUploadOrsigninStatus(id);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(id);

        if (BidProtype.EPC.getCode().equals(section.getBidClassifyCode())) {
            // 最高投标限价
            if (StringUtil.isNotEmpty(tenderDoc.getControlPrice())) {
                process.add(StaffBidOpenFlow.CONTROL_PRICE.getName());
            }
        }

        if (BidProtype.CONSTRUCTION.getCode().equals(section.getBidClassifyCode())) {
            // 招标控制价
            if (StringUtil.isNotEmpty(tenderDoc.getControlPrice())) {
                process.add(StaffBidOpenFlow.CONTROL_PRICE.getName());
            }

            // 浮动点
            if (StringUtil.isNotEmpty(tenderDoc.getFloatPoint())) {
                process.add(StaffBidOpenFlow.FLOAT_POINT.getName());
            }
        }

        // 公布投标人名单
        if (null != lineStatus && Status.END.getCode().equals(lineStatus.getPublishBidderStatus())) {
            process.add(StaffBidOpenFlow.PUBLISH_BIDDER.getName());
        }

        // 委托人身份检查
        if (null != lineStatus && Status.END.getCode().equals(lineStatus.getBidderCheckStatus())) {
            process.add(StaffBidOpenFlow.CHECK_PRINCIPAL_IDENTITY.getName());
        }

        // 文件上传及解密
        if (null != lineStatus && Status.END.getCode().equals(lineStatus.getDecryptionStatus())) {
            process.add(StaffBidOpenFlow.BIDDER_FILE_DECRYPT.getName());
            if (BidProtype.CONSTRUCTION.getCode().equals(section.getBidClassifyCode())
                    || BidProtype.EPC.getCode().equals(section.getBidClassifyCode())) {
                // 控制价分析
                process.add(StaffBidOpenFlow.CONTROL_PRICE_ANALYSIS.getName());
            }
            if (!BidProtype.QUALIFICATION.getCode().equals(section.getBidClassifyCode())) {
                process.add(StaffBidOpenFlow.FILE_CURSOR.getName());
            }
        }

        // 文件唱标
        /*String voiceFileMark = File.separator + ProjectFileTypeConstant.VOICE + File.separator + section.getId();
        List<Fdfs> fdfs = fdfsService.listFdfsByMark(voiceFileMark);
        if (fdfs != null && fdfs.size() != 0) {
            if (process.size() > 0) {
                process.add(StaffBidOpenFlow.FILE_CURSOR.getName());
            }
        }*/

        // 开标记录表
        String bidOpenRecordFileMark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + section.getId() + File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + ".pdf";
        String urlByMark = fdfsService.getUrlByMark(bidOpenRecordFileMark);
        if (StringUtil.isNotEmpty(urlByMark)) {
            process.add(StaffBidOpenFlow.BID_OPEN_RECORD.getName());
        }

        BidderFileUploadDTO bidderFileUploadDTO = bidderFileInfoService.listBidderFileUpload(id);
        if (!CommonUtil.isEmpty(bidderFileUploadDTO) && bidderFileUploadDTO.getBidderNum().equals(bidderFileUploadDTO.getUploadSuccessNum())) {
            process.add(StaffBidOpenFlow.BID_FILE_UPLOAD.getName());
        }

        // 开标结束
        if (Status.END.getCode().equals(section.getBidOpenStatus())) {
            process.add(StaffBidOpenFlow.BID_OPEN_END.getName());
            // 复会时间
            if (StringUtil.isNotEmpty(section.getResumeTime())) {
                process.add(StaffBidOpenFlow.RESUME_TIME.getName());
            }
        }

        return process;
    }

    @Override
    public JsonData tenderFileCursor(Integer bId, String tenderDto, String listBidder) {
        JsonData jsonData = new JsonData();

        List<String> list = new ArrayList<>();
        List<String> durationList = new ArrayList<>();

        Map<String, Object> map = new LinkedHashMap<>();
        // 字符串转义
        tenderDto = StringEscapeUtils.unescapeHtml4(tenderDto);
        listBidder = StringEscapeUtils.unescapeHtml4(listBidder);
        // 招标DTO
        TenderCursorDto tenderCursorDto = JSON.parseObject(tenderDto, TenderCursorDto.class);

        // 投标DTO
        List<BidderCursorDto> bidderCursorDtoList = JSON.parseArray(listBidder, BidderCursorDto.class);

        // 存放语音的目录
        String dirPath = FileUtil.getVoiceFilePath(bId.toString());

        // 存放招标信息本地语音路径
        //String localVoicePath = dirPath + File.separator + OnlineVoiceUtil.TENDER_VOICE_FILE_NAME;

        // 获取招标语音文件名
        OnlineVoiceUtil.getTenderCursor(dirPath, OnlineVoiceUtil.getStrTender(tenderCursorDto));

        // 获取当前标段下，所有语音
        List<Fdfs> fdfs = fdfsService.listFdfsByMark(ProjectFileTypeConstant.VOICE + "/" + bId + "/");
        Boolean aBoolean = false;
        if (CommonUtil.isEmpty(fdfs)) {
            // 当前标段语音不存在时，上传至fdfs
            // 追加投标语音文件名
            for (BidderCursorDto bidder : bidderCursorDtoList) {
                // 生成每个投标语音
                try {
                    JsonData bidderCursor = OnlineVoiceUtil.getBidderCursor(dirPath, bidder.getId(), OnlineVoiceUtil.getStrBidder(bidder));
                } catch (IOException e) {
                    e.printStackTrace();
                    jsonData.setCode("500");
                    jsonData.setMsg("合成语音失败");
                }
            }

            aBoolean = fdfsService.uploadProjectFile(ProjectFileTypeConstant.VOICE, bId, new File(dirPath));
            // 删除本地文件
            FileUtil.removeDir(new File(dirPath));
            if (aBoolean) {
                // 上传服务器成功后，返回url列表
                fdfs = fdfsService.listFdfsByMark(ProjectFileTypeConstant.VOICE + "/" + bId + "/");
                // 否则返回fdfs上面的url列表
                for (Fdfs f : fdfs) {
                    if (f.getMark().contains(OnlineVoiceUtil.TENDER_VOICE_FILE_NAME)) {
                        list.add(0, f.getUrl());
                        durationList.add(0, f.getWavDuration() + "");
                    } else {
                        list.add(f.getUrl());
                        durationList.add(f.getWavDuration() + "");
                    }
                }
                map.put("urls", list);
                map.put("duration", durationList);
                // 向前台返回url列表
                jsonData.setCode("200");
                jsonData.setData(map);
            } else {
                jsonData.setCode("500");
                jsonData.setMsg("语音上传服务器失败");
            }
        } else {
            fdfs = fdfsService.listFdfsByMark(ProjectFileTypeConstant.VOICE + "/" + bId + "/");
            for (Fdfs f : fdfs) {
                if (f.getMark().contains("bidder.wav")) {
                    list.add(0, f.getUrl());
                    durationList.add(0, f.getWavDuration() + "");
                } else {
                    list.add(f.getUrl());
                    durationList.add(f.getWavDuration() + "");
                }
            }
            map.put("urls", list);
            map.put("duration", durationList);
            // 向前台返回url列表
            jsonData.setCode("500");
            jsonData.setData(map);
        }
        return jsonData;
    }

    @Override
    public JsonData bidderFileCursor(Integer bidSectionId, String bidderDto) {
        JsonData jsonData = new JsonData();

        Map<String, Object> map = new LinkedHashMap<>();
        // 字符串转义
        bidderDto = StringEscapeUtils.unescapeHtml4(bidderDto);
        // 招标DTO
        BidderCursorDto bidder = JSON.parseObject(bidderDto, BidderCursorDto.class);

        // 存放语音的目录
        String dirPath = FileUtil.getVoiceFilePath(bidSectionId.toString());

        // 存放投标人本地语音路径
        String localVoicePath = dirPath + File.separator + bidder.getId() + ".wav";

        // fdfs投标人语音路径
        String bidVoiceFileName = ProjectFileTypeConstant.VOICE + "/" + bidSectionId + "/" + bidder.getId() + ".wav";

        // 获取当前投标人语音
        try {
            JsonData data = OnlineVoiceUtil.getBidderCursor(dirPath, bidder.getId(), OnlineVoiceUtil.getStrBidder(bidder));
        } catch (IOException e) {
            e.printStackTrace();
            jsonData.setCode("500");
            jsonData.setMsg("合成语音失败");
        }

        // 获取当前标段下，所有语音
        List<Fdfs> fdfs = fdfsService.listFdfsByMark(bidVoiceFileName);

        // 当前投标人语音不存在时，生成后上传再返回
        if (CommonUtil.isEmpty(fdfs)) {
            // 当前标段语音不存在时，上传至fdfs
            Boolean aBoolean = fdfsService.uploadProjectFile(ProjectFileTypeConstant.VOICE, bidSectionId, new File(localVoicePath));
            // 删除本地文件
            FileUtil.removeDir(new File(dirPath));
            if (aBoolean) {
                // 上传成功
                fdfs = fdfsService.listFdfsByMark(bidVoiceFileName);
                jsonData.setCode("200");
                jsonData.setData(fdfs.get(0).getUrl());
                map.put("urls", fdfs.get(0).getUrl());
                map.put("duration", fdfs.get(0).getWavDuration());
            } else {
                // 上传失败
                jsonData.setCode("500");
                jsonData.setMsg("语音上传服务器失败");
            }
        } else {
            // 当前投标人语音存在，直接返回url
            jsonData.setCode("200");
            map.put("urls", fdfs.get(0).getUrl());
            map.put("duration", fdfs.get(0).getWavDuration());
        }
        jsonData.setData(map);
        return jsonData;
    }

    @Override
    public Map<String, Object> getExceptionMessage(Integer bidSectionId, String step) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        Assert.notEmpty(step, "param step can not be null!");
        StaffBidOpenFlow flow = StaffBidOpenFlow.getStaffBidOpenFlowByName(step);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        // 是否是施工总承包类标段
        boolean isEPC = BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode());
        Map<String, Object> map = new HashMap<>();
        String msg = "";
        String stepName = flow.getFlowName();
        switch (flow) {
            case CONTROL_PRICE:
                msg = "当前环节招标控制价暂未设置";
                if (isEPC) {
                    msg = "当前环节最高投标限价暂未设置";
                    stepName = "最高投标限价";
                }
                break;
            case FLOAT_POINT:
                msg = "当前环节浮动点暂未设置";
                break;
            case CONTROL_PRICE_ANALYSIS:
                msg = "当前环节还有未完成";
                if (isEPC) {
                    stepName = "最高投标限价分析";
                }
                break;
            case PUBLISH_BIDDER:
            case CHECK_PRINCIPAL_IDENTITY:
            case BIDDER_FILE_DECRYPT:
            case FILE_CURSOR:
            case BID_OPEN_RECORD:
//            case BID_FILE_UPLOAD:
            case BID_OPEN_END:
            case RESUME_TIME:
                msg = "当前环节还有未完成";
                break;
            default:
                break;
        }

        if (StringUtil.isNotEmpty(msg)) {
            map.put("stepName", stepName);
            map.put("msg", msg);
        }
        return map;
    }


    /**
     * 保存项目信息
     *
     * @param project        项目信息
     * @param tenderProject  招标项目信息
     * @param tenderDoc      招标文件信息
     * @param bidSection     标段信息
     * @param calcScoreParam 计算报价得分参数信息
     * @return 标段主键id
     * @throws Exception
     */
    public Integer saveProjectInfo(Project project, TenderProject tenderProject, TenderDoc tenderDoc, BidSection bidSection, CalcScoreParam calcScoreParam) throws Exception {
        AuthUser user = CurrentUserHolder.getUser();

        project.setCreateTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));

        Integer pCount = projectService.countProjectByCode(project.getProjectCode(), project.getRegId());

        Integer pid;
        if (pCount != 0) {
            // 存在 获取pid
            pid = projectService.getProjectByCode(project.getProjectCode(), project.getRegId()).getId();
            project.setId(pid);
            projectService.updateById(project);
        } else {
            // 不存在 新增
            projectService.save(project);
            Project projectByCode = projectService.getProjectByCode(project.getProjectCode(), project.getRegId());
            pid = projectByCode.getId();
        }

        // 新增招标项目
        tenderProject.setProjectId(pid);
        tenderProject.setCreateTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));
        tenderProject.setUserId(user.getUserId());

        // 判定招标项目编号是否存在
        Integer tpCount = tenderProjectService.countTenderProjectByCode(tenderProject.getTenderProjectCode(), tenderProject.getRegId());

        Integer tid;
        TenderProject oldTenderProject = new TenderProject();
        // 招标项目存在 获取原有招标项目信息
        if (tpCount != 0) {
            oldTenderProject = tenderProjectService.getTenderProjectByCode(tenderProject.getTenderProjectCode(), tenderProject.getRegId());
        }

        if (pCount != 0 && tpCount != 0 && oldTenderProject.getProjectId().equals(pid)) {
            // 如果 招标项目编号存在 项目编号存在 且关联关系正常时 获取pid
            tid = oldTenderProject.getId();
            tenderProject.setId(tid);
            tenderProjectService.updateById(tenderProject);
        } else if ((pCount != 0 && tpCount == 0) || (pCount == 0 && tpCount == 0)) {
            // 如果 项目编号存在 招标项目编号不存在 新增招标项目
            tenderProjectService.save(tenderProject);
            tid = tenderProject.getId();
        } else {
            // 其他情况属于异常情况
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException("招标项目编号与项目编号关系异常, 项目新增失败!");
        }

        // 新增标段
        bidSection.setTenderProjectId(tid);
        bidSection.setBidOpenStatus(0);
        bidSection.setEvalStatus(0);
        bidSection.setDataFrom(0);

        Integer bCount = bidSectionService.countBidSection(bidSection);
        if (bCount != 0) {
            // 存在 则手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new CustomException("标段编号已存在, 项目新增失败!");
        }

        Integer bidSectionId;
        if (bidSection.getId() == null) {
            bidSectionService.save(bidSection);
            bidSectionId = bidSection.getId();

        } else {
            bidSectionId = bidSection.getId();
            bidSectionService.updateBidSectionById(bidSection);
        }

        // 新增或修改标段信息关系表
        BidSectionRelate bidSectionRelate = BidSectionRelate.builder()
                .bidSectionId(bidSectionId)
                .regId(bidSection.getRegId())
                .build();

        BidSectionRelate old = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        if (old != null) {
            bidSectionRelate.setAwayEvalSite(null);
            bidSectionRelate.setAwayRegId(null);
            bidSectionRelate.setHomeEvalSite(null);
            bidSectionRelate.setHomeOpenSite(null);
            bidSectionRelate.setId(old.getId());
            bidSectionRelateService.updateById(bidSectionRelate);
        } else {
            bidSectionRelateService.save(bidSectionRelate);
        }

        // 新增招标文件信息
        tenderDoc.setBidSectionId(bidSectionId);
        if (tenderDoc.getBidDocReferEndTime().length() == 16) {
            tenderDoc.setBidDocReferEndTime(tenderDoc.getBidDocReferEndTime() + ":00");
        }

        if (tenderDoc.getBidDocReferEndTime().length() == 19) {
            tenderDoc.setBidDocReferEndTime(tenderDoc.getBidDocReferEndTime());
        }

        if (tenderDoc.getBidOpenTime().length() == 16) {
            tenderDoc.setBidOpenTime(tenderDoc.getBidOpenTime() + ":00");
        }

        if (tenderDoc.getBidOpenTime().length() == 19) {
            tenderDoc.setBidOpenTime(tenderDoc.getBidOpenTime());
        }

        tenderDoc.setSubmitTime(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS));
        tenderDoc.setVersion(1);

        if (tenderDoc.getId() == null) {
            tenderDocService.save(tenderDoc);
        } else {
            tenderDocService.updateTenderDoc(tenderDoc);
        }

        // 新增评标基准价参数信息
        CalcScoreParam oldCalc = calcScoreParamService.getCalcScoreParamBySectionId(bidSectionId);
        if (oldCalc != null) {
            calcScoreParam.setId(oldCalc.getId());
            calcScoreParamService.updateCalcScoreParam(calcScoreParam);
        } else {
            if (calcScoreParam == null) {
                calcScoreParam = CalcScoreParam.builder()
                        .build();
            }

            calcScoreParam.setBidSectionId(bidSectionId);
            calcScoreParamService.insertCalcScoreParam(calcScoreParam);
        }

        return bidSectionId;
    }

    /**
     * 更新项目信息
     *
     * @param project       项目
     * @param tenderProject 招标项目
     * @param tenderDoc     招标文件
     * @param bidSection    标段
     * @throws Exception
     */
    public Integer modifyProjectInfo(Project project, TenderProject tenderProject, TenderDoc tenderDoc, BidSection bidSection) throws CustomException {
        Project projectOld = projectService.getById(project.getId());
        TenderProject tenderProjectOld = tenderProjectService.getTenderProjectById(tenderProject.getId());
        BidSection bidSectionOld = bidSectionService.getBidSectionById(bidSection.getId());

        // 判断项目编号、招标项目编号、标段编号是否发生了改变
        boolean pCode = projectOld.getProjectCode().equals(project.getProjectCode());
        boolean tpCode = tenderProjectOld.getTenderProjectCode().equals(tenderProject.getTenderProjectCode());
        boolean bidCode = bidSectionOld.getBidSectionCode().equals(bidSection.getBidSectionCode());
        if (!pCode || !tpCode || !bidCode) {
            throw new CustomException("项目编号发生改变,不能进行修改!");
        }

        // 判断标段类型是否发生改变
        if (!bidSectionOld.getBidClassifyCode().equals(bidSection.getBidClassifyCode())) {
            throw new CustomException("标段类型发生改变,不能进行修改!");
        }

        projectService.updateById(project);
        tenderProjectService.updateById(tenderProject);
        bidSectionService.updateBidSectionById(bidSection);
        if (tenderDoc.getId() != null) {
            tenderDocService.updateTenderDocById(tenderDoc);
        } else {
            tenderDocService.save(tenderDoc);
        }

        // 修改标段信息关系表 行政区划
        BidSectionRelate bidSectionRelate = BidSectionRelate.builder()
                .bidSectionId(bidSection.getId())
                .regId(bidSection.getRegId())
                .build();

        BidSectionRelate old = bidSectionRelateService.getBidSectionRelateByBSId(bidSection.getId());

        if (old != null) {
            bidSectionRelate.setAwayEvalSite(null);
            bidSectionRelate.setAwayRegId(null);
            bidSectionRelate.setHomeEvalSite(null);
            bidSectionRelate.setHomeOpenSite(null);
            bidSectionRelate.setId(old.getId());
            bidSectionRelateService.updateById(bidSectionRelate);
        } else {
            bidSectionRelateService.save(bidSectionRelate);
        }
        return bidSection.getId();
    }


    /**
     * 生成开标记录表
     *
     * @param bidSectionId
     * @return 是否生成ok
     */
    @Override
    public boolean createRecordTable(Integer bidSectionId, String bidOpenPlace) {
        try {
            //pdf本地路径
            String customPath = FileUtil.getBidOpenRecordFilePath(bidSectionId.toString());
            String fdfsUploadPath = customPath + File.separator + "fdfs";
            String outPdfPath = fdfsUploadPath + File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + "." + FileType.PDF.getSuffix();

            // 生成的单个pdf路径集合
            List<String> listPdfPaths = new ArrayList<>();
            //标段分类代码获取模板文件夹
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            String template = BidProtype.getTemplate(bidSection.getBidClassifyCode());
            String projectResourcePath = FileUtil.getProjectResourcePath();
            String ftlDirPathOfPdf = projectResourcePath + "ftl/openBid/recordTable/" + template;
            // 模板名称列表
            List<BidOpenTempName> bidOpenTempNames = BidOpenTempName.listBidOpenTempName(bidSection.getBidClassifyCode());
            // 获取生成开标记录表数据
            Map<String, Object> data = getBidOpenRecordData(bidSectionId, bidOpenPlace);
            for (BidOpenTempName bidOpenTempName : bidOpenTempNames) {
                // 现场标屏蔽（投标人签到表 + 投标文件递交时间签字表）
                if (bidSection.getBidOpenOnline() == 0 &&
                        (bidOpenTempName.getName().equals(BidOpenTempName.BIDDER_SIGN_TABLE.getName())
                                || bidOpenTempName.getName().equals(BidOpenTempName.FILE_SIGN_TABLE.getName()))) {
                    continue;
                }
                String ftlPath = ftlDirPathOfPdf + File.separator + bidOpenTempName.getName() + ".ftl";
                String pdfPath = customPath + File.separator + bidOpenTempName.getName() + "." + FileType.PDF.getSuffix();
                // pdf生成
                PDFUtil.ftlToPdfTransverse(ftlPath, pdfPath, data);
                listPdfPaths.add(pdfPath);
            }
            //合成pdf
            PDFUtil.mergePdfs(listPdfPaths, outPdfPath);

            boolean aBoolean = fdfsService.uploadProjectFile(ProjectFileTypeConstant.BID_OPEN_RECORD, bidSectionId, new File(fdfsUploadPath));
            FileUtil.removeDir(new File(customPath));
            return aBoolean;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取开标记录表所需数据
     *
     * @param bidSectionId 标段id
     * @param bidOpenPlace 开标地点
     * @return
     */
    @Override
    public Map<String, Object> getBidOpenRecordData(Integer bidSectionId, String bidOpenPlace) {
        Map<String, Object> data = new HashMap<>();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        if (bidOpenPlace != null) {
            tenderDoc.setBidOpenPlace(bidOpenPlace);
        }
        // 取解密成功，且未被标书拒绝的投标人
        List<Bidder> biddersSuccess = bidderService.listDecrySuccessBidder(bidSectionId, false);
        for (Bidder bidder : biddersSuccess) {
            String bidPrice = bidder.getBidderOpenInfo().getBidPrice();
            if (!CommonUtil.isEmpty(bidPrice)) {
                String bidPriceChinese;
                String bidPriceType = bidder.getBidderOpenInfo().getBidPriceType();
                if (CommonUtil.isEmpty(bidPriceType) || "总价".equals(bidPriceType)) {
                    bidPriceChinese = ConvertMoney.moneyToChinese(bidPrice);
                } else {
                    bidPriceChinese = bidPriceType + bidPrice;
                }
                bidder.setBidPriceChinese(bidPriceChinese);
            }
        }

        List<Bidder> allBidders = bidderService.listBidderEnabled(bidSectionId, false);

        // 开标过程被pass的投标人（解密失败、未签到和标书拒绝）
        List<Bidder> biddersFail = bidderService.listFailBidder(bidSectionId);
        // 获取当前网络时间
        String year = DateTimeUtil.getInternetTime(TimeFormatter.YYYY);
        String month = DateTimeUtil.getInternetTime(TimeFormatter.MM);
        String day = DateTimeUtil.getInternetTime(TimeFormatter.DD);

        data.put("bidSection", bidSection);
        data.put("tenderProject", tenderProject);
        data.put("tenderDoc", tenderDoc);
        data.put("allBidders", allBidders);
        data.put("bidders", biddersSuccess);
        data.put("biddersFail", biddersFail);
        data.put("date", new String[]{year, month, day});
        return data;
    }

    @Override
    public boolean checkVoicePlug() {
        HttpResponseDTO clientUtil = HttpClientUtil.get(OnlineVoiceUtil.VOICE_IP + "?url=voice.wav&text=hello");
        if (!CommonUtil.isEmpty(clientUtil)) {
            return "1".equals(clientUtil.getContent());
        }
        return false;
    }

    @Override
    public boolean thisBidVoiceExist(int bidId, int bidderId) {
        // 默认，按照标段查询
        String mark = ProjectFileTypeConstant.VOICE + "/" + bidId + "/" + OnlineVoiceUtil.TENDER_VOICE_FILE_NAME;
        // 当前台传入-1时，按照标段查询，否则按照投标人id查询
        if (bidderId != -1) {
            // 查询指定投标人语音，是否存在
            mark = ProjectFileTypeConstant.VOICE + "/" + bidId + "/" + bidderId + ".wav";
        }
        // 获取当前招标语音是否存在
        int count = fdfsService.listFdfsByMark(mark).size();
        // 存在返回true
        return count > 0;
    }

    @Override
    public boolean removeSingingVoice(Integer id) {
        Assert.notNull(id, "param id can not be null");
        String voicePath = File.separator + ProjectFileTypeConstant.VOICE + File.separator + id;
        List<Fdfs> fdfss = fdfsService.listFdfsByMark(voicePath);
        if (fdfss != null && fdfss.size() > 0) {
            for (Fdfs fdfs : fdfss) {
                fdfsService.delete(fdfs);
            }
        }
        return true;
    }

    @Override
    public void saveClarifyFileAndData(Integer id, Integer regId, UploadFile uploadFile) {
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        Reg reg = regService.getRegById(regId);

        clarifyAnswerService.save(ClarifyAnswer.builder()
                .bidSectionId(id)
                .fileType("2")
                .bidSectionCode(bidSection.getBidSectionCode())
                .upfilesId(uploadFile.getId())
                .regionCode(reg.getRegNo())
                .bidClassifyCode(bidSection.getBidClassifyCode())
                .build());
    }

    @Override
    public boolean savePaperProjectInfo(ProjectInfoTemp projectInfo) throws Exception {
        Project project = Project.builder()
                .id(projectInfo.getProjectId())
                .projectCode(projectInfo.getProjectCode())
                .projectName(projectInfo.getProjectName())
                .regId(projectInfo.getRegId())
                .build();

        TenderProject tenderProject = TenderProject.builder()
                .id(projectInfo.getTenderProjectId())
                .projectId(projectInfo.getProjectId())
                .tenderProjectCode(StringUtil.isEmpty(projectInfo.getTenderProjectCode()) ? projectInfo.getProjectCode() : projectInfo.getTenderProjectCode())
                .tenderProjectName(StringUtil.isEmpty(projectInfo.getTenderProjectName()) ? projectInfo.getProjectName() : projectInfo.getTenderProjectName())
                .tenderAgencyPhone(projectInfo.getTenderAgencyPhone())
                .tendererName(projectInfo.getTendererName())
                .tenderAgencyName(projectInfo.getTenderAgencyName())
                .regId(projectInfo.getRegId())
                .tenderOrganizeForm(projectInfo.getTenderOrganizeForm())
                .tenderMode(projectInfo.getTenderMode())
                .build();

        BidSection bidSection = BidSection.builder()
                .id(projectInfo.getBidSectionId())
                .bidSectionCode(projectInfo.getBidSectionCode())
                .bidSectionName(projectInfo.getBidSectionName())
                .bidClassifyCode(projectInfo.getBidClassifyCode())
                .bidOpenOnline(projectInfo.getBidOpenOnline())
                .regId(projectInfo.getRegId())
                .paperEval(Enabled.YES.getCode().toString())
                .liveRoom("FJSZ" + projectInfo.getBidClassifyCode() + DateTimeUtil.getInternetTime(TimeFormatter.PAY_YYYY_HH_DD_HH_MM_SS))
                .build();

        TenderDoc tenderDoc = TenderDoc.builder()
                .id(projectInfo.getTenderDocId())
                .gradeId(projectInfo.getGradeId())
                .docFileId(projectInfo.getDocFileId())
                .bidDocReferEndTime(projectInfo.getBidDocReferEndTime())
                .bidOpenTime(projectInfo.getBidOpenTime())
                .representativeCount(projectInfo.getRepresentativeCount())
                .expertCount(projectInfo.getExpertCount())
                .evaluationMethod(projectInfo.getEvaluationMethod())
                .marginAmount(projectInfo.getMarginAmount())
                .build();

        CalcScoreParam calcScoreParam = projectInfo.getCalcScoreParam();

        Integer clarifyFileId = projectInfo.getClarifyFileId();
        Integer bidSectionId = this.saveProjectInfo(project, tenderProject, tenderDoc, bidSection, calcScoreParam);
        lineStatusService.insertLineStatusByBidSectionId(bidSectionId);
        if (clarifyFileId != null) {
            //保存项目
            UploadFile clarifyFile = uploadFileService.getUploadById(clarifyFileId);

            // 保存记录
            clarifyAnswerService.save(ClarifyAnswer.builder()
                    .bidSectionId(bidSectionId)
                    .fileType("2")
                    .bidSectionCode(bidSection.getBidSectionCode())
                    .upfilesId(clarifyFile.getId())
                    .regionCode(regService.getRegById(projectInfo.getRegId()).getRegNo())
                    .bidClassifyCode(projectInfo.getBidClassifyCode())
                    .build());
        }
        return bidSectionId != null;
    }
}
