package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.Reg;

import java.util.List;

/**
 * <p>
 * 行政区划 服务类
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
public interface IRegService {

    /**
     * 两个功能：
     * 1、按照区划id,查询行政区划列表
     * 2、将省市区封装成区划列表
     *
     * @param id 行政区划 ID
     * @return 普通区划列表；省市区区划列表
     */
    List<Reg> listReg(Integer id);

    /**
     * 后台区划查询
     */
    List<Reg> listAdminReg(Integer id);

    /**
     * 行政区划菜单的状态，0：禁用，1：启用
     *
     * @param ids   选择的id集合
     * @param state 状态值
     * @return true or false
     */
    Boolean updateRegEnabled(Integer[] ids, Integer state);

    /**
     * 添加行政区划
     *
     * @param reg 行政区划
     * @return
     */
    Boolean addReg(Reg reg);

    /**
     * 添加行政区划
     *
     * @param provinceName 省级名称
     * @param cityName     市级名称
     * @param districtName 县级名称
     * @param provinceCode 省级区划代码
     * @param cityCode     市级区划代码
     * @param districtCode 县级区划代码
     * @param state        区划状态
     * @return
     */
    Boolean addReg(String provinceName, String cityName, String districtName, String provinceCode, String cityCode, String districtCode, Integer state);

    Reg getRegById(Integer regId);

    /**
     * 获取地区列表
     * @return
     */
    List<Reg> getRegList();

    /**
     * 按级获取区域列表，只分一级，parentID为-1是初级
     * @return
     */
    List<Reg> getRegListForGrade();

    /**
     * 按级获取区域列表，只分一级，parentID为-1是初级
     * @param parentId 行政区划父级id,不传默认为初级
     * @return
     */
    List<Reg> getRegListByParentId(Integer parentId);

    /**
     * 通过regNo查询reg
     * @param regNo 区划代码
     * @return
     */
    Reg getRegByRegNo(String regNo);

    /**
     * 通过regName查询reg
     * @param regName 区划名称
     * @return
     */
    Reg getRegByRegName(String regName);
}
