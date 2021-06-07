package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.entity.ExpertReviewMutual;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 互保共建 Mapper 接口
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-12-28
 */
@Component
public interface ExpertReviewMutualMapper extends BaseMapper<ExpertReviewMutual> {

    /**
     * 通过条件查询结果
     * @param expertReviewMutual 条件
     * @param isHasResult 是否查询有结果的 数据列
     * @return 结果列表
     */
    List<ExpertReviewMutual> listExpertReviewMutual(@Param("expertReviewMutual") ExpertReviewMutual expertReviewMutual, @Param("isHasResult") boolean isHasResult);

    /**
     * 通过 对结果 以及 投标人分租 判断评审是否一致
     * bidderId 为null 获取所有专家对于所有人投标人打分是否一致 返回list数据量 ！= 总bidders数据量 表示不一致
     * bidderID 不为null 获取所有专家 对于当前投标人打分是否一致 返回list数据量 ！= 1 表示 不一致
     * @param gradeId (不可为空) gradeID
     * @param bidderId 投标人ID
     * @return 结束数据
     */
    List<ExpertReviewMutual> listMutualResultGroup(@Param("gradeId") Integer gradeId, @Param("bidderId") Integer bidderId);
}
