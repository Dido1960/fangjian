package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.entity.ExpertReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 专家评审表 Mapper 接口
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
@Component
public interface ExpertReviewMapper extends BaseMapper<ExpertReview> {

    /**
     * 通过gradeID 结束专家的个人评审结果
     * @param gradeIds 所有的gradeIds
     * @param evalProcess 评审流程
     * @param expertId 专家Id
     * @return 是否更新成功
     */
    void updatePersonalReviewEnd(@Param("gradeIds") String[] gradeIds, @Param("evalProcess")  Integer evalProcess, @Param("expertId")  Integer expertId, @Param("endTime") String endTime);

    /**
     * 通过gradeID 对这些环节进行环节重评
     * @param gradeIds 所有的gradeIds
     * @param evalProcess 评审流程
     * @return 是否更新成功
     */
    void updateCallPersonReview(@Param("gradeIds") String[] gradeIds, @Param("evalProcess")  Integer evalProcess);
}
