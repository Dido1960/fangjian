package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.CompanyUser;
import com.ejiaoyi.common.mapper.CompanyUserMapper;
import com.ejiaoyi.common.service.ICompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 企业用户 服务实现类
 * </p>
 *
 * @author langwei
 * @since 2020-08-04
 */
@Service
public class CompanyUserServiceImpl extends BaseServiceImpl implements ICompanyUserService {

    @Autowired
    private CompanyUserMapper companyUserMapper;

    @Override
    public String listCompanyUser() {
        List<CompanyUser> list = companyUserMapper.selectList(new QueryWrapper<CompanyUser>());
        return this.initJsonForLayUI(list, list.size());
    }

    @Override
    public CompanyUser getCompanyUserById(String userId) {
        CompanyUser user = companyUserMapper.selectById(userId);
        return user;
    }
}
