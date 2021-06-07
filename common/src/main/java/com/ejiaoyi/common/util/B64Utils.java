package com.ejiaoyi.common.util;

import org.apache.commons.codec.binary.Base64;

/**
 * 通过Base64方式，对文本内容进行加密和解密
 * @author Make
 */
public class B64Utils {
    /**
     * 字符编码
     */
    public final static String ENCODING = "UTF-8";

    /**
     * Base64编码
     *
     * @param _data
     *            待编码数据
     * @return String 编码数据
     */
    public static String s_encode(byte[] _data ) {
        byte[] b =  Base64.encodeBase64(_data );
        return new String( b );
    }

    /**
     * Base64解码
     *
     * @param _data
     *            待解码数据
     * @return String 解码数据
     */
    public static byte[] s_decode( String _data ){
        return  Base64.decodeBase64( _data.getBytes() );
    }
}