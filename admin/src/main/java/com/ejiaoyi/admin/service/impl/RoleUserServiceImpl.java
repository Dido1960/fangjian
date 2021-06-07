package com.ejiaoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.admin.mapper.RoleUserMapper;
import com.ejiaoyi.admin.service.IRoleUserService;
import com.ejiaoyi.common.entity.RoleUser;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色用户关系表 服务实现类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Service
public class RoleUserServiceImpl extends BaseServiceImpl implements IRoleUserService {

    @Autowired
    RoleUserMapper roleUserMapper;

    @Override
    public boolean addRoleUser(List<RoleUser> roleUsers) {
        int count = 0;
        for (RoleUser roleUser : roleUsers) {
            count += roleUserMapper.insert(roleUser);
        }
        return count == roleUsers.size();
    }

    @Override
    public boolean delRoleUser(Integer[] userId) {
        try {
            for (Integer uId : userId) {
                QueryWrapper<RoleUser> queryWrapper = new QueryWrapper<RoleUser>();
                queryWrapper.eq("USER_ID", uId);
                roleUserMapper.delete(queryWrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<RoleUser> listRoleUser(Integer uId) {
        return roleUserMapper.listRoleUser(uId);
    }

    @Override
    public String getRoleIds(Integer uId) {
        QueryWrapper<RoleUser> queryWrapper = new QueryWrapper<RoleUser>();
        queryWrapper.eq("USER_ID", uId);
        List<RoleUser> roleUsers = roleUserMapper.selectList(queryWrapper);
        String roleIdS = "";
        if (roleUsers.size()>0){
            for (int i= 0;i<roleUsers.size();i++){
                if (i==roleUsers.size()-1){
                    roleIdS += roleUsers.get(i).getRoleId();
                }else {
                    roleIdS = roleIdS +roleUsers.get(i).getRoleId() + "/" ;
                }
            }
        }
        return roleIdS;
    }
}
