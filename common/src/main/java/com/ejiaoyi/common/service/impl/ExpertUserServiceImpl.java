package com.ejiaoyi.common.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.mapper.ExpertUserMapper;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 标段信息 服务实现类
 *
 * @author yyb
 * @since 2020-8-25
 */
@Service
public class ExpertUserServiceImpl extends BaseServiceImpl implements IExpertUserService {

    @Autowired
    private ExpertUserMapper expertUserMapper;

    @Autowired
    private IBidApplyService bidApplyService;

    @Autowired
    private IBidVoteService bidVoteService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IExpertReviewService expertReviewService;

    @Override
    public ExpertUser getExpertUserById(Integer id) {
        return expertUserMapper.selectById(id);
    }

    @SneakyThrows
    @Override
    public List<ExpertUser> getExpertUser(String expertName, String pwd) {
        List<ExpertUser> expertUsers = expertUserMapper.listExpertUser(expertName);
        Iterator<ExpertUser> iterator = expertUsers.iterator();
        while (iterator.hasNext()){
            ExpertUser expert = iterator.next();
            String decryptPwd = SM2Util.decrypt(expert.getPwd());
            if (!decryptPwd.equals(SM2Util.decrypt(pwd))) {
                iterator.remove();
            }
        }
        return expertUsers;
    }

    @Override
    public List<ExpertUser> getExpertsOfCan(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<ExpertUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        queryWrapper.notIn("LEADER_STATUS", 0);
        queryWrapper.eq("ENABLED", 1);
        List<ExpertUser> expertUsers = expertUserMapper.selectList(queryWrapper);

        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        if (bidApply != null) {
            List<BidVote> bidVoteList = bidVoteService.getBidVoteList(bidApply.getId(), bidApply.getVoteCount());
            Map<Integer, Integer> expertCountMap = new HashMap<>();
            for (BidVote bidVote : bidVoteList) {
                expertCountMap.put(bidVote.getBidExpertId(), bidVote.getCount());
            }
            //获取候选专家的得票数
            for (ExpertUser expertUser : expertUsers) {
                Integer count = expertCountMap.get(expertUser.getId());
                count = count == null ? 0 : count;
                expertUser.setCount(count);
            }
        }
        return expertUsers;
    }

    @Override
    public List<ExpertUser> getExpertsByBidSectionId(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<ExpertUser> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("ENABLED", 1);
        wrapper.orderByDesc("LEADER_STATUS");
        return expertUserMapper.selectList(wrapper);
    }

    @Override
    public List<ExpertUser> getExpertsOfRound(Integer bidSectionId, Integer round) {
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        if (bidApply != null) {
            List<BidVote> bidVoteList = bidVoteService.getBidVoteList(bidApply.getId(), round);

            if (bidVoteList.size() > 0) {
                List<Object> data = voteInfo(bidVoteList);
                QueryWrapper<ExpertUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("BID_SECTION_ID", bidSectionId);
                queryWrapper.in("ID", (List<Integer>)data.get(1));
                queryWrapper.eq("ENABLED", 1);
                List<ExpertUser> expertUsers = expertUserMapper.selectList(queryWrapper);

                //获取候选专家的得票数
                Map<Integer, Integer> countMap = (Map<Integer, Integer>) data.get(0);
                for (ExpertUser expertUser : expertUsers) {
                    Integer count = countMap.get(expertUser.getId());
                    count = count == null ? 0 : count;
                    expertUser.setCount(count);
                }
            }
        }
        return null;
    }


