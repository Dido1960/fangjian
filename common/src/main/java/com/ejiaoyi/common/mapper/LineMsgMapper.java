package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.LineMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 网上开标消息 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface LineMsgMapper extends BaseMapper<LineMsg> {

    /**
     * 通过标段id获取该标段所有消息
     * @param bidSectionId 标段id
     * @return
     */
    List<LineMsg> listLineMsg(@Param("bidSectionId") Integer bidSectionId);

    /**
     * 获取最后一条消息记录
     * @param bidSectionId 标段id
     * @return
     */
    LineMsg getLastLineMsg(@Param("bidSectionId") Integer bidSectionId);
}
