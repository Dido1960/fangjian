package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.CompanyUser;

/**
 * <p>
 * 企业用户 服务类
 * </p>
 *
 * @author langwei
 * @since 2020-08-04
 */
public interface ICompanyUserService {


    /**
     * 获取全部的用户
     * @return
     */
    String listCompanyUser();

    /**
     * 获取user信息
     * @param userId
     * @return
     */
    CompanyUser getCompanyUserById(String userId);
}
