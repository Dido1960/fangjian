package com.ejiaoyi.common.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.annotation.RedissonLock;
import com.ejiaoyi.common.constant.*;
import com.ejiaoyi.common.dto.BidderReviewPointDTO;
import com.ejiaoyi.common.dto.DecoderCipherInfoDTO;
import com.ejiaoyi.common.dto.quantity.QuantityBidder;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.*;
import com.ejiaoyi.common.exception.CustomException;
import com.ejiaoyi.common.mapper.BidderMapper;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 投标人信息 服务实现类
 * </p>
 *
 * @author Make
 * @since 2020-7-15
 */
@Service
@Slf4j
public class BidderServiceImpl extends BaseServiceImpl implements IBidderService {

    @Autowired
    private BidderMapper bidderMapper;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private IDownloadService downloadService;

    @Autowired
    private IBsnChainInfoService bsnChainInfoService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IBidderFileInfoService bidderFileInfoService;

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private ILineMsgService lineMsgService;
    @Autowired
    private BidderQuantityServiceImpl bidderQuantityService;

    @Autowired
    private BidderReviewResultServiceImpl bidderReviewResultService;

    @Autowired
    private IBidderFileUploadService bidderFileUploadService;

    @Autowired
    private Environment env;

    @Override
    public Bidder getBidderById(Integer id) {
        Assert.notNull(id, "param id can not be null!");
        Bidder bidder = bidderMapper.selectById(id);
        if (bidder != null) {
            BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidder.getBidSectionId());
            bidder.setBidderOpenInfo(bidderOpenInfo);
        }
        return bidder;
    }

    @Override
    public Bidder getBidder(Bidder bidder) {
        QueryWrapper<Bidder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(bidder.getId() != null, "ID", bidder.getId());
        queryWrapper.eq(bidder.getBidSectionId() != null, "BID_SECTION_ID", bidder.getBidSectionId());
        queryWrapper.eq(bidder.getBidderOrgCode() != null, "BIDDER_ORG_CODE", bidder.getBidderOrgCode());
        queryWrapper.eq(bidder.getDeleteFlag() != null, "DELETE_FLAG", bidder.getDeleteFlag());
        return bidderMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Bidder> listBidderEnabled(Integer bidSectionId, Boolean ispage) {
        Bidder bidder = Bidder.builder()
                .bidSectionId(bidSectionId)
                .deleteFlag(0)
                .build();
        List<Bidder> bidders = this.listBidderByCondition(bidder, ispage);

        for (Bidder bidder1 : bidders) {
            QueryWrapper<BidderOpenInfo> query = new QueryWrapper<>();
            if (bidder1.getId() != null) {
                query.eq("BIDDER_ID", bidder1.getId());
                query.eq("BID_SECTION_ID", bidSectionId);
                bidder1.setBidderOpenInfo(bidderOpenInfoService.getBidderOpenInfo(bidder1.getId(), bidSectionId));
            }
        }

        return bidders;
    }

    @Override
    public boolean notCheckinReason(BidderOpenInfo bidderOpenInfo) {
        List<Integer> notCheckinNum = new ArrayList<>();
        notCheckinNum.add(1);
        notCheckinNum.add(2);
        notCheckinNum.add(9);
        if ((notCheckinNum.contains(bidderOpenInfo.getNotCheckin()))) {
            Bidder bidder = new Bidder();
            bidder.setId(bidderOpenInfo.getBidderId());
            bidder.setIsPassBidOpen(2);
            bidderMapper.updateById(bidder);
        }
        BidderOpenInfo bidderOpenInfoOld = bidderOpenInfoService.getBidderOpenInfo(bidderOpenInfo.getBidderId(), bidderOpenInfo.getBidSectionId());
        if (bidderOpenInfoOld != null) {
            bidderOpenInfo.setId(bidderOpenInfoOld.getId());
        } else {
            bidderOpenInfoService.insert(bidderOpenInfo);
        }
        bidderOpenInfoService.updateBidderOpenInfo(bidderOpenInfo);

        return true;
    }

    @Override
    public List<Bidder> listDecrySuccessBidder(Integer bidSectionId, boolean isPage) {
        if (isPage) {
            Page page = getPageForLayUI();
            return bidderMapper.listDecrySuccessBidder(page, bidSectionId);
        } else {
            return bidderMapper.listDecrySuccessBidder(bidSectionId);
        }
    }

    @Override
    public List<Bidder> listFailBidder(Integer bidSectionId) {
        List<Bidder> failBidders = bidderMapper.listFailBidder(bidSectionId);

        for (Bidder failBidder : failBidders) {
            //投标人开标信息
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(failBidder.getId(), bidSectionId);
            if (boi != null) {
                failBidder.setBidderOpenInfo(boi);
            }
            //投标金额大写
            if (StringUtils.isNotEmpty(failBidder.getBidderOpenInfo().getBidPrice())) {
                String bidPriceChinese;
                String price = failBidder.getBidderOpenInfo().getBidPrice();
                //判断价格是否是数字
                if (NumberUtil.isNumber(price)) {
                    bidPriceChinese = ConvertMoney.moneyToChinese(price);
                } else {
                    if (price.indexOf("%") != -1 && NumberUtil.isNumber(price.split("%")[0])) {
                        bidPriceChinese = "百分之" + ConvertMoney.moneyToChinese(price.split("%")[0]);
                    } else {
                        bidPriceChinese = price;
                    }
                }
                failBidder.setBidPriceChinese(bidPriceChinese);
            }
        }
        return failBidders;
    }

    @Override
    public List<Bidder> listBiddersForCheck(Integer bidSectionId) {
        List<Bidder> bidders = this.listAllBidders(bidSectionId, false);
        bidders.removeIf(bidder ->
                bidder.getBidderOpenInfo().getNotCheckin() != null
                        && bidder.getBidderOpenInfo().getNotCheckin() != 3);
        return bidders;
    }

    @Override
    public List<Bidder> listAllBidders(Integer bidSectionId, Boolean isPage) {
        List<Bidder> bidders = null;

        QueryWrapper<Bidder> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("DELETE_FLAG", 0);
        // 获取分页对象
        if (isPage) {
            Page<Bidder> page = this.getPageForLayUI();
            bidders = bidderMapper.selectPage(page, wrapper).getRecords();
        } else {
            bidders = bidderMapper.selectList(wrapper);
        }
        for (Bidder bidder : bidders) {
            BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSectionId);
            bidder.setBidderOpenInfo(boi);
            bidder.setBidderFileInfo(bidderFileInfo);
            //回执单id
            if (bidderFileInfo != null) {
                Fdfs receiptFile = fdfsService.getFdfdById(bidderFileInfo.getReceiptId());
                if (receiptFile != null) {
                    bidder.setReceiptFileId(receiptFile.getId());
                }
            }
        }
        return bidders;
    }

    @Override
    public List<Bidder> getBidder(String bidderOrgCode, Integer bidSectionId) {
        QueryWrapper<Bidder> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(bidderOrgCode)) {
            queryWrapper.eq("BIDDER_ORG_CODE", bidderOrgCode);
        }
        if (!CommonUtil.isEmpty(bidSectionId)) {
            queryWrapper.eq("BID_SECTION_ID", bidSectionId);
        }
        queryWrapper.eq("DELETE_FLAG", 0);
        List<Bidder> bidders = bidderMapper.selectList(queryWrapper);
        for (Bidder bidder : bidders) {
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSectionId);
            bidder.setBidderOpenInfo(boi);
        }
        return bidders;
    }

    @Override
    public List<Bidder> listPassBidOpenBidder(Integer bidSectionId) {
        QueryWrapper<Bidder> wrapper = new QueryWrapper<>();
        wrapper.eq("DELETE_FLAG", 0);
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("IS_PASS_BID_OPEN", 1);
        List<Bidder> bidders = bidderMapper.selectList(wrapper);
        for (Bidder bidder : bidders) {
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSectionId);
            bidder.setBidderOpenInfo(boi);
        }
        return bidders;
    }

    @Override
    public Map<String, Object> getBidder(Integer bidSectionId, String bidderOrgCode, String stage) {
        Map<String, Object> data = new HashMap<>();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        List<Bidder> bidders = this.getBidder(null, bidSectionId);
        // 过滤代理拒接的投标人
        bidders.removeIf(bidder ->
                bidder.getBidderOpenInfo().getNotCheckin() != null
                        && bidder.getBidderOpenInfo().getNotCheckin() != 3);

        Iterator<Bidder> iterator = bidders.iterator();
        Bidder currentBidder = null;
        while (iterator.hasNext()) {
            Bidder bidder = iterator.next();
            Integer decryptStatus = bidder.getBidderOpenInfo().getDecryptStatus();
            Integer tenderRejection = bidder.getBidderOpenInfo().getTenderRejection();
            if (stage.equals(StageConstant.CONFIRM_RECORD) && decryptStatus != 1) {
                iterator.remove();
            } else {
                //排除 被标书拒绝的投标人
                if ((tenderRejection != null && tenderRejection == 1)) {
                    iterator.remove();
                } else {
                    if (bidderOrgCode.equals(bidder.getBidderOrgCode())) {
                        currentBidder = bidder;
                        iterator.remove();
                    }
                }
            }
        }
        //是否是资格预审
        boolean isHide = BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode());

        data.put("otherBidders", bidders);
        data.put("currentBidder", currentBidder);
        data.put("isHide", isHide);
        return data;
    }

    @Override
    @RedissonLock(key = "#bidSectionId+'_'+#bidderOrgCode+'_'+#bidderName")
    public Boolean addBidderOpenInfo(Integer bidSectionId, String bidderOrgCode, String bidderName) throws Exception {
        if (StringUtils.isNotEmpty(bidderOrgCode)) {
            List<Bidder> bidders = getBidder(bidderOrgCode, bidSectionId);
            if (bidders.size() == 0) {
                Bidder bidder = Bidder.builder()
                        .bidderName(bidderName)
                        .bidderOrgCode(bidderOrgCode)
                        .bidSectionId(bidSectionId)
                        .build();
                if (bidderMapper.insert(bidder) > 0) {
                    Bidder bidderIns = getBidder(bidderOrgCode, bidSectionId).get(0);
                    BidderOpenInfo bidderOpenInfo = BidderOpenInfo.builder()
                            .bidSectionId(bidSectionId)
                            .bidderId(bidderIns.getId())
                            .build();

                    // 新增
                    BidderFileInfo bidderFileInfo = BidderFileInfo.builder()
                            .bidderId(bidderIns.getId())
                            .build();
                    BidderFileInfo initBidderFile = bidderFileInfoService.initBidderFileInfo(bidderFileInfo);
                    boolean isJoinBid = bidderOpenInfoService.insert(bidderOpenInfo) > 0 && initBidderFile != null;
                    if (!isJoinBid) {
                        throw new CustomException("参标失败！");
                    }
                    return true;
                }
            } else {
                throw new CustomException("用户已参标！");
            }
        } else {
            throw new CustomException("无效用户！");
        }

        return false;
    }

    @Override
    public Boolean confirmPrice(Bidder bidder) {
        BidderOpenInfo boi = BidderOpenInfo.builder()
                .id(bidder.getBidderOpenInfo().getId())
                .priceDetermine(1)
                .build();
        return bidderOpenInfoService.updateById(boi) > 0;
    }

    @Override
    public Boolean decrypt(Integer fileId, Integer bidderId, Integer bidSectionId, String fileType, String privateKey, String isOtherCa) {
        //获取待解密投标文件 redis标志
        String redisKey = "bidSection_" + bidSectionId + "_BidFileDecrypt_" + bidderId;
        Object bidFileDecrypt = RedisUtil.get(redisKey);
        //记录开始排队时间
        String queueStartTimeKey = "bidSection_" + bidSectionId + "_queueStartTime_" + bidderId;
        String queueStartTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        RedisUtil.set(queueStartTimeKey, queueStartTime);
        //当前排队人数
        String queueKey = "bidSection:" + bidSectionId;
        //解密中的人数
        String decryptingKey = "decrypting:" + bidSectionId;

        Bidder bidder = getBidderById(bidderId);
        BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidder.getBidSectionId());
        bidder.setBidderOpenInfo(boi);
        //修改投标文件解密开始时间
        BidderOpenInfo bidderOpenInfo = BidderOpenInfo.builder()
                .id(boi.getId())
                .decryptStartTime(queueStartTime)
                .build();
        bidderOpenInfoService.updateById(bidderOpenInfo);
        //获取投标文件信息
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        bidderOpenInfo.setBidderFileInfo(bidderFileInfo);
        if (bidFileDecrypt == null) {
            RedisUtil.set(redisKey, Status.NOT_START.getCode());
            //排队中的人数+1
            updateDecryptRedisNum(queueKey, 1);
            ThreadUtlis.run(() -> {
                try {
                    // 1. 设置开始解密
                    RedisUtil.set(redisKey, Status.PROCESSING.getCode());
                    //排队中的人数-1
                    updateDecryptRedisNum(queueKey, -1);
                    //解密中的人数+1
                    updateDecryptRedisNum(decryptingKey, 1);
                    // 2. 解密
                    if (FileType.GEFORTJY.getType().toString().equals(fileType)) {
                        gefDescrypt(fileId, bidder, bidderOpenInfo);
                    } else if (FileType.SGEFORETJY.getType().toString().equals(fileType)) {
                        if (Enabled.YES.getCode().toString().equals(isOtherCa)) {
                            otherSgefDescrypt(fileId, bidder, bidderOpenInfo);
                        } else {
                            sgefDescrypt(fileId, bidder, bidderOpenInfo, privateKey);
                        }
                    }
                } catch (Exception e) {
                    //抛出异常时修改解密状态为失败
                    e.printStackTrace();
                    BidderOpenInfo newBidderOpenInfo = BidderOpenInfo.builder()
                            .id(boi.getId())
                            .decryptStatus(2)
                            .tenderDecryptStatus(2)
                            .build();
                    bidderOpenInfoService.updateById(newBidderOpenInfo);
                } finally {
                    // 3.解密结束 修改缓存
                    RedisUtil.delete(redisKey);
                    RedisUtil.delete(queueStartTimeKey);
                    //解密中的人数-1
                    updateDecryptRedisNum(decryptingKey, -1);
                    // 文件解析完成，删除本地缓存文件
//                    String customFilePath = FileUtil.getDecryptBidderFilePath(fileId.toString());
//                    FileUtil.removeDir(new File(customFilePath));
                    //上传时间
                    String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                    //上传区块链
                    ThreadUtlis.run(() -> {
                        try {
                            List<Bidder> bidder2 = getBidder(bidder.getBidderOrgCode(), bidder.getBidSectionId());
                            bsnChainInfoService.decryptBsnChainPut(fileId, bidder2.get(0), nowTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }
        return true;
    }

    @Override
    public Bidder getBidderReviewPoints(Integer bidderId) {
        Assert.notNull(bidderId, "param bidderId not be null!");
        Bidder bidder = bidderMapper.selectById(bidderId);
        if (bidder.getBidDocId() != null) {
            // 下载路径
            String outPath = FileUtil.getCustomFilePath() + UUID.randomUUID().toString() + ".xml";
            try {
                List<BidderReviewPointDTO> bidderReviewPointDTOS = bidderFileInfoService.saveBidderReviewPoint(outPath, bidderId, bidder.getBidSectionId(), true);
                bidder.setBidderReviewPointDTOS(bidderReviewPointDTOS);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("bidder:" + bidderId + " review point parse exception: " + e.getMessage());
            } finally {
                FileUtil.deleteFile(outPath);
            }
        }
        return bidder;
    }

    @Override
    public Boolean updateBidderById(Bidder bidder) {
        return bidderMapper.updateById(bidder) > 0;
    }

    @Override
    public Map<String, Integer> getDecryptNum(Integer bidSectionId) {
        HashMap<String, Integer> resultMap = new HashMap<>();
        List<Bidder> list = this.listBiddersForDecrypt(bidSectionId);
        resultMap.put("decryptNum", list.size());
        //获取解密成功的人数
        resultMap.put("successNum", getDecryptSuccessNum(bidSectionId, list));
        return resultMap;
    }

    @Override
    public List<Bidder> listBiddersForDecrypt(Integer bidSectionId) {
        List<Bidder> bidders = this.listBiddersForCheck(bidSectionId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        boolean isQualification = BidProtype.QUALIFICATION.getCode().equals(bidSection.getBidClassifyCode());

        Iterator<Bidder> iterator = bidders.iterator();
        while (iterator.hasNext()) {
            Bidder bidder = iterator.next();
            BidderOpenInfo boi = bidder.getBidderOpenInfo();
            Integer bidderIdentityStatus = boi.getBidderIdentityStatus();
            Integer marginPayStatus = boi.getMarginPayStatus();
            if (bidderIdentityStatus != null && bidderIdentityStatus == 0) {
                iterator.remove();
                continue;
            }
            if (!isQualification && marginPayStatus != null && marginPayStatus == 0) {
                iterator.remove();
            }

        }
        return calDecryptTime(bidders, bidSectionId);
    }

    @Override
    public List<Bidder> listBiddersForSing(Integer bidSectionId) {
        List<Bidder> bidders = bidderMapper.listBiddersForSing(bidSectionId);
        for (Bidder bidder : bidders) {
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSectionId);
            bidder.setBidderOpenInfo(boi);
            // 设置唱标内容
            bidder.setBidPrice(boi.getBidPrice());
            bidder.setMarginPay(boi.getMarginPay());
            bidder.setTimeLimit(boi.getTimeLimit());
            bidder.setQuality(boi.getQuality());
            bidder.setBidPriceType(boi.getBidPriceType());

            LineMsg lineMsg = LineMsg.builder()
                    .bidSectionId(bidSectionId)
                    .bidderId(bidder.getId())
                    .question(0)
                    .build();
            //查询投标人质询状态
            if (lineMsgService.listDissentLineMsg(lineMsg).size() > 0) {
                bidder.setQuestion(lineMsg.getQuestion());
            } else {
                lineMsg.setQuestion(1);
                if (lineMsgService.listDissentLineMsg(lineMsg).size() > 0) {
                    bidder.setQuestion(lineMsg.getQuestion());
                }
            }
        }
        return bidders;
    }

    @Override
    public List<Bidder> listDetailedBidder(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null !");
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] allGradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(allGradeIds, EvalProcess.PRELIMINARY.getCode());
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())) {
            //EPC增加 资格审查grade
            grades.addAll(gradeService.listGrade(allGradeIds, EvalProcess.QUALIFICATION.getCode()));
        }
        String[] gradeIds = new String[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            gradeIds[i] = String.valueOf(grades.get(i).getId());
        }
        List<Bidder> bidders = bidderMapper.listDetailedBidder(bidSectionId, gradeIds);
        for (Bidder bidder : bidders) {
            BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSectionId);
            bidder.setBidderOpenInfo(bidderOpenInfo);
        }

        return bidders;
    }

    @Override
    public List<Bidder> listPassDetailedBidder(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null !");
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] allGradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(allGradeIds, EvalProcess.DETAILED.getCode());
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        if (BidProtype.EPC.getCode().equals(bidSection.getBidClassifyCode())) {
            //EPC增加 资格审查grade
            grades.addAll(gradeService.listGrade(allGradeIds, EvalProcess.QUALIFICATION.getCode()));
        }
        String[] gradeIds = new String[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            gradeIds[i] = String.valueOf(grades.get(i).getId());
        }
        List<Bidder> bidders = bidderMapper.listDetailedBidder(bidSectionId, gradeIds);
        for (Bidder bidder : bidders) {
            BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSectionId);
            bidder.setBidderOpenInfo(bidderOpenInfo);
        }

        return bidders;
    }

    @Override
    public List<Bidder> listNoPassFirstStepBidder(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null !");
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        List<Grade> grades = gradeService.listGrade(tenderDoc.getGradeId().split(","), EvalProcess.PRELIMINARY.getCode());
        String[] gradeIds = new String[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            gradeIds[i] = String.valueOf(grades.get(i).getId());
        }
        List<Bidder> bidders = bidderMapper.listNoPassFirstStepBidder(bidSectionId, gradeIds);
        for (Bidder bidder : bidders) {
            bidder.setBidderOpenInfo(bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSectionId));
        }

        return bidders;
    }

    @Override
    public Boolean updateBidderInfo(BidderOpenInfo bidderOpenInfo, Integer isPassBidOpen) {
        Boolean flag = bidderOpenInfoService.updateBidderOpenInfoById(bidderOpenInfo) > 0;
        BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfoById(bidderOpenInfo.getId());
        Bidder bidder = new Bidder();
        bidder.setId(boi.getBidderId());
        bidder.setIsPassBidOpen(isPassBidOpen);
        Boolean bFlag = this.updateBidderById(bidder);
        return flag && bFlag;
    }

    @Override
    public List<Bidder> calDecryptTime(List<Bidder> bidders, Integer bidSectionId) {
        for (Bidder bidder : bidders) {
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSectionId);
            bidder.setBidderOpenInfo(boi);
            String decryptStartTime = boi.getDecryptStartTime();
            String decryptEndTime = boi.getDecryptEndTime();
            if (!CommonUtil.isEmpty(decryptStartTime) && !CommonUtil.isEmpty(decryptEndTime)) {
                bidder.setDecryptStatus(2);
                long timeDiff = DateTimeUtil.getTimeDiff(decryptStartTime, decryptEndTime, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                String mm = Long.toString(timeDiff / 60);
                String ss = Long.toString(timeDiff % 60);
                bidder.setDecryptTimeMinute(mm);
                bidder.setDecryptTimeSecond(ss.length() > 1 ? ss : "0" + ss);
            }
            if (!CommonUtil.isEmpty(decryptStartTime) && CommonUtil.isEmpty(decryptEndTime)) {
                bidder.setDecryptStatus(1);
            }
            if (CommonUtil.isEmpty(decryptStartTime)) {
                bidder.setDecryptStatus(0);
            }
        }
        return bidders;
    }

    @Override
    public void dockUploadBidFile(Integer fileId, Bidder bidder) {
        Fdfs fdfs = fdfsService.downloadByUpload(fileId);
        String fileMD5 = FileUtil.getFileMD5(fdfs.getBytes());

        Integer bidderId = bidder.getId();
        BidderFileInfo bfi = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        Integer bidOpenInfoId = bidder.getBidderOpenInfo().getId();

        BidderOpenInfo bidderOpenInfo = new BidderOpenInfo();
        bidderOpenInfo.setId(bidOpenInfoId);
        bfi.setGefId(fileId);
        bfi.setGefHash(fileMD5);

        bidderOpenInfoService.updateById(bidderOpenInfo);

        Bidder bidderNew = new Bidder();
        bidderNew.setId(bidderId);
        bidderNew.setBidDocId(fileId);
        bidderNew.setBidDocType(0);
        bidderMapper.updateById(bidderNew);

    }

    @Override
    public void updateBiddersIsPassBidOpen(Integer bidderId, Integer isPassBidOpen) {
        Bidder bidder = Bidder.builder().id(bidderId).isPassBidOpen(isPassBidOpen).build();
        bidderMapper.updateById(bidder);
    }

    @Override
    public List<Bidder> listBidderPassQualifyReview(Integer bidSectionId) {
        Assert.notNull(bidSectionId, "param bidSectionId can not be null !");
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        List<Grade> grades = gradeService.listGrade(tenderDoc.getGradeId().split(","), EvalProcess.QUALIFICATION.getCode());
        String[] gradeIds = new String[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            gradeIds[i] = String.valueOf(grades.get(i).getId());
        }
        List<Bidder> bidders = bidderMapper.listDetailedBidder(bidSectionId, gradeIds);
        for (Bidder bidder : bidders) {
            BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSectionId);
            bidder.setBidderOpenInfo(bidderOpenInfo);
        }

        return bidders;
    }

    @Override
    public List<Bidder> pageTenderDecryptBidders(Integer bidSectionId, Boolean isQualification) {
        Page<Bidder> page = this.getPageForLayUI();
        List<Bidder> bidders = bidderMapper.listTenderDecryptBidders(page, bidSectionId, isQualification);
        return calDecryptTime(bidders, bidSectionId);
    }

    @Override
    public List<QuantityBidder> listClearQuantityBidder(Integer bidSectionId) {
        List<Bidder> bidders = listDecrySuccessBidder(bidSectionId, false);
        List<QuantityBidder> quantityBidders = new ArrayList<>();
        for (Bidder bidder : bidders) {
            QuantityBidder quantityBidder = QuantityBidder.builder()
                    .name(bidder.getBidderName())
                    .creditCode(bidder.getBidderOrgCode())
                    .bidXmlUid(bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId()).getXmlUid())
                    .build();
            quantityBidders.add(quantityBidder);
        }
        return quantityBidders;
    }

    @Override
    public boolean validAllQuantityService(Integer bidSectionId) {
        List<Bidder> bidders = listDecrySuccessBidder(bidSectionId, false);
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        for (Bidder bidder : bidders) {
            BidderQuantity bidderQuantity = bidderQuantityService.getBidderQuantityByBidXmlUid(bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId()).getXmlUid());
            if (bidderQuantity == null) {
                return false;
            }

            if (Enabled.YES.getCode().equals(tenderDoc.getStructureStatus())) {
                if (!ServiceState.COMPLETED.getCode().equals(bidderQuantity.getStructureAnalysisState())) {
                    return false;
                }
            }

            if (Enabled.YES.getCode().equals(tenderDoc.getPriceStatus())) {
                if (!ServiceState.COMPLETED.getCode().equals(bidderQuantity.getPriceAnalysisState())) {
                    return false;
                }
            }

            if (Enabled.YES.getCode().equals(tenderDoc.getFundBasisStatus())) {
                if (!ServiceState.COMPLETED.getCode().equals(bidderQuantity.getRuleAnalysisState())) {
                    return false;
                }
            }

            if (!ServiceState.COMPLETED.getCode().equals(bidderQuantity.getArithmeticAnalysisState())) {
                return false;
            }

        }
        return true;
    }

    @Override
    public List<QuantityBidder> listPriceScoreQuantityBidder(Integer bidSectionId) {
        List<Bidder> bidders = listDetailedBidder(bidSectionId);
        List<QuantityBidder> quantityBidders = new ArrayList<>();
        for (Bidder bidder : bidders) {
            QuantityBidder quantityBidder = QuantityBidder.builder()
                    .name(bidder.getBidderName())
                    .creditCode(bidder.getBidderOrgCode())
                    .bidXmlUid(bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId()).getXmlUid())
                    .build();
            quantityBidders.add(quantityBidder);
        }
        return quantityBidders;
    }

    @Override
    public Integer saveBidder(Bidder bidder) {
        bidderMapper.insert(bidder);
        return bidder.getId();
    }

    @Override
    public boolean validProcessPassInfo(Integer bidSectionId, Integer bidderId, Integer reviewProcess) {
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String[] gradeIds = tenderDoc.getGradeId().split(",");
        List<Grade> grades = gradeService.listGrade(gradeIds, reviewProcess);
        if (CollectionUtil.isEmpty(grades) || grades.size() == 0) {
            return false;
        }
        for (Grade grade : grades) {
            BidderReviewResult bidderReviewResult = bidderReviewResultService.getBidderReviewResult(BidderReviewResult.builder()
                    .bidSectionId(bidSectionId)
                    .gradeId(grade.getId())
                    .bidderId(bidderId)
                    .build());
            if (bidderReviewResult == null) {
                return false;
            }

            if (Enabled.NO.getCode().toString().equals(bidderReviewResult.getResult())) {
                return false;
            }
        }

        return true;
    }

    /**
     * gef 文件解密
     *
     * @param fileId         文件id
     * @param bidder         投标人
     * @param bidderOpenInfo 修改的开标信息
     * @throws Exception
     */
    private void gefDescrypt(Integer fileId, Bidder bidder, BidderOpenInfo bidderOpenInfo) throws Exception {
        //投标文件解密路径
        String customFilePath = FileUtil.getDecryptBidderFilePath(fileId.toString());
        //解压路径
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        UploadFile gef = uploadFileService.getById(fileId);
        String localPath = FileUtil.getCustomFilePath() + gef.getPath();
        // 解压文件路径
        String outPath;
        if (!new File(localPath).exists()) {
            // fdfs路径
            String fdfsFilePath = fdfsService.getUrlByUpload(fileId);
            outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + FileType.GEFORTJY.getSuffix();
            // 文件下载
            byte[] bytes = fdfsService.downloadByUrl(fdfsFilePath);
            FileUtil.writeFile(bytes, outPath);
//            downloadService.multiThreadDownload(fdfsFilePath, outPath);
        } else {
            outPath = localPath;
        }
        // 解压
        boolean unzip = CompressUtil.unzip(outPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
        if (!unzip) {
            // 文件解析失败，删除本地缓存文件
            FileUtil.removeDir(new File(customFilePath));
            bidderOpenInfo.setDecryptStatus(2);
            bidderOpenInfo.setTenderDecryptStatus(2);
            bidderOpenInfoService.updateById(bidderOpenInfo);
        } else {
            //保存文件解密路径
            BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfo.getId())
                    .fileUnzipPath(unzipPath)
                    .build());

            //文件是否破损
            boolean validResult = true;
            List<File> files = FileUtil.listFileByFormat(unzipPath, FileType.PDF.getSuffix());
            for (File file : files) {
                if (!PDFUtil.validPdf(file.getAbsolutePath())) {
                    validResult = false;
                    bidderOpenInfo.setSealStatus(0);
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    break;
                }
            }
            //文件无破损
            if (validResult) {
                //判断招标解密是否解密成功
                boolean tenderDecryptFlag = parseBidFileInfo(unzipPath, bidder, false);
                if (tenderDecryptFlag) {
                    BidSection bidSection = bidSectionService.getBidSectionById(bidder.getBidSectionId());
                    String bidPrice = null;
                    // 是否是施工类标段
                    boolean isConstruction = BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode());
                    if (isConstruction) {
                        // 解析清单报价更新到数据库
                        String bidProjectInfoPath = unzipPath + File.separator + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;
                        bidPrice = TenderXmlUtil.parseQuantityXmlByFilePath(bidProjectInfoPath);
                    }

                    // 多线程上传文件到文件服务器（施工类：先上传工程量清单xml,然后在上传其他文件）
                    ThreadUtlis.run(() -> {
                        bidderFileUploadService.bidderFileUpload(bidder.getId(), fileId, new File(unzipPath), isConstruction);
                    });

                    //设置解密状态为成功
                    String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                    bidderOpenInfo.setDecryptStatus(1);
                    bidderOpenInfo.setTenderDecryptStatus(1);
                    bidderOpenInfo.setDecryptEndTime(nowTime);
                    if (StringUtils.isNotEmpty(bidPrice)) {
                        bidderOpenInfo.setBidPrice(bidPrice);
                    }
                    //修改解密状态
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    //修改默认投标文件类型
                    Bidder bidder1 = Bidder.builder()
                            .id(bidder.getId())
                            .bidDocId(fileId)
                            .bidDocType(FileType.GEFORTJY.getType())
                            .build();
                    bidderMapper.updateById(bidder1);
                } else {
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                }
            }
        }

    }

    /**
     * sgef 文件解密（BJCA锁解密）
     *
     * @param fileId         文件id
     * @param bidder         投标人
     * @param bidderOpenInfo 待修改的开标信息
     * @param privateKey     加密因子
     * @throws Exception
     */
    private void sgefDescrypt(Integer fileId, Bidder bidder, BidderOpenInfo bidderOpenInfo, String privateKey) throws Exception {
        //投标文件解密路径
        String customFilePath = FileUtil.getDecryptBidderFilePath(String.valueOf(fileId));
        //解压路径
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        UploadFile gef = uploadFileService.getById(fileId);
        String localPath = FileUtil.getCustomFilePath() + gef.getPath();
        // 解压文件路径
        String outPath;
        if (!new File(localPath).exists()) {
            //fdfs路径
            String fdfsFilePath = fdfsService.getUrlByUpload(fileId);
            //下载路径
            outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + FileType.SGEFORETJY.getSuffix();
            // 文件下载
            byte[] bytes = fdfsService.downloadByUrl(fdfsFilePath);
            FileUtil.writeFile(bytes, outPath);
//            downloadService.multiThreadDownload(fdfsFilePath, outPath);
        } else {
            outPath = localPath;
        }
        // sgef信封解密后的gef
        String cipherPath = customFilePath + File.separator + "decryptSGef." + FileType.GEFORTJY.getSuffix();
        // sgef信封解密
        CertTools.fileDecoder(new File(outPath), cipherPath, privateKey);
        //解压
        boolean unzip = CompressUtil.unzip(cipherPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
        if (!unzip) {
            // 文件解析失败，删除本地缓存文件
            FileUtil.removeDir(new File(customFilePath));
            bidderOpenInfo.setDecryptStatus(2);
            bidderOpenInfo.setTenderDecryptStatus(2);
            bidderOpenInfoService.updateById(bidderOpenInfo);
        } else {
            //保存文件解密路径
            BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfo.getId())
                    .fileUnzipPath(unzipPath)
                    .build());

            //文件是否破损
            boolean validResult = true;
            List<File> files = FileUtil.listFileByFormat(unzipPath, FileType.PDF.getSuffix());
            for (File file : files) {
                if (!PDFUtil.validPdf(file.getAbsolutePath())) {
                    validResult = false;
                    bidderOpenInfo.setDecryptStatus(2);
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    break;
                }
            }
            //文件无破损
            if (validResult) {
                //判断招标解密是否解密成功
                boolean tenderDecryptFlag = parseBidFileInfo(unzipPath, bidder, false);
                if (tenderDecryptFlag) {
                    BidSection bidSection = bidSectionService.getBidSectionById(bidder.getBidSectionId());
                    String bidPrice = null;
                    // 是否是施工类标段
                    boolean isConstruction = BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode());
                    if (isConstruction) {
                        // 解析清单报价更新到数据库
                        String bidProjectInfoPath = unzipPath + File.separator + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;
                        bidPrice = TenderXmlUtil.parseQuantityXmlByFilePath(bidProjectInfoPath);
                    }

                    // 多线程上传文件到文件服务器（施工类：先上传工程量清单xml,然后在上传其他文件）
                    ThreadUtlis.run(() -> {
                        bidderFileUploadService.bidderFileUpload(bidder.getId(), fileId, new File(unzipPath), isConstruction);
                    });

                    String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                    // 设置解密状态为成功
                    bidderOpenInfo.setDecryptStatus(1);
                    bidderOpenInfo.setTenderDecryptStatus(1);
                    bidderOpenInfo.setDecryptEndTime(nowTime);
                    if (StringUtils.isNotEmpty(bidPrice)) {
                        bidderOpenInfo.setBidPrice(bidPrice);
                    }
                    // 修改解密状态
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    // 修改默认投标文件类型
                    Bidder bidder1 = Bidder.builder()
                            .id(bidder.getId())
                            .bidDocId(fileId)
                            .bidDocType(FileType.SGEFORETJY.getType())
                            .build();
                    bidderMapper.updateById(bidder1);
                } else {
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                }
            }
        }
    }

    /**
     * sgef 文件解密(互认锁解密)
     *
     * @param fileId         文件id
     * @param bidder         投标人
     * @param bidderOpenInfo 待修改的开标信息
     * @throws Exception
     */
    private void otherSgefDescrypt(Integer fileId, Bidder bidder, BidderOpenInfo bidderOpenInfo) throws Exception {
        //投标文件解密路径
        String customFilePath = FileUtil.getDecryptBidderFilePath(String.valueOf(fileId));
        //解压路径
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        UploadFile gef = uploadFileService.getById(fileId);
        String localPath = FileUtil.getCustomFilePath() + gef.getPath();
        // 解压文件路径
        String outPath;
        if (!new File(localPath).exists()) {
            // fdfs路径
            String fdfsFilePath = fdfsService.getUrlByUpload(fileId);
            //下载路径
            outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + FileType.SGEFORETJY.getSuffix();
            // 文件下载
            byte[] bytes = fdfsService.downloadByUrl(fdfsFilePath);
            FileUtil.writeFile(bytes, outPath);
        } else {
            outPath = localPath;
        }
        // sgef信封解密后的gef
        String cipherPath = customFilePath + File.separator + "decryptSGef." + FileType.GEFORTJY.getSuffix();
        DecoderCipherInfoDTO decoderCipherInfoDTO = CertTools.otherFileDecoder(new File(outPath), cipherPath);
        String fileType = null;
        if (decoderCipherInfoDTO != null) {
            fileType = decoderCipherInfoDTO.getFileType();
        }

        if (!Enabled.YES.getCode().toString().equals(fileType)) {
            setRedisMsg(bidder.getBidSectionId(), bidder.getId(), "解密的文件非互认文件！");
            throw new Exception("解密的文件非互认文件！");
        }
        //解压
        boolean unzip = CompressUtil.unzip(cipherPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
        if (!unzip) {
            // 文件解析失败，删除本地缓存文件
            FileUtil.removeDir(new File(customFilePath));
            bidderOpenInfo.setDecryptStatus(2);
            bidderOpenInfo.setTenderDecryptStatus(2);
            bidderOpenInfoService.updateById(bidderOpenInfo);
        } else {
            //保存文件解密路径
            BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfo.getId())
                    .fileUnzipPath(unzipPath)
                    .build());

            //文件是否破损
            boolean validResult = true;
            List<File> files = FileUtil.listFileByFormat(unzipPath, FileType.PDF.getSuffix());
            for (File file : files) {
                if (!PDFUtil.validPdf(file.getAbsolutePath())) {
                    validResult = false;
                    bidderOpenInfo.setDecryptStatus(2);
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    break;
                }
            }
            //文件无破损
            if (validResult) {
                //判断招标解密是否解密成功
                boolean tenderDecryptFlag = parseBidFileInfo(unzipPath, bidder, false);
                if (tenderDecryptFlag) {
                    BidSection bidSection = bidSectionService.getBidSectionById(bidder.getBidSectionId());
                    String bidPrice = null;
                    // 是否是施工类标段
                    boolean isConstruction = BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode());
                    if (isConstruction) {
                        // 解析清单报价更新到数据库
                        String bidProjectInfoPath = unzipPath + File.separator + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;
                        bidPrice = TenderXmlUtil.parseQuantityXmlByFilePath(bidProjectInfoPath);
                    }

                    // 多线程上传文件到文件服务器（施工类：先上传工程量清单xml,然后在上传其他文件）
                    ThreadUtlis.run(() -> {
                        bidderFileUploadService.bidderFileUpload(bidder.getId(), fileId, new File(unzipPath), isConstruction);
                    });

                    String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                    // 设置解密状态为成功
                    bidderOpenInfo.setDecryptStatus(1);
                    bidderOpenInfo.setTenderDecryptStatus(1);
                    bidderOpenInfo.setDecryptEndTime(nowTime);
                    if (StringUtils.isNotEmpty(bidPrice)) {
                        bidderOpenInfo.setBidPrice(bidPrice);
                    }
                    // 修改解密状态
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    // 修改默认投标文件类型
                    Bidder bidder1 = Bidder.builder()
                            .id(bidder.getId())
                            .bidDocId(fileId)
                            .bidDocType(FileType.SGEFORETJY.getType())
                            .build();
                    bidderMapper.updateById(bidder1);
                } else {
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                }
            }
        }
    }

    /**
     * sgef 文件解密(手机证书解密)
     * 手机证书解密方式：1 按工具文件流方式解析，将sgef解密成des加密的文件
     * 2 按des加密方式将文件解密成gef
     * 3 将gef解压缩即可
     *
     * @param fileId         文件id
     * @param bidder         投标人
     * @param bidderOpenInfo 待修改的开标信息
     * @param phoneCertNo    手机证书号
     * @throws Exception
     */
    private void phoneSgefDecrypt(Integer fileId, Bidder bidder, BidderOpenInfo bidderOpenInfo, String phoneCertNo) throws Exception {
        //投标文件解密路径
        String customFilePath = FileUtil.getDecryptBidderFilePath(String.valueOf(fileId));
        //解压路径
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        UploadFile gef = uploadFileService.getById(fileId);
        String localPath = FileUtil.getCustomFilePath() + gef.getPath();
        // 解压文件路径
        String outPath;
        if (!new File(localPath).exists()) {
            // fdfs路径
            Fdfs fdfs = fdfsService.downloadByUpload(fileId);
            //下载路径
            outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + FileType.SGEFORETJY.getSuffix();
            // 文件下载
            FileUtil.writeFile(fdfs.getBytes(), outPath);
        } else {
            outPath = localPath;
        }
        // sgef信封解密后的des加密文件
        String desFilePath = customFilePath + File.separator + "desDecryptFile";
        String cipherPath = customFilePath + File.separator + "decryptSGef." + FileType.GEFORTJY.getSuffix();
        DecoderCipherInfoDTO decoderCipherInfoDTO = CertTools.otherFileDecoder(new File(outPath), desFilePath);
        if (decoderCipherInfoDTO != null
                && (StringUtils.isEmpty(phoneCertNo)
                || !phoneCertNo.equals(decoderCipherInfoDTO.getCertId()))) {
            log.error("phoneCertNo:" + phoneCertNo);
            log.error("decoderCipherInfoDTO.getCertId():" + decoderCipherInfoDTO.getCertId());
            setRedisMsg(bidder.getBidSectionId(), bidder.getId(), "非正确的加密证书，请选择正确的加密证书进行解密！");
            throw new Exception("非正确的加密证书，请选择正确的加密证书进行解密！");
        }
        // des解密
        DesUtil.decryptFile(decoderCipherInfoDTO.getCipher(), desFilePath, cipherPath);

        // 解压
        boolean unzip = CompressUtil.unzip(cipherPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());

        if (!unzip) {
            // 文件解析失败，删除本地缓存文件
            FileUtil.removeDir(new File(customFilePath));
            bidderOpenInfo.setDecryptStatus(2);
            bidderOpenInfo.setTenderDecryptStatus(2);
            bidderOpenInfoService.updateById(bidderOpenInfo);
        } else {

            //保存文件解密路径
            BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfo.getId())
                    .fileUnzipPath(unzipPath)
                    .build());

            //文件是否破损
            boolean validResult = true;
            List<File> files = FileUtil.listFileByFormat(unzipPath, FileType.PDF.getSuffix());
            for (File file : files) {
                if (!PDFUtil.validPdf(file.getAbsolutePath())) {
                    validResult = false;
                    bidderOpenInfo.setDecryptStatus(2);
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    break;
                }
            }
            //文件无破损
            if (validResult) {
                //判断招标解密是否解密成功
                boolean tenderDecryptFlag = parseBidFileInfo(unzipPath, bidder, false);
                if (tenderDecryptFlag) {
                    BidSection bidSection = bidSectionService.getBidSectionById(bidder.getBidSectionId());
                    String bidPrice = null;
                    // 是否是施工类标段
                    boolean isConstruction = BidProtype.CONSTRUCTION.getCode().equals(bidSection.getBidClassifyCode());
                    if (isConstruction) {
                        // 解析清单报价更新到数据库
                        String bidProjectInfoPath = unzipPath + File.separator + TenderFileConstant.ENGINEER_QUANTITY_LIST_XML;
                        bidPrice = TenderXmlUtil.parseQuantityXmlByFilePath(bidProjectInfoPath);
                    }

                    // 多线程上传文件到文件服务器（施工类：先上传工程量清单xml,然后在上传其他文件）
                    ThreadUtlis.run(() -> {
                        bidderFileUploadService.bidderFileUpload(bidder.getId(), fileId, new File(unzipPath), isConstruction);
                    });

                    String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                    // 设置解密状态为成功
                    bidderOpenInfo.setDecryptStatus(1);
                    bidderOpenInfo.setTenderDecryptStatus(1);
                    bidderOpenInfo.setDecryptEndTime(nowTime);
                    if (StringUtils.isNotEmpty(bidPrice)) {
                        bidderOpenInfo.setBidPrice(bidPrice);
                    }
                    // 修改解密状态
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    // 修改默认投标文件类型
                    Bidder bidder1 = Bidder.builder()
                            .id(bidder.getId())
                            .bidDocId(fileId)
                            .bidDocType(FileType.SGEFORETJY.getType())
                            .build();
                    bidderMapper.updateById(bidder1);
                } else {
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                }
            }
        }
    }

    /**
     * 解析XML投标文件
     *
     * @param unzipPath 解压路径
     * @param bidder    投标人
     * @param isPaper   是否纸质标
     * @return 招标解密状态校验
     * @throws Exception
     */
    private boolean parseBidFileInfo(String unzipPath, Bidder bidder, boolean isPaper) throws Exception {
        // 解析投标项目相关信息
        String bidRelateInfoPath = unzipPath + File.separator + TenderFileConstant.TENDER_RELATE_CONTENT;
        Map<String, String> tenderRelateInfo = TenderXmlUtil.parseTenderRelateInfo(bidRelateInfoPath);
        if (CommonUtil.isEmpty(tenderRelateInfo)) {
            throw new CustomException("投标文件解密失败！");
        }
        Integer bidSectionId = bidder.getBidSectionId();
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);

        // 投标文件项目类型
        String tenderClassifyCode = tenderRelateInfo.get("bidClassifyCode");
        BidProtype bidProtype = BidProtype.QUALIFICATION;
        if ("EPCQualification".equals(tenderClassifyCode)) {
            tenderClassifyCode = BidProtype.QUALIFICATION.getName();
        }

        // 解析投标项目信息
        String bidProjectInfoPath = unzipPath + File.separator + TenderFileConstant.TENDER_INFO;
        Map<String, String> bidProjectInfo = TenderXmlUtil.parseTenderDetailInfo(bidProjectInfoPath, bidProtype);

        // 纸质标无评审点
        if (!isPaper) {
            // 解析评审点
            String reviewPoint = unzipPath + File.separator + TenderFileConstant.JUDGING_POINT_XML;
            bidderFileInfoService.saveBidderReviewPoint(reviewPoint, bidder.getId(), bidSectionId, false);
        }

        // 修改投标人开标信息
        BidderOpenInfo bidderOpenInfo = BidderOpenInfo.builder()
                .id(bidder.getBidderOpenInfo().getId())
                .bidPriceType(bidProjectInfo.get(BidInfoTemplateConstant.QUOTE_TYPE))
                .bidPrice(bidProjectInfo.get(BidInfoTemplateConstant.TOTAL_QUOTE))
                .timeLimit(bidProjectInfo.get(BidInfoTemplateConstant.TOTAL_TIME_LIMIT))
                .quality(bidProjectInfo.get(BidInfoTemplateConstant.QUALITY_INFORMATION))
                .rate(bidProjectInfo.get(BidInfoTemplateConstant.RATE))
                .build();
        if (isPaper) {
            bidderOpenInfo.setTenderDecryptStatus(1);
            return bidderOpenInfoService.updateById(bidderOpenInfo) == 1;
        } else {
            //判断投标文件的 招标解密状态
            String bidCode = bidProjectInfo.get(BidInfoTemplateConstant.SECT_CODE);
            boolean bidCodeFlag = bidCode.equals(bidSection.getBidSectionCode());
            boolean claCodeFlag = tenderClassifyCode.equals(bidSection.getBidClassifyCode());
            if (bidCodeFlag && claCodeFlag) {
                bidderOpenInfo.setTenderDecryptStatus(1);
            }
            bidderOpenInfoService.updateById(bidderOpenInfo);
            System.out.println("对比文件是否该项目：" + (bidCodeFlag && claCodeFlag));
            return bidCodeFlag && claCodeFlag;
        }
    }

    /**
     * 修改redis 数字的值
     *
     * @param redisKey redis键
     * @param addNum   增加或减少的数字
     */
    @RedissonLock(key = "#redisKey")
    private void updateDecryptRedisNum(String redisKey, Integer addNum) {
        Integer redisNum = (Integer) RedisUtil.get(redisKey);
        if (redisNum == null || redisNum < 0) {
            redisNum = 0;
            RedisUtil.set(redisKey, redisNum);
        }
        Integer newRedisNum = redisNum + addNum;
        RedisUtil.set(redisKey, newRedisNum < 0 ? 0 : newRedisNum);
    }

    /**
     * 筛选解密成功的投标人数
     *
     * @param bidSectionId 标段名称
     * @param list         筛选的投标人
     * @return
     */
    private Integer getDecryptSuccessNum(Integer bidSectionId, List<Bidder> list) {
        List<Integer> bidderIdsList = new ArrayList<>();
        if (list.size() == 0) {
            return null;
        }
        for (Bidder bidder : list) {
            bidderIdsList.add(bidder.getId());
        }
        return bidderOpenInfoService.coutListBidderOpenInfo(bidSectionId, bidderIdsList, 1);
    }

    /**
     * 通过条件查询投标人信息
     *
     * @param bidder 查询条件
     * @param isPage 是否分页
     * @return
     */
    private List<Bidder> listBidderByCondition(Bidder bidder, Boolean isPage) {
        QueryWrapper<Bidder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(bidder.getBidSectionId() != null, "BID_SECTION_ID", bidder.getBidSectionId());
        queryWrapper.eq(bidder.getBidderName() != null, "BIDDER_NAME", bidder.getBidderName());
        queryWrapper.eq(bidder.getBidderOrgCode() != null, "BIDDER_ORG_CODE", bidder.getBidderOrgCode());
        queryWrapper.eq(bidder.getIsPassBidOpen() != null, "IS_PASS_BID_OPEN", bidder.getIsPassBidOpen());
        queryWrapper.eq(bidder.getDeleteFlag() != null, "DELETE_FLAG", bidder.getDeleteFlag());
        // 获取分页对象
        if (isPage) {
            Page page = this.getPageForLayUI();
            return bidderMapper.selectPage(page, queryWrapper).getRecords();
        }

        return bidderMapper.selectList(queryWrapper);
    }

    @Override
    public List<Bidder> listBidderByName(String bidderName, Integer bidSectionId) {
        QueryWrapper<Bidder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(bidderName), "BIDDER_NAME", bidderName);
        queryWrapper.eq(!CommonUtil.isEmpty(bidSectionId), "BID_SECTION_ID", bidSectionId);
        queryWrapper.eq("DELETE_FLAG", 0);
        return bidderMapper.selectList(queryWrapper);
    }


    @Override
    public List<Bidder> listSiteBiddersForSing(Integer bidSectionId) {
        List<Bidder> bidders = bidderMapper.listSiteBiddersForSing(bidSectionId);
        for (Bidder bidder : bidders) {
            BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSectionId);
            bidder.setBidderOpenInfo(boi);
            // 设置唱标内容
            bidder.setBidPrice(boi.getBidPrice());
            bidder.setMarginPay(boi.getMarginPay());
            bidder.setTimeLimit(boi.getTimeLimit());
            bidder.setQuality(boi.getQuality());
            bidder.setBidPriceType(boi.getBidPriceType());
        }
        return bidders;
    }

    @Override
    public Bidder getBidderForRelate(String bidderOrgCode, Integer preRelatedId) {
        Assert.notNull(bidderOrgCode, "param bidderOrgCode can not be null!");
        Assert.notNull(preRelatedId, "param preRelatedId can not be null!");
        QueryWrapper<Bidder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BIDDER_ORG_CODE", bidderOrgCode);
        queryWrapper.eq("BID_SECTION_ID", preRelatedId);
        queryWrapper.eq("DELETE_FLAG", 0);
        List<Bidder> bidders = bidderMapper.selectList(queryWrapper);
        if (!CommonUtil.isEmpty(bidders)) {
            return bidders.get(0);
        }
        return null;
    }

    @Override
    public Boolean paperDecrypt(Integer fileId, Integer bidderId, Integer bidSectionId, String fileType, String privateKey, String isOtherCa) {
        //获取待解密投标文件 redis标志
        String redisKey = "bidSection_" + bidSectionId + "_BidFileDecrypt_" + bidderId;
        Object bidFileDecrypt = RedisUtil.get(redisKey);
        //记录开始排队时间
        String queueStartTimeKey = "bidSection_" + bidSectionId + "_queueStartTime_" + bidderId;
        String queueStartTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        RedisUtil.set(queueStartTimeKey, queueStartTime);

        //当前排队人数
        String queueKey = "bidSection:" + bidSectionId;
        //解密中的人数
        String decryptingKey = "decrypting:" + bidSectionId;

        Bidder bidder = getBidderById(bidderId);
        BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidder.getBidSectionId());
        bidder.setBidderOpenInfo(boi);
        //修改投标文件解密开始时间
        BidderOpenInfo bidderOpenInfo = BidderOpenInfo.builder()
                .id(boi.getId())
                .decryptStartTime(queueStartTime)
                .build();
        bidderOpenInfoService.updateById(bidderOpenInfo);
        //获取投标文件信息
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        bidderOpenInfo.setBidderFileInfo(bidderFileInfo);
        if (bidFileDecrypt == null) {
            RedisUtil.set(redisKey, Status.NOT_START.getCode());
            //排队中的人数+1
            updateDecryptRedisNum(queueKey, 1);
            ThreadUtlis.run(() -> {
                try {
                    // 1. 设置开始解密
                    RedisUtil.set(redisKey, Status.PROCESSING.getCode());
                    //排队中的人数-1
                    updateDecryptRedisNum(queueKey, -1);
                    //解密中的人数+1
                    updateDecryptRedisNum(decryptingKey, 1);
                    // 2. 解密
                    if (FileType.GEFORTJY.getType().toString().equals(fileType)) {
                        yckDescrypt(fileId, bidder, bidderOpenInfo);
                    } else if (FileType.SGEFORETJY.getType().toString().equals(fileType)) {
                        if (Enabled.YES.getCode().toString().equals(isOtherCa)) {
                            otherSyckDescrypt(fileId, bidder, bidderOpenInfo);
                        } else {
                            syckDescrypt(fileId, bidder, bidderOpenInfo, privateKey);
                        }
                    }
                } catch (Exception e) {
                    //抛出异常时修改解密状态为失败
                    e.printStackTrace();
                    BidderOpenInfo newBidderOpenInfo = BidderOpenInfo.builder()
                            .id(boi.getId())
                            .decryptStatus(2)
                            .tenderDecryptStatus(2)
                            .build();
                    bidderOpenInfoService.updateById(newBidderOpenInfo);
                } finally {
                    // 3.解密结束 修改缓存
                    RedisUtil.delete(redisKey);
                    RedisUtil.delete(queueStartTimeKey);
                    //解密中的人数-1
                    updateDecryptRedisNum(decryptingKey, -1);
                    // 文件解析完成，删除本地缓存文件
//                    String customFilePath = FileUtil.getDecryptBidderFilePath(fileId.toString());
//                    FileUtil.removeDir(new File(customFilePath));
                    //上传时间
                    String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                    //上传区块链
                    ThreadUtlis.run(() -> {
                        try {
                            List<Bidder> bidder2 = getBidder(bidder.getBidderOrgCode(), bidder.getBidSectionId());
                            bsnChainInfoService.decryptBsnChainPut(fileId, bidder2.get(0), nowTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }
        return true;
    }

    @Override
    public List<Bidder> listFileUploadSuccessBidder(Integer bidSectionId) {
        // 获取所有未删除的投标人列表
        List<Bidder> noDelBidders = this.listBidderEnabled(bidSectionId, false);
        Iterator<Bidder> iterator = noDelBidders.listIterator();
        while (iterator.hasNext()) {
            Bidder noDelBidder = iterator.next();
            BidderOpenInfo bidderOpenInfo = bidderOpenInfoService.getBidderOpenInfo(noDelBidder.getId(), bidSectionId);
            if (!NotCheckinStatus.SUBMIT.equals(bidderOpenInfo.getNotCheckin())) {
                // 移除文件未上传成功的投标人
                iterator.remove();
            }
        }
        return noDelBidders;
    }

    @Override
    public List<Bidder> listOfflineDecrySuccessBidder(Integer bidSectionId) {
        return bidderMapper.listOfflineDecrySuccessBidder(bidSectionId);
    }

    /**
     * yck 文件解密
     *
     * @param fileId         文件id
     * @param bidder         投标人
     * @param bidderOpenInfo 修改的开标信息
     * @throws Exception
     */
    private void yckDescrypt(Integer fileId, Bidder bidder, BidderOpenInfo bidderOpenInfo) throws Exception {
        //投标文件解密路径
        String customFilePath = FileUtil.getDecryptBidderFilePath(fileId.toString());
        //解压路径
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        UploadFile gef = uploadFileService.getById(fileId);
        String localPath = FileUtil.getCustomFilePath() + gef.getPath();
        // 解压文件路径
        String outPath;
        if (!new File(localPath).exists()) {
            // fdfs路径
            String fdfsFilePath = fdfsService.getUrlByUpload(fileId);
            outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + FileType.GEFORTJY.getSuffix();
            // 文件下载
            byte[] bytes = fdfsService.downloadByUrl(fdfsFilePath);
            FileUtil.writeFile(bytes, outPath);
        } else {
            outPath = localPath;
        }
        // 解压
        boolean unzip = CompressUtil.unzip(outPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
        if (!unzip) {
            // 文件解析失败，删除本地缓存文件
            FileUtil.removeDir(new File(customFilePath));
            bidderOpenInfo.setDecryptStatus(2);
            bidderOpenInfo.setTenderDecryptStatus(2);
            bidderOpenInfoService.updateById(bidderOpenInfo);
        } else {
            //保存文件解密路径
            BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfo.getId())
                    .fileUnzipPath(unzipPath)
                    .build());

            //文件是否破损
            boolean validResult = true;
            List<File> files = FileUtil.listFileByFormat(unzipPath, FileType.PDF.getSuffix());
            for (File file : files) {
                if (!PDFUtil.validPdf(file.getAbsolutePath())) {
                    validResult = false;
                    bidderOpenInfo.setSealStatus(0);
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    break;
                }
            }
            //文件无破损
            if (validResult) {
                //判断招标解密是否解密成功
                boolean tenderDecryptFlag = parseBidFileInfo(unzipPath, bidder, true);
                if (tenderDecryptFlag) {
                    // 多线程上传文件到文件服务器
                    ThreadUtlis.run(() -> {
                        bidderFileUploadService.paperBidderFileUpload(bidder.getId(), fileId, new File(unzipPath));
                    });

                    //设置解密状态为成功
                    String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                    bidderOpenInfo.setDecryptStatus(1);
                    bidderOpenInfo.setTenderDecryptStatus(1);
                    bidderOpenInfo.setDecryptEndTime(nowTime);//修改解密状态
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    //修改默认投标文件类型
                    Bidder bidder1 = Bidder.builder()
                            .id(bidder.getId())
                            .bidDocId(fileId)
                            .bidDocType(FileType.GEFORTJY.getType())
                            .build();
                    bidderMapper.updateById(bidder1);
                } else {
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                }
            }
        }

    }

    /**
     * syck 文件解密（BJCA锁解密）
     *
     * @param fileId         文件id
     * @param bidder         投标人
     * @param bidderOpenInfo 待修改的开标信息
     * @param privateKey     加密因子
     * @throws Exception
     */
    private void syckDescrypt(Integer fileId, Bidder bidder, BidderOpenInfo bidderOpenInfo, String privateKey) throws Exception {
        //投标文件解密路径
        String customFilePath = FileUtil.getDecryptBidderFilePath(String.valueOf(fileId));
        //解压路径
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        UploadFile gef = uploadFileService.getById(fileId);
        String localPath = FileUtil.getCustomFilePath() + gef.getPath();
        // 解压文件路径
        String outPath;
        if (!new File(localPath).exists()) {
            //fdfs路径
            String fdfsFilePath = fdfsService.getUrlByUpload(fileId);
            //下载路径
            outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + FileType.SGEFORETJY.getSuffix();
            // 文件下载
            byte[] bytes = fdfsService.downloadByUrl(fdfsFilePath);
            FileUtil.writeFile(bytes, outPath);
        } else {
            outPath = localPath;
        }
        // sgef信封解密后的gef
        String cipherPath = customFilePath + File.separator + "decryptSGef." + FileType.GEFORTJY.getSuffix();
        // sgef信封解密
        CertTools.fileDecoder(new File(outPath), cipherPath, privateKey);
        //解压
        boolean unzip = CompressUtil.unzip(cipherPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
        if (!unzip) {
            // 文件解析失败，删除本地缓存文件
            FileUtil.removeDir(new File(customFilePath));
            bidderOpenInfo.setDecryptStatus(2);
            bidderOpenInfo.setTenderDecryptStatus(2);
            bidderOpenInfoService.updateById(bidderOpenInfo);
        } else {
            //保存文件解密路径
            BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfo.getId())
                    .fileUnzipPath(unzipPath)
                    .build());

            //文件是否破损
            boolean validResult = true;
            List<File> files = FileUtil.listFileByFormat(unzipPath, FileType.PDF.getSuffix());
            for (File file : files) {
                if (!PDFUtil.validPdf(file.getAbsolutePath())) {
                    validResult = false;
                    bidderOpenInfo.setDecryptStatus(2);
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    break;
                }
            }
            //文件无破损
            if (validResult) {
                //判断招标解密是否解密成功
                boolean tenderDecryptFlag = parseBidFileInfo(unzipPath, bidder, true);
                if (tenderDecryptFlag) {

                    // 多线程上传文件到文件服务器
                    ThreadUtlis.run(() -> {
                        bidderFileUploadService.paperBidderFileUpload(bidder.getId(), fileId, new File(unzipPath));
                    });

                    String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                    // 设置解密状态为成功
                    bidderOpenInfo.setDecryptStatus(1);
                    bidderOpenInfo.setTenderDecryptStatus(1);
                    bidderOpenInfo.setDecryptEndTime(nowTime);
                    // 修改解密状态
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    // 修改默认投标文件类型
                    Bidder bidder1 = Bidder.builder()
                            .id(bidder.getId())
                            .bidDocId(fileId)
                            .bidDocType(FileType.SGEFORETJY.getType())
                            .build();
                    bidderMapper.updateById(bidder1);
                } else {
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                }
            }
        }
    }

    /**
     * syck 文件解密(互认锁解密)
     *
     * @param fileId         文件id
     * @param bidder         投标人
     * @param bidderOpenInfo 待修改的开标信息
     * @throws Exception
     */
    private void otherSyckDescrypt(Integer fileId, Bidder bidder, BidderOpenInfo bidderOpenInfo) throws Exception {
        //投标文件解密路径
        String customFilePath = FileUtil.getDecryptBidderFilePath(String.valueOf(fileId));
        //解压路径
        String unzipPath = customFilePath + File.separator + "unzip";
        FileUtil.createDir(unzipPath);
        UploadFile gef = uploadFileService.getById(fileId);
        String localPath = FileUtil.getCustomFilePath() + gef.getPath();
        // 解压文件路径
        String outPath;
        if (!new File(localPath).exists()) {
            // fdfs路径
            String fdfsFilePath = fdfsService.getUrlByUpload(fileId);
            //下载路径
            outPath = customFilePath + File.separator + UUID.randomUUID().toString() + "." + FileType.SGEFORETJY.getSuffix();
            // 文件下载
            byte[] bytes = fdfsService.downloadByUrl(fdfsFilePath);
            FileUtil.writeFile(bytes, outPath);
        } else {
            outPath = localPath;
        }
        // sgef信封解密后的gef
        String cipherPath = customFilePath + File.separator + "decryptSGef." + FileType.GEFORTJY.getSuffix();
        DecoderCipherInfoDTO decoderCipherInfoDTO = CertTools.otherFileDecoder(new File(outPath), cipherPath);
        String fileType = null;
        if (decoderCipherInfoDTO != null) {
            fileType = decoderCipherInfoDTO.getFileType();
        }

        if (!Enabled.YES.getCode().toString().equals(fileType)) {
            setRedisMsg(bidder.getBidSectionId(), bidder.getId(), "解密的文件非互认文件！");
            throw new Exception("解密的文件非互认文件");
        }
        //解压
        boolean unzip = CompressUtil.unzip(cipherPath, unzipPath, DigestUtils.md5Hex(PassWordConstant.UNZIP_PASS_WORD).toUpperCase());
        if (!unzip) {
            // 文件解析失败，删除本地缓存文件
            FileUtil.removeDir(new File(customFilePath));
            bidderOpenInfo.setDecryptStatus(2);
            bidderOpenInfo.setTenderDecryptStatus(2);
            bidderOpenInfoService.updateById(bidderOpenInfo);
        } else {
            //保存文件解密路径
            BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidder.getId());
            bidderFileInfoService.updateById(BidderFileInfo.builder()
                    .id(bidderFileInfo.getId())
                    .fileUnzipPath(unzipPath)
                    .build());

            //文件是否破损
            boolean validResult = true;
            List<File> files = FileUtil.listFileByFormat(unzipPath, FileType.PDF.getSuffix());
            for (File file : files) {
                if (!PDFUtil.validPdf(file.getAbsolutePath())) {
                    validResult = false;
                    bidderOpenInfo.setDecryptStatus(2);
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    break;
                }
            }
            //文件无破损
            if (validResult) {
                //判断招标解密是否解密成功
                boolean tenderDecryptFlag = parseBidFileInfo(unzipPath, bidder, true);
                if (tenderDecryptFlag) {
                    // 多线程上传文件到文件服务器
                    ThreadUtlis.run(() -> {
                        bidderFileUploadService.paperBidderFileUpload(bidder.getId(), fileId, new File(unzipPath));
                    });

                    String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                    // 设置解密状态为成功
                    bidderOpenInfo.setDecryptStatus(1);
                    bidderOpenInfo.setTenderDecryptStatus(1);
                    bidderOpenInfo.setDecryptEndTime(nowTime);
                    // 修改解密状态
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                    // 修改默认投标文件类型
                    Bidder bidder1 = Bidder.builder()
                            .id(bidder.getId())
                            .bidDocId(fileId)
                            .bidDocType(FileType.SGEFORETJY.getType())
                            .build();
                    bidderMapper.updateById(bidder1);
                } else {
                    bidderOpenInfo.setTenderDecryptStatus(2);
                    bidderOpenInfoService.updateById(bidderOpenInfo);
                }
            }
        }
    }

    @Override
    public List<Bidder> listAllSimpleBidders(Integer bidSectionId, Boolean isPage) {
        List<Bidder> bidders;

        QueryWrapper<Bidder> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("DELETE_FLAG", 0);
        // 获取分页对象
        if (isPage) {
            Page<Bidder> page = this.getPageForLayUI();
            bidders = bidderMapper.selectPage(page, wrapper).getRecords();
        } else {
            bidders = bidderMapper.selectList(wrapper);
        }
        return bidders;
    }

    @Override
    public List<Bidder> listPassBidOpenSimpleBidder(Integer bidSectionId) {
        QueryWrapper<Bidder> wrapper = new QueryWrapper<>();
        wrapper.eq("DELETE_FLAG", 0);
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        wrapper.eq("IS_PASS_BID_OPEN", 1);

        return bidderMapper.selectList(wrapper);
    }

    @Override
    public Boolean phoneDecrypt(Integer fileId, Integer bidderId, Integer bidSectionId, String phoneCertNo) {
        // 获取待解密投标文件 redis标志
        String redisKey = "bidSection_" + bidSectionId + "_BidFileDecrypt_" + bidderId;
        Object bidFileDecrypt = RedisUtil.get(redisKey);
        //记录开始排队时间
        String queueStartTimeKey = "bidSection_" + bidSectionId + "_queueStartTime_" + bidderId;
        String queueStartTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        RedisUtil.set(queueStartTimeKey, queueStartTime);

        //当前排队人数
        String queueKey = "bidSection:" + bidSectionId;
        //解密中的人数
        String decryptingKey = "decrypting:" + bidSectionId;

        Bidder bidder = getBidderById(bidderId);
        BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidderId, bidder.getBidSectionId());
        bidder.setBidderOpenInfo(boi);
        //修改投标文件解密开始时间
        BidderOpenInfo bidderOpenInfo = BidderOpenInfo.builder()
                .id(boi.getId())
                .decryptStartTime(queueStartTime)
                .build();
        bidderOpenInfoService.updateById(bidderOpenInfo);
        //获取投标文件信息
        BidderFileInfo bidderFileInfo = bidderFileInfoService.getBidderFileInfoByBidderId(bidderId);
        bidderOpenInfo.setBidderFileInfo(bidderFileInfo);
        if (bidFileDecrypt == null) {
            RedisUtil.set(redisKey, Status.NOT_START.getCode());
            //排队中的人数+1
            updateDecryptRedisNum(queueKey, 1);
            ThreadUtlis.run(() -> {
                try {
                    // 1. 设置开始解密
                    RedisUtil.set(redisKey, Status.PROCESSING.getCode());
                    //排队中的人数-1
                    updateDecryptRedisNum(queueKey, -1);
                    //解密中的人数+1
                    updateDecryptRedisNum(decryptingKey, 1);
                    // 2. 解密
                    phoneSgefDecrypt(fileId, bidder, bidderOpenInfo, phoneCertNo);
                } catch (Exception e) {
                    //抛出异常时修改解密状态为失败
                    e.printStackTrace();
                    BidderOpenInfo newBidderOpenInfo = BidderOpenInfo.builder()
                            .id(boi.getId())
                            .decryptStatus(2)
                            .tenderDecryptStatus(2)
                            .build();
                    bidderOpenInfoService.updateById(newBidderOpenInfo);
                } finally {
                    // 3.解密结束 修改缓存
                    RedisUtil.delete(redisKey);
                    RedisUtil.delete(queueStartTimeKey);
                    //解密中的人数-1
                    updateDecryptRedisNum(decryptingKey, -1);
                    // 文件解析完成，删除本地缓存文件
//                    String customFilePath = FileUtil.getDecryptBidderFilePath(fileId.toString());
//                    FileUtil.removeDir(new File(customFilePath));
                    //上传时间
                    String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
                    //上传区块链
                    ThreadUtlis.run(() -> {
                        try {
                            List<Bidder> bidder2 = getBidder(bidder.getBidderOrgCode(), bidder.getBidSectionId());
                            bsnChainInfoService.decryptBsnChainPut(fileId, bidder2.get(0), nowTime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }
        return true;
    }

    @Override
    public String pageBidderBySectionId(Integer bidSectionId) {
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
        String bidOpenTime = tenderDoc.getBidOpenTime();
        String nowTime = DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        long timeDiff = DateTimeUtil.getTimeDiff(nowTime, bidOpenTime, TimeUnit.SECONDS, TimeFormatter.YYYY_HH_DD_HH_MM_SS);
        List<Bidder> list = null;
        Page page = this.getPageForLayUI();
        if (timeDiff < 30 * 60) {
            QueryWrapper<Bidder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("DELETE_FLAG", 0);
            queryWrapper.eq("BID_SECTION_ID", bidSectionId);
            queryWrapper.eq("IS_PASS_BID_OPEN", 1);
            list = bidderMapper.selectPage(page, queryWrapper).getRecords();
            for (Bidder bidder : list) {
                BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfo(bidder.getId(), bidSectionId);
                if (boi != null) {
                    bidder.setBidderOpenInfo(boi);
                }
            }
        }

        return this.initJsonForLayUI(list, (int) page.getTotal());
    }

    @Override
    public Bidder getClearV3Bidder(Integer bidSectionId, Integer id) {
        QueryWrapper<Bidder> query = new QueryWrapper<>();
        query.eq("BID_SECTION_ID", bidSectionId);
        query.eq("ID", id);
        return bidderMapper.selectOne(query);
    }

    /**
     * 设置redis缓存错误消息
     *
     * @param bidSectionId 标段id
     * @param bidderId     投标人id
     * @param errorMsg     错误信息
     */
    private void setRedisMsg(Integer bidSectionId, Integer bidderId, String errorMsg) {
        String decryptMsg = "BID_SECTION_" + bidSectionId + "_DECRYPT_MSG:" + bidderId;
        RedisUtil.set(decryptMsg, errorMsg);
    }
}
