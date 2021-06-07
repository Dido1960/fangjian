package com.ejiaoyi.common.crypto;

/**
 * SM4 配置类
 *
 * @author Z0001
 * @since 2020-06-08
 */
public class SM4Context {

    public int mode;

    public long[] sk;

    public boolean padding;

    public SM4Context() {
        this.mode = 1;
        this.padding = true;
        this.sk = new long[32];
    }
}
