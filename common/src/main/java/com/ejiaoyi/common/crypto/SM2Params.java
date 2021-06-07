package com.ejiaoyi.common.crypto;

import java.math.BigInteger;

/**
 * 国家密码管理局推荐的椭圆曲线相关参数
 *
 * @author Z0001
 * @since 2020-05-15
 */
public class SM2Params {

    public static final BigInteger P = new BigInteger("FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFF", 16);

    public static final BigInteger A = new BigInteger("FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFC", 16);

    public static final BigInteger B = new BigInteger("28E9FA9E" + "9D9F5E34" + "4D5A9E4B" + "CF6509A7" + "F39789F5" + "15AB8F92" + "DDBCBD41" + "4D940E93", 16);

    public static final BigInteger N = new BigInteger("FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "7203DF6B" + "21C6052B" + "53BBF409" + "39D54123", 16);

    public static final BigInteger GX = new BigInteger("32C4AE2C" + "1F198119" + "5F990446" + "6A39C994" + "8FE30BBF" + "F2660BE1" + "715A4589" + "334C74C7", 16);

    public static final BigInteger GY = new BigInteger("BC3736A2" + "F4F6779C" + "59BDCEE3" + "6B692153" + "D0A9877C" + "C62A4740" + "02DF32E5" + "2139F0A0", 16);
}