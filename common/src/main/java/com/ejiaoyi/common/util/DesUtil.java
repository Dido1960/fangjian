package com.ejiaoyi.common.util;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;

/**
 * DES 加解密工具类
 *
 * @author Z0001
 * @since 2020-4-21
 */
@Slf4j
public class DesUtil {

    /**
     * 密钥因子 必须是8的倍数
     */
    private static final String DES_KEY = "3955C482C8C85B4CA8C9D8D4987F8B7F";
    private static final String DES = "DES";

    /**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "GITMRR1T";

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";

    /**
     * 加密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return 返回加密后的数据
     * @throws Exception 异常
     */
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);
        return cipher.doFinal(src);
    }

    /**
     * 解密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return 返回解密后的原始数据
     * @throws Exception 异常
     */
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);

        return cipher.doFinal(src);
    }

    /**
     * 加密 使用默认密钥
     *
     * @param src 数据源
     * @return 返回加密后的数据
     */
    public static String encrypt(String src) {
        Assert.notEmpty(src, "param src can not be empty");

        try {
            return byte2hex(encrypt(src.getBytes(), DES_KEY.getBytes()));
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * 解密 使用默认密钥
     *
     * @param src 数据源
     * @return 返回解密后的数据
     */
    public static String decrypt(String src) {
        Assert.notEmpty(src, "param src can not be empty");

        try {
            return new String(decrypt(hex2byte(src), DES_KEY.getBytes()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * 二进制转字符串
     *
     * @param bytes 二进制
     * @return 字符串
     */
    public static String byte2hex(byte[] bytes) {
        Assert.notNull(bytes, "param bytes can not be null");

        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (byte b : bytes) {
            stmp = (Integer.toHexString(b & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }

        return hs.toString().toUpperCase();
    }

    /**
     * 字符串转二进制
     *
     * @param str 字符串
     * @return 二进制
     */
    public static byte[] hex2byte(String str) {
        Assert.notEmpty(str, "param str can not be empty");

        byte[] bytes = str.getBytes();
        if ((bytes.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[bytes.length / 2];
        for (int n = 0; n < bytes.length; n += 2) {
            String item = new String(bytes, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * DES加密文件
     *
     * @param srcFile  待加密的文件
     * @param destFile 加密后存放的文件路径
     * @return 加密后的文件路径
     */
    public static String encryptFile(String password, String srcFile, String destFile) {

        if (password== null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(password), iv);
            InputStream is = new FileInputStream(srcFile);
            OutputStream out = new FileOutputStream(destFile);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }
            cis.close();
            is.close();
            out.close();
            return destFile;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * DES解密文件
     *
     * @param srcFile  已加密的文件
     * @param destFile 解密后存放的文件路径
     * @return 解密后的文件路径
     */
    public static String decryptFile(String password, String srcFile, String destFile) {
        if (password== null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        try {
            File file = new File(destFile);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generateKey(password), iv);
            InputStream is = new FileInputStream(srcFile);
            OutputStream out = new FileOutputStream(destFile);
            CipherOutputStream cos = new CipherOutputStream(out, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) >= 0) {
                cos.write(buffer, 0, r);
            }
            cos.close();
            is.close();
            out.close();
            return destFile;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }


}
