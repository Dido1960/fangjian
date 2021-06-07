package com.ejiaoyi.expert.controller;


import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.ExecuteCode;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc  评委组长推选控制器
 * @author  yyb
 * @date  2020-8-25 14:54
 */
@RestController
@RequestMapping("/selectLeader")
public class SelectLeaderController {

    @Autowired
    private IExpertUserService expertUserService;

    @Autowired
    private IBidApplyService bidApplyService;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IBidderService bidderService;


    /**
     * 跳转专家评标诚信承诺书页面
     *
     * @return
     */
    @RequestMapping("/commitBookPage")
    public ModelAndView commitBook() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView modelAndView = new ModelAndView();

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        // 纸质标直接进入评标
        if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
            modelAndView.setViewName("redirect:/paper/paperEvalPage");
            return modelAndView;
        }
        ExpertUser expertUser = expertUserService.getExpertUserById(user.getUserId());
        if (!CommonUtil.isEmpty(expertUser.getAvoid()) && Enabled.NO.getCode().toString().equals(expertUser.getAvoid())){
            //专家已点击确认
            ExpertUser chairman = expertUserService.getChairmanByBidSectionId(bidSectionId);
            if (CommonUtil.isEmpty(chairman)){
                //组长推选
                modelAndView.setViewName("redirect:/selectLeader/selectLeaderPage");
            }else {
                if (Status.PROCESSING.getCode().equals(bidSection.getEvalStatus())){
                    //已经开始评标
                    modelAndView.setViewName("redirect:/expert/startEval");
                }else {
                    //等待开始评标
                    modelAndView.setViewName("redirect:/expert/confirmBidEvalPage");
                }
            }
        }else {
            modelAndView.setViewName("/selectLeader/commitBook");
        }

        List<Bidder> bidders = bidderService.listPassBidOpenBidder(bidSectionId);
        LocalDateTime now = LocalDateTime.now();
        modelAndView.addObject("now",now);
        modelAndView.addObject("user",user);
        modelAndView.addObject("bidSection", bidSection);
        modelAndView.addObject("bidders", bidders);
        return modelAndView;
    }

    /**
     * 跳转专家回避页面
     *
     * @return
     */
    @RequestMapping("/avoidPage")
    public ModelAndView avoidPage() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView modelAndView = new ModelAndView("/selectLeader/avoid");
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        modelAndView.addObject("bidSection", bidSection);
        return modelAndView;
    }

    /**
     * 跳转评委组长推选页面
     *
     * @return
     */
    @RequestMapping("/selectLeaderPage")
    public ModelAndView selectLeaderPage() {
        ModelAndView modelAndView = new ModelAndView("/selectLeader/selectLeaderPage");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        ExpertUser expertUser = expertUserService.getExpertUserById(user.getUserId());
        Boolean isFirstRound = expertUserService.isFirstRound(bidSectionId);
        Boolean isVote = expertUserService.isVote(bidSectionId, expertUser.getId());
        BidApply bidApply = bidApplyService.getBidApplyByBidSectionId(bidSectionId);
        String voteOver = (bidApply != null && !CommonUtil.isEmpty(bidApply.getChairMan())) ? "" : "1";
        Integer voteRound = (bidApply != null && !CommonUtil.isEmpty(bidApply.getVoteCount())) ? bidApply.getVoteCount() : 1;
        modelAndView.addObject("voteOver", voteOver);
        modelAndView.addObject("isVote", isVote ? "1" : "");
        modelAndView.addObject("isFirstVote", isFirstRound ? "" : "1");
        modelAndView.addObject("voteRound", voteRound);
        modelAndView.addObject("bidSectionId", bidSectionId);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    /**
     * 专家投票
     *
     * @param bidVote 投票信息
     * @return
     */
    @RequestMapping("/chooseLeader")
    public boolean chooseLeader(BidVote bidVote) {
        try {
            AuthUser user = CurrentUserHolder.getUser();
            assert user != null;
            ExpertUser expertUser = expertUserService.getExpertUserById(user.getUserId());
            Integer currentExpertId = expertUser.getId();
            return expertUserService.chooseLeader(bidVote, user.getBidSectionId(), currentExpertId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 心跳数据
     *
     * @param round 轮数
     * @return
     */
    @RequestMapping("/heartBeatVote")
    public Map<String, Object> heartBeatVote(Integer round) {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        return expertUserService.heartBeatVote(bidSectionId, round);
    }

    /**
     * 获取上一次投票信息
     *
     * @return
     */
    @RequestMapping("/showLastVote")
    public List<ExpertUser> showLastVoteInfo() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        return expertUserService.getLastRoundVote(user.getBidSectionId());
    }

    /**
     * 获取所有评标专家
     *
     * @return
     */
    @RequestMapping("/listAllExperts")
    public JsonData listAllExperts() {
        JsonData jsonData = new JsonData();
//        try {
            AuthUser user = CurrentUserHolder.getUser();
            Integer bidSectionId = user.getBidSectionId();
            jsonData.setCode(ExecuteCode.SUCCESS.getCode().toString());
            Map<String, Object> data = new HashMap<>();
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            //获取同意承若书的专家信息
            data.put("experts", expertUserService.listExpertsByBidSectionId(bidSectionId));
            //获取没有回避的专家数目
            data.put("countExperts", expertUserService.countExperts(bidSectionId));
            //招标文件规定的专家数目
            data.put("expertCount", tenderDoc.getExpertCount());

            jsonData.setData(data);
//        } catch (Exception e) {
//            e.printStackTrace();
//            jsonData.setCode(ExecuteCode.FAIL.getCode().toString());
//            jsonData.setMsg(e.getMessage());
//        }

        return jsonData;
    }


}
