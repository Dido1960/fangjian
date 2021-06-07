package com.ejiaoyi.common.crypto;

import cn.hutool.core.util.HexUtil;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;
import java.math.BigInteger;

/**
 * SM2 工具类
 *
 * @author Z0001
 * @since 2020-05-15
 */
public class SM2Util {

    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "04af1fd9e53956f696f14dd0e8beadefe6bfaa04e5c871492689e5afa1b6293266c0ab54cdda3d8aacb3089c2fbec58af5bd52bc84ccef70dfcd12300f0a859842";

    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "00e21de8e9b08847d1282a50d1cdac31075d9fb5e78322d5405260e6d63934c5f4";


    /**
     * 生成随机秘钥对
     */
    public static void generateKeyPair() {
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = sm2.ECC_KEY_PAIR_GENERATOR.generateKeyPair();
        ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecPrivateKeyParameters.getD();
        ECPoint publicKey = ecPublicKeyParameters.getQ();

        System.out.println("公钥: " + HexUtil.encodeHexStr(publicKey.getEncoded()));
        System.out.println("私钥: " + HexUtil.encodeHexStr(privateKey.toByteArray()));
    }

    /**
     * 数据加密
     *
     * @param publicKey 公钥
     * @param data      加密数据
     * @return 密文
     * @throws IOException IO流异常
     */
    public static String encrypt(byte[] publicKey, byte[] data) throws IOException {
        if (publicKey == null || publicKey.length == 0) {
            return null;
        }

        if (data == null || data.length == 0) {
            return null;
        }

        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);

        SMCipher cipher = new SMCipher();
        SM2 sm2 = SM2.Instance();
        ECPoint userKey = sm2.ECC_CURVE.decodePoint(publicKey);

        ECPoint c1 = cipher.initEncrypt(sm2, userKey);
        cipher.encrypt(source);
        byte[] c3 = new byte[32];
        cipher.doFinal(c3);

        return HexUtil.encodeHexStr(c1.getEncoded()) + HexUtil.encodeHexStr(source) + HexUtil.encodeHexStr(c3);
    }

    /**
     * 数据解密
     *
     * @param privateKey    私钥
     * @param encryptedData 加密数据
     * @return 原文数据
     * @throws IOException IO流异常
     */
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) throws IOException {
        if (privateKey == null || privateKey.length == 0) {
            return null;
        }

        if (encryptedData == null || encryptedData.length == 0) {
            return null;
        }
        String data = HexUtil.encodeHexStr(encryptedData);
        byte[] c1Bytes = HexUtil.decodeHex(data.substring(0, 130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = HexUtil.decodeHex(data.substring(130, 130 + 2 * c2Len));
        byte[] c3 = HexUtil.decodeHex(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));

        SM2 sm2 = SM2.Instance();
        BigInteger userD = new BigInteger(1, privateKey);

        // 通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.ECC_CURVE.decodePoint(c1Bytes);
        SMCipher cipher = new SMCipher();
        cipher.initDecrypt(userD, c1);
        cipher.decrypt(c2);
        cipher.doFinal(c3);

        // 返回解密结果
        return c2;
    }

    /**
     * 解密
     *
     * @param encrypt 密文
     * @return 原文
     * @throws Exception 异常
     */
    public static String decrypt(String encrypt) throws Exception {
        return new String(SM2Util.decrypt(HexUtil.decodeHex(SM2Util.PRIVATE_KEY), HexUtil.decodeHex(encrypt)));
    }

    /**
     * 加密
     *
     * @param content 原文
     * @return 密文
     * @throws Exception 异常
     */
    public static String encrypt(String content) throws Exception {
        byte[] sourceData = content.getBytes();

        return SM2Util.encrypt(HexUtil.decodeHex(SM2Util.PUBLIC_KEY), sourceData);
    }

    public static void main(String[] args) throws Exception {
        String a = SM2Util.encrypt("QpkZ0Wjw");

        System.out.println("密文:" + a);

        String b = SM2Util.decrypt("040a430d5978414705900bd830154e32f6b7fdc583e27eec1d8c57189dd0665df98cde0f2f3a5470d2807b01778ee9645bd43ee1834c934dd83b05e90e853f8d99fdf093cab1e760ea24dc9f13b350911d5a71902b52165acac5d13beb57e2f2f1a65e730518e180a23249");

        System.out.println("原文:" + b);
    }
}