package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.ExpertReview;
import com.ejiaoyi.common.entity.ExpertReviewMutual;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ejiaoyi.common.entity.GradeItem;

import java.util.List;

/**
 * <p>
 * 专家对企业评审单项评审结果 互保共建 服务类
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-12-28
 */
public interface IExpertReviewMutualService  extends IService<ExpertReviewMutual>{

    /**
     * 专家打分项初始化
     * @param bidSectionId 标段id
     * @param expertId     专家id
     * @param gradeId 当前gradeId
     * @param bidders      包含的投标人
     * @return 专家打分项初始化
     */
    ExpertReview initReviewMutual(Integer bidSectionId, Integer expertId, Integer gradeId, List<Bidder> bidders);

    /**
     * 通过条件查询结果
     * @param expertReviewMutual 条件
     * @param isHasResult 是否查询有结果的 数据列
     * @return 结果列表
     */
    List<ExpertReviewMutual> listExpertReviewMutual(ExpertReviewMutual expertReviewMutual, boolean isHasResult);

    /**
     * 通过 对结果 以及 投标人分租 判断评审是否一致
     * bidderId 为null 获取所有专家对于所有人投标人打分是否一致 返回list数据量 ！= 总bidders数据量 表示不一致
     * bidderID 不为null 获取所有专家 对于当前投标人打分是否一致 返回list数据量 ！= 1 表示 不一致
     * @param gradeId (不可为空) gradeID
     * @param bidderId 投标人ID
     * @return 结束数据
     */
    List<ExpertReviewMutual> listMutualResultGroup(Integer gradeId, Integer bidderId);

    /**
     * 批量删除 数据
     * @param gradeIds gradeID
     * @return 删除量
     */
    Integer deleteByGradeIds(Integer[] gradeIds);
}
