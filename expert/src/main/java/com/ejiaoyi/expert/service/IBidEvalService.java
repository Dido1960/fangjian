package com.ejiaoyi.expert.service;

import com.ejiaoyi.common.dto.BidderResultDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.ReviewType;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.expert.dto.ExpertUserMsgDTO;
import com.ejiaoyi.expert.enums.ExpertUserMsgStatus;
import com.ejiaoyi.expert.support.AuthUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评标 服务类
 * </p>
 *
 * @author yyb
 * @since 2020-11-05
 */
public interface IBidEvalService {
    /**
     * 投标人对应当前评审方法是否评审完毕 对应属性isGradeEnd
     * @param bidders
     * @param currentGrade
     * @param scoreType grade类型  参考scoreType
     * @return
     */
    List<Bidder> listBidderGradeIsEnd(List<Bidder> bidders, Grade currentGrade, String scoreType);

    /**
     * 根据条件获取评分明细内容
     *
     * @param bidSectionId 标段编号
     * @param gradeId     评分标准id
     * @param evalProcess  评审环节
     * @return
     */
    Grade getGradeDetailItem(Integer bidSectionId, Integer gradeId, Integer evalProcess);

    /**
     * 获取当前投标人的评审单项 （打分制）
     *
     * @param bidderId 投标人主键
     * @param gradeId  评审标准id
     * @return
     */
    List<ExpertReviewSingleItem> listItemByBidderId(Integer bidderId, Integer gradeId);

    /**
     * 保存或更新专家对企业评审单项评审结果
     *
     * @param expertReviewSingleItem
     * @return
     */
    Integer saveExpertReviewSingleItem(ExpertReviewSingleItem expertReviewSingleItem);

    /**
     * 初步评审 校验专家是否对某投标人评审完成
     *
     * @param gradeId  评分标准id
     * @param bidderId 投标人id
     * @return  校验专家是否对某投标人评审完成
     */
    boolean validBidderReviewComplete(Integer bidderId, Integer gradeId);


    /**
     * 专家对投标人该评审标准一键通过（合格制）
     *
     * @param gradeId  评审标准id
     * @param userId   专家id
     */
    void passAllQualified(Integer gradeId, Integer userId);

    /**
     * 获取开标合格投标人列表
     *
     * @param bidSectionId 标段id
     * @param gradeIds     评分标准（招标文件的评标办法）
     * @param evalProcess  评审环节
     * @return
     */
    List<Bidder> listPassOpenBidder(Integer bidSectionId, String[] gradeIds, Integer evalProcess);

    /**
     * 检测评标标准 是否所有专家都已经打分完毕
     *
     * @param bidSectionId 标段id
     * @param gradeId      评审id
     * @return
     */
    boolean validAllExpertEval(Integer bidSectionId, Integer gradeId);

    /**
     * 环节重新评审
     *
     * @param bidSectionId 标段id
     * @param evalProcess  评审环节
     * @return
     */
    void callPersonReview(Integer bidSectionId, Integer evalProcess);

    /**
     * 专家组长对其他专家 推送消息
     * @param bidSectionId 标段ID
     * @param evalProcess 评审环节
     */
    void expertUserMsgPush(Integer bidSectionId, Integer evalProcess, ExpertUserMsgStatus msgStatus);

    /**
     * 校验初步评审是否符合个人结束标志
     * @return 校验初步评审是否符合个人结束标志
     */
    JsonData validQualifiedEnd();

    /**
     * 获取不合格项的专家评审情况
     *
     * @return 获取不合格项的专家评审情况
     */
    List<ExpertReviewSingleItem> listUnqualifiedItem();

    /**
     * 个人评审结束
     *
     * @param bidSectionId 标段id
     * @param evalProcess  评审环节
     * @return
     */
    boolean updateReviewEnd(Integer bidSectionId, Integer evalProcess);

