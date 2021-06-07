package com.ejiaoyi.common.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.constant.BlockchainConstant;
import com.ejiaoyi.common.constant.BlockchainType;
import com.ejiaoyi.common.constant.FileState;
import com.ejiaoyi.common.constant.ProjectFileTypeConstant;
import com.ejiaoyi.common.dto.BidEvalReportDTO;
import com.ejiaoyi.common.dto.BidMsgExportDTO;
import com.ejiaoyi.common.dto.BsnChainDTO;
import com.ejiaoyi.common.entity.*;
import com.ejiaoyi.common.enums.FileType;
import com.ejiaoyi.common.mapper.BsnChainInfoMapper;
import com.ejiaoyi.common.service.*;
import com.ejiaoyi.common.util.BlockchainUtil;
import com.ejiaoyi.common.util.CommonUtil;
import com.ejiaoyi.common.util.FileUtil;
import com.ejiaoyi.common.util.ThreadUtlis;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 区块信息表 服务实现类
 * </p>
 *
 * @author samzqr
 * @since 2020-08-17
 */
@Service
public class BsnChainInfoServiceImpl extends BaseServiceImpl implements IBsnChainInfoService {

    @Autowired
    private BsnChainInfoMapper bsnChainInfoMapper;

    @Autowired
    private IBidSectionService bidSectionService;

    @Autowired
    private ITenderDocService tenderDocService;

    @Autowired
    private IFDFSService fdfsService;

    @Autowired
    private ILineMsgService lineMsgService;

    @Autowired
    private IBidderService bidderService;

    @Autowired
    private IBidderFileInfoService bidderFileInfoService;

    @Autowired
    private IBidderOpenInfoService bidderOpenInfoService;

    @Autowired
    private ITenderProjectService tenderProjectService;

    @Override
    public Integer addBsnChainInfo(BsnChainInfo bsnChainInfo) {
        return bsnChainInfoMapper.insert(bsnChainInfo);
    }

    @Override
    public List<BsnChainInfo> getAllBsnChainInfo(Integer bidderId, Integer type) {
        QueryWrapper<BsnChainInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("BIDDER_ID", bidderId);
        queryWrapper.eq("TYPE", type);
        return bsnChainInfoMapper.selectList(queryWrapper);
    }

    @Override
    public BsnChainInfo getLastBsnChainInfo(Integer bidderId, Integer type) {
        return bsnChainInfoMapper.getLastBsnChainInfo(bidderId, type);
    }

    @Override
    public String addBidDocumentsWitness(String bid_dep_cert_hash_code, String bid_doc_hash_code, String bid_doc_suffix, Integer bidderId) {
        String token = BlockchainUtil.getToken();
        //请求参数封装
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("platform", BlockchainConstant.PLAT_FORM);
        map.put("api_key", BlockchainConstant.API_KEY);
        map.put("token", token);

        Bidder bidder = bidderService.getBidderById(bidderId);
        BidSection bidSection = bidSectionService.getBidSectionById(bidder.getBidSectionId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidder.getBidSectionId());

        map.put("bid_dep_cert_hash_code", bid_dep_cert_hash_code);
        map.put("bid_doc_hash_code", bid_doc_hash_code);
        map.put("bid_doc_suffix", bid_doc_suffix);
        map.put("bidder_credit_code", bidder.getBidderOrgCode());
        map.put("bidder_name", bidder.getBidderName());
        map.put("no_name", bidSection.getBidSectionName());
        map.put("no_num", bidSection.getBidSectionCode());
        map.put("open_bid_time", tenderDoc.getBidOpenTime());
        map.put("the_bid_time", DateUtil.formatLocalDateTime(LocalDateTime.now()));
        String json = JSONUtils.toJSONString(map);

        String data = BlockchainUtil.doThisMethod(BlockchainConstant.BID_DOCUMENTS_WITNESS, json);
        JSONObject parse = JSONObject.parseObject(data);
        JSONObject blockInfo = parse.getJSONObject("blockInfo");
        //存入数据库
        BsnChainInfo bsnChainInfo = BsnChainInfo.builder().bidderId(bidder.getId())
                .type(BlockchainType.BID_RELATE_CONTENT)
                .txId(blockInfo.getString("txId"))
                .status(blockInfo.getString("status"))
                .baseKey(parse.getString("baseKey"))
                .queryAddress(parse.getString("queryAddress"))
                .bidSectionId(bidder.getBidSectionId()).build();
        this.addBsnChainInfo(bsnChainInfo);
        return parse.getString("queryAddress");
    }