    @Override
    @RedissonLock(key = "#bidVote.bidExpertId")
    public Boolean chooseLeader(BidVote bidVote, Integer bidSectionId, Integer currentExpertId) throws Exception {
        // 判断是否投过票
        if (isVote(bidSectionId, currentExpertId)){
            return false;
        }
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        Boolean flag ;
        //当前投票专家不是第一个投票人
        if (bidApply == null) {
            if (!bidApplyService.insert(bidSectionId)) {
                return false;
            }
            bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        }
        //初始化投票轮次为第一轮
        if (CommonUtil.isEmpty(bidApply.getVoteCount())){
            bidApplyService.updateBidApplyById(BidApply.builder()
                    .id(bidApply.getId())
                    .voteCount(1)
                    .build());
            bidApply.setVoteCount(1);

        }

        bidVote.setBidApplyId(bidApply.getId());
        bidVote.setVoteRound(bidApply.getVoteCount());
        BidVote oldBidVote = bidVoteService.getBidVote(bidVote);
        //被投票专家是否第一次被投
        if (oldBidVote != null) {
            BidVote newBidVote = BidVote.builder()
                    .id(oldBidVote.getId())
                    .count(oldBidVote.getCount() + 1)
                    .votePerson(oldBidVote.getVotePerson() + "," + currentExpertId)
                    .build();
            flag = bidVoteService.updateBidVoteById(newBidVote);
        } else {
            bidVote.setBidApplyId(bidApply.getId());
            bidVote.setVoteRound(bidApply.getVoteCount());
            bidVote.setVotePerson(currentExpertId.toString());
            bidVote.setCount(1);
            bidVote.setEnabled(1);
            flag = bidVoteService.insertBidVote(bidVote);
        }
        //如果本轮（本次）投票结束，淘汰票数低的
        if (flag) {
            endCurrentRoundVote(bidApply);
        }

        return flag;
    }

    @Override
    public Map<String, Object> heartBeatVote(Integer bidSectionId, Integer round) {
        Map<String, Object> voteSituation = new HashMap<>();
        // 该标段所有评标专家信息
        List<ExpertUser> listExperts = listExpertsByBidSectionId(bidSectionId);
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        // 判断投票是否结束
        Boolean voteOver = bidApply != null && !CommonUtil.isEmpty(bidApply.getChairMan());
        boolean roundChange = false;
        Integer currentRound = 1;
        if (bidApply != null && !CommonUtil.isEmpty(bidApply.getVoteCount())) {
            roundChange = !round.equals(bidApply.getVoteCount());
            currentRound = bidApply.getVoteCount();
        }
        //结束投票
        //专家组长的信息
        if (voteOver) {
            ExpertUser leaderExpertUser = getExpertUserById(bidApply.getChairMan());
            voteSituation.put("leaderExpertUser", leaderExpertUser);
        }
        voteSituation.put("listExperts", listExperts);
        voteSituation.put("voteOver", voteOver);
        voteSituation.put("currentRound", currentRound);
        voteSituation.put("roundChange", roundChange);
        return voteSituation;

    }

    @Override
    public Boolean isVote(Integer bidSectionId, Integer currentExpertId) {
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        if (bidApply != null) {
            List<BidVote> bidVoteList = bidVoteService.getBidVoteList(bidApply.getId(), bidApply.getVoteCount());
            if (bidVoteList.size() > 0) {
                List<Object> data = voteInfo(bidVoteList);
                List<Integer> votedExpertIds = (List<Integer>) data.get(1);
                return votedExpertIds.contains(currentExpertId);
            }
        }
        return false;
    }

    @Override
    public Boolean isFirstRound(Integer bidSectionId) {
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        if (bidApply != null) {
            if (!CommonUtil.isEmpty(bidApply.getChairMan())) {
                return false;
            }
            return CommonUtil.isEmpty(bidApply.getVoteCount()) || bidApply.getVoteCount() == 1;
        }
        return true;
    }

    @Override
    public List<ExpertUser> getLastRoundVote(Integer bidSectionId) {
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        Integer voteCount = bidApply.getVoteCount();
        if (CommonUtil.isEmpty(voteCount) || voteCount == 1) {
            voteCount = 1;
        } else {
            voteCount = voteCount - 1;
        }
        List<BidVote> bidVoteList = bidVoteService.getBidVoteList(bidApply.getId(), voteCount);

        if (bidVoteList.size() > 0) {
            List<Object> data = voteInfo(bidVoteList);
            QueryWrapper<ExpertUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("ID", (List<Integer>) data.get(1));
            List<ExpertUser> expertUsers = expertUserMapper.selectList(queryWrapper);

            Map<Integer, Integer> countMap = (Map<Integer, Integer>) data.get(0);
            for (ExpertUser expertUser : expertUsers) {
                expertUser.setCount(0);
                if (countMap.containsKey(expertUser.getId())) {
                    expertUser.setCount(countMap.get(expertUser.getId()));
                }
            }

            return expertUsers;
        }

        return null;
    }

