package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.Site;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 场地信息 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface SiteMapper extends BaseMapper<Site> {

    List<Site> pagedSite(Page page,@Param("site") Site site);
}
