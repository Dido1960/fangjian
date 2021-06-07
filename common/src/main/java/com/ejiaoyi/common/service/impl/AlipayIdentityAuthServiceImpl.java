package com.ejiaoyi.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayUserCertifyOpenCertifyRequest;
import com.alipay.api.request.AlipayUserCertifyOpenInitializeRequest;
import com.alipay.api.request.AlipayUserCertifyOpenQueryRequest;
import com.alipay.api.response.AlipayUserCertifyOpenCertifyResponse;
import com.alipay.api.response.AlipayUserCertifyOpenInitializeResponse;
import com.alipay.api.response.AlipayUserCertifyOpenQueryResponse;
import com.ejiaoyi.common.service.IAlipayIdentityAuthService;
import com.ejiaoyi.common.constant.AlipayIdentityAuthConstant;
import com.ejiaoyi.common.entity.ApiLog;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.service.IApiLogService;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 支付宝身份认证服务实现类
 *
 * @author Make
 * @since 2020-08-04
 */
@Service
public class AlipayIdentityAuthServiceImpl extends BaseServiceImpl implements IAlipayIdentityAuthService {

    @Autowired
    private IApiLogService apiLogService;

    @Override
    public String authInitialize(String outerOrderNo, String idCard, String name, String callBackUrl) {
        LocalDateTime createTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS),
                DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
        LocalDateTime responseTime;
        // 记录API请求日志
        ApiLog apiLog = ApiLog.builder()
                .apiName(AlipayIdentityAuthConstant.AliHttpInterface.URL)
                .methodName(AlipayIdentityAuthConstant.ApiInterfaceConstants.INITIALIZE)
                .createApiTime(createTime)
                .build();

        String certifyId = "";

        //构造身份信息json对象
        JSONObject identityObj = new JSONObject();
        identityObj.put("identity_type", AlipayIdentityAuthConstant.IDENTITY_TYPE);
        identityObj.put("cert_type", AlipayIdentityAuthConstant.CERT_TYPE);
        identityObj.put("cert_name", name);
        identityObj.put("cert_no", idCard);

        //构造商户配置json对象
        JSONObject merchantConfigObj = new JSONObject();

        // 设置回调地址,必填. 如果需要直接在支付宝APP里面打开回调地址使用alipay协议，参考下面的案例：appId用固定值 20000067，url替换为urlEncode后的业务回跳地址
        // alipays://platformapi/startapp?appId=20000067&url=https%3A%2F%2Fapp.cqkqinfo.com%2Fcertify%2FzmxyBackNew.do
        merchantConfigObj.put("return_url", "alipays://platformapi/startapp?appId=20000067&url="+callBackUrl);

