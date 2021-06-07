package com.ejiaoyi.common.constant;

/**
 * @Desc: 投标人解密状态
 * @author: lgq
 * @date:
 */
public interface BidderFileType {
    /**
     * 非加密文件
     */
    Integer GEF = 0;

    /**
     * 加密文件
     */
    Integer SGEF = 1;

    /**
     * 手机证书加密文件
     */
    Integer PHONE_SGEF = 2;
}
