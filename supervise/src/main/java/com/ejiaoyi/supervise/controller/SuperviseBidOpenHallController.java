package com.ejiaoyi.supervise.controller;

import com.alibaba.fastjson.JSONArray;
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
import com.ejiaoyi.supervise.support.AuthUser;
import com.ejiaoyi.supervise.support.CurrentUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 监管不见面前端控制器
 *
 * @author Z0001
 * @since 2020/3/26
 */
@RestController
@RequestMapping("/bidOpenHall")
public class SuperviseBidOpenHallController extends BaseController {

    @Autowired
    private IBidSectionService bidSectionService;
    @Autowired
    private ITenderProjectService projectService;
    @Autowired
    private ITenderDocService tenderDocService;
    @Autowired
    private IBidderService bidderService;
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
     * 存入坐标
     * @param latiLongitude 监管的经纬度
     * @return
     */
    @RequestMapping("/saveLatiLongitude")
    public Boolean saveLatiLongitude(LatiLongitude latiLongitude){
        com.ejiaoyi.supervise.support.AuthUser user = com.ejiaoyi.supervise.support.CurrentUserHolder.getUser();
        latiLongitude.setBidderName(user.getName());
        // 游客身份登录
        latiLongitude.setType(1);
        return latiLongitudeService.saveLatiLongitude(latiLongitude);
    }

    /**
     * 不见面游客开标大厅
     *
     * @return
     */
    @RequestMapping("/toVisitorBidHall")
    public ModelAndView toVisitorBidHall() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/information");
        com.ejiaoyi.supervise.support.AuthUser user = com.ejiaoyi.supervise.support.CurrentUserHolder.getUser();
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
     * 标段信息页面
     */
    @RequestMapping("/intoInfoPage")
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
        com.ejiaoyi.supervise.support.AuthUser user = com.ejiaoyi.supervise.support.CurrentUserHolder.getUser();
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
        // 参与解密的投标人
        List<Bidder> bidders = bidderService.listBiddersForDecrypt(bidSectionId);
        int allCount = bidders.size();
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
        int unDecryptCount = allCount - wait - decrypted;
        unDecryptCount = Math.max(unDecryptCount, 0);

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

        mav.addObject("decNumb", decrypted);
        mav.addObject("unDecryptCount", unDecryptCount);
        mav.addObject("wait", wait);
        mav.addObject("progress", progress);
        mav.addObject("decrypted", FileUtil.getReadSize(total.get()));
        mav.addObject("bidders", bidders);
        return mav;
    }


    /**
     * 点赞
     */
    @PostMapping("/like")
    public Integer like(Integer bidSectionId) {
        //评标未结束
        BidSection bidSectionById = bidSectionService.getBidSectionById(bidSectionId);
        if (bidSectionById.getBidOpenStatus() != 2) {
            // 从redis获取
            Integer number = (Integer) RedisUtil.get("like:like_" + bidSectionId);
            if (CommonUtil.isEmpty(number)) {
                // 从数据库获取
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
     * 投标人分布
     */
    @RequestMapping("/map")
    public ModelAndView map() {
        ModelAndView mav = new ModelAndView("/bidOpeningHall/map");
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
        ModelAndView mav = new ModelAndView("/bidOpeningHall/state");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        //所有参标者
        List<Bidder> bidders = bidderService.listAllBidders(bidSectionId, false);
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
            BidSection bidSection = bidSectionService.getBidSectionById(user.getBidSectionId());
            //评标结束点赞数记录到数据库
            if (bidSection.getBidOpenStatus() == 2) {
                Integer number = (Integer) RedisUtil.get("like:like_" + bidSection.getId());
                if (CommonUtil.isEmpty(number)) {
                    return true;
                }
                //记录到数据库
                LikeCount likeCountOld = likeCountService.LikeByBidSectionId(bidSection.getId());
                LikeCount likeCount = null;
                if (CommonUtil.isEmpty(likeCountOld)) {
                    likeCount = LikeCount.builder().count(number).bidSectionId(bidSection.getId()).build();
                } else {
                    likeCount = likeCountOld.toBuilder().count(number).build();
                }
                likeCountService.insert(likeCount);
                //清除redis
                RedisUtil.delete("like:like_" + bidSection.getId());
                return true;
            }
            return false;
        }
        return true;
    }


}
