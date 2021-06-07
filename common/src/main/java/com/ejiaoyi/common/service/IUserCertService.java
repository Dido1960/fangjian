package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.UserCert;
import com.ejiaoyi.common.enums.SysUserType;

import java.util.List;

/**
 * <p>
 * 用户CA绑定表 服务类
 * </p>
 *
 * @author lesgod
 * @since 2020-05-12
 */
public interface IUserCertService extends IBaseService {

    /**
     * 获取所有的锁信息
     *
     * @return
     * @author lesgod
     * @date 2020/5/12 14:31
     */
    List<UserCert> listUserCert(Integer userId, SysUserType userType);

    /**
     * 插入绑定信息
     *
     * @return
     * @author lesgod
     * @date 2020/5/12 14:32
     */
    void insertUser(UserCert userCert);

    /**
     * 验证绑定信息
     *
     * @return
     * @author lesgod
     * @date 2020/5/12 14:32
     */
    UserCert bindValidUser(UserCert userCert);

    /**
     * 解开绑定
     *
     * @return
     * @author lesgod
     * @date 2020/5/13 10:20
     */
    void removeUserCert(UserCert userCert);



    /**
     * 根据锁号，查询是否绑定用户
     *
     * @param UkeyNum 锁号
     * @return 绑定返回true
     */
    Boolean getCertUserByuKeyNum(String UkeyNum);

    /**
     * 根据锁号，解绑用户
     *
     * @param UkeyNum 锁号
     * @return 解绑用户
     */
    Boolean delUserCertByUkeyNum(String UkeyNum);

    /**
     * 获取对应的锁绑定信息
     *
     * @return
     * @author lesgod
     * @date 2020-6-22 13:44
     */
    UserCert getUserCertByCasn(String casn, SysUserType user);

    /**
     * 更新CA信息
     *
     * @return
     * @author lesgod
     * @date 2020-6-23 17:10
     */
    void updateUserCert(UserCert userCert);
}