    /**
     * 结束投票（本轮or本次）
     *
     * @param bidApply 评标申请记录
     */
    private void endCurrentRoundVote(BidApply bidApply) {
        List<ExpertUser> expertsOfCan = listExpertsByBidSectionId(bidApply.getBidSectionId());
        List<BidVote> bidVoteList = bidVoteService.getBidVoteList(bidApply.getId(), bidApply.getVoteCount());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidApply.getBidSectionId());
        //当前总的投票数人数
        int votePersonTotal = 0;
        //最大得票数
        int maxCount = 0;
        for (BidVote vote : bidVoteList) {
            votePersonTotal += vote.getCount();
            if (maxCount < vote.getCount()) {
                maxCount = vote.getCount();
            }
        }

        //当前投票专家为最后一名投票人
        if (tenderDoc.getExpertCount() == votePersonTotal) {
            //得票最多的专家id
            List<Integer> maxCountExpertIds = new ArrayList<>();
            for (BidVote vote : bidVoteList) {
                if (maxCount == vote.getCount()) {
                    maxCountExpertIds.add(vote.getBidExpertId());
                }
            }
            //改变专家下一轮投票的候选资格
            for (ExpertUser expertUser : expertsOfCan) {
                if (!maxCountExpertIds.contains(expertUser.getId())) {
                    ExpertUser expert = ExpertUser.builder()
                            .id(expertUser.getId())
                            .leaderStatus("0")
                            .build();
                    expertUserMapper.updateById(expert);
                }
            }
            BidApply bidApply1 = new BidApply();
            bidApply1.setId(bidApply.getId());
            if (maxCountExpertIds.size() > 1) {
                //进入下一轮
                bidApply1.setVoteCount((bidApply.getVoteCount() == null ? 1 : bidApply.getVoteCount()) + 1);
            } else {
                //设置专家组长id
                bidApply1.setChairMan(maxCountExpertIds.get(0));
                ExpertUser expertUser = ExpertUser.builder()
                        .id(maxCountExpertIds.get(0))
                        .isChairman("1")
                        .build();
                this.updateExpertById(expertUser);
            }
            bidApplyService.updateBidApplyById(bidApply1);
        }
    }

    /**
     * 获取专家的得票数
     * 获取投票的专家id
     *
     * @param bidVoteList 投票信息
     * @return
     */
    private List<Object> voteInfo(List<BidVote> bidVoteList) {
        List<Object> data = new ArrayList<>();
        StringBuilder votedStr = new StringBuilder();
        Map<Integer, Integer> expertCountMap = new HashMap<>();
        for (BidVote bidVote : bidVoteList) {
            votedStr.append(bidVote.getVotePerson()).append(",");
            expertCountMap.put(bidVote.getBidExpertId(), bidVote.getCount());
        }
        String voteExpertIdStr = votedStr.toString();
        voteExpertIdStr = voteExpertIdStr.substring(0, voteExpertIdStr.length() - 1);
        String[] votedExpertIdArr = voteExpertIdStr.split(",");
        List<Integer> votedExpertIds = new ArrayList<>();
        for (String votedExpertId : votedExpertIdArr) {
            votedExpertIds.add(Integer.parseInt(votedExpertId));
        }

        data.add(expertCountMap);
        data.add(votedExpertIds);

        return data;

    }
    @Override
    public List<ExpertUser> listExpertsByBidSectionId(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        QueryWrapper<ExpertUser> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("AVOID", 0);
        wrapper.eq("ENABLED", 1);
        wrapper.orderByDesc("LEADER_STATUS");

        //获取评标专家最新一轮的的票情况
        List<ExpertUser> expertUsers = expertUserMapper.selectList(wrapper);
        Map<Integer, Integer> expertCountMap = new HashMap<>();
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        if (bidApply != null) {
            List<BidVote> bidVoteList = bidVoteService.getBidVoteList(bidApply.getId(), bidApply.getVoteCount());
            for (BidVote bidVote : bidVoteList) {
                expertCountMap.put(bidVote.getBidExpertId(), bidVote.getCount());
            }
        }
        //获取候选专家的得票数
        for (ExpertUser expertUser : expertUsers) {
            Integer count = expertCountMap.get(expertUser.getId());
            count = count == null ? 0 : count;
            expertUser.setCount(count);
        }

        return expertUsers;

    }

    @Override
    public Integer countExperts(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null!");
        return expertUserMapper.countExperts(bidSectionId);
    }

    @Override
    public Boolean updateExpertById(ExpertUser expertUser) {
        return expertUserMapper.updateById(expertUser) > 0;
    }

    @Override
    public ExpertUser getChairmanByBidSectionId(Integer bidSectionId) {
        QueryWrapper<ExpertUser> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("AVOID", 0);
        wrapper.eq("ENABLED", 1);
        wrapper.eq("IS_CHAIRMAN", "1");
        return expertUserMapper.selectOne(wrapper);
    }

    @Override
    public List<ExpertUser> listExpertUserSigin(Integer id) {
        QueryWrapper<ExpertUser> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID",id);
        wrapper.eq("AVOID",0);
        wrapper.eq("ENABLED",1);
        return expertUserMapper.selectList(wrapper);
    }

    @Override
    public List<ExpertUser> listExpertsExceptLeader(Integer bidSectionId, Integer expertId) {
        QueryWrapper<ExpertUser> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
//        wrapper.eq("AVOID", 0);
        wrapper.eq("ENABLED", 1);
        wrapper.ne("ID", expertId);
        return expertUserMapper.selectList(wrapper);
    }

    @Override
    public String listExpertIdsForPersonEnd(Integer bidSectionId, Integer gradeId) {
        StringBuilder result = new StringBuilder();
        List<ExpertUser> expertUsers = listExpertsByBidSectionId(bidSectionId);
        for (ExpertUser expertUser : expertUsers) {
            ExpertReview query = ExpertReview.builder()
                    .gradeId(gradeId)
                    .bidSectionId(bidSectionId)
                    .expertId(expertUser.getId())
                    .build();
            ExpertReview expertReview = expertReviewService.getExpertReview(query);
            if (!CommonUtil.isEmpty(expertReview) && Enabled.YES.getCode().equals(expertReview.getEnabled())){
                result.append(expertUser.getId()).append(",");
            }
        }
        if (result.length() > 0){
            result.substring(result.length() -1);
        }
        return result.toString();
    }

    @Override
    public String pageExpertUsers(Integer bidSectionId) {
        if(!CommonUtil.isEmpty(bidSectionId)){
            Page page = this.getPageForLayUI();
            QueryWrapper<ExpertUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("BID_SECTION_ID",bidSectionId);
            queryWrapper.eq("AVOID",0);
            // 未删除的专家
            queryWrapper.eq("ENABLED",1);
            List<ExpertUser> list = expertUserMapper.selectList(queryWrapper);
            return this.initJsonForLayUI(list, (int) page.getTotal());
        }
        return null;
    }

    @Override
    public List<ExpertUser> listExperts(Integer bidSectionId) {
        QueryWrapper<ExpertUser> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.and(sql ->sql.isNull("AVOID").or().ne("AVOID",1));
        wrapper.eq("ENABLED", 1);
        return expertUserMapper.selectList(wrapper);
    }

    @Override
    public ExpertUser getExpertUser(ExpertUser expertUser) {
        QueryWrapper<ExpertUser> wrapper = new QueryWrapper<>();
        wrapper.eq(expertUser.getBidSectionId() != null, "BID_SECTION_ID", expertUser.getBidSectionId());
        wrapper.eq(StringUtils.isNotEmpty(expertUser.getIdCard()), "ID_CARD", expertUser.getIdCard());
        wrapper.eq(StringUtils.isNotEmpty(expertUser.getPhoneNumber()), "PHONE_NUMBER", expertUser.getPhoneNumber());
        wrapper.eq(StringUtils.isNotEmpty(expertUser.getExpertName()), "EXPERT_NAME", expertUser.getExpertName());
        return expertUserMapper.selectOne(wrapper);
    }

    @Override
    public Integer saveExpert(ExpertUser expertUser) {
        expertUserMapper.insert(expertUser);
        return expertUser.getId();
    }
}
