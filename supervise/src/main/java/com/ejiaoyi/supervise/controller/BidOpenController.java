package com.ejiaoyi.supervise.controller;

import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.ClientCheckOneDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.BidProtype;
import com.ejiaoyi.common.enums.FileType;
import com.ejiaoyi.common.enums.StatusEnum;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.supervise.support.AuthUser;
import com.ejiaoyi.supervise.support.CurrentUserHolder;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 开标进度控制器
 *
 * @author yyb
 * @date 2020-9-1 14:14
 */
@RestController
@RequestMapping("gov/bidOpen")
public class BidOpenController extends BaseController {

    @Autowired
    IBidSectionService bidSectionService;

    @Autowired
    ITenderDocService tenderDocService;

    @Autowired
    IBidderService bidderService;

    @Autowired
    IFDFSService fdfsService;

    @Autowired
    ITenderProjectService tenderProjectService;

    @Autowired
    IGovUserService govUserService;

    @Autowired
    IDepService depService;

    @Autowired
    IProjectPauseService projectPauseService;

    @Autowired
    ILineStatusService lineStatusService;

    @Autowired
    IFreeBackApplyService freeBackApplyService;

    @Autowired
    IBidderOpenInfoService bidderOpenInfoService;

    @Autowired
    IBidderExceptionService bidderExceptionService;

    @Autowired
    private IBidSectionRelateService bidSectionRelateService;

    @Autowired
    private IClearBidV3Service clearBidV3Service;