    @Override
    public String decryptBsnChainPut(Integer fileId, Bidder bidder, String decryptTime) throws Exception {
        String queryAddress = null;
        boolean addBsnFlag = false;
        BidSection bidSection = bidSectionService.getBidSectionById(bidder.getBidSectionId());
        BidderOpenInfo boi = bidder.getBidderOpenInfo();
        BidderFileInfo bfi = bidderFileInfoService.getBidderFileInfoByBidderId(boi.getBidderId());

        String czrHashCode = bfi.getCzrHash();
        String sureBidResult = lineMsgService.sureBidResult(bidder.getBidSectionId(), bidder.getId());
        String docDetermineTime = CommonUtil.isEmpty(boi.getDocDetermineTime()) ? "/" : boi.getDocDetermineTime();

        String bidDocHash;
        if (fileId.equals(bfi.getGefId())) {
            bidDocHash = bfi.getGefHash();
        } else {
            bidDocHash = bfi.getSgefHash();
        }
        //开标一览表数据
        Map<String, String> openBidRecord = new HashMap<>();
        openBidRecord.put("投标人", bidder.getBidderName());
        openBidRecord.put("投标保证金", boi.getMarginPay());
        openBidRecord.put("法人或委托人", boi.getClientName());
        String bidDocType = null;
        if (CommonUtil.isEmpty(boi.getSigninTime()) || (boi.getNotCheckin() != null && boi.getNotCheckin() != 3)) {
            openBidRecord.put("签到状态", "未签到");
            openBidRecord.put("未签到原因", boi.getNotCheckinReason());
        }else {
            openBidRecord.put("签到状态", "已签到");
            openBidRecord.put("签到时间", boi.getSigninTime());
        }

        if (CommonUtil.isEmpty(bidder.getBidDocId())) {
            openBidRecord.put("投标文件", "未上传");
        } else {
            if (bidder.getBidDocId() != null){
                if ("1".equals(bidSection.getPaperEval())) {
                    if (bidder.getBidDocType() == 1){
                        bidDocType = "syck";
                    }else {
                        bidDocType = "yck";
                    }
                } else {
                    if (bidder.getBidDocType() == 1){
                        bidDocType = FileType.SGEFORETJY.getSuffix();
                    }else {
                        bidDocType = FileType.GEFORTJY.getSuffix();
                    }
                }
            }
            Fdfs fdfs = fdfsService.getFdfsByUpload(bidder.getBidDocId());
            openBidRecord.put("投标文件", fdfs.getName());
        }
        if (boi.getDecryptStatus() != 1) {
            openBidRecord.put("文件解密", "失败");
        } else {
            openBidRecord.put("文件解密", "成功");
            openBidRecord.put("投标报价", bidder.getBidderOpenInfo().getBidPrice());
            openBidRecord.put("投标工期", bidder.getBidderOpenInfo().getTimeLimit());
        }
        Map<String, String> datas = new HashMap<>();
        datas.put("bid_dep_cert_hash_code", czrHashCode);
        datas.put("bid_doc_hash_code", bidDocHash);
        datas.put("bid_doc_suffix", "." + bidDocType);
        datas.put("bid_opening_list_data", JSONUtils.toJSONString(openBidRecord));
        datas.put("bidder_credit_code", bidder.getBidderOrgCode());
        datas.put("bidder_name", bidder.getBidderName());
        datas.put("confirm_the_time", docDetermineTime);
        datas.put("decrypt_time", decryptTime);
        datas.put("no_name", bidSection.getBidSectionName());
        datas.put("no_num", bidSection.getBidSectionCode());
        datas.put("start_time", boi.getDecryptStartTime());
        datas.put("the_bid_time", boi.getUpfileTime());
        //是否确认开标结果
        datas.put("whether_results_bid_opening", sureBidResult);
        datas.put("api_key", BlockchainConstant.API_KEY);
        datas.put("platform", BlockchainConstant.PLAT_FORM);
        datas.put("token", BlockchainUtil.getToken());
        //替换值中的空字符
        for (Map.Entry<String, String> data : datas.entrySet()) {
            if (CommonUtil.isEmpty(data.getValue())) {
                data.setValue("/");
            }
        }

        String jsonData = JSONUtils.toJSONString(datas);
        String resultJsonStr = BlockchainUtil.doThisMethod(BlockchainConstant.BIDDER_DECLASSIFIED_RECORDS, jsonData);

        //数据解析
        JSONObject dataJsonObject = JSONObject.parseObject(resultJsonStr);
        if (dataJsonObject != null) {
            JSONObject blockJsonObject = JSONObject.parseObject(dataJsonObject.getString("blockInfo"));
            queryAddress = dataJsonObject.getString("queryAddress");
            BsnChainInfo bsnChainInfo = BsnChainInfo.builder()
                    .bidderId(bidder.getId())
                    .bidSectionId(bidder.getBidSectionId())
                    .type(BlockchainType.BIDDER_DECLASSIFIED_RECORDS)
                    .txId(blockJsonObject.getString("txId"))
                    .status(blockJsonObject.getString("status"))
                    .baseKey(dataJsonObject.getString("baseKey"))
                    .queryAddress(queryAddress)
                    .build();

            addBsnFlag = this.addBsnChainInfo(bsnChainInfo) > 0;
        }

        if (!addBsnFlag) {
            return null;
        }
        return queryAddress;
    }


