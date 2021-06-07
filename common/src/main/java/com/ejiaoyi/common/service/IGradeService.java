package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.ExpertReviewDetailDTO;
import com.ejiaoyi.common.dto.GradeDTO;
import com.ejiaoyi.common.dto.ParseEvalMethodDTO;
import com.ejiaoyi.common.dto.ProcessCompletionDTO;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.Grade;
import com.ejiaoyi.common.enums.ConDetailedMethod;
import com.ejiaoyi.common.enums.EvalProcess;

import java.util.List;


/**
 * 评标分数服务类
 *
 * @author Make
 * @since 2020-09-01
 */
public interface IGradeService {

    /**
     * 根据id获取评分标准
     *
     * @param id 评分标准主键id
     * @return 招标文件所有的评分标准
     */
    Grade getGradeById(Integer id);

    /**
     * 解析评标分数信息
     *
     * @param xmlPath 评标分数文件地址
     * @return 关联评标办法grade,多个评标办法间用逗号隔开
     * @throws Exception
     */
    ParseEvalMethodDTO addParseEvalMethodInfo(String xmlPath) throws Exception;

    /**
     * 评审环节完成情况
     * @param gradeIds 评标办法ids
     * @param evalProcess 流程环节
     * @return
     */
    ProcessCompletionDTO getProcessCompletion(String[] gradeIds, Integer evalProcess);

    /**
     * 根据招标文件评分标准ids获取所有评分标准
     *
     * @param ids           评分标准主键id数组
     * @param reviewProcess 评审阶段
     * @return 招标文件所有的评分标准
     */
    List<Grade> listGrade(String[] ids, Integer reviewProcess);

    /**
     * 根据招标文件评分标准ids获取所有评分标准
     *
     * @param gradeId           评分标准主键id
     * @param reviewProcess 评审阶段
     * @return 招标文件所有的评分标准
     */
    Grade getGrade(Integer gradeId, Integer reviewProcess);

    /**
     * 根据id修改评分标准
     *
     * @param grade 评分标准
     * @return 招标文件所有的评分标准
     */
    boolean updateGrade(Grade grade);

    /**
     * 通过gradeID数组以及方法名称 获取对应的grade
     * @param gradeIds ID数组
     * @param methodName 对应的方法名对象
     * @return grade
     */
    Grade getGradeByIdsAndName(String[] gradeIds, ConDetailedMethod methodName);

    /**
     * 通过评审类型获取grade
     * @param gradeIds 当前标段的所欲gradeID
     * @param evalProcess 当前的评审流程
     * @param reviewType 评审类型
     * @return gradeID
     */
    List<Grade> listGradeByReviewType(String[] gradeIds, Integer evalProcess, Integer reviewType);

    /**
     * 通过评审类型获取总分
     * @param gradeIds
     * @param evalProcess
     * @param reviewType
     * @return
     */
    String getGradeScoreByReviewType(String[] gradeIds, Integer evalProcess, Integer reviewType);

    /**
     * 获取单个评分标准DTO 合格制
     *
     * @param gradeId      评分标准id
     * @param bidSectionId 标段id
     * @param bidders      投标人集合
     * @return 单个评分标准DTO
     */
    GradeDTO getGradeDto(Integer gradeId, Integer bidSectionId, List<Bidder> bidders);

    /**
     * 获取单个评分标准DTO 打分制
     *
     * @param gradeId      评分标准id
     * @param bidSectionId 标段id
     * @param bidders      投标人集合
     * @return 单个评分标准DTO
     */
    GradeDTO getGradeDtoScore(Integer gradeId, Integer bidSectionId, List<Bidder> bidders);

    /**
     * 获取单个评分标准DTO 违章行为
     *
     * @param gradeId      评分标准id
     * @param bidSectionId 标段id
     * @param bidders      投标人集合
     * @return 单个评分标准DTO
     */
    GradeDTO getGradeViolationDto(Integer gradeId, Integer bidSectionId, List<Bidder> bidders);

    /**
     * 获取单个评分标准DTO 扣分表
     *
     * @param gradeId      评分标准id
     * @param bidSectionId 标段id
     * @param bidders      投标人集合
     * @return 单个评分标准DTO
     */
    GradeDTO getGradeDtoDeduct(Integer gradeId, Integer bidSectionId, List<Bidder> bidders);

    /**
     * 获取 “某评分项”的“所有专家”对“某投标人”的评审明细 打分制
     * @param bidSectionId 标段id
     * @param bidderId    投标人id
     * @param gradeItemId 评分项id
     * @return
     */
    List<ExpertReviewDetailDTO> listAllExpertGradeItemScore(Integer bidSectionId, Integer bidderId, Integer gradeItemId);

    /**
     * 获取当前评审类型的gradeids字符串
     * @param gradeIds 所有的gradeID
     * @param evalProcess 评审流程
     * @return gradeids字符串
     */
    String[] getGradeIds(String[] gradeIds, Integer evalProcess);

    /**
     * 批量更新 小组结束状态
     * @param ids id数组
     * @param status 小组结束状态
     * @return
     */
    Integer updateGroupEndByIds(Integer[] ids, Integer status);

    /**
     * 插入grade
     * @param grade 数据
     * @return 插入后ID
     */
    Integer insertGrade(Grade grade);
}