        //构造身份认证初始化服务业务参数数据
        JSONObject bizContentObj = new JSONObject();
        //商户请求的唯一标识，推荐为uuid，必填
        bizContentObj.put("outer_order_no", outerOrderNo);
        bizContentObj.put("biz_code", AlipayIdentityAuthConstant.BizCodeConstants.FACE);
        bizContentObj.put("identity_param", identityObj);
        bizContentObj.put("merchant_config", merchantConfigObj);

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayIdentityAuthConstant.AliHttpInterface.URL, AlipayIdentityAuthConstant.AlipayConstants.APP_ID,
                AlipayIdentityAuthConstant.AlipayConstants.APP_PRIVATE_KEY, AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_UTF8,
                AlipayIdentityAuthConstant.AlipayConstants.ALIPAY_PUBLIC_KEY, AlipayConstants.SIGN_TYPE_RSA2);
        AlipayUserCertifyOpenInitializeRequest request = new AlipayUserCertifyOpenInitializeRequest();

        String bizContent = bizContentObj.toString();

        // 请求写入日志表中
        apiLog.setParams(bizContent);
        Integer apiLogId = apiLogService.addLog(apiLog);

        request.setBizContent(bizContent);

        AlipayUserCertifyOpenInitializeResponse response = null;
        try {
            response = alipayClient.execute(request);

            responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS),
                    DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
            ApiLog log = ApiLog.builder()
                    .id(apiLogId)
                    .response(response.getBody())
                    .responseTime(responseTime)
                    .responseTimeConsume(DateTimeUtil.getTimeDiff(createTime, responseTime))
                    .build();

            apiLogService.updateLog(log);

            if(response.isSuccess()){
                certifyId = response.getCertifyId();
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();

            responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS),
                    DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
            ApiLog log = ApiLog.builder()
                    .id(apiLogId)
                    .response("code:" + e.getErrCode() + "; " + e.getErrMsg())
                    .responseTime(responseTime)
                    .responseTimeConsume(DateTimeUtil.getTimeDiff(createTime, responseTime))
                    .build();

            apiLogService.updateLog(log);

            return certifyId;
        }

        return certifyId;
    }



    @Override
    public String startCertify(String certifyId) {
        LocalDateTime createTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS),
                DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
        LocalDateTime responseTime;
        // 记录API请求日志
        ApiLog apiLog = ApiLog.builder()
                .apiName(AlipayIdentityAuthConstant.AliHttpInterface.URL)
                .methodName(AlipayIdentityAuthConstant.ApiInterfaceConstants.CERTIFY)
                .createApiTime(createTime)
                .build();

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayIdentityAuthConstant.AliHttpInterface.URL, AlipayIdentityAuthConstant.AlipayConstants.APP_ID,
                AlipayIdentityAuthConstant.AlipayConstants.APP_PRIVATE_KEY, AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_UTF8,
                AlipayIdentityAuthConstant.AlipayConstants.ALIPAY_PUBLIC_KEY, AlipayConstants.SIGN_TYPE_RSA2);
        AlipayUserCertifyOpenCertifyRequest request = new AlipayUserCertifyOpenCertifyRequest();

        JSONObject bizContent = new JSONObject();
        bizContent.put("certify_id", certifyId);
        String bizContentStr = bizContent.toString();

        // 请求写入日志表中
        apiLog.setParams(bizContentStr);
        Integer apiLogId = apiLogService.addLog(apiLog);

        request.setBizContent(bizContentStr);
        AlipayUserCertifyOpenCertifyResponse response = null;
        try {
            response = alipayClient.pageExecute(request, "GET");

            responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));

            if(response.isSuccess()){
                return response.getBody();
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();

            responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS),
                    DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));

            return null;
        }

        return null;
    }

    @Override
    public boolean queryCertify(String certifyId) {
        boolean flag = false;
        LocalDateTime createTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS),
                DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
        LocalDateTime responseTime;
        // 记录API请求日志
        ApiLog apiLog = ApiLog.builder()
                .apiName(AlipayIdentityAuthConstant.AliHttpInterface.URL)
                .methodName(AlipayIdentityAuthConstant.ApiInterfaceConstants.QUERY)
                .createApiTime(createTime)
                .build();

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayIdentityAuthConstant.AliHttpInterface.URL, AlipayIdentityAuthConstant.AlipayConstants.APP_ID,
                AlipayIdentityAuthConstant.AlipayConstants.APP_PRIVATE_KEY, AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_UTF8,
                AlipayIdentityAuthConstant.AlipayConstants.ALIPAY_PUBLIC_KEY, AlipayConstants.SIGN_TYPE_RSA2);

        AlipayUserCertifyOpenQueryRequest request = new AlipayUserCertifyOpenQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("certify_id", certifyId);
        String bizContentStr = bizContent.toString();

        // 请求写入日志表中
        apiLog.setParams(bizContentStr);
        Integer apiLogId = apiLogService.addLog(apiLog);

        request.setBizContent(bizContentStr);
        AlipayUserCertifyOpenQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            if(response.isSuccess()){
                String body = response.getBody();
                JSONObject jsonObject = JSON.parseObject(body);
                String queryResponse = jsonObject.getString("alipay_user_certify_open_query_response");

                JSONObject query = JSON.parseObject(queryResponse);
                String code = query.getString("code");

                responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
                ApiLog log = ApiLog.builder()
                        .id(apiLogId)
                        .response(response.getBody())
                        .responseTime(responseTime)
                        .responseTimeConsume(DateTimeUtil.getTimeDiff(createTime, responseTime))
                        .build();
                apiLogService.updateLog(log);

                // 校验请求是否成功
                if (AlipayIdentityAuthConstant.SUCCESS_CODE.equals(code)){
                    String passed = query.getString("passed");
                    // 校验是否认证成功
                    if (AlipayIdentityAuthConstant.VERIFY_PASSED_T.equals(passed)) {
                        flag = true;
                    }
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();

            responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
            ApiLog log = ApiLog.builder()
                    .id(apiLogId)
                    .response("code:" + e.getErrCode() + "; " + e.getErrMsg())
                    .responseTime(responseTime)
                    .responseTimeConsume(DateTimeUtil.getTimeDiff(createTime, responseTime))
                    .build();

            apiLogService.updateLog(log);

            return false;
        }

        return flag;
    }
}
