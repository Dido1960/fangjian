package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.SignatureConfigInfo;
import com.ejiaoyi.common.mapper.SignatureConfigInfoMapper;
import com.ejiaoyi.common.service.ISignatureConfigInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 签章配置信息（回执单签章） 服务实现类
 * </p>
 *
 * @author Mike
 * @since 2021-02-18
 */
@Service
public class SignatureConfigInfoServiceImpl extends BaseServiceImpl<SignatureConfigInfo> implements ISignatureConfigInfoService {
    @Autowired
    private SignatureConfigInfoMapper signatureConfigInfoMapper;

    @Override
    public SignatureConfigInfo getSignatureConfigInfoByRegNo(String regNo) {
        Assert.notNull(regNo, "param regNo can not be null!");
        QueryWrapper<SignatureConfigInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("REG_NO", regNo);
        return signatureConfigInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public String pagedSignatureConfigInfo(SignatureConfigInfo signatureConfigInfo) {
        Page page = this.getPageForLayUI();
        List<SignatureConfigInfo> list = signatureConfigInfoMapper.pagedSignatureConfigInfo(page,signatureConfigInfo);
        return this.initJsonForLayUI(list,(int)page.getTotal());
    }

    @Override
    public Integer updateSignatureConfigInfo(SignatureConfigInfo signatureConfigInfo) {
        return signatureConfigInfoMapper.updateById(signatureConfigInfo);
    }

    @Override
    public Integer addSignatureConfigInfo(SignatureConfigInfo signatureConfigInfo) {
        return signatureConfigInfoMapper.insert(signatureConfigInfo);
    }

    @Override
    public Integer deleteSignatureConfigInfo(Integer[] ids) {
        return signatureConfigInfoMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public SignatureConfigInfo getSignatureConfigInfoById(Integer id) {
        return signatureConfigInfoMapper.selectById(id);
    }
}
