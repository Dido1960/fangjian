package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.UserReg;
import com.ejiaoyi.common.mapper.UserRegMapper;
import com.ejiaoyi.common.service.IUserRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户区划关联表 服务实现类
 * </p>
 *
 * @author langwei
 * @since 2020-08-05
 */
@Service
public class UserRegServiceImpl extends ServiceImpl<UserRegMapper, UserReg> implements IUserRegService {

    @Autowired
    private UserRegMapper userRegMapper;

    @Override
    public List<UserReg> listUserRegByUserId(Integer userId) {
        QueryWrapper<UserReg> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_ID",userId);
        return userRegMapper.selectList(queryWrapper);
    }

    @Override
    public void addUserRegList(String[] userRegs, String userId) {
        QueryWrapper<UserReg> query1 = new QueryWrapper<>();
        query1.eq("USER_ID",userId);
        userRegMapper.delete(query1);
        for (String use : userRegs){
            UserReg userReg = new UserReg();
            userReg.setUserId(Integer.parseInt(userId));
            userReg.setRegId(Integer.parseInt(use));
            userRegMapper.insert(userReg);
        }
    }
}
