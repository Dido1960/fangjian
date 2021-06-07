package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.Dep;
import com.ejiaoyi.common.entity.GovUser;
import com.ejiaoyi.common.entity.Reg;
import com.ejiaoyi.common.mapper.DepMapper;
import com.ejiaoyi.common.mapper.ExpertUserMapper;
import com.ejiaoyi.common.mapper.GovUserMapper;
import com.ejiaoyi.common.mapper.RegMapper;
import com.ejiaoyi.common.service.IDepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 标段信息 服务实现类
 *
 * @author yyb
 * @since 2020-8-25
 */
@Service
public class DepServiceImpl extends BaseServiceImpl implements IDepService {

    @Autowired
    DepMapper depMapper;

    @Autowired
    RegMapper regMapper;

    @Autowired
    GovUserMapper govUserMapper;

    @Override
    public String pagedDep(Dep dep) {
        Page page = this.getPageForLayUI();
        List<Dep> list = depMapper.pagedDep(page, dep);
        return this.initJsonForLayUI(list, (int) page.getTotal());
    }


    @Override
    public Boolean addDep(Dep dep) {
        //新增时，默认设置,父级id为-1
        dep.setEnabled(1);
        dep.setParentId(-1);
        int i = depMapper.insert(dep);
        if(i==1){
            return true;
        }else {
            return false;
        }
    }


    @Override
    public Boolean updateDep(Dep dep) {
        return 1 == depMapper.updateById(dep);
    }

    @Override
    public Boolean updateDepStatus(Dep dep) {
        depMapper.selectById(dep.getId());
        dep.setEnabled(dep.getEnabled());
        int i = depMapper.updateById(dep);
        if(i==1){
            return true;
        }
        return false;
    }

    @Override
    public Dep getDepById(Integer id) {
        Dep dep = depMapper.selectById(id);
        Reg reg = regMapper.selectById(dep.getRegId());
        if(reg!=null) {
            dep.setRegName(reg.getRegName());
        }
        return dep;
    }

    @Override
    public JsonData delDep(Integer[] ids) {
        JsonData data = new JsonData();
        for (Integer id : ids) {
            QueryWrapper<GovUser> wrapper = new QueryWrapper<>();
            wrapper.eq("DEP_ID",id);
            List<GovUser> govUsers = govUserMapper.selectList(wrapper);
            if(govUsers.size()>0){
                data.setCode("2");
                data.setMsg("该部门下存在用户，不能删除！");
                return data;
            }
        }

        for (Integer id : ids) {
            depMapper.deleteById(id);
        }
        data.setCode("1");
        data.setMsg("删除成功！");
        return data;
    }
}
