package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.BidderFileUploadDTO;
import com.ejiaoyi.common.dto.BidderReviewPointDTO;
import com.ejiaoyi.common.dto.DownBidderFileDTO;
import com.ejiaoyi.common.entity.Bidder;
import com.ejiaoyi.common.entity.BidderFileInfo;
import com.ejiaoyi.common.enums.BidProtype;

import java.util.List;

/**
 * <p>
 * 投标文件信息 服务类
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-09-30
 */
public interface IBidderFileInfoService {
    /**
     * 通过获取评审点
     * @param reviewPoint 地址
     * @param bidderId 投标人id
     * @param bidSectionId 标段id
     * @param downStatus 是否需要下载
     * @return
     */
    List<BidderReviewPointDTO> saveBidderReviewPoint(String reviewPoint, Integer bidderId, Integer bidSectionId, boolean downStatus);

    /**
     * 通过biddreId获取
     * @param bidderId bidderId
     * @return
     */
    BidderFileInfo getBidderFileInfoByBidderId(Integer bidderId);

    /**
     * 通过BidderId查找 如果没有则插入数据并返会查找后的实例;
     * @param bidderFileInfo 对象
     * @return
     */
    BidderFileInfo initBidderFileInfo(BidderFileInfo bidderFileInfo);

    /**
     * 通过ID更新
     * @param bidderFileInfo 对象
     * @return
     */
    Integer updateById(BidderFileInfo bidderFileInfo);

    /**
     * 获取投标人投标的相关文件
     * @param bidSectionId 标段id
     * @return 投标人相关文件集合
     */
    List<DownBidderFileDTO> listDownBidderFileDTO(Integer bidSectionId);


    /**
     * 获取(资格预审)全部投标人的Pdf文件
     * @param bidSectionId
     * @return
     */
    List<DownBidderFileDTO> listDownAllBiddersPdfFileDTO(Integer bidSectionId);


    /**
     * 获取(非资格预审-施工)得分排名第一名的投标人的Pdf文件
     * @param bidSectionId
     * @return
     */
    List<DownBidderFileDTO> listDownFirstBidderPdfFileDTO1(Integer bidSectionId);


    /**
     * 获取(非资格预审-监理)得分排名第一名的投标人的Pdf文件
     * @param bidSectionId
     * @return
     */
    List<DownBidderFileDTO> listDownFirstBidderPdfFileDTO2(Integer bidSectionId);


    /**
     * 获取(非资格预审-总承包)得分排名第一名的投标人的Pdf文件
     * @param bidSectionId
     * @return
     */
    List<DownBidderFileDTO> listDownFirstBidderPdfFileDTO3(Integer bidSectionId);


    /**
     * 获取(非资格预审-其他的)排名为第一名投标人的pdf文件
     * @param bidSectionId
     * @return
     */
    List<DownBidderFileDTO> listDownFirstBidderPdfFileDTO4(Integer bidSectionId);

    /**
     * 通过投标清单uuid获取投标人相关信息
     * @param xmlUid 标清单uuid
     * @return
     */
    Bidder getBidderByXmlUid(String xmlUid);

    /**
     * 获取 投标文件 上传数据
     * @param bidSectionId 标段Id
     * @return 上传数据
     */
    BidderFileUploadDTO listBidderFileUpload(Integer bidSectionId);

    /**
     * 通过ID 查询
     * @param id
     * @return
     */
    BidderFileInfo getById(Integer id);

    /**
     * 获取所有未解析完成的工程量清单信息
     * @return
     */
    List<BidderFileInfo> listXmlNoParseCompleteInfo();


}
