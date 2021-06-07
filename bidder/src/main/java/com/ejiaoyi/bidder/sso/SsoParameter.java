package com.ejiaoyi.bidder.sso;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 单点登录所需参数
 * @author fengjunhong
 * @since 2020-6-30
 */
@Component("SsoParameter")
public class SsoParameter {
    @Value("${sso.url}")
    public String ssoUrl;

    @Value("${sso.platform}")
    public String platform;

    @Value("${sso.apikey}")
    public String apiKey;

    public String getPlatform() {
        return platform;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSsoUrl() {
        return ssoUrl;
    }
}
