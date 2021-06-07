package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.entity.BackPushStatus;
import com.ejiaoyi.common.entity.ExpertUser;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.mapper.BackPushStatusMapper;
import com.ejiaoyi.common.service.IBackPushStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.service.IExpertUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 回退申请推送状态 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Service
public class BackPushStatusServiceImpl extends ServiceImpl<BackPushStatusMapper, BackPushStatus> implements IBackPushStatusService {

    @Autowired
    private IExpertUserService expertUserService;
    @Autowired
    private BackPushStatusMapper backPushStatusMapper;

    @Override
    @CacheEvict(value = CacheName.BACK_PUSH, allEntries = true)
    public Integer initBackPush(BackPushStatus backPushStatus) {
        Assert.notNull(backPushStatus.getBidSectionId(), "param bidSectionId can not be null!");
        List<ExpertUser> expertUsers = expertUserService.listExperts(backPushStatus.getBidSectionId());
        int result = 0;
        for (ExpertUser expertUser : expertUsers) {
            backPushStatus.setId(null);
            backPushStatus.setExpertId(expertUser.getId());
            result += backPushStatusMapper.insert(backPushStatus);
        }
        return result;
    }

    @Override
    @Cacheable(value = CacheName.BACK_PUSH, key = "#expertId", unless = "#result==null")
    public List<BackPushStatus> listBackPushByExpertId(Integer expertId) {
        Assert.notNull(expertId, "param expertId can not be null!");
        QueryWrapper<BackPushStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("EXPERT_ID", expertId);
        queryWrapper.eq("PUSH_RESULT", Enabled.NO.getCode());
        queryWrapper.orderByDesc("ID");
        return backPushStatusMapper.selectList(queryWrapper);
    }

    @Override
    @CacheEvict(value = CacheName.BACK_PUSH, allEntries = true)
    public Integer updateBackPush(BackPushStatus backPushStatus) {
        Assert.notNull(backPushStatus.getId(), "param id can not be null!");
        return backPushStatusMapper.updateById(backPushStatus);
    }

    @Override
    @CacheEvict(value = CacheName.BACK_PUSH, allEntries = true)
    public Integer updateAllBackPush(Integer bidSectionId, Integer expertId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        Assert.notNull(expertId, "param expertId can not be null!");
        UpdateWrapper<BackPushStatus> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("BID_SECTION_ID", bidSectionId);
        updateWrapper.eq("EXPERT_ID", expertId);
        updateWrapper.eq("PUSH_RESULT", Enabled.NO.getCode());
        updateWrapper.set("PUSH_RESULT", Enabled.YES.getCode());
        return backPushStatusMapper.update(null,updateWrapper);
    }

}
