package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.DownBidderFileDTO;

import java.util.List;

/**
 * 文件查询 接口
 *
 * @author Make
 * @since 2020-11-16
 */
public interface IQueryFileService {

    /**
     * 获取投标人投标的相关文件
     * @param bidSectionId 标段id
     * @return 投标人相关文件集合
     */
    List<DownBidderFileDTO> listDownBidderFileDTO(Integer bidSectionId);

    /**
     * 获取投标人投标的相关文件
     * @param bidSectionId 标段id
     * @param bidderId 投标人id
     * @return 投标人相关文件集合
     */
   DownBidderFileDTO getBidderPdfFileByBidderId(Integer bidSectionId,Integer bidderId);
}
