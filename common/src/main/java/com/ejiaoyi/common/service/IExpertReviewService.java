package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.ExpertReview;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 专家评审表 服务类
 * </p>
 *
 * @author Make
 * @since 2020-11-05
 */
public interface IExpertReviewService extends IService<ExpertReview> {

    /**
     * 获取专家评审表信息
     *
     * @param query 查询条件
     * @return 专家评审表信息
     */
    ExpertReview getExpertReview(ExpertReview query);

    /**
     * 获取已经评审完毕的专家评审表信息
     *
     * @param gradeId 评审类别
     * @return 已经评审完毕的专家评审表信息
     */
    List<ExpertReview> listCompleteExpertReviewInfo(Integer gradeId);

    /**
     * 获取已经评审完毕的专家评审表信息
     *
     * @param bidSectionId 标段主键
     * @param expertId 专家主键
     * @param gradeId 评审主键
     * @return 已经评审完毕的专家评审表信息
     */
    List<ExpertReview> listCompleteExpertReviewInfo(Integer bidSectionId, Integer expertId, Integer gradeId);

    /**
     * 修改专家评审表
     *
     * @param expertReview 专家评审表
     * @return
     */
    boolean updateExpertReview(ExpertReview expertReview);

    /**
     * 插入数据
     * @param expertReview 数据
     * @return 插入数据
     */
    Integer insertExpertReview(ExpertReview expertReview);

    /**
     * 通过gradeID 结束专家的个人评审结果
     * @param gradeIds 所有的gradeIds
     * @param evalProcess 评审流程
     * @param expertId 专家Id
     * @return 是否更新成功
     */
    boolean updatePersonalReviewEnd(String[] gradeIds, Integer evalProcess, Integer expertId);

    /**
     * 通过gradeID 对这些环节进行环节重评
     * @param gradeIds 所有的gradeIds
     * @param evalProcess 评审流程
     * @return 是否更新成功
     */
    boolean updateCallPersonReview(String[] gradeIds, Integer evalProcess);

    /**
     * 批量更新当前标段 当前grades的enabled数据
     * @param gradeIds gradeIDs
     * @param bidSectionId 标段id
     * @param enabled 是否小组结束
     * @return
     */
    Integer updateAllExpertReview(Integer[] gradeIds, Integer bidSectionId, int enabled);


    /**
     * 批量删除
     * @param gradeIds gradeIDs
     * @param bidSectionId 标段id
     * @return
     */
    Integer deleteByGradeIds(Integer[] gradeIds, Integer bidSectionId);
}