    /**
     * 进入开评标查看进度页面
     *
     * @return
     */
    @RequestMapping("/baseGovPage")
    public ModelAndView bidOpenOnline(Integer bidSectionId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/gov/bidOpen/bidProgress");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        if (bidSectionId != null) {
            user.setBidSectionId(bidSectionId);
        }
        return mav;
    }

    @RequestMapping("/bidProgress")
    public ModelAndView baseBidder() {
        ModelAndView mav = new ModelAndView("/progressOfBid/baseGov");
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        GovUser govUser = govUserService.getGovUserById(user.getUserId());
        Dep dep = depService.getDepById(govUser.getDepId());
        ProjectPause projectPause = projectPauseService.getProjectPauseByBidSectionId(bidSectionId);
        FreeBackApply waitFreeBackApply = freeBackApplyService.getFreeBackApplyByBidSectionId(bidSectionId);
        //获取项目暂停状态
        Integer pauseStatus = 1;
        if (projectPause != null) {
            pauseStatus = projectPause.getPauseStatus();
        }
        BidSectionRelate relate = bidSectionRelateService.getBidSectionRelateByBSId(bidSectionId);
        // 价格分计算是否完成
        Integer priceScoreFlag = clearBidV3Service.priceScoreFlag(bidSection.getYwCode());
        mav.addObject("bidSection", bidSection);
        mav.addObject("relate", relate);
        mav.addObject("dep", dep);
        mav.addObject("pauseStatus", pauseStatus);
        mav.addObject("waitFreeBackApply", waitFreeBackApply);
        mav.addObject("priceScoreFlag", priceScoreFlag.equals(StatusEnum.SUCCESS.getStatus()));
        return mav;
    }

    /**
     * 签到信息页面
     *
     * @return
     */
    @RequestMapping("/signInfoPage")
    public ModelAndView signInfoPage() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView mav = new ModelAndView("/progressOfBid/bidOpening/signInfo");
        List<Bidder> bidders = bidderService.listAllBidders(bidSectionId, false);
        mav.addObject("biddersCount", bidders.size());
        return mav;
    }

    /**
     * 分页查询所有投标人列表
     *
     * @return
     */
    @RequestMapping("/pageAllBidders")
    public List<Bidder> pageAllBidders() {
        AuthUser user = CurrentUserHolder.getUser();
        assert user != null;
        Integer bidSectionId = user.getBidSectionId();
        return bidderService.listAllBidders(bidSectionId, true);
    }

    /**
     * 回执单页面
     *
     * @param id fdfsId
     * @return
     */
    @RequestMapping("/reciptPage")
    public ModelAndView reciptPage(Integer id) {
        ModelAndView modelAndView = new ModelAndView("/viewPdf/recipt");
        modelAndView.addObject("mark", fdfsService.getFdfdById(id).getMark());
        return modelAndView;
    }

    /**
     * 通过fdfs的id跳转到pdf加载iframe
     *
     * @param fileId fdfsId
     * @return
     */
    @RequestMapping("/showPdfIframe")
    public ModelAndView showPdfIframe(Integer fileId) {
        ModelAndView modelAndView = new ModelAndView("/viewPdf/iframe");
        modelAndView.addObject("fileId", fileId);
        return modelAndView;
    }

    /**
     * 通过fdfs的id跳转到pdf加载iframe
     *
     * @param fileId fdfsId
     * @return
     */
    @RequestMapping("/showCheckPdfIframe")
    public ModelAndView showCheckPdfIframe(Integer fileId) {
        ModelAndView modelAndView = new ModelAndView("/viewPdf/checkIframe");
        modelAndView.addObject("fileId", fileId);
        return modelAndView;
    }

    /**
     * 通过fdfs的id获取文件地址
     *
     * @param fileId 文件id
     * @return
     */
    @RequestMapping("/showCheckPdfById")
    public ModelAndView showCheckPdfById(Integer fileId) {
        ModelAndView mav = new ModelAndView("/viewPdf/showCheckPdfById");
        AuthUser user = CurrentUserHolder.getUser();
        mav.addObject("fdfs", fdfsService.getFdfdById(fileId));
        mav.addObject("user", user);
        return mav;
    }

    /**
     * 通过fdfs的id获取文件地址
     *
     * @param fileId 文件id
     * @return
     */
    @RequestMapping("/showPdfById")
    public ModelAndView showPdfById(Integer fileId) {
        ModelAndView mav = new ModelAndView("/viewPdf/showPdfById");
        AuthUser user = CurrentUserHolder.getUser();
        mav.addObject("fdfs", fdfsService.getFdfdById(fileId));
        mav.addObject("user", user);
        return mav;
    }

    /**
     * 进入身份检查页面
     *
     * @return
     */
    @RequestMapping("/identityCheckPage")
    public ModelAndView identityCheckPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidOpening/identityCheck");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        List<Bidder> bidders = bidderService.listAllBidders(bidSectionId, false);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        mav.addObject("user", user);
        mav.addObject("bidSection", bidSection);
        mav.addObject("biddersCount", bidders.size());
        return mav;
    }

    /**
     * 授权委托书页面
     *
     * @param clientCheckOne
     * @return
     */
    @RequestMapping("/clientCheckShowPage")
    public ModelAndView clientCheckShowPage(ClientCheckOneDTO clientCheckOne) {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView mav = new ModelAndView("/progressOfBid/bidOpening/clientCheckShow");
        Integer bidderIdTo = clientCheckOne.getBidderIdTo();
        // 判断是修改了选择还是第一次跳转页面,clientCheckOne.getBidderIdNow()!=null为从当前页面跳转下一个页面
        // 修改当前bio为已检查
        bidderOpenInfoService.updateClientCheck(bidderIdTo, clientCheckOne.getCheckType(), clientCheckOne.getPassType());
        // 查询需要跳转的招标人数据
        clientCheckOne.setBidderOpenInfo(bidderOpenInfoService.getBidderOpenInfoById(bidderIdTo));
        // 获取需要跳转的页面前后招标人
        clientCheckOne.setLastBoi(bidderOpenInfoService.getAroundBoi(bidSectionId, bidderIdTo, -1));
        clientCheckOne.setNextBoi(bidderOpenInfoService.getAroundBoi(bidSectionId, bidderIdTo, 1));

        // 更新检查人数
        // 获取投标人列表
        List<Bidder> list = bidderService.listBiddersForCheck(bidSectionId);
        // 判断当前列表的检查人数与未检查人数
        Map<String, Integer> checkNum = bidderOpenInfoService.getCheckNumByList(list);
        clientCheckOne.setNotCheck(checkNum.get("notCheck"));
        clientCheckOne.setIsCheck(checkNum.get("isCheck"));

        // 判断是否是A10
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        Boolean isA10Bid = BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode());
        clientCheckOne.setIsA10Bid(isA10Bid);

        //判断是否有异常理由
        clientCheckOne.setIdentityBidderException(bidderExceptionService.getExceptionReason(clientCheckOne.getBidderOpenInfo(), 1));
        clientCheckOne.setMarginPayBidderException(bidderExceptionService.getExceptionReason(clientCheckOne.getBidderOpenInfo(), 2));

        mav.addObject("clientCheckOne", clientCheckOne);
        return mav;
    }

    /**
     * 跳转招标控制价页面
     *
     * @return
     */
    @RequestMapping("/bidControlPricePage")
    public ModelAndView bidControlPrice() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView mav = new ModelAndView("/progressOfBid/bidOpening/bidControlPrice");
        mav.addObject("tenderDoc", tenderDocService.getTenderDocBySectionId(bidSectionId));
        mav.addObject("bidders", bidderService.listDecrySuccessBidder(bidSectionId,false));
        mav.addObject("bidSection", bidSectionService.getBidSectionById(bidSectionId));
        return mav;
    }

    /**
     * 跳转抽取浮动点页面
     *
     * @return
     */
    @RequestMapping("/floatPointPage")
    public ModelAndView floatPoint() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView mav = new ModelAndView("/progressOfBid/bidOpening/floatPoint");
        mav.addObject("tenderDoc", tenderDocService.getTenderDocBySectionId(bidSectionId));
        return mav;
    }

    /**
     * 跳转投标文件上传及解密页面
     *
     * @return
     */
    @RequestMapping("/bidderFileDecryptPage")
    public ModelAndView bidderFileDecrypt() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView mav = new ModelAndView("/progressOfBid/bidOpening/bidderFileDecrypt");
        List<Bidder> bidders = bidderService.listBiddersForDecrypt(bidSectionId);
        mav.addObject("bidders", bidders);
        mav.addObject("biddersCount", bidders.size());
        return mav;
    }

    /**
     * 分页查询标书解密投标人列表
     *
     * @return
     */
    @RequestMapping("/pageTenderDecryptBidders")
    public List<Bidder> pageTenderDecryptBidders() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        boolean isQualification = BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode());
        return bidderService.pageTenderDecryptBidders(bidSectionId,isQualification);
    }

    /**
     * 跳转开标结束页面
     *
     * @return
     */
    @RequestMapping("/bidOpenEndPage")
    public ModelAndView bidOpenEnd() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView mav = new ModelAndView("/progressOfBid/bidOpening/bidOpenEnd");
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        mav.addObject("bidSection", bidSection);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        mav.addObject("tenderProject", tenderProject);
        return mav;
    }

    /**
     * 进入开标记录表页面
     *
     * @return
     */
    @RequestMapping("/bidOpenRecordPage")
    public ModelAndView bidOpenRecordPage() {
        ModelAndView mav = new ModelAndView("/progressOfBid/bidOpening/bidOpenRecord");
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        try {
            String mark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + bidSectionId +
                    File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD  + "." + FileType.PDF.getSuffix();
            //判断是否已生成开标记录表
            String fdfsUrl = fdfsService.getUrlByMark(mark);
            //加载开标记录表pdf
            if (StringUtil.isNotEmpty(fdfsUrl)) {
                Fdfs fdfs = fdfsService.getFdfsByMark(mark);
                mav.setViewName("/viewPdf/iframe");
                mav.addObject("fileId", fdfs.getId());
                mav.addObject("user", user);
                return mav;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 网上开标状态
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        List<Bidder> bidders = bidderService.listDecrySuccessBidder(bidSectionId, false);
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("tenderProject", tenderProject);
        mav.addObject("bidders", bidders);
        mav.addObject("lineStatus", lineStatus);
        return mav;
    }

    /**
     * 跳转投标文件上传及解密页面
     *
     * @return
     */
    @RequestMapping("/siteBidderFileDecryptPage")
    public ModelAndView siteBidderFileDecrypt() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        ModelAndView mav = new ModelAndView("/progressOfBid/bidOpening/siteBidderFileDecrypt");
        List<Bidder> bidders = bidderService.listAllBidders(bidSectionId, false);
        mav.addObject("bidders", bidders);
        mav.addObject("biddersCount", bidders.size());
        return mav;
    }

    /**
     * 分页查询现场标标书解密投标人列表
     *
     * @return
     */
    @RequestMapping("/pageSiteTenderDecryptBidders")
    public List<Bidder> pageSiteTenderDecryptBidders() {
        AuthUser user = CurrentUserHolder.getUser();
        Integer bidSectionId = user.getBidSectionId();
        return bidderService.listAllBidders(bidSectionId, true);
    }

}
