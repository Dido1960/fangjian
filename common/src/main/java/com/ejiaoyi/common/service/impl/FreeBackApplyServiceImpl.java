package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.constant.BackStatus;
import com.ejiaoyi.common.entity.ExpertUser;
import com.ejiaoyi.common.entity.Fdfs;
import com.ejiaoyi.common.entity.FreeBackApply;
import com.ejiaoyi.common.entity.GovUser;
import com.ejiaoyi.common.mapper.FreeBackApplyMapper;
import com.ejiaoyi.common.service.IExpertUserService;
import com.ejiaoyi.common.service.IFDFSService;
import com.ejiaoyi.common.service.IFreeBackApplyService;
import com.ejiaoyi.common.service.IGovUserService;
import com.ejiaoyi.common.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 自由回退申请 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
public class FreeBackApplyServiceImpl extends ServiceImpl<FreeBackApplyMapper, FreeBackApply> implements IFreeBackApplyService {

    @Autowired
    private FreeBackApplyMapper freeBackApplyMapper;
    @Autowired
    private IExpertUserService expertUserService;
    @Autowired
    private IGovUserService govUserService;
    @Autowired
    private IFDFSService fdfsService;

    @Override
    public FreeBackApply getFreeBackApplyById(Integer id) {
        FreeBackApply freeBackApply = freeBackApplyMapper.selectById(id);
        if (!CommonUtil.isEmpty(freeBackApply)){
            return getFreeBackAppliesBeautify(freeBackApply);
        }
        return null;
    }

    @Override
    public FreeBackApply getFreeBackApplyByBidSectionId(Integer bidSectionId) {
        QueryWrapper<FreeBackApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID",bidSectionId);
        queryWrapper.eq("CHECK_STATUS",0);
        return freeBackApplyMapper.selectOne(queryWrapper);
    }

    @Override
    public List<FreeBackApply> listFreeBackApply(Integer bidSectionId) {
        QueryWrapper<FreeBackApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID",bidSectionId);
        queryWrapper.ne("CHECK_STATUS",0);
        List<FreeBackApply> freeBackApplies = freeBackApplyMapper.selectList(queryWrapper);
        for (FreeBackApply freeBackApply : freeBackApplies) {
            ExpertUser expertUser = expertUserService.getExpertUserById(freeBackApply.getApplyUser());
            if (expertUser != null) {
                freeBackApply.setApplyUserName(expertUser.getExpertName());
            }
            if (freeBackApply.getCheckUser() != null) {
                GovUser govUser = govUserService.getGovUserById(freeBackApply.getCheckUser());
                if (govUser != null) {
                    freeBackApply.setApplyUserName(govUser.getName());
                }
            }
        }
        return freeBackApplies;
    }

    @Override
    public FreeBackApply getFreeBackAppliesBeautify(FreeBackApply freeBackApply) {
        if (!CommonUtil.isEmpty(freeBackApply)){
            // 回退处理人员姓名
            GovUser govUser = govUserService.getGovUserById(freeBackApply.getCheckUser());
            if (!CommonUtil.isEmpty(govUser)){
                freeBackApply.setCheckUserName(govUser.getName());
            }
            // 获取专家姓名
            ExpertUser expertUser = expertUserService.getExpertUserById(freeBackApply.getApplyUser());
            if (CommonUtil.isEmpty(expertUser)){
                freeBackApply.setApplyUserName("未知");
            } else {
                freeBackApply.setApplyUserName(expertUser.getExpertName());
            }
            // 历史回退数据url
            if (freeBackApply.getFreeBackAnnexId() != null) {
                Fdfs fdfs = fdfsService.getFdfdById(freeBackApply.getFreeBackAnnexId());
                if (fdfs != null) {
                    freeBackApply.setFreeBackAnnexUrl(fdfs.getUrl());
                }
            }
            return freeBackApply;
        }
        return null;
    }

    @Override
    public List<FreeBackApply> listFreeBackApplyBeautify(List<FreeBackApply> freeBackApplies) {
        List<FreeBackApply> list = new ArrayList<>();
        if (!CommonUtil.isEmpty(freeBackApplies)){
            for (FreeBackApply freeBackApply : freeBackApplies) {
                list.add(getFreeBackAppliesBeautify(freeBackApply));
            }
            // 按照插入时间排序
            return list;
        }
        return null;
    }
    @Override
    public Integer addFreeBackApply(FreeBackApply freeBackApply) {
        return freeBackApplyMapper.insert(freeBackApply);
    }

    @Override
    public List<FreeBackApply> getApplyingByBsId(Integer bidSectionId) {
        QueryWrapper<FreeBackApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.eq("CHECK_STATUS", BackStatus.UNREVIEWED);
        return freeBackApplyMapper.selectList(queryWrapper);
    }

    @Override
    public List<FreeBackApply> getApplyedByBsId(Integer bidSectionId) {
        QueryWrapper<FreeBackApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        return freeBackApplyMapper.selectList(queryWrapper);
    }

    @Override
    public FreeBackApply getApplyById(Integer id) {
        FreeBackApply freeBackApply = freeBackApplyMapper.selectById(id);
        ExpertUser expertUser = expertUserService.getExpertUserById(freeBackApply.getApplyUser());
        if (expertUser != null) {
            freeBackApply.setApplyUserName(expertUser.getExpertName());
        }
        if (freeBackApply.getCheckUser() != null) {
            GovUser govUser = govUserService.getGovUserById(freeBackApply.getCheckUser());
            if (govUser != null) {
                freeBackApply.setCheckStatusName(govUser.getName());
            }
        }
        return freeBackApply;
    }

    @Override
    public List<FreeBackApply> listAllFreeBackApply(Integer bidSectionId) {
        QueryWrapper<FreeBackApply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID",bidSectionId);
        List<FreeBackApply> freeBackApplies = freeBackApplyMapper.selectList(queryWrapper);
        for (FreeBackApply freeBackApply : freeBackApplies) {
            ExpertUser expertUser = expertUserService.getExpertUserById(freeBackApply.getApplyUser());
            if (expertUser != null) {
                freeBackApply.setApplyUserName(expertUser.getExpertName());
            }
            if (freeBackApply.getCheckUser() != null) {
                GovUser govUser = govUserService.getGovUserById(freeBackApply.getCheckUser());
                if (govUser != null) {
                    freeBackApply.setCheckStatusName(govUser.getName());
                }
            }
        }
        return freeBackApplies;
    }
}
