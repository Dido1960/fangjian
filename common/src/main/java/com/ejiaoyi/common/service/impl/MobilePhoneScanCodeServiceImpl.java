package com.ejiaoyi.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.dto.*;
import com.ejiaoyi.common.entity.CompanyUser;
import com.ejiaoyi.common.service.IMobilePhoneScanCodeService;
import com.ejiaoyi.common.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * API接口认证信息 服务实现类
 * </p>
 *
 * @author Z0001
 * @since 2020-04-01
 */
@Service
@Slf4j
public class MobilePhoneScanCodeServiceImpl extends BaseServiceImpl implements IMobilePhoneScanCodeService {

    @Autowired
    private Environment env;

    /**
     * 成功
     */
    private final String SUCCESS = "200";

    /**
     * 获取token的url
     */
    private static String GET_TOKEN_URL = "/api/sso/getToken";

    /**
     * 获取登录二维码信息的url
     */
    private static String GET_QR_CODE_URL = "/api/sso/getQrCode";

    /**
     * 获取二维码登录结果获取的url
     */
    private static String QR_LOGIN_URL = "/api/sso/qrLogin";

    /**
     * 获取用户扫码后的证书
     */
    private static String QR_USER_CERT = "/api/sso/qrFileDecrypt";

    /**
     * 扫码文件解密成功调用
     */
    private static String QR_FILE_DECRYPT_SUCCESS = "/api/sso/qrFileDecryptSuccess";

    @Override
    public MobilePhoneScanCodeDTO generateQRCode() {
        String token = this.getToken();

        Map<String, String> param = new HashMap<>(3);
        param.put("platform", env.getProperty("sso.platform"));
        param.put("api_key", env.getProperty("sso.apikey"));
        param.put("token", token);

        // 获取登录二维码信息
        HttpResponseDTO httpResponseDTO = HttpClientUtil.postRaw(env.getProperty("sso.url") + GET_QR_CODE_URL, JSONObject.toJSONString(param));
        if (httpResponseDTO.getCode() != HttpStatus.SC_OK) {
            return MobilePhoneScanCodeDTO.builder()
                    .httpStatus(false)
                    .build();
        }

        String content = httpResponseDTO.getContent();
        JSONObject parse = JSONObject.parseObject(content);
        String data = parse.getString("data");
        MobilePhoneScanCodeDTO mobilePhoneScanCodeDTO = JSONObject.parseObject(data, MobilePhoneScanCodeDTO.class);

        mobilePhoneScanCodeDTO.setHttpStatus(true);
        mobilePhoneScanCodeDTO.setToken(this.getToken());
        return mobilePhoneScanCodeDTO;
    }

    @Override
    public QrLoginResultDTO verifyQRLogin(String verifyId, String token) {
        Map<String, String> param = new HashMap<>(4);
        param.put("platform", env.getProperty("sso.platform"));
        param.put("api_key", env.getProperty("sso.apikey"));
        param.put("token", token);
        param.put("verifyId", verifyId);

        // 获取登录二维码信息
        HttpResponseDTO httpResponseDTO = HttpClientUtil.postRaw(env.getProperty("sso.url") + QR_LOGIN_URL, JSONObject.toJSONString(param));

        QrLoginResultDTO qrLoginResultDTO = JSONObject.parseObject(httpResponseDTO.getContent(), QrLoginResultDTO.class);
        if (SUCCESS.equals(qrLoginResultDTO.getCode())) {
            String content = httpResponseDTO.getContent();
            JSONObject parse = JSONObject.parseObject(content);
            String data = parse.getString("data");
            if (StringUtils.isNotEmpty(data)) {
                CompanyUser companyUser = JSONObject.parseObject(data, CompanyUser.class);
                qrLoginResultDTO.setCompanyUser(companyUser);
            }
        }

        return qrLoginResultDTO;
    }

    @Override
    public QrLoginResultDTO verifyQrUserCert(String verifyId, String token) {
        Map<String, String> param = new HashMap<>(4);
        param.put("platform", env.getProperty("sso.platform"));
        param.put("api_key", env.getProperty("sso.apikey"));
        param.put("token", token);
        param.put("verifyId", verifyId);

        // 获取登录二维码信息
        HttpResponseDTO httpResponseDTO = HttpClientUtil.postRaw(env.getProperty("sso.url") + QR_USER_CERT, JSONObject.toJSONString(param));

        QrLoginResultDTO qrLoginResultDTO = JSONObject.parseObject(httpResponseDTO.getContent(), QrLoginResultDTO.class);
        if (SUCCESS.equals(qrLoginResultDTO.getCode())) {
            String content = httpResponseDTO.getContent();
            JSONObject parse = JSONObject.parseObject(content);
            String data = parse.getString("data");
            if (StringUtils.isNotEmpty(data)) {
                List<PhoneCertDTO> phoneCertDTOList = JSONObject.parseArray(data, PhoneCertDTO.class);
                qrLoginResultDTO.setPhoneCertDTOList(phoneCertDTOList);
            }
        }

        return qrLoginResultDTO;
    }

    @Override
    public void qrFileDecryptSuccess(String verifyId, String projectCode, String projectName) {
        Map<String, String> param = new HashMap<>(6);
        param.put("platform", env.getProperty("sso.platform"));
        param.put("api_key", env.getProperty("sso.apikey"));
        param.put("projectCode", projectCode);
        param.put("projectName", projectName);
        param.put("token", getToken());
        param.put("verifyId", verifyId);

        // 通知管理员解密信息
        HttpResponseDTO httpResponseDTO = HttpClientUtil.postRaw(env.getProperty("sso.url") + QR_FILE_DECRYPT_SUCCESS, JSONObject.toJSONString(param));
        log.info("通知管理员解密信息:" + httpResponseDTO.getContent());
    }

    /**
     * 获取token
     * @return
     */
    private String getToken() {
        Map<String, String> param = new HashMap<>(2);
        param.put("platform", env.getProperty("sso.platform"));
        param.put("api_key", env.getProperty("sso.apikey"));

        // 获取token
        HttpResponseDTO dto = HttpClientUtil.postRaw(env.getProperty("sso.url") + GET_TOKEN_URL, JSONObject.toJSONString(param));
        JsonData jsonData = JSONObject.parseObject(dto.getContent(), JsonData.class);
        return (String) jsonData.getData();
    }

}
