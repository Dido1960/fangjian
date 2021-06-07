package com.ejiaoyi.common.util;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.constant.BlockchainConstant;
import com.ejiaoyi.common.dto.HttpResponseDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: 区块链工具类
 * @Auther: liuguoqiang
 * @Date: 2020-8-17 15:13
 */
public class BlockchainUtil {
    private static String baseUrl;

    static {
        try {
            Environment env = ApplicationContextUtil.getApplicationContext().getBean(Environment.class);
            baseUrl = env.getProperty("block-chain.base-url");
            if (StringUtils.isEmpty(baseUrl)) {
                baseUrl = BlockchainConstant.BASE_URL;
            }
        } catch (Exception e) {
            baseUrl = BlockchainConstant.BASE_URL;
        }
    }

    /**
     * 获取区块链token
     *
     * @return 获取失败返回null
     */
    public static String getToken(){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("platform", BlockchainConstant.PLAT_FORM);
        map.put("api_key", BlockchainConstant.API_KEY);
        String json = JSONUtils.toJSONString(map);
        HttpResponseDTO httpResponseDTO = HttpClientUtil.postRaw(baseUrl + BlockchainConstant.GET_TOKEN, json);
        String token = null;
        if (httpResponseDTO != null && httpResponseDTO.getCode() == 200) {
            String content = httpResponseDTO.getContent();
            JSONObject parse = JSONObject.parseObject(content);
            token = parse.getString("data");
        }
        return token;
    }

    /**
     * 连接区块链的方法
     * @param method 需要调用的接口地址，不需要BASE_URL
     * @param json 需要传递的参数，需要完整的封装参数
     * @return 请求失败返回null 成功返回data json串
     */
    public static String doThisMethod(String method, String json){
        HttpResponseDTO httpResponseDTO = HttpClientUtil.postRaw(baseUrl + method, json);
        String data = null;
        if (httpResponseDTO != null && httpResponseDTO.getCode() == 200) {
            String content = httpResponseDTO.getContent();
            JSONObject parse = JSONObject.parseObject(content);
            data  = parse.getString("data");
        }
        return data;
    }
}
