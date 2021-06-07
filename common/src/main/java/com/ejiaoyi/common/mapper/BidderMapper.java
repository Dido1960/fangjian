package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.dto.statistical.BidderListDTO;
import com.ejiaoyi.common.dto.statistical.ConfirmBidOpenRecordDTO;
import com.ejiaoyi.common.entity.BidSection;
import com.ejiaoyi.common.entity.Bidder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 投标人信息 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface BidderMapper extends BaseMapper<Bidder> {

    /**
     * 通过标段id获取解密成功，且未被标书拒绝的投标人
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listDecrySuccessBidder( @Param("bidSectionId") Integer bidSectionId);

    /**
     * 通过标段id获取解密失败，未签到和被标书拒绝的投标人
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listFailBidder( @Param("bidSectionId") Integer bidSectionId);

    /**
     * 通过标段id获取解密成功，且未被标书拒绝的投标人
     * @param bidSectionId 标段id
     * @param page 分页参数
     * @return
     */
    List<Bidder> listDecrySuccessBidder(@Param("page")Page page, @Param("bidSectionId") Integer bidSectionId);

    /**
     * 现场标，所有解密成功的投标人
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listOfflineDecrySuccessBidder(@Param("bidSectionId") Integer bidSectionId);

    /**
     * 查询唱标的可参与投标人列表
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listBiddersForSing(@Param("bidSectionId") Integer bidSectionId);

    /**
     * 通过标段id获取开标合格投标人列表
     *
     * @param bidSectionId 标段id
     * @param gradeIds     初步评审评分标准
     * @return
     */
    List<Bidder> listDetailedBidder(@Param("bidSectionId") Integer bidSectionId, @Param("gradeIds") String[] gradeIds);

    /**
     * 获取未通过初步评审的投标人
     *
     * @param bidSectionId 标段id
     * @param gradeIds     初步评审评分标准
     * @return
     */
    List<Bidder> listNoPassFirstStepBidder(@Param("bidSectionId") Integer bidSectionId, @Param("gradeIds") String[] gradeIds);

    /**
     * 分页获取进入标书解密的投标人信息
     *
     * @param page        分页参数
     * @param bidSectionId 标段id
     * @param isQualification 是否是资格预审
     * @return
     */
    List<Bidder> listTenderDecryptBidders(@Param("page") Page page, @Param("bidSectionId") Integer bidSectionId, @Param("isQualification")Boolean isQualification);

    /**
     * 查询现场标唱标的可参与投标人列表
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<Bidder> listSiteBiddersForSing(@Param("bidSectionId") Integer bidSectionId);

    /**
     * 投标人名单-App接口
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<BidderListDTO> listBiddersForPhone(@Param("bidSectionId") Integer bidSectionId);

    /**
     * 开标一览表-App接口
     *
     * @param bidSectionId 标段id
     * @return
     */
    List<ConfirmBidOpenRecordDTO> confirmBidOpenRecordForPhone(@Param("bidSectionId") Integer bidSectionId);
}