    /**
     * 获取不合格项的专家评审意见 (按照评分标准和投标人分类)
     *
     * @param bidSectionId 标段id
     * @param evalProcess 评审环节
     * @return
     */
    List<Bidder> listExpertOpinion(Integer bidSectionId,Integer evalProcess);

    /**
     *  资格预审 详细评审 验证小组结束相关条件
     * @return 是否可以小组结束
     */
    JsonData checkPreDetailedGroupEnd();

    /**
     * 评标委员会评审结束
     *
     * @param bidSectionId 标段id
     * @param evalProcess  评审环节
     * @throws CustomException
     */
    void updateGroupReviewEndStatus(Integer bidSectionId, Integer evalProcess) throws CustomException;

    /**
     * 获取通过初次评审的投标人
     * @param bidSectionId 标段ID
     * @return 投标人list
     */
    List<Bidder> listBidderPassPreliminary(Integer bidSectionId);

    /**
     * 获取当前grade 并封装当前投标人扣分制的实际打分情况
     * @param bidSectionId 标段ID
     * @param gradeId gradeID
     * @param evalProcess 评审类型
     * @param bidderId 投标人id
     * @return 获取当前grade 并封装当前投标人扣分制的实际打分情况
     */
    Grade getGradeForDeduct(Integer bidSectionId, Integer gradeId, Integer evalProcess, Integer bidderId);

    /**
     * 判断当前投标人当前grade扣分制完成情况
     * @param bidderId 投标人id
     * @param gradeId gradeId
     * @return 判断当前投标人当前grade扣分制完成情况
     */
    Boolean validBidderDeductCompletion(Integer bidderId, Integer gradeId);

    /**
     * 一键不扣分
     * @param gradeId gradeId
     * @return 更新数量
     */
    Integer oneKeyNoDeduct(Integer gradeId);

    /**
     * 检查个人扣分结束是否符合条件
     * @return 检查个人扣分结束是否符合条件
     */
    JsonData checkPersonalDeductEnd();

    /**
     * 检查是否有扣分项
     * @return 检查是否有扣分项
     */
    Boolean checkDeduct();

    /**
     * 按照投标人获取扣分项的列表
     * @return 按照投标人获取扣分项的列表
     */
    List<Bidder> listDeductOpinion();

    /**
     * 检查 施工扣分制是否可以小组结束
     * @return 施工扣分制是否可以小组结束
     */
    JsonData checkDeductGroupEnd();

    /**
     * 施工详细评审 小组结束
     * @return 小组结束
     */
    Boolean endDeductGroupReview();

    /**
     * 获取施工综合排名 的投标人列表
     * @param bidSectionId 标段id
     * @return 投标人列表
     */
    List<EvalResultSg> listConRankingBidder(Integer bidSectionId);

    /**
     * 生成评标报告
     * @param user 登录用户信息
     * @return 生成状态
     */
    Boolean generateReport(AuthUser user);

    /**
     * 查询bidder列表中bidder对于当前grade列表是否个人评审完成 用于监管详细评审
     * @param bidders 投标人列表
     * @param grades 评标方法列表
     * @param reviewType  评审类型
     * @return 分装后的biddes;
     */
    List<Bidder> listBidderGradesIsEnd(List<Bidder> bidders, List<Grade> grades, Integer reviewType);

    /**
     * 监理 保存详细评审单项结果
     * @param id 单项ID
     * @param score 大分分数
     * @param reviewType 评审类型
     * @return 保存结果
     */
    JsonData saveSupDetailedResult(Integer id, String score, Integer reviewType);

    /**
     * 监理  检查当前投标人当前评审类型是否打分完成
     * @param bidderId 投标人Id
     * @param reviewType 评审类型
     * @return 检查当前投标人当前评审类型是否打分完成
     */
    Boolean validSupBidderCompletion(Integer bidderId, Integer reviewType);

    /**
     * 监理 检查是否可以个人评审结束
     * @return 监理 检查是否可以个人评审结束
     */
    JsonData checkSupPersonalEnd();

