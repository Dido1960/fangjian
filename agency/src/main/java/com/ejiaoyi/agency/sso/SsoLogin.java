package com.ejiaoyi.agency.sso;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.dto.HttpResponseDTO;
import com.ejiaoyi.common.dto.JsonData;
import com.ejiaoyi.common.util.ApplicationContextUtil;
import com.ejiaoyi.common.util.HttpClientUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * 单点登录逻辑代码
 *
 * @author fengjunhong
 * @since 2020-6-30
 */
public class SsoLogin {

    Map<String, Object> map = new HashMap<>();

    /**
     * 单点服务器路由
     */
    SsoParameter ssoParameter;

    /**
     * 获取token
     * @return
     */
    private String getToken() {
        // 获取单点服务器路由对象
        ssoParameter = (SsoParameter) ApplicationContextUtil.getApplicationContext().getBean("SsoParameter");

        map.put("platform", ssoParameter.getPlatform());
        map.put("api_key", ssoParameter.getApiKey());

        // 获取token
        HttpResponseDTO dto = HttpClientUtil.postRaw(ssoParameter.getSsoUrl() + "/api/sso/getToken", JSONObject.toJSONString(map));
        JsonData jsonData = JSONObject.parseObject(dto.getContent(), JsonData.class);
        return (String) jsonData.getData();
    }

    /**
     * 返回登录用户对象
     *
     * @param loginName 用户名
     * @param password 密码
     * @param userType 用户类型
     * @return
     * @throws Exception
     */
    public JSONObject getUser(String loginName, String password, Integer userType) throws Exception {
        Assert.notEmpty(loginName);

        String token = getToken();

        // 验证账号密码
        map.put("token", token);
        map.put("login_name", loginName);
        map.put("password", SM2Util.encrypt(password));
        map.put("userType", userType);
        HttpResponseDTO dto = HttpClientUtil.postRaw(ssoParameter.getSsoUrl() + "/api/sso/getUserInfo", JSONObject.toJSONString(map));
        JsonData jsonData = JSONObject.parseObject(dto.getContent(), JsonData.class);

        if (jsonData == null) {
            throw new UsernameNotFoundException("用户名或密码错误，请重新输入!");
        }
        return (JSONObject) jsonData.getData();
    }

    /**
     * 返回CA绑定用户信息(北京ca)
     *
     * @param ukeyNum 锁号
     * @param userType 用户类型
     * @return
     */
    public JSONObject getCertBindUser(String ukeyNum,Integer userType) {
        String token = getToken();

        // 验证账号密码
        map.put("token", token);
        map.put("usb_key_num", ukeyNum);
        map.put("user_type", userType);
        HttpResponseDTO dto = HttpClientUtil.postRaw(ssoParameter.getSsoUrl() + "/api/sso/getUserInfoByCert", JSONObject.toJSONString(map));
        JsonData jsonData = JSONObject.parseObject(dto.getContent(), JsonData.class);

        if (jsonData == null) {
            throw new UsernameNotFoundException("当前用户不存在!");
        }
        return (JSONObject) jsonData.getData();
    }


    /**
     * 返回CA绑定用户信息(其他公司ca)
     *
     * @param certSerialNumber 证书序列号
     * @param userType 用户类型
     * @return
     */
    public JSONObject getOtherCertBindUser(String certSerialNumber,Integer userType) {
        String token = getToken();

        // 验证账号密码
        map.put("token", token);
        map.put("cert_serial_number", certSerialNumber);
        map.put("user_type", userType);
        HttpResponseDTO dto = HttpClientUtil.postRaw(ssoParameter.getSsoUrl() + "/api/sso/getUserInfoByCert", JSONObject.toJSONString(map));
        JsonData jsonData = JSONObject.parseObject(dto.getContent(), JsonData.class);

        if (jsonData == null) {
            throw new UsernameNotFoundException("当前用户不存在!");
        }
        return (JSONObject) jsonData.getData();
    }
}
