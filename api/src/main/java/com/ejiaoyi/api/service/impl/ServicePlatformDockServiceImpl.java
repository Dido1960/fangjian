package com.ejiaoyi.api.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.api.dto.*;
import com.ejiaoyi.api.exception.APIException;
import com.ejiaoyi.api.service.IServicePlatformDockService;
import com.ejiaoyi.api.sso.SsoLogin;
import com.ejiaoyi.common.constant.*;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.dto.ParseBidFileDTO;
import com.ejiaoyi.common.dto.ParseEvalMethodDTO;
import com.ejiaoyi.common.dto.quantity.DoService;
import com.ejiaoyi.common.dto.quantity.GetServiceResult;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.enums.quantity.PathType;
import com.ejiaoyi.common.enums.quantity.QuantityServiceVersion;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.impl.*;
import com.ejiaoyi.common.util.*;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 服务平台对接处理 服务实现类
 *
 * @author Mike
 * @since 2020/12/28
 */
@Slf4j
@Service
public class ServicePlatformDockServiceImpl extends BaseServiceImpl implements IServicePlatformDockService {

    @Value("${quantity.path-type}")
    private String pathType;
    @Value("${quantity.user-type}")
    private String userType;
    @Autowired
    private FDFSServiceImpl fdfsService;

    @Autowired
    private UploadFileServiceImpl uploadFileService;

    @Autowired
    private RegServiceImpl regService;

    @Autowired
    private GradeServiceImpl gradeService;

    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private BidSectionServiceImpl bidSectionService;

    @Autowired
    private TenderProjectServiceImpl tenderProjectService;

    @Autowired
    private BidSectionRelateServiceImpl bidSectionRelateService;

    @Autowired
    private TenderDocServiceImpl tenderDocService;

    @Autowired
    private CalcScoreParamServiceImpl calcScoreParamService;

    @Autowired
    private BidderServiceImpl bidderService;

    @Autowired
    private BidderFileInfoServiceImpl bidderFileInfoService;

    @Autowired
    private BidderOpenInfoServiceImpl bidderOpenInfoService;

    @Autowired
    private ClarifyAnswerServiceImpl clarifyAnswerService;

    @Autowired
    private ExpertUserServiceImpl expertUserService;

    @Autowired
    private BidApplyServiceImpl bidApplyService;

    @Autowired
    private WiningBidFileServiceImpl winingBidFileService;

    @Autowired
    private LineStatusServiceImpl lineStatusService;

    @Autowired
    private Environment env;

