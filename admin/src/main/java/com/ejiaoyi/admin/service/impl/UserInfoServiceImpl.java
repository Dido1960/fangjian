package com.ejiaoyi.admin.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.admin.mapper.RoleUserMapper;
import com.ejiaoyi.admin.mapper.UserInfoMapper;
import com.ejiaoyi.admin.service.IUserInfoService;
import com.ejiaoyi.admin.support.CurrentUserHolder;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.entity.RoleUser;
import com.ejiaoyi.common.entity.UserCert;
import com.ejiaoyi.common.entity.UserInfo;
import com.ejiaoyi.common.enums.SysUserType;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.util.PinyinToolkit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 用户信息处理逻辑
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Service
public class UserInfoServiceImpl extends BaseServiceImpl implements IUserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    RoleUserMapper roleUserMapper;
    @Autowired
    RoleUserServiceImpl roleUserService;

    @Autowired
    UserCertServiceImpl userCertService;

    @Override
    public UserInfo userAuth(String loginName) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_Name", loginName);
        return userInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public String pagedUser(Integer regId, String name, Integer enabled) {
        Page page = this.getPageForLayUI();
        List<UserInfo> userInfoList = userInfoMapper.pagedUserInfo(page, regId, name, enabled);
        if (CurrentUserHolder.getUser().getUserRoleStatus() == null
                || CurrentUserHolder.getUser().getUserRoleStatus() != -1) {
            Iterator<UserInfo> iterator = userInfoList.iterator();
            while (iterator.hasNext()) {
                UserInfo userInfo = iterator.next();
                if (userInfo.getUserRoleStatus() != null
                        && userInfo.getUserRoleStatus() == -1) {
                    iterator.remove();
                    break;
                }
            }
        }
        return this.initJsonForLayUI(userInfoList, (int) page.getTotal());
    }

    @Override
    public boolean updateUser(UserInfo userInfo, String roleIds) {
        //1、更新用户
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
        queryWrapper.eq("ID", userInfo.getId());
        //更新用户状态
        int result = userInfoMapper.update(userInfo, queryWrapper);
        int result2 = 0;
        String[] newRoleIds = null;
        if (StringUtils.isNotEmpty(roleIds)) {
            //1.1 清除旧的用户角色关系
            newRoleIds = roleIds.split("/");
            QueryWrapper<RoleUser> roleUserQueryWrapper = new QueryWrapper<RoleUser>();
            roleUserQueryWrapper.eq("USER_ID", userInfo.getId());
            //删除旧的用户角色
            roleUserMapper.delete(roleUserQueryWrapper);
            //i从1开始取
            for (int i = 1; i < newRoleIds.length; i++) {
                //添加新的角色权限
                result2 += roleUserMapper.insert(new RoleUser().setRoleId(Integer.valueOf(newRoleIds[i])).setUserId(userInfo.getId()));
            }
            return 1 == result && result2 == newRoleIds.length - 1;
        } else {
            return 1 == result;
        }

    }

    @Override
    public boolean delUser(Integer[] ids) {
        int count = 0;
        for (Integer id : ids) {
            count += userInfoMapper.deleteById(id);
        }
        return count == ids.length;
    }

    @Override
    public boolean addUser(UserInfo userInfo) {

        userInfo.setAllSpelling(PinyinToolkit.cn2Spell(userInfo.getName()));
        userInfo.setFirstSpelling(PinyinToolkit.cn2FirstSpell(userInfo.getName()));
        try {
            userInfo.setPassword(SM2Util.encrypt(userInfo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean flag = false;
        List<RoleUser> roleUserList = new ArrayList<>();
        if (userInfo != null) {
            //启用状态
            userInfo.setEnabled(1);
            //添加用户
            flag = 1 == userInfoMapper.insert(userInfo);
            String[] rIds = null;
            if (userInfo.getRoleId()!=null){
               rIds =  userInfo.getRoleId().split("/");
            }
            if (rIds!=null){
                for (String rId : rIds) {
                    if (StringUtils.isNotEmpty(rId) && !"/".equals(rId)) {
                        RoleUser roleUser = new RoleUser();
                        //角色id
                        roleUser.setRoleId(Integer.valueOf(rId));
                        //用户id
                        roleUser.setUserId(userInfo.getId());
                        //封装用户角色对象
                        roleUserList.add(roleUser);
                    }
                }
            }
            //批量添加用户权限
            flag = roleUserService.addRoleUser(roleUserList);
        }
        return flag;
    }

    @Override
    public boolean deleteUser(Integer[] uIds) {
        assert uIds != null;
        Assert.notEmpty(uIds, "param uIds can not be empty!");
        boolean flag = false;
        int count = 0;
        //批量删用户信息
        count = userInfoMapper.deleteBatchIds(Arrays.asList(uIds));
        // 批量删除用户对应的权限
        flag = roleUserService.delRoleUser(uIds);
        return count == uIds.length && flag;
    }

    @Override
    public List<UserInfo> listUserInfo(UserInfo userInfo) {
        if (userInfo != null) {
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<UserInfo>();
            queryWrapper.eq("id", userInfo.getId());
            return userInfoMapper.selectList(queryWrapper);
        }
        return null;
    }

    @Override
    public UserInfo getUserInfo(Integer userId) {
        return userInfoMapper.selectById(userId);
    }

    @Override
    public boolean updatePass(Integer id, String password) {
        UserInfo user = userInfoMapper.selectById(id);
        try {
            String pass = SM2Util.encrypt(password);
            user.setPassword(pass);
            int i = userInfoMapper.updateById(user);
            if (i == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    @Override
    public boolean updateUserData(UserInfo userInfo) {
        System.out.println("userInfo----" + userInfo.getUserFileId());
        UserInfo userInfo1 = userInfoMapper.selectById(userInfo.getId());
        userInfo1.setName(userInfo.getName());
        userInfo1.setLoginName(userInfo.getLoginName());
        userInfo1.setPhone(userInfo.getPhone());

        userInfo1.setUserFileId(userInfo.getUserFileId());
        int i = userInfoMapper.updateById(userInfo1);

        if (i == 1) {
            return true;
        }
        return false;
    }

    /**
     * @return
     * @author lesgod
     * @date 2020-6-22 13:36
     */
    @Override
    public UserInfo userCasn(String casn) {
        UserCert userCert = userCertService.getUserCertByCasn(casn, SysUserType.USER);
        if (userCert != null) {
            return this.getUserInfo(userCert.getUserId());
        }
        return null;
    }

    /**
     * 通过用户名获取用户
     *
     * @return
     * @author 刘国强
     * @date 2020-7-1
     */
    @Override
    public UserInfo getUserByLoginName(String loginName) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();

        wrapper.eq("LOGIN_NAME",loginName);

        List<UserInfo> userInfos = userInfoMapper.selectList(wrapper);
        UserInfo user= null;
        if (userInfos.size()>0){
            user =userInfos.get(0);
        }
        return user;
    }

    /**
     * 密码重置
     *
     * @return
     * @author  yyb
     * @date 2020/7/02 10:11
     */
    @Override
    public void resetUserPassword(UserInfo userInfo) {
        try {
            userInfo.setPassword(SM2Util.encrypt(userInfo.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        userInfoMapper.updateById(userInfo);

    }

}
