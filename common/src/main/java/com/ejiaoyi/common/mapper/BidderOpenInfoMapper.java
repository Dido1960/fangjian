package com.ejiaoyi.common.mapper;

import com.ejiaoyi.common.entity.BidderOpenInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* 投标人开标信息 Mapper 接口
*
* @author Z0001
* @since 2020-07-03
*/
@Component
public interface BidderOpenInfoMapper extends BaseMapper<BidderOpenInfo> {

    /**
     * 获取投标人列表
     * @param bid 标段主键
     * @param isQualification 是否是资格预审
     * @return
     */
    List<BidderOpenInfo> listBidderOpenInfo(@Param("bid") Integer bid, @Param("isQualification")boolean isQualification);


    /**
     * 查询某个解密状态的投标人个数
     * @param bidSectionId 标段id
     * @param decryptStatus 解密状态
     * @return
     */
    @Select("SELECT COUNT(*) from bidder_open_info " +
            "WHERE BID_SECTION_ID = #{bidSectionId} " +
            "AND DECRYPT_STATUS = #{decryptStatus}")
    Integer selectDecryptStatusCount(@Param("bidSectionId") Integer bidSectionId, @Param("decryptStatus") Integer decryptStatus);
}
