package com.ejiaoyi.expert.controller;

import com.ejiaoyi.common.constant.EvalStatus;
import com.ejiaoyi.common.constant.ExpertStatus;
import com.ejiaoyi.common.dto.WebSocketMessage;
import com.ejiaoyi.common.entity.BidApply;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.ExpertUser;
import com.ejiaoyi.common.enums.Enabled;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.service.IBidApplyService;
import com.ejiaoyi.common.service.IBidSectionService;
import com.ejiaoyi.common.service.IExpertUserService;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 登录控制器
 *
 * @author Z0001
 * @since 2020/3/26
 */
@RestController
public class LoginController extends BaseController {

    @Autowired
    IExpertUserService expertUserService;

    @Autowired
    IBidApplyService bidApplyService;

    @Autowired
    IBidSectionService bidSectionService;

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping("/login.html")
    public ModelAndView loginPage() {
        return new ModelAndView("/login");
    }

    /**
     * 系统首页
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView indexPage(Integer bidSectionId) {
        AuthUser user = CurrentUserHolder.getUser();
        ModelAndView mv = new ModelAndView();

        Map<Integer, Integer> sectionIdAndUserId = user.getSectionIdAndUserId();
        BidApply bidApply = null;
        BidSection bidSection = null;
        //如果匹配多个，跳转列表页面
        if (sectionIdAndUserId.size() > 1 && bidSectionId == null) {
            List<BidSection> listBidSections = new ArrayList<>();
            for (Integer key : sectionIdAndUserId.keySet()) {
                BidSection bidSectionById = bidSectionService.getBidSectionById(key);
                if (!CommonUtil.isEmpty(bidSectionById)) {
                    listBidSections.add(bidSectionById);
                }
            }
            if (listBidSections.size() > 1) {
                mv.setViewName("/index");
                mv.addObject("user", user);
                mv.addObject("listBidSections", listBidSections);
                return mv;
            }
            if (listBidSections.size() == 0) {
                setSystemActionMessage("未查询的评审标段！", WebSocketMessage.Dialog.ALERT, WebSocketMessage.Type.ERROR);
                mv.setViewName("redirect:/login.html");
                return mv;
            }
            bidSection = listBidSections.get(0);
            if (!Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
                bidApply = bidApplyService.getBidApplyByBidSectionId(bidSection.getId());
            }
            bidSectionId = bidSection.getId();
        } else {
            bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());
            if (!Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
                bidApply = bidApplyService.getBidApplyByBidSectionId(user.getBidSectionId());
            }
        }

        //设置专家信息为当前评标的标段
        if (bidSectionId != null) {
            Integer userId = sectionIdAndUserId.get(bidSectionId);
            ExpertUser expertUser = expertUserService.getExpertUserById(userId);
            user.setBidSectionId(bidSectionId);
            user.setUserId(userId);
            user.setIsChairman(expertUser.getIsChairman());
        }
        // 如果匹配一个，直接进入
        ExpertUser expert = expertUserService.getExpertUserById(user.getUserId());

        if (CommonUtil.isEmpty(bidSection)) {
            setSystemActionMessage("未查询的评审标段！", WebSocketMessage.Dialog.ALERT, WebSocketMessage.Type.ERROR);
            mv.setViewName("redirect:/login.html");
            return mv;
        }

        if (!Status.END.getCode().equals(bidSection.getBidOpenStatus())) {
            setSystemActionMessage("您将要评审的标段尚未开标结束，尚不能进行评标！", WebSocketMessage.Dialog.ALERT, WebSocketMessage.Type.ERROR);
            mv.setViewName("redirect:/login.html");
            return mv;
        }

        if (Status.END.getCode().equals(bidSection.getEvalStatus())) {
            setSystemActionMessage("您将要评审的标段已评标结束，尚不能再进入评标！", WebSocketMessage.Dialog.ALERT, WebSocketMessage.Type.ERROR);
            mv.setViewName("redirect:/login.html");
            return mv;
        }

        // 回避后
        if (ExpertStatus.AVOIDED.equals(expert.getAvoid())) {
            throw new BadCredentialsException("你已回避，登录失败!");
        }
        // 未确认，进入承诺书页面
        if (!ExpertStatus.CONFIRMED.equals(expert.getAvoid())) {
            mv.setViewName("redirect:/selectLeader/commitBookPage");
            return mv;
        }

        // 纸质标直接进入评标
        if (Enabled.YES.getCode().toString().equals(bidSection.getPaperEval())) {
            mv.setViewName("redirect:/paper/paperEvalPage");
            return mv;
        }

        //进入等待评标页面
        if (bidApply != null && !CommonUtil.isEmpty(bidApply.getChairMan())) {
            mv.setViewName("redirect:/expert/confirmBidEvalPage");
        } else {
            //进入选组长页面
            mv.setViewName("redirect:/selectLeader/selectLeaderPage");
        }
        // 组长已经开启评标，直接进入评标页面
        boolean status = (bidSection.getEvalStatus().equals(EvalStatus.PROGRESSING)
                || bidSection.getEvalStatus() > EvalStatus.PROGRESSING)
                && !CommonUtil.isEmpty(bidApply.getChairMan());
        if (status) {
            mv.setViewName("redirect:/expert/startEval");
            return mv;
        }
        return mv;
    }

    /**
     * 退出系统
     *
     * @param httpSession
     * @return
     */
    @RequestMapping("/logout")
    public boolean logout(HttpSession httpSession) {
        httpSession.invalidate();
        return true;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @RequestMapping("/getUser")
    public ExpertUser getUser() {
        AuthUser currentUser = CurrentUserHolder.getUser();
        return expertUserService.getExpertUserById(currentUser.getUserId());
    }


}