    /**
     * 监理 一键不扣分
     */
    void supOneKeyNoDeduct();

    /**
     * 监理 获取投标人打分数据 以及评审点
     * @param bidderId 投标人ID
     * @param reviewType 评审类型
     * @return 监理 获取投标人打分数据 以及评审点
     */
    Map<String, Object> getSupBidderData(Integer bidderId, Integer reviewType);

    /**
     * 监理 检查是否可以结束小组评审
     * @return 监理 检查是否可以结束小组评审
     */
    JsonData checkSupGroupEnd();

    /**
     * 监理 结束小组评审
     * @return 结束小组评审
     */
    Boolean endSupGroupReview();

    /**
     * 监理 小组汇总 获取投标人结果数据
     * @param bidderId 投标人ID
     * @param reviewType 评审类型
     * @param isAllExpertEnd 是否所有专家都个人评审结束
     * @return 获取投标人结果数据
     */
    Map<String, Object> getSupBidderDataForResult(Integer bidderId, Integer reviewType, Integer isAllExpertEnd);

    /**
     * 获取监理综合排名数据
     * @param bidSectionId 标段ID
     * @return 获取监理综合排名数据
     */
    List<EvalResultJl> listSupRankingBidder(Integer bidSectionId);

    /**
     * 施工 获取详细评审投标人数据
     * @param gradeId gradeID
     * @param bidderId 投标人ID
     * @return 获取投标人数据
     */
    Map<String, Object> getConBidderData(Integer gradeId, Integer bidderId);

    /**
     * 获取施工详细评审小组评审结果数据
     * @param bidderId 投标人ID
     * @param gradeId gradeID
     * @param isAllExpertEnd 是否所有人都结束小组评审
     * @return 细评审小组评审结果数据
     */
    Map<String, Object> getConBidderDataForResult(Integer bidderId, Integer gradeId, Integer isAllExpertEnd);

    /**
     * 获取施工 每个投标人当前grade的最后结果
     * @param gradeId gradeId
     * @return 获取每个投标人当前grade的最后结果
     */
    List<BidderReviewResultDeduct> getConBiddersGradeResult(Integer gradeId);

    /**
     * 获取 合格制 投标人打分情况以及评审点
     * @param gradeId gradeId
     * @param bidderId 投标人ID
     * @return 投标人打分情况以及评审点
     */
    Map<String, Object> getPreBidderData(Integer gradeId, Integer bidderId);

    /**
     * 校验 初步评审 是否可以小组结束
     * @return 是否可以小组结束
     */
    JsonData checkPreGroupEnd();

    /**
     * 获取初步评审 每个投标人当前grade的最后结果
     * @param gradeId gradeId
     * @return 获取每个投标人当前grade的最后结果
     */
    List<BidderReviewResult> getPreBiddersGradeResult(Integer gradeId);

    /**
     * 获取合格制 当前投标人 小组评审结果数据
     * @param bidderId 投标人ID
     * @param gradeId gradeID
     * @param isAllExpertEnd 是否所有人都结束小组评审
     * @return 小组评审结果数据
     */
    Map<String, Object> getPreBidderDataForResult(Integer bidderId, Integer gradeId, Integer isAllExpertEnd);

    /**
     * 获取监理的评审类型列表，并计算总分
     * @return 获取监理的评审类型列表
     */
    List<ReviewType> listSupReviewType();

    /**
     * 监理 获取每个投标人当前reviewType的最后结果
     * @param reviewType reviewType
     * @return 获取每个投标人当前reviewType的最后结果
     */
    List<BidderResultDTO> getSupBiddersReviewResult(Integer reviewType);

    /**
     * 获取投标人报价得分
     * @param bidSectionId 标段信息
     * @return
     */
    List<Bidder> listBidderQuoteScore(Integer bidSectionId);

    /**
     * 判断资格审查能否个人结束
     * @return 判断资格审查能否个人结束
     */
    JsonData validQualifyReviewEnd();

