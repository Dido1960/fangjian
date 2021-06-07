package com.ejiaoyi.common.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.SignatureConfigInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ejiaoyi.common.entity.Site;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 签章配置信息（回执单签章） Mapper 接口
 * </p>
 *
 * @author Mike
 * @since 2021-02-18
 */
@Component
public interface SignatureConfigInfoMapper extends BaseMapper<SignatureConfigInfo> {

    List<SignatureConfigInfo> pagedSignatureConfigInfo(Page page, @Param("signatureConfigInfo") SignatureConfigInfo signatureConfigInfo);
}
