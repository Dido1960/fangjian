package com.ejiaoyi.expert.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.dto.BidderPriceScoreDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.EvalProcess;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.service.IBidderQuantityScoreService;
import com.ejiaoyi.common.service.IClearBidV3Service;
import com.ejiaoyi.common.service.IEvalResultSgService;
import com.ejiaoyi.common.service.ITenderDocService;
import com.ejiaoyi.common.service.impl.*;
import com.ejiaoyi.common.util.ApplicationContextUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.DesUtil;
import com.ejiaoyi.expert.support.AuthUser;
import com.ejiaoyi.expert.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 清标v3.O 控制器
 *
 * @author fengjunhong
 * @since 2020/12/16
 */
@RestController
@RequestMapping("/clearBidV3")
public class ClearBidV3Controller extends BaseController {

    @Autowired
    private BidSectionServiceImpl bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IClearBidV3Service clearBidV3Service;

    @Autowired
    private IBidderQuantityScoreService bidderQuantityScoreService;

    @Autowired
    SgServiceImpl sgService;

    /**
     * 跳转到清标前端页面
     *
     * @param backPage
     * @return
     */
    @RequestMapping("/toSysFront")
    public ModelAndView toSysFront(Integer bId,String backPage) {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = bId;
        Map<String, Object> map = new LinkedHashMap<>();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        //  1、传递需要清标显示的模块
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String showClearModule = clearBidV3Service.getShowClearModule(tenderDoc);
        clearBidV3Service.setClearModule(bidSection.getYwCode(),showClearModule);
        // 2、 进入清标V3.0系统
        map.put("ywCode", bidSection.getYwCode());
        map.put("backPage", backPage);
        map.put("timeStamp", DateTimeUtil.getInternetTime(TimeFormatter.YYYYHHDDHHMMSS));
        map.put("index", UUID.randomUUID().toString());
        map.put("showPriceBtn", false);
        Environment env = ApplicationContextUtil.getApplicationContext().getBean(Environment.class);
        map.put("sysName", Objects.requireNonNull(env.getProperty("clearSysName")));
        String path = Objects.requireNonNull(env.getProperty("clearPage"));
        return new ModelAndView("redirect:" + path + "?param=" + DesUtil.encrypt(JSONObject.toJSONString(map)) + "&uid=" + map.get("index"));
    }

    /**
     * 报价得分页面
     *
     * @param backPage
     * @return
     */
    @RequestMapping("/toPriceScorePage")
    public ModelAndView toPriceScorePage(Integer bId,String backPage) {
//        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = bId;
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        boolean firstStepEnd = sgService.isGroupCompletion(gradeIds, EvalProcess.PRELIMINARY.getCode());
        // 初步评审结束、未计算报价分时，才需要执行此操作
        if (firstStepEnd && Status.NOT_START.getCode().equals(bidSection.getPriceRecordStatus())){
            // 1、传递废标的投标人（废标不计算报价得分）
            clearBidV3Service.cancelBidders(bidSection);
        }
        // 2、进入报价得分页面
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("ywCode", bidSection.getYwCode());
        map.put("backPage", backPage);
        map.put("timeStamp", DateTimeUtil.getInternetTime(TimeFormatter.YYYYHHDDHHMMSS));
        map.put("index", UUID.randomUUID().toString());
        map.put("showPriceBtn", false);
        Environment env = ApplicationContextUtil.getApplicationContext().getBean(Environment.class);
        map.put("sysName", Objects.requireNonNull(env.getProperty("clearSysName")));
        String path = Objects.requireNonNull(env.getProperty("priceScorePage"));
        return new ModelAndView("redirect:" + path + "?param=" + DesUtil.encrypt(JSONObject.toJSONString(map)) + "&uid=" + map.get("index"));
    }

    /**
     * 通知更新投标人报价得分
     * @return
     */
    @RequestMapping(value = "/pushPriceScoreNotify")
    public String getPushPriceScoreNotify(@RequestBody String encryptContent) {
        try {
            // step1：解密
            String decrypt = SM2Util.decrypt(encryptContent);
            // step: 解构
            List<BidderQuantityScore> bidderQuantityScores = JSON.parseArray(decrypt, BidderQuantityScore.class);;
            // 获取标段id
            Integer bidSectionId = bidderQuantityScores.get(0).getBidSectionId();
            // 更新报价得分
            bidderQuantityScoreService.saveBidderQuantityScore(bidSectionId, bidderQuantityScores);
            // 接收成功
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
}
