package com.ejiaoyi.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.Reg;
import com.ejiaoyi.common.mapper.RegMapper;
import com.ejiaoyi.common.service.IRegService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.PinyinToolkit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 行政区划 服务实现类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Service
public class RegServiceImpl extends BaseServiceImpl implements IRegService {

    @Autowired
    RegMapper regMapper;

    @Override
    public List<Reg> listReg(Integer id) {
        // 查询所有部门区划
        if (id == null) {
            return regMapper.selectList(new QueryWrapper<Reg>().eq("ENABLED", 1));
        }
        return selectRegList(id);
    }

    @Override
    public List<Reg> listAdminReg(Integer id) {
        // 查询所有部门区划
        if (id == null) {
            return regMapper.selectList(null);
        }
        return selectRegList(id);
    }

    private List<Reg> selectRegList(Integer id){
        //创建部门区划列表
        List<Reg> regList = new ArrayList<>();
        // 按照区划id，查询区划对象
        QueryWrapper<Reg> queryWrapper = new QueryWrapper<Reg>().eq("id", id);
        int count = regMapper.selectCount(queryWrapper);
        if (count != 0) {
            //根据部门区划id，获取部门区划对象
            Reg reg = regMapper.selectOne(queryWrapper);
            // 部门区划id=-1，属于省级
            if (reg.getParentId() == -1) {
                //省级对象添加到regList
                regList.add(reg);
                //1、当前区划属于省级
                queryWrapper = new QueryWrapper<Reg>().eq("parent_id", id);
                //市级对象List
                List<Reg> cityRegList = regMapper.selectList(queryWrapper);
                //市级对象List追加到部门区划List
                regList.addAll(cityRegList);
                //遍历市级对象
                for (Reg reg1 : cityRegList) {
                    //区级对象List追加到部门区划List
                    queryWrapper = new QueryWrapper<Reg>().eq("parent_id", reg1.getId());
                    regList.addAll(regMapper.selectList(queryWrapper));
                }
            } else {
                //获取parentId
                Integer parentId = reg.getParentId();
                //查询当前区划的父级
                queryWrapper = new QueryWrapper<Reg>().eq("id", parentId);
                //获取当前id的父级对象
                Reg parentReg = regMapper.selectOne(queryWrapper);
                //1、市级,父级id为-1
                if (parentReg.getParentId() == -1) {
                    //获取市级对象
                    queryWrapper = new QueryWrapper<Reg>().eq("id", id);
                    Reg cityReg = regMapper.selectOne(queryWrapper);
                    //市级对象添加到区划List
                    regList.add(cityReg);
                    //获取当前市级对象下的所有区级区划
                    queryWrapper = new QueryWrapper<Reg>().eq("parent_id", cityReg.getId());
                    //区级对象List
                    List<Reg> districtRegList = regMapper.selectList(queryWrapper);
                    //区级对象List追加到部门区划List
                    regList.addAll(districtRegList);
                } else {
                    //区级对象
                    queryWrapper = new QueryWrapper<Reg>().eq("id", id);
                    regList.add(regMapper.selectOne(queryWrapper));
                }
            }
        }
        //当前ID无效
        return regList;
    }

    @Override
    public Boolean updateRegEnabled(Integer[] ids, Integer state) {

        Integer count = 0;

        //设置状态
        Reg reg = new Reg().setEnabled(state);

        for (Integer id : ids) {
            QueryWrapper<Reg> regQueryWrapper = new QueryWrapper<Reg>().eq("id", id);
            if (1 == regMapper.update(reg, regQueryWrapper)) {
                count++;
            }
        }

        return ids.length == count;
    }

    /**
     * 根据行政区划代码，返回区划对象
     *
     * @param regCode 行政区划代码
     * @return 区划对象
     */
    public Reg getRegByRegCode(String regCode) {
        if (StringUtils.isNotEmpty(regCode)) {
            QueryWrapper<Reg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("REG_NO", regCode);

            return regMapper.selectOne(queryWrapper);
        } else {
            return null;
        }

    }

    @Override
    public Boolean addReg(Reg reg) {
        return regMapper.insert(reg) == 1;
    }

