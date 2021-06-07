package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.BidVote;
import com.ejiaoyi.common.entity.ExpertUser;

import java.util.List;
import java.util.Map;

/**
 * 评标专家信息服务类
 *
 * @author yyb
 * @since 2020-08-25
 */
public interface IExpertUserService {

    /**
     * 通过id获取专家信息
     *
     * @param id 专家id
     * @return 专家
     */
    ExpertUser getExpertUserById(Integer id);


    /**
     * 通过专家名称和随机密码查询专家信息
     *
     * @param expertName 专家名称
     * @param pwd        随机密码
     * @return 专家
     */
    List<ExpertUser> getExpertUser(String expertName, String pwd);


    /**
     * 获取有候选资格的专家
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<ExpertUser> getExpertsOfCan(Integer bidSectionId);

    /**
     * 获取所有评标专家（某标段）
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<ExpertUser> getExpertsByBidSectionId(Integer bidSectionId);

    /**
     * 获取某一轮投票的专家信息
     *
     * @param bidSectionId 标段id
     * @param round        轮数
     * @return
     */
    List<ExpertUser> getExpertsOfRound(Integer bidSectionId, Integer round);

    /**
     * 推选
     *
     * @param bidVote         投票信息
     * @param bidSectionId    标段id
     * @param currentExpertId 当前专家id
     * @return
     * @throws Exception
     */
    Boolean chooseLeader(BidVote bidVote, Integer bidSectionId, Integer currentExpertId) throws Exception;

    /**
     * 投票情况
     *
     * @param bidSectionId 标段id
     * @param round        轮数
     * @return
     */
    Map<String, Object> heartBeatVote(Integer bidSectionId, Integer round);


    /**
     * 本轮是否投过票
     *
     * @param bidSectionId    标段id
     * @param currentExpertId 当前专家的id
     * @return
     */
    Boolean isVote(Integer bidSectionId, Integer currentExpertId);

    /**
     * 是否是第一轮，或者第一轮投票是否出结束
     *
     * @param bidSectionId 标段id
     * @return
     */
    Boolean isFirstRound(Integer bidSectionId);

    /**
     * 获取上一轮投票情况
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<ExpertUser> getLastRoundVote(Integer bidSectionId);

    /**
     * 获取同意承若书的所有评标专家（某标段）
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<ExpertUser> listExpertsByBidSectionId(Integer bidSectionId);

    /**
     * 获取没有回避的评标专家（某标段）
     *
     * @param bidSectionId 标段id
     * @return
     */
    Integer countExperts(Integer bidSectionId);

    /**
     * 根据id修改专家信息
     *
     * @param expertUser 专家信息
     * @return
     */
    Boolean updateExpertById(ExpertUser expertUser);

    /**
     * 获取当前标段的专家组长
     * @param bidSectionId - 标段Id
     * @return 专家组长
     */
    ExpertUser getChairmanByBidSectionId(Integer bidSectionId);

    /**
     * 获取签到的专家
     * @param id
     * @return
     */
    List<ExpertUser> listExpertUserSigin(Integer id);

    /**
     * 获取除了组长以外的其他专家
     *
     * @param bidSectionId 标段ID
     * @param expertId     组长Id
     * @return
     */
    List<ExpertUser> listExpertsExceptLeader(Integer bidSectionId, Integer expertId);

    /**
     * 获取已经结束个人评审的 专家id
     * @param bidSectionId 标段ID
     * @param gradeId 当前gradeID
     * @return 专家id
     */
    String listExpertIdsForPersonEnd(Integer bidSectionId, Integer gradeId);

    /**
     * 分页获取当前标段的所有专家
     * @param bidSectionId 标段主键
     * @return
     */
    String pageExpertUsers(Integer bidSectionId);

    /**
     * 获取标段的所有的专家 排除被删除以及回避的专家
     * @param bidSectionId 标段id
     * @return 所有的专家
     */
    List<ExpertUser> listExperts(Integer bidSectionId);

    /**
     * 根据条件查询专家信息
     * @param expertUser 查询条件
     * @return 专家信息
     */
    ExpertUser getExpertUser(ExpertUser expertUser);

    /**
     * 保存专家
     * @param expertUser 要插入的专家信息
     * @return 插入主键id
     */
    Integer saveExpert(ExpertUser expertUser);
}
