package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.TenderProject;
import com.ejiaoyi.common.mapper.TenderProjectMapper;
import com.ejiaoyi.common.service.ITenderProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 招标项目信息 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-7-13
 */
@Service
public class TenderProjectServiceImpl extends ServiceImpl<TenderProjectMapper, TenderProject> implements ITenderProjectService {

    @Autowired
    private TenderProjectMapper tenderProjectMapper;

    @Override
    public TenderProject getTenderProjectById(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        return tenderProjectMapper.selectById(id);
    }

    @Override
    public Integer countTenderProjectByCode(String tenderProjectCode, Integer regId) {
        QueryWrapper<TenderProject> projectQuery = new QueryWrapper<>();
        projectQuery.eq(StringUtils.isNotEmpty(tenderProjectCode), "TENDER_PROJECT_CODE", tenderProjectCode)
                .eq(regId != null, "REG_ID", regId)
                .eq("DELETE_FLAG", "0");
        return tenderProjectMapper.selectCount(projectQuery);
    }
    @Override
    public TenderProject getTenderProjectByCode(String tenderProjectCode, Integer regId){
        QueryWrapper<TenderProject> projectQuery = new QueryWrapper<>();
        projectQuery.eq(StringUtils.isNotEmpty(tenderProjectCode), "TENDER_PROJECT_CODE", tenderProjectCode)
                .eq(regId != null, "REG_ID", regId)
                .eq("DELETE_FLAG", "0");
        return tenderProjectMapper.selectOne(projectQuery);
    }
}
