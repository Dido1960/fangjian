package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.entity.BidApply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
* 评标申请记录 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface BidApplyMapper extends BaseMapper<BidApply> {

    /**
     * 清除评标组长id
     *
     * @param id bidApplyId
     */
    void updateClearChairManId(@Param("id") Integer id);
}
