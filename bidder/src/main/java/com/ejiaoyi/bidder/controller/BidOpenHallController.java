package com.ejiaoyi.bidder.controller;

import com.alibaba.fastjson.JSONArray;
import com.ejiaoyi.bidder.service.IBidderModelService;
import com.ejiaoyi.bidder.support.AuthUser;
import com.ejiaoyi.bidder.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.constant.LiveBroadCastConstant;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.CalcUtil;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 登录控制器
 *
 * @author Z0001
 * @since 2020/3/26
 */
@RestController
@RequestMapping("/visitor")
public class BidOpenHallController extends BaseController {

    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private ITenderProjectService projectService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private IBidderService bidderService;
    @Autowired
    private IBidderModelService bidderModelService;
    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;
    @Autowired
    private IUploadFileService uploadFileService;
    @Autowired
    private ILikeCountService likeCountService;
    @Autowired
    private LatiLongitudeService latiLongitudeService;
    @Autowired
    private Environment env;


    /**
     * 不见面开标大厅首页
     */
    @RequestMapping("/noFaceIndex.html")
    public ModelAndView noFaceIndex() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/noFaceIndex");
        mav.addObject("conListPage", env.getProperty("noface.conListUrl"));
        mav.addObject("govListPage", env.getProperty("noface.govListUrl"));
        mav.addObject("trafficListPage", env.getProperty("noface.trafficListUrl"));
        mav.addObject("waterListPage", env.getProperty("noface.waterListUrl"));
        return mav;
    }

    /**
     * 未登录页面（游客）
     */
    @RequestMapping("/noLoginPage.html")
    public ModelAndView noLoginPage() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/list");
        List<Object> hallBidSections = bidderModelService.listBidOpenHallBidSection();
        if (hallBidSections.size() > 2){
            mav.addObject("bidSectionOne", hallBidSections.get(2));
        }
        mav.addObject("startOpens", hallBidSections.get(0));
        mav.addObject("notOpens", hallBidSections.get(1));
        mav.addObject("liveUrlAddress", LiveBroadCastConstant.LIVE_URL);
        return mav;
    }

    /**
     * 游客登录页面
     */
    @RequestMapping("/listPage")
    @UserLog("'游客登录'")
    public ModelAndView loginPage() {
        AuthUser user = CurrentUserHolder.getUser();
        ModelAndView mav = new ModelAndView("/bidOpeningHall/list");
        mav.addObject("user", user);
        List<Object> hallBidSections = bidderModelService.listBidOpenHallBidSection();
        if (hallBidSections.size() > 2){
            mav.addObject("bidSectionOne", hallBidSections.get(2));
        }
        mav.addObject("startOpens", hallBidSections.get(0));
        mav.addObject("notOpens", hallBidSections.get(1));
        mav.addObject("liveUrlAddress", LiveBroadCastConstant.LIVE_URL);
        return mav;
    }


    @RequestMapping("/listRightPage")
    public ModelAndView listRightPage() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/listRight");
        List<Object> hallBidSections = bidderModelService.listBidOpenHallBidSection();
        if (hallBidSections.size() > 2){
            mav.addObject("bidSectionOne", hallBidSections.get(2));
        }
        mav.addObject("startOpens", hallBidSections.get(0));
        mav.addObject("notOpens", hallBidSections.get(1));
        mav.addObject("liveUrlAddress", LiveBroadCastConstant.LIVE_URL);
        return mav;
    }


    /**
     * 标段信息页面
     */
    @RequestMapping("/intoInforPage")
    public JsonData intoInforPage(Integer bidSectionId, HttpServletRequest request) {
        JsonData jsonData = new JsonData();
        jsonData.setCode("200");
        //判断是否开标，未开标不能进入；
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        if (CommonUtil.isEmpty(bidSection.getBidOpenStatus()) || bidSection.getBidOpenStatus() == 0) {
            //未开标
            jsonData.setMsg("0");
            return jsonData;
        }
        if (!CommonUtil.isEmpty(bidSection.getBidOpenStatus()) && bidSection.getBidOpenStatus() == 2) {
            //开标结束
            jsonData.setMsg("2");
            return jsonData;
        }
        //开标中
        jsonData.setMsg("1");
        //user绑定项目
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        user.setBidSectionId(bidSectionId);

        return jsonData;
    }

    /**
     * 身份检查
     */
    @RequestMapping("/identity")
    public ModelAndView identity() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/bidOpeningHall/identity");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        //所有参标者
        List<Bidder> bidders = bidderService.listAllBidders(bidSectionId, false);
        if (BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode())) {
            // 资格预审项目，【游客页面】不显示投标人名称
            for (Bidder bidder : bidders) {
                bidder.setBidderName("******");
            }
        }
        mav.addObject("bidders", bidders);
        mav.addObject("bidSection", bidSection);
        return mav;
    }

    /**
     * 文件解密
     */
    @RequestMapping("/decrypt")
    public ModelAndView decrypt() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/decrypt");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        // 参与解密的投标人
        List<Bidder> bidders = bidderService.listBiddersForDecrypt(bidSectionId);
        Integer allCount = bidders.size();
        // 当前排队人数
        Object queueNum = RedisUtil.get("bidSection:" + bidSectionId);
        Integer queueCount = queueNum == null ? 0 : (Integer) queueNum;
        // 解密中的人数
        Object decryptingNum = RedisUtil.get("decrypting:" + bidSectionId);
        Integer decryptingCount = decryptingNum == null ? 0 : (Integer) decryptingNum;
        // 等待人数
        Integer wait = queueCount + decryptingCount;

        // 解密失败的人数
        Integer failCount = bidderOpenInfoService.selectDecryptStatusCount(bidSectionId, 2);
        // 解密成功的人数
        Integer successCount = bidderOpenInfoService.selectDecryptStatusCount(bidSectionId, 1);
        //已解密的人数
        Integer decrypted = failCount + successCount;
        // 未解密的人数
        Integer unDecryptCount = allCount - wait - decrypted;
        unDecryptCount = unDecryptCount < 0 ? 0 : unDecryptCount;

        //文件总大小
        AtomicLong total = new AtomicLong(1L);
        bidders.forEach(bidder -> {
            Integer fileId = bidder.getBidDocId();
            Integer decryptStatus = bidder.getBidderOpenInfo().getDecryptStatus();
            if (decryptStatus == 1 && !CommonUtil.isEmpty(fileId)) {
                UploadFile uploadById = uploadFileService.getUploadById(fileId);
                total.addAndGet(Long.valueOf(uploadById.getByteSize()));
            }
        });

        //解密总进度
        int progress = 100;
        if (allCount != 0) {
            double divide = CalcUtil.divide(decrypted, allCount);
            progress = (int) (divide * 100);
        }
        // 资格预审项目，【游客页面】不显示投标人名称
        if (BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode())) {
            for (Bidder bidder : bidders) {
                bidder.setBidderName("******");
            }
        }
        mav.addObject("decNumb", decrypted);
        mav.addObject("unDecryptCount", unDecryptCount);
        mav.addObject("wait", wait);
        mav.addObject("progress", progress);
        mav.addObject("decrypted", FileUtil.getReadSize(total.get()));
        mav.addObject("bidders", bidders);
        return mav;
    }

    /**
     * 设置标段
     */
    @PostMapping("/setbidSection")
    public void setbidSection(Integer bidSectionId) {
        CurrentUserHolder.getUser().setBidSectionId(bidSectionId);
    }

    /**
     * 点赞
     */
    @PostMapping("/like")
    public Integer like(Integer bidSectionId) {
        //评标未结束
        BidSection bidSectionById = bidSectionService.getBidSectionById(bidSectionId);
        if (bidSectionById.getBidOpenStatus() != 2) {
            //从redis获取
            Integer number = (Integer) RedisUtil.get("like:like_" + bidSectionId);
            if (CommonUtil.isEmpty(number)) {
                //从数据库获取
                LikeCount likeCount = likeCountService.LikeByBidSectionId(bidSectionId);
                if (CommonUtil.isEmpty(likeCount)) {
                    number = 1;
                } else {
                    number = likeCount.getCount();
                }
            } else {
                number++;
            }
            RedisUtil.set("like:like_" + bidSectionId, number);
            return number;
        } else {
            return 0;
        }
    }

    /**
     * 查看点赞情况
     */
    @PostMapping("/checkLike")
    public Integer checkLike(Integer bidSectionId) {
        //评标未结束
        BidSection bidSectionById = bidSectionService.getBidSectionById(bidSectionId);
        if (bidSectionById.getBidOpenStatus() != 2) {
            Integer number = (Integer) RedisUtil.get("like:like_" + bidSectionId);
            if (CommonUtil.isEmpty(number)) {
                LikeCount likeCount = likeCountService.LikeByBidSectionId(bidSectionId);
                if (CommonUtil.isEmpty(likeCount)) {
                    number = 0;
                } else {
                    number = likeCount.getCount();
                }
            }
            RedisUtil.set("like:like_" + bidSectionId, number);
            return number;
        }
        return 0;
    }

    /**
     * 标段信息页面
     */
    @RequestMapping("/information")
    public ModelAndView information() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/information");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();

        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        // 代理查询
        TenderProject tenderProject = projectService.getTenderProjectById(bidSection.getTenderProjectId());
        if (!CommonUtil.isEmpty(tenderProject)) {
            bidSection.setTenderAgencyName(tenderProject.getTenderAgencyName());
            bidSection.setTenderAgencyPhone(tenderProject.getTenderAgencyPhone());
        }
        // 开标时间查询
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        bidSection.setBidOpenTime(tenderDoc.getBidOpenTime());
        mav.addObject("user", user);
        mav.addObject("bidSection", bidSection);
        mav.addObject("liveUrlAddress", LiveBroadCastConstant.LIVE_URL);
        // 获取坐标点
        Map<String, String> bidderPoint = latiLongitudeService.getBidderPoint(bidSectionId);
        mav.addObject("geoCoordMap", bidderPoint.get("point"));
        mav.addObject("geoCoordData", JSONArray.parse(bidderPoint.get("data")));
        mav.addObject("total", bidderPoint.get("total"));
        mav.addObject("min", bidderPoint.get("min"));
        mav.addObject("max", bidderPoint.get("max"));

        return mav;
    }

    /**
     * 投标人分布
     */
    @RequestMapping("/map")
    public ModelAndView map() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/bidOpeningHall/map");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        mav.addObject("bidSection", bidSection);
        return mav;
    }

    /**
     * 投标人状态
     */
    @RequestMapping("/state")
    public ModelAndView state() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/bidOpeningHall/state");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        //所有参标者
        List<Bidder> bidders = bidderService.listAllBidders(bidSectionId, false);
        if (BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode())) {
            // 资格预审项目，【游客页面】不显示投标人名称
            for (Bidder bidder : bidders) {
                bidder.setBidderName("******");
            }
        }
        mav.addObject("bidders", bidderService.calDecryptTime(bidders, bidSectionId));
        return mav;
    }

    /**
     * 检查开标是否结束
     *
     * @return
     */
    @PostMapping("/checkOpenEnd")
    public Boolean checkOpenEnd() {
        AuthUser user = CurrentUserHolder.getUser();
        if (!CommonUtil.isEmpty(user.getBidSectionId())) {
            BidSection bidSectionById = bidSectionService.getBidSectionById(user.getBidSectionId());
            //评标结束点赞数记录到数据库
            if (bidSectionById.getBidOpenStatus() == 2) {
                Integer number = (Integer) RedisUtil.get("like:like_" + bidSectionById.getId());
                if (CommonUtil.isEmpty(number)) {
                    return true;
                }
                //记录到数据库
                LikeCount likeCountOld = likeCountService.LikeByBidSectionId(bidSectionById.getId());
                LikeCount likeCount = null;
                if (CommonUtil.isEmpty(likeCountOld)) {
                    likeCount = LikeCount.builder().count(number).bidSectionId(bidSectionById.getId()).build();
                } else {
                    likeCount = likeCountOld.toBuilder().count(number).build();
                }
                likeCountService.insert(likeCount);
                //清除redis
                RedisUtil.delete("like:like_" + bidSectionById.getId());
                return true;
            }
            return false;
        }
        return true;
    }


}
