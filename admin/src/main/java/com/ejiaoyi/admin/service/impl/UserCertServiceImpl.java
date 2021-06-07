package com.ejiaoyi.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.admin.service.IUserInfoService;
import com.ejiaoyi.common.entity.UserCert;
import com.ejiaoyi.common.enums.SysUserType;
import com.ejiaoyi.common.mapper.UserCertMapper;
import com.ejiaoyi.common.service.IGovUserService;
import com.ejiaoyi.common.service.IUserCertService;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 用户CA绑定表 服务实现类
 * </p>
 *
 * @author lesgod
 * @since 2020-05-12
 */
@Service
public class UserCertServiceImpl extends BaseServiceImpl implements IUserCertService {

    @Autowired
    UserCertMapper userCertMapper;

    @Autowired
    IUserInfoService userInfoService;
    @Autowired
    IGovUserService govUserService;


    /**
     * 获取用户的锁绑定信息
     *
     * @param userId   用户ID
     * @param userType 用户类别
     * @return
     * @author lesgod
     * @date 2020/5/12 13:27
     */
    @Override
    public List<UserCert> listUserCert(Integer userId, SysUserType userType) {
        QueryWrapper<UserCert> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("USER_TYPE", userType.getType());
        queryWrapper.eq("USER_ID", userId);
        queryWrapper.ne("DELETE_FLAG", 1);
        return userCertMapper.selectList(queryWrapper);
    }

    /**
     * 插入User
     *
     * @return
     * @author lesgod
     * @date 2020/5/12 13:29
     */
    @Override
    public void insertUser(UserCert userCert) {
        userCertMapper.insert(userCert);
    }


    /**
     * 绑定新增
     *
     * @return
     * @author lesgod
     * @date 2020/5/13 10:21
     */
    @Override
    public UserCert bindValidUser(UserCert userCert) {
        QueryWrapper<UserCert> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeLeft("UKEY_NUM", userCert.getUkeyNum().indexOf("/")==-1?userCert.getUkeyNum():userCert.getUkeyNum().split("/")[1]);
        queryWrapper.eq("USER_TYPE", userCert.getUserType());
        queryWrapper.ne("DELETE_FLAG", 1);
        return userCertMapper.selectOne(queryWrapper);
    }

    /**
     * 解开绑定
     *
     * @return
     * @author lesgod
     * @date 2020/5/13 10:21
     */
    @Override
    public void removeUserCert(UserCert userCert) {
        userCert.setDeleteFlag(1);
        userCertMapper.updateById(userCert);
    }


    @Override
    public Boolean getCertUserByuKeyNum(String UkeyNum) {
        Assert.notNull(UkeyNum, "param uKeyNum can not be empty");
        QueryWrapper<UserCert> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeLeft("UKEY_NUM", UkeyNum.indexOf("/") == -1 ? UkeyNum : UkeyNum.split("/")[1]);
        queryWrapper.ne("DELETE_FLAG", 1);
        return userCertMapper.selectCount(queryWrapper) == 1;
    }

    @Override
    public Boolean delUserCertByUkeyNum(String UkeyNum) {
        Assert.notNull(UkeyNum, "param uKeyNum can not be empty");
        QueryWrapper<UserCert> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeLeft("UKEY_NUM", UkeyNum.indexOf("/") == -1 ? UkeyNum : UkeyNum.split("/")[1]);
        queryWrapper.ne("DELETE_FLAG", 1);
        // 删除CA与用户关联
        return userCertMapper.delete(queryWrapper) == 1;
    }


    /**
     * 获取用户信息
     *
     * @return
     * @author lesgod
     * @date 2020-6-22 13:40
     */
    @Override
    public UserCert getUserCertByCasn(String casn, SysUserType user) {
        QueryWrapper<UserCert> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeLeft("UKEY_NUM", casn.indexOf("/") == -1 ? casn : casn.split("/")[1]);
        queryWrapper.eq("USER_TYPE", user.getType());
        queryWrapper.eq("LOGIN_FLAG", 1);
        queryWrapper.ne("DELETE_FLAG", 1);
        return userCertMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateUserCert(UserCert userCert) {
        userCertMapper.updateById(userCert);
    }
}