    @Override
    public void tableBsnChainPut(Integer bidSectionId) {
        ThreadUtlis.run(() -> {
            BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
            TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);
            List<Bidder> bidders = bidderService.getBidder(null, bidSectionId);
            Iterator<Bidder> iterator = bidders.iterator();
            while (iterator.hasNext()) {
                Bidder bidder = iterator.next();
                Integer decryptStatus = bidder.getBidderOpenInfo().getDecryptStatus();
                Integer tenderRejection = bidder.getBidderOpenInfo().getTenderRejection();
                //排除没有成功解密和被标书拒绝的投标人
                if (!FileState.SUCCESS.equals(decryptStatus) || (tenderRejection != null && tenderRejection == 1)) {
                    iterator.remove();
                }
            }
            for (Bidder bidder : bidders) {
                //开标记录表mark
                String mark = File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + File.separator + bidSectionId +
                        File.separator + ProjectFileTypeConstant.BID_OPEN_RECORD + "." + FileType.PDF.getSuffix();
                Fdfs bidOpenRecordFile = fdfsService.downloadByMark(mark);
                String bidOpenRecordHashCode = FileUtil.getFileMD5(bidOpenRecordFile.getBytes());
                Map<String, String> datas = new HashMap<>();
                datas.put("bid_open_record_hash_code", bidOpenRecordHashCode);
                datas.put("bidder_credit_code", bidder.getBidderOrgCode());
                datas.put("bidder_name", bidder.getBidderName());
                datas.put("no_name", bidSection.getBidSectionName());
                datas.put("no_num", bidSection.getBidSectionCode());
                datas.put("open_bid_end_time", bidSection.getBidOpenEndTime());
                datas.put("open_bid_start_time", tenderDoc.getBidOpenTime());
                datas.put("open_bid_time", tenderDoc.getBidOpenTime());
                datas.put("platform", BlockchainConstant.PLAT_FORM);
                datas.put("api_key", BlockchainConstant.API_KEY);
                datas.put("token", BlockchainUtil.getToken());
                //替换值中的空字符
                for (Map.Entry<String, String> data : datas.entrySet()) {
                    if (CommonUtil.isEmpty(data.getValue())) {
                        data.setValue("/");
                    }
                }

                String jsonData = JSONUtils.toJSONString(datas);
                String resultJsonStr = BlockchainUtil.doThisMethod(BlockchainConstant.BID_OPENING_RECORD, jsonData);

                //数据解析
                JSONObject dataJsonObject = JSONObject.parseObject(resultJsonStr);
                if (!CommonUtil.isEmpty(dataJsonObject)) {
                    JSONObject blockJsonObject = JSONObject.parseObject(dataJsonObject.getString("blockInfo"));
                    String queryAddress = dataJsonObject.getString("queryAddress");
                    BsnChainInfo bsnChainInfo = BsnChainInfo.builder()
                            .bidderId(bidder.getId())
                            .bidSectionId(bidSectionId)
                            .type(BlockchainType.BID_OPENING_RECORD)
                            .txId(blockJsonObject.getString("txId"))
                            .status(blockJsonObject.getString("status"))
                            .baseKey(dataJsonObject.getString("baseKey"))
                            .queryAddress(queryAddress)
                            .build();
                    this.addBsnChainInfo(bsnChainInfo);
                }

            }
        });
    }

    @Override
    public String bidderAttorney(BidderOpenInfo bidderOpenInfo) {
        Assert.notNull(bidderOpenInfo.getId(), "param id can not be null !");
        Assert.notNull(bidderOpenInfo.getSqwtsFileId(), "param sqwtsFileId can not be null !");

        String token = BlockchainUtil.getToken();
        //请求参数封装
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("platform", BlockchainConstant.PLAT_FORM);
        map.put("api_key", BlockchainConstant.API_KEY);
        map.put("token", token);

        BidderOpenInfo boi = bidderOpenInfoService.getBidderOpenInfoById(bidderOpenInfo.getId());
        Bidder bidder = bidderService.getBidderById(boi.getBidderId());
        BidSection bidSection = bidSectionService.getBidSectionById(bidder.getBidSectionId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidder.getBidSectionId());
        Fdfs file = fdfsService.getFdfsByUpload(bidderOpenInfo.getSqwtsFileId());

        map.put("attorney_hash_code", file.getFileHash());
        map.put("bidder_credit_code", bidder.getBidderOrgCode());
        map.put("bidder_name", bidder.getBidderName());
        map.put("no_name", bidSection.getBidSectionName());
        map.put("no_num", bidSection.getBidSectionCode());
        map.put("open_bid_time", tenderDoc.getBidOpenTime());
        map.put("the_upload_time", file.getInsertTime());
        String json = JSONUtils.toJSONString(map);

        String data = BlockchainUtil.doThisMethod(BlockchainConstant.BIDDER_ATTORNEY, json);
        JSONObject parse = JSONObject.parseObject(data);
        JSONObject blockInfo = parse.getJSONObject("blockInfo");
        //存入数据库
        BsnChainInfo bsnChainInfo = BsnChainInfo.builder().bidderId(bidder.getId())
                .type(BlockchainType.BIDDER_ATTORNEY)
                .txId(blockInfo.getString("txId"))
                .status(blockInfo.getString("status"))
                .baseKey(parse.getString("baseKey"))
                .queryAddress(parse.getString("queryAddress"))
                .bidSectionId(bidder.getBidSectionId()).build();
        this.addBsnChainInfo(bsnChainInfo);
        return parse.getString("queryAddress");
    }

    @Override
    public void msgExportBsnChainPut(Integer bidSectionId, String fileHash) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);

        String token = BlockchainUtil.getToken();

        BidMsgExportDTO data = BidMsgExportDTO.builder()
                .noName(bidSection.getBidSectionName())
                .noNum(bidSection.getBidSectionCode())
                .openBidTime(tenderDoc.getBidOpenTime())
                .agencyName(tenderProject.getTenderAgencyName())
                .bidMsgExportHashCode(fileHash)
                .build();
        //请求参数封装
        data.setPlatform(BlockchainConstant.PLAT_FORM);
        data.setApi_key(BlockchainConstant.API_KEY);
        data.setToken(token);

        String json = JSONObject.toJSONString(data);
