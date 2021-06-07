package com.ejiaoyi.common.service;

import com.ejiaoyi.common.dto.MobilePhoneScanCodeDTO;
import com.ejiaoyi.common.dto.QrLoginResultDTO;


/**
 * <p>
 * 手机扫码 服务类
 * </p>
 *
 * @author Mike
 * @since 2021-03-12
 */
public interface IMobilePhoneScanCodeService {

    /**
     * 获取手机登录二维码信息
     * @return 手机登录二维码信息
     */
    MobilePhoneScanCodeDTO generateQRCode();

    /**
     * 获取二维码登录结果
     * @param verifyId 校验编号
     * @param token token
     * @return
     */
    QrLoginResultDTO verifyQRLogin(String verifyId, String token);

    /**
     * 获取用户扫码后的证书
     * @param verifyId 校验编号
     * @param token token
     * @return
     */
    QrLoginResultDTO verifyQrUserCert(String verifyId, String token);

    /**
     * 解密成功后通知管理员
     * @param verifyId 校验编号
     * @param projectCode 项目编号
     * @param projectName 项目名称
     */
    void qrFileDecryptSuccess(String verifyId, String projectCode, String projectName);
}
