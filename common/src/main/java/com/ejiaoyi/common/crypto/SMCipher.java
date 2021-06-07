package com.ejiaoyi.common.crypto;

import com.ejiaoyi.common.util.ConvertUtil;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * SM 系列算法密码类
 *
 * @author Z0001
 * @since 2020-05-15
 */
public class SMCipher {
    private int ct;
    private ECPoint p2;
    private SM3Digest SM3KeyBase;
    private SM3Digest SM3C3;
    private final byte[] key;
    private byte keyOff;

    public SMCipher() {
        this.ct = 1;
        this.key = new byte[32];
        this.keyOff = 0;
    }

    private void reset() {
        this.SM3KeyBase = new SM3Digest();
        this.SM3C3 = new SM3Digest();

        byte[] p = ConvertUtil.byteConvert32Bytes(p2.getX().toBigInteger());
        this.SM3KeyBase.update(p, 0, p.length);
        this.SM3C3.update(p, 0, p.length);

        p = ConvertUtil.byteConvert32Bytes(p2.getY().toBigInteger());
        this.SM3KeyBase.update(p, 0, p.length);
        this.ct = 1;
        nextKey();
    }

    private void nextKey() {
        SM3Digest SM3CurKey = new SM3Digest(this.SM3KeyBase);
        SM3CurKey.update((byte) (ct >> 24 & 0xff));
        SM3CurKey.update((byte) (ct >> 16 & 0xff));
        SM3CurKey.update((byte) (ct >> 8 & 0xff));
        SM3CurKey.update((byte) (ct & 0xff));
        SM3CurKey.doFinal(key, 0);
        this.keyOff = 0;
        this.ct++;
    }

    public ECPoint initEncrypt(SM2 sm2, ECPoint userKey) {
        AsymmetricCipherKeyPair key = sm2.ECC_KEY_PAIR_GENERATOR.generateKeyPair();
        ECPrivateKeyParameters ecPrivateKeyParameters = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecPublicKeyParameters = (ECPublicKeyParameters) key.getPublic();
        BigInteger k = ecPrivateKeyParameters.getD();
        ECPoint c1 = ecPublicKeyParameters.getQ();
        this.p2 = userKey.multiply(k);
        reset();
        return c1;
    }

    public void encrypt(byte[] data) {
        this.SM3C3.update(data, 0, data.length);
        for (int i = 0; i < data.length; i++) {
            if (keyOff == key.length) {
                nextKey();
            }
            data[i] ^= key[keyOff++];
        }
    }

    public void initDecrypt(BigInteger userD, ECPoint c1) {
        this.p2 = c1.multiply(userD);
        reset();
    }

    public void decrypt(byte[] data) {
        for (int i = 0; i < data.length; i++) {
            if (keyOff == key.length) {
                nextKey();
            }
            data[i] ^= key[keyOff++];
        }

        this.SM3C3.update(data, 0, data.length);
    }

    public void doFinal(byte[] c3) {
        byte[] p = ConvertUtil.byteConvert32Bytes(p2.getY().toBigInteger());
        this.SM3C3.update(p, 0, p.length);
        this.SM3C3.doFinal(c3, 0);
        reset();
    }
}