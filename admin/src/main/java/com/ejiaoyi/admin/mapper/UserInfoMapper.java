package com.ejiaoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Component
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 用户登陆验证 获取账户信息
     *
     * @param loginName 用户名
     * @return 用户登录信息
     */
    UserInfo userAuth(@Param("loginName") String loginName);

    /**
     * 获取用户信息
     *
     * @param page    分页参数
     * @param regId   区划id
     * @param name    人员名称
     * @param enabled 启用状态
     * @return 用户信息
     */
    List<UserInfo> pagedUserInfo(Page page, @Param("regId") Integer regId, @Param("name") String name, @Param("enabled") Integer enabled);
}
