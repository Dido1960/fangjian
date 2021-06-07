package com.ejiaoyi.bidder.controller;

import com.ejiaoyi.common.constant.CacheName;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.dto.MobilePhoneScanCodeDTO;
import com.ejiaoyi.common.dto.PhoneCertDTO;
import com.ejiaoyi.common.dto.QrLoginResultDTO;
import com.ejiaoyi.common.service.impl.MobilePhoneScanCodeServiceImpl;
import com.ejiaoyi.common.util.RedisUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * 手机扫码 控制层
 *
 * @author Mike
 * @date 2021/3/11 11:24
 */
@RestController
@RequestMapping("/mobilePhoneScanCode")
public class MobilePhoneScanCodeController {

    /**
     * 成功
     */
    private final String SUCCESS = "200";

    @Autowired
    private MobilePhoneScanCodeServiceImpl mobilePhoneScanCodeService;

    /**
     * 获取手机登录二维码信息
     * @return
     */
    @RequestMapping("/generateQRCode")
    public MobilePhoneScanCodeDTO generateQRCode() {
        return mobilePhoneScanCodeService.generateQRCode();
    }

    /**
     * 获取二维码登录结果
     * @param verifyId 校验编号
     * @param token token
     * @return
     */
    @RequestMapping("/verifyQRLogin")
    public JsonData verifyQRLogin(String verifyId, String token) {
        System.out.println("verifyId:" + verifyId + "===================token:" + token);
        JsonData jsonData = new JsonData();
        QrLoginResultDTO qrLoginResultDTO = mobilePhoneScanCodeService.verifyQRLogin(verifyId, token);
        jsonData.setCode(qrLoginResultDTO.getCode());
        jsonData.setMsg(qrLoginResultDTO.getMsg());
        if (SUCCESS.equals(qrLoginResultDTO.getCode())
                && qrLoginResultDTO.getCompanyUser() != null) {
            // 手机扫码登录令牌
            String qrCodeKey = CacheName.PHONE_LOGIN +UUID.randomUUID().toString();
            RedisUtil.set(qrCodeKey, qrLoginResultDTO.getCompanyUser());
            jsonData.setData(qrCodeKey);
        }
        return jsonData;
    }

    /**
     * 跳转二维码扫码解密页面
     * @return
     */
    @RequestMapping("/decryptQrPage")
    public ModelAndView decryptQrPage() {
        return new ModelAndView("/mobilePhone/decryptQrPage");
    }

    /**
     * 获取二维码登录结果
     * @param verifyId 校验编号
     * @param token token
     * @return
     */
    @RequestMapping("/verifyQrUserCert")
    public JsonData verifyQrUserCert(String verifyId, String token) {
        System.out.println("verifyId:" + verifyId + "===================token:" + token);
        JsonData jsonData = new JsonData();
        QrLoginResultDTO qrLoginResultDTO = mobilePhoneScanCodeService.verifyQrUserCert(verifyId, token);
        jsonData.setCode(qrLoginResultDTO.getCode());
        jsonData.setMsg(qrLoginResultDTO.getMsg());
        if (SUCCESS.equals(qrLoginResultDTO.getCode())
                && CollectionUtils.isNotEmpty(qrLoginResultDTO.getPhoneCertDTOList())) {
            jsonData.setData(qrLoginResultDTO.getPhoneCertDTOList());
        }
        return jsonData;
    }

    /**
     * 通知管理员解密文件成功信息
     * @param verifyId 校验编号
     * @param token token
     * @param projectCode 标段编号
     * @param projectName 标段名称
     */
    @RequestMapping("/qrFileDecryptSuccess")
    public void qrFileDecryptSuccess(String verifyId, String projectCode, String projectName) {
        mobilePhoneScanCodeService.qrFileDecryptSuccess(verifyId, projectCode, projectName);
    }
}