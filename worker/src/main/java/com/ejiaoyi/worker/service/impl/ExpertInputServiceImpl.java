package com.ejiaoyi.worker.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.mapper.*;
import com.ejiaoyi.common.service.ITenderDocService;
import com.ejiaoyi.common.service.IWordbookService;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.service.impl.BidApplyServiceImpl;
import com.ejiaoyi.common.service.impl.WordbookServiceImpl;
import com.ejiaoyi.common.util.RandomStrUtil;
import com.ejiaoyi.worker.service.IExpertInputService;
import com.ejiaoyi.worker.support.CurrentUserHolder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020-8-24 13:45
 */
@Service
public class ExpertInputServiceImpl extends BaseServiceImpl implements IExpertInputService {
    @Autowired
    private BidSectionMapper bidSectionMapper;
    @Autowired
    private BidApplyServiceImpl bidApplyService;
    @Autowired
    private TenderDocMapper tenderDocMapper;
    @Autowired
    private ExpertUserMapper expertUserMapper;
    @Autowired
    private IWordbookService wordbookService;
    @Autowired
    private TenderProjectMapper tenderProjectMapper;
    @Autowired
    private ITenderDocService tenderDocService;

    @Override
    public String listBidSection(BidSection bidSection) {
        Page page = this.getPageForLayUI();
        List<BidSection> bidSections = bidSectionMapper.listBidSection(page, bidSection, null);
        return this.initJsonForLayUI(bidSections, (int) page.getTotal());
    }

    @Override
    public Integer getProjectTotal(BidSection bidSection) {
        return bidSectionMapper.listBidSection(bidSection, null).size();
    }

    @Override
    @RedissonLock(key = "'biApply_'+ #bidSectionId")
    public BidApply bidApplyInfo(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        List<BidApply> list = bidApplyService.listBidApply(bidSectionId);
        if (list.size() == 0) {
            BidApply bidApply = BidApply.builder().bidSectionId(bidSectionId).build();
            bidApply.setId(bidApplyService.saveBidApply(bidApply));
            return bidApply;
        }
        return list.get(0);
    }

    @Override
    public BidSection getBidSection(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        BidSection bidSection = bidSectionMapper.selectById(bidSectionId);
        QueryWrapper<TenderDoc> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("DELETE_FLAG", 1);
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        List<TenderDoc> list = tenderDocMapper.selectList(queryWrapper);
        if (list.size() > 0) {
            bidSection.setTenderDoc(list.get(0));
        }
        return bidSection;
    }

    @Override
    public List<ExpertUser> listExpertByBidSectionId(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<ExpertUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.eq("ENABLED", 1);
        return expertUserMapper.selectList(queryWrapper);
    }

    @Override
    public List<Wordbook> getExpertCategoryList() {
        return wordbookService.listWordbookByTopKey("expertCategory");
    }

