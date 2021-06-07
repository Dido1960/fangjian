package com.ejiaoyi.common.util;

import com.alibaba.fastjson.JSONObject;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class BDIPUtils {
    private static Logger logger = LoggerFactory.getLogger(BDIPUtils.class);
    private static final String HTTP = "http://api.map.baidu.com/location/ip";
    private static final String HTTPS = "https://api.map.baidu.com/location/ip";
    private static final String AK = "Ykswepd0YvdNc5zmAWAkVhY0UCTrdZGE";
    private static final String COOR = "bd09ll";

    /*@Resource
    private RestTemplate restTemplate;*/
    private RestTemplate restTemplate = new RestTemplate();

    public JSONObject getDate(String ip) {
        String url = HTTPS + "?coor=bd09ll&ak=" + AK + "&ip=" + ip;
        return restTemplate.getForObject(url, JSONObject.class);
    }

    public static void main(String[] args) {
        String ip = "127.0.0.1"; // 访问IP
        BDIPUtils bdipUtils = new BDIPUtils();
        JSONObject jsonObject = bdipUtils.getDate(ip);
        logger.info("result={}", jsonObject.toJSONString());
        if (jsonObject != null) {
            JSONObject json = jsonObject.getJSONObject("content");
            logger.info("地址={}", json.get("address"));
            JSONObject pointJSON = json.getJSONObject("point");
            logger.info("经度={}; 纬度={}", pointJSON.get("x"), pointJSON.get("y"));
        }

    }

    /**
     * 通过IP获取地址信息
     ***/
    public static String addressDescribe(String ip) {
        if (!StringUtil.isNotEmpty(ip)) {
            return null;
        }
        try {
            BDIPUtils bdipUtils = new BDIPUtils();
            JSONObject jsonObject = bdipUtils.getDate(ip);
            logger.info("result={}", jsonObject.toJSONString());
            if (jsonObject != null) {

                JSONObject json = jsonObject.getJSONObject("content");
                if(json!=null) {
                    logger.info("地址={}", json.get("address"));
                    String address = (String) json.get("address");
                    JSONObject pointJSON = json.getJSONObject("point");
                    return address + "{经度=[" + pointJSON.get("x") + "];纬度=[" + pointJSON.get("y") + "]}";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}