//        String json = JSONUtils.toJSONString(data);

        String resultData = BlockchainUtil.doThisMethod(BlockchainConstant.BID_MSG_EXPORT_INFO, json);

        JSONObject parse = JSONObject.parseObject(resultData);
        JSONObject blockInfo = parse.getJSONObject("blockInfo");
        //存入数据库
        BsnChainInfo bsnChainInfo = BsnChainInfo.builder()
                .type(BlockchainType.BID_MSG_EXPORT)
                .txId(blockInfo.getString("txId"))
                .status(blockInfo.getString("status"))
                .baseKey(parse.getString("baseKey"))
                .queryAddress(parse.getString("queryAddress"))
                .bidSectionId(bidSectionId).build();
        this.addBsnChainInfo(bsnChainInfo);
    }

    @Override
    public void bidEvalReportBsnChainPut(Integer bidSectionId, String fileHash) {
        BidSection bidSection = bidSectionService.getBidSectionById(bidSectionId);
        TenderProject tenderProject = tenderProjectService.getTenderProjectById(bidSection.getTenderProjectId());
        TenderDoc tenderDoc = tenderDocService.getTenderDocBySectionId(bidSectionId);

        String token = BlockchainUtil.getToken();

        BidEvalReportDTO data = BidEvalReportDTO.builder()
                .noName(bidSection.getBidSectionName())
                .noNum(bidSection.getBidSectionCode())
                .openBidTime(tenderDoc.getBidOpenTime())
                .bidEvalStartTime(bidSection.getEvalStartTime())
                .agencyName(tenderProject.getTenderAgencyName())
                .bidEvalReportHashCode(fileHash)
                .build();
        //请求参数封装
        data.setPlatform(BlockchainConstant.PLAT_FORM);
        data.setApi_key(BlockchainConstant.API_KEY);
        data.setToken(token);

        String json = JSONObject.toJSONString(data);

        String resultData = BlockchainUtil.doThisMethod(BlockchainConstant.BID_EVAL_REPORT_INFO, json);

        JSONObject parse = JSONObject.parseObject(resultData);
        JSONObject blockInfo = parse.getJSONObject("blockInfo");
        //存入数据库
        BsnChainInfo bsnChainInfo = BsnChainInfo.builder()
                .type(BlockchainType.BID_EVAL_REPORT)
                .txId(blockInfo.getString("txId"))
                .status(blockInfo.getString("status"))
                .baseKey(parse.getString("baseKey"))
                .queryAddress(parse.getString("queryAddress"))
                .bidSectionId(bidSectionId).build();
        this.addBsnChainInfo(bsnChainInfo);
    }

}
