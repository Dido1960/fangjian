package com.ejiaoyi.common.service.impl;


import cn.unitid.realidentity.sdk.ApiException;
import cn.unitid.realidentity.sdk.AutoRetryApiClient;
import cn.unitid.realidentity.sdk.request.VerifyTokenGetRequset;
import cn.unitid.realidentity.sdk.request.VerifyTokenResultRequset;
import cn.unitid.realidentity.sdk.request.VerifyTokenStatusRequset;
import cn.unitid.realidentity.sdk.response.VerifyTokenGetResponse;
import cn.unitid.realidentity.sdk.response.VerifyTokenResultResponse;
import cn.unitid.realidentity.sdk.response.VerifyTokenStatusResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.service.IXinIdIdentityAuthService;
import com.ejiaoyi.common.constant.XinIdConstant;
import com.ejiaoyi.common.entity.ApiLog;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.service.IApiLogService;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.ejiaoyi.common.constant.XinIdConstant.STATE_SUCCESS;

/**
 * 江苏一证通身份认证服务实现类
 *
 * @author Make
 * @since 2020-08-04
 */
@Service
public class XinIdIdentityAuthServiceImpl extends BaseServiceImpl implements IXinIdIdentityAuthService {

    @Autowired
    private IApiLogService apiLogService;

    @Override
    public String getH5URL(String ticketId, String idCard, String name, String callBackUrl){
        String verifyUrl = "";

        LocalDateTime createTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
        LocalDateTime responseTime;
        // 记录API请求日志
        ApiLog apiLog = ApiLog.builder()
                .apiName(XinIdConstant.RequestUrl.H5_GET_URL)
                .methodName("getH5URL")
                .createApiTime(createTime)
                .build();

        AutoRetryApiClient apiClient = new AutoRetryApiClient(XinIdConstant.RequestUrl.H5_GET_URL, XinIdConstant.APPKEY, XinIdConstant.SECRETKEY);
        VerifyTokenGetRequset req = new VerifyTokenGetRequset();
        //随机数，唯一
        req.setTicketId(ticketId);
        //业务场景
        req.setBizScene(VerifyTokenGetRequset.BizScene.LIVENESS_H5);
        //扩展参数
        JSONObject json = new JSONObject();

        //认证成功回调地址
        json.put("callback", callBackUrl);
        json.put("realname", name);
        json.put("idcard", idCard);
        json.put("livenessRate","0.022403");

        req.setAttr(json.toJSONString());

        // 请求写入日志表中
        apiLog.setParams(req.toString());
        Integer apiLogId = apiLogService.addLog(apiLog);

        VerifyTokenGetResponse response = null;
        try {
            response = apiClient.execute(req);

            responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
            ApiLog log = ApiLog.builder()
                    .id(apiLogId)
                    .params(json.toJSONString())
                    .response(response.getBody())
                    .responseTime(responseTime)
                    .responseTimeConsume(DateTimeUtil.getTimeDiff(createTime, responseTime))
                    .build();
            apiLogService.updateLog(log);

            if (STATE_SUCCESS.equals(response.getMsg())) {
                verifyUrl = response.getData().getVerifyPageUrl();
            }
            //后续业务处理
        } catch (ApiException e) {
            e.printStackTrace();

            responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
            ApiLog log = ApiLog.builder()
                    .id(apiLogId)
                    .params(json.toJSONString())
                    .response("code:" + e.getErrCode() + "; " + e.getErrMsg())
                    .responseTime(responseTime)
                    .responseTimeConsume(DateTimeUtil.getTimeDiff(createTime, responseTime))
                    .build();

            apiLogService.updateLog(log);

            return verifyUrl;
        }
        return verifyUrl;
    }

