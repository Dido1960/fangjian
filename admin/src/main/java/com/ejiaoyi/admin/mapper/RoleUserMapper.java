package com.ejiaoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.RoleUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 角色用户关系表 Mapper 接口
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Component
public interface RoleUserMapper extends BaseMapper<RoleUser> {

    /**
     * 根据用户id,获取用户角色名称
     *
     * @param uId 用户id
     * @return 用户角色列表
     */
    List<RoleUser> listRoleUser(@Param("uId") Integer uId);

}
