package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.GovUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 政府用户 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface GovUserMapper extends BaseMapper<GovUser> {
    List<GovUser> pagedGovUser(Page page, @Param("depId") Integer depId,
                               @Param("name") String name,
                               @Param("enabled") Integer enabled);

    List<GovUser> pagedGovUserByRegId(Page page,@Param("regId") Integer regId,  @Param("name") String name ,@Param("depName")String depName);

    /**
     * 获取政府人员对象
     *
     * @param regId 区划id
     * @param id    用户id
     * @return 政府人员
     */
    GovUser getGovUser(@Param("regId") Integer regId,  @Param("id") Integer id);
}
