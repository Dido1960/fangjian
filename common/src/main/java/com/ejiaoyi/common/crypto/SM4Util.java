package com.ejiaoyi.common.crypto;

import cn.hutool.core.util.HexUtil;

import java.nio.charset.StandardCharsets;

/**
 * SM4对称国密算法工具类
 *
 * @author Z0001
 * @since 2020-06-08
 */
public class SM4Util {

    private static final String DEFAULT_SECRET_KEY = "JeF8U9wHFOMfs2Y8";
    private static final String DEFAULT_IV = "UISwD9fW6cFh9SNS";

    public static String encryptECB(String plainText) {
        return SM4Util.encryptECB(plainText, DEFAULT_SECRET_KEY);
    }

    public static String decryptECB(String cipherText) {
        return SM4Util.decryptECB(cipherText, DEFAULT_SECRET_KEY);
    }

    public static String encryptECB(String plainText, String secretKey) {
        try {
            SM4Context ctx = new SM4Context();
            ctx.padding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

            SM4 sm4 = new SM4();
            sm4.SM4SetEncKey(ctx, keyBytes);
            byte[] encrypted = sm4.SM4CryptECB(ctx, plainText.getBytes(StandardCharsets.UTF_8));
            return HexUtil.encodeHexStr(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptECB(String cipherText, String secretKey) {
        try {
            SM4Context ctx = new SM4Context();
            ctx.padding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

            SM4 sm4 = new SM4();
            sm4.SM4SetDecKey(ctx, keyBytes);
            byte[] decrypted = sm4.SM4CryptECB(ctx, HexUtil.decodeHex(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptCBC(String plainText) {
        return SM4Util.encryptCBC(plainText, DEFAULT_SECRET_KEY, DEFAULT_IV);
    }

    public static String decryptCBC(String cipherText) {
        return SM4Util.decryptCBC(cipherText, DEFAULT_SECRET_KEY, DEFAULT_IV);
    }

    public static String encryptCBC(String plainText, String secretKey, String iv) {
        try {
            SM4Context ctx = new SM4Context();
            ctx.padding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);

            SM4 sm4 = new SM4();
            sm4.SM4SetEncKey(ctx, keyBytes);
            byte[] encrypted = sm4.SM4CryptCBC(ctx, ivBytes, plainText.getBytes(StandardCharsets.UTF_8));
            return HexUtil.encodeHexStr(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptCBC(String cipherText, String secretKey, String iv) {
        try {
            SM4Context ctx = new SM4Context();
            ctx.padding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);

            SM4 sm4 = new SM4();
            sm4.SM4SetDecKey(ctx, keyBytes);
            byte[] decrypted = sm4.SM4CryptCBC(ctx, ivBytes, HexUtil.decodeHex(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}