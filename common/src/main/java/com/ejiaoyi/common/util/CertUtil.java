package com.ejiaoyi.common.util;

import com.ejiaoyi.common.crypto.SM2Util;
import com.ejiaoyi.common.crypto.SM4Util;
import lombok.extern.slf4j.Slf4j;

/**
 * 数字证书 相关操作工具类
 *
 * @author Z0001
 * @since 2020-06-08
 */
@Slf4j
public class CertUtil {

    /**
     * 数字证书信息 加密算法
     *
     * @param content 原文
     * @param key     密钥 密钥长度必须为16位 16进制数据 规定采用数字证书UKEY序列号
     *                key取锁号后16位 ；不足16位，零补齐
     * @return 密文
     */
    public static String encrypt(String content, String key) {
        System.out.println("原文:" + content);
        // 16位锁号
        String keyNum16 = null;
        if (key.length() > 16) {
            // 锁号大于16位
            keyNum16 = key.substring(key.length() - 16);
        } else {
            // 少于16位时，用零补齐
            StringBuffer sb = new StringBuffer(keyNum16);
            while (sb.length() < 16) {
                sb.append(0);
            }
        }
        // 先进行SM4 对称算法加密
        String encrypt = SM4Util.encryptECB(content, keyNum16);
        System.out.println("SM4加密:" + encrypt);
        try {
            // 再使用SM2 进行非对称加密
            encrypt = SM2Util.encrypt(encrypt);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CertUtil encrypt error: " + e.getMessage());
        }

        return encrypt;
    }

    /**
     * 数字证书信息 解密算法
     *
     * @param content 密文
     * @param key     密钥 密钥长度必须为16位 16进制数据 规定采用数字证书UKEY序列号
     * @return 原文
     */
    public static String decrypt(String content, String key) {
        String decrypt = "";
        try {
            // 再使用SM4 进行非对称解密
            decrypt = SM2Util.decrypt(content);
            // 锁号大于16位
            String keyNum16 = key.substring(key.length() - 16);
            // 再使用SM2 进行对称解密
            decrypt = SM4Util.decryptECB(decrypt, keyNum16);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("CertUtil encrypt error: " + e.getMessage());
        }

        return decrypt;
    }

    public static void main(String[] args) {
        String content = "数字证书元数据加密算法测试数字证书元数据加密算法测试数字证书元数据加密算法测试数字证书元数据加密算法测试数字证书元数据加密算法测试数字证书元数据加密算法测试数字证书元数据加密算法测试数字证书元数据加密算法测试数字证书元数据加密算法测试数字证书元数据加密算法测试";
        System.out.println("数字证书Ukey序列号:5018202002010028");
        String a = CertUtil.encrypt(content, "50182020020100285018202002010028");
        System.out.println("密文:" + a);
        System.out.println("长度:" + a.length());
        /*String b = CertUtil.decrypt(a, "5018202002010028");
        System.out.println("原文:" + b);*/

//        System.out.println("数字证书Ukey序列号:5018202002010137");
//        String c = CertUtil.encrypt(content, "5018202002010137");
//        System.out.println("密文:" + c);
//        String d = CertUtil.decrypt(c, "5018202002010137");
//        System.out.println("原文:" + d);

    }

}
