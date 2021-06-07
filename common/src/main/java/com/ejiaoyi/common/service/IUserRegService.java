package com.ejiaoyi.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.UserReg;

import java.util.List;

/**
 * <p>
 * 用户区划关联表 服务类
 * </p>
 *
 * @author langwei
 * @since 2020-08-05
 */
public interface IUserRegService extends IService<UserReg> {

    /**
     * 根据用户id 查询所属区划
     */
    List<UserReg> listUserRegByUserId(Integer userId);

    /**
     * 添加userreg数据
     */
    void addUserRegList(String[] userRegs, String userId);
}
