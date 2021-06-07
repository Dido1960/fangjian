package com.ejiaoyi.agency.controller;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.agency.dto.SiteBidDecryptDto;
import com.ejiaoyi.agency.service.impl.SiteOpenBidServiceImpl;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.dto.DecoderCipherInfoDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.service.impl.BidderServiceImpl;
import com.ejiaoyi.common.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 现场标前端控制器
 *
 * @author Mike
 * @date 2021/01/07 14:24
 */
@RestController
@RequestMapping("/siteOpenBid")
public class SiteOpenBidController extends BaseController {
    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private SiteOpenBidServiceImpl siteOpenBidService;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Autowired
    private ILineStatusService lineStatusService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidderFileInfoService bidderFileInfoService;

    @Autowired
    private IBidderFileUploadService bidderFileUploadService;

    /**
     * 跳转到投标人解密页面
     *
     * @param id 标段id
     * @return
     */
    @RequestMapping("/bidderFileDecryptPage")
    public ModelAndView bidderFileDecryptPage(Integer id) {
        ModelAndView mav = new ModelAndView("/siteOpenBid/bidderFileDecryptPage");
        // 等待开标的项目
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        // 招标文件内容
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());
        String lastDecryptStartTime = null;
        // 网上开标状态
        lineStatusService.updateFileUploadOrsigninStatus(id);
        LineStatus lineStatus = lineStatusService.getLineStatusBySectionId(id);
        String decryptionPeriods = lineStatus.getDecryptionPeriods();
        if (!CommonUtil.isEmpty(decryptionPeriods)) {
            JSONArray jArray = JSONArray.parseArray(decryptionPeriods);
            JSONObject jObject = jArray.getJSONObject(jArray.size() - 1);
            lastDecryptStartTime = (String) jObject.get("startTime");
        }
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        mav.addObject("lineStatus", lineStatus);
        mav.addObject("lastDecryptStartTime", lastDecryptStartTime);
        return mav;
    }

    @RequestMapping("/bidderFileDecrypt")
    @UserLog(value = "'解析投标文件信息: 标段id：'+#bidSectionId+'投标文件上传id：'+#bidderFileId", dmlType = DMLType.INSERT)
    public JsonData bidderFileDecrypt(Integer bidSectionId, Integer bidderFileId) {
        JsonData result = new JsonData();
        try {
            siteOpenBidService.parseBidderProject(bidSectionId, bidderFileId);
            result.setCode("1");
            result.setMsg("投标文件解密成功");
            return result;
        } catch (CustomException ce) {
            result.setCode("2");
            result.setMsg(ce.getMessage());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode("2");
            result.setMsg("投标文件未解析成功, 投标人新增失败!");
            return result;
        }
    }

    /**
     * 通过id获取当前标段的开标流程的完成情况
     *
     * @param id
     * @return
     */
    @RequestMapping("/listBidOpenProcessComplete")
    public List<String> listBidOpenProcessComplete(Integer id) {
        return siteOpenBidService.listBidOpenProcessComplete(id);
    }

    /**
     * 跳转到添加控制价
     *
     * @return
     */
    @RequestMapping("/addControlPricePage")
    public ModelAndView addControlPricePage(Integer id) {
        ModelAndView mav = new ModelAndView("/siteOpenBid/addControlPrice");
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 跳转施工总承包最高投标限价页面
     *
     * @return
     */
    @RequestMapping("/maxBidPricePage")
    public ModelAndView maxBidPricePage(Integer id) {
        ModelAndView mav = new ModelAndView("/siteOpenBid/maxBidPrice");
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 跳转到浮动点抽取页面
     *
     * @return
     */
    @RequestMapping("/floatPointPage")
    public ModelAndView floatPointPage(Integer id) {
        ModelAndView mav = new ModelAndView("/siteOpenBid/floatPoint");

        // 等待开标的项目
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSection.getId());

        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 获取投标人开标信息
     *
     * @param bidderOpenInfo 开标信息
     * @return
     */
    @RequestMapping("/getBidderOpenInfo")
    public BidderOpenInfo getBidderOpenInfo(BidderOpenInfo bidderOpenInfo) {
        return bidderOpenInfoService.getBidderOpenInfo(bidderOpenInfo);
    }

    /**
     * 跳转到控制价分析页面
     *
     * @return
     */
    @RequestMapping("/controlPriceAnalysisPage")
    public ModelAndView controlPriceAnalysisPage(Integer id) {
        ModelAndView mav = new ModelAndView("/siteOpenBid/controlPriceAnalysisPage");
        mav.addObject("tenderDoc", tenderDocService.getTenderDocBySectionId(id));
        mav.addObject("bidSection", bidSectionService.getBidSectionById(id));
        return mav;
    }

    /**
     * 跳转到唱标页面
     *
     * @return
     */
    @RequestMapping("/fileCursorPage")
    public ModelAndView fileCursorPage(Integer id) {
        ModelAndView mav = new ModelAndView("/siteOpenBid/fileCursorPage");
        BidSection bidSection = bidSectionService.getBidSectionById(id);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(id);
        // 加载招标项目、标段信息
        mav.addObject("bidSection", bidSection);
        mav.addObject("tenderDoc", tenderDoc);
        return mav;
    }

    /**
     * 获取可以参与唱标的投标人列表
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/listBiddersForSing")
    public List<Bidder> listBiddersForSing(Integer bidSectionId) {
        return bidderService.listSiteBiddersForSing(bidSectionId);
    }

    /**
     * 标书决绝理由页面
     */
    @RequestMapping("/bidRejectionPage")
    public ModelAndView bidRejectionPage(Integer flag, Integer bidSectionId, Integer bidderId) {
        ModelAndView mav = new ModelAndView("/siteOpenBid/reason");
        BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidSectionId);
        mav.addObject("bidderOpenInfo", bidderOpenInfo);
        mav.addObject("flag", flag);
        return mav;
    }

    /**
     * 获取可以参与解密的投标人信息
     *
     * @param bidSectionId 标段id
     * @return
     */
    @RequestMapping("/listBiddersForDecrypt")
    public JsonData listBiddersForDecrypt(Integer bidSectionId) {
        JsonData jsonData = new JsonData();
        try {
            // 参与解密的投标人
            List<Bidder> bidders = bidderService.listAllBidders(bidSectionId, false);

            Map<String, Object> data = new HashMap<>(1);

            data.put("bidders", bidders);
            jsonData.setCode(ExecuteCode.SUCCESS.getCode().toString());
            jsonData.setMsg(bidders.size() == 0 ? "暂无投标人信息" : "");
            jsonData.setData(data);

        } catch (Exception e) {
            e.printStackTrace();
            jsonData.setCode(ExecuteCode.FAIL.getCode().toString());
            jsonData.setMsg(e.getMessage());
        }
        return jsonData;
    }

    /**
     * 保存投标人信息
     * @param bidder
     * @return
     */
    @RequestMapping("/saveBidderInfo")
    public JsonData saveBidderInfo(Bidder bidder) {
        Assert.notNull(bidder, "param bidder can not be null");
        Assert.notNull(bidder.getBidSectionId(), "param bidder.getBidSectionId() can not be null");
        JsonData data = new JsonData();
        try {
            if(StringUtils.isNotEmpty(bidder.getBidderName())) {
                List<Bidder> bidderList = bidderService.listBidderByName(bidder.getBidderName(), bidder.getBidSectionId());
                if (CollectionUtils.isNotEmpty(bidderList) && bidderList.size() > 0) {
                    data.setCode("2");
                    data.setMsg("投标人名称已存在！");
                    return data;
                }
            }

            int bidderId = bidderService.saveBidder(bidder);
            bidderOpenInfoService.insert(BidderOpenInfo.builder()
                    .bidSectionId(bidder.getBidSectionId())
                    .bidderId(bidderId)
                    .clientName(bidder.getLegalPerson())
                    .marginPayStatus(1)
                    .build());
            bidderFileInfoService.initBidderFileInfo(BidderFileInfo.builder()
                    .bidderId(bidderId)
                    .build());
            data.setCode("1");
            data.setMsg("新增成功！");
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            data.setCode("2");
            data.setMsg("新增失败！");
            return data;
        }
    }

    /**
     * 删除投标人
     *
     * @param ids 删除的投标人id
     * @param reason 原因
     * @return
     */
    @RequestMapping("/delBidders")
    public JsonData delBidders(String ids, String reason) {
        JsonData data = new JsonData();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                bidderService.updateBidderById(Bidder.builder()
                        .id(Integer.parseInt(id))
                        .deleteFlag(1)
                        .deleteReason(reason)
                        .build());
                BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(Integer.parseInt(id));
                bidderFileInfoService.updateById(BidderFileInfo.builder()
                        .id(bidderFileInfo.getId())
                        .bidderId(Integer.parseInt(id))
                        .build());
            }
            data.setCode("1");
            data.setMsg("删除成功！");
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            data.setCode("2");
            data.setMsg("删除失败！");
            return data;
        }
    }

    /**
     * 更新投标人信息
     *
     * @param bidder  更新数据
     * @return
     */
    @RequestMapping("/updateBidder")
    public boolean updateBidder(Bidder bidder) {
        return bidderService.updateBidderById(bidder);
    }

    /**
     * 投标文件解密
     *
     * @param fileId   文件id
     * @param bidderId 投标人id
     * @return
     */
    @RequestMapping("/siteDecryptGef")
    @UserLog(value = "'投标文件GEF解密: 文件id：'+#fileId+',投标人id：'+#bidderId+',标段id：'+#bidSectionId", dmlType = DMLType.UPDATE)
    public SiteBidDecryptDto siteDecryptGef(Integer fileId, Integer bidderId, Integer bidSectionId) {
        try {
            return siteOpenBidService.siteDecryptGef(fileId, bidderId, bidSectionId);
        } catch (Exception e) {
            e.printStackTrace();
            return SiteBidDecryptDto.builder()
                    .decryptStatus(false)
                    .nameConsistentStatus(true)
                    .decryptMsg(e.getMessage())
                    .build();
        }
    }

    /**
     * 获取拆信封后结构信息
     * @param fileId 拆信封的文件id
     * @return
     */
    @RequestMapping("/getDecoderCipherInfo")
    public JsonData getDecoderCipherInfo(Integer fileId) {
        JsonData result = new JsonData();
        try {
            DecoderCipherInfoDTO decoderCipherInfo = siteOpenBidService.getDecoderCipherInfo(fileId);
            result.setCode(ExecuteCode.SUCCESS.getCode().toString());
            result.setData(decoderCipherInfo);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(ExecuteCode.FAIL.getCode().toString());
        }
        return result;
    }

    /**
     * 投标文件解密
     *
     * @param fileId   文件id
     * @param bidderId 投标人id
     * @return
     */
    @RequestMapping("/siteDecryptSgef")
    @UserLog(value = "'投标文件SGEF解密: 文件id：'+#fileId+',投标人id：'+#bidderId+',标段id：'+#bidSectionId+',私钥：'+#privateKey+',是否互认解密：'+#isOtherCa", dmlType = DMLType.UPDATE)
    public SiteBidDecryptDto siteDecryptSgef(Integer fileId, Integer bidderId, Integer bidSectionId, String privateKey, String isOtherCa) {
        try {
            return siteOpenBidService.siteDecryptSgef(fileId, bidderId, bidSectionId, privateKey, isOtherCa);
        } catch (Exception e) {
            e.printStackTrace();
            return SiteBidDecryptDto.builder()
                    .decryptStatus(false)
                    .nameConsistentStatus(true)
                    .decryptMsg(e.getMessage())
                    .build();
        }
    }

    /**
     * 开标结束页面
     *
     * @param id 标段id
     * @return
     */
    @RequestMapping("/bidOpenEndPage")
    public ModelAndView bidOpenEndPage(Integer id) {
        return new ModelAndView("/siteOpenBid/bidOpenEndPage", "bidSection", bidSectionService.getBidSectionById(id));
    }

    /**
     * 开标结束弹窗页面
     *
     * @return 开标结束弹窗页面
     */
    @RequestMapping("/endSure")
    public ModelAndView bidOpenEndPage() {
        return new ModelAndView("/siteOpenBid/endSure");
    }

    /**
     * 检查是否完成开标页面
     *
     * @param id 标段id
     * @return
     */
    @RequestMapping("/checkFinishBidOpenPage")
    public ModelAndView checkFinishBidOpenPage(Integer id) {
        return new ModelAndView("/siteOpenBid/checkFinishBidOpenPage", "bidSection", bidSectionService.getBidSectionById(id));
    }

    /**
     * 调整选择投标人名称页面
     *
     * @param fileBidderName 文件投标人名称
     * @param dataBaseBidderName 数据库投标人名称
     * @return
     */
    @RequestMapping("/changeBidderNamePage")
    public ModelAndView changeBidderNamePage(String fileBidderName, String dataBaseBidderName) {
        ModelAndView mav = new ModelAndView("/siteOpenBid/changeBidderNamePage");
        mav.addObject("fileBidderName", fileBidderName);
        mav.addObject("dataBaseBidderName", dataBaseBidderName);
        return mav;
    }

    /**
     * 保存投标人解密信息
     *
     * @param siteBidDecryptDto 待更新的投标人
     * @return
     */
    @RequestMapping("/saveSiteBidDecryptInfo")
    public boolean saveSiteBidDecryptInfo(@RequestBody SiteBidDecryptDto siteBidDecryptDto) {
        Bidder updateBidder = siteBidDecryptDto.getUpdateBidder();
        BidderOpenInfo updateBidderOpenInfo = siteBidDecryptDto.getUpdateBidderOpenInfo();
        BidderFileInfo updateBidderFileInfo = siteBidDecryptDto.getUpdateBidderFileInfo();
        Boolean bidderStatus = bidderService.updateBidderById(updateBidder);

        if (!bidderStatus) {
            return false;
        }

        boolean openStatus = bidderOpenInfoService.updateById(updateBidderOpenInfo) == 1;

        if (!openStatus) {
            return false;
        }

        return  bidderFileInfoService.updateById(updateBidderFileInfo) == 1;
    }
}
