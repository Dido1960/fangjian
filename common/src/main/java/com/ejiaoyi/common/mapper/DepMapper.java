package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.Dep;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * </p>
 *
 * @author samzqr
 * @since 2020-07-02
 */
@Component
public interface DepMapper extends BaseMapper<Dep> {
    /**
     * 获取部门信息
     *
     * @param page 分页参数
     * @param dep  部门对象
     * @return 部门信息
     */
    List<Dep> pagedDep(Page page, @Param("dep") Dep dep);
}