    @Override
    public String getH5SessionStatus(String ticketId) {
        LocalDateTime createTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
        LocalDateTime responseTime;
        // 记录API请求日志
        ApiLog apiLog = ApiLog.builder()
                .apiName(XinIdConstant.RequestUrl.H5_GET_URL)
                .methodName("getH5ResState")
                .createApiTime(createTime)
                .build();

        // 调用出错，自动重试客户端
        AutoRetryApiClient apiClient = new AutoRetryApiClient(XinIdConstant.RequestUrl.H5_GET_SESSONSTATUS, XinIdConstant.APPKEY, XinIdConstant.SECRETKEY);
        VerifyTokenStatusRequset req = new VerifyTokenStatusRequset();
        req.setTicketId(ticketId);
        // 请求写入日志表中
        apiLog.setParams("{'ticketId':'" + ticketId + "'}");
        Integer apiLogId = apiLogService.addLog(apiLog);

        VerifyTokenStatusResponse response = null;
        try {
            response = apiClient.execute(req);

            responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
            ApiLog log = ApiLog.builder()
                    .id(apiLogId)
                    .response(response.getBody())
                    .responseTime(responseTime)
                    .responseTimeConsume(DateTimeUtil.getTimeDiff(createTime, responseTime))
                    .build();
            apiLogService.updateLog(log);

            if (STATE_SUCCESS.equals(response.getMsg())) {
                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
                JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.get("data").toString());
                return (String) jsonObject1.get("status");
            }

            //后续业务处理
        } catch (ApiException e) {
            e.printStackTrace();
            e.printStackTrace();
            responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
            ApiLog log = ApiLog.builder()
                    .id(apiLogId)
                    .response("code:" + e.getErrCode() + "; " + e.getErrMsg())
                    .responseTime(responseTime)
                    .responseTimeConsume(DateTimeUtil.getTimeDiff(createTime, responseTime))
                    .build();

            apiLogService.updateLog(log);
            return null;
        }
        return null;
    }

    @Override
    public String getH5ResState(String ticketId) {
        LocalDateTime createTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
        LocalDateTime responseTime;
        // 记录API请求日志
        ApiLog apiLog = ApiLog.builder()
                .apiName(XinIdConstant.RequestUrl.H5_GET_URL)
                .methodName("getH5ResState")
                .createApiTime(createTime)
                .build();

        // 调用出错，自动重试客户端
        AutoRetryApiClient apiClient = new AutoRetryApiClient(XinIdConstant.RequestUrl.H5_GET_URL, XinIdConstant.APPKEY, XinIdConstant.SECRETKEY);
        VerifyTokenResultRequset req = new VerifyTokenResultRequset();
        req.setTicketId(ticketId);

        // 请求写入日志表中
        apiLog.setParams(req.toString());
        Integer apiLogId = apiLogService.addLog(apiLog);

        VerifyTokenResultResponse response = null;
        try {
            response = apiClient.execute(req);

            responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
            ApiLog log = ApiLog.builder()
                    .id(apiLogId)
                    .response(response.getBody())
                    .responseTime(responseTime)
                    .responseTimeConsume(DateTimeUtil.getTimeDiff(createTime, responseTime))
                    .build();
            apiLogService.updateLog(log);

            if (STATE_SUCCESS.equals(response.getMsg())) {
                JSONObject jsonObject = JSONObject.parseObject(response.getBody());
                JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.get("data").toString());
                JSONObject jsonObject2 = JSONObject.parseObject(jsonObject1.get("result").toString());
                JSONArray jsonArray = (JSONArray) jsonObject2.get("pictures");

                return jsonArray.get(0).toString();
            }
            //后续业务处理
        } catch (ApiException e) {
            e.printStackTrace();
            responseTime = LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA));
            ApiLog log = ApiLog.builder()
                    .id(apiLogId)
                    .response("code:" + e.getErrCode() + "; " + e.getErrMsg())
                    .responseTime(responseTime)
                    .responseTimeConsume(DateTimeUtil.getTimeDiff(createTime, responseTime))
                    .build();

            apiLogService.updateLog(log);

            return null;
        }
        return null;
    }
}
