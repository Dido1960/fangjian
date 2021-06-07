package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.Dep;

/**
 * 部门信息 接口
 *
 * @author yyb
 * @since 2020-9-01
 */
public interface IDepService {

    /**
     *分页查询部门信息
     * @param dep
     * @return
     */
    String pagedDep(Dep dep);

    /**
     * 新增部门
     * @param dep
     * @return
     */
    Boolean addDep(Dep dep);

    /**
     * 修改部门信息
     * @param dep
     * @return
     */
    Boolean updateDep(Dep dep);

    /**
     * 修改部门的启用状态
     * @param dep
     * @return
     */
    Boolean updateDepStatus(Dep dep);


    /**
     * 根据id查询部门
     * @param id
     * @return
     */
    Dep getDepById(Integer id);

    /**
     * 批量删除部门
     * @param ids
     * @return
     */
    JsonData delDep(Integer[] ids);


}
