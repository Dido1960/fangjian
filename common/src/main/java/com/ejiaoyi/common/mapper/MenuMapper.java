package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@Component
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取菜单信息
     *
     * @param page     分页参数
     * @param menuName 菜单名称
     * @return 菜单信息
     */
    List<Menu> pagedMenu(Page page, @Param("menuName") String menuName, @Param("id") Integer id);

    /**
     * 根据pId和orderNo查询该父级节点下的排序号大于orderNo的子节点
     *
     * @param parentId 父级主键
     * @param orderNo  菜单排序号
     * @return
     */
    List<Menu> listMenuByPidAndOrderNo(@Param("pId") Integer parentId, @Param("orderNo") Integer orderNo);

    /**
     * 设置菜单向后移动一位
     *
     * @param ids 菜单主键数组
     */
    boolean moveMenu(@Param("ids") Integer[] ids);

    /**
     * 根据父级id返回菜单最大排序号
     *
     * @param pId 父级id
     * @return 菜单最大排序号
     */
    Integer maxOrderNo(Integer pId);

    /**
     * 根据角色id数组，获取菜单列表
     *
     * @param roleIds 角色id数组
     * @return 获取菜单列表
     */
    List<Menu> listMenuByRoleIdS(@Param("roleIds") List<Integer> roleIds);
}