    @Override
    public String addExpert(ExpertUser expertUser) {
        Assert.notNull(expertUser.getBidSectionId(), "param bidSectionId can not be null!");
        List<ExpertUser> list = this.listExpertByBidSectionId(expertUser.getBidSectionId());
        BidSection bidSection = this.getBidSection(expertUser.getBidSectionId());
        int representative = Integer.parseInt(wordbookService.getHashKey("expertCategory", "业主代表"));

        //数据封装
        expertUser.setRegId(CurrentUserHolder.getUser().getRegId());
        expertUser.setEnabled(1);
        expertUser.setLeaderStatus("1");
        expertUser.setCheckinTime(DateUtil.formatLocalDateTime(LocalDateTime.now()));
        expertUser.setAvoid("2");
        //生成六位随机数
        String randomStr = RandomStrUtil.getRandomStr(6);
        expertUser.setPassWord(randomStr);
        try {
            expertUser.setPwd(SM2Util.encrypt(randomStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String errorInfo = "添加失败";
        if (list.size() < bidSection.getTenderDoc().getExpertCount()) {
            if (expertUser.getCategory() == representative) {
                // 业主代表人数
                List<ExpertUser> representativeList = this.getRepresentativeList(expertUser.getBidSectionId());
                if (representativeList.size() < bidSection.getTenderDoc().getRepresentativeCount()) {
                    errorInfo = expertUserMapper.insert(expertUser) > 0 ? null : "添加失败";
                }else {
                    errorInfo = "业主代表已经存在";
                }

            } else {
                errorInfo = expertUserMapper.insert(expertUser) > 0 ? null : "添加失败";
            }
        }
        return errorInfo;
    }

    @Override
    public List<ExpertUser> getRepresentativeList(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        int representative = Integer.parseInt(wordbookService.getHashKey("expertCategory", "业主代表"));
        QueryWrapper<ExpertUser> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("ENABLED", 1);
        wrapper.eq("CATEGORY", representative);
        return expertUserMapper.selectList(wrapper);
    }

    @Override
    public Boolean deleteExpert(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        ExpertUser expertUser = expertUserMapper.selectById(id);
        expertUser.setEnabled(0);
        return expertUserMapper.updateById(expertUser) == 1;
    }

    @Override
    public TenderProject getTenderProject(BidSection bidSection) {
        Assert.notNull(bidSection.getTenderProjectId(), "param tenderProjectId can not be null!");
        return tenderProjectMapper.selectById(bidSection.getTenderProjectId());
    }

    @Override
    public List<BidSection> getBidSectionListForExpert(BidSection bidSection) {
        Assert.notNull(bidSection.getTenderProjectId(), "param tenderProjectId can not be null!");
        Assert.notNull(bidSection.getId(), "param id can not be null!");

        List<BidSection> list = bidSectionMapper.getBidSectionListForExpert(bidSection);
        ArrayList<BidSection> resultList = new ArrayList<>();
        for (BidSection section : list) {
            List<ExpertUser> expertList = this.listExpertByBidSectionId(section.getId());
            if (expertList.size() > 0) {
                section.setExpertList(expertList);
                resultList.add(section);
            }
        }
        return resultList;
    }

    @Override
    public Boolean addExpertList(Integer bidSectionId, Integer[] ids, Integer representativeCount) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");

        //评标记录初始化
        BidApply bidApply = this.bidApplyInfo(bidSectionId);

        int code = Integer.parseInt(wordbookService.getHashKey("expertCategory", "业主代表"));
        //当前标段招标文件信息
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        //当前标段已有专家人数
        QueryWrapper<ExpertUser> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("ENABLED", 1);
        Integer oldExpertCount = expertUserMapper.selectCount(wrapper);

        wrapper.eq("CATEGORY", code);
        //已有业主代表人数
        Integer oldRepresentativeCount = expertUserMapper.selectCount(wrapper);

        if ((oldExpertCount + ids.length) > tenderDoc.getExpertCount() || (oldRepresentativeCount + representativeCount) > tenderDoc.getRepresentativeCount()) {
            return false;
        }

        for (Integer expertId : ids) {
            ExpertUser expertUser = expertUserMapper.selectById(expertId);
            //生成六位随机数
            String randomStr = RandomStrUtil.getRandomStr(6);
            String encrypt = null;
            try {
                encrypt = SM2Util.encrypt(randomStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ExpertUser newExpert = ExpertUser.builder()
                    .regId(expertUser.getRegId())
                    .idCard(expertUser.getIdCard())
                    .enabled(1)
                    .bidSectionId(bidSectionId)
                    .bidApplyId(bidApply.getId())
                    .expertName(expertUser.getExpertName())
                    .phoneNumber(expertUser.getPhoneNumber())
                    .company(expertUser.getCompany())
                    .leaderStatus("1")
                    .pwd(encrypt)
                    .checkinTime(DateUtil.formatLocalDateTime(LocalDateTime.now()))
                    .category(expertUser.getCategory())
                    .passWord(randomStr)
                    .avoid("2")
                    .build();
            if (!this.isIdCardRepeat(newExpert.getIdCard(), newExpert.getBidSectionId())) {
                expertUserMapper.insert(newExpert);
            }
        }
        return true;
    }

    @Override
    public List<ExpertUser> searchExpert(String expertName) {
        return expertUserMapper.searchExpert(expertName);
    }

    @Override
    public Boolean isIdCardRepeat(String idCard, Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        Assert.notNull(idCard, "param idCard can not be null!");
        List<ExpertUser> list = this.listExpertByBidSectionId(bidSectionId);
        for (ExpertUser expertUser : list) {
            if (idCard.equals(expertUser.getIdCard())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean isPhoneRepeat(String phone, Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        Assert.notNull(phone, "param phone can not be null!");
        List<ExpertUser> list = this.listExpertByBidSectionId(bidSectionId);
        for (ExpertUser expertUser : list) {
            if (phone.equals(expertUser.getPhoneNumber())){
                return true;
            }
        }
        return false;
    }
}
