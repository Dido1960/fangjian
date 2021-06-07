package com.ejiaoyi.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 签名工具类
 * @author scqsk
 */
@Component("signature")
public class SignatureConfig {
    @Value("${signature.ip}")
    String signatureIp;

    @Value("${signature.port}")
    String signaturePort;

    public String getSignatureIp() {
        return signatureIp;
    }

    public void setSignatureIp(String signatureIp) {
        this.signatureIp = signatureIp;
    }

    public String getSignaturePort() {
        return signaturePort;
    }

    public void setSignaturePort(String signaturePort) {
        this.signaturePort = signaturePort;
    }
}
