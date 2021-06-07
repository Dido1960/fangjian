package com.ejiaoyi.common.util.cfcaUtil;

/**
 * @Author qazhang
 * @Description 测试工具包，仅供测试使用
 * @CodeReviewer maben
 *
 */
public final class TextHexKit {

    static private byte[] hexNumberTable = new byte[256];
    static private byte[] lookUpHexAlphabet = new byte[16];
    static {
        for (int i = 0; i < hexNumberTable.length; i++) {
            hexNumberTable[i] = -1;
        }
        for (int i = '9'; i >= '0'; i--) {
            hexNumberTable[i] = (byte) (i - '0');
        }
        for (int i = 'F'; i >= 'A'; i--) {
            hexNumberTable[i] = (byte) (i - 'A' + 10);
        }
        for (int i = 'f'; i >= 'a'; i--) {
            hexNumberTable[i] = (byte) (i - 'a' + 10);
        }
        for (int i = 0; i < 10; i++) {
            lookUpHexAlphabet[i] = (byte) ('0' + i);
        }
        for (int i = 10; i <= 15; i++) {
            lookUpHexAlphabet[i] = (byte) ('a' + i - 10);
        }
    }

    /**
     * array of byte to encode
     *
     * @param binaryData
     * @return return encode binary array
     */
    static public String encode(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        }
        int lengthData = binaryData.length;
        int lengthEncode = lengthData * 2;
        byte[] encodedData = new byte[lengthEncode];
        for (int i = 0; i < lengthData; i++) {
            encodedData[i * 2] = lookUpHexAlphabet[(binaryData[i] >> 4) & 0xf];
            encodedData[i * 2 + 1] = lookUpHexAlphabet[binaryData[i] & 0xf];
        }

        return new String(encodedData);
    }

    static public byte[] decode(String hexEncoded) {
        if (hexEncoded == null) {
            return null;
        }
        byte[] binaryData = hexEncoded.getBytes();
        int lengthData = binaryData.length;
        if (lengthData % 2 != 0) {
            return null;
        }
        int lengthDecode = lengthData / 2;
        byte[] decodedData = new byte[lengthDecode];

        int I = 0;
        for (int i = 0; i < lengthDecode; i++) {
            I = i * 2;
            if (binaryData[I] == -1 || binaryData[I + 1] == -1) {
                return null;
            }
            decodedData[i] = (byte) ((hexNumberTable[binaryData[i * 2]] << 4) | hexNumberTable[binaryData[i * 2 + 1]]);
        }
        return decodedData;
    }

}
