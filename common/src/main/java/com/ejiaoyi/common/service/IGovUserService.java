package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.GovUser;

import java.util.List;

/**
 * <p>
 * 政府用户 服务类
 * </p>
 *
 * @author samzqr
 * @since 2020-07-02
 */
public interface IGovUserService {



    /**
     * 获取政府用户登录时候
     * @param loginName 登录名称
     * @param govDepType 政府部门类型
     * */
    GovUser getGovUserByLoginName(String loginName, Integer govDepType);


    /**
     * 分页查询政府用户
     * @param regId 区划id
     * @param name
     * @param enabled 启用状态
     * @return
     */
    String pagedUser(Integer regId, String name, Integer enabled);

    /**
     * 新增政府用户
     * @param govUser 政府用户
     * @return
     */
    Boolean addGovUser(GovUser govUser) throws Exception;

    /**
     * 根据id进行删除
     * @param id
     * @return
     */
    Boolean deleteById(Integer id);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    GovUser getGovUserById(Integer id);

    /**
     * 修改用户信息
     * @param govUser
     * @return
     */
    boolean updateGovUser(GovUser govUser);

    /**
     * 查询区划下的用户
     * @param regNo 区划no
     * @param govUser 用户实体
     * @return
     */
    String pageUserByRegId(String regNo, GovUser govUser);

    /**
     * 获取用户
     * */
    GovUser getGovUser(Integer regId, Integer userId);

    GovUser getGovUserByCasn(String casn, Integer govDepType);

    List<GovUser> getAll();

    String listAllGovUser();

    List<GovUser> findGovUserByIds(List<Integer> idList);
}