    @Override
    public UploadFile receiveDocument(MultipartFile file, String fileSM3) throws Exception {
        UploadFile uploadFile;
        //文件索引
        String path = File.separator + "uploads"
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.YYYY)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.MM)
                + File.separator + DateTimeUtil.getInternetTime(TimeFormatter.DD);

        String fileName = file.getOriginalFilename();
        String fileExt = FileUtil.getSuffix(fileName);
        String uploadRecordUUID = UUID.randomUUID().toString();
        path = path + File.separator + uploadRecordUUID + "." + fileExt.toLowerCase();

        if (!fileExt.toLowerCase().equals(FileType.GEFORTJY.getSuffix())
                && !fileExt.toLowerCase().equals(FileType.PDF.getSuffix())){
            throw new APIException(DockApiCode.FILE_VALID_ERROR_INFO, "非gef或pdf格式文件", null);
        }

        String sm3 = FileUtil.getSM3(file.getBytes());
        if (!sm3.equals(fileSM3)) {
            throw new APIException(DockApiCode.FILE_VALID_ERROR_INFO, "文件SM3值与推送内容不匹配，文件SM3值：" + sm3 + " 推送fileSM3：" + fileSM3, null);
        }
        fdfsService.upload(file, path);
        // 附件信息存库,作为文件快速索引存在
        uploadFile = UploadFile.builder()
                .fileUid(uploadRecordUUID)
                .name(fileName)
                .path(path)
                .suffix(fileExt.toLowerCase())
                .byteSize(Integer.parseInt(String.valueOf(file.getSize())))
                .readSize(FileUtil.getReadSize(file.getSize()))
                .build();
        uploadFileService.insert(uploadFile);
        uploadFile.setUrl(fdfsService.getUrlByMark(path));

        return uploadFile;
    }

    @Override
    public void receiveProjectInfo(GetProjectInfo getProjectInfo) throws Exception {
        // 获取代理信息
        CompanyUser companyUser = getCompanyUserByCode(getProjectInfo.getTenderAgencyCode());
        if (companyUser == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的招标代理信息，错误内容：" + getProjectInfo.getTenderAgencyCode(), null);
        }

        // 获取招标文件
        UploadFile uploadFile = uploadFileService.getUploadByUid(getProjectInfo.getBidFileUid());
        if (uploadFile == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的招标文件信息，错误内容：" + getProjectInfo.getBidFileUid(), null);
        }

        if (!FileType.GEFORTJY.getSuffix().equals(uploadFile.getSuffix())) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "您推送的招标文件不是GEF文件", null);
        }

        // 关联澄清答疑文件
        UploadFile clarifyFile = null;
        if (StringUtils.isNotEmpty(getProjectInfo.getClarifyFileUid())) {
            clarifyFile = uploadFileService.getUploadByUid(getProjectInfo.getClarifyFileUid());
            if (clarifyFile == null) {
                throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的澄清答疑文件信息，错误内容：" + getProjectInfo.getClarifyFileUid(), null);
            }
        }

        if (clarifyFile != null) {
            if (!FileType.PDF.getSuffix().equals(clarifyFile.getSuffix())) {
                throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "您推送的澄清答疑文件不是PDF文件", null);
            }
        }

        // 获取主场区划信息
        Reg reg = regService.getRegByRegNo(getProjectInfo.getRegCode());
        if (reg == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的主场区划信息，错误内容：" + getProjectInfo.getRegCode(), null);
        }

        // 如果是远程异地评标获取客场区划信息
        Reg awayReg = null;
        if (Enabled.YES.getCode().toString().equals(getProjectInfo.getRemoteEvaluation())) {
            awayReg = regService.getRegByRegNo(getProjectInfo.getAwayRegCode());
            if (awayReg == null) {
                throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的客场区划信息，错误内容：" + getProjectInfo.getAwayRegCode(), null);
            }
        }

        // 解析招标文件数据
        ParseBidFileDTO parseBidFileDTO = parseProjectInfo(uploadFile.getId());
        Project project = parseBidFileDTO.getProject();
        TenderProject tenderProject = parseBidFileDTO.getTenderProject();
        BidSection bidSection = parseBidFileDTO.getBidSection();
        TenderDoc tenderDoc = parseBidFileDTO.getTenderDoc();
        // 判断招标文件解析信息与推送的信息是否一致
        // 判断项目编号和标段编号一致性
        if (!project.getProjectCode().contains(getProjectInfo.getProjectCode())
                || !getProjectInfo.getBidSectionCode().equals(bidSection.getBidSectionCode())) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "项目编号或标段编号与招标文件解析内容不匹配 " +
                    "项目编号 ==> 推送：" + getProjectInfo.getProjectCode() + " <==> 文件解析：" + project.getProjectCode()
                    + " 标段编号 ==> 推送：" + getProjectInfo.getBidSectionCode() + " <==> 文件解析：" + bidSection.getBidSectionCode(), null);
        }

        // 判断标段类型一致性
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(getProjectInfo.getBidSectionClassifyCode());
        if (bidProtype == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的标段分类信息，错误内容：" + getProjectInfo.getBidSectionClassifyCode(), null);
        }

        if (!getProjectInfo.getBidSectionClassifyCode().equals(bidSection.getBidClassifyCode())) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "标段分类代码与招标文件解析内容不匹配 " +
                    "标段分类代码 ==> 推送：" + getProjectInfo.getBidSectionClassifyCode() + " <==> 文件解析：" + bidSection.getBidClassifyCode(), null);
        }

        // 封装数据
        project.setProjectCode(getProjectInfo.getProjectCode());
        project.setAddress(getProjectInfo.getAddress());
        project.setSourceFund(getProjectInfo.getSourceFund());
        project.setContributionScale(getProjectInfo.getContributionScale());
        project.setContactPerson(getProjectInfo.getContactPerson());
        project.setContactInformation(getProjectInfo.getContactInformation());
        project.setRegId(reg.getId());

        tenderProject.setRegId(reg.getId());
        tenderProject.setTenderProjectCode(getProjectInfo.getProjectCode());

        bidSection.setRemoteEvaluation(Integer.parseInt(getProjectInfo.getRemoteEvaluation()));
        bidSection.setBidOpenOnline(Enabled.YES.getCode());
        bidSection.setRegId(reg.getId());
        bidSection.setLiveRoom("FJSZ" + getProjectInfo.getBidSectionClassifyCode() + DateTimeUtil.getInternetTime(TimeFormatter.PAY_YYYY_HH_DD_HH_MM_SS));

        BidSectionRelate bidSectionRelate = BidSectionRelate.builder()
                .regId(reg.getId())
                .awayRegId(awayReg != null ? awayReg.getId() : null)
                .build();

        tenderDoc.setDocFileId(uploadFile.getId());

        parseBidFileDTO.setBidSectionRelate(bidSectionRelate);

        parseBidFileDTO.setCompanyUser(companyUser);
        Integer bidSectionId = saveProjectInfo(parseBidFileDTO);
        bidSection.setId(bidSectionId);
        saveClarifyFileInfo(bidSection, clarifyFile);
        if (bidSectionId == null) {
            throw new APIException(DockApiCode.ERROR_OTHER, "项目信息推送失败!", null);
        }
    }

    @Override
    public void receiveBidderInfo(GetBidderInfo getBidderInfo) throws Exception {
        String bidSectionCode = getBidderInfo.getBidSectionCode();
        String bidSectionClassifyCode = getBidderInfo.getBidSectionClassifyCode();
        String regionCode = getBidderInfo.getRegCode();
        String bidderOrgCode = getBidderInfo.getBidderOrgCode();
        String bidderName = getBidderInfo.getBidderName();
        String bidManager = getBidderInfo.getBidManager();
        String legalPerson = getBidderInfo.getLegalPerson();
        String phone = getBidderInfo.getPhone();

        Reg reg = regService.getRegByRegNo(regionCode);
        if (reg == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的区划信息，错误内容：" + regionCode, null);
        }

        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSectionClassifyCode);
        if (bidProtype == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的标段类型，错误内容：" + bidSectionClassifyCode, null);
        }

        Integer count = bidSectionService.countBidSection(BidSection.builder()
                .bidSectionCode(bidSectionCode)
                .regId(reg.getId())
                .bidClassifyCode(bidSectionClassifyCode)
                .build());

        if (count == 0) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配到‘" + bidSectionCode + "’标段信息", null);
        } else if (count > 1) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "匹配到了多条‘" + bidSectionCode + "’标段信息", null);
        }

        BidSection bidSection = bidSectionService.getBidSectionByCode(bidSectionCode, bidSectionClassifyCode, reg.getId());

        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());
        int compare = DateTimeUtil.compareDate(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), tenderDoc.getBidOpenTime(), TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        if (compare != -1) {
            throw new APIException(DockApiCode.ERROR_OTHER, "开标时间已到，不再接收投标人信息，开标时间为：" + tenderDoc.getBidOpenTime(), null);
        }

        Bidder oldBidder = bidderService.getBidder(Bidder.builder()
                .bidderOrgCode(bidderOrgCode)
                .bidSectionId(bidSection.getId())
                .build());
        if (oldBidder == null) {
            // 加入悲观锁，防止数据重复插入
            String bidderKey = "receiveBidderInfo_" + bidSection.getId() + "_" + bidderOrgCode;
            RedissonUtil.lock(bidderKey);
            Bidder bidder = Bidder.builder()
                    .bidderName(bidderName)
                    .bidderOrgCode(bidderOrgCode)
                    .bidManager(bidManager)
                    .legalPerson(legalPerson)
                    .phone(phone)
                    .dataFrom(1)
                    .bidSectionId(bidSection.getId())
                    .build();
            Integer bidderId = bidderService.saveBidder(bidder);
            if (bidderId != null) {
                BidderOpenInfo bidderOpenInfo = BidderOpenInfo.builder()
                        .bidSectionId(bidSection.getId())
                        .bidderId(bidderId)
                        .build();

                // 新增
                BidderFileInfo bidderFileInfo = BidderFileInfo.builder()
                        .bidderId(bidderId)
                        .build();
                BidderFileInfo initBidderFile = bidderFileInfoService.initBidderFileInfo(bidderFileInfo);
                boolean isJoinBid = bidderOpenInfoService.insert(bidderOpenInfo) > 0 && initBidderFile != null;
                if (!isJoinBid) {
                    throw new APIException(DockApiCode.ERROR_OTHER, "投标人报名信息推送失败!", null);
                }
            }
            // 释放悲观锁
            RedissonUtil.unlock(bidderKey);
        } else {
            throw new APIException(DockApiCode.ERROR_OTHER, "投标人已被推送，无需重复推送!", null);
        }
    }

    @Override
    public void receiveMarginInfo(GetBidderMarginInfo getBidderMarginInfo) throws Exception {
        String bidSectionCode = getBidderMarginInfo.getBidSectionCode();
        String bidSectionClassifyCode = getBidderMarginInfo.getBidSectionClassifyCode();
        String regionCode = getBidderMarginInfo.getRegCode();
        String bidderOrgCode = getBidderMarginInfo.getBidderOrgCode();
        String marginReceiveWay = getBidderMarginInfo.getMarginReceiveWay();
        String marginReceiveStatus = getBidderMarginInfo.getMarginReceiveStatus();
        String marginReceiveTime = getBidderMarginInfo.getMarginReceiveTime();

        Reg reg = regService.getRegByRegNo(regionCode);
        if (reg == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的区划信息，错误内容：" + regionCode, null);
        }

        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSectionClassifyCode);
        if (bidProtype == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的标段类型，错误内容：" + bidSectionClassifyCode, null);
        }

        Integer count = bidSectionService.countBidSection(BidSection.builder()
                .bidSectionCode(bidSectionCode)
                .regId(reg.getId())
                .bidClassifyCode(bidSectionClassifyCode)
                .build());

        if (count == 0) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配到‘" + bidSectionCode + "’标段信息", null);
        } else if (count > 1) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "匹配到了多条‘" + bidSectionCode + "’标段信息", null);
        }

        BidSection bidSection = bidSectionService.getBidSectionByCode(bidSectionCode, bidSectionClassifyCode, reg.getId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());
        int compare = DateTimeUtil.compareDate(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), tenderDoc.getBidOpenTime(), TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        if (compare != -1) {
            throw new APIException(DockApiCode.ERROR_OTHER, "开标时间已到，不再接收投标人保证金信息，开标时间为：" + tenderDoc.getBidOpenTime(), null);
        }
        Bidder bidder = bidderService.getBidder(Bidder.builder()
                .bidderOrgCode(bidderOrgCode)
                .bidSectionId(bidSection.getId())
                .build());
        if (bidder == null) {
            throw new APIException(DockApiCode.NO_DATA, "该标段未匹配到统一社会信用代码为" + bidderOrgCode + "投标人信息!", null);
        }

        MarginWayEnum marginWayCode = MarginWayEnum.getMarginWayCode(marginReceiveWay);
        if (marginWayCode == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的保证金缴纳方式，错误内容：" + marginReceiveWay, null);
        }

        BidderOpenInfo old = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSection.getId());
        BidderOpenInfo bidderOpenInfo = BidderOpenInfo.builder()
                .id(old.getId())
                .build();
        if (Enabled.YES.getCode().toString().equals(marginReceiveStatus)) {
            if (MarginWayEnum.MARGIN.getCode().equals(marginReceiveWay)) {
                bidderOpenInfo.setMarginPayStatus(1);
            } else if (MarginWayEnum.GUARANTEE.getCode().equals(marginReceiveWay)) {
                bidderOpenInfo.setMarginPayStatus(2);
            }
        } else {
            bidderOpenInfo.setMarginPayStatus(0);
        }

        bidderOpenInfoService.updateBidderOpenInfo(bidderOpenInfo);
    }

    @Override
    public void receiveExpertInfo(GetExpertInfo getExpertInfo) throws Exception {
        String bidSectionCode = getExpertInfo.getBidSectionCode();
        String bidSectionClassifyCode = getExpertInfo.getBidSectionClassifyCode();
        String regionCode = getExpertInfo.getRegCode();
        String expertName = getExpertInfo.getExpertName();
        String expertRegCode = getExpertInfo.getExpertRegCode();
        String phone = getExpertInfo.getPhone();
        String idCard = getExpertInfo.getIdCard();
        String company = getExpertInfo.getCompany();
        String category = getExpertInfo.getCategory();
        String dataType = getExpertInfo.getDataType();
        String enabled = getExpertInfo.getEnabled();
        String unavailableReason = getExpertInfo.getUnavailableReason();

        Reg reg = regService.getRegByRegNo(regionCode);
        if (reg == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的区划信息，错误内容：" + regionCode, null);
        }

        Reg expertReg = regService.getRegByRegNo(expertRegCode);
        if (expertReg == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的专家评标行政区划信息，错误内容：" + expertRegCode, null);
        }

        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSectionClassifyCode);
        if (bidProtype == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的标段类型，错误内容：" + bidSectionClassifyCode, null);
        }

        ExpertDataTypeEnum expertDataType = ExpertDataTypeEnum.getExpertDataTypeCode(dataType);
        if (expertDataType == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的数据类型，错误内容：" + dataType, null);
        }

        // 专家不可用原因
        if (Enabled.NO.getCode().toString().equals(enabled) && StringUtils.isEmpty(unavailableReason)) {

            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "您推送的专家enable=0,请说明不可用原因", null);
        }

        // 专家不可用原因
        ExpertCategoryEnum expertCategory = ExpertCategoryEnum.getExpertCategoryCode(Integer.parseInt(category));
        if (expertCategory == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "专家类别传值错误，错误内容：" + category, null);
        }

        Integer count = bidSectionService.countBidSection(BidSection.builder()
                .bidSectionCode(bidSectionCode)
                .regId(reg.getId())
                .bidClassifyCode(bidSectionClassifyCode)
                .build());

        if (count == 0) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配到‘" + bidSectionCode + "’标段信息", null);
        } else if (count > 1) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "匹配到了多条‘" + bidSectionCode + "’标段信息", null);
        }

        BidSection bidSection = bidSectionService.getBidSectionByCode(bidSectionCode, bidSectionClassifyCode, reg.getId());

        ExpertUser oldExpert = expertUserService.getExpertUser(ExpertUser.builder()
                .idCard(idCard)
                .bidSectionId(bidSection.getId())
                .phoneNumber(phone)
                .expertName(expertName)
                .build());
        if (oldExpert == null) {
            Integer bidApplyId;
            BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSection.getId());
            if (bidApply == null) {
                bidApply = BidApply.builder()
                        .bidSectionId(bidSection.getId())
                        .voteCount(1)
                        .build();
                bidApplyId = bidApplyService.saveBidApply(bidApply);
            } else {
                bidApplyId = bidApply.getId();
            }
            // 加入悲观锁，防止数据重复插入
            String expertKey = "receiveExpertInfo_" + bidSection.getId() + "_" + idCard;
            RedissonUtil.lock(expertKey);
            String randomStr = RandomStrUtil.getRandomStr(6);
            ExpertUser expertUser = null;
            try {
                expertUser = ExpertUser.builder()
                        .expertName(expertName)
                        .bidApplyId(bidApplyId)
                        .bidSectionId(bidSection.getId())
                        .regId(expertReg.getId())
                        .idCard(idCard)
                        .phoneNumber(phone)
                        .category(expertCategory.getCode())
                        .company(company)
                        .enabled(Integer.valueOf(enabled))
                        .unavailableReason(unavailableReason)
                        .dataType(dataType)
                        .checkinTime(DateUtil.formatLocalDateTime(LocalDateTime.now()))
                        .leaderStatus(Enabled.YES.getCode().toString())
                        .avoid("2")
                        .passWord(randomStr)
                        .pwd(SM2Util.encrypt(randomStr))
                        .build();
                expertUserService.saveExpert(expertUser);
            } catch (Exception e) {
                e.printStackTrace();
                throw new APIException(DockApiCode.ERROR_OTHER, "评标专家信息推送失败!", null);
            } finally {
                // 释放悲观锁
                RedissonUtil.unlock(expertKey);
            }

        } else {
            if (oldExpert.getEnabled().toString().equals(enabled)) {
                throw new APIException(DockApiCode.ERROR_OTHER, "评标专家信息已被推送，无需重复推送!", null);
            }

            expertUserService.updateExpertById(ExpertUser.builder()
                    .id(oldExpert.getId())
                    .enabled(Integer.valueOf(enabled))
                    .unavailableReason(unavailableReason)
                    .build());
        }
    }

    @Override
    public void receiveWiningBidDocument(GetWiningBidFileInfo getWiningBidFileInfo) throws Exception {
        String regionCode = getWiningBidFileInfo.getRegCode();
        String bidSectionClassifyCode = getWiningBidFileInfo.getBidSectionClassifyCode();
        String bidSectionCode = getWiningBidFileInfo.getBidSectionCode();

        // 校验区划信息是否存在
        Reg reg = regService.getRegByRegNo(regionCode);
        if (reg == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的区划信息，错误内容：" + regionCode, null);
        }

        // 校验标段分类是否合法
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSectionClassifyCode);
        if (bidProtype == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的标段类型，错误内容：" + bidSectionClassifyCode, null);
        }

        // 校验标段信息是否存在
        Integer count = bidSectionService.countBidSection(BidSection.builder()
                .bidSectionCode(bidSectionCode)
                .regId(reg.getId())
                .bidClassifyCode(bidSectionClassifyCode)
                .build());

        if (count == 0) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配到‘" + bidSectionCode + "’标段信息", null);
        } else if (count > 1) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "匹配到了多条‘" + bidSectionCode + "’标段信息", null);
        }

        BidSection bidSection = bidSectionService.getBidSectionByCode(bidSectionCode, bidSectionClassifyCode, reg.getId());
        UploadFile uploadFile = uploadFileService.getUploadByUid(getWiningBidFileInfo.getWiningBidFileUid());
        if (uploadFile == null) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "未匹配相应的中标通知书文件信息，错误内容：" + getWiningBidFileInfo.getWiningBidFileUid(), null);
        }

        if (!FileType.PDF.getSuffix().equals(uploadFile.getSuffix())) {
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "您推送的中标通知书文件不是PDF文件", null);
        }

        // 保存中标通知书信息
        winingBidFileService.save(WiningBidFile.builder()
                .bidSectionId(bidSection.getId())
                .winingBidFileId(uploadFile.getId())
                .build());
    }

    /**
     * 招标文件解析
     * @param bidFileId 招标文件id
     * @return 解析后的招标文件信息
     * @throws Exception
     */
    private ParseBidFileDTO parseProjectInfo(Integer bidFileId) throws Exception {
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
                //downloadService.multiThreadDownload(bidFilePath, outPath, 3);
                unzip = CompressUtil.unzip(outPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
            } else {
                unzip = CompressUtil.unzip(localPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
            }
        } catch (Exception e) {
            throw new APIException(DockApiCode.FILE_VALID_ERROR_INFO, "招标文件解析失败", null);
        } finally {
            if (!unzip) {
                throw new APIException(DockApiCode.FILE_VALID_ERROR_INFO, "招标文件解析失败", null);
            } else {
                // 将所有附件上传到文件服务器
                Boolean aBoolean = fdfsService.uploadProjectFile(ProjectFileTypeConstant.TENDER_DOC, bidFileId, new File(unzipPath));
                if (!aBoolean) {
                    throw new APIException(DockApiCode.FILE_VALID_ERROR_INFO, "招标文件解析失败", null);
                }
            }
        }

        // 解析招标项目相关信息
        String bidRelateInfoPath = unzipPath + BidFileConstant.CONFIG_XML;
        Map<String, String> bidRelateInfo = BidXmlUtil.parseBidRelateInfo(bidRelateInfoPath);
        // 判断服务类型是否该工程类型
        if (!ServiceType.CONSTUCTION.getName().equals(bidRelateInfo.get(BidRelateConstant.SERVICE_TYPE))) {
            throw new APIException(DockApiCode.FILE_VALID_ERROR_INFO, "非房屋建设与市政基础招标文件", null);
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
                .build();

        // 评标办法信息
        String evalMethodPath = unzipPath + BidFileConstant.BID_EVALUATION_METHOD;
        ParseEvalMethodDTO parseEvalMethodDTO = gradeService.addParseEvalMethodInfo(evalMethodPath);

        String xmlUid = null;
        if (BidProtype.CONSTRUCTION.equals(bidProtype)) {
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
            // xmlUid = quantityService.parseQuantity(fdfs.getUrl(), fdfs.getFileHash().toLowerCase(), PathType.NETWORK, QuantityServiceVersion.V1);
//            String serviceId = quantityService.registerQuantity(url, md5, thisPathType, QuantityServiceVersion.V1);
            GetServiceResult getServiceResult = new GetServiceResult();
            getServiceResult.setApiKey(env.getProperty("api.quantity.api-key"));
            getServiceResult.setPlatform(env.getProperty("api.quantity.platform"));
            getServiceResult.setServerCode(env.getProperty("api.quantity.server-code"));
            getServiceResult.setVersion(QuantityServiceVersion.V1.getCode());

//            // 10分钟内未解析成功，抛出异常
//            int i = 1;
//            // 是否需要执行清单解析
//            boolean isDo = true;
//            boolean doStatus = false;
//            while (i <= 60){
//                if (isDo) {
//                    doStatus = quantityService.doQuantity(serviceId);
//                    if (doStatus) {
//                        isDo = false;
//                    }
//                }
//                // 执行了解析清单，再获取解析结果
//                if (doStatus) {
//                    String token = quantityService.getToken();
//                    getServiceResult.setToken(token);
//                    getServiceResult.setServiceSerialNumber(serviceId);
//                    String result = quantityService.getParseQuantityResult(getServiceResult);
//                    if (StringUtils.isNotEmpty(result)) {
//                        xmlUid = result;
//                        break;
//                    }
//                }
//
//                i++;
//                Thread.sleep(10000);
//            }
//            if (StringUtils.isEmpty(xmlUid)) {
//                throw new CustomException("工程量清单服务器解析超时！");
//            }
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
                .bidDocReferEndTime(bidProjectInfo.get(BidDetailConstant.BID_OPEN_TIME))
                .bidOpenTime(bidProjectInfo.get(BidDetailConstant.BID_OPEN_TIME))
                .xmlUid(xmlUid)
                .representativeCount(representativeCount)
                .marginAmount(bidProjectInfo.get(BidDetailConstant.MARGIN_AMOUNT))
                .evaluationMethod(bidProjectInfo.get(BidDetailConstant.BID_EVAL_METHOD))
                .expertCount(expertCount)
                .build();

        FileUtil.removeDir(new File(customFilePath));
        return ParseBidFileDTO.builder()
                .project(project)
                .tenderProject(tenderProject)
                .bidSection(bidSection)
                .tenderDoc(tenderDoc)
                .calcScoreParam(parseEvalMethodDTO.getCalcScoreParam())
                .build();
    }

    /**
     * 保存项目信息
     *
     * @param parseBidFileDTO 解析招标文件信息
     * @return 标段主键id
     * @throws Exception
     */
    private Integer saveProjectInfo(ParseBidFileDTO parseBidFileDTO) {
        Project project = parseBidFileDTO.getProject();
        TenderProject tenderProject = parseBidFileDTO.getTenderProject();
        BidSection bidSection = parseBidFileDTO.getBidSection();
        TenderDoc tenderDoc = parseBidFileDTO.getTenderDoc();
        CalcScoreParam calcScoreParam = parseBidFileDTO.getCalcScoreParam();
        BidSectionRelate bidSectionRelate = parseBidFileDTO.getBidSectionRelate();
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
        tenderProject.setUserId(parseBidFileDTO.getCompanyUser().getId());

        Integer tpCount = tenderProjectService.countTenderProjectByCode(tenderProject.getTenderProjectCode(),tenderProject.getRegId());

        Integer tid;
        TenderProject oldTenderProject = new TenderProject();
        // 招标项目存在 获取原有招标项目信息
        if (tpCount != 0) {
            oldTenderProject = tenderProjectService.getTenderProjectByCode(tenderProject.getTenderProjectCode(),tenderProject.getRegId());
        }

        if (pCount != 0 && tpCount != 0 && oldTenderProject.getProjectId().equals(pid)) {
            // 如果 招标项目编号存在 项目编号存在 且关联关系正常时 获取pid
            tid = oldTenderProject.getId();
            tenderProject.setId(tid);
            tenderProjectService.updateById(tenderProject);
        } else if ((pCount != 0 && tpCount == 0) || (pCount == 0 && tpCount == 0)) {
            // 如果 项目编号存在 招标项目编号不存在 新增招标项目
            tenderProjectService.save(tenderProject);
            TenderProject tenderProjectByCode = tenderProjectService.getTenderProjectByCode(tenderProject.getTenderProjectCode(), tenderProject.getRegId());
            tid = tenderProjectByCode.getId();
        } else {
            // 其他情况属于异常情况
            log.error("招标项目编号与项目编号关系异常, 项目新增失败!");
            throw new APIException(DockApiCode.AGRS_VALIDATION_FAILS, "项目信息推送失败!", null);
        }

        // 新增标段
        bidSection.setTenderProjectId(tid);
        bidSection.setBidOpenStatus(0);
        bidSection.setEvalStatus(0);
        bidSection.setDataFrom(1);

        Integer bCount = bidSectionService.countBidSection(bidSection);
        Integer bidSectionId;
        if (bCount == 0) {
            bidSectionId = bidSectionService.save(bidSection);
            lineStatusService.insertLineStatusByBidSectionId(bidSectionId);
        } else {
            BidSection oldSection = bidSectionService.getBidSectionByCode(bidSection.getBidSectionCode(), bidSection.getBidClassifyCode(), bidSection.getRegId());
            if (Status.END.getCode().equals(oldSection.getBidOpenStatus())) {
                log.error("该项目已开标结束，不再接收项目推送!【" + oldSection.getBidSectionCode() + "】");
                throw new APIException(DockApiCode.ERROR_OTHER, "该项目已开标结束，不再接收项目推送!【" + oldSection.getBidSectionCode() + "】", null);
            }
            if (Enabled.NO.getCode().equals(oldSection.getDataFrom())) {
                log.error("该项目已在本系统建立，不再接收项目推送!【" + oldSection.getBidSectionCode() + "】");
                throw new APIException(DockApiCode.ERROR_OTHER, "该项目已在本系统建立，不再接收项目推送!【" + oldSection.getBidSectionCode() + "】", null);
            }
            bidSectionId = oldSection.getId();
            bidSection.setId(bidSectionId);
            bidSectionService.updateBidSectionById(bidSection);
        }

        // 新增或修改标段信息关系表
        bidSectionRelate.setBidSectionId(bidSectionId);

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
        TenderDoc oldTenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);

        if (oldTenderDoc == null) {
            tenderDocService.save(tenderDoc);
        } else {
            tenderDocService.updateTenderDoc(tenderDoc);
        }

        // 新增评标基准价参数信息
        CalcScoreParam oldCalc = calcScoreParamService.getCalcScoreParamBySectionId(bidSectionId);
        if (oldCalc != null) {
            if (calcScoreParam != null) {
                calcScoreParam.setId(oldCalc.getId());
                calcScoreParamService.updateCalcScoreParam(calcScoreParam);
            }
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
     * 通过统一社会信用代码查询企业信息
     * @param unifiedSocialCreditCode 统一社会信用代码
     * @return
     */
    private CompanyUser getCompanyUserByCode(String unifiedSocialCreditCode){
        return JSONObject.parseObject(JSONObject.toJSONString(new SsoLogin().getUserInfoByCode(unifiedSocialCreditCode)), CompanyUser.class);
    }

    /**
     * 标段关联澄清文件
     * @param bidSection 标段信息
     * @param clarifyFile 澄清答疑文件
     */
    private void saveClarifyFileInfo(BidSection bidSection, UploadFile clarifyFile) {
        if (clarifyFile != null) {
            clarifyAnswerService.save(ClarifyAnswer.builder()
                    .bidSectionId(bidSection.getId())
                    .fileType("2")
                    .bidSectionCode(bidSection.getBidSectionCode())
                    .upfilesId(clarifyFile.getId())
                    .regionCode(regService.getRegById(bidSection.getRegId()).getRegNo())
                    .bidClassifyCode(bidSection.getBidClassifyCode())
                    .build());
        }
    }
}