    /**
     * 资格审查 获取不合格项的专家评审情况
     *
     * @return 资格审查 获取不合格项的专家评审情况
     */
    List<ExpertReviewSingleItem> listUnQualifyReviewItem();


    /**
     * 校验 资格审查 是否可以小组结束
     * @return 是否可以小组结束
     */
    JsonData checkQualifyGroupEnd();

    /**
     * 获取资格审查 每个投标人当前grade的最后结果
     * @param gradeId gradeId
     * @return 获取每个投标人当前grade的最后结果
     */
    List<BidderReviewResult> getQualifyBiddersGradeResult(Integer gradeId);

    /**
     * 判断当前bidder是否以及评审完毕
     * @param bidders 投标人列表
     * @param grades grade列表
     * @return 判断当前bidder是否已经评审完毕
     */
    List<Bidder> listBidderEpcGradesIsEnd(List<Bidder> bidders, List<Grade> grades);

    /**
     * 总承包详细评审获取投标人打分数据
     * @param bidderId 投标人Id
     * @return 总承包详细评审获取投标人打分数据
     */
    Map<String, Object> getEpcDetailedBidderData(Integer bidderId);

    /**
     * 总承包 详细评审 保存单项打分数据
     * @param id 结果ID
     * @param score 打分
     * @return 保存结果
     */
    JsonData saveEpcDetailedResult(Integer id, String score);

    /**
     * 总承包 详细评审 判断当前投标人是否打分完成
     * @param bidderId 投标人ID
     * @return 判断当前投标人是否打分完成
     */
    Boolean validEpcDetailedBidderCompletion(Integer bidderId);

    /**
     * 总承包 详细评审 判断是否可以个人结束
     * @return 判断是否可以个人结束
     */
    JsonData checkEpcDetailedPersonalEnd();

    /**
     * 总承包 详细评审 获取小组 投标人结果数据
     * @return 获取小组 投标人 结果数据
     */
    List<BidderResultDTO> getEpcDetailedGroupBiddersResult();

    /**
     * 总承包 详细评审 小组汇总 当前投标人 结果数据
     * @param bidderId 投标人ID
     * @param isAllExpertEnd 是否所有专家都个人评审结束
     * @return 获取小组 当前投标人 结果数据
     */
    Map<String, Object> getEpcDetailedGroupBidderResult(Integer bidderId, Integer isAllExpertEnd);

    /**
     * 总承包 详细评审 检查是否可以小组评审结束
     * @return 详细评审 检查是否可以小组评审结束
     */
    JsonData checkEpcDetailedGroupEnd();

    /**
     * 总承包 详细评审 小组评审结束
     * @return 详细评审 小组评审结束
     */
    Boolean endEpcDetailedGroupEnd();

    /**
     * 获取投标人修正得报价得分
     * @param bidSectionId 标段信息
     * @return
     */
    List<Bidder> listBidderQuoteAppendixScore(Integer bidSectionId);

    /**
     * 总承包 获取综合排名数据
     * @param bidSectionId 标段ID
     * @return 获取综合排名数据
     */
    List<EvalResultEpc> listEpcRankingBidder(Integer bidSectionId);

    /**
     * 获取合格投标人
     * @param bidSectionId 标段id
     * @param evalFlow 评标流程环节
     * @return 投标人集合
     */
    List<Bidder> listQualifiedBidder(Integer bidSectionId, Integer evalFlow);

    /**
     * 生成复会报告
     * @param bidSectionId 标段id
     */
    void generateResumptionReport(Integer bidSectionId);


    /**
     * 获取组长消息
     * @return
     */
    ExpertUserMsgDTO getExpertUserMsg();

    /**
     * 删除已读消息
     */
    void deleteExpertUserMsg();

    /**
     * 判断当前标段 是否有正在回退的请求
     * @param bidSectionId
     * @return
     */
    boolean isFreeBackApplying(Integer bidSectionId);
}
