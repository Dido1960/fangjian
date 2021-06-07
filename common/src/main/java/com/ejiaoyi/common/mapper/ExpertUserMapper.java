package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.entity.ExpertUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 评标专家 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface ExpertUserMapper extends BaseMapper<ExpertUser> {
    /**
     * 用于专家名称模糊查询
     * @param expertName
     * @return
     */
    List<ExpertUser> searchExpert(@Param("expertName") String expertName);

    /**
     * 获取某标段的没有回避的评标专家个数
     *
     * @param bidSectionId 标段id
     * @return
     */
    Integer countExperts(@Param("bidSectionId") Integer bidSectionId);

    /**
     * 用于专家名称模糊查询
     *
     * @param loginName 登录名
     * @return
     */
    List<ExpertUser> listExpertUser(@Param("loginName") String loginName);

}
