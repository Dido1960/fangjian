package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.entity.BidSectionRelate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
* 标段关联信息 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface BidSectionRelateMapper extends BaseMapper<BidSectionRelate> {
    /**
     * 清除评标报告id
     *
     * @param id relateID
     */
    void updateClearReportId(@Param("id") Integer id);
}
