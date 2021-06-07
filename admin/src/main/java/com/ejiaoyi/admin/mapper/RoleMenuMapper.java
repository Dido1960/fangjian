package com.ejiaoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.Menu;
import com.ejiaoyi.common.entity.Role;
import com.ejiaoyi.common.entity.RoleMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 角色菜单关系表 Mapper 接口
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Component
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 根据角色获取菜单list
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    List<Menu> listTreeMenuByRole(@Param("role") Role role);


}