    @Override
    public Boolean addReg(String provinceName, String cityName, String districtName, String provinceCode, String cityCode, String districtCode, Integer state) {
        //标识，添加是否成功
        boolean flag = true;

        //省级对象
        Reg provinceReg = getRegByRegCode(provinceCode);

        //市级对象
        Reg cityceReg = getRegByRegCode(cityCode);

        //区级对象
        Reg districtReg = getRegByRegCode(districtCode);

        //省级对象为null
        if (provinceReg == null) {
            //省级城市，父级id为 -1
            Reg reg = getReg(provinceName, provinceCode, -1, state);
            List<Reg> regListForGrade = getRegListByParentId(-1);
            reg.setOrderNo(regListForGrade.size() + 1);
            flag = addReg(reg);

            if (StringUtils.isNotEmpty(cityCode)) {
                //市级对象的父id，取省级的id
                Reg cReg = getReg(cityName, cityCode, getRegByRegCode(provinceCode).getId(), state);
                List<Reg> cityList = getRegListByParentId(reg.getId());
                cReg.setOrderNo(cityList.size() + 1);
                flag = addReg(cReg);
            }

            if (StringUtils.isNotEmpty(districtCode)) {
                //区级对象的父id，取市级的id
                Reg dReg = getReg(districtName, districtCode, getRegByRegCode(cityCode).getId(), state);
                List<Reg> list = getRegListByParentId(reg.getId());
                dReg.setOrderNo(list.size() + 1);
                flag = addReg(dReg);
            }
        }

        //省级对象不为null
        if (provinceReg != null) {
            if (cityceReg == null && StringUtils.isNotEmpty(cityCode)) {
                //市级对象的父id，取省级的id
                Reg reg = getReg(cityName, cityCode, getRegByRegCode(provinceCode).getId(), state);
                List<Reg> list = getRegListByParentId(provinceReg.getId());
                reg.setOrderNo(list.size() + 1);
                flag = addReg(reg);

                if (StringUtils.isNotEmpty(districtCode)) {
                    //区级对象的父id，取市级的id
                    Reg dReg = getReg(districtName, districtCode, getRegByRegCode(cityCode).getId(), state);
                    List<Reg> dlist = getRegListByParentId(reg.getId());
                    dReg.setOrderNo(dlist.size() + 1);
                    flag = addReg(dReg);
                }
            } else {
                // 市级对象存在
                if (districtReg == null && StringUtils.isNotEmpty(districtCode)) {
                    //区级对象存在
                    //区级对象的父id，取市级的id
                    Reg reg = getReg(districtName, districtCode, getRegByRegCode(cityCode).getId(), state);
                    List<Reg> dlist = getRegListByParentId(cityceReg.getId());
                    reg.setOrderNo(dlist.size() + 1);
                    flag = addReg(reg);
                }
            }

        }

        return flag;

    }

    @Override
    public Reg getRegById(Integer regId) {
        return regMapper.selectById(regId);
    }

    @Override
    public List<Reg> getRegList() {
        return regMapper.selectList(null);
    }

    @Override
    public List<Reg> getRegListForGrade() {
        QueryWrapper<Reg> wrapper = new QueryWrapper<>();
        wrapper.eq("PARENT_ID",-1);
        List<Reg> list = regMapper.selectList(wrapper);
        for (Reg reg : list) {
            QueryWrapper<Reg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("PARENT_ID",reg.getId());
            reg.setSubsetList(regMapper.selectList(queryWrapper));
        }
        return list;
    }

    @Override
    public Reg getRegByRegNo(String regNo) {
        QueryWrapper<Reg> wrapper = new QueryWrapper<>();
        wrapper.eq("REG_NO",regNo);
        List<Reg> regs = regMapper.selectList(wrapper);
        if (regs.size()>0){
            return regs.get(0);
        }
        return null;
    }


    /**
     * 构造行政区划对象
     *
     * @param regName  行政区划名称
     * @param regNo    行政区划代码
     * @param parentId 父级id
     * @param state 启用状态
     * @return 行政区划对象
     */
    private Reg getReg(String regName, String regNo, Integer parentId, Integer state) {

        Reg reg = Reg.builder().
                regName(regName).
                regNo(regNo).
                allSpelling(PinyinToolkit.cn2Spell(regName)).
                firstSpelling(PinyinToolkit.cn2FirstSpell(regName)).
                parentId(parentId).
                enabled(state).
                build();

        return reg;
    }

    @Override
    public List<Reg> getRegListByParentId(Integer parentId) {
        QueryWrapper<Reg> wrapper = new QueryWrapper<>();
        if (CommonUtil.isEmpty(parentId)) {
            parentId = -1;
        }
        wrapper.eq("PARENT_ID",parentId);
        List<Reg> list = regMapper.selectList(wrapper);
        for (Reg reg : list) {
            QueryWrapper<Reg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("PARENT_ID",reg.getId());
            reg.setSubsetList(regMapper.selectList(queryWrapper));
        }
        return list;
    }

    @Override
    public Reg getRegByRegName(String regName) {
        QueryWrapper<Reg> wrapper = new QueryWrapper<>();
        wrapper.eq("REG_NAME",regName);
        wrapper.eq("ENABLED",1);
        List<Reg> regs = regMapper.selectList(wrapper);
        if (regs.size()>0){
            return regs.get(0);
        }
        return null;
    }
}
