package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.BidderFileInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 投标文件信息 Mapper 接口
 * </p>
 *
 * @author liuguoqiang
 * @since 2020-09-30
 */
@Component
public interface BidderFileInfoMapper extends BaseMapper<BidderFileInfo> {

    /**
     * 获取所有施工清单解析未完成的投标人文件信息
     * @return
     */
    List<BidderFileInfo> listXmlNoParseCompleteInfo();
}
