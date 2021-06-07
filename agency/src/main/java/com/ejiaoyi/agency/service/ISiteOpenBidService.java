package com.ejiaoyi.agency.service;

import com.ejiaoyi.agency.dto.SiteBidDecryptDto;
import com.ejiaoyi.common.dto.DecoderCipherInfoDTO;
import com.ejiaoyi.common.entity.Bidder;

import java.util.List;

/**
 * 现场开标服务类
 *
 * @author Make
 * @since 2021-01-07
 */
public interface ISiteOpenBidService {

    /**
     * 现场标投标文件解密GEF
     *
     * @param fileId       文件id
     * @param bidderId     投标人id
     * @param bidSectionId 标段id
     * @throws Exception
     * @return
     */
    SiteBidDecryptDto siteDecryptGef(Integer fileId, Integer bidderId, Integer bidSectionId) throws Exception;

    /**
     * 获取拆信封后结构信息
     * @param fileId 文件id
     * @return
     * @throws Exception
     */
    DecoderCipherInfoDTO getDecoderCipherInfo(Integer fileId) throws Exception;

    /**
     * 现场标投标文件解密SGEF
     *
     * @param fileId       文件id
     * @param bidderId     投标人id
     * @param bidSectionId 标段id
     * @param privateKey 私钥
     * @param isOtherCa 是否互认
     * @throws Exception
     * @return
     */
    SiteBidDecryptDto siteDecryptSgef(Integer fileId, Integer bidderId, Integer bidSectionId, String privateKey, String isOtherCa) throws Exception;

    /**
     * 解密投标文件
     *
     * @param bidSectionId 标段主键
     * @param bidderFileId 投标文件ID
     * @throws Exception
     */
    void parseBidderProject(Integer bidSectionId, Integer bidderFileId) throws Exception;

    /**
     * 通过id获取当前标段的开标流程的完成情况
     *
     * @param id 标段id
     * @return
     */
    List<String> listBidOpenProcessComplete(Integer id);
}
