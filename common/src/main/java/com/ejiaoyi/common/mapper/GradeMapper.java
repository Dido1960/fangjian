package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.dto.ProcessCompletionDTO;
import com.ejiaoyi.common.entity.Grade;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 评分标准表 Mapper 接口
 *
 * @author Z0001
 * @since 2020-07-03
 */
@Component
public interface GradeMapper extends BaseMapper<Grade> {
    /**
     * 获取所有评审环节完成情况
     *
     * @param gradeIds    评标办法ids
     * @param evalProcess 流程环节
     * @return
     */
    ProcessCompletionDTO getProcessCompletion(@Param("gradeIds") String[] gradeIds, @Param("evalProcess") Integer evalProcess);

    /**
     * 通过评审类型获取总分
     *
     * @param gradeIds    当前标段的所欲gradeID
     * @param evalProcess 当前的评审流程
     * @param reviewType  评审类型
     * @return 总分
     */
    String getGradeScoreByReviewType(@Param("gradeIds") String[] gradeIds, @Param("evalProcess") Integer evalProcess, @Param("reviewType") Integer reviewType);
}
