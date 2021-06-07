package com.ejiaoyi.common.service.impl;

import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 评标报告服务 实现
 */
@Service
public class EvalReportServiceImpl extends BaseServiceImpl implements IEvalReportService {

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private ITenderProjectService tenderProjectService;

    @Autowired
    private IRegService regService;

    @Autowired
    private IExpertUserService expertUserService;

    @Qualifier("reportServiceImpl")
    @Autowired
    private ReportServiceImpl reportService;

    @Autowired
    private ZgysServiceImpl zgysService;

    @Autowired
    private SgServiceImpl sgService;

    @Autowired
    private SjServiceImpl sjService;

    @Autowired
    private JlServiceImpl jlService;

    @Autowired
    private EpcServiceImpl epcService;

    @Override
    public Map<String, Object> getEvalReportData(Integer bidSectionId, TemplateNameEnum templateNameEnum, Integer expertId) {
        Map<String, Object> data = new HashMap<>();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        Reg reg = regService.getRegById(bidSection.getRegId());
        if (reg.getParentId() != -1 && reg.getParentId() != 1) {
            reg = regService.getRegById(reg.getParentId());
        }
        List<ExpertUser> expertUsers = expertUserService.listExpertsByBidSectionId(bidSectionId);
        //将专家数组排序：专家组长放在第一位，其他按照id升序排序
        expertUsers.sort((o1, o2) -> {
            boolean equals1 = "1".equals(o1.getIsChairman());
            boolean equals2 = "1".equals(o2.getIsChairman());
            if (equals1 && !equals2) {
                return -1;
            } else if (!equals1 && equals2) {
                return 1;
            } else {
                return o1.getId() - o2.getId();
            }
        });
        List<ExpertUser> representatives = new ArrayList<>();
        ExpertUser isChairMan = null;
        for (ExpertUser expertUser : expertUsers) {
            if ("1".equals(expertUser.getIsChairman())) {
                isChairMan = expertUser;
            }
            if (ExpertUserType.REPRESENTATIVE.getType().equals(expertUser.getCategory())) {
                representatives.add(expertUser);
            }
        }
        // 此处存放可重用的数据
        String year = DateTimeUtil.getInternetTime(TimeFormatter.YYYY);
        String month = DateTimeUtil.getInternetTime(TimeFormatter.MM);
        String day = DateTimeUtil.getInternetTime(TimeFormatter.DD);
        data.put("date", new String[]{year, month, day});
        data.put("representatives", representatives);
        data.put("isChairMan", isChairMan);
        data.put("reg", reg);
        data.put("tenderDoc", tenderDoc);
        data.put("bidSection", bidSection);
        data.put("tenderProject", tenderProject);
        data.put("expertUsers",expertUsers);
        // 获取每个模板不同的数据
        Map<String, Object> tempDataMap = getTemplateDataMap(bidSectionId, templateNameEnum);
        if (!CommonUtil.isEmpty(tempDataMap)){
            data.putAll(tempDataMap);
        }
        return data;
    }

    /**
     * 获取评标报告数据
     * @param bidSectionId 标段信息
     * @param templateNameEnum 评标报告模板
     * @return
     */
    @Override
    public Map<String,Object> getTemplateDataMap(Integer bidSectionId, TemplateNameEnum templateNameEnum) {
        // 根据标段类型，返回不同对象
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
        // 根据标段类型，返回不同对象
        switch (Objects.requireNonNull(bidProtype)){
            case CONSTRUCTION:
                // 施工
                return sgService.getSgReportData(bidSectionId,templateNameEnum);
            case QUALIFICATION:
                // 资格预审
                return zgysService.getZgysReportData(bidSectionId,templateNameEnum);
            case ELEVATOR:
            case INVESTIGATION:
            case DESIGN:
                // 设计、勘察、电梯
                return sjService.getSjReportData(bidSectionId,templateNameEnum);
            case SUPERVISION:
                // 监理
                return jlService.getJLReportData(bidSectionId,templateNameEnum);
            case EPC:
                // 施工总承包
                return epcService.getEPCReportData(bidSectionId,templateNameEnum);
            default:
                // 指定类型不存在
                return null;
        }
    }

    /**
     * 返回模板对应的数据
     * @param expertUser 评审专家
     * @param backTemplateNameEnum 模板枚举
     * @return
     */
    @Override
    public Map<String,Object> getBackTempDataMap(ExpertUser expertUser, TemplateNameEnum backTemplateNameEnum,FreeBackApply freeBackApply) {
        Map<String, Object> data = new HashMap<>();
        // 构造一个专家list
        List<ExpertUser> expertUsers = new ArrayList<>();
        expertUsers.add(expertUser);
        BidSection bidSection = bidSectionService.getBidSectionById(expertUser.getBidSectionId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        // 此处存放可重用的数据
        String year = DateTimeUtil.getInternetTime(TimeFormatter.YYYY);
        String month = DateTimeUtil.getInternetTime(TimeFormatter.MM);
        String day = DateTimeUtil.getInternetTime(TimeFormatter.DD);
        data.put("date", new String[]{year, month, day});
        data.put("expertUsers",expertUsers);
        data.put("tenderDoc", tenderDoc);
        data.put("bidSection", bidSection);
        data.put("tenderProject", tenderProject);
        data.put("freeBackApply",freeBackApply);
        // 获取每个模板不同的数据
        Map<String, Object> backApplyData = getTemplateDataMap(expertUser.getBidSectionId(), backTemplateNameEnum);
        if (!CommonUtil.isEmpty(backApplyData)){
            data.putAll(backApplyData);
        }
        return data;
    }

    /**
     * 根据标段类型，返回不同模板数据子类
     * @param bidSectionId 标段主键
     * @return
     */
    @Override
    public ReportServiceImpl getReportDataMapSubclass(Integer bidSectionId){
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        BidProtype bidProtype = BidProtype.getBidProtypeByCode(bidSection.getBidClassifyCode());
        // 根据标段类型，返回不同对象
        switch (Objects.requireNonNull(bidProtype)){
            case CONSTRUCTION:
                // 施工
                return sgService;
            case QUALIFICATION:
                // 资格预审
                return zgysService;
            case ELEVATOR:
            case INVESTIGATION:
            case DESIGN:
                // 设计、勘察、电梯
                return sjService;
            case SUPERVISION:
                // 监理
                return jlService;
            case EPC:
                // 施工总承包
                return epcService;
            default:
                // 指定类型不存在
                return null;
        }
    }

}
