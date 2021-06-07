package com.ejiaoyi.bidder.service;

/**
 * 身份认证 服务类
 *
 * @author Make
 * @since 2020-8-3
 */
public interface IIdentityAuthService {

    /**
     * 校验二维码有效性
     *
     * @param bidSectionId 标段id
     * @return 是否失效
     */
    boolean verifyQRCodeInvalid(Integer bidSectionId);

    /**
     * 校验该投标人是否完成了身份认证
     *
     * @param bidderId 投标人id
     * @return 是否完成
     */
    boolean verifyCompleteAuth(Integer bidderId);

    /**
     * 获取扫码后回调地址
     *
     * @param boiId bidderopeninfo 主键
     * @return
     */
    String getCallBakUrl(Integer boiId, Integer authType);

    /**
     * 将文件上传到本地服务器
     * @param utl
     * @param id
     * @return
     */
    String uploadPhoto(String utl,Integer id);
}
