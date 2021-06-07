package com.ejiaoyi.common.service;

import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.ejiaoyi.common.entity.BsnChainInfo;

import java.util.List;

/**
 * @Desc:
 * @author: yyb
 * @date: 2020-8-17 17:05
 */
public interface IBsnChainInfoService {
    /**
     * 添加区块链信息
     * @param bsnChainInfo 信息
     * @return
     */
    Integer addBsnChainInfo(BsnChainInfo bsnChainInfo);

    /**
     * 获取所有区块信息的地址
     * @param bidderId 投标人id
     * @param type 类型
     * @return
     */
    List<BsnChainInfo> getAllBsnChainInfo(Integer bidderId, Integer type);

    /**
     * 获取最新的一条区块信息的地址
     * @param bidderId 投标人id
     * @param type 类型
     * @return  最新的一条区块信息
     */
    BsnChainInfo getLastBsnChainInfo(Integer bidderId,Integer type);

    /**
     *
     * @param bid_dep_cert_hash_code 存证文件hash
     * @param bid_doc_hash_code 投标文件hash
     * @param bid_doc_suffix 投标文件后缀
     * @param bidderId 投标人id
     * @Author liuguoqiang
     * @return
     */
    String addBidDocumentsWitness(String bid_dep_cert_hash_code, String bid_doc_hash_code, String bid_doc_suffix, Integer bidderId);


    /**
     * 投标文件解密区块信息推送，确认开标一览表
     *
     * @param fileId 文件id
     * @param bidder 投标人
     * @param decryptTime 解密时间
     * @return
     * @throws Exception
     */
    String decryptBsnChainPut(Integer fileId, Bidder bidder, String decryptTime) throws Exception;


    /**
     * 开标记录表区块信息推送
     * @param bidSectionId 标段id
     * @return
     */
    void tableBsnChainPut(Integer bidSectionId);

    /**
     * 授权委托书上传
     * @param bidderOpenInfo （投标人ID，授权委托书ID）
     * @return 区块链地址
     */
    String bidderAttorney(BidderOpenInfo bidderOpenInfo);

    /**
     * 区块链 推送消息记录 数据
     * @param bidSectionId 标段ID
     * @param fileHash 文件HASH
     */
    void msgExportBsnChainPut(Integer bidSectionId, String fileHash);

    /**
     * 区块链  评标报告上传
     * @param bidSectionId 标段ID
     * @param fileHash 文件HASH
     */
    void bidEvalReportBsnChainPut(Integer bidSectionId, String fileHash);
}
