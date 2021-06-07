package com.ejiaoyi.supervise.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.constant.ExpertStatus;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.BidderQuantityScore;
import com.ejiaoyi.common.entity.TenderDoc;
import com.ejiaoyi.common.enums.Status;
import com.ejiaoyi.common.enums.StatusEnum;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.IBidderQuantityScoreService;
import com.ejiaoyi.common.service.IClearBidV3Service;
import com.ejiaoyi.common.service.ITenderDocService;
import com.ejiaoyi.common.service.impl.BidSectionServiceImpl;
import com.ejiaoyi.common.util.ApplicationContextUtil;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.DateTimeUtil;
import com.ejiaoyi.common.util.DesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 跳转到清标前端页面
     *
     * @param backPage
     * @return
     */
    @RequestMapping("/toSysFront")
    public ModelAndView toSysFront(Integer bId,String backPage) {
        Integer bidSectionId = bId;
        Map map = new LinkedHashMap();
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
     * 清标系统 -> 报价得分计算状态
     * return： 参考 StatusEnum枚举
     */
    @RequestMapping("/getPriceScoreStatus")
    public Integer getPriceScoreStatus(Integer bId) {
        BidSection bidSection = bidSectionService.getBidSectionById(bId);
        if (!CommonUtil.isEmpty(bidSection.getYwCode())){
            return clearBidV3Service.priceScoreFlag(bidSection.getYwCode());
        }
        return StatusEnum.WAIT.getStatus();
    }


    /**
     * 报价得分页面
     *
     * @param backPage
     * @return
     */
    @RequestMapping("/toPriceScorePage")
    public ModelAndView toPriceScorePage(Integer bId,String backPage) {
        Integer bidSectionId = bId;
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        // 进入报价得分页面
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

}
