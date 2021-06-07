package com.ejiaoyi.common.crypto;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECFieldElement.Fp;
import org.bouncycastle.math.ec.ECPoint;

import java.security.SecureRandom;

/**
 * SM2 主类
 *
 * @author Z0001
 * @since 2020-05-15
 */
public class SM2 {

    public static SM2 Instance() {
        return new SM2();
    }

    public final ECCurve ECC_CURVE;
    public final ECPoint ECC_POINT_G;
    public final ECDomainParameters ECC_BC_SPEC;
    public final ECKeyPairGenerator ECC_KEY_PAIR_GENERATOR;
    public final ECFieldElement ECC_GX_FIELD_ELEMENT;
    public final ECFieldElement ECC_GY_FIELD_ELEMENT;

    public SM2() {
        this.ECC_GX_FIELD_ELEMENT = new Fp(SM2Params.P, SM2Params.GX);
        this.ECC_GY_FIELD_ELEMENT = new Fp(SM2Params.P, SM2Params.GY);

        this.ECC_CURVE = new ECCurve.Fp(SM2Params.P, SM2Params.A, SM2Params.B);
        this.ECC_POINT_G = new ECPoint.Fp(this.ECC_CURVE, this.ECC_GX_FIELD_ELEMENT, this.ECC_GY_FIELD_ELEMENT);

        this.ECC_BC_SPEC = new ECDomainParameters(this.ECC_CURVE, this.ECC_POINT_G, SM2Params.N);

        ECKeyGenerationParameters ecKeyGenerationParameters;
        ecKeyGenerationParameters = new ECKeyGenerationParameters(this.ECC_BC_SPEC, new SecureRandom());

        this.ECC_KEY_PAIR_GENERATOR = new ECKeyPairGenerator();
        this.ECC_KEY_PAIR_GENERATOR.init(ecKeyGenerationParameters);
    }
}