package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.entity.Dep;
import com.ejiaoyi.common.entity.GovUser;
import com.ejiaoyi.common.entity.Reg;
import com.ejiaoyi.common.entity.UserCert;
import com.ejiaoyi.common.enums.SysUserType;
import com.ejiaoyi.common.mapper.DepMapper;
import com.ejiaoyi.common.mapper.GovUserMapper;
import com.ejiaoyi.common.mapper.RegMapper;
import com.ejiaoyi.common.mapper.UserCertMapper;
import com.ejiaoyi.common.service.IGovUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 政府用户 服务实现类
 * </p>
 *
 * @author samzqr
 * @since 2020-07-02
 */
@Service
public class GovUserServiceImpl extends BaseServiceImpl implements IGovUserService {

    @Autowired
    GovUserMapper govUserMapper;

    @Autowired
    DepMapper depMapper;

    @Autowired
    RegMapper regMapper;

    @Autowired
    UserCertMapper userCertMapper;

    @Autowired
    RegServiceImpl regService;


    @Override
    public String pagedUser(Integer depId, String name, Integer enabled) {
        Page page = this.getPageForLayUI();
        List<GovUser> userInfoList = govUserMapper.pagedGovUser(page, depId, name, enabled);
        return this.initJsonForLayUI(userInfoList, (int) page.getTotal());
    }

    @Override
    public Boolean addGovUser(GovUser govUser) throws Exception {
        govUser.setEnabled(1);
        govUser.setPassword(SM2Util.encrypt(govUser.getPassword()));
        int i = govUserMapper.insert(govUser);
        return i == 1;
    }

    @Override
    public Boolean deleteById(Integer id) {
        int i = govUserMapper.deleteById(id);
        return i == 1;
    }

    @Override
    public GovUser getGovUserById(Integer id) {
        GovUser govUser = govUserMapper.selectById(id);
        if(govUser==null){
            return null;
        }
        Dep dep = depMapper.selectById(govUser.getDepId());
        if (dep != null) {
            govUser.setDepName(dep.getDepName());
        }
        return govUser;
    }

    @Override
    public boolean updateGovUser(GovUser govUser) {
        if (StringUtils.isNotEmpty(govUser.getPassword())) {
            try {
                govUser.setPassword(SM2Util.encrypt(govUser.getPassword()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return govUserMapper.updateById(govUser) == 1;
    }

    @Override
    public String pageUserByRegId(String regNo, GovUser user) {
        Page page = this.getPageForLayUI();
        String name = null;
        String depName = null;
        if (user.getDepName() != null) {
            depName = user.getDepName();
        }
        if (user.getName() != null) {
            name = user.getName();
        }

        Reg reg = regService.getRegByRegCode(regNo);
        List<GovUser> list = govUserMapper.pagedGovUserByRegId(page, reg.getId(), name, depName);

        for (GovUser govUser : list) {
            Dep dep = depMapper.selectById(govUser.getDepId());
            govUser.setDepName(dep.getDepName());
        }

        return this.initJsonForLayUI(list, (int) page.getTotal());
    }

    @Override
    public GovUser getGovUser(Integer regId, Integer userId) {
        return govUserMapper.getGovUser(regId, userId);
    }

    @Override
    public GovUser getGovUserByCasn(String casn, Integer govDepType) {
        QueryWrapper<UserCert> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeLeft("UKEY_NUM", casn.indexOf("/") == -1 ? casn : casn.split("/")[1]);
        queryWrapper.eq("USER_TYPE", SysUserType.GOVUSER.getType());
        queryWrapper.eq("LOGIN_FLAG", 1);
        queryWrapper.ne("DELETE_FLAG", 1);
        UserCert userCert= userCertMapper.selectOne(queryWrapper);
        if(userCert == null){
            return null;
        }

        GovUser govUser = this.getGovUserById(userCert.getUserId());

        if(govUser == null){
            return null;
        }

        // 判断用户是否绑定部门
        Dep dep = depMapper.selectById(govUser.getDepId());
        if (dep == null || dep.getGovDepType() == null) {
            return null;
        }

        // 判断用户是否拥有该权限
        if (!govDepType.equals(dep.getGovDepType())) {
            return null;
        }

        govUser.setDep(dep);
        return govUser;
    }

    @Override
    public List<GovUser> getAll() {

        List<GovUser> govUsers = govUserMapper.selectList(null);

        for (GovUser govUser : govUsers) {
            Dep dep = depMapper.selectById(govUser.getDepId());
            govUser.setDepName(dep.getDepName());
        }
        return govUsers;
    }

    @Override
    public String listAllGovUser() {
        Page page = govUserMapper.selectPage(this.getPageForLayUI(), null);

        return this.initJsonForLayUI(page.getRecords(),(int)page.getTotal());
    }

    @Override
    public List<GovUser> findGovUserByIds(List<Integer> idList) {

        QueryWrapper<GovUser> wrapper = new QueryWrapper<>();
        wrapper.in("ID",idList);
        return govUserMapper.selectList(wrapper);
    }

    @Override
    public GovUser getGovUserByLoginName(String loginName, Integer govDepType) {
        QueryWrapper<GovUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_Name", loginName);
        GovUser govUser = govUserMapper.selectOne(queryWrapper);
        if (govUser == null) {
            return null;
        }

        // 判断用户是否绑定部门
        Dep dep = depMapper.selectById(govUser.getDepId());
        if (dep == null || dep.getGovDepType() == null) {
            return null;
        }

        // 判断用户是否拥有该权限
        if (!govDepType.equals(dep.getGovDepType())) {
            return null;
        }
        govUser.setDep(dep);

        return govUser;
    }

}