package com.ejiaoyi.worker.controller;

import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.ExecuteCode;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.worker.service.IExpertInputService;
import com.ejiaoyi.worker.support.AuthUser;
import com.ejiaoyi.worker.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.util.JAXBSource;
import java.util.List;

/**
 * @Description: 专家录入前端控制器
 * @Auther: liuguoqiang
 * @Date: 2020-8-21 17:30
 */
@RestController
@RequestMapping("/expertInput")
public class ExpertInputController {
    @Autowired
    private IExpertInputService expertInputService;

    /**
     * 获取标段分页信息
     *
     * @param bidSection
     * @return
     */
    @RequestMapping(value = "/listBidSection", produces = {"text/html;charset=utf-8"})
    public String listBidSection(BidSection bidSection) {
        AuthUser user = CurrentUserHolder.getUser();
        bidSection.setRegId(user.getRegId());
        return expertInputService.listBidSection(bidSection);
    }

    /**
     * 获取 项目标段数目
     *
     * @return
     */
    @RequestMapping("/getProjectTotal")
    public Integer getProjectTotal(BidSection bidSection) {
        AuthUser user = CurrentUserHolder.getUser();
        bidSection.setRegId(user.getRegId());
        return expertInputService.getProjectTotal(bidSection);
    }

    /**
     * 转发专家录入base页面
     *
     * @return
     */
    @RequestMapping("/expertInput")
    public ModelAndView expertInput() {
        Integer bidSectionId = CurrentUserHolder.getUser().getBidSectionId();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("forward:/expertInput/expertBasePage");
        mav.addObject("bidSectionId", bidSectionId);
        return mav;
    }

    @RequestMapping("/expertBasePage")
    public ModelAndView expertBasePage(Integer bidSectionId){
        ModelAndView mav = new ModelAndView("/expertInput/expertBase");
        mav.addObject("bidSectionId",bidSectionId);
        return mav;
    }

    @RequestMapping("/expertInputPage")
    public ModelAndView expertInputPage() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView mav = new ModelAndView("/expertInput/expertInput");
        //第一次进入初始化评标申请记录
        BidApply bidApply = expertInputService.bidApplyInfo(bidSectionId);
        //查询当前标段信息，专家人数
        BidSection bidSection = expertInputService.getBidSection(bidSectionId);
        //已经录入的专家列表
        List<ExpertUser> expertList = expertInputService.listExpertByBidSectionId(bidSectionId);
        //专家分类列表
        List<Wordbook> expertCategoryList = expertInputService.getExpertCategoryList();
        mav.addObject("user", user);
        mav.addObject("bidApply", bidApply);
        mav.addObject("bidSection", bidSection);
        mav.addObject("expertList", expertList);
        mav.addObject("eListSize", expertList.size());
        mav.addObject("expertCategoryList", expertCategoryList);
        return mav;
    }

    @RequestMapping("/addExpertPage")
    public ModelAndView addExpertPage(Integer bidSectionId) {
        ModelAndView mav = new ModelAndView("/expertInput/addExpert");
        //专家分类列表
        List<Wordbook> expertCategoryList = expertInputService.getExpertCategoryList();
        BidApply bidApply = expertInputService.bidApplyInfo(bidSectionId);
        mav.addObject("bidSectionId", bidSectionId);
        mav.addObject("bidApply", bidApply);
        mav.addObject("expertCategoryList", expertCategoryList);
        return mav;
    }

    /**
     * 添加专家
     *
     * @param expertUser
     * @return
     */
    @RequestMapping("/addExpert")
    public JsonData addExpert(ExpertUser expertUser) {
        JsonData data = new JsonData();
        data.setCode(ExecuteCode.SUCCESS.getCode().toString());
        String errorInfo = expertInputService.addExpert(expertUser);

        if (!CommonUtil.isEmpty(errorInfo)){
            data.setCode(ExecuteCode.FAIL.getCode().toString());
            data.setMsg(errorInfo);
        }
        return data;
    }

    /**
     * 禁用专家
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteExpert")
    public Boolean deleteExpert(Integer id) {
        return expertInputService.deleteExpert(id);
    }

    /**
     * 专家复用
     *
     * @param bidSection
     * @return
     */
    @RequestMapping("/expertMultiplexingPage")
    public ModelAndView expertMultiplexingPage(BidSection bidSection) {
        ModelAndView mav = new ModelAndView("/expertInput/expertMultiplexing");
        //获取项目信息
        TenderProject tenderProject = expertInputService.getTenderProject(bidSection);
        //筛选可用标段列表并封装专家列表(排除当前标段)
        AuthUser user = CurrentUserHolder.getUser();
        bidSection.setRegId(user.getRegId());
        List<BidSection> list = expertInputService.getBidSectionListForExpert(bidSection);


        mav.addObject("tenderProject", tenderProject);
        mav.addObject("list", list);
        mav.addObject("bsId", bidSection.getId());


        return mav;
    }

    @RequestMapping("/addExpertList")
    public Boolean addExpertList(Integer bidSectionId, Integer[] ids, Integer representativeCount) {
        return expertInputService.addExpertList(bidSectionId, ids, representativeCount);
    }

    @RequestMapping("/searchExpert")
    public List<ExpertUser> searchExpert(String expertName){
        return expertInputService.searchExpert(expertName);
    }

    @RequestMapping("/isIdCardRepeat")
    public Boolean isIdCardRepeat(String idCard,Integer bidSectionId){
        return expertInputService.isIdCardRepeat(idCard,bidSectionId);
    }

    @RequestMapping("/isPhoneRepeat")
    public Boolean isPhoneRepeat(String phone,Integer bidSectionId){
        return expertInputService.isPhoneRepeat(phone,bidSectionId);
    }

    /**
     *  打印
     * @param bidSectionId
     * @return
     */
    @RequestMapping("/printExpert")
    public ModelAndView listExpertUser(Integer bidSectionId){
        ModelAndView mav = new ModelAndView("/expertInput/printExpert");
        //查询当前标段信息，专家人数
        BidSection bidSection = expertInputService.getBidSection(bidSectionId);
        mav.addObject("bidSection",bidSection);
        mav.addObject("list",expertInputService.listExpertByBidSectionId(bidSectionId));
        return mav;
    }


    /**
     * 设置用户操作的bidsetionId
     *
     * @param bidSectionId
     * @return
     */
    @PostMapping("/setBidSectionId")
    public void setBidSectionId(Integer bidSectionId) {
        CurrentUserHolder.getUser().setBidSectionId(bidSectionId);
    }
}
